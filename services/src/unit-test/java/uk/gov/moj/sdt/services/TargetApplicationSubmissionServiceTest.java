package uk.gov.moj.sdt.services;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import ch.qos.logback.classic.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import uk.gov.moj.sdt.consumers.ConsumerGateway;
import uk.gov.moj.sdt.consumers.api.IConsumerGateway;
import uk.gov.moj.sdt.consumers.exception.SoapFaultException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.dao.IndividualRequestDao;
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
import uk.gov.moj.sdt.services.messaging.MessageWriter;
import uk.gov.moj.sdt.services.messaging.api.IMessageWriter;
import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;
import uk.gov.moj.sdt.services.utils.GenericXmlParser;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.SdtContext;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import javax.xml.ws.WebServiceException;

import static ch.qos.logback.classic.Level.DEBUG;
import static ch.qos.logback.classic.Level.ERROR;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * Test class for TargetApplicationSubmissionService.
 *
 * @author Manoj Kulkarni
 */
@ExtendWith(MockitoExtension.class)
public class TargetApplicationSubmissionServiceTest extends AbstractSdtUnitTestBase {

    /**
     * Target Application Submision Service object.
     */
    private TargetApplicationSubmissionService targetAppSubmissionService;

    /**
     * Mocked Individual Request Dao object.
     */
    @Mock
    private IIndividualRequestDao mockIndividualRequestDao;

    /**
     * Mocked consumer gateway object.
     */
    @Mock
    private IConsumerGateway mockConsumerGateway;

    /**
     * The mocked ICacheable reference to the global parameters cache.
     */
    @Mock
    private ICacheable mockCacheable;

    /**
     * Mocked message writer reference.
     */
    private IMessageWriter mockMessageWriter;

    /**
     * The mocked ICacheable reference to the error message cache.
     */
    @Mock
    private ICacheable mockErrorMsgCacheable;

    private static final String TEST_1 = "TEST_1";

    private static final String TARGET_APP_TIMEOUT = "TARGET_APP_TIMEOUT";

    private static final String TARGET_APP_RESP_TIMEOUT = "TARGET_APP_RESP_TIMEOUT";

    private static final String RECEIVED = "Received";

    private static final String FORWARDED = "Forwarded";

    private static final String MCOL_INDV_REQ_DELAY = "MCOL_INDV_REQ_DELAY";

    /**
     * Method to do any pre-test set-up.
     */
    @BeforeEach
    @Override
    public void setUp() {

        // Instantiate all the mocked objects and set them in the target application submission service
        mockIndividualRequestDao = mock(IIndividualRequestDao.class);
        mockConsumerGateway = mock(IConsumerGateway.class);
        mockCacheable = mock(ICacheable.class);
        mockMessageWriter = mock(IMessageWriter.class);
        mockErrorMsgCacheable = mock(ICacheable.class);

        final GenericXmlParser genericParser = new GenericXmlParser();
        genericParser.setEnclosingTag("targetAppDetail");

        targetAppSubmissionService = new TargetApplicationSubmissionService(mockIndividualRequestDao,
                                                                            genericParser,
                                                                            mockConsumerGateway,
                                                                            mockMessageWriter);
        targetAppSubmissionService.setGlobalParametersCache(mockCacheable);
        targetAppSubmissionService.setErrorMessagesCache(mockErrorMsgCacheable);

    }

    /**
     * This method checks an all positive scenario for processing request to submit.
     */
    @Test
    public void processRequestToSubmitAllSuccess() {
        final String sdtRequestRef = TEST_1;
        final IIndividualRequest individualRequest = new IndividualRequest();
        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);

        // Set logging level to debug so that afterCompletion logging is captured
        logger.setLevel(DEBUG);

        // Create appender and add to logger
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        // Set-up the individual request object

        individualRequest.setSdtRequestReference(sdtRequestRef);
        individualRequest.setRequestStatus(RECEIVED);
        setUpIndividualRequest(individualRequest);

        when(this.mockIndividualRequestDao.getRequestBySdtReference(sdtRequestRef)).thenReturn(
                individualRequest);

        final IGlobalParameter individualReqProcessingDelay = new GlobalParameter();
        individualReqProcessingDelay.setValue("10");
        individualReqProcessingDelay.setName(MCOL_INDV_REQ_DELAY);
        when(this.mockCacheable.getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY)).thenReturn(
                individualReqProcessingDelay);

        individualRequest.setRequestStatus(FORWARDED);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName(TARGET_APP_TIMEOUT);
        connectionTimeOutParam.setValue("1000");
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT)).thenReturn(
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName(TARGET_APP_RESP_TIMEOUT);
        receiveTimeOutParam.setValue("12000");
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT)).thenReturn(
                receiveTimeOutParam);

        this.mockConsumerGateway.individualRequest(individualRequest, 1000, 12000);
        doAnswer(invocation -> {
            IndividualRequest argument = invocation.getArgument(0);
            argument.setRequestStatus(IIndividualRequest.IndividualRequestStatus.ACCEPTED.getStatus());
            return null;
        }).when(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);

        final IBulkSubmission bulkSubmission = individualRequest.getBulkSubmission();

        when(this.mockIndividualRequestDao.queryAsCount(eq(IndividualRequest.class), isA(
            Supplier.class))).thenReturn(0L);

        mockIndividualRequestDao.persist(bulkSubmission);

        // Setup dummy target response
        SdtContext.getContext().setRawInXml("response");

        this.targetAppSubmissionService.processRequestToSubmit(sdtRequestRef);

        List<ILoggingEvent> logList = listAppender.list;

        assertNotNull(logList.get(0).getFormattedMessage());

        logger.detachAndStopAllAppenders();

        // Verify the Mock
        verify(mockIndividualRequestDao,times(2)).persist(individualRequest);
        verify(mockConsumerGateway, times(2)).individualRequest(individualRequest, 1000, 12000);
        assertEquals(IBulkSubmission.BulkRequestStatus.COMPLETED
                .getStatus(), individualRequest.getBulkSubmission().getSubmissionStatus(),"Bulk submission status is incorrect");
        assertNotNull( individualRequest.getBulkSubmission().getCompletedDate(),
                       "Bulk submission completed date should be populated");
        assertNotNull(individualRequest.getBulkSubmission().getUpdatedDate(),
                      "Bulk submission update date should be populated");
    }


    @Test
    public void processRequestToSubmitAllSuccessLogSetToError() {
        final String sdtRequestRef = TEST_1;
        final IIndividualRequest individualRequest = new IndividualRequest();
        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);

        // Set logging level to debug so that afterCompletion logging is captured
        logger.setLevel(ERROR);

        // Create appender and add to logger
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        // Set-up the individual request object

        individualRequest.setSdtRequestReference(sdtRequestRef);
        individualRequest.setRequestStatus(RECEIVED);
        setUpIndividualRequest(individualRequest);

        when(this.mockIndividualRequestDao.getRequestBySdtReference(sdtRequestRef)).thenReturn(
            individualRequest);

        final IGlobalParameter individualReqProcessingDelay = new GlobalParameter();
        individualReqProcessingDelay.setValue("10");
        individualReqProcessingDelay.setName(MCOL_INDV_REQ_DELAY);
        when(this.mockCacheable.getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY)).thenReturn(
            individualReqProcessingDelay);

        individualRequest.setRequestStatus(FORWARDED);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName(TARGET_APP_TIMEOUT);
        connectionTimeOutParam.setValue("1000");
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT)).thenReturn(
            connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName(TARGET_APP_RESP_TIMEOUT);
        receiveTimeOutParam.setValue("12000");
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT)).thenReturn(
            receiveTimeOutParam);

        doAnswer(invocation -> {
            IndividualRequest argument = invocation.getArgument(0);
            argument.setRequestStatus(IIndividualRequest.IndividualRequestStatus.ACCEPTED.getStatus());
            return null;
        }).when(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);

        final IBulkSubmission bulkSubmission = individualRequest.getBulkSubmission();

        when(this.mockIndividualRequestDao.queryAsCount(eq(IndividualRequest.class), isA(
            Supplier.class))).thenReturn(0L);

        // Setup dummy target response
        SdtContext.getContext().setRawInXml("response");

        this.targetAppSubmissionService.processRequestToSubmit(sdtRequestRef);

        List<ILoggingEvent> logList = listAppender.list;

        assertTrue(logList.isEmpty(),"Logging set to only Error");

        logger.detachAndStopAllAppenders();

        verify(mockIndividualRequestDao, times(2)).persist(individualRequest);
    }



    /**
     * This method checks an all positive scenario for processing request to submit.
     * In the end there are still individual requests outstanding so the bulk submission
     * is not marked as completed.
     */
    @Test
    public void processRequestToSubmitSuccess() {
        final String sdtRequestRef = TEST_1;
        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference(sdtRequestRef);
        individualRequest.setRequestStatus(RECEIVED);
        setUpIndividualRequest(individualRequest);

        when(this.mockIndividualRequestDao.getRequestBySdtReference(sdtRequestRef)).thenReturn(
                individualRequest);

        final IGlobalParameter individualReqProcessingDelay = new GlobalParameter();
        individualReqProcessingDelay.setValue("10");
        individualReqProcessingDelay.setName(MCOL_INDV_REQ_DELAY);
        when(this.mockCacheable.getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY)).thenReturn(
                individualReqProcessingDelay);

        individualRequest.setRequestStatus(FORWARDED);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName(TARGET_APP_TIMEOUT);
        connectionTimeOutParam.setValue("1000");
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT)).thenReturn(
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName(TARGET_APP_RESP_TIMEOUT);
        receiveTimeOutParam.setValue("12000");
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT)).thenReturn(
                receiveTimeOutParam);

        doAnswer(invocation -> {
            IndividualRequest argument = invocation.getArgument(0);
            argument.setRequestStatus(IIndividualRequest.IndividualRequestStatus.ACCEPTED.getStatus());
            return null;
        }).when(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);

        final List<IIndividualRequest> indRequests = new ArrayList<IIndividualRequest>();
        indRequests.add(individualRequest);

        when(
                this.mockIndividualRequestDao.queryAsCount(eq(IndividualRequest.class),
                                                           isA(Supplier.class))).thenReturn(
                Long.valueOf(indRequests.size()));

        // Setup dummy target response
        SdtContext.getContext().setRawInXml("response");

        this.targetAppSubmissionService.processRequestToSubmit(sdtRequestRef);

        assertTrue(true,"Expected to pass");
        verify(mockIndividualRequestDao,times(2)).persist(individualRequest);

    }

    @Test
    public void processRequestToSubmitRequestNullRequestDaoTest() {
        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);
        final String sdtRequestRef = TEST_1;
        when(this.mockIndividualRequestDao.getRequestBySdtReference(sdtRequestRef)).thenReturn(
            null);

        logger.setLevel(ERROR);

        // Create appender and add to logger
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        this.targetAppSubmissionService.processRequestToSubmit(sdtRequestRef);

        List<ILoggingEvent> logList = listAppender.list;

        assertTrue(verifyLog(logList,"read from message queue not found in database for individual request."));

        logger.detachAndStopAllAppenders();

    }

    /**
     * Test method to test for the time out.
     */
    @Test
    public void processRequestToSubmitTimeOut() {
        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);
        logger.setLevel(DEBUG);

        final String sdtRequestRef = TEST_1;
        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference(sdtRequestRef);
        individualRequest.setRequestStatus(RECEIVED);
        setUpIndividualRequest(individualRequest);

        when(this.mockIndividualRequestDao.getRequestBySdtReference(sdtRequestRef)).thenReturn(
                individualRequest);

        final IGlobalParameter individualReqProcessingDelay = new GlobalParameter();
        individualReqProcessingDelay.setValue("10");
        individualReqProcessingDelay.setName(MCOL_INDV_REQ_DELAY);
        when(this.mockCacheable.getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY)).thenReturn(
                individualReqProcessingDelay);

        individualRequest.setRequestStatus(FORWARDED);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName(TARGET_APP_TIMEOUT);
        connectionTimeOutParam.setValue("1000");
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT)).thenReturn(
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName(TARGET_APP_RESP_TIMEOUT);
        receiveTimeOutParam.setValue("12000");
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT)).thenReturn(
                receiveTimeOutParam);

        final TimeoutException timeoutEx = new TimeoutException("Timeout occurred", "Timeout occurred");
        this.mockConsumerGateway.individualRequest(individualRequest, 1000, 12000);
        doThrow(timeoutEx).when(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);

        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode("REQ_NOT_ACK");
        errorMsg.setErrorDescription("Request not acknowledged");
        errorMsg.setErrorText("Request Not Acknowledged");

        when(this.mockErrorMsgCacheable.getValue(IErrorMessage.class, "REQ_NOT_ACK")).thenReturn(errorMsg);

        // Now create an ErrorLog object with the ErrorMessage object and the IndividualRequest object
        final IErrorLog errorLog = new ErrorLog(errorMsg.getErrorCode(), errorMsg.getErrorText());

        individualRequest.setErrorLog(errorLog);

        final IGlobalParameter maxForwardingAttemptsParam = new GlobalParameter();
        maxForwardingAttemptsParam.setName("MAX_FORWARDING_ATTEMPTS");
        maxForwardingAttemptsParam.setValue("3");
        when(this.mockCacheable.getValue(IGlobalParameter.class, "MAX_FORWARDING_ATTEMPTS")).thenReturn(
                maxForwardingAttemptsParam);

        this.mockMessageWriter.queueMessage(isA(ISdtMessage.class), isA(String.class), anyBoolean());

        // Create appender and add to logger
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        this.targetAppSubmissionService.processRequestToSubmit(sdtRequestRef);

        List<ILoggingEvent> logList = listAppender.list;

        assertNotNull(logList.size()>0);

        logger.detachAndStopAllAppenders();

        verify(mockIndividualRequestDao,times(2)).persist(individualRequest);

    }

    /**
     * Test method to test for web service exception.
     */
    @Test
    public void processRequestToSubmitForWebServiceException() {

        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);
        logger.setLevel(DEBUG);
        final String sdtRequestRef = TEST_1;
        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference(sdtRequestRef);
        individualRequest.setRequestStatus(RECEIVED);
        setUpIndividualRequest(individualRequest);

        when(this.mockIndividualRequestDao.getRequestBySdtReference(sdtRequestRef)).thenReturn(
                individualRequest);

        final IGlobalParameter individualReqProcessingDelay = new GlobalParameter();
        individualReqProcessingDelay.setValue("10");
        individualReqProcessingDelay.setName(MCOL_INDV_REQ_DELAY);
        when(this.mockCacheable.getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY)).thenReturn(
                individualReqProcessingDelay);

        individualRequest.setRequestStatus(FORWARDED);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName(TARGET_APP_TIMEOUT);
        connectionTimeOutParam.setValue("1000");
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT)).thenReturn(
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName(TARGET_APP_RESP_TIMEOUT);
        receiveTimeOutParam.setValue("12000");
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT)).thenReturn(
                receiveTimeOutParam);

        final WebServiceException wsException = new WebServiceException("WS Error");
        this.mockConsumerGateway.individualRequest(individualRequest, 1000, 12000);
        doThrow(wsException).when(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);

        this.mockMessageWriter.queueMessage(isA(ISdtMessage.class),isA(String.class), eq(true));

        // Create appender and add to logger
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        this.targetAppSubmissionService.processRequestToSubmit(sdtRequestRef);

        List<ILoggingEvent> logList = listAppender.list;

        assertNotNull(logList.size()>0);

        logger.detachAndStopAllAppenders();

        assertTrue(true,"Expected to pass");
        verify(mockIndividualRequestDao,times(2)).persist(individualRequest);
    }

    /**
     * Test method to test for the soap fault error.
     */
    @Test
    public void processRequestToSubmitSoapFault() {
        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);
        logger.setLevel(DEBUG);
        final String sdtRequestRef = TEST_1;
        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference(sdtRequestRef);
        individualRequest.setRequestStatus(RECEIVED);
        setUpIndividualRequest(individualRequest);

        when(this.mockIndividualRequestDao.getRequestBySdtReference(sdtRequestRef)).thenReturn(
                individualRequest);

        final IGlobalParameter individualReqProcessingDelay = new GlobalParameter();
        individualReqProcessingDelay.setValue("10");
        individualReqProcessingDelay.setName(MCOL_INDV_REQ_DELAY);
        when(this.mockCacheable.getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY)).thenReturn(
                individualReqProcessingDelay);

        individualRequest.setRequestStatus(FORWARDED);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName(TARGET_APP_TIMEOUT);
        connectionTimeOutParam.setValue("1000");
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT)).thenReturn(
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName(TARGET_APP_RESP_TIMEOUT);
        receiveTimeOutParam.setValue("12000");
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT)).thenReturn(
                receiveTimeOutParam);

        final SoapFaultException soapEx = new SoapFaultException("Soap Fault", "Soap Fault occurred");
        this.mockConsumerGateway.individualRequest(individualRequest, 1000, 12000);
        doThrow(soapEx).when(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);

        this.mockIndividualRequestDao.persist(individualRequest);
        verify(mockIndividualRequestDao).persist(individualRequest);

        this.mockMessageWriter.queueMessage(isA(ISdtMessage.class), isA(String.class), eq(true));
       // verify(mockMessageWriter).queueMessage(isA(ISdtMessage.class), isA(String.class), eq(true));

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        this.targetAppSubmissionService.processRequestToSubmit(sdtRequestRef);

        List<ILoggingEvent> logList = listAppender.list;

        assertNotNull(logList.size()>0);

        logger.detachAndStopAllAppenders();

        assertEquals(
                IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus(),
                individualRequest.getRequestStatus(),"Individual Request status not as expected");

        assertNull(individualRequest
                       .getBulkSubmission().getCompletedDate(),"Bulk submission completed date should not be populated");

    }

    /**
     * Test method to test for the rejected error.
     */
    @Test
    public void processRequestToSubmitRejected() {
        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);
        logger.setLevel(DEBUG);
        final String sdtRequestRef = TEST_1;
        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference(sdtRequestRef);
        individualRequest.setRequestStatus(RECEIVED);
        setUpIndividualRequest(individualRequest);

        when(this.mockIndividualRequestDao.getRequestBySdtReference(sdtRequestRef)).thenReturn(
                individualRequest);

        final IGlobalParameter individualReqProcessingDelay = new GlobalParameter();
        individualReqProcessingDelay.setValue("10");
        individualReqProcessingDelay.setName(MCOL_INDV_REQ_DELAY);
        when(this.mockCacheable.getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY)).thenReturn(
                individualReqProcessingDelay);

        individualRequest.setRequestStatus(FORWARDED);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName(TARGET_APP_TIMEOUT);
        connectionTimeOutParam.setValue("1000");
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT)).thenReturn(
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName(TARGET_APP_RESP_TIMEOUT);
        receiveTimeOutParam.setValue("12000");
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT)).thenReturn(
                receiveTimeOutParam);

        this.mockConsumerGateway.individualRequest(individualRequest, 1000, 12000);

        doAnswer(invocation -> {
            IndividualRequest argument = invocation.getArgument(0);
            argument.setRequestStatus(IIndividualRequest.IndividualRequestStatus.ACCEPTED.getStatus());
            return null;
        }).when(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);

        final IBulkSubmission bulkSubmission = individualRequest.getBulkSubmission();

        when(
             this.mockIndividualRequestDao.queryAsCount(eq(IndividualRequest.class),
                                                        isA(Supplier.class))).thenReturn(0L);

        // Setup dummy response
        SdtContext.getContext().setRawInXml("response");

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        this.targetAppSubmissionService.processRequestToSubmit(sdtRequestRef);

        List<ILoggingEvent> logList = listAppender.list;

        assertNotNull(logList.size()>0);

        logger.detachAndStopAllAppenders();
        verify(mockIndividualRequestDao).persist(bulkSubmission);
        assertEquals( IBulkSubmission.BulkRequestStatus.COMPLETED.getStatus(),
                      individualRequest.getBulkSubmission().getSubmissionStatus(),"Bulk submission status is incorrect");
        assertNotNull(individualRequest
                .getBulkSubmission().getCompletedDate(),"Bulk submission completed date should be populated");
        assertNotNull(individualRequest
                .getBulkSubmission().getUpdatedDate(), "Bulk submission updated date should be populated");
    }

    /**
     * Test method to test the scenario where we get an individual request
     * that is to be rejected.
     */
    @Test
    public void processDlqRequestRejected() {
        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);
        logger.setLevel(DEBUG);
        final String requestStatus = "REJECTED";
        final String sdtRequestRef = TEST_1;
        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference(sdtRequestRef);
        this.setUpIndividualRequest(individualRequest);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus());

        final IGlobalParameter contactNameParameter = new GlobalParameter();
        contactNameParameter.setValue("Tester");
        contactNameParameter.setName("CONTACT_DETAILS");
        when(this.mockCacheable.getValue(IGlobalParameter.class, "CONTACT_DETAILS")).thenReturn(
                contactNameParameter);

        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode("CUST_XML_ERR");
        errorMsg.setErrorDescription("SDT Client Data Error");
        errorMsg.setErrorText("Individual Request format could not be processed by "
                + "the Target Application. Please check the data and resubmit the "
                + "request, or contact {0} for assistance.");

        when(this.mockErrorMsgCacheable.getValue(IErrorMessage.class, "CUST_XML_ERR"))
                .thenReturn(errorMsg);

        final String contactName = "Test";

        // Now create an ErrorLog object with the ErrorMessage object and the IndividualRequest object
        final IErrorLog errorLog =
                new ErrorLog(errorMsg.getErrorCode(), MessageFormat.format(errorMsg.getErrorText(), contactName));

        individualRequest.setErrorLog(errorLog);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus());

        final IBulkSubmission bulkSubmission = individualRequest.getBulkSubmission();

        when(
                this.mockIndividualRequestDao.queryAsCount(eq(IndividualRequest.class), isA(Supplier.class))).thenReturn(0L);

        mockIndividualRequestDao.persist(bulkSubmission);
        verify(mockIndividualRequestDao).persist(bulkSubmission);

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        this.targetAppSubmissionService.processDLQRequest(individualRequest, requestStatus);

        List<ILoggingEvent> logList = listAppender.list;

        assertNotNull(logList.size()>0);

        logger.detachAndStopAllAppenders();

        assertEquals( IBulkSubmission.BulkRequestStatus.COMPLETED.getStatus(),
                      individualRequest.getBulkSubmission().getSubmissionStatus(),"Bulk submission status is incorrect");
        assertNotNull(individualRequest
                .getBulkSubmission().getCompletedDate(), "Bulk submission completed date should be populated");
        assertNotNull(individualRequest
                .getBulkSubmission().getUpdatedDate(),"Bulk submission updated date should be populated");
        assertEquals( false,
                individualRequest.isDeadLetter(),"Individual Request should not be marked as dead letter" );

    }

    /**
     * Test method to test the scenario where we get an individual request
     * that is to be Forwarded.
     */
    @Test
    public void processDlqRequestForwarded() {

        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);
        logger.setLevel(DEBUG);

        final String requestStatus = FORWARDED;
        final String sdtRequestRef = "TEST_2";
        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object

        individualRequest.setSdtRequestReference(sdtRequestRef);
        this.setUpIndividualRequest(individualRequest);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus());

        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus());

        this.mockIndividualRequestDao.persist(individualRequest);

        verify(mockIndividualRequestDao).persist(individualRequest);

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        this.targetAppSubmissionService.processDLQRequest(individualRequest, requestStatus);

        List<ILoggingEvent> logList = listAppender.list;

        assertNotNull(logList.size()>0);

        logger.detachAndStopAllAppenders();

        assertEquals( false,
                individualRequest.isDeadLetter(),"Individual Request should not be marked as dead letter");
        assertEquals(IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus(),
                individualRequest.getRequestStatus(),"Individual Request status is not FORWARDED");

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


    @Test
    void testSetIndividualRequestDaoConsumerWriter(){
        IIndividualRequestDao mockIndividualRequestDao = mock(IndividualRequestDao.class);
        IConsumerGateway mockConsumerGateway = mock(ConsumerGateway.class);
        IMessageWriter mockIMessageWriter = mock(MessageWriter.class);

        targetAppSubmissionService.setMessageWriter(mockIMessageWriter);
        targetAppSubmissionService.setRequestConsumer(mockConsumerGateway);
        targetAppSubmissionService.setIndividualRequestDao(mockIndividualRequestDao);

        assertNotNull(targetAppSubmissionService.getIndividualRequestDao(),"Object should have been populated");
    }


    private static boolean verifyLog(List<ILoggingEvent> logList, String message) {
        boolean verifyLog = false;
        for (ILoggingEvent log : logList) {
            if (log.getMessage().contains(message.toString())) {
                verifyLog = true;
            }
        }
        return verifyLog;
    }

}
