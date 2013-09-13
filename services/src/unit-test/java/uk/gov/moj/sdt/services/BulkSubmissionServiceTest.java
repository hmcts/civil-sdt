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
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.RequestRouting;
import uk.gov.moj.sdt.domain.RequestType;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IRequestType;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.messaging.api.IMessageWriter;
import uk.gov.moj.sdt.misc.IndividualRequestStatus;

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
    private IMessageWriter mockMessageWriter;

    /**
     * Setup of the mock dao and injection of other objects.
     */
    @Before
    public void setUp ()
    {
        bulkSubmissionService = new BulkSubmissionService ();

        mockGenericDao = EasyMock.createMock (IGenericDao.class);
        bulkSubmissionService.setGenericDao (mockGenericDao);

        mockMessageWriter = EasyMock.createMock (IMessageWriter.class);
        bulkSubmissionService.setMessageWriter (mockMessageWriter);
    }

    /**
     * Test method for the saving of bulk submission.
     */
    @Test
    public void saveBulkSubmission ()
    {
        // Activate Mock Generic Dao
        final IBulkSubmission bulkSubmission = this.createBulkSubmission ();
        mockGenericDao.persist (bulkSubmission);

        EasyMock.expectLastCall ();

        final List<IndividualRequest> individualRequests = bulkSubmission.getIndividualRequests ();
        for (IndividualRequest request : individualRequests)
        {
            mockGenericDao.persist (request);
        }

        EasyMock.expectLastCall ().once ();

        for (IndividualRequest request : individualRequests)
        {
            mockMessageWriter.queueMessage (request.getSdtRequestReference ());
        }

        EasyMock.expectLastCall ().anyTimes ();

        // Replay the EasyMock
        EasyMock.replay (mockGenericDao);
        EasyMock.replay (mockMessageWriter);

        // Call the bulk submission service
        bulkSubmissionService.saveBulkSubmission (bulkSubmission);

        Assert.assertTrue ("Expected to pass", true);

        // Verify the Mock
        EasyMock.verify (mockGenericDao);
        EasyMock.verify (mockMessageWriter);

    }

    /**
     * This method tests bulk submission with multiple individual request containing
     * 2 valid and 1 invalid request.
     */
    @Test
    public void testSubmissionWithMultipleRequests ()
    {
        // Activate Mock Generic Dao
        final IBulkSubmission bulkSubmission = this.createBulkSubmission ();
        final List<IndividualRequest> requests = bulkSubmission.getIndividualRequests ();
        final IndividualRequest iRequest1 = this.getValidIndividualRequest ("SDT_test_2", "ICustReq124");
        final IndividualRequest iRequest2 = this.getValidIndividualRequest ("SDT test 3", "ICustReq125");

        LOGGER.debug ("Size of Individual Requests is " + requests.size ());
        requests.add (iRequest1);
        requests.add (iRequest2);
        LOGGER.debug ("After adding requests, Size of Individual Requests is " + requests.size ());

        bulkSubmission.setIndividualRequests (requests);

        LOGGER.debug ("Size of Individual Requests in Bulk Submission is " +
                bulkSubmission.getIndividualRequests ().size ());

        mockGenericDao.persist (bulkSubmission);

        EasyMock.expectLastCall ();

        final List<IndividualRequest> individualRequests = bulkSubmission.getIndividualRequests ();
        LOGGER.debug ("Individal Requests fetched number is " + individualRequests.size ());
        for (IndividualRequest request : individualRequests)
        {
            LOGGER.debug ("Persisting Individal Request - " + request.getSdtRequestReference ());
            mockGenericDao.persist (request);
            EasyMock.expectLastCall ();
        }

        for (IndividualRequest request : individualRequests)
        {
            if (request.getRequestStatus ().equals (IndividualRequestStatus.RECEIVED.getStatus ()))
            {
                mockMessageWriter.queueMessage (request.getSdtRequestReference ());
                EasyMock.expectLastCall ();
            }
        }

        // Replay the EasyMock
        EasyMock.replay (mockGenericDao);
        EasyMock.replay (mockMessageWriter);

        // Call the bulk submission service
        bulkSubmissionService.saveBulkSubmission (bulkSubmission);

        Assert.assertTrue ("Expected to pass", true);

        // Verify the Mock
        EasyMock.verify (mockGenericDao);
        EasyMock.verify (mockMessageWriter);
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
        targetApp.setTargetApplicationCode ("TEST_CODE");
        targetApp.setTargetApplicationName ("TEST_TargetApp");
        final Set<RequestRouting> requestRoutings = new HashSet<RequestRouting> ();

        final RequestRouting requestRouting = new RequestRouting ();
        requestRouting.setId (1L);
        requestRouting.setWebServiceEndpoint ("MCOL_END_POINT");

        final IRequestType requestType = new RequestType ();
        requestType.setId (1L);
        requestType.setName ("RequestTest1");
        requestType.setDescription ("RequestTestDesc1");
        requestType.setStatus ("RequestTestStatus");

        requestRouting.setRequestType (requestType);

        requestRoutings.add (requestRouting);

        targetApp.setRequestRoutings (requestRoutings);

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
        bulkSubmission.setSdtBulkReference ("SDT_BULKREF_0001");
        bulkSubmission.setSubmissionStatus ("SUBMITTED");
        bulkSubmission.setUpdatedDate (LocalDateTime.fromDateFields (new java.util.Date (System.currentTimeMillis ())));

        final List<IndividualRequest> individualRequests = new ArrayList<IndividualRequest> ();
        final IndividualRequest individualRequest = new IndividualRequest ();
        individualRequest.setCompletedDate (LocalDateTime.fromDateFields (new java.util.Date (System
                .currentTimeMillis ())));
        individualRequest
                .setCreatedDate (LocalDateTime.fromDateFields (new java.util.Date (System.currentTimeMillis ())));
        individualRequest.setCustomerRequestReference ("ICustReq123");
        individualRequest.setSdtRequestReference ("SDT_test_1");
        individualRequest.setId (1L);
        individualRequest.setPayload ("IXML1");
        individualRequest.setRequestStatus ("Accepted");
        individualRequests.add (individualRequest);

        bulkSubmission.setIndividualRequests (individualRequests);

        return bulkSubmission;
    }

    /**
     * 
     * @param sdtReference the unique sdt reference number
     * @param customerReference the customer reference number
     * @return a valid individual request object
     */
    private IndividualRequest getValidIndividualRequest (final String sdtReference, final String customerReference)
    {
        final IndividualRequest individualRequest = new IndividualRequest ();
        individualRequest.setCompletedDate (LocalDateTime.fromDateFields (new java.util.Date (System
                .currentTimeMillis ())));
        individualRequest
                .setCreatedDate (LocalDateTime.fromDateFields (new java.util.Date (System.currentTimeMillis ())));
        individualRequest.setCustomerRequestReference (customerReference);
        individualRequest.setSdtRequestReference (sdtReference);
        individualRequest.setId (1L);
        individualRequest.setPayload ("IXML1");
        individualRequest.setRequestStatus ("Received");

        return individualRequest;
    }

}
