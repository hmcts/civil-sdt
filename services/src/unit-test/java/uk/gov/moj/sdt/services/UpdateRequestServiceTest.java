/* Copyrights and Licenses
 * 
 * Copyright (c) 2013 by the Ministry of Justice. All rights reserved.
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.hibernate.criterion.Criterion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.ServiceRouting;
import uk.gov.moj.sdt.domain.ServiceType;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.IServiceRouting;
import uk.gov.moj.sdt.domain.api.IServiceType;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.utils.GenericXmlParser;
import uk.gov.moj.sdt.utils.SdtContext;

/**
 * Test class for Update Request Service.
 * 
 * @author Manoj Kulkarni
 * 
 */
public class UpdateRequestServiceTest
{
    /**
     * Logger for debugging.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (UpdateRequestServiceTest.class);

    /**
     * Mocked Individual Request Dao object.
     */
    private IIndividualRequestDao mockIndividualRequestDao;

    /**
     * Update Request Service object.
     */
    private UpdateRequestService updateRequestService;

    /**
     * Method to do any pre-test set-up.
     */
    @Before
    public void setUp ()
    {
        updateRequestService = new UpdateRequestService ();

        // Instantiate all the mocked objects and set them in the target application submission service
        mockIndividualRequestDao = EasyMock.createMock (IIndividualRequestDao.class);
        updateRequestService.setIndividualRequestDao (mockIndividualRequestDao);

        final GenericXmlParser genericParser = new GenericXmlParser ();
        genericParser.setEnclosingTag ("targetAppDetail");
        updateRequestService.setIndividualResponseXmlParser (genericParser);
    }

    /**
     * Method to test all successful scenario for the update individual request method.
     */
    @Test
    public void updateIndividualRequestRejected ()
    {
        final IIndividualRequest individualRequestParam = this.getRejectedIndividualRequestFromTargetApp ();

        final IIndividualRequest individualRequest = new IndividualRequest ();
        individualRequest.setSdtRequestReference (individualRequestParam.getSdtRequestReference ());
        this.setUpIndividualRequest (individualRequest);

        EasyMock.expect (
                this.mockIndividualRequestDao.getRequestBySdtReference (individualRequestParam
                        .getSdtRequestReference ())).andReturn (individualRequest);

        individualRequest.setRequestStatus (individualRequestParam.getRequestStatus ());
        individualRequest.setErrorLog (individualRequestParam.getErrorLog ());

        this.mockIndividualRequestDao.persist (individualRequest);
        EasyMock.expectLastCall ();

        final String[] completeRequestStatus =
                new String[] {IIndividualRequest.IndividualRequestStatus.ACCEPTED.getStatus (),
                        IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus ()};

        final IBulkSubmission bulkSubmission = individualRequest.getBulkSubmission ();

        EasyMock.expect (
                this.mockIndividualRequestDao.queryAsList (EasyMock.same (IIndividualRequest.class),
                        EasyMock.isA (Criterion.class), EasyMock.isA (Criterion.class))).andReturn (null);

        bulkSubmission.markAsCompleted ();

        mockIndividualRequestDao.persist (bulkSubmission);
        EasyMock.expectLastCall ();

        EasyMock.replay (mockIndividualRequestDao);

        // Setup dummy target response
        SdtContext.getContext ().setRawInXml ("response");

        this.updateRequestService.updateIndividualRequest (individualRequestParam);
        EasyMock.verify (mockIndividualRequestDao);
        Assert.assertTrue ("Expected to pass", true);

    }

    /**
     * Method to test all successful scenario for the update individual request method.
     */
    @Test
    public void updateIndividualRequestAccepted ()
    {
        final IIndividualRequest individualRequestParam = this.getAcceptedIndividualRequestFromTargetApp ();

        final IIndividualRequest individualRequest = new IndividualRequest ();
        individualRequest.setSdtRequestReference (individualRequestParam.getSdtRequestReference ());
        this.setUpIndividualRequest (individualRequest);

        EasyMock.expect (
                this.mockIndividualRequestDao.getRequestBySdtReference (individualRequestParam
                        .getSdtRequestReference ())).andReturn (individualRequest);

        individualRequest.setRequestStatus (individualRequestParam.getRequestStatus ());

        this.mockIndividualRequestDao.persist (individualRequest);
        EasyMock.expectLastCall ();

        final String[] completeRequestStatus =
                new String[] {IIndividualRequest.IndividualRequestStatus.ACCEPTED.getStatus (),
                        IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus ()};

        final IBulkSubmission bulkSubmission = individualRequest.getBulkSubmission ();

        EasyMock.expect (
                this.mockIndividualRequestDao.queryAsList (EasyMock.same (IIndividualRequest.class),
                        EasyMock.isA (Criterion.class), EasyMock.isA (Criterion.class))).andReturn (null);

        bulkSubmission.markAsCompleted ();

        mockIndividualRequestDao.persist (bulkSubmission);
        EasyMock.expectLastCall ();

        EasyMock.replay (mockIndividualRequestDao);

        // Setup dummy target response
        SdtContext.getContext ().setRawInXml ("response");

        this.updateRequestService.updateIndividualRequest (individualRequestParam);
        EasyMock.verify (mockIndividualRequestDao);
        Assert.assertTrue ("Expected to pass", true);

    }

    /**
     * This method is to test the scenario that the individual request from the
     * target app is not found in the database.
     */
    @Test
    public void updateIndividualRequestFail ()
    {
        final IIndividualRequest individualRequestParam = this.getAcceptedIndividualRequestFromTargetApp ();
        individualRequestParam.setSdtRequestReference ("TEST");

        EasyMock.expect (
                this.mockIndividualRequestDao.getRequestBySdtReference (individualRequestParam
                        .getSdtRequestReference ())).andReturn (null);

        EasyMock.replay (mockIndividualRequestDao);

        this.updateRequestService.updateIndividualRequest (individualRequestParam);
        EasyMock.verify (mockIndividualRequestDao);
        Assert.assertTrue ("Expected to pass", true);

    }

    /**
     * Method to test all successful scenario for the update individual request method
     * where the bulk submission still has some requests pending to process.
     */
    @Test
    public void updateIndividualRequestBulkRequestPending ()
    {
        final IIndividualRequest individualRequestParam = this.getAcceptedIndividualRequestFromTargetApp ();

        final IIndividualRequest individualRequest = new IndividualRequest ();
        individualRequest.setSdtRequestReference (individualRequestParam.getSdtRequestReference ());
        this.setUpIndividualRequest (individualRequest);

        EasyMock.expect (
                this.mockIndividualRequestDao.getRequestBySdtReference (individualRequestParam
                        .getSdtRequestReference ())).andReturn (individualRequest);

        individualRequest.setRequestStatus (individualRequestParam.getRequestStatus ());

        this.mockIndividualRequestDao.persist (individualRequest);
        EasyMock.expectLastCall ();

        final String[] completeRequestStatus =
                new String[] {IIndividualRequest.IndividualRequestStatus.ACCEPTED.getStatus (),
                        IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus ()};

        final IBulkSubmission bulkSubmission = individualRequest.getBulkSubmission ();

        final List<IIndividualRequest> indRequests = new ArrayList<IIndividualRequest> ();
        indRequests.add (individualRequest);

        EasyMock.expect (
                this.mockIndividualRequestDao.queryAsList (EasyMock.same (IIndividualRequest.class),
                        EasyMock.isA (Criterion.class), EasyMock.isA (Criterion.class))).andReturn (indRequests);

        EasyMock.replay (mockIndividualRequestDao);

        // Setup dummy target response
        SdtContext.getContext ().setRawInXml ("response");

        this.updateRequestService.updateIndividualRequest (individualRequestParam);
        EasyMock.verify (mockIndividualRequestDao);
        Assert.assertTrue ("Expected to pass", true);

    }

    /**
     * 
     * @return the individual request as obtained from the update item transformer
     */
    private IIndividualRequest getRejectedIndividualRequestFromTargetApp ()
    {
        final IIndividualRequest domainObject = new IndividualRequest ();
        domainObject.setSdtRequestReference ("MCOL_IREQ_0001");

        final IErrorLog errorLog = new ErrorLog ("INVALID_NAME", "Invalid name");
        domainObject.markRequestAsRejected (errorLog);

        return domainObject;
    }

    /**
     * 
     * @return the individual request as obtained from the update item transformer
     */
    private IIndividualRequest getAcceptedIndividualRequestFromTargetApp ()
    {
        final IIndividualRequest domainObject = new IndividualRequest ();
        domainObject.setSdtRequestReference ("MCOL_IREQ_0001");

        domainObject.markRequestAsAccepted ();

        return domainObject;
    }

    /**
     * Set up a valid individual request object.
     * 
     * @param request the individual request
     */
    private void setUpIndividualRequest (final IIndividualRequest request)
    {
        final IBulkSubmission bulkSubmission = new BulkSubmission ();
        final IBulkCustomer bulkCustomer = new BulkCustomer ();
        final ITargetApplication targetApp = new TargetApplication ();

        targetApp.setId (1L);
        targetApp.setTargetApplicationCode ("mcol");
        targetApp.setTargetApplicationName ("TEST_TargetApp");
        final Set<IServiceRouting> serviceRoutings = new HashSet<IServiceRouting> ();

        final ServiceRouting serviceRouting = new ServiceRouting ();
        serviceRouting.setId (1L);
        serviceRouting.setWebServiceEndpoint ("MCOL_END_POINT");

        final IServiceType serviceType = new ServiceType ();
        serviceType.setId (1L);
        serviceType.setName ("RequestTest1");
        serviceType.setDescription ("RequestTestDesc1");
        serviceType.setStatus ("RequestTestStatus");

        serviceRouting.setServiceType (serviceType);

        serviceRoutings.add (serviceRouting);

        targetApp.setServiceRoutings (serviceRoutings);

        bulkSubmission.setTargetApplication (targetApp);

        bulkCustomer.setId (1L);
        bulkCustomer.setSdtCustomerId (10L);

        bulkSubmission.setBulkCustomer (bulkCustomer);
        bulkSubmission.setCustomerReference ("TEST_CUST_REF");
        bulkSubmission.setId (1L);
        bulkSubmission.setNumberOfRequest (1);
        final List<IIndividualRequest> requests = new ArrayList<IIndividualRequest> ();
        requests.add (request);

        bulkSubmission.setIndividualRequests (requests);

        request.setBulkSubmission (bulkSubmission);
    }

}
