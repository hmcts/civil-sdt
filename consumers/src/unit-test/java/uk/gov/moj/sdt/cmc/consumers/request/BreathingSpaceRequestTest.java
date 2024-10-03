package uk.gov.moj.sdt.cmc.consumers.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

/**
 * Test conversion of BreathingSpaceRequest to Json.  This simulates the conversion that takes
 * place when the BreathingSpaceRequest is sent to the breathingSpace endpoint.
 */
class BreathingSpaceRequestTest extends RequestTestBase {

    private BreathingSpaceRequest breathingSpaceRequest;

    @Override
    protected void setUpLocalTests() {
        super.setUpLocalTests();
        breathingSpaceRequest = new BreathingSpaceRequest();
    }

    @Test
    void testConvertToJsonAllFields() throws JsonProcessingException {
        breathingSpaceRequest.setClaimNumber("12345678");
        breathingSpaceRequest.setDefendantId("1");
        breathingSpaceRequest.setBreathingSpaceNotificationType("BS");

        // Use pretty print to avoid having to declare expected value as one long hard to read string
        String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(breathingSpaceRequest);

        String expectedResult = """
            {
              "caseManRef" : "12345678",
              "respondentId" : "1",
              "bsType" : "BS"
            }""";
        assertExpectedRequestJson(expectedResult, result);
    }
}
