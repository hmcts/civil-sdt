package uk.gov.moj.sdt.cmc.consumers.client.converter;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.client.BaseXmlTest;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResult;
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

    private static final String CLAIM_DEFENCES = "ClaimDefences.xml";

    private XmlToObjectConverter xmlToObject = new XmlToObjectConverter();

    @BeforeEach
    public void setup() {
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

// TODO: fix broken tests
//
//    @Test
//    void shouldReadAttributesFromClaimDefencesRequest() throws IOException {
//        String xmlContent = readXmlAsString(CLAIM_DEFENCES);
//        SubmitQueryRequest request = xmlToObject.convertXmlToObject(xmlContent, SubmitQueryRequest.class);
//        assertNotNull(request);
//    }
//
//    @Test
//    void shouldConvertClaimDefencesRequestToString() throws IOException {
//        String xmlContent = readXmlAsString(CLAIM_DEFENCES);
//        String request = xmlToObject.convertXmlToJson(xmlContent);
//        assertNotNull(request);
//    }

    private ClaimDefencesResponse createClaimDefencesResponse() {
        ClaimDefencesResponse response = new ClaimDefencesResponse();
        ClaimDefencesResult result1 = createClaimDefencesResult("case1", "resp1",
                "2020-09-20", "2020-09-21 11:12:13", "type1",
                "defence1");
        ClaimDefencesResult result2 = createClaimDefencesResult("case2", "resp2",
                "2020-09-27", "2020-09-28 12:13:!4", "type2",
                "defence2");
        ClaimDefencesResult result3 = createClaimDefencesResult("case3", "resp3",
                "2020-09-29", "2020-09-30 13:14:15", "type3",
                "defence2");
        ClaimDefencesResult[] results = new ClaimDefencesResult[3];
        results[0] = result1;
        results[1] = result2;
        results[2] = result3;

        response.setResults(results);
        response.setResultCount(results.length);
        return response;
    }
    private ClaimDefencesResult createClaimDefencesResult(String caseManRef, String respondentId,
                                                          String defendantResponseFiledDate,
                                                          String defendantResponseCreatedDate, String responseType,
                                                          String defence) {
        return new ClaimDefencesResult(caseManRef, respondentId, defendantResponseFiledDate,
                defendantResponseCreatedDate, responseType ,defence);
    }

}
