package uk.gov.moj.sdt.cmc.consumers.client.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.client.BaseXmlTest;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResult;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.domain.SubmitQueryRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlToObjectTest extends BaseXmlTest {

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
                "2020-09-20", "2020-09-21", "type1",
                "defence1");
        ClaimDefencesResult result2 = createClaimDefencesResult("case2", "resp2",
                "2020-09-27", "2020-09-28", "type2",
                "defence2");
        ClaimDefencesResult result3 = createClaimDefencesResult("case3", "resp3",
                "2020-09-29", "2020-09-30", "type3",
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
