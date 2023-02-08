
package uk.gov.moj.sdt.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import uk.gov.moj.sdt.consumers.api.IConsumerGateway;
import uk.gov.moj.sdt.consumers.exception.OutageException;
import uk.gov.moj.sdt.consumers.exception.SoapFaultException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkCustomerApplication;
import uk.gov.moj.sdt.domain.ErrorMessage;
import uk.gov.moj.sdt.domain.GlobalParameter;
import uk.gov.moj.sdt.domain.ServiceRouting;
import uk.gov.moj.sdt.domain.ServiceType;
import uk.gov.moj.sdt.domain.SubmitQueryRequest;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkCustomerApplication;
import uk.gov.moj.sdt.domain.api.IErrorMessage;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.domain.api.IServiceRouting;
import uk.gov.moj.sdt.domain.api.IServiceType;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.services.utils.GenericXmlParser;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.SdtContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.ws.WebServiceException;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import static ch.qos.logback.classic.Level.DEBUG;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Test class for SubmitQueryService.
 *
 * @author 274994
 */
@ExtendWith(MockitoExtension.class)
public class SubmitQueryServiceTest extends AbstractSdtUnitTestBase {
    /**
     * Submit Query Service object.
     */
    private SubmitQueryService submitQueryService;

    /**
     * Mocked consumer gateway object.
     */
    @Mock
    private IConsumerGateway mockConsumerGateway;

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

    private static final String TARGET_APP_TIMEOUT = "TARGET_APP_TIMEOUT";

    private static final String TARGET_APP_RESP_TIMEOUT = "TARGET_APP_RESP_TIMEOUT";
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
                                                    mockGlobalParamCache,
                                                    mockErrorMsgCacheable,
                                                    genericParser,
                                                    genericParser,
                                                    mockBulkCustomerDao);
        submitQueryService.setBulkCustomerDao(mockBulkCustomerDao);

        //To cover code coverage
        submitQueryService.setErrorMessagesCache(mockErrorMsgCacheable);
        submitQueryService.setGlobalParametersCache(mockGlobalParamCache);
        submitQueryService.setQueryResponseXmlParser(genericParser);
        submitQueryService.setQueryRequestXmlParser(genericParser);
        submitQueryService.setRequestConsumer(mockConsumerGateway);


    }

    /**
     * Unit test method to test for request timed out.
     */
    @Test
    public void testSubmitQueryRequestTimeout() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        setUpSubmitQueryRequest(submitQueryRequest);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName(TARGET_APP_TIMEOUT);
        connectionTimeOutParam.setValue("1000");
        when(mockGlobalParamCache.getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT)).thenReturn(
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName(TARGET_APP_RESP_TIMEOUT);
        receiveTimeOutParam.setValue("12000");
        when(mockGlobalParamCache.getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT))
                .thenReturn(receiveTimeOutParam);

        final IGlobalParameter maxQueryReq = new GlobalParameter();
        maxQueryReq.setName("MCOL_MAX_CONCURRENT_QUERY_REQ");
        maxQueryReq.setValue("5");
        when(mockGlobalParamCache.getValue(IGlobalParameter.class, "MCOL_MAX_CONCURRENT_QUERY_REQ"))
                .thenReturn(maxQueryReq);

        final IGlobalParameter contactDetails = new GlobalParameter();
        contactDetails.setName("CONTACT_DETAILS");
        contactDetails.setValue("SDT Team");
        when(mockGlobalParamCache.getValue(IGlobalParameter.class, "CONTACT_DETAILS")).thenReturn(
                contactDetails);

        final TimeoutException timeoutEx = new TimeoutException("TIMEOUT_ERROR", "Timeout occurred");
        mockConsumerGateway.submitQuery(submitQueryRequest, 1000, 12000);

        doThrow(timeoutEx).when(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);

        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode("TAR_APP_ERROR");
        errorMsg.setErrorDescription("Request timed out");
        errorMsg.setErrorText("The system encountered a problem.");

        when(this.mockErrorMsgCacheable.getValue(IErrorMessage.class, "TAR_APP_ERROR")).thenReturn(
                errorMsg);

        SdtContext.getContext().setRawInXml("criteria");
        this.submitQueryService.submitQuery(submitQueryRequest);

        assertEquals( null, SdtContext.getContext().getRawOutXml(),"Raw output xml should be null");

        assertTrue(true,"Expected to pass");
    }

    /**
     * Unit test method to test server outage exception.
     */
    @Test
    public void testSubmitQueryRequestOutage() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        setUpSubmitQueryRequest(submitQueryRequest);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName(TARGET_APP_TIMEOUT);
        connectionTimeOutParam.setValue("1000");
        when(this.mockGlobalParamCache.getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT)).thenReturn(
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName(TARGET_APP_RESP_TIMEOUT);
        receiveTimeOutParam.setValue("12000");
        when(this.mockGlobalParamCache.getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT))
                .thenReturn(receiveTimeOutParam);

        final IGlobalParameter maxQueryReq = new GlobalParameter();
        maxQueryReq.setName("MCOL_MAX_CONCURRENT_QUERY_REQ");
        maxQueryReq.setValue("5");
        when(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "MCOL_MAX_CONCURRENT_QUERY_REQ"))
                .thenReturn(maxQueryReq);

        final IGlobalParameter contactDetails = new GlobalParameter();
        contactDetails.setName("CONTACT_DETAILS");
        contactDetails.setValue("SDT Team");
        when(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "CONTACT_DETAILS")).thenReturn(
                contactDetails);

        final OutageException outageEx = new OutageException("OUTAGE_ERROR", "Server unavailable.");
        mockConsumerGateway.submitQuery(submitQueryRequest, 1000, 12000);

        doThrow(outageEx).when(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);

        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode("TAR_APP_ERROR");
        errorMsg.setErrorDescription("Server unavailable.");
        errorMsg.setErrorText("The system encountered a problem.");

        when(this.mockErrorMsgCacheable.getValue(IErrorMessage.class, "TAR_APP_ERROR")).thenReturn(
                errorMsg);

        SdtContext.getContext().setRawInXml("criteria");
        this.submitQueryService.submitQuery(submitQueryRequest);

        assertEquals(null, SdtContext.getContext().getRawOutXml(),"Raw output xml should be null");

        assertTrue(true,"Expected to pass");
    }

    /**
     * Unit test method to test server soap fault exception.
     */
    @Test
    public void testSubmitQueryRequestSoapFault() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        setUpSubmitQueryRequest(submitQueryRequest);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName(TARGET_APP_TIMEOUT);
        connectionTimeOutParam.setValue("1000");
        when(mockGlobalParamCache.getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT)).thenReturn(
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName(TARGET_APP_RESP_TIMEOUT);
        receiveTimeOutParam.setValue("12000");
        when(mockGlobalParamCache.getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT))
            .thenReturn(receiveTimeOutParam);

        final IGlobalParameter maxQueryReq = new GlobalParameter();
        maxQueryReq.setName("MCOL_MAX_CONCURRENT_QUERY_REQ");
        maxQueryReq.setValue("5");
        when(mockGlobalParamCache.getValue(IGlobalParameter.class, "MCOL_MAX_CONCURRENT_QUERY_REQ"))
                .thenReturn(maxQueryReq);

        final SoapFaultException soapFaultEx = new SoapFaultException("SOAPFAULT_ERROR", "Soap fault occurred");

        doThrow(soapFaultEx).when(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);

        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode("SDT_INT_ERR");
        errorMsg.setErrorDescription("SDT Internal Error.");
        errorMsg.setErrorText("SDT Internal Error, please report to {0}");
        when(mockErrorMsgCacheable.getValue(IErrorMessage.class, "SDT_INT_ERR")).thenReturn(errorMsg);

        final IGlobalParameter contactNameParameter = new GlobalParameter();
        contactNameParameter.setValue("Tester");
        contactNameParameter.setName("CONTACT_DETAILS");
        when(mockGlobalParamCache.getValue(IGlobalParameter.class, "CONTACT_DETAILS")).thenReturn(
                contactNameParameter);

        SdtContext.getContext().setRawInXml("criteria");
        this.submitQueryService.submitQuery(submitQueryRequest);

        assertNull(SdtContext.getContext().getRawOutXml(),"Raw output xml should be null");

        verify(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);
        assertTrue(true,"Expected to pass");

        assertEquals("SDT Internal Error, please report to Tester", submitQueryRequest.getErrorLog()
                .getErrorText());
    }

    /**
     * Unit test method to test web service exception.
     */
   @Test
    public void testSubmitQueryRequestForWebServiceException() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        setUpSubmitQueryRequest(submitQueryRequest);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName(TARGET_APP_TIMEOUT);
        connectionTimeOutParam.setValue("1000");
        when(this.mockGlobalParamCache.getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT)).thenReturn(
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName(TARGET_APP_RESP_TIMEOUT);
        receiveTimeOutParam.setValue("12000");
        when(this.mockGlobalParamCache.getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT))
                .thenReturn(receiveTimeOutParam);

        final IGlobalParameter maxQueryReq = new GlobalParameter();
        maxQueryReq.setName("MCOL_MAX_CONCURRENT_QUERY_REQ");
        maxQueryReq.setValue("5");
        when(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "MCOL_MAX_CONCURRENT_QUERY_REQ"))
                .thenReturn(maxQueryReq);

        final WebServiceException wsException = new WebServiceException("WS_ERROR");

        doThrow(wsException).when(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);

        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode("SDT_INT_ERR");
        errorMsg.setErrorDescription("SDT Internal Error.");
        errorMsg.setErrorText("SDT Internal Error, please report to {0}");
        when(this.mockErrorMsgCacheable.getValue(IErrorMessage.class, "SDT_INT_ERR")).thenReturn(errorMsg);

        final IGlobalParameter contactNameParameter = new GlobalParameter();
        contactNameParameter.setValue("Tester");
        contactNameParameter.setName("CONTACT_DETAILS");
        when(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "CONTACT_DETAILS")).thenReturn(
                contactNameParameter);

        SdtContext.getContext().setRawInXml("criteria");
        this.submitQueryService.submitQuery(submitQueryRequest);

        verify(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);
        assertNull(SdtContext.getContext().getRawOutXml(),"Raw output xml should be null");

        assertEquals("SDT Internal Error, please report to Tester", submitQueryRequest.getErrorLog()
                .getErrorText());
    }

    /**
     * Unit test method to test server soap fault exception.
     */
    @Test
    public void testSubmitQueryRequestThrottling() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        setUpSubmitQueryRequest(submitQueryRequest);

        final IGlobalParameter maxQueryReq = new GlobalParameter();
        maxQueryReq.setName("MCOL_MAX_CONCURRENT_QUERY_REQ");
        maxQueryReq.setValue("0");
        when(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "MCOL_MAX_CONCURRENT_QUERY_REQ"))
                .thenReturn(maxQueryReq);

        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode("TAR_APP_BUSY");
        errorMsg.setErrorDescription("Target Application Busy.");
        errorMsg.setErrorText("Target Application Busy.");
        when(mockErrorMsgCacheable.getValue(IErrorMessage.class, "TAR_APP_BUSY"))
                .thenReturn(errorMsg);

        SdtContext.getContext().setRawInXml("criteria");
        this.submitQueryService.submitQuery(submitQueryRequest);

        assertNull(SdtContext.getContext().getRawOutXml(),"Raw output xml should be null");
    }

    /**
     * Unit test method to test submit query request success.
     */
    @Test
    public void testSubmitQueryServiceSuccess() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();
        Logger logger = (Logger) LoggerFactory.getLogger(SubmitQueryService.class);
        setUpSubmitQueryRequest(submitQueryRequest);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName(TARGET_APP_TIMEOUT);
        connectionTimeOutParam.setValue("1000");
        when(mockGlobalParamCache.getValue(IGlobalParameter.class, TARGET_APP_TIMEOUT)).thenReturn(
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName(TARGET_APP_RESP_TIMEOUT);
        receiveTimeOutParam.setValue("12000");
        when(mockGlobalParamCache.getValue(IGlobalParameter.class, TARGET_APP_RESP_TIMEOUT))
                .thenReturn(receiveTimeOutParam);

        final IGlobalParameter maxQueryReq = new GlobalParameter();
        maxQueryReq.setName("MCOL_MAX_CONCURRENT_QUERY_REQ");
        maxQueryReq.setValue("5");
        when(mockGlobalParamCache.getValue(IGlobalParameter.class, "MCOL_MAX_CONCURRENT_QUERY_REQ"))
                .thenReturn(maxQueryReq);

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

        SdtContext.getContext().setRawInXml("criteria");
        submitQueryService.submitQuery(submitQueryRequest);

        List<ILoggingEvent> logList = listAppender.list;

        assertTrue(verifyLog(logList,"Increment concurrent requests. Current requests in progress"));

        logger.detachAndStopAllAppenders();

        verify(mockConsumerGateway).submitQuery(submitQueryRequest, 1000, 12000);

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

        final Set<IBulkCustomerApplication> bulkCustomerApplications = new HashSet<IBulkCustomerApplication>();
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

    /**
     * Method to search for message within the Log list
     * @param logList
     * @param message
     * @return Boolean
     */
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
