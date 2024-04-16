package uk.gov.moj.sdt.cmc.consumers;

import com.google.common.collect.Lists;
import feign.FeignException;
import org.jetbrains.annotations.NotNull;
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
import uk.gov.moj.sdt.cmc.consumers.response.ClaimStatusUpdateResponse;
import uk.gov.moj.sdt.cmc.consumers.response.JudgementWarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ProcessingStatus;
import uk.gov.moj.sdt.cmc.consumers.response.WarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponse;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.SubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.idam.IdamRepository;
import uk.gov.moj.sdt.idam.S2SRepository;
import uk.gov.moj.sdt.response.SubmitQueryResponse;
import uk.gov.moj.sdt.utils.cmc.RequestType;
import uk.gov.moj.sdt.utils.cmc.exception.CMCException;
import uk.gov.moj.sdt.utils.cmc.exception.CaseOffLineException;
import uk.gov.moj.sdt.utils.cmc.xml.XmlElementValueReader;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusCodeType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.xml.soap.SOAPException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

    private static final long  RECEIVE_TIME_OUT = 10;

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
        response.setProcessingStatus(ProcessingStatus.PROCESSED);
        when(breathingSpace.breathingSpace(any(), anyString(), any())).thenReturn(response);

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        verify(breathingSpace).breathingSpace(any(), anyString(), any(BreathingSpaceRequest.class));
        verify(xmlToObject).convertXmlToObject(anyString(), any());
        verify(individualRequest, times(2)).getRequestPayload();
        verify(individualRequest).setRequestStatus(ProcessingStatus.PROCESSED.name());
        verify(individualRequest).getSdtRequestReference();
        verify(individualRequest).getRequestType();
    }

    @Test
    void shouldInvokeClaimStatusUpdate() throws Exception {
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        setupMockBehaviour(CLAIM_STATUS_UPDATE, individualRequest);
        ClaimStatusUpdateResponse response = new ClaimStatusUpdateResponse();
        response.setProcessingStatus(ProcessingStatus.PROCESSED);
        when(claimStatusUpdate.claimStatusUpdate(any(), anyString(), any())).thenReturn(response);

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        verify(claimStatusUpdate).claimStatusUpdate(any(), anyString(), any(ClaimStatusUpdateRequest.class));
        verify(xmlToObject).convertXmlToObject(anyString(), any());
        verify(individualRequest, times(2)).getRequestPayload();
        verify(individualRequest).getRequestType();
        verify(individualRequest).getSdtRequestReference();
        verify(individualRequest).setRequestStatus(ProcessingStatus.PROCESSED.name());
    }

    @Test
    void shouldInvokeJudgementRequest() throws Exception {
        JudgementResponse response = new JudgementResponse();
        Date date = formattedDate();
        response.setJudgmentEnteredDate(date);
        response.setFirstPaymentDate(date);
        when(judgementService.requestJudgment(any(), any(), any())).thenReturn(response);

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        setupMockBehaviour(JUDGMENT, individualRequest);
        when(xmlToObject.convertObjectToXml(response)).thenReturn(new String(XML));

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        verify(judgementService).requestJudgment(any(), any(), any(JudgementRequest.class));
        verify(xmlToObject).convertXmlToObject(anyString(), any());
        verify(xmlToObject).convertObjectToXml(any(JudgementResponse.class));
        verify(individualRequest, times(2)).getRequestPayload();
        verify(individualRequest).getSdtRequestReference();
        verify(individualRequest).getRequestType();
        verify(individualRequest).setTargetApplicationResponse(XML);
    }

    @Test
    void shouldInvokeClaimRequest() throws Exception {
        ClaimResponse response = new ClaimResponse();
        Date date = formattedDate();
        response.setIssueDate(date);
        response.setServiceDate(date);
        response.setClaimNumber("MCOL-0000001");

        when(xmlToObject.convertObjectToXml(any())).thenReturn("");
        when(claimRequestService.claimRequest(any(), any(), any())).thenReturn(response);

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        setupMockBehaviour(CLAIM, individualRequest);

        when(individualRequest.getSdtRequestReference()).thenReturn("MCOL-0000001");

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        verify(claimRequestService).claimRequest(any(), any(), any(ClaimRequest.class));
        verify(xmlToObject).convertXmlToObject(anyString(), any());
        verify(xmlToObject).convertObjectToXml(any(ClaimResponse.class));
        verify(individualRequest, times(2)).getRequestPayload();
        verify(individualRequest).getSdtRequestReference();
        verify(individualRequest).getRequestType();
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
    void shouldThrowCaseOffLineWhenInvokingJudgementRequest() throws Exception {
        JudgementResponse response = new JudgementResponse();
        Date date = formattedDate();
        response.setJudgmentEnteredDate(date);
        response.setFirstPaymentDate(date);
        RuntimeException exception = new RuntimeException(CASE_OFF_LINE);
        doThrow(exception).when(judgementService).requestJudgment(any(), any(), any());

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        setupMockBehaviour(JUDGMENT, individualRequest);

        try {
            cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);
        } catch (CaseOffLineException coe) {
            assertEquals(CASE_OFF_LINE, coe.getMessage());
        }
    }

    @Test
    void shouldThrowTimeOutWhenInvokingJudgementRequest() throws Exception {
        JudgementResponse response = new JudgementResponse();
        Date date = formattedDate();
        response.setJudgmentEnteredDate(date);
        response.setFirstPaymentDate(date);
        RuntimeException exception = new RuntimeException("Timeout");
        doThrow(exception).when(judgementService).requestJudgment(any(), any(), any());

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        setupMockBehaviour(JUDGMENT, individualRequest);

        try {
            cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);
        } catch (CMCException ce) {
            assertEquals("Timeout", ce.getMessage());
        }
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
        ClaimDefencesResponse response = createClaimDefencesResponse();
        when(claimDefences.claimDefences(any(), any(), any(), anyString(), anyString())).thenReturn(response);
        SubmitQueryResponse submitQueryResponse =
            cmcConsumerGateway.submitQuery(submitQueryRequest, 1000, 1000);

        assertNotNull(submitQueryResponse);
        assertEquals(1, submitQueryResponse.getClaimDefencesResults().size());
        assertEquals(1, submitQueryResponse.getClaimDefencesResultsCount().intValue());
    }

    @NotNull
    private ClaimDefencesResponse createClaimDefencesResponse() {
        ClaimDefencesResponse response = new ClaimDefencesResponse();

        ClaimDefencesResult claimDefencesResult = new ClaimDefencesResult("CaseManRe123",
                                                                          "RespondandantId1",
                                                                          LocalDate.now(),
                                                                          LocalDateTime.now(),
                                                                          "ResponseType",
                                                                          "Defence");
        response.setResultCount(1);
        response.setResults(Lists.newArrayList(claimDefencesResult));
        return response;
    }

    @Test
    void shouldHandleNullRequestPayload() throws IOException {
        IIndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setSdtRequestReference(SDT_REFERENCE);
        individualRequest.setRequestType(BREATHING_SPACE.getType());

        BreathingSpaceResponse breathingSpaceResponse = new BreathingSpaceResponse();
        breathingSpaceResponse.setProcessingStatus(ProcessingStatus.PROCESSED);

        when(xmlToObject.convertXmlToObject("", BreathingSpaceRequest.class)).thenReturn(breathingSpaceRequest);
        when(breathingSpace.breathingSpace(null, SDT_REFERENCE, breathingSpaceRequest))
            .thenReturn(breathingSpaceResponse);

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        assertEquals(ProcessingStatus.PROCESSED.name(), individualRequest.getRequestStatus(),
                     "Request has unexpected processing status");

        verify(xmlToObject).convertXmlToObject("", BreathingSpaceRequest.class);
        verify(breathingSpace).breathingSpace(null, SDT_REFERENCE, breathingSpaceRequest);
    }

    /**
     * Test to verify submit query consumer does throw Feign exception.
     * But returns response with status code and error message from Feign Exception
     *
     * @throws feign.FeignException exception
     */
    @Test
    void shouldGetResponseWithHttpStatusAndExceptionMessageWhenFeignExceptionThrown() throws SOAPException {
        ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();
        ITargetApplication targetApplication = mock(ITargetApplication.class);
        submitQueryRequest.setTargetApplication(targetApplication);
        IBulkCustomer iBulkCustomer = mock(IBulkCustomer.class);
        submitQueryRequest.setBulkCustomer(iBulkCustomer);
        when(iBulkCustomer.getSdtCustomerId()).thenReturn(123567L);
        when(targetApplication.getTargetApplicationCode()).thenReturn("Code");
        when(xmlElementValueReader.getElementValue(any(), any())).thenReturn("2021-01-22");

        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(400);
        when(feignException.getMessage()).thenReturn("Error Processing Claim Defences Query");
        when(claimDefences.claimDefences(any(), any(), any(), anyString(), anyString())).thenThrow(feignException);
        SubmitQueryResponse submitQueryResponse =
            cmcConsumerGateway.submitQuery(submitQueryRequest, 1000, 1000);

        assertNotNull(submitQueryResponse);
        assertEquals(StatusCodeType.ERROR.value(), submitQueryRequest.getStatus());
        assertEquals(String.valueOf(400), submitQueryRequest.getErrorLog().getErrorCode());
        assertEquals("Error Processing Claim Defences Query", submitQueryRequest.getErrorLog().getErrorText());
    }

    @Test
    void shouldNotUpdateErrorMessageWhenMaxNumberOfResultsErrorAlreadyReceived() {
        ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();
        ITargetApplication targetApplication = mock(ITargetApplication.class);
        submitQueryRequest.setTargetApplication(targetApplication);
        IBulkCustomer iBulkCustomer = mock(IBulkCustomer.class);
        submitQueryRequest.setBulkCustomer(iBulkCustomer);
        final IErrorLog errorLog = new ErrorLog("78", "Max number of records reached");
        submitQueryRequest.reject(errorLog);
        when(iBulkCustomer.getSdtCustomerId()).thenReturn(123567L);
        when(targetApplication.getTargetApplicationCode()).thenReturn("Code");
        when(xmlElementValueReader.getElementValue(any(), any())).thenReturn("2021-01-22");

        ClaimDefencesResponse response = createClaimDefencesResponse();
        when(claimDefences.claimDefences(any(), any(), any(), anyString(), anyString())).thenReturn(response);

        SubmitQueryResponse submitQueryResponse =
            cmcConsumerGateway.submitQuery(submitQueryRequest, 1000, 1000);

        assertNotNull(submitQueryResponse);
        assertEquals("78", submitQueryRequest.getErrorLog().getErrorCode());
        assertEquals("Max number of records reached", submitQueryRequest.getErrorLog().getErrorText());
    }

    @Test
    void shouldUpdateErrorMessageWhenPositiveResponseFromAndNoMaxRecordsErrorAlreadyReceive() {
        ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();
        ITargetApplication targetApplication = mock(ITargetApplication.class);
        submitQueryRequest.setTargetApplication(targetApplication);
        IBulkCustomer iBulkCustomer = mock(IBulkCustomer.class);
        submitQueryRequest.setBulkCustomer(iBulkCustomer);
        final IErrorLog errorLog = new ErrorLog("77", "No claims found");
        submitQueryRequest.reject(errorLog);
        when(iBulkCustomer.getSdtCustomerId()).thenReturn(123567L);
        when(targetApplication.getTargetApplicationCode()).thenReturn("Code");
        when(xmlElementValueReader.getElementValue(any(), any())).thenReturn("2021-01-22");

        ClaimDefencesResponse response = createClaimDefencesResponse();
        when(claimDefences.claimDefences(any(), any(), any(), anyString(), anyString())).thenReturn(response);

        SubmitQueryResponse submitQueryResponse =
            cmcConsumerGateway.submitQuery(submitQueryRequest, 1000, 1000);

        assertNotNull(submitQueryResponse);
        assertNull(submitQueryRequest.getErrorLog());
        assertEquals(StatusCodeType.OK.value(), submitQueryRequest.getStatus());
    }

    @Test
    void shouldNotUpdateErrorStatusWhenNoResultsReturned() {
        ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();
        ITargetApplication targetApplication = mock(ITargetApplication.class);
        submitQueryRequest.setTargetApplication(targetApplication);
        IBulkCustomer iBulkCustomer = mock(IBulkCustomer.class);
        submitQueryRequest.setBulkCustomer(iBulkCustomer);
        submitQueryRequest.setErrorLog(null);
        when(iBulkCustomer.getSdtCustomerId()).thenReturn(123567L);
        when(targetApplication.getTargetApplicationCode()).thenReturn("Code");
        when(xmlElementValueReader.getElementValue(any(), any())).thenReturn("2021-01-22");

        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(77);
        when(claimDefences.claimDefences(any(), any(), any(), anyString(), anyString())).thenThrow(feignException);
        SubmitQueryResponse submitQueryResponse =
            cmcConsumerGateway.submitQuery(submitQueryRequest, 1000, 1000);

        assertNotNull(submitQueryResponse);
        assertNull(submitQueryRequest.getErrorLog());
    }

    @Test
    void shouldUpdateStatusWhenResultsReturned() {
        ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();
        ITargetApplication targetApplication = mock(ITargetApplication.class);
        submitQueryRequest.setTargetApplication(targetApplication);
        IBulkCustomer iBulkCustomer = mock(IBulkCustomer.class);
        submitQueryRequest.setBulkCustomer(iBulkCustomer);
        submitQueryRequest.setErrorLog(null);
        when(iBulkCustomer.getSdtCustomerId()).thenReturn(123567L);
        when(targetApplication.getTargetApplicationCode()).thenReturn("Code");
        when(xmlElementValueReader.getElementValue(any(), any())).thenReturn("2021-01-22");

        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(400);
        when(feignException.getMessage()).thenReturn("Error processing claim request");
        when(claimDefences.claimDefences(any(), any(), any(), anyString(), anyString())).thenThrow(feignException);
        SubmitQueryResponse submitQueryResponse =
            cmcConsumerGateway.submitQuery(submitQueryRequest, 1000, 1000);

        assertNotNull(submitQueryResponse);
        assertNotNull(submitQueryRequest.getErrorLog());
        assertEquals("400", submitQueryRequest.getErrorLog().getErrorCode());
        assertEquals("Error processing claim request", submitQueryRequest.getErrorLog().getErrorText());
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
