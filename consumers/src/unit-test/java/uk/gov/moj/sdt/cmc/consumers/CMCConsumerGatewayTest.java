package uk.gov.moj.sdt.cmc.consumers;

import com.google.common.collect.Lists;
import feign.Request;
import feign.RetryableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.IBreathingSpaceService;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimDefencesService;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimRequestService;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimStatusUpdateService;
import uk.gov.moj.sdt.cmc.consumers.api.IJudgementService;
import uk.gov.moj.sdt.cmc.consumers.api.IJudgementWarrantService;
import uk.gov.moj.sdt.cmc.consumers.api.IWarrantService;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlConverter;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResult;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.request.JudgementWarrantRequest;
import uk.gov.moj.sdt.cmc.consumers.request.WarrantRequest;
import uk.gov.moj.sdt.cmc.consumers.request.claim.ClaimRequest;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgementRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimResponseDetail;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimStatusUpdateResponse;
import uk.gov.moj.sdt.cmc.consumers.response.JudgementWarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ResponseStatus;
import uk.gov.moj.sdt.cmc.consumers.response.WarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponse;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponseDetail;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.idam.IdamRepository;
import uk.gov.moj.sdt.idam.S2SRepository;
import uk.gov.moj.sdt.response.SubmitQueryResponse;
import uk.gov.moj.sdt.utils.cmc.RequestType;
import uk.gov.moj.sdt.utils.cmc.xml.XmlElementValueReader;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.moj.sdt.utils.cmc.RequestType.BREATHING_SPACE;
import static uk.gov.moj.sdt.utils.cmc.RequestType.CLAIM;
import static uk.gov.moj.sdt.utils.cmc.RequestType.CLAIM_STATUS_UPDATE;
import static uk.gov.moj.sdt.utils.cmc.RequestType.JUDGMENT;
import static uk.gov.moj.sdt.utils.cmc.RequestType.JUDGMENT_WARRANT;
import static uk.gov.moj.sdt.utils.cmc.RequestType.WARRANT;
import static uk.gov.moj.sdt.utils.cmc.exception.CMCExceptionMessages.CASE_OFF_LINE;

/**
 * Test class for the consumer gateway.
 *
 * @author Mark Dathorne
 */
@ExtendWith(MockitoExtension.class)
class CMCConsumerGatewayTest {

    private static final long CONNECTION_TIME_OUT = 10;

    private static final long RECEIVE_TIME_OUT = 10;

    private static final byte[] XML = "".getBytes(StandardCharsets.UTF_8);

    private static final String SDT_REFERENCE = "MCOL-0000001";

    private static final String SDT_USER_AUTH_TOKEN = "sdt user token";

    private static final String S2S_TOKEN = "sds token";

    private CMCConsumerGateway cmcConsumerGateway;

    @Mock
    private XmlConverter xmlToObject;

    @Mock
    private IBreathingSpaceService breathingSpace;

    @Mock
    private IJudgementService judgementService;

    @Mock
    private BreathingSpaceRequest breathingSpaceRequest;

    @Mock
    private ClaimStatusUpdateRequest claimStatusUpdateRequest;

    @Mock
    private JudgementRequest judgementRequest;

    @Mock
    private ClaimRequest claimRequest;

    @Mock
    private IClaimDefencesService claimDefences;

    @Mock
    private IWarrantService warrantService;

    @Mock
    private IJudgementWarrantService judgementWarrantService;

    @Mock
    private XmlElementValueReader xmlElementValueReader;

    @Mock
    private IClaimStatusUpdateService claimStatusUpdate;

    @Mock
    private IClaimRequestService claimRequestService;
    @Mock
    private WarrantRequest warrantRequest;

    @Mock
    private JudgementWarrantRequest judgementWarrantRequest;

    @Mock
    private IdamRepository idamRepository;

    @Mock
    private S2SRepository s2SRepository;

    @BeforeEach
    public void setUpLocalTests() {
        cmcConsumerGateway = new CMCConsumerGateway(breathingSpace,
                                                    judgementService,
                                                    claimStatusUpdate,
                                                    claimRequestService,
                                                    claimDefences,
                                                    warrantService,
                                                    judgementWarrantService,
                                                    xmlToObject,
                                                    xmlElementValueReader,
                                                    idamRepository,
                                                    s2SRepository);
        when(idamRepository.getSdtSystemUserAccessToken()).thenReturn(SDT_USER_AUTH_TOKEN);
        when(s2SRepository.getS2SToken()).thenReturn(S2S_TOKEN);
    }

    @Test
    void shouldInvokeBreathingSpace() throws Exception {
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        setupMockBehaviour(BREATHING_SPACE, individualRequest);
        BreathingSpaceResponse response = new BreathingSpaceResponse();
        response.setResponseStatus(ResponseStatus.INITIALLY_ACCEPTED);
        when(breathingSpace.breathingSpace(any(), anyString(), any())).thenReturn(response);

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        verify(breathingSpace).breathingSpace(any(), anyString(), any(BreathingSpaceRequest.class));
        verify(xmlToObject).convertXmlToObject(anyString(), any());
        verify(individualRequest, times(2)).getRequestPayload();
        verify(individualRequest).markRequestAsInitiallyAccepted();
        verify(individualRequest).getSdtRequestReference();
        verify(individualRequest).getRequestType();
    }

    @Test
    void shouldInvokeClaimStatusUpdate() throws Exception {
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        setupMockBehaviour(CLAIM_STATUS_UPDATE, individualRequest);
        ClaimStatusUpdateResponse response = new ClaimStatusUpdateResponse();
        response.setResponseStatus(ResponseStatus.INITIALLY_ACCEPTED);
        when(claimStatusUpdate.claimStatusUpdate(any(), anyString(), any())).thenReturn(response);

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        verify(claimStatusUpdate).claimStatusUpdate(any(), anyString(), any(ClaimStatusUpdateRequest.class));
        verify(xmlToObject).convertXmlToObject(anyString(), any());
        verify(individualRequest, times(2)).getRequestPayload();
        verify(individualRequest).getRequestType();
        verify(individualRequest).getSdtRequestReference();
        verify(individualRequest).markRequestAsInitiallyAccepted();
    }

    @Test
    void shouldInvokeJudgementRequest() throws Exception {
        JudgementResponse response = new JudgementResponse();
        Date date = formattedDate();
        JudgementResponseDetail judgementResponseDetail = new JudgementResponseDetail();
        judgementResponseDetail.setJudgmentEnteredDate(date);
        judgementResponseDetail.setFirstPaymentDate(date);
        response.setJudgementResponseDetail(judgementResponseDetail);
        when(judgementService.requestJudgment(any(), any(), any())).thenReturn(response);

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        setupMockBehaviour(JUDGMENT, individualRequest);
        when(xmlToObject.convertObjectToXml(judgementResponseDetail)).thenReturn(new String(XML));

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        verify(judgementService).requestJudgment(any(), any(), any(JudgementRequest.class));
        verify(xmlToObject).convertXmlToObject(anyString(), any());
        verify(xmlToObject).convertObjectToXml(any(JudgementResponseDetail.class));
        verify(individualRequest, times(2)).getRequestPayload();
        verify(individualRequest).getSdtRequestReference();
        verify(individualRequest).getRequestType();
        verify(individualRequest).setTargetApplicationResponse(XML);
    }

    @Test
    void shouldInvokeClaimRequest() throws Exception {
        ClaimResponse response = new ClaimResponse();
        ClaimResponseDetail claimResponseDetail = new ClaimResponseDetail();
        Date date = formattedDate();
        claimResponseDetail.setIssueDate(date);
        claimResponseDetail.setServiceDate(date);
        claimResponseDetail.setClaimNumber("MCOL-0000001");
        response.setClaimResponseDetail(claimResponseDetail);

        when(xmlToObject.convertObjectToXml(any())).thenReturn("");
        when(claimRequestService.claimRequest(any(), any(), any())).thenReturn(response);

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        setupMockBehaviour(CLAIM, individualRequest);

        IBulkCustomer bulkCustomer = mock(IBulkCustomer.class);
        when(bulkCustomer.getSdtCustomerId()).thenReturn(10000001L);

        IBulkSubmission bulkSubmission = mock(IBulkSubmission.class);
        when(individualRequest.getBulkSubmission()).thenReturn(bulkSubmission);
        when(bulkSubmission.getBulkCustomer()).thenReturn(bulkCustomer);

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        verify(claimRequestService).claimRequest(any(), any(), any(ClaimRequest.class));
        verify(xmlToObject).convertXmlToObject(anyString(), any());
        verify(xmlToObject).convertObjectToXml(any(ClaimResponseDetail.class));
        verify(individualRequest, times(2)).getRequestPayload();
        verify(individualRequest).getSdtRequestReference();
        verify(individualRequest).getRequestType();
        verify(individualRequest).getBulkSubmission();
        verify(bulkSubmission).getBulkCustomer();
        verify(bulkCustomer).getSdtCustomerId();
        verify(individualRequest).setTargetApplicationResponse(XML);
    }

    @Test
    void shouldInvokeWarrantRequest() throws Exception {
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        setupMockBehaviour(WARRANT, individualRequest);
        WarrantResponse response = new WarrantResponse();

        when(xmlToObject.convertObjectToXml(any())).thenReturn("");
        when(warrantService.warrantRequest(anyString(), anyString(), any(), anyString(), any())).thenReturn(response);

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        verify(warrantService).warrantRequest(anyString(), anyString(), any(), anyString(), any(WarrantRequest.class));
        verify(xmlToObject).convertXmlToObject(anyString(), any());
        verify(individualRequest, times(2)).getRequestPayload();
        verify(individualRequest).getRequestType();
        verify(individualRequest).getSdtRequestReference();
    }

    @Test
    void shouldInvokeJudgementWarrantRequest() throws Exception {
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        setupMockBehaviour(JUDGMENT_WARRANT, individualRequest);
        JudgementWarrantResponse response = new JudgementWarrantResponse();

        when(xmlToObject.convertObjectToXml(any())).thenReturn("");
        when(judgementWarrantService.judgementWarrantRequest(anyString(), anyString(), any(), anyString(), any()))
            .thenReturn(response);

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        verify(judgementWarrantService)
            .judgementWarrantRequest(anyString(), anyString(), any(), anyString(), any(JudgementWarrantRequest.class));
        verify(xmlToObject).convertXmlToObject(anyString(), any());
        verify(individualRequest, times(2)).getRequestPayload();
        verify(individualRequest).getRequestType();
        verify(individualRequest).getSdtRequestReference();
    }

    @Test
    void shouldAddCaseOffLineErrorWhenInvokingJudgementRequest() throws Exception {
        JudgementResponse response = new JudgementResponse();
        Date date = formattedDate();
        JudgementResponseDetail judgementResponseDetail = new JudgementResponseDetail();
        judgementResponseDetail.setJudgmentEnteredDate(date);
        judgementResponseDetail.setFirstPaymentDate(date);
        response.setJudgementResponseDetail(judgementResponseDetail);
        RuntimeException exception = new RuntimeException(CASE_OFF_LINE);
        doThrow(exception).when(judgementService).requestJudgment(any(), any(), any());

        IIndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setRequestType(JUDGMENT.getType());

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);
        IErrorLog errorLog = individualRequest.getErrorLog();
        assertNotNull(errorLog, "Error log should not be null");
        assertEquals("200", errorLog.getErrorCode(), "Unexpected error code");
        assertEquals("Case is offline", errorLog.getErrorText(), "Unexpected error text");
    }

    @Test
    void shouldThrowTimeOutWhenInvokingJudgementRequest() throws Exception {
        SocketTimeoutException socketTimeoutException = new SocketTimeoutException("Timeout");
        Request request = mock(Request.class);
        RetryableException retryableException =
            new RetryableException(-1, "Timeout", Request.HttpMethod.POST, socketTimeoutException, null, request);
        doThrow(retryableException).when(judgementService).requestJudgment(any(), any(), any());

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        setupMockBehaviour(JUDGMENT, individualRequest);

        TimeoutException exception =
            assertThrows(TimeoutException.class,
                         () -> cmcConsumerGateway.individualRequest(individualRequest,
                                                                    CONNECTION_TIME_OUT,
                                                                    RECEIVE_TIME_OUT),
                         "Expected TimeoutException not thrown");
        assertEquals("TIMEOUT_ERROR", exception.getErrorCode(), "TimeoutException has unexpected error code");
        assertEquals("Read time out error sending [MCOL-0000001] to CMC",
                     exception.getErrorDescription(),
                     "TimeoutException has unexpected error description");
    }

    @Test
    void shouldReturnResponseForClaimDefences() {
        ISubmitQueryRequest submitQueryRequest = mock(ISubmitQueryRequest.class);
        ITargetApplication targetApplication = mock(ITargetApplication.class);
        when(submitQueryRequest.getTargetApplication()).thenReturn(targetApplication);
        IBulkCustomer iBulkCustomer = mock(IBulkCustomer.class);
        when(submitQueryRequest.getBulkCustomer()).thenReturn(iBulkCustomer);
        when(iBulkCustomer.getSdtCustomerId()).thenReturn(123567L);
        when(targetApplication.getTargetApplicationCode()).thenReturn("Code");
        when(xmlElementValueReader.getElementValue(any(), any())).thenReturn("2021-01-22");
        ClaimDefencesResponse response = new ClaimDefencesResponse();

        ClaimDefencesResult claimDefencesResult = new ClaimDefencesResult("CaseManRe123",
                                                                          "RespondandantId1",
                                                                          LocalDate.now(),
                                                                          LocalDateTime.now(),
                                                                          "ResponseType",
                                                                          "Defence");
        response.setResultCount(1);
        response.setResults(Lists.newArrayList(claimDefencesResult));
        when(claimDefences.claimDefences(any(), any(), any(), anyString(), anyString())).thenReturn(response);
        SubmitQueryResponse submitQueryResponse =
            cmcConsumerGateway.submitQuery(submitQueryRequest, 1000, 1000);

        assertNotNull(submitQueryResponse);
        assertEquals(1, submitQueryResponse.getClaimDefencesResults().size());
        assertEquals(1, submitQueryResponse.getClaimDefencesResultsCount().intValue());
    }

    @Test
    void shouldHandleNullRequestPayload() throws IOException {
        IIndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setSdtRequestReference(SDT_REFERENCE);
        individualRequest.setRequestType(BREATHING_SPACE.getType());

        BreathingSpaceResponse breathingSpaceResponse = new BreathingSpaceResponse();
        breathingSpaceResponse.setResponseStatus(ResponseStatus.INITIALLY_ACCEPTED);

        when(xmlToObject.convertXmlToObject("", BreathingSpaceRequest.class)).thenReturn(breathingSpaceRequest);
        when(breathingSpace.breathingSpace(null, SDT_REFERENCE, breathingSpaceRequest))
            .thenReturn(breathingSpaceResponse);

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        assertEquals(IIndividualRequest.IndividualRequestStatus.INITIALLY_ACCEPTED.getStatus(),
                     individualRequest.getRequestStatus(),
                     "Request has unexpected processing status");

        verify(xmlToObject).convertXmlToObject("", BreathingSpaceRequest.class);
        verify(breathingSpace).breathingSpace(null, SDT_REFERENCE, breathingSpaceRequest);
    }

    private Date formattedDate() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String date = formatter.format(Calendar.getInstance().getTime());
        return formatter.parse(date);
    }

    private void setupMockBehaviour(RequestType requestType, IIndividualRequest individualRequest) throws Exception {
        when(individualRequest.getSdtRequestReference()).thenReturn(SDT_REFERENCE);
        when(individualRequest.getRequestPayload()).thenReturn(XML);
        when(individualRequest.getRequestType()).thenReturn(requestType.getType());
        if (requestType.equals(BREATHING_SPACE)) {
            when(xmlToObject.convertXmlToObject(anyString(), any())).thenReturn(breathingSpaceRequest);
        } else if (requestType.equals(CLAIM_STATUS_UPDATE)) {
            when(xmlToObject.convertXmlToObject(anyString(), any())).thenReturn(claimStatusUpdateRequest);
        } else if (requestType.equals(JUDGMENT)) {
            when(xmlToObject.convertXmlToObject(anyString(), any())).thenReturn(judgementRequest);
        } else if (requestType.equals(CLAIM)) {
            when(xmlToObject.convertXmlToObject(anyString(), any())).thenReturn(claimRequest);
        } else if (requestType.equals(WARRANT)) {
            when(xmlToObject.convertXmlToObject(anyString(), any())).thenReturn(warrantRequest);
        } else if (requestType.equals(JUDGMENT_WARRANT)) {
            when(xmlToObject.convertXmlToObject(anyString(), any())).thenReturn(judgementWarrantRequest);
        }
    }

}
