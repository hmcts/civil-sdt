package uk.gov.moj.sdt.cmc.consumers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.IBreathingSpace;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimStatusUpdate;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ProcessingStatus;
import uk.gov.moj.sdt.domain.RequestType;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private CMCConsumerGateway cmcConsumerGateway;

    @Mock
    private XmlToObjectConverter xmlToObject;

    @Mock
    private IBreathingSpace breathingSpace;

    @Mock
    private IIndividualRequest individualRequest;

    private IClaimStatusUpdate claimStatusUpdate;

    @Mock
    RequestType requestTypeMock;

    @BeforeEach
    public void setUpLocalTests() {
        xmlToObject = mock(XmlToObjectConverter.class);
        breathingSpace = mock(IBreathingSpace.class);
        individualRequest = mock(IIndividualRequest.class);
        //cmcConsumerGateway = new CMCConsumerGateway(breathingSpace, xmlToObject);
        mockXmlToObject();
    }

    @Test
    void shouldInvokeBreathingSpace() throws Exception {
        BreathingSpaceResponse response = new BreathingSpaceResponse();
        response.setProcessingStatus(ProcessingStatus.PROCESSED);
        when(breathingSpace.breathingSpace(any())).thenReturn(response);
        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);
        verify(breathingSpace).breathingSpace(any(BreathingSpaceRequest.class));
        verify(xmlToObject).convertXmlToObject(anyString(), any());
        verify(individualRequest).getRequestPayload();
    }
//@Test
   // void shouldInvokeClaimStatusUpdate() {
   // ClaimStatusUpdateRequest ClaimStatusUpdateRequest = mock(ClaimStatusUpdateRequest.class);
    //    mockXmlToObject(ClaimStatusUpdateRequest);
    //    when(individualRequest.getRequestType()).thenReturn("mcolClaimStatusUpdate");
    //    cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);
    //    verify(claimStatusUpdate).claimStatusUpdate(any(ClaimStatusUpdateRequest.class));
   // }

    private void mockXmlToObject() {
        BreathingSpaceRequest breathingSpaceRequest = mock(BreathingSpaceRequest.class);
        when(individualRequest.getRequestPayload()).thenReturn(XML);
        try {
            when(xmlToObject.convertXmlToObject(anyString(), any())).thenReturn(breathingSpaceRequest);
        } catch (IOException e) {
        }
    }

}
