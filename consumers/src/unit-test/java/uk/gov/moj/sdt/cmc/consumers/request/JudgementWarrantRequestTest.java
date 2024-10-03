package uk.gov.moj.sdt.cmc.consumers.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.FullPaymentType;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgementRequest;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.PaymentSchedule;

import static uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgmentType.D;

/**
 * Test conversion of JudgementWarrantRequest to Json.  This simulates the conversion that takes
 * place when the JudgementWarrantRequest is sent to the requestJudgmentAndWarrant endpoint.
 */
class JudgementWarrantRequestTest extends RequestTestBase {

    private static final String CLAIM_NUMBER = "12345678";
    private static final String STATEMENT_OF_TRUTH_NAME = "signature";

    private JudgementWarrantRequest judgementWarrantRequest;

    @Override
    protected void setUpLocalTests() {
        super.setUpLocalTests();
        judgementWarrantRequest = new JudgementWarrantRequest();
    }

    @Test
    void testConvertToJsonMandatoryFieldsOnly() throws JsonProcessingException {
        judgementWarrantRequest.setJudgmentRequest(createJudgementRequest());
        judgementWarrantRequest.setWarrantRequest(createWarrantRequest());

        // Use pretty print to avoid having to declare expected value as one long hard to read string
        String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(judgementWarrantRequest);

        String expectedResult = """
            {
              "judgmentRequest" : {
                "caseManRef" : "12345678",
                "jointJudgment" : true,
                "judgmentType" : "D",
                "sentParticularsSeparately" : false,
                "respondentId" : null,
                "respondent1Address" : null,
                "respondent1DOB" : null,
                "respondent2Address" : null,
                "respondent2DOB" : null,
                "paymentSchedule" : {
                  "paymentScheduleType" : "inFull",
                  "paymentInFullBy" : "2024-10-02",
                  "installmentFrequency" : null,
                  "installmentAmount" : null
                },
                "interest" : null,
                "solicitorCost" : null,
                "deductedAmount" : null,
                "claimAmountAdmitted" : null,
                "courtFee" : null,
                "legalCosts" : null,
                "payee" : null,
                "sotName" : "signature"
              },
              "warrantRequest" : {
                "caseManRef" : "12345678",
                "respondentId" : "1",
                "respondentAddress" : null,
                "balanceOfDebt" : 100,
                "warrantAmount" : 200,
                "solicitorCost" : null,
                "additionalNotes" : null,
                "sotName" : "signature"
              }
            }""";
        assertExpectedRequestJson(expectedResult, result);
    }

    private JudgementRequest createJudgementRequest() {
        FullPaymentType fullPaymentType = new FullPaymentType();
        fullPaymentType.setFullByDate(createDate(2024, 10, 2));

        PaymentSchedule paymentSchedule = new PaymentSchedule();
        paymentSchedule.setInFullByPayment(fullPaymentType);

        JudgementRequest judgementRequest = new JudgementRequest();

        judgementRequest.setClaimNumber(CLAIM_NUMBER);
        judgementRequest.setJointJudgment(true);
        judgementRequest.setJudgmentType(D);
        judgementRequest.setSentParticularsSeparately(false);
        judgementRequest.setPaymentSchedule(paymentSchedule);
        judgementRequest.setSotSignature(createSotSignature(STATEMENT_OF_TRUTH_NAME));

        return judgementRequest;
    }

    private WarrantRequest createWarrantRequest() {
        WarrantRequest warrantRequest = new WarrantRequest();

        warrantRequest.setClaimNumber(CLAIM_NUMBER);
        warrantRequest.setDefendantId("1");
        warrantRequest.setBalanceOfDebt(100L);
        warrantRequest.setWarrantAmount(200L);
        warrantRequest.setSotSignature(createSotSignature(STATEMENT_OF_TRUTH_NAME));

        return warrantRequest;
    }
}
