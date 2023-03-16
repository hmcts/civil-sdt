package uk.gov.moj.sdt.cmc.consumers.client.converter;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.client.BaseXmlTest;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.request.UpdateType;
import uk.gov.moj.sdt.consumers.util.McolDefenceDetailTypeUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlToObjectTest extends BaseXmlTest {

    private static final String JSON_RESPONSE = "{\"bsType\":\"BC\",\"caseManRef\":\"H0PR0001\","
        + "\"respondentId\":\"1\"}";

    private static final String JSON_STRING_REQUESTED = "{\"claimNumber\":\"H0PR0001\","
        + "\"defendantId\":\"1\","
        + "\"breathingSpaceNotificationType\":\"BC\"}";

    private static final String BREATHING_SPACE = "BreathingSpace.xml";

    private static final String CLAIM_STATUS_UPDATE = "ClaimStatusUpdate.xml";

    private static final String EXPECTED_CLAIM_STATUS = "Expected_ClaimStatusRequest.json";

    private static final String EXPECTED_CLAIM_STATUS_REQUEST = "ExpectedCMC_ClaimStatusRequest.json";

    private XmlToObjectConverter xmlToObject = new XmlToObjectConverter();

    private McolDefenceDetailTypeUtil mcolDefenceDetailTypeUtil;

    @BeforeEach
    public void setup() {
        mcolDefenceDetailTypeUtil = new McolDefenceDetailTypeUtil();
    }

    @Test
    void shouldConvertBreathingSpaceRequestToString() throws IOException {
        String xmlContent = readXmlAsString(BREATHING_SPACE);
        String request = xmlToObject.convertXmlToJson(xmlContent);
        assertNotNull(request);
        assertEquals(JSON_STRING_REQUESTED, request);
    }

    @Test
    void shouldConvertBreathingSpaceRequest() throws IOException {
        String xmlContent = readXmlAsString(BREATHING_SPACE);
        BreathingSpaceRequest request = xmlToObject.convertXmlToObject(xmlContent, BreathingSpaceRequest.class);
        assertNotNull(request);
        assertEquals("1", request.getRespondentId());
        assertEquals("H0PR0001", request.getCaseManRef());
        assertEquals("BC", request.getBsType());
    }

    @Test
    void shouldConvertBreathingSpaceRequestAndConvertToJson() throws IOException {
        String xmlContent = readXmlAsString(BREATHING_SPACE);
        BreathingSpaceRequest request = xmlToObject.convertXmlToObject(xmlContent, BreathingSpaceRequest.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(request);
        assertNotNull(jsonString);
        assertEquals(JSON_RESPONSE, jsonString);
    }

    @Test
    void shouldConvertClaimStatusUpdateRequestToString() throws IOException {
        String xmlContent = readXmlAsString(CLAIM_STATUS_UPDATE);
        String request = xmlToObject.convertXmlToJson(xmlContent);
        assertNotNull(request);
        String expectedValue = readFile(EXPECTED_CLAIM_STATUS);
        assertEquals(expectedValue, request);
    }

    @Test
    void shouldConvertClaimStatusUpdateRequest() throws IOException {
        String xmlContent = readXmlAsString(CLAIM_STATUS_UPDATE);
        ClaimStatusUpdateRequest request = xmlToObject.convertXmlToObject(xmlContent, ClaimStatusUpdateRequest.class);
        assertNotNull(request);
        assertEquals("1676030589543579", request.getCaseManRef());
        assertEquals("1", request.getRespondentId());
        assertEquals(UpdateType.WD, request.getUpdateType());
        assertNotNull(request.getPaidInFullDate());
        assertTrue(request.getSection38Compliancy());
    }

    @Test
    void shouldConvertClaimStatusUpdateRequestAndConvertToJson() throws IOException {
        String xmlContent = readXmlAsString(CLAIM_STATUS_UPDATE);
        ClaimStatusUpdateRequest request = xmlToObject.convertXmlToObject(xmlContent, ClaimStatusUpdateRequest.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(request);
        assertNotNull(jsonString);
        String expectedValue = readFile(EXPECTED_CLAIM_STATUS_REQUEST);
        assertEquals(expectedValue, jsonString);
    }

}
