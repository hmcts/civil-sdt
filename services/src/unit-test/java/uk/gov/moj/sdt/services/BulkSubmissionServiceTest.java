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

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.dao.api.ITargetApplicationDao;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.ServiceRequest;
import uk.gov.moj.sdt.domain.ServiceRouting;
import uk.gov.moj.sdt.domain.ServiceType;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IIndividualRequest.IndividualRequestStatus;
import uk.gov.moj.sdt.domain.api.IServiceRequest;
import uk.gov.moj.sdt.domain.api.IServiceRouting;
import uk.gov.moj.sdt.domain.api.IServiceType;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.utils.IndividualRequestsXmlParser;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.Utilities;
import uk.gov.moj.sdt.utils.api.ISdtBulkReferenceGenerator;
import uk.gov.moj.sdt.utils.transaction.synchronizer.api.IMessageSynchronizer;

/**
 * Test class for BulkSubmissionService.
 * 
 * @author Manoj Kulkarni
 * 
 */
public class BulkSubmissionServiceTest
{
    /**
     * Logger for debugging.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (BulkSubmissionServiceTest.class);

    /**
     * Bulk Submission Service for testing.
     */
    private BulkSubmissionService bulkSubmissionService;

    /**
     * Generic dao.
     */
    private IGenericDao mockGenericDao;

    /**
     * Message writer for queueing messages to the messaging server.
     */
    // private IMessageWriter mockMessageWriter;

    /**
     * Individual requests xml parser for parsing the xml requests.
     */
    private IndividualRequestsXmlParser individualRequestsXmlParser;

    /**
     * Bulk Customer dao.
     */
    private IBulkCustomerDao mockBulkCustomerDao;

    /**
     * Target Application dao.
     */
    private ITargetApplicationDao mockTargetApplicationDao;

    /**
     * SDT Bulk Reference Generator.
     */
    private ISdtBulkReferenceGenerator mockSdtBulkReferenceGenerator;

    /**
     * Message Synchroniser reference.
     */
    private IMessageSynchronizer mockMessageSynchronizer;

    /**
     * Setup of the mock dao and injection of other objects.
     */
    @Before
    public void setUp ()
    {
        bulkSubmissionService = new BulkSubmissionService ();

        mockGenericDao = EasyMock.createMock (IGenericDao.class);
        bulkSubmissionService.setGenericDao (mockGenericDao);

        // This class cannot be easily mocked since it's within a Runnable block so it's been removed for clarity
        // mockMessageWriter = EasyMock.createMock (IMessageWriter.class);
        // bulkSubmissionService.setMessageWriter (mockMessageWriter);

        mockBulkCustomerDao = EasyMock.createMock (IBulkCustomerDao.class);
        bulkSubmissionService.setBulkCustomerDao (mockBulkCustomerDao);

        mockTargetApplicationDao = EasyMock.createMock (ITargetApplicationDao.class);
        bulkSubmissionService.setTargetApplicationDao (mockTargetApplicationDao);

        individualRequestsXmlParser = new IndividualRequestsXmlParser ();
        bulkSubmissionService.setIndividualRequestsXmlparser (individualRequestsXmlParser);

        mockSdtBulkReferenceGenerator = EasyMock.createMock (ISdtBulkReferenceGenerator.class);
        bulkSubmissionService.setSdtBulkReferenceGenerator (mockSdtBulkReferenceGenerator);

        mockMessageSynchronizer = EasyMock.createMock (IMessageSynchronizer.class);
        bulkSubmissionService.setMessageSynchronizer (mockMessageSynchronizer);
    }

    /**
     * Test method for the saving of bulk submission.
     * 
     * @throws IOException if there is any error in reading the file.
     */
    @Test
    public void saveBulkSubmission () throws IOException
    {
        SdtContext.getContext ().setRawInXml (this.getRawXml ("testXMLValid2.xml"));

        // Activate Mock Generic Dao
        final IBulkSubmission bulkSubmission = this.createBulkSubmission ();
        mockGenericDao.persist (bulkSubmission);
        EasyMock.expectLastCall ();

        // Mock the serviceRequest fetch
        final IServiceRequest serviceRequest = new ServiceRequest ();
        EasyMock.expect (mockGenericDao.fetch (IServiceRequest.class, 1)).andReturn (serviceRequest);

        // Replay the EasyMock
        EasyMock.replay (mockGenericDao);

        // Put a dummy value into the SdtContext
        SdtContext.getContext ().setServiceRequestId (new Long (1));

        // Call the bulk submission service
        bulkSubmissionService.saveBulkSubmission (bulkSubmission);

        LOGGER.debug ("bulkSubmission[" + bulkSubmission + "]");

        // Verify the Mock
        EasyMock.verify (mockGenericDao);

        Assert.assertTrue ("Expected to pass", true);

    }

    /**
     * This method tests bulk submission with multiple individual request containing
     * 2 valid and 1 invalid request.
     * 
     * @throws IOException if there is any issue
     */
    @Test
    public void testSubmissionWithMultipleRequests () throws IOException
    {
        final String rawXml = this.getRawXml ("testXMLValid3.xml");
        SdtContext.getContext ().setRawInXml (rawXml);

        // Activate Mock Generic Dao
        final IBulkSubmission bulkSubmission = this.createBulkSubmission ();
        addValidIndividualRequest (bulkSubmission, "ICustReq124");
        addValidIndividualRequest (bulkSubmission, "ICustReq125");

        LOGGER.debug ("Size of Individual Requests in Bulk Submission is " +
                bulkSubmission.getIndividualRequests ().size ());

        mockGenericDao.persist (bulkSubmission);
        EasyMock.expectLastCall ();

        // Mock the serviceRequest fetch
        final IServiceRequest serviceRequest = new ServiceRequest ();
        EasyMock.expect (mockGenericDao.fetch (IServiceRequest.class, 1)).andReturn (serviceRequest);

        // Replay the EasyMock
        EasyMock.replay (mockGenericDao);

        // Put a dummy value into the SdtContext
        SdtContext.getContext ().setServiceRequestId (new Long (1));

        // Call the bulk submission service
        bulkSubmissionService.saveBulkSubmission (bulkSubmission);

        // Verify the Mock
        EasyMock.verify (mockGenericDao);

        Assert.assertTrue ("Expected to pass", true);
    }

    /**
     * 
     * @return Bulk Submission object for the testing.
     */
    private IBulkSubmission createBulkSubmission ()
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

        bulkSubmission
                .setCompletedDate (LocalDateTime.fromDateFields (new java.util.Date (System.currentTimeMillis ())));
        bulkSubmission.setCreatedDate (LocalDateTime.fromDateFields (new java.util.Date (System.currentTimeMillis ())));
        bulkSubmission.setCustomerReference ("TEST_CUST_REF");
        bulkSubmission.setId (1L);
        bulkSubmission.setNumberOfRequest (2);
        bulkSubmission.setPayload ("TEST_XML");
        bulkSubmission.setSubmissionStatus ("SUBMITTED");
        bulkSubmission.setUpdatedDate (LocalDateTime.fromDateFields (new java.util.Date (System.currentTimeMillis ())));

        final IndividualRequest individualRequest = new IndividualRequest ();
        individualRequest.setCompletedDate (LocalDateTime.fromDateFields (new java.util.Date (System
                .currentTimeMillis ())));
        individualRequest
                .setCreatedDate (LocalDateTime.fromDateFields (new java.util.Date (System.currentTimeMillis ())));
        individualRequest.setCustomerRequestReference ("ICustReq123");
        individualRequest.setId (1L);
        individualRequest.setRequestStatus (IndividualRequestStatus.RECEIVED.getStatus ());

        bulkSubmission.addIndividualRequest (individualRequest);

        return bulkSubmission;
    }

    /**
     * 
     * @param customerReference the customer reference number
     * @param bulkSubmission bulk submission
     */
    private void addValidIndividualRequest (final IBulkSubmission bulkSubmission, final String customerReference)
    {
        final IndividualRequest individualRequest = new IndividualRequest ();
        individualRequest.setCompletedDate (LocalDateTime.fromDateFields (new java.util.Date (System
                .currentTimeMillis ())));
        individualRequest
                .setCreatedDate (LocalDateTime.fromDateFields (new java.util.Date (System.currentTimeMillis ())));
        individualRequest.setCustomerRequestReference (customerReference);
        individualRequest.setId (1L);
        individualRequest.setRequestStatus (IndividualRequestStatus.RECEIVED.getStatus ());

        bulkSubmission.addIndividualRequest (individualRequest);
    }

    /**
     * 
     * @return rax xml from a test file
     * @param fileName the name of the file to load
     * @throws IOException during the read operations
     */
    private String getRawXml (final String fileName) throws IOException
    {
        // Read the test xml file.
        File myFile;
        String message = "";

        // XPathHandler xmlHandler = new XPathHandler ();

        myFile = new File (Utilities.checkFileExists ("src/unit-test/resources/", fileName, false));

        message = FileUtils.readFileToString (myFile);

        return message;

    }

}
