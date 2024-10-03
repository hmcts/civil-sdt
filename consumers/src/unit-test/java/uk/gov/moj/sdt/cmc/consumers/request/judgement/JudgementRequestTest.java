package uk.gov.moj.sdt.cmc.consumers.request.judgement;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.request.RequestTestBase;
import uk.gov.moj.sdt.cmc.consumers.request.common.SotSignature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgmentType.D;
import static uk.gov.moj.sdt.cmc.consumers.request.judgement.InstalmentFrequencyType.W;

/**
 * Test conversion of JudgementRequest to Json.  This simulates the conversion that takes
 * place when the JudgementRequest is sent to the requestJudgment endpoint.
 */
class JudgementRequestTest extends RequestTestBase {

    private static final String CLAIM_NUMBER = "12345678";
    private static final String STATEMENT_OF_TRUTH_NAME = "Test SoT name";

    private JudgementRequest judgementRequest;

    @Override
    protected void setUpLocalTests() {
        super.setUpLocalTests();
        judgementRequest = new JudgementRequest();
    }

    @Test
    void testConvertToJsonAllFields() throws JsonProcessingException {
        InstalmentPaymentType instalmentPaymentType = new InstalmentPaymentType();
        instalmentPaymentType.setAmount(5L);
        instalmentPaymentType.setFrequency(W);

        PaymentSchedule paymentSchedule = new PaymentSchedule();
        paymentSchedule.setInstalment(instalmentPaymentType);

        judgementRequest.setClaimNumber(CLAIM_NUMBER);
        judgementRequest.setJointJudgment(true);
        judgementRequest.setJudgmentType(D);
        judgementRequest.setSentParticularsSeparately(true);
        judgementRequest.setDefendantId("1");
        judgementRequest.setDefendant1Address(createAddress("defendant1", "DD1 1DD"));
        judgementRequest.setDefendant1DateOfBirth(createDate(2005, 1, 1));
        judgementRequest.setDefendant2Address(createAddress("defendant2", "DD2 2DD"));
        judgementRequest.setDefendant2DateOfBirth(createDate(2006, 2, 2));
        judgementRequest.setPaymentSchedule(paymentSchedule);
        judgementRequest.setInterest(2);
        judgementRequest.setSolicitorCost(10L);
        judgementRequest.setDeductedAmount(20L);
        judgementRequest.setClaimAmountAdmitted(30L);
        judgementRequest.setCourtFee(40L);
        judgementRequest.setLegalCosts(50L);
        judgementRequest.setPayee(createPayee());
        judgementRequest.setSotSignature(createSotSignature(STATEMENT_OF_TRUTH_NAME));

        // Use pretty print to avoid having to declare expected value as one long hard to read string
        String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(judgementRequest);

        String expectedResult = """
            {
              "caseManRef" : "12345678",
              "jointJudgment" : true,
              "judgmentType" : "D",
              "sentParticularsSeparately" : true,
              "respondentId" : "1",
              "respondent1Address" : {
                "addressLine1" : "defendant1 line 1",
                "addressLine2" : "defendant1 line 2",
                "addressLine3" : "defendant1 line 3",
                "posttown" : "defendant1 line 4",
                "postcode" : "DD1 1DD"
              },
              "respondent1DOB" : "2005-01-01",
              "respondent2Address" : {
                "addressLine1" : "defendant2 line 1",
                "addressLine2" : "defendant2 line 2",
                "addressLine3" : "defendant2 line 3",
                "posttown" : "defendant2 line 4",
                "postcode" : "DD2 2DD"
              },
              "respondent2DOB" : "2006-02-02",
              "paymentSchedule" : {
                "paymentScheduleType" : "installment",
                "paymentInFullBy" : null,
                "installmentFrequency" : "W",
                "installmentAmount" : 5
              },
              "interest" : 2,
              "solicitorCost" : 10,
              "deductedAmount" : 20,
              "claimAmountAdmitted" : 30,
              "courtFee" : 40,
              "legalCosts" : 50,
              "payee" : {
                "name" : "payee",
                "address" : {
                  "addressLine1" : "payee line 1",
                  "addressLine2" : "payee line 2",
                  "addressLine3" : "payee line 3",
                  "addressLine4" : "payee line 4",
                  "postTown" : "payee line 5",
                  "postcode" : "PP1 1PP"
                },
                "telephoneNumber" : "00001 000001",
                "dxNumber" : "00002 000002",
                "faxNumber" : "00003 000003",
                "email" : "payee@payee.com",
                "pcm" : "LE",
                "reference" : "payee ref",
                "bankAccountNumber" : "1234567890",
                "bankAccountHolder" : "bank account holder",
                "bankSortCode" : "00-00-00",
                "bankName" : "The Bank",
                "bankInfo1" : "Bank info1",
                "bankInfo2" : "Bank info2",
                "slipCodeline1" : "Slip code 1",
                "slipCodeline2" : "Slip code 2",
                "giroAccountNumber" : "0987654321",
                "giroTransCode1" : "giro 1",
                "giroTransCode2" : "giro 2",
                "apacsTransCode" : "APACS code"
              },
              "sotName" : "Test SoT name"
            }""";
        assertExpectedRequestJson(expectedResult, result);
    }

    @Test
    void testConvertToJsonMandatoryFieldsOnly() throws JsonProcessingException {
        ImmediatePaymentType immediatePaymentType = new ImmediatePaymentType();

        PaymentSchedule paymentSchedule = new PaymentSchedule();
        paymentSchedule.setImmediatePayment(immediatePaymentType);

        judgementRequest.setClaimNumber(CLAIM_NUMBER);
        judgementRequest.setJointJudgment(true);
        judgementRequest.setJudgmentType(D);
        judgementRequest.setSentParticularsSeparately(false);
        judgementRequest.setPaymentSchedule(paymentSchedule);
        judgementRequest.setSotSignature(createSotSignature(STATEMENT_OF_TRUTH_NAME));

        String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(judgementRequest);

        String expectedResult = """
            {
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
                "paymentScheduleType" : "immediate",
                "paymentInFullBy" : null,
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
              "sotName" : "Test SoT name"
            }""";
        assertExpectedRequestJson(expectedResult, result);
    }

    @Test
    void testGetSotName() {
        SotSignature sotSignature = createSotSignature(STATEMENT_OF_TRUTH_NAME);

        judgementRequest.setSotSignature(sotSignature);

        assertEquals(STATEMENT_OF_TRUTH_NAME,
                     judgementRequest.getSotName(),
                     "JudgementRequest has unexpected statement of truth name");
    }

    @Test
    void testGetSotNameNull() {
        judgementRequest.setSotSignature(null);

        String sotName = judgementRequest.getSotName();

        assertNotNull(sotName, "JudgementRequest statement of truth name should not be null");
        assertEquals("", sotName, "JudgmentRequest should have an empty string for statement of truth name");
    }

    private Payee createPayee() {

        String addressLinePrefix = "payee line ";
        PayeeAddress payeeAddress = new PayeeAddress();
        payeeAddress.setAddressLine1(addressLinePrefix + "1");
        payeeAddress.setAddressLine2(addressLinePrefix + "2");
        payeeAddress.setAddressLine3(addressLinePrefix + "3");
        payeeAddress.setAddressLine4(addressLinePrefix + "4");
        payeeAddress.setAddressLine5(addressLinePrefix + "5");
        payeeAddress.setPostcode("PP1 1PP");

        Payee payee = new Payee();

        payee.setName("payee");
        payee.setAddress(payeeAddress);
        payee.setTelephoneNumber("00001 000001");
        payee.setDxNumber("00002 000002");
        payee.setFaxNumber("00003 000003");
        payee.setEmail("payee@payee.com");
        payee.setPcm("LE");
        payee.setReference("payee ref");
        payee.setBankAccountNumber("1234567890");
        payee.setBankAccountHolder("bank account holder");
        payee.setBankSortCode("00-00-00");
        payee.setBankName("The Bank");
        payee.setBankInfo1("Bank info1");
        payee.setBankInfo2("Bank info2");
        payee.setSlipCodeline1("Slip code 1");
        payee.setSlipCodeline2("Slip code 2");
        payee.setGiroAccountNumber("0987654321");
        payee.setGiroTransCode1("giro 1");
        payee.setGiroTransCode2("giro 2");
        payee.setApacsTransCode("APACS code");

        return payee;
    }
}
