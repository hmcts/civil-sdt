package uk.gov.moj.sdt.cmc.consumers.client.converter;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.client.BaseXmlTest;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgementRequest;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgmentType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlToObjectTest extends BaseXmlTest {

    private static final String JSON_RESPONSE = "{\"bsType\":\"BC\",\"caseManRef\":\"H0PR0001\","
        + "\"respondentId\":\"1\"}";

    private static final String JSON_STRING_REQUESTED = "{\"claimNumber\":\"H0PR0001\","
        + "\"defendantId\":\"1\","
        + "\"breathingSpaceNotificationType\":\"BC\"}";

    private static final String BREATHING_SPACE = "BreathingSpace.xml";

    private static final String JUDGEMENT = "Judgement.xml";

    private static final String JUDGEMENT_JSON = "Expected_Judgement.json";

    private XmlToObjectConverter xmlToObject = new XmlToObjectConverter();

    @BeforeEach
    public void setup() {
    }

    @Test
    void shouldConvertBreathingSpaceRequestToString() throws IOException {
        String xmlContent = readXmlAsString(BREATHING_SPACE);
        String request = xmlToObject.convertXmlToJson(xmlContent);
        assertNotNull(request);
        assertEquals(JSON_STRING_REQUESTED, request);
    }

    @Test
    void shouldConvertBreathingSpaceRequest() throws IOException {
        String xmlContent = readXmlAsString(BREATHING_SPACE);
        BreathingSpaceRequest request = xmlToObject.convertXmlToObject(xmlContent, BreathingSpaceRequest.class);
        assertNotNull(request);
        assertEquals("1", request.getRespondentId());
        assertEquals("H0PR0001", request.getCaseManRef());
        assertEquals("BC", request.getBsType());
    }

    @Test
    void shouldConvertBreathingSpaceRequestAndConvertToJson() throws IOException {
        String xmlContent = readXmlAsString(BREATHING_SPACE);
        BreathingSpaceRequest request = xmlToObject.convertXmlToObject(xmlContent, BreathingSpaceRequest.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(request);
        assertNotNull(jsonString);
        assertEquals(JSON_RESPONSE, jsonString);
    }

    @Test
    void shouldConvertJudgementRequestToString() throws IOException {
        String xmlContent = readXmlAsString(JUDGEMENT);
        String request = xmlToObject.convertXmlToJson(xmlContent);
        assertNotNull(request);
        String expectedContent = readFile(JUDGEMENT_JSON);
        assertEquals(expectedContent, request);
    }

    @Test
    void shouldConvertJudgementRequest() throws IOException {
        String xmlContent = readXmlAsString(JUDGEMENT);
        JudgementRequest request = xmlToObject.convertXmlToObject(xmlContent, JudgementRequest.class);
        assertNotNull(request);
        assertEquals("9QZ00005", request.getCaseManRef());
        assertEquals(JudgmentType.A, request.getJudgmentType());
        assertTrue(request.isSentParticularsSeparately());
        assertFalse(request.isJointJudgment());
        assertNotNull(request.getRespondent1Address());
        assertEquals("defendant1Address line1", request.getRespondent1Address().getAddressLine1());
        assertEquals("defendant1Address line2", request.getRespondent1Address().getAddressLine2());
        assertEquals("defendant1Address line3", request.getRespondent1Address().getAddressLine3());
        assertEquals("defendant1Address line4", request.getRespondent1Address().getAddressLine4());
        assertEquals("RG42 2DL", request.getRespondent1Address().getPostCode());
        assertNull(request.getRespondent1Address().getPostTown());
        assertNotNull(request.getRespondent1DOB());
        assertNull(request.getRespondent2Address());
        assertNull(request.getRespondent2DOB());
        assertNotNull(request.getPaymentSchedule());
        assertNull(request.getPaymentSchedule().getPaymentInFullBy());
        assertNull(request.getPaymentSchedule().getInstallmentAmount());
        assertNotNull(request.getPaymentSchedule().getImmediatePayment());
        assertNull(request.getPaymentSchedule().getInstallmentFrequency());
        assertEquals(99999, request.getInterest());
        assertEquals(5000, request.getSolicitorCost());
        assertEquals(10000000, request.getDeductedAmount());
        assertNull(request.getClaimAmountAdmitted());
        assertNull(request.getCourtFee());
        assertNull(request.getLegalCosts());
        assertNotNull(request.getPayee());
        assertEquals("payee name", request.getPayee().getName());
        assertNotNull(request.getPayee().getAddress());
        assertEquals("payee line1", request.getPayee().getAddress().getAddressLine1());
        assertEquals("payee line2", request.getPayee().getAddress().getAddressLine2());
        assertEquals("payee line3", request.getPayee().getAddress().getAddressLine3());
        assertEquals("payee line4", request.getPayee().getAddress().getAddressLine4());
        assertNull(request.getPayee().getAddress().getPostTown());
        assertEquals("RG42 2DL", request.getPayee().getAddress().getPostCode());
        assertEquals("12345678901234", request.getPayee().getTelephoneNumber());
        assertEquals("payee dx number", request.getPayee().getDxNumber());
        assertEquals("payee fx number", request.getPayee().getFaxNumber());
        assertEquals("payee fx number", request.getPayee().getFaxNumber());
        assertEquals("payee email", request.getPayee().getEmail());
        assertEquals("12", request.getPayee().getPcm());
        assertEquals("payee reference", request.getPayee().getReference());
        assertEquals("123456789012345678", request.getPayee().getBankAccountNumber());
        assertEquals("payee bank account holder", request.getPayee().getBankAccountHolder());
        assertEquals("12345678", request.getPayee().getBankSortCode());
        assertEquals("payee bank name", request.getPayee().getBankName());
        assertEquals("payee bank info1", request.getPayee().getBankInfo1());
        assertEquals("payee bank info2", request.getPayee().getBankInfo2());
        assertEquals("payee slip code line1", request.getPayee().getSlipCodeline1());
        assertEquals("payee slip code line2", request.getPayee().getSlipCodeline2());
        assertEquals("12345678", request.getPayee().getGiroAccountNumber());
        assertEquals("123456789", request.getPayee().getGiroTransCode1());
        assertEquals("12", request.getPayee().getGiroTransCode2());
        assertEquals("sotSignature", request.getSotName());
    }
}
