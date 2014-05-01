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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.ws.WebServiceException;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.hibernate.criterion.Criterion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.consumers.api.IConsumerGateway;
import uk.gov.moj.sdt.consumers.exception.SoapFaultException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.ErrorMessage;
import uk.gov.moj.sdt.domain.GlobalParameter;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.ServiceRouting;
import uk.gov.moj.sdt.domain.ServiceType;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IErrorMessage;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.IServiceRouting;
import uk.gov.moj.sdt.domain.api.IServiceType;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.services.messaging.api.IMessageWriter;
import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;
import uk.gov.moj.sdt.services.utils.GenericXmlParser;
import uk.gov.moj.sdt.utils.SdtContext;

/**
 * Test class for TargetApplicationSubmissionService.
 * 
 * @author Manoj Kulkarni
 * 
 */
public class TargetApplicationSubmissionServiceTest
{
    /**
     * Logger for debugging.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (TargetApplicationSubmissionServiceTest.class);

    /**
     * Target Application Submision Service object.
     */
    private TargetApplicationSubmissionService targetAppSubmissionService;

    /**
     * Mocked Individual Request Dao object.
     */
    private IIndividualRequestDao mockIndividualRequestDao;

    /**
     * Mocked consumer gateway object.
     */
    private IConsumerGateway mockConsumerGateway;

    /**
     * The mocked ICacheable reference to the global parameters cache.
     */
    private ICacheable mockCacheable;

    /**
     * Mocked message writer reference.
     */
    private IMessageWriter mockMessageWriter;

    /**
     * The mocked ICacheable reference to the error message cache.
     */
    private ICacheable mockErrorMsgCacheable;

    /**
     * Method to do any pre-test set-up.
     */
    @Before
    public void setUp ()
    {
        targetAppSubmissionService = new TargetApplicationSubmissionService ();

        // Instantiate all the mocked objects and set them in the target application submission service
        mockIndividualRequestDao = EasyMock.createMock (IIndividualRequestDao.class);
        targetAppSubmissionService.setIndividualRequestDao (mockIndividualRequestDao);

        mockConsumerGateway = EasyMock.createMock (IConsumerGateway.class);

        targetAppSubmissionService.setRequestConsumer (mockConsumerGateway);

        mockCacheable = EasyMock.createMock (ICacheable.class);
        targetAppSubmissionService.setGlobalParametersCache (mockCacheable);

        mockMessageWriter = EasyMock.createMock (IMessageWriter.class);
        targetAppSubmissionService.setMessageWriter (mockMessageWriter);

        mockErrorMsgCacheable = EasyMock.createMock (ICacheable.class);
        targetAppSubmissionService.setErrorMessagesCache (mockErrorMsgCacheable);

        final GenericXmlParser genericParser = new GenericXmlParser ();
        genericParser.setEnclosingTag ("targetAppDetail");

        targetAppSubmissionService.setIndividualResponseXmlParser (genericParser);
    }

    /**
     * This method checks an all positive scenario for processing request to submit.
     * 
     */
    @Test
    public void processRequestToSubmitAllSuccess ()
    {
        final String sdtRequestRef = "TEST_1";
        final IIndividualRequest individualRequest = new IndividualRequest ();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference (sdtRequestRef);
        individualRequest.setRequestStatus ("Received");
        setUpIndividualRequest (individualRequest);

        EasyMock.expect (this.mockIndividualRequestDao.getRequestBySdtReference (sdtRequestRef)).andReturn (
                individualRequest);

        final IGlobalParameter individualReqProcessingDelay = new GlobalParameter ();
        individualReqProcessingDelay.setValue ("10");
        individualReqProcessingDelay.setName ("MCOL_INDV_REQ_DELAY");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "MCOL_INDV_REQ_DELAY")).andReturn (
                individualReqProcessingDelay);

        individualRequest.setRequestStatus ("Forwarded");
        this.mockIndividualRequestDao.persist (individualRequest);
        EasyMock.expectLastCall ();

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter ();
        connectionTimeOutParam.setName ("TARGET_APP_TIMEOUT");
        connectionTimeOutParam.setValue ("1000");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "TARGET_APP_TIMEOUT")).andReturn (
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter ();
        receiveTimeOutParam.setName ("TARGET_APP_RESP_TIMEOUT");
        receiveTimeOutParam.setValue ("12000");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "TARGET_APP_RESP_TIMEOUT")).andReturn (
                receiveTimeOutParam);

        this.mockConsumerGateway.individualRequest (individualRequest, 1000, 12000);
        EasyMock.expectLastCall ().andAnswer (new IAnswer<Object> ()
        {
            @Override
            public Object answer () throws Throwable
            {
                ((IndividualRequest) EasyMock.getCurrentArguments ()[0])
                        .setRequestStatus (IIndividualRequest.IndividualRequestStatus.ACCEPTED.getStatus ());
                // required to be null for a void method
                return null;
            }
        });

        this.mockIndividualRequestDao.persist (individualRequest);
        EasyMock.expectLastCall ();

        final IBulkSubmission bulkSubmission = individualRequest.getBulkSubmission ();

        EasyMock.expect (
                this.mockIndividualRequestDao.queryAsCount (EasyMock.same (IIndividualRequest.class),
                        EasyMock.isA (Criterion.class), EasyMock.isA (Criterion.class))).andReturn (0L);

        mockIndividualRequestDao.persist (bulkSubmission);
        EasyMock.expectLastCall ();

        EasyMock.replay (mockIndividualRequestDao);
        EasyMock.replay (mockConsumerGateway);
        EasyMock.replay (mockCacheable);

        // Setup dummy target response
        SdtContext.getContext ().setRawInXml ("response");

        this.targetAppSubmissionService.processRequestToSubmit (sdtRequestRef);

        EasyMock.verify (mockIndividualRequestDao);
        EasyMock.verify (mockConsumerGateway);
        EasyMock.verify (mockCacheable);

        Assert.assertEquals ("Bulk submission status is incorrect", IBulkSubmission.BulkRequestStatus.COMPLETED
                .getStatus (), individualRequest.getBulkSubmission ().getSubmissionStatus ());
        Assert.assertNotNull ("Bulk submission completed date should be populated", individualRequest
                .getBulkSubmission ().getCompletedDate ());
        Assert.assertNotNull ("Bulk submission updated date should be populated", individualRequest
                .getBulkSubmission ().getUpdatedDate ());

    }

    /**
     * This method checks an all positive scenario for processing request to submit.
     * In the end there are still individual requests outstanding so the bulk submission
     * is not marked as completed.
     * 
     */
    @Test
    public void processRequestToSubmitSuccess ()
    {
        final String sdtRequestRef = "TEST_1";
        final IIndividualRequest individualRequest = new IndividualRequest ();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference (sdtRequestRef);
        individualRequest.setRequestStatus ("Received");
        setUpIndividualRequest (individualRequest);

        EasyMock.expect (this.mockIndividualRequestDao.getRequestBySdtReference (sdtRequestRef)).andReturn (
                individualRequest);

        final IGlobalParameter individualReqProcessingDelay = new GlobalParameter ();
        individualReqProcessingDelay.setValue ("10");
        individualReqProcessingDelay.setName ("MCOL_INDV_REQ_DELAY");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "MCOL_INDV_REQ_DELAY")).andReturn (
                individualReqProcessingDelay);

        individualRequest.setRequestStatus ("Forwarded");
        this.mockIndividualRequestDao.persist (individualRequest);
        EasyMock.expectLastCall ();

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter ();
        connectionTimeOutParam.setName ("TARGET_APP_TIMEOUT");
        connectionTimeOutParam.setValue ("1000");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "TARGET_APP_TIMEOUT")).andReturn (
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter ();
        receiveTimeOutParam.setName ("TARGET_APP_RESP_TIMEOUT");
        receiveTimeOutParam.setValue ("12000");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "TARGET_APP_RESP_TIMEOUT")).andReturn (
                receiveTimeOutParam);

        this.mockConsumerGateway.individualRequest (individualRequest, 1000, 12000);
        EasyMock.expectLastCall ().andAnswer (new IAnswer<Object> ()
        {
            @Override
            public Object answer () throws Throwable
            {
                ((IndividualRequest) EasyMock.getCurrentArguments ()[0])
                        .setRequestStatus (IIndividualRequest.IndividualRequestStatus.ACCEPTED.getStatus ());
                // required to be null for a void method
                return null;
            }
        });

        this.mockIndividualRequestDao.persist (individualRequest);
        EasyMock.expectLastCall ();

        final List<IIndividualRequest> indRequests = new ArrayList<IIndividualRequest> ();
        indRequests.add (individualRequest);

        EasyMock.expect (
                this.mockIndividualRequestDao.queryAsCount (EasyMock.same (IIndividualRequest.class),
                        EasyMock.isA (Criterion.class), EasyMock.isA (Criterion.class))).andReturn (
                Long.valueOf (indRequests.size ()));

        EasyMock.replay (mockIndividualRequestDao);
        EasyMock.replay (mockConsumerGateway);
        EasyMock.replay (mockCacheable);

        // Setup dummy target response
        SdtContext.getContext ().setRawInXml ("response");

        this.targetAppSubmissionService.processRequestToSubmit (sdtRequestRef);

        EasyMock.verify (mockIndividualRequestDao);
        EasyMock.verify (mockConsumerGateway);
        EasyMock.verify (mockCacheable);

        Assert.assertTrue ("Expected to pass", true);

    }

    /**
     * Test method to test for the time out.
     */
    @Test
    public void processRequestToSubmitTimeOut ()
    {
        LOGGER.debug ("Timeout scenario");

        final String sdtRequestRef = "TEST_1";
        final IIndividualRequest individualRequest = new IndividualRequest ();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference (sdtRequestRef);
        individualRequest.setRequestStatus ("Received");
        setUpIndividualRequest (individualRequest);

        EasyMock.expect (this.mockIndividualRequestDao.getRequestBySdtReference (sdtRequestRef)).andReturn (
                individualRequest);

        final IGlobalParameter individualReqProcessingDelay = new GlobalParameter ();
        individualReqProcessingDelay.setValue ("10");
        individualReqProcessingDelay.setName ("MCOL_INDV_REQ_DELAY");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "MCOL_INDV_REQ_DELAY")).andReturn (
                individualReqProcessingDelay);

        individualRequest.setRequestStatus ("Forwarded");
        this.mockIndividualRequestDao.persist (individualRequest);
        EasyMock.expectLastCall ().times (1);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter ();
        connectionTimeOutParam.setName ("TARGET_APP_TIMEOUT");
        connectionTimeOutParam.setValue ("1000");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "TARGET_APP_TIMEOUT")).andReturn (
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter ();
        receiveTimeOutParam.setName ("TARGET_APP_RESP_TIMEOUT");
        receiveTimeOutParam.setValue ("12000");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "TARGET_APP_RESP_TIMEOUT")).andReturn (
                receiveTimeOutParam);

        final TimeoutException timeoutEx = new TimeoutException ("Timeout occurred", "Timeout occurred");
        this.mockConsumerGateway.individualRequest (individualRequest, 1000, 12000);
        EasyMock.expectLastCall ().andThrow (timeoutEx);

        final IErrorMessage errorMsg = new ErrorMessage ();
        errorMsg.setErrorCode ("REQ_NOT_ACK");
        errorMsg.setErrorDescription ("Request not acknowledged");
        errorMsg.setErrorText ("Request Not Acknowledged");

        EasyMock.expect (this.mockErrorMsgCacheable.getValue (IErrorMessage.class, "REQ_NOT_ACK")).andReturn (errorMsg);

        // Now create an ErrorLog object with the ErrorMessage object and the IndividualRequest object
        final IErrorLog errorLog = new ErrorLog (errorMsg.getErrorCode (), errorMsg.getErrorText ());

        individualRequest.setErrorLog (errorLog);

        this.mockIndividualRequestDao.persist (individualRequest);
        EasyMock.expectLastCall ();

        final IGlobalParameter maxForwardingAttemptsParam = new GlobalParameter ();
        maxForwardingAttemptsParam.setName ("MAX_FORWARDING_ATTEMPTS");
        maxForwardingAttemptsParam.setValue ("3");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "MAX_FORWARDING_ATTEMPTS")).andReturn (
                maxForwardingAttemptsParam);

        this.mockMessageWriter.queueMessage (EasyMock.isA (ISdtMessage.class), EasyMock.isA (String.class),
                EasyMock.anyBoolean ());
        EasyMock.expectLastCall ();

        EasyMock.replay (mockIndividualRequestDao);
        EasyMock.replay (mockConsumerGateway);
        EasyMock.replay (mockCacheable);
        EasyMock.replay (mockMessageWriter);
        EasyMock.replay (mockErrorMsgCacheable);

        this.targetAppSubmissionService.processRequestToSubmit (sdtRequestRef);

        EasyMock.verify (mockIndividualRequestDao);
        EasyMock.verify (mockConsumerGateway);
        EasyMock.verify (mockCacheable);
        EasyMock.verify (mockMessageWriter);
        EasyMock.verify (mockErrorMsgCacheable);

        Assert.assertTrue ("Expected to pass", true);
    }

    /**
     * Test method to test for web service exception.
     */
    @Test
    public void processRequestToSubmitForWebServiceException ()
    {
        LOGGER.debug ("Web service exception scenario");

        final String sdtRequestRef = "TEST_1";
        final IIndividualRequest individualRequest = new IndividualRequest ();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference (sdtRequestRef);
        individualRequest.setRequestStatus ("Received");
        setUpIndividualRequest (individualRequest);

        EasyMock.expect (this.mockIndividualRequestDao.getRequestBySdtReference (sdtRequestRef)).andReturn (
                individualRequest);

        final IGlobalParameter individualReqProcessingDelay = new GlobalParameter ();
        individualReqProcessingDelay.setValue ("10");
        individualReqProcessingDelay.setName ("MCOL_INDV_REQ_DELAY");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "MCOL_INDV_REQ_DELAY")).andReturn (
                individualReqProcessingDelay);

        individualRequest.setRequestStatus ("Forwarded");
        this.mockIndividualRequestDao.persist (individualRequest);
        EasyMock.expectLastCall ().times (1);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter ();
        connectionTimeOutParam.setName ("TARGET_APP_TIMEOUT");
        connectionTimeOutParam.setValue ("1000");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "TARGET_APP_TIMEOUT")).andReturn (
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter ();
        receiveTimeOutParam.setName ("TARGET_APP_RESP_TIMEOUT");
        receiveTimeOutParam.setValue ("12000");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "TARGET_APP_RESP_TIMEOUT")).andReturn (
                receiveTimeOutParam);

        final WebServiceException wsException = new WebServiceException ("WS Error");
        this.mockConsumerGateway.individualRequest (individualRequest, 1000, 12000);
        EasyMock.expectLastCall ().andThrow (wsException);

        this.mockIndividualRequestDao.persist (individualRequest);
        EasyMock.expectLastCall ();

        this.mockMessageWriter.queueMessage (EasyMock.isA (ISdtMessage.class), EasyMock.isA (String.class),
                EasyMock.eq (true));
        EasyMock.expectLastCall ();

        EasyMock.replay (mockIndividualRequestDao);
        EasyMock.replay (mockConsumerGateway);
        EasyMock.replay (mockCacheable);
        EasyMock.replay (mockMessageWriter);
        EasyMock.replay (mockErrorMsgCacheable);

        this.targetAppSubmissionService.processRequestToSubmit (sdtRequestRef);

        EasyMock.verify (mockIndividualRequestDao);
        EasyMock.verify (mockConsumerGateway);
        EasyMock.verify (mockCacheable);
        EasyMock.verify (mockMessageWriter);
        EasyMock.verify (mockErrorMsgCacheable);

        Assert.assertTrue ("Expected to pass", true);
    }

    /**
     * Test method to test for the soap fault error.
     */
    @Test
    public void processRequestToSubmitSoapFault ()
    {
        final String sdtRequestRef = "TEST_1";
        final IIndividualRequest individualRequest = new IndividualRequest ();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference (sdtRequestRef);
        individualRequest.setRequestStatus ("Received");
        setUpIndividualRequest (individualRequest);

        EasyMock.expect (this.mockIndividualRequestDao.getRequestBySdtReference (sdtRequestRef)).andReturn (
                individualRequest);

        final IGlobalParameter individualReqProcessingDelay = new GlobalParameter ();
        individualReqProcessingDelay.setValue ("10");
        individualReqProcessingDelay.setName ("MCOL_INDV_REQ_DELAY");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "MCOL_INDV_REQ_DELAY")).andReturn (
                individualReqProcessingDelay);

        individualRequest.setRequestStatus ("Forwarded");
        this.mockIndividualRequestDao.persist (individualRequest);
        EasyMock.expectLastCall ().times (1);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter ();
        connectionTimeOutParam.setName ("TARGET_APP_TIMEOUT");
        connectionTimeOutParam.setValue ("1000");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "TARGET_APP_TIMEOUT")).andReturn (
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter ();
        receiveTimeOutParam.setName ("TARGET_APP_RESP_TIMEOUT");
        receiveTimeOutParam.setValue ("12000");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "TARGET_APP_RESP_TIMEOUT")).andReturn (
                receiveTimeOutParam);

        final SoapFaultException soapEx = new SoapFaultException ("Soap Fault", "Soap Fault occurred");
        this.mockConsumerGateway.individualRequest (individualRequest, 1000, 12000);
        EasyMock.expectLastCall ().andThrow (soapEx);

        this.mockIndividualRequestDao.persist (individualRequest);
        EasyMock.expectLastCall ();

        this.mockMessageWriter.queueMessage (EasyMock.isA (ISdtMessage.class), EasyMock.isA (String.class),
                EasyMock.eq (true));
        EasyMock.expectLastCall ();

        EasyMock.replay (mockIndividualRequestDao);
        EasyMock.replay (mockConsumerGateway);
        EasyMock.replay (mockMessageWriter);
        EasyMock.replay (mockCacheable);

        this.targetAppSubmissionService.processRequestToSubmit (sdtRequestRef);

        EasyMock.verify (mockIndividualRequestDao);
        EasyMock.verify (mockConsumerGateway);
        EasyMock.verify (mockMessageWriter);
        EasyMock.verify (mockCacheable);

        Assert.assertEquals ("Individual Request status not as expected",
                IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus (),
                individualRequest.getRequestStatus ());

        Assert.assertNull ("Bulk submission completed date should not be populated", individualRequest
                .getBulkSubmission ().getCompletedDate ());

    }

    /**
     * Test method to test for the rejected error.
     */
    @Test
    public void processRequestToSubmitRejected ()
    {
        final String sdtRequestRef = "TEST_1";
        final IIndividualRequest individualRequest = new IndividualRequest ();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference (sdtRequestRef);
        individualRequest.setRequestStatus ("Received");
        setUpIndividualRequest (individualRequest);

        EasyMock.expect (this.mockIndividualRequestDao.getRequestBySdtReference (sdtRequestRef)).andReturn (
                individualRequest);

        final IGlobalParameter individualReqProcessingDelay = new GlobalParameter ();
        individualReqProcessingDelay.setValue ("10");
        individualReqProcessingDelay.setName ("MCOL_INDV_REQ_DELAY");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "MCOL_INDV_REQ_DELAY")).andReturn (
                individualReqProcessingDelay);

        individualRequest.setRequestStatus ("Forwarded");
        this.mockIndividualRequestDao.persist (individualRequest);
        EasyMock.expectLastCall ().times (1);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter ();
        connectionTimeOutParam.setName ("TARGET_APP_TIMEOUT");
        connectionTimeOutParam.setValue ("1000");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "TARGET_APP_TIMEOUT")).andReturn (
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter ();
        receiveTimeOutParam.setName ("TARGET_APP_RESP_TIMEOUT");
        receiveTimeOutParam.setValue ("12000");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "TARGET_APP_RESP_TIMEOUT")).andReturn (
                receiveTimeOutParam);

        this.mockConsumerGateway.individualRequest (individualRequest, 1000, 12000);

        EasyMock.expectLastCall ().andAnswer (new IAnswer<Object> ()
        {
            @Override
            public Object answer () throws Throwable
            {
                ((IndividualRequest) EasyMock.getCurrentArguments ()[0])
                        .setRequestStatus (IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus ());
                // required to be null for a void method
                return null;
            }
        });

        this.mockIndividualRequestDao.persist (individualRequest);
        EasyMock.expectLastCall ();

        final IBulkSubmission bulkSubmission = individualRequest.getBulkSubmission ();

        EasyMock.expect (
                this.mockIndividualRequestDao.queryAsCount (EasyMock.same (IIndividualRequest.class),
                        EasyMock.isA (Criterion.class), EasyMock.isA (Criterion.class))).andReturn (0L);

        mockIndividualRequestDao.persist (bulkSubmission);
        EasyMock.expectLastCall ();

        EasyMock.replay (mockIndividualRequestDao);
        EasyMock.replay (mockConsumerGateway);
        EasyMock.replay (mockCacheable);
        EasyMock.replay (mockErrorMsgCacheable);

        // Setup dummy response
        SdtContext.getContext ().setRawInXml ("response");

        this.targetAppSubmissionService.processRequestToSubmit (sdtRequestRef);

        EasyMock.verify (mockIndividualRequestDao);
        EasyMock.verify (mockConsumerGateway);
        EasyMock.verify (mockCacheable);
        EasyMock.verify (mockErrorMsgCacheable);

        Assert.assertEquals ("Bulk submission status is incorrect", IBulkSubmission.BulkRequestStatus.COMPLETED
                .getStatus (), individualRequest.getBulkSubmission ().getSubmissionStatus ());
        Assert.assertNotNull ("Bulk submission completed date should be populated", individualRequest
                .getBulkSubmission ().getCompletedDate ());
        Assert.assertNotNull ("Bulk submission updated date should be populated", individualRequest
                .getBulkSubmission ().getUpdatedDate ());
    }

    /**
     * Test method to test the scenario where we get an individual request
     * that is to be rejected.
     */
    @Test
    public void processDlqRequestRejected ()
    {
        final String requestStatus = "REJECTED";
        final String sdtRequestRef = "TEST_1";
        final IIndividualRequest individualRequest = new IndividualRequest ();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference (sdtRequestRef);
        this.setUpIndividualRequest (individualRequest);
        individualRequest.setRequestStatus (IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus ());

        final IGlobalParameter contactNameParameter = new GlobalParameter ();
        contactNameParameter.setValue ("Tester");
        contactNameParameter.setName ("CONTACT_DETAILS");
        EasyMock.expect (this.mockCacheable.getValue (IGlobalParameter.class, "CONTACT_DETAILS")).andReturn (
                contactNameParameter);

        final IErrorMessage errorMsg = new ErrorMessage ();
        errorMsg.setErrorCode ("SDT_CLIENT_ERR");
        errorMsg.setErrorDescription ("SDT Client Error");
        errorMsg.setErrorText ("SDT Client Error");

        EasyMock.expect (this.mockErrorMsgCacheable.getValue (IErrorMessage.class, "SDT_CLIENT_ERR")).andReturn (
                errorMsg);

        final String contactName = "Test";

        // Now create an ErrorLog object with the ErrorMessage object and the IndividualRequest object
        final IErrorLog errorLog =
                new ErrorLog (errorMsg.getErrorCode (), MessageFormat.format (errorMsg.getErrorText (), contactName));

        individualRequest.setErrorLog (errorLog);
        individualRequest.setRequestStatus (IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus ());

        this.mockIndividualRequestDao.persist (individualRequest);
        EasyMock.expectLastCall ();

        final IBulkSubmission bulkSubmission = individualRequest.getBulkSubmission ();

        EasyMock.expect (
                this.mockIndividualRequestDao.queryAsCount (EasyMock.same (IIndividualRequest.class),
                        EasyMock.isA (Criterion.class), EasyMock.isA (Criterion.class))).andReturn (0L);

        mockIndividualRequestDao.persist (bulkSubmission);
        EasyMock.expectLastCall ();

        EasyMock.replay (mockIndividualRequestDao);
        EasyMock.replay (mockCacheable);
        EasyMock.replay (mockErrorMsgCacheable);

        this.targetAppSubmissionService.processDLQRequest (individualRequest, requestStatus);

        EasyMock.verify (mockIndividualRequestDao);
        EasyMock.verify (mockCacheable);
        EasyMock.verify (mockErrorMsgCacheable);

        Assert.assertEquals ("Bulk submission status is incorrect", IBulkSubmission.BulkRequestStatus.COMPLETED
                .getStatus (), individualRequest.getBulkSubmission ().getSubmissionStatus ());
        Assert.assertNotNull ("Bulk submission completed date should be populated", individualRequest
                .getBulkSubmission ().getCompletedDate ());
        Assert.assertNotNull ("Bulk submission updated date should be populated", individualRequest
                .getBulkSubmission ().getUpdatedDate ());
        Assert.assertEquals ("Individual Request should not be marked as dead letter", false,
                individualRequest.isDeadLetter ());

    }

    /**
     * Test method to test the scenario where we get an individual request
     * that is to be Forwarded.
     */
    @Test
    public void processDlqRequestForwarded ()
    {
        final String requestStatus = "FORWARDED";
        final String sdtRequestRef = "TEST_2";
        final IIndividualRequest individualRequest = new IndividualRequest ();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference (sdtRequestRef);
        this.setUpIndividualRequest (individualRequest);
        individualRequest.setRequestStatus (IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus ());

        individualRequest.setRequestStatus (IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus ());

        this.mockIndividualRequestDao.persist (individualRequest);
        EasyMock.expectLastCall ();

        EasyMock.replay (mockIndividualRequestDao);

        this.targetAppSubmissionService.processDLQRequest (individualRequest, requestStatus);

        EasyMock.verify (mockIndividualRequestDao);

        Assert.assertEquals ("Individual Request should not be marked as dead letter", false,
                individualRequest.isDeadLetter ());
        Assert.assertEquals ("Individual Request status is not FORWARDED",
                IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus (),
                individualRequest.getRequestStatus ());

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
        targetApp.setTargetApplicationCode ("MCOL");
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
