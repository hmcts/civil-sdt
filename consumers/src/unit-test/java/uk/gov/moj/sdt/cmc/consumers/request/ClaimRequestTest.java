package uk.gov.moj.sdt.cmc.consumers.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.request.claim.ClaimRequest;
import uk.gov.moj.sdt.cmc.consumers.request.claim.Claimant;
import uk.gov.moj.sdt.cmc.consumers.request.claim.Defendant;
import uk.gov.moj.sdt.cmc.consumers.request.claim.Interest;
import uk.gov.moj.sdt.cmc.consumers.request.common.Address;
import uk.gov.moj.sdt.cmc.consumers.request.common.SotSignature;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test conversion of ClaimRequest to Json.  This simulates the conversion that takes
 * place when the ClaimRequest is sent to the createSDTClaim endpoint.
 */
class ClaimRequestTest extends RequestTestBase {

    private static final String BULK_CUSTOMER_ID = "10000001";
    private static final String CLAIMANT_REF = "claimantRef";
    private static final String STATEMENT_OF_TRUTH_NAME = "signature";

    private ClaimRequest claimRequest;

    @Override
    protected void setUpLocalTests() {
        super.setUpLocalTests();
        claimRequest = new ClaimRequest();
    }

    @Test
    void testConvertToJsonAllFields() throws JsonProcessingException {
        List<String> particulars = new ArrayList<>();
        particulars.add("particulars1");
        particulars.add("particulars2");

        claimRequest.setBulkCustomerId(BULK_CUSTOMER_ID);
        claimRequest.setClaimantReference(CLAIMANT_REF);
        claimRequest.setClaimant(createClaimant());
        claimRequest.setDefendant1(createDefendant(1));
        claimRequest.setDefendant2(createDefendant(2));
        claimRequest.setSendParticularsSeparately(true);
        claimRequest.setParticulars(particulars);
        claimRequest.setReserveRightToClaimInterest(true);
        claimRequest.setInterest(createInterest());
        claimRequest.setClaimAmount(100.0);
        claimRequest.setSolicitorCost(20.0);
        claimRequest.setSotSignature(createSotSignature(STATEMENT_OF_TRUTH_NAME));

        // Use pretty print to avoid having to declare expected value as one long hard to read string
        String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(claimRequest);

        String expectedResult = """
            {
              "bulkCustomerId" : "10000001",
              "claimantReference" : "claimantRef",
              "applicant" : {
                "name" : "claimant name",
                "primaryAddress" : {
                  "addressLine1" : "claimant line 1",
                  "addressLine2" : "claimant line 2",
                  "addressLine3" : "claimant line 3",
                  "posttown" : "claimant line 4",
                  "postcode" : "CC1 1CC"
                }
              },
              "respondent1" : {
                "name" : "defendant1 name",
                "primaryAddress" : {
                  "addressLine1" : "defendant1 line 1",
                  "addressLine2" : "defendant1 line 2",
                  "addressLine3" : "defendant1 line 3",
                  "posttown" : "defendant1 line 4",
                  "postcode" : "DD1 1DD"
                }
              },
              "respondent2" : {
                "name" : "defendant2 name",
                "primaryAddress" : {
                  "addressLine1" : "defendant2 line 1",
                  "addressLine2" : "defendant2 line 2",
                  "addressLine3" : "defendant2 line 3",
                  "posttown" : "defendant2 line 4",
                  "postcode" : "DD2 2DD"
                }
              },
              "sendParticularsSeparately" : true,
              "particularsLines" : [ "particulars1", "particulars2" ],
              "claimInterest" : true,
              "interestDailyAmount" : 10.0,
              "interestOwedDate" : "2024-08-01",
              "interestClaimDate" : "2024-08-10",
              "claimAmountInterestBase" : 5.0,
              "claimAmount" : 100.0,
              "solicitorCost" : 20.0,
              "statementOfTruthName" : "signature"
            }""";
        assertExpectedRequestJson(expectedResult, result);
    }

    @Test
    void testConvertToJsonMandatoryFieldsOnly() throws JsonProcessingException {
        List<String> particulars = new ArrayList<>();
        particulars.add("particulars1");

        claimRequest.setBulkCustomerId(BULK_CUSTOMER_ID);
        claimRequest.setClaimantReference(CLAIMANT_REF);
        claimRequest.setDefendant1(createDefendant(1));
        claimRequest.setSendParticularsSeparately(true);
        claimRequest.setParticulars(particulars);
        claimRequest.setReserveRightToClaimInterest(false);
        claimRequest.setClaimAmount(100.0);
        claimRequest.setSotSignature(createSotSignature(STATEMENT_OF_TRUTH_NAME));

        String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(claimRequest);

        String expectedResult = """
            {
              "bulkCustomerId" : "10000001",
              "claimantReference" : "claimantRef",
              "applicant" : null,
              "respondent1" : {
                "name" : "defendant1 name",
                "primaryAddress" : {
                  "addressLine1" : "defendant1 line 1",
                  "addressLine2" : "defendant1 line 2",
                  "addressLine3" : "defendant1 line 3",
                  "posttown" : "defendant1 line 4",
                  "postcode" : "DD1 1DD"
                }
              },
              "respondent2" : null,
              "sendParticularsSeparately" : true,
              "particularsLines" : [ "particulars1" ],
              "claimInterest" : false,
              "interestDailyAmount" : null,
              "interestOwedDate" : null,
              "interestClaimDate" : null,
              "claimAmountInterestBase" : null,
              "claimAmount" : 100.0,
              "solicitorCost" : null,
              "statementOfTruthName" : "signature"
            }""";
        assertExpectedRequestJson(expectedResult, result);
    }

    @Test
    void testGetStatementOfTruthName() {
        SotSignature sotSignature = createSotSignature(STATEMENT_OF_TRUTH_NAME);

        claimRequest.setSotSignature(sotSignature);

        assertEquals(STATEMENT_OF_TRUTH_NAME,
                     claimRequest.getStatementOfTruthName(),
                     "ClaimRequest has unexpected statement of truth name");
    }

    @Test
    void testGetStatementOfTruthNameNull() {
        claimRequest.setSotSignature(null);

        String statementOfTruthName = claimRequest.getStatementOfTruthName();
        assertNotNull(statementOfTruthName, "ClaimRequest statement of truth name should not be null");
        assertEquals("",
                     statementOfTruthName,
                     "ClaimRequest should have an empty string for the statement of truth name");
    }

    private Claimant createClaimant() {
        Address claimantAddress = createAddress("claimant", "CC1 1CC");

        Claimant claimant = new Claimant();
        claimant.setName("claimant name");
        claimant.setAddress(claimantAddress);

        return claimant;
    }

    private Defendant createDefendant(int defendantNumber) {
        String defendantPrefix = "defendant" + defendantNumber;
        String defendantPostcode = "DD" + defendantNumber + " " + defendantNumber + "DD";

        Address defendantAddress = createAddress(defendantPrefix, defendantPostcode);

        Defendant defendant = new Defendant();
        defendant.setName(defendantPrefix + " name");
        defendant.setAddress(defendantAddress);

        return defendant;
    }

    private Interest createInterest() {
        Interest interest = new Interest();

        interest.setDailyAmount(10.0);
        interest.setOwedDate(createDate(2024, 8, 1));
        interest.setClaimDate(createDate(2024, 8, 10));
        interest.setClaimAmountInterestBase(5.0);

        return interest;
    }
}
