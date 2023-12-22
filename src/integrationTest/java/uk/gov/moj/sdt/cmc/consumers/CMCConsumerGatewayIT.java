package uk.gov.moj.sdt.cmc.consumers;

import com.github.tomakehurst.wiremock.client.WireMock;
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
import uk.gov.moj.sdt.utils.cmc.exception.CMCException;
import uk.gov.moj.sdt.utils.cmc.exception.CaseOffLineException;

import java.nio.charset.StandardCharsets;
import javax.inject.Inject;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
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

    private static final String IDAMID = "1234";

    private static final String IDAMID_SUCCESS = "12345";

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
        wireMockServer.stubFor(WireMock.post(urlPathMatching("/breathingSpace"))
                                   .withHeader("IDAMID", containing(IDAMID_SUCCESS))
                                   .withHeader("SDTREQUESTID", containing(SDT_REFERENCE))
                                   .willReturn(aResponse()
                                                   .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                                   .withBody("""
                                                                 {
                                                                       "processingStatus": "PROCESSED"
                                                                 }
                                                                 """)
                                                   .withStatus(200)));

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        String xmlContent = readXmlAsString(BREATHING_SPACE_XML);
        setupMockBehaviour(BREATHING_SPACE, xmlContent, individualRequest);
        SdtContext.getContext().setCustomerIdamId(IDAMID_SUCCESS);
        cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut);
        verify(individualRequest).setRequestStatus(any());
    }

    @Test
    public void shouldReturn400OnBreathingSpace001UnknownUser() {
        wireMockServer.stubFor(WireMock.post(urlPathMatching("/breathingSpace"))
                                   .withHeader("IDAMID", containing(IDAMID))
                                   .withHeader("SDTREQUESTID", containing(SDT_REFERENCE))
                                   .willReturn(aResponse()
                                                   .withStatus(400)
                                                   .withBody("""
                                                                 {
                                                                       "error": "Bad Request",
                                                                       "message": "001 unknown user"
                                                                 }
                                                                 """)));

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        String xmlContent = readXmlAsString(BREATHING_SPACE_XML);
        setupMockBehaviour(BREATHING_SPACE, xmlContent, individualRequest);
        SdtContext.getContext().setCustomerIdamId(IDAMID);
        CMCException exception = assertThrows(CMCException.class, () ->
            cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut));
        assertTrue(exception.getMessage().contains("001 unknown user"));
    }


    @Test
    public void shouldReturn400OnBreathingSpaceSpecifiedClaimDoesNotBelongToTheRequestingCustomer() {
        wireMockServer.stubFor(WireMock.post(urlPathMatching("/breathingSpace"))
                                   .withHeader("IDAMID", containing(IDAMID))
                                   .withHeader("SDTREQUESTID", containing(SDT_REFERENCE))
                                   .willReturn(aResponse()
                                                   .withStatus(400)
                                                   .withBody("""
                                                                 {
                                                                       "error": "Bad Request",
                                                                       "message": "023 Specified claim does not belong to the requesting customer"
                                                                 }
                                                                 """)));

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        String xmlContent = readXmlAsString(BREATHING_SPACE_XML);
        setupMockBehaviour(BREATHING_SPACE, xmlContent, individualRequest);
        SdtContext.getContext().setCustomerIdamId(IDAMID);
        CMCException exception = assertThrows(CMCException.class, () ->
            cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut));
        assertTrue(exception.getMessage().contains("023 Specified claim does not belong to the requesting customer"));
    }

    @Test
    public void shouldReturn400OnBreathingSpaceDefendant2SpecifiedButThereIsOnly1Defendant() {
        wireMockServer.stubFor(WireMock.post(urlPathMatching("/breathingSpace"))
                                   .withHeader("IDAMID", containing(IDAMID))
                                   .withHeader("SDTREQUESTID", containing(SDT_REFERENCE))
                                   .willReturn(aResponse()
                                                   .withStatus(400)
                                                   .withBody("""
                                                                 {
                                                                       "error": "Bad Request",
                                                                       "message": "028 Defendant 2 is specified but there is only 1 defendant on the claim"
                                                                 }
                                                                 """)));

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        String xmlContent = readXmlAsString(BREATHING_SPACE_XML);
        setupMockBehaviour(BREATHING_SPACE, xmlContent, individualRequest);
        SdtContext.getContext().setCustomerIdamId(IDAMID);
        CMCException exception = assertThrows(CMCException.class, () ->
            cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut));
        assertTrue(exception.getMessage().contains("028 Defendant 2 is specified but there is only 1 defendant on the claim"));
    }

    @Test
    public void shouldReturn400OnBreathingSpaceInvalidOnTheReferencedClaim() {
        wireMockServer.stubFor(WireMock.post(urlPathMatching("/breathingSpace"))
                                   .withHeader("IDAMID", containing(IDAMID))
                                   .withHeader("SDTREQUESTID", containing(SDT_REFERENCE))
                                   .willReturn(aResponse()
                                                   .withStatus(400)
                                                   .withBody("""
                                                                 {
                                                                       "error": "Bad Request",
                                                                       "message": "099 This breathing space request is invalid on the referenced claim"
                                                                 }
                                                                 """)));

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        String xmlContent = readXmlAsString(BREATHING_SPACE_XML);
        setupMockBehaviour(BREATHING_SPACE, xmlContent, individualRequest);
        SdtContext.getContext().setCustomerIdamId(IDAMID);
        CMCException exception = assertThrows(CMCException.class, () ->
            cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut));
        assertTrue(exception.getMessage().contains("099 This breathing space request is invalid on the referenced claim"));
    }

    @Test
    public void shouldReturn400DefendantIsAlreadyInActiveBreathingSpace() {
        wireMockServer.stubFor(WireMock.post(urlPathMatching("/breathingSpace"))
                                   .withHeader("IDAMID", containing(IDAMID))
                                   .withHeader("SDTREQUESTID", containing(SDT_REFERENCE))
                                   .willReturn(aResponse()
                                                   .withStatus(400)
                                                   .withBody("""
                                                                 {
                                                                       "error": "Bad Request",
                                                                       "message": "100 The defendant is already in active Breathing Space"
                                                                 }
                                                                 """)));

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        String xmlContent = readXmlAsString(BREATHING_SPACE_XML);
        setupMockBehaviour(BREATHING_SPACE, xmlContent, individualRequest);
        SdtContext.getContext().setCustomerIdamId(IDAMID);
        CMCException exception = assertThrows(CMCException.class, () ->
            cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut));
        assertTrue(exception.getMessage().contains("100 The defendant is already in active Breathing Space"));
    }

    @Test
    public void shouldReturn400DefendantIsNotCurrentlyInActiveBreathingSpace() {
        wireMockServer.stubFor(WireMock.post(urlPathMatching("/breathingSpace"))
                                   .withHeader("IDAMID", containing(IDAMID))
                                   .withHeader("SDTREQUESTID", containing(SDT_REFERENCE))
                                   .willReturn(aResponse()
                                                   .withStatus(400)
                                                   .withBody("""
                                                                 {
                                                                       "error": "Bad Request",
                                                                       "message": "101 The defendant is not currently in active Breathing Space"
                                                                 }
                                                                 """)));

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        String xmlContent = readXmlAsString(BREATHING_SPACE_XML);
        setupMockBehaviour(BREATHING_SPACE, xmlContent, individualRequest);
        SdtContext.getContext().setCustomerIdamId(IDAMID);
        CMCException exception = assertThrows(CMCException.class, () ->
            cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut));
        assertTrue(exception.getMessage().contains("101 The defendant is not currently in active Breathing Space"));
    }

    @Test
    public void shouldReturn400IncorrectBreathingSpaceCeasingEventType() {
        wireMockServer.stubFor(WireMock.post(urlPathMatching("/breathingSpace"))
                                   .withHeader("IDAMID", containing(IDAMID))
                                   .withHeader("SDTREQUESTID", containing(SDT_REFERENCE))
                                   .willReturn(aResponse()
                                                   .withStatus(400)
                                                   .withBody("""
                                                                 {
                                                                       "error": "Bad Request",
                                                                       "message": "102 Incorrect Breathing Space ceasing event type"
                                                                 }
                                                                 """)));

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        String xmlContent = readXmlAsString(BREATHING_SPACE_XML);
        setupMockBehaviour(BREATHING_SPACE, xmlContent, individualRequest);
        SdtContext.getContext().setCustomerIdamId(IDAMID);
        CMCException exception = assertThrows(CMCException.class, () ->
            cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut));
        assertTrue(exception.getMessage().contains("102 Incorrect Breathing Space ceasing event type"));
    }

    @Test
    public void shouldReturn400CaseOffline() {
        wireMockServer.stubFor(WireMock.post(urlPathMatching("/breathingSpace"))
                                   .withHeader("IDAMID", containing(IDAMID))
                                   .withHeader("SDTREQUESTID", containing(SDT_REFERENCE))
                                   .willReturn(aResponse()
                                                   .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                                   .withStatus(400)
                                                   .withBody("""
                                                                 {
                                                                       "error": "Bad Request",
                                                                       "message": "200 Case Is Offline."
                                                                 }
                                                                 """)));

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        String xmlContent = readXmlAsString(BREATHING_SPACE_XML);
        setupMockBehaviour(BREATHING_SPACE, xmlContent, individualRequest);
        SdtContext.getContext().setCustomerIdamId(IDAMID);
        CaseOffLineException exception = assertThrows(CaseOffLineException.class, () ->
            cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut));
        assertTrue(exception.getMessage().contains("200 Case Is Offline."));
    }

    @Test
    public void shouldReturn400RequestAlreadyProcessed() {
        wireMockServer.stubFor(WireMock.post(urlPathMatching("/breathingSpace"))
                                   .withHeader("IDAMID", containing(IDAMID))
                                   .withHeader("SDTREQUESTID", containing(SDT_REFERENCE))
                                   .willReturn(aResponse()
                                                   .withStatus(400)
                                                   .withBody("""
                                                                 {
                                                                       "error": "Bad Request",
                                                                       "message": "201 Request already processed"
                                                                 }
                                                                 """)));

        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        String xmlContent = readXmlAsString(BREATHING_SPACE_XML);
        setupMockBehaviour(BREATHING_SPACE, xmlContent, individualRequest);
        SdtContext.getContext().setCustomerIdamId(IDAMID);
        CMCException exception = assertThrows(CMCException.class, () ->
            cmcConsumerGateway.individualRequest(individualRequest, connectionTimeOut, receiveTimeOut));
        assertTrue(exception.getMessage().contains("201 Request already processed"));
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
