package uk.gov.moj.sdt.cmc.consumers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.moj.sdt.base.WireMockBaseTest;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.utils.cmc.RequestType;

import java.nio.charset.StandardCharsets;
import javax.inject.Inject;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.moj.sdt.utils.cmc.RequestType.CLAIM;

public class CMCConsumerGatewayIT extends WireMockBaseTest {

    @Inject
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private CMCConsumerGateway cmcConsumerGateway;

    private static long connectionTimeOut = 1000;

    private static long receiveTimeOut = 1000;

    private static final String SDT_REFERENCE = "MCOL-0000001";

    private static final byte[] XML = "".getBytes(StandardCharsets.UTF_8);

    @Before
    public void setUp() {
        super.initMock();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldCreateClaim() {
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        setupMockBehaviour(CLAIM, individualRequest);
        cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut);
    }

    private void setupMockBehaviour(RequestType requestType, IIndividualRequest individualRequest) {
        when(individualRequest.getSdtRequestReference()).thenReturn(SDT_REFERENCE);
        when(individualRequest.getRequestPayload()).thenReturn(XML);
        when(individualRequest.getRequestType()).thenReturn(requestType.getType());
    }

}
