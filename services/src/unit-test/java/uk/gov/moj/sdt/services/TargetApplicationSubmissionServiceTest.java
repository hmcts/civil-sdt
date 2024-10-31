package uk.gov.moj.sdt.services;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import uk.gov.moj.sdt.consumers.api.IConsumerGateway;
import uk.gov.moj.sdt.consumers.exception.SoapFaultException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.dao.api.IBulkSubmissionDao;
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
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.cmc.RequestTypeXmlNodeValidator;
import uk.gov.moj.sdt.utils.cmc.exception.CMCException;
import uk.gov.moj.sdt.utils.cmc.exception.CMCUnsupportedRequestTypeException;

import javax.xml.ws.WebServiceException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static ch.qos.logback.classic.Level.DEBUG;
import static ch.qos.logback.classic.Level.ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.moj.sdt.utils.cmc.RequestType.BREATHING_SPACE;
import static uk.gov.moj.sdt.utils.cmc.RequestType.CLAIM;
import static uk.gov.moj.sdt.utils.cmc.RequestType.JUDGMENT;

/**
 * Test class for TargetApplicationSubmissionService.
 *
 * @author Manoj Kulkarni
 */
@ExtendWith(MockitoExtension.class)
class TargetApplicationSubmissionServiceTest extends AbstractSdtUnitTestBase {

    private static final String TEST_1 = "TEST_1";

    private static final String MCOL_INDV_REQ_DELAY = "MCOL_INDV_REQ_DELAY";

    private static final String TARGET_APP_TIMEOUT = "TARGET_APP_TIMEOUT";

    private static final String TARGET_APP_RESP_TIMEOUT = "TARGET_APP_RESP_TIMEOUT";

    private static final String CONTACT_DETAILS = "CONTACT_DETAILS";

    private static final String TEN_MILLISECONDS = "10";

    private static final String ONE_THOUSAND_MILLISECONDS = "1000";

    private static final String TWELVE_THOUSAND_MILLISECONDS = "12000";

    private static final String DELAY_REQUEST_PROCESSING = "Delay request processing for 10 milliseconds";

    private static final String RESPONSE = "response";

    private static final String BULK_SUBMISSION_COMPLETED_DATE = "Bulk submission completed date should be populated";

    private static final String BULK_SUBMISSION_STATUS_IS_INCORRECT = "Bulk submission status is incorrect";

    private static final String REQ_NOT_ACK = "REQ_NOT_ACK";

    private static final String REQUEST_NOT_ACKNOWLEDGED = "Request Not Acknowledged";

    private static final String MAX_FORWARDING_ATTEMPTS = "MAX_FORWARDING_ATTEMPTS";

    private static final String TARGET_APP_CODE_MCOL = "MCOL";

    private static final String NODE_NAME_CLAIM_NUMBER = "claimNumber";

    private static final String CUST_XML_ERR_CODE = "CUST_XML_ERR";

    private static final String CUST_XML_ERR_DESCRIPTION = "SDT Client Data Error";

    private static final String CUST_XML_ERR_TEXT =
        "Individual Request format could not be processed by the Target Application. " +
            "Please check the data and resubmit the request, or contact {0} for assistance.";

    private static final String INVALID_CMC_REQUEST = "INVALID_CMC_REQUEST";

    private static final String REQUEST_PAYLOAD = "Test Xml";

    private static final String UNSUPPORTED_CMC_REQUEST_TYPE = "mcolSetAside";

    private static final String UNEXPECTED_INTERNAL_SYSTEM_ERROR =
        "Individual request has unexpected internal system error";

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
     * Mocked Bulk submission Dao object.
     */
    @Mock
    private IBulkSubmissionDao mockBulkSubmissionDao;

    /**
     * Mocked consumer gateway object.
     */
    @Mock
    private IConsumerGateway mockConsumerGateway;

    @Mock
    private IConsumerGateway mockCmcConsumerGateway;

    /**
     * The mocked ICacheable reference to the global parameters cache.
     */
    @Mock
    private ICacheable mockCacheable;

    /**
     * Mocked message writer reference.
     */
    @Mock
    private IMessageWriter mockMessageWriter;

    /**
     * The mocked ICacheable reference to the error message cache.
     */
    @Mock
    private ICacheable mockErrorMsgCacheable;

    @Mock
    private RequestTypeXmlNodeValidator requestTypeXmlNodeValidator;

    /**
     * Method to do any pre-test set-up.
     */
    @BeforeEach
    @Override
    public void setUp() {
        final GenericXmlParser genericParser = new GenericXmlParser();
        genericParser.setEnclosingTag("targetAppDetail");

        targetAppSubmissionService = new TargetApplicationSubmissionService(mockIndividualRequestDao,
                                                                            mockBulkSubmissionDao,
                                                                            genericParser,
                                                                            mockConsumerGateway,
                                                                            mockCmcConsumerGateway,
                                                                            mockMessageWriter,
                                                                            requestTypeXmlNodeValidator);
        targetAppSubmissionService.setGlobalParametersCache(mockCacheable);
        targetAppSubmissionService.setErrorMessagesCache(mockErrorMsgCacheable);
    }

    /**
     * This method checks an all positive scenario for processing request to submit.
     */
    @Test
    void processRequestToSubmitAllSuccess() {
        final IIndividualRequest individualRequest = new IndividualRequest();
        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);

        // Set logging level to debug so that afterCompletion logging is captured
        logger.setLevel(DEBUG);

        // Create appender and add to logger
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        // Set-up the individual request object
        individualRequest.setSdtRequestReference(TEST_1);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus());
        setUpIndividualRequest(individualRequest);

        when(this.mockIndividualRequestDao.getRequestBySdtReference(TEST_1)).thenReturn(
            individualRequest);

        final IGlobalParameter individualReqProcessingDelay = new GlobalParameter();
        individualReqProcessingDelay.setValue(TEN_MILLISECONDS);
        individualReqProcessingDelay.setName(MCOL_INDV_REQ_DELAY);
        when(this.mockCacheable.getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY)).thenReturn(
            individualReqProcessingDelay);

        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus());

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName(TARGET_APP_TIMEOUT);
        connectionTimeOutParam.setValue(ONE_THOUSAND_MILLISECONDS);
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT)).thenReturn(
            connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName(TARGET_APP_RESP_TIMEOUT);
        receiveTimeOutParam.setValue(TWELVE_THOUSAND_MILLISECONDS);
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT)).thenReturn(
            receiveTimeOutParam);

        doAnswer(invocation -> {
            IndividualRequest argument = invocation.getArgument(0);
            argument.setRequestStatus(IIndividualRequest.IndividualRequestStatus.ACCEPTED.getStatus());
            return null;
        }).when(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);

        when(this.mockIndividualRequestDao.queryAsCount(eq(IndividualRequest.class), isA(
            Supplier.class))).thenReturn(0L);

        // Setup dummy target response
        SdtContext.getContext().setRawInXml(RESPONSE);

        this.targetAppSubmissionService.processRequestToSubmit(TEST_1);

        List<ILoggingEvent> logList = listAppender.list;

        logger.detachAndStopAllAppenders();

        //Check that DEBUG logging is occurring
        assertTrue(verifyLog(logList, DELAY_REQUEST_PROCESSING));

        // Verify the Mock
        verify(mockIndividualRequestDao,times(2)).persist(individualRequest);
        verify(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);
        assertEquals(IBulkSubmission.BulkRequestStatus.COMPLETED.getStatus(),
                     individualRequest.getBulkSubmission().getSubmissionStatus(),
                     BULK_SUBMISSION_STATUS_IS_INCORRECT);
        assertNotNull( individualRequest.getBulkSubmission().getCompletedDate(),
                       BULK_SUBMISSION_COMPLETED_DATE);
        assertNotNull(individualRequest.getBulkSubmission().getUpdatedDate(),
                      BULK_SUBMISSION_COMPLETED_DATE);
    }

    @Test
    public void processRequestToSubmitAllSuccessLogSetToError() {
        final IIndividualRequest individualRequest = new IndividualRequest();
        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);

        // Set logging level to debug so that afterCompletion logging is captured
        logger.setLevel(ERROR);

        // Create appender and add to logger
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        // Set-up the individual request object
        individualRequest.setSdtRequestReference(TEST_1);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus());
        setUpIndividualRequest(individualRequest);

        when(this.mockIndividualRequestDao.getRequestBySdtReference(TEST_1)).thenReturn(
            individualRequest);

        mockGlobalParameterCache(TEN_MILLISECONDS, ONE_THOUSAND_MILLISECONDS, TWELVE_THOUSAND_MILLISECONDS);

        doAnswer(invocation -> {
            IndividualRequest argument = invocation.getArgument(0);
            argument.setRequestStatus(IIndividualRequest.IndividualRequestStatus.ACCEPTED.getStatus());
            return null;
        }).when(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);

        when(this.mockIndividualRequestDao.queryAsCount(eq(IndividualRequest.class), isA(
            Supplier.class))).thenReturn(0L);

        // Setup dummy target response
        SdtContext.getContext().setRawInXml(RESPONSE);

        this.targetAppSubmissionService.processRequestToSubmit(TEST_1);

        List<ILoggingEvent> logList = listAppender.list;

        assertTrue(logList.isEmpty(),"No log messages expected when log level is set to Error");

        logger.detachAndStopAllAppenders();

        verify(mockIndividualRequestDao, times(2)).persist(individualRequest);
        verify(mockCacheable).getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockIndividualRequestDao).getRequestBySdtReference(TEST_1);
        verify(mockIndividualRequestDao).queryAsCount(eq(IndividualRequest.class), isA(Supplier.class));
    }

    /**
     * This method checks an all positive scenario for processing request to submit.
     * In the end there are still individual requests outstanding so the bulk submission
     * is not marked as completed.
     */
    @Test
    void processRequestToSubmitSuccess() {
        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object
        individualRequest.setSdtRequestReference(TEST_1);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus());
        setUpIndividualRequest(individualRequest);

        when(this.mockIndividualRequestDao.getRequestBySdtReference(TEST_1)).thenReturn(
            individualRequest);

        mockGlobalParameterCache(TEN_MILLISECONDS, ONE_THOUSAND_MILLISECONDS, TWELVE_THOUSAND_MILLISECONDS);

        doAnswer(invocation -> {
            IndividualRequest argument = invocation.getArgument(0);
            argument.setRequestStatus(IIndividualRequest.IndividualRequestStatus.ACCEPTED.getStatus());
            return null;
        }).when(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);

        final List<IIndividualRequest> indRequests = new ArrayList<>();
        indRequests.add(individualRequest);

        when(this.mockIndividualRequestDao.queryAsCount(eq(IndividualRequest.class), isA(Supplier.class)))
            .thenReturn(Long.valueOf(indRequests.size()));

        // Setup dummy target response
        SdtContext.getContext().setRawInXml(RESPONSE);

        this.targetAppSubmissionService.processRequestToSubmit(TEST_1);

        verify(mockIndividualRequestDao, times(2)).persist(individualRequest);
        verify(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);
        verify(mockCacheable).getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockIndividualRequestDao).getRequestBySdtReference(TEST_1);
        verify(mockIndividualRequestDao).queryAsCount(eq(IndividualRequest.class), isA(Supplier.class));
    }

    @Test
    public void processRequestToSubmitRequestNullRequestDaoTest() {
        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);
        when(this.mockIndividualRequestDao.getRequestBySdtReference(TEST_1)).thenReturn(
            null);

        logger.setLevel(ERROR);

        // Create appender and add to logger
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        this.targetAppSubmissionService.processRequestToSubmit(TEST_1);

        List<ILoggingEvent> logList = listAppender.list;

        assertTrue(verifyLog(logList,"read from message queue not found in database for individual request."));

        logger.detachAndStopAllAppenders();
        verify(mockIndividualRequestDao).getRequestBySdtReference(TEST_1);
    }

    /**
     * Test method to test for the time out.
     */
    @Test
    void processRequestToSubmitTimeOut() {
        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);
        logger.setLevel(DEBUG);

        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object
        individualRequest.setSdtRequestReference(TEST_1);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus());
        setUpIndividualRequest(individualRequest);

        when(mockIndividualRequestDao.getRequestBySdtReference(TEST_1)).thenReturn(individualRequest);

        mockGlobalParameterCache(TEN_MILLISECONDS, ONE_THOUSAND_MILLISECONDS, TWELVE_THOUSAND_MILLISECONDS);

        final TimeoutException timeoutEx = new TimeoutException("TIMEOUT_ERROR", "Timeout occurred");
        doThrow(timeoutEx).when(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);

        final IErrorMessage errorMsg =
            expectErrorMessage(REQ_NOT_ACK, REQUEST_NOT_ACKNOWLEDGED, REQUEST_NOT_ACKNOWLEDGED);
        when(mockErrorMsgCacheable.getValue(IErrorMessage.class, REQ_NOT_ACK)).thenReturn(errorMsg);

        final IGlobalParameter maxForwardingAttemptsParam = new GlobalParameter();
        maxForwardingAttemptsParam.setName(MAX_FORWARDING_ATTEMPTS);
        maxForwardingAttemptsParam.setValue("3");
        when(mockCacheable.getValue(IGlobalParameter.class, MAX_FORWARDING_ATTEMPTS))
            .thenReturn(maxForwardingAttemptsParam);

        // Create appender and add to logger
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        this.targetAppSubmissionService.processRequestToSubmit(TEST_1);

        List<ILoggingEvent> logList = listAppender.list;

        assertFalse(logList.isEmpty());
        assertTrue(verifyLog(logList, DELAY_REQUEST_PROCESSING));
        assertTrue(verifyLog(logList, "Re-queuing request for SDT reference [TEST_1]"));

        logger.detachAndStopAllAppenders();

        assertEquals(REQUEST_NOT_ACKNOWLEDGED,
                     individualRequest.getInternalSystemError(),
                     UNEXPECTED_INTERNAL_SYSTEM_ERROR);

        verify(mockIndividualRequestDao,times(2)).persist(individualRequest);
        verify(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);
        verify(mockCacheable).getValue(IGlobalParameter.class, MAX_FORWARDING_ATTEMPTS);
        verify(mockErrorMsgCacheable).getValue(IErrorMessage.class, REQ_NOT_ACK);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockMessageWriter).queueMessage(any(ISdtMessage.class),any(String.class));
        verify(mockIndividualRequestDao).getRequestBySdtReference(TEST_1);
    }

    @Test
    void processRequestToSubmitTimeOutCmc() {
        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);
        logger.setLevel(DEBUG);

        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object
        individualRequest.setSdtRequestReference(TEST_1);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus());
        individualRequest.setRequestType(CLAIM.getType());
        setUpIndividualRequest(individualRequest);
        individualRequest.getBulkSubmission().getBulkCustomer().setReadyForAlternateService(true);

        when(mockIndividualRequestDao.getRequestBySdtReference(TEST_1)).thenReturn(individualRequest);

        mockGlobalParameterCache(TEN_MILLISECONDS, ONE_THOUSAND_MILLISECONDS, TWELVE_THOUSAND_MILLISECONDS);

        when(requestTypeXmlNodeValidator.isCMCClaimRequest(CLAIM.getType(), true)).thenReturn(true);

        final TimeoutException timeoutEx = new TimeoutException("TIMEOUT_ERROR", "Timeout occurred");
        doThrow(timeoutEx).when(mockCmcConsumerGateway).individualRequest(individualRequest, 1000, 12000);

        final IErrorMessage errorMsg =
            expectErrorMessage(REQ_NOT_ACK, REQUEST_NOT_ACKNOWLEDGED, REQUEST_NOT_ACKNOWLEDGED);
        when(mockErrorMsgCacheable.getValue(IErrorMessage.class, REQ_NOT_ACK)).thenReturn(errorMsg);

        final IGlobalParameter maxForwardingAttemptsParam = new GlobalParameter();
        maxForwardingAttemptsParam.setName(MAX_FORWARDING_ATTEMPTS);
        maxForwardingAttemptsParam.setValue("3");
        when(mockCacheable.getValue(IGlobalParameter.class, MAX_FORWARDING_ATTEMPTS)).thenReturn(
            maxForwardingAttemptsParam);

        // Create appender and add to logger
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        targetAppSubmissionService.processRequestToSubmit(TEST_1);

        List<ILoggingEvent> logList = listAppender.list;

        assertFalse(logList.isEmpty());
        assertTrue(verifyLog(logList, DELAY_REQUEST_PROCESSING));
        assertTrue(verifyLog(logList, "Re-queuing request for SDT reference [TEST_1]"));

        logger.detachAndStopAllAppenders();

        assertEquals(REQUEST_NOT_ACKNOWLEDGED,
                     individualRequest.getInternalSystemError(),
                     UNEXPECTED_INTERNAL_SYSTEM_ERROR);

        verify(mockIndividualRequestDao,times(2)).persist(individualRequest);
        verify(mockCmcConsumerGateway).individualRequest(individualRequest, 1000, 12000);
        verify(mockCacheable).getValue(IGlobalParameter.class, MAX_FORWARDING_ATTEMPTS);
        verify(mockErrorMsgCacheable).getValue(IErrorMessage.class, REQ_NOT_ACK);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockMessageWriter).queueMessage(any(ISdtMessage.class), any(String.class));
        verify(mockIndividualRequestDao).getRequestBySdtReference(TEST_1);
    }

    /**
     * Test method to test for web service exception.
     */
    @Test
    void processRequestToSubmitForWebServiceException() {
        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);
        logger.setLevel(DEBUG);
        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object
        individualRequest.setSdtRequestReference(TEST_1);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus());
        setUpIndividualRequest(individualRequest);

        when(this.mockIndividualRequestDao.getRequestBySdtReference(TEST_1)).thenReturn(
            individualRequest);

        final IGlobalParameter individualReqProcessingDelay = new GlobalParameter();
        individualReqProcessingDelay.setValue(TEN_MILLISECONDS);
        individualReqProcessingDelay.setName(MCOL_INDV_REQ_DELAY);
        when(this.mockCacheable.getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY)).thenReturn(
            individualReqProcessingDelay);

        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus());

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName(TARGET_APP_TIMEOUT);
        connectionTimeOutParam.setValue(ONE_THOUSAND_MILLISECONDS);
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT)).thenReturn(
            connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName(TARGET_APP_RESP_TIMEOUT);
        receiveTimeOutParam.setValue(TWELVE_THOUSAND_MILLISECONDS);
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT)).thenReturn(
            receiveTimeOutParam);

        final WebServiceException wsException = new WebServiceException("WS Error");

        doThrow(wsException).when(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);

        // Create appender and add to logger
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        this.targetAppSubmissionService.processRequestToSubmit(TEST_1);

        List<ILoggingEvent> logList = listAppender.list;
        assertFalse(logList.isEmpty());
        assertTrue(verifyLog(logList,"Exception calling target application for SDT reference [TEST_1] - WS Error"));

        logger.detachAndStopAllAppenders();

        verify(mockIndividualRequestDao,times(2)).persist(individualRequest);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockCacheable).getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY);
        verify(mockIndividualRequestDao).getRequestBySdtReference(TEST_1);
    }

    /**
     * Test method to test for the soap fault error.
     */
    @Test
    void processRequestToSubmitSoapFault() {
        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);
        logger.setLevel(DEBUG);

        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object
        individualRequest.setSdtRequestReference(TEST_1);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus());
        setUpIndividualRequest(individualRequest);

        when(this.mockIndividualRequestDao.getRequestBySdtReference(TEST_1)).thenReturn(
            individualRequest);

        mockGlobalParameterCache(TEN_MILLISECONDS, ONE_THOUSAND_MILLISECONDS, TWELVE_THOUSAND_MILLISECONDS);

        final SoapFaultException soapEx = new SoapFaultException("Soap Fault", "Soap Fault occurred");

        doThrow(soapEx).when(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        this.targetAppSubmissionService.processRequestToSubmit(TEST_1);

        List<ILoggingEvent> logList = listAppender.list;
        assertFalse(logList.isEmpty());
        assertTrue(verifyLog(logList,"with internal error and send to dead letter queue"));

        logger.detachAndStopAllAppenders();

        assertEquals(
            IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus(),
            individualRequest.getRequestStatus(),"Individual Request status not as expected");

        assertNull(individualRequest.getBulkSubmission().getCompletedDate(),
                   "Bulk submission completed date should not be populated");

        verify(mockIndividualRequestDao,times(2)).persist(individualRequest);
        verify(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockCacheable).getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY);
        verify(mockIndividualRequestDao).getRequestBySdtReference(TEST_1);
    }

    /**
     * Test method to test for the rejected error.
     */
    @Test
    void processRequestToSubmitRejected() {
        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);
        logger.setLevel(DEBUG);

        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object
        individualRequest.setSdtRequestReference(TEST_1);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus());
        setUpIndividualRequest(individualRequest);

        when(this.mockIndividualRequestDao.getRequestBySdtReference(TEST_1)).thenReturn(
            individualRequest);

        mockGlobalParameterCache(TEN_MILLISECONDS, ONE_THOUSAND_MILLISECONDS, TWELVE_THOUSAND_MILLISECONDS);

        doAnswer(invocation -> {
            IndividualRequest argument = invocation.getArgument(0);
            argument.setRequestStatus(IIndividualRequest.IndividualRequestStatus.ACCEPTED.getStatus());
            return null;
        }).when(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);

        final IBulkSubmission bulkSubmission = individualRequest.getBulkSubmission();

        // Setup dummy response
        SdtContext.getContext().setRawInXml(RESPONSE);

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        this.targetAppSubmissionService.processRequestToSubmit(TEST_1);

        List<ILoggingEvent> logList = listAppender.list;
        assertFalse(logList.isEmpty());
        assertTrue(verifyLog(logList,DELAY_REQUEST_PROCESSING));

        logger.detachAndStopAllAppenders();
        verify(mockBulkSubmissionDao).persist(bulkSubmission);
        assertEquals( IBulkSubmission.BulkRequestStatus.COMPLETED.getStatus(),
                      individualRequest.getBulkSubmission().getSubmissionStatus(),BULK_SUBMISSION_STATUS_IS_INCORRECT);
        assertNotNull(individualRequest
                          .getBulkSubmission().getCompletedDate(),BULK_SUBMISSION_COMPLETED_DATE);
        assertNotNull(individualRequest
                          .getBulkSubmission().getUpdatedDate(), "Bulk submission updated date should be populated");

        verify(mockConsumerGateway).individualRequest(individualRequest, 1000, 12000);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockCacheable).getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY);
        verify(mockIndividualRequestDao).getRequestBySdtReference(TEST_1);
        verify(mockIndividualRequestDao).queryAsCount(eq(IndividualRequest.class), isA(Supplier.class));
    }

    /**
     * Test method to test the scenario where we get an individual request
     * that is to be rejected.
     */
    @Test
    void processDlqRequestRejected() {
        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);
        logger.setLevel(DEBUG);
        final String requestStatus = "REJECTED";
        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object
        individualRequest.setSdtRequestReference(TEST_1);
        this.setUpIndividualRequest(individualRequest);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus());

        final IGlobalParameter contactNameParameter = new GlobalParameter();
        contactNameParameter.setValue("Tester");
        contactNameParameter.setName(CONTACT_DETAILS);
        when(this.mockCacheable.getValue(IGlobalParameter.class, CONTACT_DETAILS)).thenReturn(
            contactNameParameter);

        final IErrorMessage errorMsg =
            expectErrorMessage(CUST_XML_ERR_CODE, CUST_XML_ERR_DESCRIPTION, CUST_XML_ERR_TEXT);

        when(this.mockErrorMsgCacheable.getValue(IErrorMessage.class, CUST_XML_ERR_CODE))
            .thenReturn(errorMsg);

        final String contactName = "Test";

        // Now create an ErrorLog object with the ErrorMessage object and the IndividualRequest object
        final IErrorLog errorLog =
            new ErrorLog(errorMsg.getErrorCode(), MessageFormat.format(errorMsg.getErrorText(), contactName));

        individualRequest.setErrorLog(errorLog);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus());

        final IBulkSubmission bulkSubmission = individualRequest.getBulkSubmission();

        when(this.mockIndividualRequestDao.queryAsCount(eq(IndividualRequest.class), isA(Supplier.class)))
            .thenReturn(0L);

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        this.targetAppSubmissionService.processDLQRequest(individualRequest, requestStatus);

        List<ILoggingEvent> logList = listAppender.list;
        assertFalse(logList.isEmpty());
        assertTrue(verifyLog(logList,"REJECTED following Service team's investigation of DLQ Request"));
        logger.detachAndStopAllAppenders();

        assertEquals( IBulkSubmission.BulkRequestStatus.COMPLETED.getStatus(),
                      individualRequest.getBulkSubmission().getSubmissionStatus(),BULK_SUBMISSION_STATUS_IS_INCORRECT);
        assertNotNull(individualRequest
                          .getBulkSubmission().getCompletedDate(), BULK_SUBMISSION_COMPLETED_DATE);
        assertNotNull(individualRequest
                          .getBulkSubmission().getUpdatedDate(),"Bulk submission updated date should be populated");
        assertFalse( individualRequest.isDeadLetter(),"Individual Request should not be marked as dead letter" );

        verify(mockBulkSubmissionDao).persist(bulkSubmission);

        verify(mockCacheable).getValue(IGlobalParameter.class, CONTACT_DETAILS);
        verify(mockErrorMsgCacheable).getValue(IErrorMessage.class, CUST_XML_ERR_CODE);
        verify(mockIndividualRequestDao).queryAsCount(eq(IndividualRequest.class), isA(Supplier.class));
    }

    /**
     * Test method to test the scenario where we get an individual request
     * that is to be Forwarded.
     */
    @Test
    void processDlqRequestForwarded() {
        final String sdtRequestRef = "TEST_2";
        final IIndividualRequest individualRequest = new IndividualRequest();

        // Set-up the individual request object
        individualRequest.setSdtRequestReference(sdtRequestRef);
        this.setUpIndividualRequest(individualRequest);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus());

        targetAppSubmissionService.processDLQRequest(individualRequest,
                                                     IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus());

        assertFalse(individualRequest.isDeadLetter(),"Individual Request should not be marked as dead letter");
        assertEquals(IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus(),
                     individualRequest.getRequestStatus(),"Individual Request status is not FORWARDED");

        verify(mockIndividualRequestDao).persist(individualRequest);
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
        targetApp.setTargetApplicationCode(TARGET_APP_CODE_MCOL);
        targetApp.setTargetApplicationName("TEST_TargetApp");
        final Set<IServiceRouting> serviceRoutings = new HashSet<>();

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
        final List<IIndividualRequest> requests = new ArrayList<>();
        requests.add(request);

        bulkSubmission.setIndividualRequests(requests);

        request.setBulkSubmission(bulkSubmission);
        request.setRequestPayload(REQUEST_PAYLOAD.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void processCCDReferenceRequestToSubmitSuccess() {
        final IIndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setSdtRequestReference(TEST_1);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus());
        setUpIndividualRequest(individualRequest);
        individualRequest.setRequestType(JUDGMENT.getType());

        when(this.mockIndividualRequestDao.getRequestBySdtReference(TEST_1)).thenReturn(individualRequest);

        final IGlobalParameter individualReqProcessingDelay = new GlobalParameter();
        individualReqProcessingDelay.setValue(TEN_MILLISECONDS);
        individualReqProcessingDelay.setName(MCOL_INDV_REQ_DELAY);
        when(this.mockCacheable.getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY))
            .thenReturn(individualReqProcessingDelay);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName(TARGET_APP_TIMEOUT);
        connectionTimeOutParam.setValue(ONE_THOUSAND_MILLISECONDS);
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT))
            .thenReturn(connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName(TARGET_APP_RESP_TIMEOUT);
        receiveTimeOutParam.setValue(TWELVE_THOUSAND_MILLISECONDS);
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT))
            .thenReturn(receiveTimeOutParam);

        final List<IIndividualRequest> indRequests = new ArrayList<>();
        indRequests.add(individualRequest);

        when(this.mockIndividualRequestDao.queryAsCount(same(IndividualRequest.class), isA(Supplier.class)))
            .thenReturn(Long.valueOf(indRequests.size()));
        when(requestTypeXmlNodeValidator.isCMCRequestType(anyString(), anyString(), anyString(), anyBoolean()))
            .thenReturn(true);

        SdtContext.getContext().setRawInXml(RESPONSE);

        this.targetAppSubmissionService.processRequestToSubmit(TEST_1);
        verify(mockCmcConsumerGateway).individualRequest(any(IIndividualRequest.class), anyLong(), anyLong());
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockCacheable).getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY);
    }

    @Test
    void processCCDReferenceFailsOnUnSupportedRequest() {
        final IIndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setSdtRequestReference(TEST_1);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus());
        individualRequest.setCustomerRequestReference("CustReqRef1");
        setUpIndividualRequest(individualRequest);
        individualRequest.setRequestType(UNSUPPORTED_CMC_REQUEST_TYPE);
        individualRequest.getBulkSubmission().getBulkCustomer().setReadyForAlternateService(true);

        when(mockIndividualRequestDao.getRequestBySdtReference(TEST_1)).thenReturn(individualRequest);

        mockGlobalParameterCache(TEN_MILLISECONDS, ONE_THOUSAND_MILLISECONDS, TWELVE_THOUSAND_MILLISECONDS);

        when(requestTypeXmlNodeValidator.isCMCClaimRequest(UNSUPPORTED_CMC_REQUEST_TYPE, true)).thenReturn(false);
        CMCUnsupportedRequestTypeException cmcUnsupportedRequestTypeException =
            new CMCUnsupportedRequestTypeException("Request Type mcolSetAside not supported");
        when(requestTypeXmlNodeValidator.isCMCRequestType(UNSUPPORTED_CMC_REQUEST_TYPE,
                                                          REQUEST_PAYLOAD,
                                                          NODE_NAME_CLAIM_NUMBER,
                                                          true))
            .thenThrow(cmcUnsupportedRequestTypeException);
        when(requestTypeXmlNodeValidator.isCMCRequestType(UNSUPPORTED_CMC_REQUEST_TYPE,
                                                          REQUEST_PAYLOAD,
                                                          NODE_NAME_CLAIM_NUMBER,
                                                          false))
            .thenReturn(true);

        IErrorMessage errInvalidCmcRequest =
            expectErrorMessage(INVALID_CMC_REQUEST,
                               "Invalid CMC request type",
                               "Individual request {0} for CMC has an invalid request type {1}");
        when(mockErrorMsgCacheable.getValue(IErrorMessage.class, INVALID_CMC_REQUEST)).thenReturn(errInvalidCmcRequest);

        targetAppSubmissionService.processRequestToSubmit(TEST_1);

        assertEquals(IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus(),
                     individualRequest.getRequestStatus(),
                     "Individual request has unexpected request status");

        IErrorLog requestErrorLog = individualRequest.getErrorLog();
        assertNotNull(requestErrorLog, "Individual request should have an error log");
        assertEquals(INVALID_CMC_REQUEST, requestErrorLog.getErrorCode(), "Error log has unexpected error code");
        assertEquals("Individual request CustReqRef1 for CMC has an invalid request type mcolSetAside",
                     requestErrorLog.getErrorText(),
                     "Error log has unexpected text");

        verify(mockIndividualRequestDao).getRequestBySdtReference(TEST_1);
        verify(mockCacheable).getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockErrorMsgCacheable).getValue(IErrorMessage.class, INVALID_CMC_REQUEST);
        verify(mockIndividualRequestDao, times(2)).persist(individualRequest);
        verify(requestTypeXmlNodeValidator, times(2)).isCMCClaimRequest(UNSUPPORTED_CMC_REQUEST_TYPE, true);
        verify(requestTypeXmlNodeValidator).isCMCRequestType(UNSUPPORTED_CMC_REQUEST_TYPE,
                                                             REQUEST_PAYLOAD,
                                                             NODE_NAME_CLAIM_NUMBER,
                                                             true);
        verify(requestTypeXmlNodeValidator).isCMCRequestType(UNSUPPORTED_CMC_REQUEST_TYPE,
                                                             REQUEST_PAYLOAD,
                                                             NODE_NAME_CLAIM_NUMBER,
                                                             false);
        verify(mockCmcConsumerGateway, never()).individualRequest(any(IIndividualRequest.class), anyLong(), anyLong());
    }

    @Test
    void processRequestToSubmitCMCException() {
        IIndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setSdtRequestReference(TEST_1);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus());
        individualRequest.setRequestType(CLAIM.getType());
        setUpIndividualRequest(individualRequest);
        individualRequest.getBulkSubmission().getBulkCustomer().setReadyForAlternateService(true);

        when(mockIndividualRequestDao.getRequestBySdtReference(TEST_1)).thenReturn(individualRequest);

        mockGlobalParameterCache(TEN_MILLISECONDS, ONE_THOUSAND_MILLISECONDS, TWELVE_THOUSAND_MILLISECONDS);

        when(requestTypeXmlNodeValidator.isCMCClaimRequest(CLAIM.getType(), true)).thenReturn(true);

        CMCException cmcException = new CMCException("Unexpected CMC exception");
        doThrow(cmcException).when(mockCmcConsumerGateway).individualRequest(individualRequest, 1000, 12000);

        targetAppSubmissionService.processRequestToSubmit(TEST_1);

        assertEquals(IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus(),
                     individualRequest.getRequestStatus(),
                     "Individual request has unexpected status");
        assertEquals(1,
                     individualRequest.getForwardingAttempts(),
                     "Individual request has unexpected number of forwarding attempts");
        assertEquals("Unexpected CMC exception",
                     individualRequest.getInternalSystemError(),
                     UNEXPECTED_INTERNAL_SYSTEM_ERROR);
        assertTrue(individualRequest.isDeadLetter(), "Individual request dead letter flag should be set to true");

        verify(mockIndividualRequestDao).getRequestBySdtReference(TEST_1);
        verify(mockIndividualRequestDao, times(2)).persist(individualRequest);
        verify(requestTypeXmlNodeValidator).isCMCClaimRequest(CLAIM.getType(), true);
        verify(mockCmcConsumerGateway).individualRequest(individualRequest, 1000, 12000);
    }

    private void mockGlobalParameterCache(String individualRequestDelay,
                                          String targetAppTimeout,
                                          String targetAppResponseTimeout) {
        final IGlobalParameter individualReqProcessingDelay = new GlobalParameter();
        individualReqProcessingDelay.setValue(individualRequestDelay);
        individualReqProcessingDelay.setName(MCOL_INDV_REQ_DELAY);
        when(this.mockCacheable.getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY)).thenReturn(
            individualReqProcessingDelay);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName(TARGET_APP_TIMEOUT);
        connectionTimeOutParam.setValue(targetAppTimeout);
        when(this.mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT))
            .thenReturn(connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName(TARGET_APP_RESP_TIMEOUT);
        receiveTimeOutParam.setValue(targetAppResponseTimeout);
        when(this.mockCacheable.getValue(IGlobalParameter.class,
                                         TARGET_APP_RESP_TIMEOUT
        )).thenReturn(receiveTimeOutParam);
    }

    @Test
    void processClaimRequestWhenBulkCustomerReadyForAlternateService() {
        final IIndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setSdtRequestReference(TEST_1);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus());
        setUpIndividualRequest(individualRequest);
        individualRequest.setRequestType(CLAIM.getType());
        individualRequest.getBulkSubmission().getBulkCustomer().setReadyForAlternateService(true);

        mockGlobalParameterCache(TEN_MILLISECONDS, ONE_THOUSAND_MILLISECONDS, TWELVE_THOUSAND_MILLISECONDS);

        when(this.mockIndividualRequestDao.getRequestBySdtReference(TEST_1)).thenReturn(individualRequest);
        doCallRealMethod().when(requestTypeXmlNodeValidator).isCMCClaimRequest(CLAIM.getType(), true);

        this.targetAppSubmissionService.processRequestToSubmit(TEST_1);

        assertEquals(IIndividualRequest.IndividualRequestStatus.FORWARDED.getStatus(),
                     individualRequest.getRequestStatus(),
                     "Individual request has unexpected status");

        verify(mockCmcConsumerGateway).individualRequest(any(IIndividualRequest.class), anyLong(), anyLong());
        verify(requestTypeXmlNodeValidator, times(2)).isCMCClaimRequest(CLAIM.getType(), true);
        verify(mockIndividualRequestDao, times(2)).persist(individualRequest);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockCacheable).getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY);
    }

    @Test
    void processRequestToSubmitNullRequestPayload() {

        TargetApplication targetApplication = new TargetApplication();
        targetApplication.setTargetApplicationCode(TARGET_APP_CODE_MCOL);

        BulkCustomer bulkCustomer = new BulkCustomer();
        bulkCustomer.setReadyForAlternateService(false);

        BulkSubmission bulkSubmission = new BulkSubmission();
        bulkSubmission.setTargetApplication(targetApplication);
        bulkSubmission.setBulkCustomer(bulkCustomer);

        IndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setRequestType(BREATHING_SPACE.getType());
        individualRequest.setSdtRequestReference(TEST_1);
        individualRequest.setRequestPayload(null);
        individualRequest.setBulkSubmission(bulkSubmission);
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus());

        GlobalParameter mcolIndvReqDelay = new GlobalParameter();
        mcolIndvReqDelay.setValue(null);

        SdtContext.getContext().setRawInXml(RESPONSE);

        when(mockIndividualRequestDao.getRequestBySdtReference(TEST_1)).thenReturn(individualRequest);

        when(mockCacheable.getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY)).thenReturn(mcolIndvReqDelay);
        when(mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT)).thenReturn(null);
        when(mockCacheable.getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT)).thenReturn(null);

        when(requestTypeXmlNodeValidator.isCMCClaimRequest(BREATHING_SPACE.getType(), false))
            .thenReturn(false);
        when(requestTypeXmlNodeValidator.isCMCRequestType(BREATHING_SPACE.getType(), "", NODE_NAME_CLAIM_NUMBER, true))
            .thenReturn(false);

        when(mockIndividualRequestDao.queryAsCount(any(), any())).thenReturn(1L);

        targetAppSubmissionService.processRequestToSubmit(TEST_1);

        assertEquals(1, individualRequest.getForwardingAttempts(),
                     "Individual request has unexpected number of forwarding attempts");

        verify(mockIndividualRequestDao).getRequestBySdtReference(TEST_1);

        verify(mockCacheable).getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);

        verify(requestTypeXmlNodeValidator, times(2)).
            isCMCClaimRequest(BREATHING_SPACE.getType(), false);
        verify(requestTypeXmlNodeValidator).
            isCMCRequestType(BREATHING_SPACE.getType(), "", NODE_NAME_CLAIM_NUMBER, true);
        verify(requestTypeXmlNodeValidator).
            isCMCRequestType(BREATHING_SPACE.getType(), "", NODE_NAME_CLAIM_NUMBER, false);

        verify(mockIndividualRequestDao).queryAsCount(any(), any());

        verify(mockIndividualRequestDao, times(2)).persist(individualRequest);
        verify(mockConsumerGateway).individualRequest(individualRequest, 0, 0);
    }

    @ParameterizedTest
    @EnumSource(
        value = IIndividualRequest.IndividualRequestStatus.class,
        names = {"RECEIVED", "FORWARDED"},
        mode = EnumSource.Mode.EXCLUDE
    )
    void processRequestToSubmitAlreadyProcessed(IIndividualRequest.IndividualRequestStatus requestStatus) {
        Logger logger = (Logger) LoggerFactory.getLogger(TargetApplicationSubmissionService.class);

        String sdtRequestRef = "TestReqRef-" + requestStatus.getStatus();

        IIndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setSdtRequestReference(sdtRequestRef);
        individualRequest.setRequestStatus(requestStatus.getStatus());

        when(mockIndividualRequestDao.getRequestBySdtReference(sdtRequestRef)).thenReturn(individualRequest);

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        targetAppSubmissionService.processRequestToSubmit(sdtRequestRef);

        List<ILoggingEvent> logList = listAppender.list;
        assertFalse(logList.isEmpty(), "Log should not be empty");

        String expectedLogMessage = "SDT Reference " + sdtRequestRef + " already processed";
        assertTrue(verifyLog(logList, expectedLogMessage), "Log does not contain '" + expectedLogMessage + "'");

        logger.detachAndStopAllAppenders();

        assertEquals(requestStatus.getStatus(),
                     individualRequest.getRequestStatus(),
                     "Individual request status should not have been changed");

        verify(mockConsumerGateway, never()).individualRequest(any(IIndividualRequest.class), anyLong(), anyLong());
        verify(mockCmcConsumerGateway, never()).individualRequest(any(IIndividualRequest.class), anyLong(), anyLong());
    }

    @ParameterizedTest
    @EnumSource(value = IIndividualRequest.IndividualRequestStatus.class, names = {"RECEIVED", "FORWARDED"})
    void processRequestToSubmitNotAlreadyProcessed(IIndividualRequest.IndividualRequestStatus requestStatus) {
        IIndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setSdtRequestReference(TEST_1);
        individualRequest.setRequestStatus(requestStatus.getStatus());
        setUpIndividualRequest(individualRequest);
        individualRequest.setRequestType(CLAIM.getType());
        individualRequest.getBulkSubmission().getBulkCustomer().setReadyForAlternateService(false);

        SdtContext.getContext().setRawInXml(RESPONSE);

        when(mockIndividualRequestDao.getRequestBySdtReference(TEST_1)).thenReturn(individualRequest);

        mockGlobalParameterCache(TEN_MILLISECONDS, ONE_THOUSAND_MILLISECONDS, TWELVE_THOUSAND_MILLISECONDS);

        when(requestTypeXmlNodeValidator.isCMCClaimRequest(CLAIM.getType(), false)).thenReturn(false);

        targetAppSubmissionService.processRequestToSubmit(TEST_1);

        verify(mockIndividualRequestDao).getRequestBySdtReference(TEST_1);
        verify(mockCacheable).getValue(IGlobalParameter.class, MCOL_INDV_REQ_DELAY);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockCacheable).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockIndividualRequestDao, times(2)).persist(individualRequest);
        verify(mockConsumerGateway).individualRequest(any(IIndividualRequest.class), anyLong(), anyLong());
    }

    private static boolean verifyLog(List<ILoggingEvent> logList, String message) {
        boolean verifyLog = false;
        for (ILoggingEvent log : logList) {
            if (log.getFormattedMessage().contains(message)) {
                verifyLog = true;
                break;
            }
        }
        return verifyLog;
    }

    @NotNull
    private IErrorMessage expectErrorMessage(String errorCode, String errorDescription, String errorText) {
        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode(errorCode);
        errorMsg.setErrorDescription(errorDescription);
        errorMsg.setErrorText(errorText);

        return errorMsg;
    }
}
