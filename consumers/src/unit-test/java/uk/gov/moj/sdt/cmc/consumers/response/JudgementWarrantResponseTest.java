package uk.gov.moj.sdt.cmc.consumers.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static uk.gov.moj.sdt.cmc.consumers.response.JudgmentWarrantStatus.JUDGMENT_ACCEPTED_WARRANT_ACCEPTED_BY_CCBC;
import static uk.gov.moj.sdt.cmc.consumers.response.ResponseStatus.ACCEPTED;
import static uk.gov.moj.sdt.cmc.consumers.response.ResponseStatus.INITIALLY_ACCEPTED;

class JudgementWarrantResponseTest extends ResponseTestBase {

    @Test
    void testConvertToObjectAllFields() throws JsonProcessingException {
        String responseJson = """
            {
              "responseStatus" : "Accepted",
              "judgementWarrantResponseDetail" : {
                "issueDate" : "2024-09-29",
                "judgmentEnteredDate" : "2024-09-30",
                "firstPaymentDate" : "2024-10-01",
                "warrantNumber" : "W12345678",
                "enforcingCourtCode" : "123",
                "enforcingCourtName" : "Test Court",
                "fee" : 100,
                "judgmentWarrantStatus" : "Judgment accepted by CCBC.  Warrant accepted by CCBC."
              }
            }""";

        JudgementWarrantResponse judgementWarrantResponse =
            objectMapper.readValue(responseJson, JudgementWarrantResponse.class);

        assertNotNull(judgementWarrantResponse, "JudgementWarrantResponse should not be null");
        assertEquals(ACCEPTED,
                     judgementWarrantResponse.getResponseStatus(),
                     "JudgementWarrantResponse has unexpected response status");

        JudgementWarrantResponseDetail judgementWarrantResponseDetail =
            judgementWarrantResponse.getJudgementWarrantResponseDetail();
        assertNotNull(judgementWarrantResponseDetail, "JudgementWarrantResponseDetail should not be null");
        assertEquals(createDate(2024, 9, 29),
                     judgementWarrantResponseDetail.getIssueDate(),
                     "JudgementWarrantResponseDetail has unexpected issue date");
        assertEquals(createDate(2024, 9, 30),
                     judgementWarrantResponseDetail.getJudgmentEnteredDate(),
                     "JudgementWarrantResponseDetail has unexpected judgment entered date");
        assertEquals(createDate(2024, 10, 1),
                     judgementWarrantResponseDetail.getFirstPaymentDate(),
                     "JudgementWarrantResponseDetail has unexpected first payment date");
        assertEquals("W12345678",
                     judgementWarrantResponseDetail.getWarrantNumber(),
                     "JudgementWarrantResponseDetail has unexpected warrant number");
        assertEquals("123",
                     judgementWarrantResponseDetail.getEnforcingCourtCode(),
                     "JudgementWarrantResponseDetail has unexpected enforcing court code");
        assertEquals("Test Court",
                     judgementWarrantResponseDetail.getEnforcingCourtName(),
                     "JudgementWarrantResponseDetail has unexpected enforcing court name");
        assertEquals(100L, judgementWarrantResponseDetail.getFee(), "WarrantResponseDetail has unexpected fee");
        assertEquals(JUDGMENT_ACCEPTED_WARRANT_ACCEPTED_BY_CCBC.getMessage(),
                     judgementWarrantResponseDetail.getJudgmentWarrantStatus(),
                     "JudgementWarrantResponseDetail has unexpected judgment warrant status");
    }

    @Test
    void testConvertToObjectMandatoryFieldsOnly() throws JsonProcessingException {
        String responseJson = """
            {
              "responseStatus" : "Initially Accepted",
              "judgementWarrantResponseDetail" : {
                "issueDate" : null,
                "judgmentEnteredDate" : null,
                "firstPaymentDate" : null,
                "warrantNumber" : null,
                "enforcingCourtCode" : null,
                "enforcingCourtName" : null,
                "fee" : null,
                "judgmentWarrantStatus" : null
              }
            }""";

        JudgementWarrantResponse judgementWarrantResponse =
            objectMapper.readValue(responseJson, JudgementWarrantResponse.class);

        assertNotNull(judgementWarrantResponse, "JudgementWarrantResponse should not be null");
        assertEquals(INITIALLY_ACCEPTED,
                     judgementWarrantResponse.getResponseStatus(),
                     "JudgementWarrantResponse has unexpected response status");

        JudgementWarrantResponseDetail judgementWarrantResponseDetail =
            judgementWarrantResponse.getJudgementWarrantResponseDetail();
        assertNotNull(judgementWarrantResponseDetail, "JudgementWarrantResponseDetail should not be null");
        assertNull(judgementWarrantResponseDetail.getIssueDate(),
                   "JudgementWarrantResponseDetail issue date should be null");
        assertNull(judgementWarrantResponseDetail.getJudgmentEnteredDate(),
                   "JudgementWarrantResponseDetail judgment entered date should be null");
        assertNull(judgementWarrantResponseDetail.getFirstPaymentDate(),
                   "JudgementWarrantResponseDetail first payment date should be null");
        assertNull(judgementWarrantResponseDetail.getWarrantNumber(),
                   "JudgementWarrantResponseDetail warrant number should be null");
        assertNull(judgementWarrantResponseDetail.getEnforcingCourtCode(),
                   "JudgementWarrantResponseDetail enforcing court code should be null");
        assertNull(judgementWarrantResponseDetail.getEnforcingCourtName(),
                   "JudgementWarrantResponseDetail enforcing court name should be null");
        assertNull(judgementWarrantResponseDetail.getFee(), "JudgementWarrantResponseDetail fee should be null");
        assertNull(judgementWarrantResponseDetail.getJudgmentWarrantStatus(),
                   "JudgementWarrantResponseDetail judgment warrant status should be null");
    }
}
