package uk.gov.moj.sdt.cmc.consumers.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import static uk.gov.moj.sdt.cmc.consumers.request.UpdateType.WD;

/**
 * Test conversion of ClaimStatusUpdateRequest to Json.  This simulates the conversion that takes
 * place when the ClaimStatusUpdateRequest is sent to the claimStatusUpdate endpoint.
 */
class ClaimStatusUpdateRequestTest extends RequestTestBase {

    private static final String CLAIM_NUMBER = "12345678";

    private ClaimStatusUpdateRequest claimStatusUpdateRequest;

    @Override
    protected void setUpLocalTests() {
        super.setUpLocalTests();
        claimStatusUpdateRequest = new ClaimStatusUpdateRequest();
    }

    @Test
    void testConvertToJsonAllFields() throws JsonProcessingException {
        claimStatusUpdateRequest.setClaimNumber(CLAIM_NUMBER);
        claimStatusUpdateRequest.setDefendantId("1");
        claimStatusUpdateRequest.setNotificationType(WD);
        claimStatusUpdateRequest.setPaidInFullDate(createDate(2024, 10, 1));
        claimStatusUpdateRequest.setSection38Compliancy(true);

        // Use pretty print to avoid having to declare expected value as one long hard to read string
        String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(claimStatusUpdateRequest);

        String expectedResult = """
            {
              "caseManRef" : "12345678",
              "respondentId" : "1",
              "updateType" : "WD",
              "paidInFullDate" : "2024-10-01",
              "section38Compliancy" : true
            }""";
        assertExpectedRequestJson(expectedResult, result);
    }

    @Test
    void testConvertToJsonMandatoryFieldsOnly() throws JsonProcessingException {
        claimStatusUpdateRequest.setClaimNumber(CLAIM_NUMBER);
        claimStatusUpdateRequest.setNotificationType(WD);
        claimStatusUpdateRequest.setPaidInFullDate(createDate(2024, 10, 1));

        String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(claimStatusUpdateRequest);

        String expectedResult = """
            {
              "caseManRef" : "12345678",
              "respondentId" : null,
              "updateType" : "WD",
              "paidInFullDate" : "2024-10-01",
              "section38Compliancy" : null
            }""";
        assertExpectedRequestJson(expectedResult, result);
    }
}
