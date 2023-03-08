package uk.gov.moj.sdt.cmc.consumers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.IBreathingSpace;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimDefences;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimStatusUpdateService;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimStatusUpdateResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ProcessingStatus;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.utils.cmc.RequestType;
import uk.gov.moj.sdt.utils.cmc.xml.XmlElementValueReader;

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
    private XmlToObjectConverter xmlToObject;

    @Mock
    private IBreathingSpace breathingSpace;

    @Mock
    private IIndividualRequest individualRequest;

    @Mock
    private BreathingSpaceRequest breathingSpaceRequest;

    @Mock
    private IClaimDefences claimDefences;

    @Mock
    XmlElementValueReader xmlElementValueReader;

    @Mock
    private IClaimStatusUpdateService claimStatusUpdate;


    @BeforeEach
    public void setUpLocalTests() {
        cmcConsumerGateway = new CMCConsumerGateway(breathingSpace,
                                                    claimStatusUpdate,
                                                    claimDefences,
                                                    xmlToObject,
                                                    xmlElementValueReader);
    }

    @Test
    void shouldInvokeBreathingSpace() throws Exception {

        setupMockBehaviour(BREATHING_SPACE);
        BreathingSpaceResponse response = new BreathingSpaceResponse();
        response.setProcessingStatus(ProcessingStatus.PROCESSED);

        when(breathingSpace.breathingSpace(any())).thenReturn(response);
        when(individualRequest.getRequestPayload()).thenReturn(XML);
        when(individualRequest.getRequestType()).thenReturn(RequestType.BREATHING_SPACE.getType());
        when(xmlToObject.convertXmlToObject(anyString(), any())).thenReturn(breathingSpaceRequest);

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        verify(breathingSpace).breathingSpace(any(BreathingSpaceRequest.class));
        verify(xmlToObject).convertXmlToObject(anyString(), any());
        verify(individualRequest).getRequestPayload();
        verify(individualRequest).setRequestStatus(ProcessingStatus.PROCESSED.name());
    }

    @Test
    void shouldInvokeClaimStatusUpdate() throws Exception {
        setupMockBehaviour(CLAIM_STATUS_UPDATE);
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

    private void setupMockBehaviour(RequestType requestType) throws Exception {
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
