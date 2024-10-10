package uk.gov.moj.sdt.cmc.consumers.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.request.common.SotSignature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test conversion of WarrantRequest to Json.  This simulates the conversion that takes
 * place when the WarrantRequest is sent to the requestWarrant endpoint.
 */
class WarrantRequestTest extends RequestTestBase {

    private static final String CLAIM_NUMBER = "12345678";
    private static final String STATEMENT_OF_TRUTH_NAME = "Test SoT Name";

    private WarrantRequest warrantRequest;

    @Override
    protected void setUpLocalTests() {
        super.setUpLocalTests();
        warrantRequest = new WarrantRequest();
    }

    @Test
    void testConvertToJsonAllFields() throws JsonProcessingException {
        warrantRequest.setClaimNumber(CLAIM_NUMBER);
        warrantRequest.setDefendantId("1");
        warrantRequest.setDefendantAddress(createAddress("defendant", "DD1 1DD"));
        warrantRequest.setBalanceOfDebt(100L);
        warrantRequest.setWarrantAmount(200L);
        warrantRequest.setSolicitorCost(10L);
        warrantRequest.setAdditionalNotes("Some additional notes");
        warrantRequest.setSotSignature(createSotSignature(STATEMENT_OF_TRUTH_NAME));

        // Use pretty print to avoid having to declare expected value as one long hard to read string
        String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(warrantRequest);

        String expectedResult = """
            {
              "caseManRef" : "12345678",
              "respondentId" : "1",
              "respondentAddress" : {
                "addressLine1" : "defendant line 1",
                "addressLine2" : "defendant line 2",
                "addressLine3" : "defendant line 3",
                "posttown" : "defendant line 4",
                "postcode" : "DD1 1DD"
              },
              "balanceOfDebt" : 100,
              "warrantAmount" : 200,
              "solicitorCost" : 10,
              "additionalNotes" : "Some additional notes",
              "sotName" : "Test SoT Name"
            }""";
        assertExpectedRequestJson(expectedResult, result);
    }

    @Test
    void testConvertToJsonMandatoryFieldsOnly() throws JsonProcessingException {
        warrantRequest.setClaimNumber(CLAIM_NUMBER);
        warrantRequest.setDefendantId("1");
        warrantRequest.setBalanceOfDebt(100L);
        warrantRequest.setWarrantAmount(200L);
        warrantRequest.setSotSignature(createSotSignature(STATEMENT_OF_TRUTH_NAME));

        String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(warrantRequest);

        String expectedResult = """
            {
              "caseManRef" : "12345678",
              "respondentId" : "1",
              "respondentAddress" : null,
              "balanceOfDebt" : 100,
              "warrantAmount" : 200,
              "solicitorCost" : null,
              "additionalNotes" : null,
              "sotName" : "Test SoT Name"
            }""";
        assertExpectedRequestJson(expectedResult, result);
    }

    @Test
    void testGetSotName() {
        SotSignature sotSignature = createSotSignature(STATEMENT_OF_TRUTH_NAME);

        warrantRequest.setSotSignature(sotSignature);

        assertEquals(STATEMENT_OF_TRUTH_NAME,
                     warrantRequest.getSotName(),
                     "WarrantRequest has unexpected statement of truth name");
    }

    @Test
    void testGetSotNameNull() {
        warrantRequest.setSotSignature(null);

        String sotName = warrantRequest.getSotName();
        assertNotNull(sotName, "WarrantRequest statement of truth name should not be null");
        assertEquals("", sotName, "WarrantRequest should have an empty string for statement of truth name");
    }
}
