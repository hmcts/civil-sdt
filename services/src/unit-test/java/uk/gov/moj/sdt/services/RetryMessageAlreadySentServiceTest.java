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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.GlobalParameter;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.ServiceRouting;
import uk.gov.moj.sdt.domain.ServiceType;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.IServiceRouting;
import uk.gov.moj.sdt.domain.api.IServiceType;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.services.utils.api.IMessagingUtility;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

/**
 * Test class for the RetryMessageAlreadySentService.
 *
 * @author Manoj Kulkarni
 */
public class RetryMessageAlreadySentServiceTest extends AbstractSdtUnitTestBase {
    /**
     * Mocked Individual Request Dao object.
     */
    private IIndividualRequestDao mockIndividualRequestDao;

    /**
     * The mocked ICacheable reference to the global parameters cache.
     */
    private ICacheable mockCacheable;

    /**
     * Mocked messaging utility reference.
     */
    private IMessagingUtility mockMessagingUtility;

    /**
     * RetryMessageAlreadySentService instance to be tested.
     */
    private RetryMessageAlreadySentService messageTaskService;

    /**
     * Method to do any pre-test set-up.
     */
    @Before
    public void setUp() {
        messageTaskService = new RetryMessageAlreadySentService();

        // Instantiate all the mocked objects and set them in the message task service
        mockIndividualRequestDao = EasyMock.createMock(IIndividualRequestDao.class);
        messageTaskService.setIndividualRequestDao(mockIndividualRequestDao);

        mockCacheable = EasyMock.createMock(ICacheable.class);
        messageTaskService.setGlobalParametersCache(mockCacheable);

        mockMessagingUtility = EasyMock.createMock(IMessagingUtility.class);
        messageTaskService.setMessagingUtility(mockMessagingUtility);

    }

    /**
     * This method tests the all successful scenario of the queue pending message
     * when the forwarding attempts of individual request is same as the max forwarding attempts.
     */
    @Test
    public void queuePendingMessageMaxForwardingAttempts() {
        final String sdtRequestRef = "TEST_1";
        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference(sdtRequestRef);
        setUpIndividualRequest(individualRequest);
        individualRequest.setRequestStatus("Forwarded");
        individualRequest.setForwardingAttempts(3);

        final List<IIndividualRequest> individualRequests = new ArrayList<IIndividualRequest>();
        individualRequests.add(individualRequest);

        final IGlobalParameter maxForwardingAttemptsParam = new GlobalParameter();
        maxForwardingAttemptsParam.setName("MAX_FORWARDING_ATTEMPTS");
        maxForwardingAttemptsParam.setValue("3");
        EasyMock.expect(this.mockCacheable.getValue(IGlobalParameter.class, "MAX_FORWARDING_ATTEMPTS")).andReturn(
                maxForwardingAttemptsParam);

        final int maxForwardingAttempts = Integer.valueOf(maxForwardingAttemptsParam.getValue());

        EasyMock.expect(mockIndividualRequestDao.getPendingIndividualRequests(maxForwardingAttempts)).andReturn(
                individualRequests);

        for (IIndividualRequest individualRequestObj : individualRequests) {
            mockMessagingUtility.enqueueRequest(individualRequestObj);
            EasyMock.expectLastCall();

            // Re-set the forwarding attempts on the individual request.
            individualRequestObj.resetForwardingAttempts();
        }

        mockIndividualRequestDao.persistBulk(individualRequests);
        EasyMock.expectLastCall();

        EasyMock.replay(mockIndividualRequestDao);
        EasyMock.replay(mockCacheable);
        EasyMock.replay(mockMessagingUtility);

        this.messageTaskService.queueMessages();

        EasyMock.verify(mockIndividualRequestDao);
        EasyMock.verify(mockCacheable);
        EasyMock.verify(mockMessagingUtility);

        Assert.assertEquals("Forwarding attempts on individual request", 0, individualRequests.get(0)
                .getForwardingAttempts());
        Assert.assertEquals("Status set to Receieved", IIndividualRequest.IndividualRequestStatus.RECEIVED
                .getStatus(), individualRequests.get(0).getRequestStatus());

        Assert.assertTrue("Test completed succesfully", true);

    }

    /**
     * This method tests the all successful scenario of the queue pending message
     * when the forwarding attempts of individual request has exceeded the max forwarding attempts.
     */
    @Test
    public void queuePendingMessageExceededForwardingAttempts() {
        final String sdtRequestRef = "TEST_1";
        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference(sdtRequestRef);
        setUpIndividualRequest(individualRequest);
        individualRequest.setRequestStatus("Forwarded");
        individualRequest.setForwardingAttempts(5);

        final List<IIndividualRequest> individualRequests = new ArrayList<IIndividualRequest>();
        individualRequests.add(individualRequest);

        final IGlobalParameter maxForwardingAttemptsParam = new GlobalParameter();
        maxForwardingAttemptsParam.setName("MAX_FORWARDING_ATTEMPTS");
        maxForwardingAttemptsParam.setValue("3");
        EasyMock.expect(this.mockCacheable.getValue(IGlobalParameter.class, "MAX_FORWARDING_ATTEMPTS")).andReturn(
                maxForwardingAttemptsParam);

        final int maxForwardingAttempts = Integer.valueOf(maxForwardingAttemptsParam.getValue());

        EasyMock.expect(mockIndividualRequestDao.getPendingIndividualRequests(maxForwardingAttempts)).andReturn(
                individualRequests);

        for (IIndividualRequest individualRequestObj : individualRequests) {

            mockMessagingUtility.enqueueRequest(individualRequestObj);
            EasyMock.expectLastCall();

            // Re-set the forwarding attempts on the individual request.
            individualRequestObj.resetForwardingAttempts();
        }

        mockIndividualRequestDao.persistBulk(individualRequests);
        EasyMock.expectLastCall();

        EasyMock.replay(mockIndividualRequestDao);
        EasyMock.replay(mockCacheable);
        EasyMock.replay(mockMessagingUtility);

        this.messageTaskService.queueMessages();

        EasyMock.verify(mockIndividualRequestDao);
        EasyMock.verify(mockCacheable);
        EasyMock.verify(mockMessagingUtility);

        Assert.assertEquals("Forwarding attempts on individual request", 0, individualRequests.get(0)
                .getForwardingAttempts());
        Assert.assertEquals("Status set to Receieved", IIndividualRequest.IndividualRequestStatus.RECEIVED
                .getStatus(), individualRequests.get(0).getRequestStatus());

        Assert.assertTrue("Test completed succesfully", true);

    }

    /**
     * This method tests the all successful scenario of the queue pending message
     * when the forwarding attempts of individual request has exceeded the max forwarding attempts.
     * The method also tests for scenario where the request marked with dead letter
     * flag is ignored and not picked up by the service method.
     */
    @Test
    public void queuePendingDLQMessageExceededForwardingAttempts() {
        final String sdtRequestRef = "TEST_1";
        final String dlqSdtRequestRef = "TEST_2";
        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference(sdtRequestRef);
        setUpIndividualRequest(individualRequest);
        individualRequest.setRequestStatus("Forwarded");
        individualRequest.setForwardingAttempts(5);

        final IIndividualRequest dlqIndividualRequest = new IndividualRequest();
        dlqIndividualRequest.setSdtRequestReference(dlqSdtRequestRef);
        setUpIndividualRequest(dlqIndividualRequest);
        dlqIndividualRequest.setRequestStatus("Forwarded");
        dlqIndividualRequest.setForwardingAttempts(5);
        dlqIndividualRequest.setDeadLetter(true);

        final List<IIndividualRequest> individualRequests = new ArrayList<IIndividualRequest>();
        individualRequests.add(individualRequest);
        individualRequests.add(dlqIndividualRequest);

        final IGlobalParameter maxForwardingAttemptsParam = new GlobalParameter();
        maxForwardingAttemptsParam.setName("MAX_FORWARDING_ATTEMPTS");
        maxForwardingAttemptsParam.setValue("3");
        EasyMock.expect(this.mockCacheable.getValue(IGlobalParameter.class, "MAX_FORWARDING_ATTEMPTS")).andReturn(
                maxForwardingAttemptsParam);

        final int maxForwardingAttempts = Integer.valueOf(maxForwardingAttemptsParam.getValue());

        final List<IIndividualRequest> returnedListOfRequests = new ArrayList<IIndividualRequest>();
        returnedListOfRequests.add(individualRequest);

        EasyMock.expect(mockIndividualRequestDao.getPendingIndividualRequests(maxForwardingAttempts)).andReturn(
                returnedListOfRequests);

        for (IIndividualRequest individualRequestObj : returnedListOfRequests) {

            mockMessagingUtility.enqueueRequest(individualRequestObj);
            EasyMock.expectLastCall();

            // Re-set the forwarding attempts on the individual request.
            individualRequestObj.resetForwardingAttempts();
        }

        mockIndividualRequestDao.persistBulk(returnedListOfRequests);
        EasyMock.expectLastCall();

        EasyMock.replay(mockIndividualRequestDao);
        EasyMock.replay(mockCacheable);
        EasyMock.replay(mockMessagingUtility);

        this.messageTaskService.queueMessages();

        EasyMock.verify(mockIndividualRequestDao);
        EasyMock.verify(mockCacheable);
        EasyMock.verify(mockMessagingUtility);

        Assert.assertEquals("Forwarding attempts on individual request", 0, returnedListOfRequests.get(0)
                .getForwardingAttempts());
        Assert.assertEquals("Status set to Receieved", IIndividualRequest.IndividualRequestStatus.RECEIVED
                .getStatus(), returnedListOfRequests.get(0).getRequestStatus());

        Assert.assertTrue("Test completed succesfully", true);

    }

    /**
     * This method tests the queue pending message method and the condition that
     * there are no pending messages to return from database.
     */
    @Test
    public void queuePendingMessageWhenNoMessageFound() {
        final List<IIndividualRequest> individualRequests = new ArrayList<IIndividualRequest>();

        final IGlobalParameter maxForwardingAttemptsParam = new GlobalParameter();
        maxForwardingAttemptsParam.setName("MAX_FORWARDING_ATTEMPTS");
        maxForwardingAttemptsParam.setValue("3");
        EasyMock.expect(this.mockCacheable.getValue(IGlobalParameter.class, "MAX_FORWARDING_ATTEMPTS")).andReturn(
                maxForwardingAttemptsParam);

        final int maxForwardingAttempts = Integer.valueOf(maxForwardingAttemptsParam.getValue());

        EasyMock.expect(mockIndividualRequestDao.getPendingIndividualRequests(maxForwardingAttempts)).andReturn(
                individualRequests);

        EasyMock.replay(mockIndividualRequestDao);

        this.messageTaskService.queueMessages();

        EasyMock.verify(mockIndividualRequestDao);

        Assert.assertTrue("Method test completed successfully", true);

    }

    /**
     * This method tests the all successful scenario of the queue pending message
     * when the max forwarding attempts global parameter is not defined in the database.
     */
    @Test
    public void queuePendingMessageForwardingAttemptsNotAvailable() {
        final String sdtRequestRef = "TEST_1";
        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference(sdtRequestRef);
        setUpIndividualRequest(individualRequest);
        individualRequest.setRequestStatus("Forwarded");
        individualRequest.setForwardingAttempts(3);

        final List<IIndividualRequest> individualRequests = new ArrayList<IIndividualRequest>();
        individualRequests.add(individualRequest);

        EasyMock.expect(this.mockCacheable.getValue(IGlobalParameter.class, "MAX_FORWARDING_ATTEMPTS")).andReturn(
                null);

        final int maxForwardingAttempts = 3;

        EasyMock.expect(mockIndividualRequestDao.getPendingIndividualRequests(maxForwardingAttempts)).andReturn(
                individualRequests);

        for (IIndividualRequest individualRequestObj : individualRequests) {
            mockMessagingUtility.enqueueRequest(individualRequestObj);
            EasyMock.expectLastCall();

            // Re-set the forwarding attempts on the individual request.
            individualRequestObj.resetForwardingAttempts();
        }

        mockIndividualRequestDao.persistBulk(individualRequests);
        EasyMock.expectLastCall();

        EasyMock.replay(mockIndividualRequestDao);
        EasyMock.replay(mockCacheable);
        EasyMock.replay(mockMessagingUtility);

        this.messageTaskService.queueMessages();

        EasyMock.verify(mockIndividualRequestDao);
        EasyMock.verify(mockCacheable);
        EasyMock.verify(mockMessagingUtility);

        Assert.assertEquals("Forwarding attempts on individual request", 0, individualRequests.get(0)
                .getForwardingAttempts());
        Assert.assertEquals("Status set to Receieved", IIndividualRequest.IndividualRequestStatus.RECEIVED
                .getStatus(), individualRequests.get(0).getRequestStatus());

        Assert.assertTrue("Test completed succesfully", true);

    }

    /**
     * Set up a valid individual request object.
     *
     * @param request the individual request
     */
    private void setUpIndividualRequest(final IIndividualRequest request) {
        final IBulkSubmission bulkSubmission = new BulkSubmission();
        final IBulkCustomer bulkCustomer = new BulkCustomer();
        final ITargetApplication targetApp = new TargetApplication();

        targetApp.setId(1L);
        targetApp.setTargetApplicationCode("MCOL");
        targetApp.setTargetApplicationName("TEST_TargetApp");
        final Set<IServiceRouting> serviceRoutings = new HashSet<IServiceRouting>();

        final ServiceRouting serviceRouting = new ServiceRouting();
        serviceRouting.setId(1L);
        serviceRouting.setWebServiceEndpoint("MCOL_END_POINT");

        final IServiceType serviceType = new ServiceType();
        serviceType.setId(1L);
        serviceType.setName("RequestTest1");
        serviceType.setDescription("RequestTestDesc1");
        serviceType.setStatus("RequestTestStatus");

        serviceRouting.setServiceType(serviceType);

        serviceRoutings.add(serviceRouting);

        targetApp.setServiceRoutings(serviceRoutings);

        bulkSubmission.setTargetApplication(targetApp);

        bulkCustomer.setId(1L);
        bulkCustomer.setSdtCustomerId(10L);

        bulkSubmission.setBulkCustomer(bulkCustomer);
        bulkSubmission.setCustomerReference("TEST_CUST_REF");
        bulkSubmission.setId(1L);
        bulkSubmission.setNumberOfRequest(1);
        final List<IIndividualRequest> requests = new ArrayList<IIndividualRequest>();
        requests.add(request);

        bulkSubmission.setIndividualRequests(requests);

        request.setBulkSubmission(bulkSubmission);
    }

}
