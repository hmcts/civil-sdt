package uk.gov.moj.sdt.services;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResult;
import uk.gov.moj.sdt.cmc.consumers.util.ResponsesSummaryUtil;
import uk.gov.moj.sdt.consumers.api.IConsumerGateway;
import uk.gov.moj.sdt.consumers.exception.OutageException;
import uk.gov.moj.sdt.consumers.exception.SoapFaultException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkCustomerApplication;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.ErrorMessage;
import uk.gov.moj.sdt.domain.GlobalParameter;
import uk.gov.moj.sdt.domain.ServiceRouting;
import uk.gov.moj.sdt.domain.ServiceType;
import uk.gov.moj.sdt.domain.SubmitQueryRequest;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkCustomerApplication;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IErrorMessage;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.domain.api.IServiceRouting;
import uk.gov.moj.sdt.domain.api.IServiceType;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.response.SubmitQueryResponse;
import uk.gov.moj.sdt.services.utils.GenericXmlParser;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.SdtContext;

import javax.xml.ws.WebServiceException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static ch.qos.logback.classic.Level.DEBUG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for SubmitQueryService.
 *
 * @author 274994
 */
@ExtendWith(MockitoExtension.class)
class SubmitQueryServiceTest extends AbstractSdtUnitTestBase {
    /**
     * Submit Query Service object.
     */
    private SubmitQueryService submitQueryService;

    /**
     * Mocked consumer gateway object.
     */
    @Mock
    private IConsumerGateway mockConsumerGateway;

    @Mock
    private IConsumerGateway mockCmcConsumerGateway;

    /**
     * The mocked ICacheable reference for global parameters cache.
     */
    @Mock
    private ICacheable mockGlobalParamCache;

    /**
     * The mocked ICacheable reference to the error message cache.
     */
    @Mock
    private ICacheable mockErrorMsgCacheable;

    /**
     * The mocked bulk customer dao reference.
     */
    @Mock
    private IBulkCustomerDao mockBulkCustomerDao;

    @Mock
    private ResponsesSummaryUtil mockResponsesSummaryUtil;

    private static final String TARGET_APP_TIMEOUT = "TARGET_APP_TIMEOUT";

    private static final String ONE_THOUSAND = "1000";

    private static final String TARGET_APP_RESP_TIMEOUT = "TARGET_APP_RESP_TIMEOUT";

    private static final String TWELVE_THOUSAND = "12000";

    private static final String MCOL_MAX_CONCURRENT_QUERY_REQ = "MCOL_MAX_CONCURRENT_QUERY_REQ";

    private static final String DEFAULT_NUM_CONCURRENT_QUERY_REQS = "5";

    private static final String CONTACT_DETAILS = "CONTACT_DETAILS";

    private static final String TAR_APP_ERROR = "TAR_APP_ERROR";
    private static final String TAR_APP_BUSY = "TAR_APP_BUSY";

    private static final String CRITERIA = "criteria";

    private static final String RAW_OUTPUT_XML_SHOULD_BE_NULL = "Raw output xml should be null";

    private static final String SDT_INT_ERR = "SDT_INT_ERR";
    /**
     * Method to do any pre-test set-up.
     */
    @BeforeEach
    @Override
    public void setUp() {

        // Instantiate all the mocked objects and set them in the target

        final GenericXmlParser genericParser = new GenericXmlParser();
        genericParser.setEnclosingTag("targetAppDetail");

        submitQueryService = new SubmitQueryService(mockConsumerGateway,
                                                    mockCmcConsumerGateway,
                                                    mockGlobalParamCache,
                                                    mockErrorMsgCacheable,
                                                    genericParser,
                                                    genericParser,
                                                    mockBulkCustomerDao,
                                                    mockResponsesSummaryUtil
        );
    }

    /**
     * Unit test method to test for request timed out.
     */
    @Test
    void testSubmitQueryRequestTimeout() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        setUpSubmitQueryRequest(submitQueryRequest);

        setUpGlobalParam(TARGET_APP_TIMEOUT, ONE_THOUSAND);
        setUpGlobalParam(TARGET_APP_RESP_TIMEOUT, TWELVE_THOUSAND);
        setUpGlobalParam(MCOL_MAX_CONCURRENT_QUERY_REQ, DEFAULT_NUM_CONCURRENT_QUERY_REQS);
        setUpGlobalParam(CONTACT_DETAILS, "SDT Team");

        final TimeoutException timeoutEx = new TimeoutException("TIMEOUT_ERROR", "Timeout occurred");

        doThrow(timeoutEx).when(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);

        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode(TAR_APP_ERROR);
        errorMsg.setErrorDescription("Request timed out");
        errorMsg.setErrorText("The system encountered a problem.");

        when(this.mockErrorMsgCacheable.getValue(IErrorMessage.class, TAR_APP_ERROR)).thenReturn(errorMsg);

        SdtContext.getContext().setRawInXml(CRITERIA);
        this.submitQueryService.submitQuery(submitQueryRequest);

        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, MCOL_MAX_CONCURRENT_QUERY_REQ);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, CONTACT_DETAILS);
        verify(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);
        verify(mockErrorMsgCacheable).getValue(IErrorMessage.class, TAR_APP_ERROR);
        assertNull(SdtContext.getContext().getRawOutXml(), RAW_OUTPUT_XML_SHOULD_BE_NULL);
    }

    /**
     * Unit test method to test server outage exception.
     */
    @Test
    void testSubmitQueryRequestOutage() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        setUpSubmitQueryRequest(submitQueryRequest);

        setUpGlobalParam(TARGET_APP_TIMEOUT, ONE_THOUSAND);
        setUpGlobalParam(TARGET_APP_RESP_TIMEOUT, TWELVE_THOUSAND);
        setUpGlobalParam(MCOL_MAX_CONCURRENT_QUERY_REQ, DEFAULT_NUM_CONCURRENT_QUERY_REQS);
        setUpGlobalParam(CONTACT_DETAILS, "SDT Team");

        final OutageException outageEx = new OutageException("OUTAGE_ERROR", "Server unavailable.");

        doThrow(outageEx).when(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);

        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode(TAR_APP_ERROR);
        errorMsg.setErrorDescription("Server unavailable.");
        errorMsg.setErrorText("The system encountered a problem.");

        when(this.mockErrorMsgCacheable.getValue(IErrorMessage.class, TAR_APP_ERROR)).thenReturn(errorMsg);

        SdtContext.getContext().setRawInXml(CRITERIA);
        this.submitQueryService.submitQuery(submitQueryRequest);

        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, MCOL_MAX_CONCURRENT_QUERY_REQ);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, CONTACT_DETAILS);
        verify(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);
        verify(mockErrorMsgCacheable).getValue(IErrorMessage.class, TAR_APP_ERROR);

        assertNull(SdtContext.getContext().getRawOutXml(), RAW_OUTPUT_XML_SHOULD_BE_NULL);
    }

    /**
     * Unit test method to test server soap fault exception.
     */
    @Test
    void testSubmitQueryRequestSoapFault() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        setUpSubmitQueryRequest(submitQueryRequest);

        setUpGlobalParam(TARGET_APP_TIMEOUT, ONE_THOUSAND);
        setUpGlobalParam(TARGET_APP_RESP_TIMEOUT, TWELVE_THOUSAND);
        setUpGlobalParam(MCOL_MAX_CONCURRENT_QUERY_REQ, DEFAULT_NUM_CONCURRENT_QUERY_REQS);

        final SoapFaultException soapFaultEx = new SoapFaultException("SOAPFAULT_ERROR", "Soap fault occurred");

        doThrow(soapFaultEx).when(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);

        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode(SDT_INT_ERR);
        errorMsg.setErrorDescription("SDT Internal Error.");
        errorMsg.setErrorText("SDT Internal Error, please report to {0}");
        when(mockErrorMsgCacheable.getValue(IErrorMessage.class, SDT_INT_ERR)).thenReturn(errorMsg);

        setUpGlobalParam(CONTACT_DETAILS, "Tester");

        SdtContext.getContext().setRawInXml(CRITERIA);
        this.submitQueryService.submitQuery(submitQueryRequest);

        assertNull(SdtContext.getContext().getRawOutXml(), RAW_OUTPUT_XML_SHOULD_BE_NULL);

        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, MCOL_MAX_CONCURRENT_QUERY_REQ);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, CONTACT_DETAILS);
        verify(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);
        verify(mockErrorMsgCacheable).getValue(IErrorMessage.class, SDT_INT_ERR);

        assertNull(SdtContext.getContext().getRawOutXml(), RAW_OUTPUT_XML_SHOULD_BE_NULL);
        assertEquals("SDT Internal Error, please report to Tester", submitQueryRequest.getErrorLog()
                .getErrorText());
    }

    /**
     * Unit test method to test web service exception.
     */
    @Test
    void testSubmitQueryRequestForWebServiceException() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        setUpSubmitQueryRequest(submitQueryRequest);

        setUpGlobalParam(TARGET_APP_TIMEOUT, ONE_THOUSAND);
        setUpGlobalParam(TARGET_APP_RESP_TIMEOUT, TWELVE_THOUSAND);
        setUpGlobalParam(MCOL_MAX_CONCURRENT_QUERY_REQ, DEFAULT_NUM_CONCURRENT_QUERY_REQS);

        final WebServiceException wsException = new WebServiceException("WS_ERROR");

        doThrow(wsException).when(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);

        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode(SDT_INT_ERR);
        errorMsg.setErrorDescription("SDT Internal Error.");
        errorMsg.setErrorText("SDT Internal Error, please report to {0}");
        when(this.mockErrorMsgCacheable.getValue(IErrorMessage.class, SDT_INT_ERR)).thenReturn(errorMsg);

        setUpGlobalParam(CONTACT_DETAILS, "Tester");

        SdtContext.getContext().setRawInXml(CRITERIA);
        this.submitQueryService.submitQuery(submitQueryRequest);

        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, MCOL_MAX_CONCURRENT_QUERY_REQ);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, CONTACT_DETAILS);
        verify(mockErrorMsgCacheable).getValue(IErrorMessage.class, SDT_INT_ERR);
        verify(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);
        assertNull(SdtContext.getContext().getRawOutXml(), RAW_OUTPUT_XML_SHOULD_BE_NULL);

        assertEquals("SDT Internal Error, please report to Tester", submitQueryRequest.getErrorLog()
                .getErrorText());
    }

    /**
     * Unit test method to test server soap fault exception.
     */
    @Test
    void testSubmitQueryRequestThrottling() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        setUpSubmitQueryRequest(submitQueryRequest);

        setUpGlobalParam(MCOL_MAX_CONCURRENT_QUERY_REQ, "0");

        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode(TAR_APP_BUSY);
        errorMsg.setErrorDescription("Target Application Busy.");
        errorMsg.setErrorText("Target Application Busy.");
        when(mockErrorMsgCacheable.getValue(IErrorMessage.class, TAR_APP_BUSY))
                .thenReturn(errorMsg);

        SdtContext.getContext().setRawInXml(CRITERIA);
        this.submitQueryService.submitQuery(submitQueryRequest);

        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, MCOL_MAX_CONCURRENT_QUERY_REQ);
        verify(mockErrorMsgCacheable).getValue(IErrorMessage.class, TAR_APP_BUSY);

        assertNull(SdtContext.getContext().getRawOutXml(), RAW_OUTPUT_XML_SHOULD_BE_NULL);
    }

    /**
     * Unit test method to test submit query request success.
     */
    @Test
    void testSubmitQueryServiceSuccess() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();
        Logger logger = (Logger) LoggerFactory.getLogger(SubmitQueryService.class);
        setUpSubmitQueryRequest(submitQueryRequest);

        setUpGlobalParam(TARGET_APP_TIMEOUT, ONE_THOUSAND);
        setUpGlobalParam(TARGET_APP_RESP_TIMEOUT, TWELVE_THOUSAND);
        setUpGlobalParam(MCOL_MAX_CONCURRENT_QUERY_REQ, DEFAULT_NUM_CONCURRENT_QUERY_REQS);

        doAnswer(invocation -> {
            SubmitQueryRequest request = (SubmitQueryRequest) invocation.getArguments()[0];
            request.setStatus(ISubmitQueryRequest.Status.OK.getStatus());
            return null;
        }).when(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);

        // Set logging level to debug so that afterCompletion logging is captured
        logger.setLevel(DEBUG);

        // Create appender and add to logger
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        SdtContext.getContext().setRawInXml(CRITERIA);
        submitQueryService.submitQuery(submitQueryRequest);

        List<ILoggingEvent> logList = listAppender.list;

        assertTrue(verifyLog(logList,"Increment concurrent requests. Current requests in progress"));

        logger.detachAndStopAllAppenders();

        verify(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);

        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, MCOL_MAX_CONCURRENT_QUERY_REQ);
        verify(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);
    }

    @Test
    void testSubmitQueryCmc() {
        // Set up query request
        ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        IBulkCustomer requestBulkCustomer = new BulkCustomer();
        requestBulkCustomer.setSdtCustomerId(11L);
        submitQueryRequest.setBulkCustomer(requestBulkCustomer);

        ITargetApplication requestTargetApplication = new TargetApplication();
        requestTargetApplication.setTargetApplicationCode("MCOL");
        submitQueryRequest.setTargetApplication(requestTargetApplication);

        // Set up bulk customer to be returned by mockBulkConsumerDao
        setUpBulkCustomerCmc("MCOL", 11L);

        // Set up global parameters to be returned by mockGlobalParamCache
        setUpGlobalParam(MCOL_MAX_CONCURRENT_QUERY_REQ, DEFAULT_NUM_CONCURRENT_QUERY_REQS);
        setUpGlobalParam(TARGET_APP_TIMEOUT, ONE_THOUSAND);
        setUpGlobalParam(TARGET_APP_RESP_TIMEOUT, TWELVE_THOUSAND);

        // Set up mockConsumerGateway
        SubmitQueryResponse mcolSubmitQueryResponse = new SubmitQueryResponse();

        when(mockConsumerGateway.submitQuery(submitQueryRequest, 1000, 12000))
            .thenReturn(mcolSubmitQueryResponse);

        // Set up mockCmcConsumerGateway
        ClaimDefencesResult cmcClaimDefencesResult = new ClaimDefencesResult("casemanRef",
                                                                             "1",
                                                                             LocalDate.now(),
                                                                             LocalDateTime.now(),
                                                                             "responseType",
                                                                             "The defence");
        List<ClaimDefencesResult> cmcClaimDefencesResults = new ArrayList<>();
        cmcClaimDefencesResults.add(cmcClaimDefencesResult);
        SubmitQueryResponse cmcSubmitQueryResponse = new SubmitQueryResponse();
        cmcSubmitQueryResponse.setClaimDefencesResults(cmcClaimDefencesResults);
        cmcSubmitQueryResponse.setClaimDefencesResultsCount(cmcClaimDefencesResults.size());

        when(mockCmcConsumerGateway.submitQuery(submitQueryRequest, 1000, 12000))
            .thenReturn(cmcSubmitQueryResponse);

        SdtContext.getContext().setRawInXml(CRITERIA);

        submitQueryService.submitQuery(submitQueryRequest);

        assertEquals(1, submitQueryRequest.getResultCount(), "Query request has unexpected result count");

        verify(mockBulkCustomerDao).getBulkCustomerBySdtId(11L);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, MCOL_MAX_CONCURRENT_QUERY_REQ);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);
        verify(mockCmcConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);
        verify(mockResponsesSummaryUtil).getSummaryResults(mcolSubmitQueryResponse, cmcClaimDefencesResults);
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("noCmcClaimDefences")
    void testSubmitQueryCmcNoCmcClaimDefences(SubmitQueryResponse cmcSubmitQueryResponse) {
        // Set up query request
        ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        IBulkCustomer requestBulkCustomer = new BulkCustomer();
        requestBulkCustomer.setSdtCustomerId(12L);
        submitQueryRequest.setBulkCustomer(requestBulkCustomer);

        ITargetApplication requestTargetApplication = new TargetApplication();
        requestTargetApplication.setTargetApplicationCode("MCOL");
        submitQueryRequest.setTargetApplication(requestTargetApplication);

        // Set up bulk customer to be returned by mockBulkConsumerDao
        setUpBulkCustomerCmc("MCOL", 12L);

        // Set up global parameters to be returned by mockGlobalParamCache
        setUpGlobalParam(MCOL_MAX_CONCURRENT_QUERY_REQ, DEFAULT_NUM_CONCURRENT_QUERY_REQS);
        setUpGlobalParam(TARGET_APP_TIMEOUT, ONE_THOUSAND);
        setUpGlobalParam(TARGET_APP_RESP_TIMEOUT, TWELVE_THOUSAND);

        // Set up mockConsumerGateway
        SubmitQueryResponse mcolSubmitQueryResponse = new SubmitQueryResponse();

        when(mockConsumerGateway.submitQuery(submitQueryRequest, 1000, 12000))
            .thenReturn(mcolSubmitQueryResponse);

        // Set up mockCmcConsumerGateway
        when(mockCmcConsumerGateway.submitQuery(submitQueryRequest, 1000, 12000))
            .thenReturn(cmcSubmitQueryResponse);

        SdtContext.getContext().setRawInXml(CRITERIA);

        submitQueryService.submitQuery(submitQueryRequest);

        assertEquals(0, submitQueryRequest.getResultCount(), "Query request has unexpected result count");

        verify(mockBulkCustomerDao).getBulkCustomerBySdtId(12L);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, MCOL_MAX_CONCURRENT_QUERY_REQ);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);
        verify(mockCmcConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);
        verify(mockResponsesSummaryUtil, never()).getSummaryResults(any(), any());
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("noCmcClaimDefences")
    void testSubmitQueryMcolMaximumNumberOfDefencesReached(SubmitQueryResponse cmcSubmitQueryResponse) {
        // Set up query request
        ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        IBulkCustomer requestBulkCustomer = new BulkCustomer();
        requestBulkCustomer.setSdtCustomerId(12L);
        submitQueryRequest.setBulkCustomer(requestBulkCustomer);

        ITargetApplication requestTargetApplication = new TargetApplication();
        requestTargetApplication.setTargetApplicationCode("MCOL");
        submitQueryRequest.setTargetApplication(requestTargetApplication);
        submitQueryRequest.setResultCount(2000);
        IErrorLog errorLog = new ErrorLog();
        errorLog.setErrorCode("78");
        errorLog.setErrorText("maximum number of defences reached");
        submitQueryRequest.setErrorLog(errorLog);

        // Set up bulk customer to be returned by mockBulkConsumerDao
        setUpBulkCustomerCmc("MCOL", 12L);

        // Set up global parameters to be returned by mockGlobalParamCache
        setUpGlobalParam(MCOL_MAX_CONCURRENT_QUERY_REQ, DEFAULT_NUM_CONCURRENT_QUERY_REQS);
        setUpGlobalParam(TARGET_APP_TIMEOUT, ONE_THOUSAND);
        setUpGlobalParam(TARGET_APP_RESP_TIMEOUT, TWELVE_THOUSAND);


        when(mockConsumerGateway.submitQuery(submitQueryRequest, 1000, 12000))
            .thenReturn(cmcSubmitQueryResponse);

        SdtContext.getContext().setRawInXml(CRITERIA);

        submitQueryService.submitQuery(submitQueryRequest);

        assertEquals(2000, submitQueryRequest.getResultCount(), "Query request has unexpected result count");

        verify(mockBulkCustomerDao).getBulkCustomerBySdtId(12L);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, MCOL_MAX_CONCURRENT_QUERY_REQ);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT);
        verify(mockGlobalParamCache).getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT);
        verify(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);
        verify(mockCmcConsumerGateway, times(0)).submitQuery(submitQueryRequest, 1000, 12000);
        verify(mockResponsesSummaryUtil, never()).getSummaryResults(any(), any());
    }

    static Stream<Arguments> noCmcClaimDefences() {
        SubmitQueryResponse nullCmcClaimDefencesResponse = new SubmitQueryResponse();

        SubmitQueryResponse noCmcClaimDefencesResponse = new SubmitQueryResponse();
        List<ClaimDefencesResult> claimDefencesResults = new ArrayList<>();
        noCmcClaimDefencesResponse.setClaimDefencesResults(claimDefencesResults);
        noCmcClaimDefencesResponse.setClaimDefencesResultsCount(claimDefencesResults.size());

        return Stream.of(
            arguments(nullCmcClaimDefencesResponse),
            arguments(noCmcClaimDefencesResponse)
        );
    }

    @Test
    void setBulkCustomerDaoTest() {
        IBulkCustomerDao bulkCustomerDaoMock = mock(IBulkCustomerDao.class);
        submitQueryService.setBulkCustomerDao(bulkCustomerDaoMock);

        Object result = this.getAccessibleField(SubmitQueryService.class, "bulkCustomerDao",
                                                IBulkCustomerDao.class, submitQueryService);

        assertEquals(bulkCustomerDaoMock, result, "bulkCustomerDao should be correctly set");
    }

    @Test
    void setErrorMessageCacheableTest() {
        ICacheable errorMsgCacheableMock = mock(ICacheable.class);
        submitQueryService.setErrorMessagesCache(errorMsgCacheableMock);

        Object result = this.getAccessibleField(SubmitQueryService.class, "errorMessagesCache",
                                                ICacheable.class, submitQueryService);

        assertEquals(errorMsgCacheableMock, result, "ErrorMsgCacheable should be correctly set");
    }

    @Test
    void setGlobalParametersCacheTest() {
        ICacheable globalParametersCacheMock = mock(ICacheable.class);
        submitQueryService.setGlobalParametersCache(globalParametersCacheMock);

        Object result = this.getAccessibleField(SubmitQueryService.class, "globalParametersCache",
                                                ICacheable.class, submitQueryService);

        assertEquals(globalParametersCacheMock, result, "GlobalParameters should be correctly set");
    }

    @Test
    void setQueryResponseXmlParserTest() {
        GenericXmlParser genericResponseXmlParserMock = mock(GenericXmlParser.class);
        submitQueryService.setQueryResponseXmlParser(genericResponseXmlParserMock);

        Object result = this.getAccessibleField(SubmitQueryService.class, "queryResponseXmlParser",
                                                GenericXmlParser.class, submitQueryService);

        assertEquals(genericResponseXmlParserMock, result, "genericXmlParser should be correctly set");
    }

    @Test
    void setQueryRequestXmlParserTest() {
        GenericXmlParser genericRequestXmlParserMock = mock(GenericXmlParser.class);
        submitQueryService.setQueryRequestXmlParser(genericRequestXmlParserMock);

        Object result = this.getAccessibleField(SubmitQueryService.class, "queryRequestXmlParser",
                                                GenericXmlParser.class, submitQueryService);

        assertEquals(genericRequestXmlParserMock, result, "genericXmlParser should be correctly set");
    }

    @Test
    void setRequestConsumerTest() {
        IConsumerGateway consumerGatewayMock = mock(IConsumerGateway.class);
        submitQueryService.setRequestConsumer(consumerGatewayMock);

        Object result = this.getAccessibleField(SubmitQueryService.class, "requestConsumer",
                                                IConsumerGateway.class, submitQueryService);

        assertEquals(consumerGatewayMock, result, "consumerGateway should be correctly set");
    }

    /**
     * Set up a valid submit query request object.
     *
     * @param submitQueryRequest test object for submit query.
     */
    private void setUpSubmitQueryRequest(final ISubmitQueryRequest submitQueryRequest) {
        final long sdtCustomerId = 10L;
        final IBulkCustomer bulkCustomer = new BulkCustomer();
        bulkCustomer.setId(1L);
        bulkCustomer.setSdtCustomerId(10L);

        final ITargetApplication targetApp = new TargetApplication();

        targetApp.setId(1L);
        targetApp.setTargetApplicationCode("MCOL");
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

        final Set<IBulkCustomerApplication> bulkCustomerApplications = new HashSet<>();
        final BulkCustomerApplication bulkCustomerApplication = new BulkCustomerApplication();
        bulkCustomerApplication.setBulkCustomer(bulkCustomer);
        bulkCustomerApplication.setTargetApplication(targetApp);
        bulkCustomerApplications.add(bulkCustomerApplication);

        bulkCustomer.setBulkCustomerApplications(bulkCustomerApplications);

        when(this.mockBulkCustomerDao.getBulkCustomerBySdtId(sdtCustomerId)).thenReturn(bulkCustomer);

        final IBulkCustomer inBulkCustomer = new BulkCustomer();
        inBulkCustomer.setSdtCustomerId(10L);

        final ITargetApplication inTargetApp = new TargetApplication();
        inTargetApp.setTargetApplicationCode("mcol");

        submitQueryRequest.setTargetApplication(inTargetApp);
        submitQueryRequest.setBulkCustomer(inBulkCustomer);
    }

    private void setUpGlobalParam(String name, String value) {
        IGlobalParameter globalParam = new GlobalParameter();
        globalParam.setName(name);
        globalParam.setValue(value);

        when(mockGlobalParamCache.getValue(IGlobalParameter.class, name)).thenReturn(globalParam);
    }

    private void setUpBulkCustomerCmc(String targetAppCode, long sdtCustomerId) {
        ITargetApplication targetApplication = new TargetApplication();
        targetApplication.setTargetApplicationCode(targetAppCode);

        IBulkCustomerApplication bulkCustomerApplication = new BulkCustomerApplication();
        bulkCustomerApplication.setTargetApplication(targetApplication);

        Set<IBulkCustomerApplication> bulkCustomerApplications = new HashSet<>();
        bulkCustomerApplications.add(bulkCustomerApplication);

        IBulkCustomer bulkCustomer = new BulkCustomer();
        bulkCustomer.setSdtCustomerId(sdtCustomerId);
        bulkCustomer.setReadyForAlternateService(Boolean.TRUE);
        bulkCustomer.setBulkCustomerApplications(bulkCustomerApplications);

        when(mockBulkCustomerDao.getBulkCustomerBySdtId(sdtCustomerId)).thenReturn(bulkCustomer);
    }

    /**
     * Method to search for message within the Log list
     * @param logList The list of log messages
     * @param message The log message to check for
     * @return boolean Whether the log message is found in the list of log messages or not
     */
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
}
