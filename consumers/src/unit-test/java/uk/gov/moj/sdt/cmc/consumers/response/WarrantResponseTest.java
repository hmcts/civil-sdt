package uk.gov.moj.sdt.cmc.consumers.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static uk.gov.moj.sdt.cmc.consumers.response.JudgmentWarrantStatus.WARRANT_ACCEPTED_BY_CCBC;
import static uk.gov.moj.sdt.cmc.consumers.response.ResponseStatus.ACCEPTED;
import static uk.gov.moj.sdt.cmc.consumers.response.ResponseStatus.INITIALLY_ACCEPTED;

class WarrantResponseTest extends ResponseTestBase {

    @Test
    void testConvertToObjectAllFields() throws JsonProcessingException {
        String responseJson = """
            {
              "responseStatus" : "Accepted",
              "warrantResponseDetail" : {
                "issueDate" : "2024-09-30",
                "warrantNumber" : "W12345678",
                "enforcingCourtCode" : "123",
                "enforcingCourtName" : "Test Court",
                "fee" : 100,
                "judgmentWarrantStatus" : "Warrant accepted by CCBC."
              }
            }""";

        WarrantResponse warrantResponse = objectMapper.readValue(responseJson, WarrantResponse.class);

        assertNotNull(warrantResponse, "WarrantResponse should not be null");
        assertEquals(ACCEPTED, warrantResponse.getResponseStatus(), "WarrantResponse has unexpected response status");

        WarrantResponseDetail warrantResponseDetail = warrantResponse.getWarrantResponseDetail();
        assertNotNull(warrantResponseDetail, "WarrantResponseDetail should not be null");
        assertEquals(createDate(2024, 9, 30),
                     warrantResponseDetail.getIssueDate(),
                     "WarrantResponseDetail has unexpected issue date");
        assertEquals("W12345678",
                     warrantResponseDetail.getWarrantNumber(),
                     "WarrantResponseDetail has unexpected warrant number");
        assertEquals("123",
                     warrantResponseDetail.getEnforcingCourtCode(),
                     "WarrantResponseDetail has unexpected enforcing court code");
        assertEquals("Test Court",
                     warrantResponseDetail.getEnforcingCourtName(),
                     "WarrantResponseDetail has unexpected enforcing court name");
        assertEquals(100L, warrantResponseDetail.getFee(), "WarrantResponseDetail has unexpected fee");
        assertEquals(WARRANT_ACCEPTED_BY_CCBC.getMessage(),
                     warrantResponseDetail.getJudgmentWarrantStatus(),
                     "WarrantResponseDetail has unexpected judgment warrant status");
    }

    @Test
    void testConvertToObjectMandatoryFieldsOnly() throws JsonProcessingException {
        String responseJson = """
            {
              "responseStatus" : "Initially Accepted",
              "warrantResponseDetail" : {
                "issueDate" : null,
                "warrantNumber" : null,
                "enforcingCourtCode" : null,
                "enforcingCourtName" : null,
                "fee" : null,
                "judgmentWarrantStatus" : null
              }
            }""";

        WarrantResponse warrantResponse = objectMapper.readValue(responseJson, WarrantResponse.class);

        assertNotNull(warrantResponse, "WarrantResponse should not be null");
        assertEquals(INITIALLY_ACCEPTED,
                     warrantResponse.getResponseStatus(),
                     "WarrantResponse has unexpected response status");

        WarrantResponseDetail warrantResponseDetail = warrantResponse.getWarrantResponseDetail();
        assertNotNull(warrantResponseDetail, "WarrantResponseDetail should not be null");
        assertNull(warrantResponseDetail.getIssueDate(), "WarrantResponseDetail issue date should be null");
        assertNull(warrantResponseDetail.getWarrantNumber(), "WarrantResponseDetail warrant number should be null");
        assertNull(warrantResponseDetail.getEnforcingCourtCode(),
                   "WarrantResponseDetail enforcing court code should be null");
        assertNull(warrantResponseDetail.getEnforcingCourtName(),
                   "WarrantResponseDetail enforcing court name should be null");
        assertNull(warrantResponseDetail.getFee(), "WarrantResponseDetail fee should be null");
        assertNull(warrantResponseDetail.getJudgmentWarrantStatus(),
                   "WarrantResponseDetail judgment warrant status should be null");
    }
}
