package uk.gov.moj.sdt.cmc.consumers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.moj.sdt.base.WireMockBaseTest;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.cmc.RequestType;

import java.nio.charset.StandardCharsets;
import javax.inject.Inject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.moj.sdt.utils.cmc.RequestType.BREATHING_SPACE;
import static uk.gov.moj.sdt.utils.cmc.RequestType.CLAIM;
import static uk.gov.moj.sdt.utils.cmc.RequestType.CLAIM_STATUS_UPDATE;
import static uk.gov.moj.sdt.utils.cmc.RequestType.JUDGMENT;
import static uk.gov.moj.sdt.utils.cmc.RequestType.JUDGMENT_WARRANT;
import static uk.gov.moj.sdt.utils.cmc.RequestType.WARRANT;

public class CMCConsumerGatewayIT extends WireMockBaseTest {

    @Inject
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private CMCConsumerGateway cmcConsumerGateway;

    private static long connectionTimeOut = 1000;

    private static long receiveTimeOut = 1000;

    private static final String SDT_REFERENCE = "MCOL-0000001";

    private static final String XML_FOLDER = "xmlrequest/";

    private static final String BREATHING_SPACE_XML = XML_FOLDER + "BreathingSpace.xml";

    private static final String CLAIM_XML = XML_FOLDER +  "Claim.xml";

    private static final String CLAIM_STATUS_UPDATE_XML = XML_FOLDER + "ClaimStatusUpdate.xml";

    private static final String CLAIM_DEFENCES_XML = XML_FOLDER + "ClaimDefences.xml";

    private static final String JUDGMENT_XML = XML_FOLDER + "Judgement.xml";

    private static final String WARRANT_REQUEST_XML = XML_FOLDER + "WarrantRequest.xml";

    private static final String JUDGMENT_WARRANT_REQUEST_XML = XML_FOLDER + "JudgementWarrantRequest.xml";

    @BeforeEach
    public void setUp() {
        super.initMock();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldReturnSuccessOnJudgment() {
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        String xmlContent = readXmlAsString(JUDGMENT_XML);
        setupMockBehaviour(JUDGMENT, xmlContent, individualRequest);
        cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut);
        verify(individualRequest).setTargetApplicationResponse(any());
    }

    @Test
    public void shouldReturnSuccessOnBreathingSpace() {
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        String xmlContent = readXmlAsString(BREATHING_SPACE_XML);
        setupMockBehaviour(BREATHING_SPACE, xmlContent, individualRequest);
        cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut);
        verify(individualRequest).setRequestStatus(any());
    }

    public void shouldReturn404OnBreathingSpace() {
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        String xmlContent = readXmlAsString(BREATHING_SPACE_XML);
        setupMockBehaviour(BREATHING_SPACE, xmlContent, individualRequest);
        SdtContext.getContext().setCustomerIdamId("1234");
        cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut);
        verify(individualRequest).setRequestStatus(any());
    }

    @Test
    public void shouldReturnSuccessOnClaimStatusUpdate() {
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        String xmlContent = readXmlAsString(CLAIM_STATUS_UPDATE_XML);
        setupMockBehaviour(CLAIM_STATUS_UPDATE, xmlContent, individualRequest);
        cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut);
        verify(individualRequest).setRequestStatus(any());
    }

    @Test
    public void shouldCreateClaim() {
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        String xmlContent = readXmlAsString(CLAIM_XML);
        setupMockBehaviour(CLAIM, xmlContent, individualRequest);
        cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut);
        verify(individualRequest).setTargetApplicationResponse(any());
    }

    @Test
    public void shouldReturnSuccessOnWarrant() {
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        String xmlContent = readXmlAsString(WARRANT_REQUEST_XML);
        setupMockBehaviour(WARRANT, xmlContent, individualRequest);
        cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut);
        verify(individualRequest).setTargetApplicationResponse(any());
    }

    @Test
    public void shouldReturnSuccessOnJudgmentWarrant() {
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        String xmlContent = readXmlAsString(JUDGMENT_WARRANT_REQUEST_XML);
        setupMockBehaviour(JUDGMENT_WARRANT, xmlContent, individualRequest);
        cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut);
        verify(individualRequest).setTargetApplicationResponse(any());
    }

    private void setupMockBehaviour(RequestType requestType,
                                    String xmlContent,
                                    IIndividualRequest individualRequest) {
        xmlContent = xmlContent == null ? "" : xmlContent;
        when(individualRequest.getSdtRequestReference()).thenReturn(SDT_REFERENCE);
        when(individualRequest.getRequestPayload()).thenReturn(xmlContent.getBytes(StandardCharsets.UTF_8));
        when(individualRequest.getRequestType()).thenReturn(requestType.getType());
    }

}
