package uk.gov.moj.sdt.cmc.consumers.client.converter;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.client.BaseXmlTest;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlToObjectTest extends BaseXmlTest {

    private static final String JSON_RESPONSE = "{\"bsType\":\"BC\",\"caseManRef\":\"H0PR0001\","
        + "\"respondentId\":\"1\"}";

    private static final String JSON_STRING_REQUESTED = "{\"claimNumber\":\"H0PR0001\","
        + "\"defendantId\":\"1\","
        + "\"breathingSpaceNotificationType\":\"BC\"}";

    private static final String BREATHING_SPACE = "BreathingSpace.xml";

    private XmlToObjectConverter xmlToObject = new XmlToObjectConverter();

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

}
