package uk.gov.moj.sdt.cmc.consumers.client.converter;

import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.client.BaseXmlTest;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlConverter;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.request.JudgementWarrantRequest;
import uk.gov.moj.sdt.cmc.consumers.request.UpdateType;
import uk.gov.moj.sdt.cmc.consumers.request.WarrantRequest;
import uk.gov.moj.sdt.cmc.consumers.request.claim.ClaimRequest;
import uk.gov.moj.sdt.cmc.consumers.request.claim.Claimant;
import uk.gov.moj.sdt.cmc.consumers.request.claim.Defendant;
import uk.gov.moj.sdt.cmc.consumers.request.common.Address;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgementRequest;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgmentType;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.Payee;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.PayeeAddress;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlToObjectTest extends BaseXmlTest {

    private static final String CLAIM_REQUEST = "Claim.xml";

    private static final String BREATHING_SPACE = "BreathingSpace.xml";

    private static final String CLAIM_STATUS_UPDATE = "ClaimStatusUpdate.xml";

    private static final String JUDGEMENT = "Judgement.xml";

    private static final String WARRANT_REQUEST = "WarrantRequest.xml";

    private static final String JUDGEMENT_WARRANT_REQUEST = "JudgementWarrantRequest.xml";

    private final XmlConverter xmlToObject = new XmlConverter();

    @Test
    void shouldConvertBreathingSpaceRequest() throws IOException {
        String xmlContent = readXmlAsString(BREATHING_SPACE);
        BreathingSpaceRequest request = xmlToObject.convertXmlToObject(xmlContent, BreathingSpaceRequest.class);

        assertNotNull(request, "BreathingSpaceRequest should not be null");
        assertEquals("1", request.getRespondentId(), "BreathingSpaceRequest has unexpected respondent id");
        assertEquals("H0PR0001", request.getCaseManRef(), "BreathingSpaceRequest has unexpected CaseMan ref");
        assertEquals("BC", request.getBsType(), "BreathingSpaceRequest has unexpected breathing space type");
    }

    @Test
    void shouldConvertJudgementRequest() throws IOException {
        String xmlContent = readXmlAsString(JUDGEMENT);
        JudgementRequest request = xmlToObject.convertXmlToObject(xmlContent, JudgementRequest.class);

        assertJudgementRequest(request);
    }

    @Test
    void shouldConvertClaimStatusUpdateRequest() throws IOException {
        String xmlContent = readXmlAsString(CLAIM_STATUS_UPDATE);
        ClaimStatusUpdateRequest request = xmlToObject.convertXmlToObject(xmlContent, ClaimStatusUpdateRequest.class);

        assertNotNull(request, "ClaimStatusUpdateRequest should not be null");
        assertEquals("1676030589543579",
                     request.getCaseManRef(),
                     "ClaimStatusUpdateRequest has unexpected CaseMan ref");
        assertEquals("1", request.getRespondentId(), "ClaimStatusUpdateRequest has unexpected respondent id");
        assertEquals(UpdateType.WD, request.getUpdateType(), "ClaimStatusUpdateRequest has unexpected update type");

        Date paidInFullDate = request.getPaidInFullDate();
        assertNotNull(paidInFullDate, "ClaimStatusUpdateRequest paid in full date should not be null");
        assertEquals(createDate(2022, 10, 23),
                     paidInFullDate,
                     "ClaimStatusUpdateRequest paid in full date has unexpected value");
        assertTrue(request.getSection38Compliancy(), "ClaimStatusUpdateRequest section 38 compliancy should be true");
    }

    @Test
    void shouldConvertWarrantRequest() throws IOException {
        String xmlContent = readXmlAsString(WARRANT_REQUEST);
        WarrantRequest request = xmlToObject.convertXmlToObject(xmlContent, WarrantRequest.class);

        assertWarrantRequest(request);
    }

    @Test
    void shouldConvertJudgementWarrantRequest() throws IOException {
        String xmlContent = readXmlAsString(JUDGEMENT_WARRANT_REQUEST);
        JudgementWarrantRequest request = xmlToObject.convertXmlToObject(xmlContent, JudgementWarrantRequest.class);

        assertNotNull(request, "JudgmementWarrantRequest should not be null");
        assertJudgementRequest(request.getJudgmentRequest());
        assertWarrantRequest(request.getWarrantRequest());
    }

    @Test
    void shouldConvertClaimRequest() throws IOException {
        String xmlContent = readXmlAsString(CLAIM_REQUEST);
        ClaimRequest request = xmlToObject.convertXmlToObject(xmlContent, ClaimRequest.class);

        assertClaimRequest(request);
    }

    private void assertJudgementRequest(JudgementRequest request) {
        assertNotNull(request, "JudgementRequest should not be null");

        assertEquals("9QZ00005", request.getCaseManRef(), "JudgementRequest has unexpected CaseMan ref");
        assertFalse(request.isJointJudgment(), "JudgementRequest joint judgment should be false");
        assertEquals(JudgmentType.A, request.getJudgmentType(), "JudgementRequest has unexpected judgment type");
        assertTrue(request.isSentParticularsSeparately(),
                   "JudgementRequest send particulars separately should be true");
        assertEquals("1", request.getRespondentId(), "JudgementRequest has unexpected respondent id");

        Address respondent1Address = request.getRespondent1Address();
        assertAddress(respondent1Address, "Respondent1", "defendant1Address", "RG42 2DL");
        assertEquals(createDate(1960, 10, 10),
                     request.getRespondent1DOB(),
                     "JudgmentRequest respondent1 date of birth has unexpected value");

        Address respondent2Address = request.getRespondent2Address();
        assertAddress(respondent2Address, "Respondent2", "defendant2Address", "RG42 2DL");
        assertEquals(createDate(1970, 9, 1),
                     request.getRespondent2DOB(),
                     "JudgmentRequest respondent2 date of birth has unexpected value");

        assertEquals("immediate",
                     request.getPaymentSchedule().getPaymentScheduleType(),
                     "JudgmentRequest has unexpected payment schedule type");
        assertEquals(99999, request.getInterest(), "JudgmentRequest has unexpected interest value");
        assertEquals(5000L, request.getSolicitorCost(), "JudgmentRequest has unexpected solicitor cost value");
        assertEquals(10000000L, request.getDeductedAmount(), "JudgmentRequest has unexpected deducted amount value");
        assertEquals(100L, request.getClaimAmountAdmitted(), "JudgmentRequest has unexpected claim amount admitted");
        assertEquals(200L, request.getCourtFee(), "JudgmentRequest has unexpected court fee");
        assertEquals(300L, request.getLegalCosts(), "JudgmentRequest has unexpected legal costs");

        Payee payee = request.getPayee();
        assertPayee(payee);

        assertEquals("sotSignature", request.getSotName(), "JudgmentRequest has unexpected statement of truth name");
    }

    private void assertWarrantRequest(WarrantRequest request) {
        assertNotNull(request, "WarrantRequest should not be null");

        assertEquals("9QZ00001", request.getCaseManRef(), "WarrantRequest has unexpected CaseMan ref");
        assertEquals("1", request.getRespondentId(), "WarrantRequest has unexpected respondent id");

        Address respondentAddress = request.getRespondentAddress();
        assertAddress(respondentAddress, "Respondent", "defendant1Address", "RG42 2DL");

        assertEquals(50000L, request.getBalanceOfDebt(), "WarrantRequest has unexpected balance of debt");
        assertEquals(10000L, request.getWarrantAmount(), "WarrantRequest has unexpected warrant amount");
        assertEquals(5000L, request.getSolicitorCost(), "WarrantRequest has unexpected solicitor cost");
        assertEquals("additional notes",
                     request.getAdditionalNotes(),
                     "WarrantRequest has unexpected additional notes");
        assertEquals("sotSignature", request.getSotName(), "WarrantRequest has unexpected statement of truth name");
    }

    private void assertPayee(Payee payee) {
        assertNotNull(payee, "Payee should not be null");
        assertEquals("payee name", payee.getName(), "Payee has unexpected name");

        PayeeAddress address = payee.getAddress();
        assertNotNull(address, "Payee address should not be null");
        assertEquals("payee line1", address.getAddressLine1(), "Payee address line 1 has unexpected value");
        assertEquals("payee line2", address.getAddressLine2(), "Payee address line 2 has unexpected value");
        assertEquals("payee line3", address.getAddressLine3(), "Payee address line 3 has unexpected value");
        assertEquals("payee line4", address.getAddressLine4(), "Payee address line 4 has unexpected value");
        assertEquals("payee line5", address.getPostTown(), "Payee address posttown has unexpected value");
        assertEquals("RG42 2DL", address.getPostcode(), "Payee address postcode has unexpected value");

        assertEquals("12345678901234", payee.getTelephoneNumber(), "Payee has unexpected telephone number");
        assertEquals("payee dx number", payee.getDxNumber(), "Payee has unexpected DX number");
        assertEquals("payee fx number", payee.getFaxNumber(), "Payee has unexpected fax number");
        assertEquals("payee email", payee.getEmail(), "Payee has unexpected email address");
        assertEquals("12", payee.getPcm(), "Payee has unexpected preferred communication method");
        assertEquals("payee reference", payee.getReference(), "Payee has unexpected reference");
        assertEquals("123456789012345678", payee.getBankAccountNumber(), "Payee has unexpected bank account number");
        assertEquals("payee bank account holder",
                     payee.getBankAccountHolder(),
                     "Payee has unexpected bank account holder");
        assertEquals("12345678", payee.getBankSortCode(), "Payee has unexpected bank sort code");
        assertEquals("payee bank name", payee.getBankName(), "Payee has unexpected bank name");
        assertEquals("payee bank info1", payee.getBankInfo1(), "Payee has unexpected bank info 1");
        assertEquals("payee bank info2", payee.getBankInfo2(), "Payee has unexpected bank info 2");
        assertEquals("payee slip code line1", payee.getSlipCodeline1(), "Payee has unexpected slip code line 1");
        assertEquals("payee slip code line2", payee.getSlipCodeline2(), "Payee has unexpected slip code line 2");
        assertEquals("12345678", payee.getGiroAccountNumber(), "Payee has unexpected Giro account number");
        assertEquals("123456789", payee.getGiroTransCode1(), "Payee has unexpected Giro trans code 1");
        assertEquals("12", payee.getGiroTransCode2(), "Payee has unexpected Giro trans code 2");
        assertEquals("12", payee.getApacsTransCode(), "Payee has unexpected Apacs trans code");
    }

    private void assertClaimRequest(ClaimRequest request) {
        assertNotNull(request, "ClaimRequest should not be null");

        assertEquals("claimRef", request.getClaimantReference(), "ClaimRequest has unexpected claimant reference");

        Claimant claimant = request.getClaimant();
        assertNotNull(claimant, "Claimant should not be null");
        assertEquals("claimantForename claimantSurname", claimant.getName(), "Claimant has unexpected name");
        assertAddress(claimant.getAddress(), "Claimant", "claimantAddress", "CC1 1CC");

        Defendant defendant1 = request.getDefendant1();
        assertDefendant(defendant1, 1, "defendant1Forename defendant1Surname", "defendant1Address", "DD1 1DD");

        Defendant defendant2 = request.getDefendant2();
        assertDefendant(defendant2, 2, "defendant2Forename defendant2Surname", "defendant2Address", "DD2 2DD");

        assertTrue(request.getSendParticularsSeparately(), "ClaimRequest SendParticularsSeparately should be true");
        assertTrue(request.getReserveRightToClaimInterest(), "ClaimRequest ReserveRightToClaimInterest should be true");

        assertEquals(1000, request.getInterestDailyAmount(), "Interest daily amount has unexpected value");
        assertEquals(createDate(2013, 8, 15), request.getInterestOwedDate(), "Interest owed date has unexpected value");
        assertEquals(createDate(2013, 8, 16),
                     request.getInterestClaimDate(),
                     "Interest claim date has unexpected value");
        assertEquals(10,
                     request.getClaimAmountInterestBase(),
                     "Interest claim amount interest base has unexpected value");

        assertEquals(9999999, request.getClaimAmount(), "ClaimRequest Claim amount has unexpected value");
        assertEquals(10000, request.getSolicitorCost(), "ClaimRequest Solicitor cost has unexpected value");

        assertParticulars(request.getParticulars());

        assertEquals("signature",
                     request.getStatementOfTruthName(),
                     "ClaimRequest has unexpected statement of truth name");
    }

    private void assertDefendant(Defendant defendant,
                                 int defendantId,
                                 String name,
                                 String addressPrefix,
                                 String postcode) {
        String messagePrefix = "Defendant" + defendantId;

        assertNotNull(defendant, messagePrefix + " should not be null");
        assertEquals(name, defendant.getName(), messagePrefix + " name has unexpected value");

        Address defendantAddress = defendant.getAddress();
        assertAddress(defendantAddress, messagePrefix, addressPrefix, postcode);
    }

    private void assertParticulars(List<String> particulars) {
        List<String> expectedParticulars = new ArrayList<>();
        expectedParticulars.add("particulars1");
        expectedParticulars.add("particulars2");

        assertNotNull(particulars, "ClaimRequest particulars should not be null");
        assertEquals(expectedParticulars.size(),
                     particulars.size(),
                     "ClaimRequest has unexpected number of particulars");
        assertEquals(expectedParticulars, particulars, "ClaimRequest has unexpected particulars");
    }

    private void assertAddress(Address address, String type, String prefix, String postcode) {
        assertNotNull(address, type + " address should not be null");
        assertEquals(prefix + " line1", address.getAddressLine1(), type + " address line 1 has unexpected value");
        assertEquals(prefix + " line2", address.getAddressLine2(), type + " address line 2 has unexpected value");
        assertEquals(prefix + " line3", address.getAddressLine3(), type + " address line 3 has unexpected value");
        assertEquals(prefix + " line4", address.getPosttown(), type + " address posttown has unexpected value");
        assertEquals(postcode, address.getPostcode(), type + " address postcode has unexpected value");
    }

    private Date createDate(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
