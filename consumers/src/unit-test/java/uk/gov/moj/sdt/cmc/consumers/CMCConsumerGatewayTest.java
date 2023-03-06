package uk.gov.moj.sdt.cmc.consumers;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.IBreathingSpace;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimDefences;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimStatusUpdate;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.util.ResponsesSummaryUtil;
import uk.gov.moj.sdt.cmc.consumers.xml.XmlElementValueReader;
import uk.gov.moj.sdt.domain.RequestType;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
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
    private XmlElementValueReader xmlElementValueReader;

    @Mock
    private IBreathingSpace breathingSpace;

    @Mock
    private IClaimStatusUpdate claimStatusUpdate;

    @Mock
    private IClaimDefences claimDefences;

    @Mock
    private IIndividualRequest individualRequest;

    @Mock
    private BreathingSpaceRequest breathingSpaceRequest;

    @Mock
    private ResponsesSummaryUtil responsesSummaryUtil;

    @BeforeEach
    public void setUpLocalTests() {
        cmcConsumerGateway = new CMCConsumerGateway(breathingSpace, claimStatusUpdate, claimDefences,
                xmlToObject, xmlElementValueReader);
    }

    @Test
    void shouldInvokeBreathingSpace() {
        when(individualRequest.getRequestType()).thenReturn(RequestType.BREATHING_SPACE.getRequestType());
        when(individualRequest.getRequestPayload()).thenReturn(XML);
        try {
            when(xmlToObject.convertXmlToObject(anyString(), any())).thenReturn(breathingSpaceRequest);
        } catch (IOException e) {
        }

        cmcConsumerGateway.individualRequest(individualRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);
        verify(breathingSpace).breathingSpace(any(BreathingSpaceRequest.class));
    }

}
