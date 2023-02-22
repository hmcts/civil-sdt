package uk.gov.moj.sdt.cmc.consumers;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.IBreathingSpace;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.model.breathingspace.BreathingSpace;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;

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

    @BeforeEach
    public void setUpLocalTests() {
        xmlToObject = mock(XmlToObjectConverter.class);
        breathingSpace = mock(IBreathingSpace.class);
        individualRequest = mock(IIndividualRequest.class);
        cmcConsumerGateway = new CMCConsumerGateway(breathingSpace, xmlToObject);
        mockXmlToObject();
    }


    @Test
    void shouldInvokeBreathingSpace() {
        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);
        verify(breathingSpace).breathingSpace(any(BreathingSpace.class));
    }

    private void mockXmlToObject() {
        BreathingSpace breathingSpaceRequest = mock(BreathingSpace.class);
        when(individualRequest.getRequestPayload()).thenReturn(XML);
        try {
            when(xmlToObject.convertXmlToObject(anyString(), any())).thenReturn(breathingSpaceRequest);
        } catch (IOException e) {
        }
    }

}
