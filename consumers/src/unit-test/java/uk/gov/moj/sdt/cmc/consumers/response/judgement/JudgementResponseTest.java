package uk.gov.moj.sdt.cmc.consumers.response.judgement;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.response.ResponseTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static uk.gov.moj.sdt.cmc.consumers.response.JudgmentWarrantStatus.JUDGMENT_ACCEPTED_BY_CCBC;
import static uk.gov.moj.sdt.cmc.consumers.response.ResponseStatus.ACCEPTED;
import static uk.gov.moj.sdt.cmc.consumers.response.ResponseStatus.INITIALLY_ACCEPTED;

class JudgementResponseTest extends ResponseTestBase {

    @Test
    void testConvertToResponseObjectAllFields() throws JsonProcessingException {
        String responseJson = """
            {
              "responseStatus" : "Accepted",
              "judgementResponseDetail" : {
                "judgmentEnteredDate" : "2024-10-01",
                "firstPaymentDate" : "2024-10-04",
                "judgmentWarrantStatus" : "Judgment accepted by CCBC."
              }
            }""";

        JudgementResponse judgementResponse = objectMapper.readValue(responseJson, JudgementResponse.class);

        assertNotNull(judgementResponse, "JudgementResponse should not be null");
        assertEquals(ACCEPTED,
                     judgementResponse.getResponseStatus(),
                     "JudgementResponse has unexpected response status");

        JudgementResponseDetail judgementResponseDetail = judgementResponse.getJudgementResponseDetail();
        assertNotNull(judgementResponseDetail, "JudgementResponseDetail should not be null");
        assertEquals(createDate(2024, 10, 1),
                     judgementResponseDetail.getJudgmentEnteredDate(),
                     "JudgementResponseDetail has unexpected judgment entered date");
        assertEquals(createDate(2024, 10, 4),
                     judgementResponseDetail.getFirstPaymentDate(),
                     "JudgementResponseDetail has unexpected first payment date");
        assertEquals(JUDGMENT_ACCEPTED_BY_CCBC.getMessage(),
                     judgementResponseDetail.getJudgmentWarrantStatus(),
                     "JudgementResponseDetail has unexpected judgment warrant status");
    }

    @Test
    void testConvertToObjectMandatoryFieldsOnly() throws JsonProcessingException {
        String responseJson = """
            {
              "responseStatus" : "Initially Accepted",
              "judgementResponseDetail" : {
                "judgmentEnteredDate" : null,
                "firstPaymentDate" : null,
                "judgmentWarrantStatus" : null
              }
            }""";

        JudgementResponse judgementResponse = objectMapper.readValue(responseJson, JudgementResponse.class);

        assertNotNull(judgementResponse, "JudgementResponse should not be null");
        assertEquals(INITIALLY_ACCEPTED,
                     judgementResponse.getResponseStatus(),
                     "JudgementResponse has unexpected response status");

        JudgementResponseDetail judgementResponseDetail = judgementResponse.getJudgementResponseDetail();
        assertNotNull(judgementResponseDetail, "JudgementResponseDetail should not be null");
        assertNull(judgementResponseDetail.getJudgmentEnteredDate(),
                   "JudgementResponseDetail judgment entered date should be null");
        assertNull(judgementResponseDetail.getFirstPaymentDate(),
                   "JudgementResponseDetail first payment date should be null");
        assertNull(judgementResponseDetail.getJudgmentWarrantStatus(),
                   "JudgementResponseDetail judgment warrant status should be null");
    }
}
