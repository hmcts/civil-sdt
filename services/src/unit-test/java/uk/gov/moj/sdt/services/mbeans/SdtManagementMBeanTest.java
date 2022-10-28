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
package uk.gov.moj.sdt.services.mbeans;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.IAnswer;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.util.ReflectionUtils;

import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.services.api.ITargetApplicationSubmissionService;
import uk.gov.moj.sdt.services.utils.api.IMessagingUtility;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.mbeans.api.ISdtManagementMBean;

/**
 * Test class for the SdtManagementMBean.
 *
 * @author Manoj Kulkarni
 */
public class SdtManagementMBeanTest extends AbstractSdtUnitTestBase {
    /**
     * Success message to return after successfully processing the SDT individual request.
     */
    private static final String OK_MESSAGE = "OK";

    /**
     * Error message to return if the mandatory parameters are missing.
     */
    private static final String MANDATORY_PARAMETERS_ERR_MSG =
            "SDT Request Reference and the Request Status should be supplied.";

    /**
     * Error message to return if the invalid request status values are supplied.
     */
    private static final String INVALID_PARAM_VALUES_MSG =
            "Invalid Request Status supplied. The Request Status should be either REJECTED or FORWARDED";

    /**
     * Error message to return if the Sdt Request Reference parameter does not correspond
     * to an Individual Request record in the database.
     */
    private static final String INVALID_SDT_REQUEST_REF_MSG = "SDT Request Reference supplied does not exist.";

    /**
     * Error message to return if the SDT Request Reference parameter is not marked as Dead Letter
     * Queue flag.
     */
    private static final String SDT_REQUEST_NOT_ON_DLQ_MSG =
            "SDT Request Reference supplied is not marked as dead letter and cannot be processed.";

    /**
     * Queue name for testing.
     */
    private static final String TEST_QUEUE_NAME = "TestQ";

    /**
     * Pool size for testing.
     */
    private static final int TEST_POOL_SIZE = 10;

    /**
     * Sdt Request Reference for testing.
     */
    private static final String TEST_SDT_REQ_REF = "Sdt_Req_Test";

    /**
     * Number of minutes to check that the request is not gone stale.
     */
    private static final int TEST_STALE_DURATION = 90;

    /**
     * The SdtManagementMBean that is under test.
     */
    private ISdtManagementMBean sdtManagementMBean;

    /**
     * Mock Individual Request Dao to perform operations on the individual request object.
     */
    private IIndividualRequestDao mockIndividualRequestDao;

    /**
     * This mock messaging utility reference for testing.
     */
    private IMessagingUtility mockMessagingUtility;

    /**
     * This mock variable holding the target application submission service.
     */
    private ITargetApplicationSubmissionService mockTargetAppSubmissionService;

    /**
     * Method to do any pre-test set-up.
     */
    @Before
    public void setUp() {
        sdtManagementMBean = new SdtManagementMBean();

        // Instantiate all the mocked objects and set them up in the MBean
        mockIndividualRequestDao = EasyMock.createMock(IIndividualRequestDao.class);
        this.setPrivateField(SdtManagementMBean.class, sdtManagementMBean, "individualRequestDao",
                IIndividualRequestDao.class, mockIndividualRequestDao);

        mockMessagingUtility = EasyMock.createMock(IMessagingUtility.class);
        this.setPrivateField(SdtManagementMBean.class, sdtManagementMBean, "messagingUtility",
                IMessagingUtility.class, mockMessagingUtility);

        mockTargetAppSubmissionService = EasyMock.createMock(ITargetApplicationSubmissionService.class);
        this.setPrivateField(SdtManagementMBean.class, sdtManagementMBean, "targetAppSubmissionService",
                ITargetApplicationSubmissionService.class, mockTargetAppSubmissionService);

    }

    /**
     * Test method for the uncache method.
     */
    @Test
    public void uncacheTest() {
        Assert.assertEquals("The cache re-set counter should be zero.", 0,
                this.sdtManagementMBean.getCacheResetControl());

        this.sdtManagementMBean.uncache();

        Assert.assertEquals("The cache re-set counter should be incremented.", 1,
                this.sdtManagementMBean.getCacheResetControl());
    }

    /**
     * Test method for the setMdbPoolSize. This method will check that if the
     * messsage listener container is not set then it returns an appropriate message.
     */
    @Test
    public void setMdbPoolSizeUnknownMessageContainer() {
        final String returnVal = this.sdtManagementMBean.setMdbPoolSize(TEST_QUEUE_NAME, TEST_POOL_SIZE);

        Assert.assertEquals("The return message should have been an error", "mdb pool [" + TEST_QUEUE_NAME +
                "] not found", returnVal);
    }

    /**
     * Test method for setMdbPoolSize. This method will check that if the
     * pool size is invalid, an appropriate error message is returned.
     */
    @Test
    public void setMdbPoolSizePoolSizeInvalid() {
        final DefaultMessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer();
        messageListenerContainer.setDestinationName(TEST_QUEUE_NAME);

        this.sdtManagementMBean.setMessageListenerContainer(messageListenerContainer);

        String returnVal = this.sdtManagementMBean.setMdbPoolSize(TEST_QUEUE_NAME, TEST_POOL_SIZE + 41);

        Assert.assertEquals("The return message should have been an error",
                "MDB pool size can only be set between 1 and 50.", returnVal);

        returnVal = this.sdtManagementMBean.setMdbPoolSize(TEST_QUEUE_NAME, TEST_POOL_SIZE - 10);

        Assert.assertEquals("The return message should have been an error",
                "MDB pool size can only be set between 1 and 50.", returnVal);
    }

    /**
     * Test method for setMdbPoolSize. A test for the positive conditions scenario.
     */
    @Test
    public void setMdbPoolSize() {
        final DefaultMessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer();
        messageListenerContainer.setDestinationName(TEST_QUEUE_NAME);

        this.sdtManagementMBean.setMessageListenerContainer(messageListenerContainer);

        final String returnVal = this.sdtManagementMBean.setMdbPoolSize(TEST_QUEUE_NAME, TEST_POOL_SIZE);

        Assert.assertEquals("The return message is incorrect", "mdb pool [" + TEST_QUEUE_NAME +
                "] size changed from 1 to " + TEST_POOL_SIZE, returnVal);

        Assert.assertEquals("The message listener container max concurrent pool size should have been changed", 10,
                messageListenerContainer.getMaxConcurrentConsumers());

    }

    /**
     * Test method for requeueOldIndividualRequests. This method tests the scenario
     * where there are no old individual requests to process.
     */
    @Test
    public void requeueOldRequestsNotFound() {
        final List<IIndividualRequest> individualRequests = new ArrayList<IIndividualRequest>();
        EasyMock.expect(mockIndividualRequestDao.getStaleIndividualRequests(TEST_STALE_DURATION)).andReturn(
                individualRequests);

        EasyMock.replay(mockIndividualRequestDao);

        this.sdtManagementMBean.requeueOldIndividualRequests(TEST_STALE_DURATION);

        EasyMock.verify(mockIndividualRequestDao);

        Assert.assertTrue("Not expected to call the method to requeue requests", true);
    }

    /**
     * Test method for requeueOldIndividualRequests. This method tests the scenario
     * where there are some old individual requests to process.
     */
    @Test
    public void requeueOldRequests() {
        final IIndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setSdtRequestReference("SDT_REQ_1");
        individualRequest.setForwardingAttempts(3);
        final List<IIndividualRequest> individualRequests = new ArrayList<IIndividualRequest>();
        individualRequests.add(individualRequest);

        EasyMock.expect(mockIndividualRequestDao.getStaleIndividualRequests(TEST_STALE_DURATION)).andReturn(
                individualRequests);

        mockMessagingUtility.enqueueRequest(individualRequest);
        EasyMock.expectLastCall();

        individualRequest.resetForwardingAttempts();

        mockIndividualRequestDao.persistBulk(individualRequests);
        EasyMock.expectLastCall();

        EasyMock.replay(mockIndividualRequestDao);
        EasyMock.replay(mockMessagingUtility);

        this.sdtManagementMBean.requeueOldIndividualRequests(TEST_STALE_DURATION);

        EasyMock.verify(mockIndividualRequestDao);
        EasyMock.verify(mockMessagingUtility);

        Assert.assertTrue("All tests passed", true);

    }

    /**
     * Test method for processDLQRequest. Calls the method with null values for
     * the Sdt Request Reference and the Request Status.
     */
    @Test
    public void processDLQRequestWithNullParams() {
        final String returnVal = this.sdtManagementMBean.processDlqRequest(null, null);

        Assert.assertEquals("Expected an error message.", MANDATORY_PARAMETERS_ERR_MSG, returnVal);
    }

    /**
     * Test method for processDLQRequest. Calls the method with valid value for
     * the Sdt Request Reference and Invalid value for the Request Status.
     */
    @Test
    public void processDLQRequestWithInvalidStatus() {
        final String returnVal = this.sdtManagementMBean.processDlqRequest(TEST_SDT_REQ_REF, "RECEIVED");

        Assert.assertEquals("Expected an error message.", INVALID_PARAM_VALUES_MSG, returnVal);
    }

    /**
     * Test method for processDLQRequest. Calls the method with invalid value for the
     * Sdt Request Reference such that does not exist in database.
     */
    @Test
    public void processDLQRequestWithInvalidSdtRequest() {
        final String invalidSdtReqReference = "INVALID_SDT_REQ_REF";

        EasyMock.expect(mockIndividualRequestDao.getRequestBySdtReference(invalidSdtReqReference)).andReturn(null);

        EasyMock.replay(mockIndividualRequestDao);

        final String returnVal =
                this.sdtManagementMBean.processDlqRequest(invalidSdtReqReference,
                        IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus());

        EasyMock.verify(mockIndividualRequestDao);

        Assert.assertEquals("Expected an error message.", INVALID_SDT_REQUEST_REF_MSG, returnVal);

    }

    /**
     * Test method for processDLQRequest. Test the scenario where the supplied
     * Sdt Request Reference is associated with an Individual Request but has the DLQ flag
     * false.
     */
    @Test
    public void processDLQRequestNotOnDLQ() {
        final IIndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setSdtRequestReference(TEST_SDT_REQ_REF);
        individualRequest.setDeadLetter(false);

        EasyMock.expect(mockIndividualRequestDao.getRequestBySdtReference(TEST_SDT_REQ_REF)).andReturn(
                individualRequest);

        EasyMock.replay(mockIndividualRequestDao);

        final String returnVal =
                this.sdtManagementMBean.processDlqRequest(TEST_SDT_REQ_REF,
                        IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus());

        EasyMock.verify(mockIndividualRequestDao);

        Assert.assertEquals("Expected an error message.", SDT_REQUEST_NOT_ON_DLQ_MSG, returnVal);

    }

    /**
     * Test method for processDQLRequest. This method tests the status change
     * for an valid Individual Request to Forwarded.
     */
    @Test
    public void processDLQRequestForwarded() {
        final IIndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setSdtRequestReference(TEST_SDT_REQ_REF);
        individualRequest.setDeadLetter(true);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus());
        individualRequest.setForwardingAttempts(3);

        Assert.assertEquals("The status is not as expected",
                IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus(),
                individualRequest.getRequestStatus());

        Assert.assertEquals("The forwarding attempts should be greater than zero", 3,
                individualRequest.getForwardingAttempts());

        EasyMock.expect(mockIndividualRequestDao.getRequestBySdtReference(TEST_SDT_REQ_REF)).andReturn(
                individualRequest);

        mockTargetAppSubmissionService.processDLQRequest(individualRequest,
                IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus());

        EasyMock.expectLastCall().andAnswer(new IAnswer<Object>() {
            @Override
            public Object answer() throws Throwable {
                ((IndividualRequest) EasyMock.getCurrentArguments()[0])
                        .setRequestStatus(IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus());
                ((IndividualRequest) EasyMock.getCurrentArguments()[0]).setDeadLetter(false);
                ((IndividualRequest) EasyMock.getCurrentArguments()[0]).setUpdatedDate(LocalDateTime.now());
                // required to be null for a void method
                return null;
            }
        });

        EasyMock.replay(mockIndividualRequestDao);
        EasyMock.replay(mockTargetAppSubmissionService);

        final String returnVal =
                this.sdtManagementMBean.processDlqRequest(TEST_SDT_REQ_REF,
                        IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus());

        EasyMock.verify(mockIndividualRequestDao);
        EasyMock.verify(mockTargetAppSubmissionService);

        Assert.assertEquals("The return value is OK", OK_MESSAGE, returnVal);

    }

    /**
     * Test method for processDQLRequest. This method tests the status change
     * for an valid Individual Request to Rejected.
     */
    @Test
    public void processDLQRequestRejected() {
        final IIndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setSdtRequestReference(TEST_SDT_REQ_REF);
        individualRequest.setDeadLetter(true);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus());
        individualRequest.setForwardingAttempts(3);

        Assert.assertEquals("The status is not as expected",
                IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus(),
                individualRequest.getRequestStatus());

        Assert.assertEquals("The forwarding attempts should be greater than zero", 3,
                individualRequest.getForwardingAttempts());

        EasyMock.expect(mockIndividualRequestDao.getRequestBySdtReference(TEST_SDT_REQ_REF)).andReturn(
                individualRequest);

        mockTargetAppSubmissionService.processDLQRequest(individualRequest,
                IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus());

        EasyMock.expectLastCall().andAnswer(new IAnswer<Object>() {
            @Override
            public Object answer() throws Throwable {
                ((IndividualRequest) EasyMock.getCurrentArguments()[0])
                        .setRequestStatus(IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus());
                ((IndividualRequest) EasyMock.getCurrentArguments()[0]).setDeadLetter(false);
                ((IndividualRequest) EasyMock.getCurrentArguments()[0]).setUpdatedDate(LocalDateTime.now());
                // required to be null for a void method
                return null;
            }
        });

        EasyMock.replay(mockIndividualRequestDao);
        EasyMock.replay(mockTargetAppSubmissionService);

        final String returnVal =
                this.sdtManagementMBean.processDlqRequest(TEST_SDT_REQ_REF,
                        IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus());

        EasyMock.verify(mockIndividualRequestDao);
        EasyMock.verify(mockTargetAppSubmissionService);

        Assert.assertEquals("The return value is OK", OK_MESSAGE, returnVal);

    }

    /**
     * This is a method to modify a private field with no accessor methods.
     *
     * @param concreteClazz        this is the class with the field accessor being changed (e.g.
     *                             MyClazz.class)
     * @param classInstance        this is the instance with the field state being changed (e.g.
     *                             myClazz)
     * @param instanceVariableName this is the field name (e.g. "myString")
     * @param variableClazz        this is the class of the field's type (e.g. String.class)
     * @param newFieldState        the new value of the filed (e.g. "a new value")
     */
    public void setPrivateField(final Class<?> concreteClazz, final Object classInstance,
                                final String instanceVariableName, final Class<?> variableClazz,
                                final Object newFieldState) {
        final Field field = ReflectionUtils.findField(concreteClazz, instanceVariableName, variableClazz);
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, classInstance, newFieldState);
    }

}
