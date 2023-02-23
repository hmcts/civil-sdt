package uk.gov.moj.sdt.cmc.consumers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.IBreathingSpace;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimStatusUpdate;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.model.claimStatusUpdate.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.model.breathingspace.BreathingSpaceRequest;
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

    private XmlToObjectConverter xmlToObject;

    private IBreathingSpace breathingSpace;

    private IIndividualRequest individualRequest;

    private IClaimStatusUpdate claimStatusUpdate;

    @Mock
    RequestType requestTypeMock;

    @BeforeEach
    public void setUpLocalTests() {
        xmlToObject = mock(XmlToObjectConverter.class);
        breathingSpace = mock(IBreathingSpace.class);
        individualRequest = mock(IIndividualRequest.class);
        claimStatusUpdate = mock(IClaimStatusUpdate.class);
        cmcConsumerGateway = new CMCConsumerGateway(breathingSpace, claimStatusUpdate, xmlToObject);
        //mockXmlToObject();
    }


    @Test
    void shouldInvokeBreathingSpace() {
        BreathingSpaceRequest breathingSpaceRequest = mock(BreathingSpaceRequest.class);
        mockXmlToObject(breathingSpaceRequest);
        when(individualRequest.getRequestType()).thenReturn("mcolBreathingSpace");
        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);
        verify(breathingSpace).breathingSpace(any(BreathingSpaceRequest.class));
    }
@Test
    void shouldInvokeClaimStatusUpdate() {
    ClaimStatusUpdateRequest ClaimStatusUpdateRequest = mock(ClaimStatusUpdateRequest.class);
        mockXmlToObject(ClaimStatusUpdateRequest);
        when(individualRequest.getRequestType()).thenReturn("mcolClaimStatusUpdate");
        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);
        verify(claimStatusUpdate).claimStatusUpdate(any(ClaimStatusUpdateRequest.class));
    }

    private void mockXmlToObject(Object obj) {
        //BreathingSpaceRequest breathingSpaceRequest = mock(BreathingSpaceRequest.class);
        when(individualRequest.getRequestPayload()).thenReturn(XML);
        try {
            when(xmlToObject.convertXmlToObject(anyString(), any())).thenReturn(obj);
        } catch (IOException e) {
        }
    }

}
