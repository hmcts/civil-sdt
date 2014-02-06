/* Copyrights and Licenses
 * 
 * Copyright (c) 2012-2013 by the Ministry of Justice. All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 * - All advertising materials mentioning features or use of this software must display the
 * following acknowledgment: "This product includes Money Claims OnLine."
 * - Products derived from this software may not be called "Money Claims OnLine" nor may
 * "Money Claims OnLine" appear in their names without prior written permission of the
 * Ministry of Justice.
 * - Redistributions of any form whatsoever must retain the following acknowledgment: "This
 * product includes Money Claims OnLine."
 * This software is provided "as is" and any expressed or implied warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the Ministry of Justice or its contributors be liable for any
 * direct, indirect, incidental, special, exemplary, or consequential damages (including, but
 * not limited to, procurement of substitute goods or services; loss of use, data, or profits;
 * or business interruption). However caused any on any theory of liability, whether in contract,
 * strict liability, or tort (including negligence or otherwise) arising in any way out of the use of this
 * software, even if advised of the possibility of such damage.
 * 
 * $Id: $
 * $LastChangedRevision: $
 * $LastChangedDate: $
 * $LastChangedBy: $ */
package uk.gov.moj.sdt.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.dao.api.ITargetApplicationDao;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.IServiceRequest;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.services.api.IBulkSubmissionService;
import uk.gov.moj.sdt.services.utils.IndividualRequestsXmlParser;
import uk.gov.moj.sdt.services.utils.api.IMessagingUtility;
import uk.gov.moj.sdt.services.utils.api.ISdtBulkReferenceGenerator;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean;

/**
 * Implementation of the IBulkSubmissionService interface providing methods
 * to do the tasks related to bulk submission.
 * 
 * @author Manoj Kulkarni
 * 
 */
@Transactional (propagation = Propagation.SUPPORTS)
public class BulkSubmissionService implements IBulkSubmissionService
{
    /**
     * Logger for debugging.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (BulkSubmissionService.class);

    /**
     * Generic Dao property for doing the task of persisting the domain objects.
     */
    private IGenericDao genericDao;

    /**
     * Bulk Customer Dao property for looking up the bulk customer object.
     */
    private IBulkCustomerDao bulkCustomerDao;

    /**
     * Target Application Dao property for looking up the target application object.
     */
    private ITargetApplicationDao targetApplicationDao;

    /**
     * A parser to retrieve the raw XML for each request.
     */
    private IndividualRequestsXmlParser individualRequestsXmlParser;

    /**
     * Messaging utility for queueing messages in the messaging server.
     */
    private IMessagingUtility messagingUtility;

    /**
     * SDT Bulk reference generator.
     */
    private ISdtBulkReferenceGenerator sdtBulkReferenceGenerator;

    @Override
    @Transactional (propagation = Propagation.REQUIRED)
    public void saveBulkSubmission (final IBulkSubmission bulkSubmission)
    {
        enrich (bulkSubmission);

        enrich (bulkSubmission.getIndividualRequests ());

        // Now persist the bulk submissions.
        getGenericDao ().persist (bulkSubmission);

        enqueueValidRequests (bulkSubmission.getIndividualRequests ());

    }

    /**
     * Queues the valid individual requests on the messaging server.
     * 
     * @param individualRequests the individual requests for a bulk submission.
     */
    private void enqueueValidRequests (final List<IIndividualRequest> individualRequests)
    {
        LOGGER.debug ("Enqueue " + individualRequests.size () + " requests");

        for (final IIndividualRequest iRequest : individualRequests)
        {
            if (iRequest.isEnqueueable ())
            {
                this.getMessagingUtility ().enqueueRequest (iRequest);
            }
        }
    }

    /**
     * Prepare individual requests for persistence.
     * 
     * @param individualRequests collection of individual requests
     */
    private void enrich (final List<IIndividualRequest> individualRequests)
    {
        for (IIndividualRequest iRequest : individualRequests)
        {
            iRequest.populateReferences ();
        }

        // Populate the individual requests with the raw xml specific for that request.
        individualRequestsXmlParser.populateRawRequest (individualRequests);
    }

    /**
     * Enrich bulk submission instance to prepare for persistence.
     * 
     * @param bulkSubmission bulk submission
     */
    private void enrich (final IBulkSubmission bulkSubmission)
    {
        LOGGER.debug ("Enrich bulk submission instance to prepare for persistence");

        // Get the Raw XML from the ThreadLocal and insert in the BulkSubmission
        bulkSubmission.setPayload (SdtContext.getContext ().getRawInXml ());

        // Get the Bulk Customer from the customer dao for the SDT customer Id
        final IBulkCustomer bulkCustomer =
                this.getBulkCustomerDao ().getBulkCustomerBySdtId (
                        bulkSubmission.getBulkCustomer ().getSdtCustomerId ());

        bulkSubmission.setBulkCustomer (bulkCustomer);

        // Set the SDT Bulk Reference
        bulkSubmission.setSdtBulkReference (sdtBulkReferenceGenerator.getSdtBulkReference (bulkSubmission
                .getTargetApplication ().getTargetApplicationCode ()));

        // Store this in the context for the sake of the outbound interceptor.
        SdtContext.getContext ().setSubmitBulkReference (bulkSubmission.getSdtBulkReference ());

        // Update last seen bulk reference.
        SdtMetricsMBean.getMetrics ().setLastBulkSubmitRef (bulkSubmission.getSdtBulkReference ());

        // Get the Target Application from the target application dao
        final ITargetApplication targetApplication =
                this.getTargetApplicationDao ().getTargetApplicationByCode (
                        bulkSubmission.getTargetApplication ().getTargetApplicationCode ());
        if (targetApplication != null)
        {
            LOGGER.debug ("Target Application found " + targetApplication.getId ());
        }

        bulkSubmission.setTargetApplication (targetApplication);

        // Mark the request as Validated.
        bulkSubmission.markAsValidated ();

        // Associate with the service request created by the ServiceRequestInboundInterceptor
        final IServiceRequest serviceRequest =
                genericDao.fetch (IServiceRequest.class, SdtContext.getContext ().getServiceRequestId ());
        bulkSubmission.setServiceRequest (serviceRequest);
    }

    /**
     * 
     * @return the Generic Dao
     */
    public IGenericDao getGenericDao ()
    {
        return genericDao;
    }

    /**
     * 
     * @param genericDao the GenericDao implementation
     */
    public void setGenericDao (final IGenericDao genericDao)
    {
        this.genericDao = genericDao;
    }

    /**
     * Get the bulk customer DAO bean.
     * 
     * @return the Bulk Customer DAO.
     */
    public IBulkCustomerDao getBulkCustomerDao ()
    {
        return bulkCustomerDao;
    }

    /**
     * Sets the Bulk Customer DAO object.
     * 
     * @param bulkCustomerDao the Bulk Customer Dao.
     */
    public void setBulkCustomerDao (final IBulkCustomerDao bulkCustomerDao)
    {
        this.bulkCustomerDao = bulkCustomerDao;
    }

    /**
     * Get the Target Application Dao bean.
     * 
     * @return the target application Dao.
     */
    public ITargetApplicationDao getTargetApplicationDao ()
    {
        return targetApplicationDao;
    }

    /**
     * Sets the Target Application DAO.
     * 
     * @param targetApplicationDao the target application DAO
     */
    public void setTargetApplicationDao (final ITargetApplicationDao targetApplicationDao)
    {
        this.targetApplicationDao = targetApplicationDao;
    }

    /**
     * Get the individualRequestsXmlParser.
     * 
     * @return the individualRequestsXmlParser.
     */
    public IndividualRequestsXmlParser getIndividualRequestsXmlParser ()
    {
        return individualRequestsXmlParser;
    }

    /**
     * Set the individualRequestsXmlParser.
     * 
     * @param individualRequestsXmlParser the individualRequestsXmlParser.
     */
    public void setIndividualRequestsXmlparser (final IndividualRequestsXmlParser individualRequestsXmlParser)
    {
        this.individualRequestsXmlParser = individualRequestsXmlParser;
    }

    /**
     * Set the sdtBulkReferenceGenerator.
     * 
     * @param sdtBulkReferenceGenerator SDT Bulk Reference Generator
     */
    public void setSdtBulkReferenceGenerator (final ISdtBulkReferenceGenerator sdtBulkReferenceGenerator)
    {
        this.sdtBulkReferenceGenerator = sdtBulkReferenceGenerator;
    }

    /**
     * Get the messaging utility class reference.
     * 
     * @return the messaging utility
     */
    public IMessagingUtility getMessagingUtility ()
    {
        return messagingUtility;
    }

    /**
     * 
     * @param messagingUtility the messaging utility class.
     */
    public void setMessagingUtility (final IMessagingUtility messagingUtility)
    {
        this.messagingUtility = messagingUtility;
    }
}
