package uk.gov.moj.sdt.cmc.consumers.client.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.client.BaseXmlTest;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.model.breathingspace.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.model.claimStatusUpdate.ClaimStatusUpdateRequest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlToObjectTest extends BaseXmlTest {

    private static final String BREATHING_SPACE = "BreathingSpace.xml";

    private static final String CLAIM_STATUS_UPDATE = "ClaimStatusUpdate.xml";

    private XmlToObjectConverter xmlToObject = new XmlToObjectConverter();

    @BeforeEach
    public void setup() {
    }

    @Test
    void shouldConvertBreathingSpaceRequestToString() throws IOException {
        String xmlContent = readXmlAsString(BREATHING_SPACE);
        String request = xmlToObject.convertXmlToJson(xmlContent);
        assertNotNull(request);
    }

    @Test
    void shouldConvertBreathingSpaceRequest() throws IOException {
        String xmlContent = readXmlAsString(BREATHING_SPACE);
        BreathingSpaceRequest request = xmlToObject.convertXmlToObject(xmlContent, BreathingSpaceRequest.class);
        assertNotNull(request);
    }

    @Test
    void shouldConvertBreathingSpaceRequestAndConvertToJson() throws IOException {
        String xmlContent = readXmlAsString(BREATHING_SPACE);
        BreathingSpaceRequest request = xmlToObject.convertXmlToObject(xmlContent, BreathingSpaceRequest.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(request);
        assertNotNull(jsonString);
    }

    @Test
    void shouldConvertClaimStatusUpdateRequestToString() throws IOException {
        String xmlContent = readXmlAsString(CLAIM_STATUS_UPDATE);
        String request = xmlToObject.convertXmlToJson(xmlContent);
        assertNotNull(request);
    }

    @Test
    void shouldConvertClaimStatusUpdateRequest() throws IOException {
        String xmlContent = readXmlAsString(CLAIM_STATUS_UPDATE);
        ClaimStatusUpdateRequest request = xmlToObject.convertXmlToObject(xmlContent, ClaimStatusUpdateRequest.class);
        assertNotNull(request);
    }

    @Test
    void shouldConvertClaimStatusUpdateRequestAndConvertToJson() throws IOException {
        String xmlContent = readXmlAsString(CLAIM_STATUS_UPDATE);
        ClaimStatusUpdateRequest request = xmlToObject.convertXmlToObject(xmlContent, ClaimStatusUpdateRequest.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(request);
        assertNotNull(jsonString);
    }
}
