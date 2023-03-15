package uk.gov.moj.sdt.cmc.consumers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.IBreathingSpaceService;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimRequestService;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimStatusUpdateService;
import uk.gov.moj.sdt.cmc.consumers.api.IJudgementService;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlConverter;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgementRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimStatusUpdateResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ProcessingStatus;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponse;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.utils.cmc.RequestType;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.moj.sdt.utils.cmc.RequestType.BREATHING_SPACE;
import static uk.gov.moj.sdt.utils.cmc.RequestType.CLAIM_STATUS_UPDATE;

/**
 * Test class for the consumer gateway.
 *
 * @author Mark Dathorne
 */
@ExtendWith(MockitoExtension.class)
class CMCConsumerGatewayTest {

    private static final long CONNECTION_TIME_OUT = 10;

    private static final long  RECEIVE_TIME_OUT = 10;

    private static final String XML = "";

    private static final String SDT_REFERENCE = "MCOL-0000001";

    private CMCConsumerGateway cmcConsumerGateway;

    @Mock
    private XmlConverter xmlToObject;

    @Mock
    private IBreathingSpaceService breathingSpace;

    @Mock
    private IJudgementService judgementService;

    @Mock
    private IClaimStatusUpdateService claimStatusUpdate;

    @Mock
    private IClaimRequestService claimRequestService;


    @BeforeEach
    public void setUpLocalTests() {
        cmcConsumerGateway = new CMCConsumerGateway(breathingSpace,
                                                    judgementService,
                                                    claimStatusUpdate,
                                                    claimRequestService,
                                                    xmlToObject);
    }

    @Test
    void shouldInvokeBreathingSpace() throws Exception {
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        setupMockBehaviour(BREATHING_SPACE, individualRequest);
        BreathingSpaceResponse response = new BreathingSpaceResponse();
        response.setProcessingStatus(ProcessingStatus.PROCESSED);
        when(breathingSpace.breathingSpace(anyString(), anyString(), any())).thenReturn(response);

        BreathingSpaceRequest breathingSpaceRequest = mock(BreathingSpaceRequest.class);
        when(individualRequest.getRequestPayload()).thenReturn(XML);
        when(individualRequest.getSdtRequestReference()).thenReturn("MCOL-0000001");
        when(xmlToObject.convertXmlToObject(anyString(), any())).thenReturn(breathingSpaceRequest);
        when(individualRequest.getRequestType()).thenReturn(RequestType.BREATHING_SPACE.getType());

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        verify(breathingSpace).breathingSpace(anyString(), anyString(), any(BreathingSpaceRequest.class));
        verify(xmlToObject).convertXmlToObject(anyString(), any());
        verify(individualRequest).getRequestPayload();
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
        when(claimStatusUpdate.claimStatusUpdate(anyString(), anyString(), any())).thenReturn(response);

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        verify(claimStatusUpdate).claimStatusUpdate(anyString(), anyString(), any(ClaimStatusUpdateRequest.class));
        verify(xmlToObject).convertXmlToObject(anyString(), any());
        verify(individualRequest).getRequestPayload();
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
        when(judgementService.requestJudgment(anyString(), any(), any())).thenReturn(response);

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        JudgementRequest judgementRequest = mock(JudgementRequest.class);
        when(individualRequest.getRequestPayload()).thenReturn(XML);
        when(individualRequest.getSdtRequestReference()).thenReturn("MCOL-0000001");
        when(xmlToObject.convertXmlToObject(anyString(), any())).thenReturn(judgementRequest);
        when(xmlToObject.convertObjectToXml(response)).thenReturn(XML);
        when(individualRequest.getRequestType()).thenReturn(RequestType.JUDGMENT.getType());

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        verify(judgementService).requestJudgment(anyString(), any(), any(JudgementRequest.class));
        verify(xmlToObject).convertXmlToObject(anyString(), any());
        verify(xmlToObject).convertObjectToXml(any(JudgementResponse.class));
        verify(individualRequest).getRequestPayload();
        verify(individualRequest).getSdtRequestReference();
        verify(individualRequest).getRequestType();
        verify(individualRequest).setTargetApplicationResponse(XML);
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
            BreathingSpaceRequest breathingSpaceRequest = mock(BreathingSpaceRequest.class);
            when(xmlToObject.convertXmlToObject(anyString(), any())).thenReturn(breathingSpaceRequest);
        } else if (requestType.equals(CLAIM_STATUS_UPDATE)) {
            ClaimStatusUpdateRequest claimStatusUpdateRequest = mock(ClaimStatusUpdateRequest.class);
            when(xmlToObject.convertXmlToObject(anyString(), any())).thenReturn(claimStatusUpdateRequest);
        }
    }

}
