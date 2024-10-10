package uk.gov.moj.sdt.cmc.consumers.client.converter;

import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.client.BaseXmlTest;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlConverter;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimResponseDetail;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimStatusUpdateResponse;
import uk.gov.moj.sdt.cmc.consumers.response.JudgementWarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.JudgementWarrantResponseDetail;
import uk.gov.moj.sdt.cmc.consumers.response.WarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.WarrantResponseDetail;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponse;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponseDetail;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static uk.gov.moj.sdt.cmc.consumers.response.JudgmentWarrantStatus.JUDGMENT_ACCEPTED_BY_CCBC;
import static uk.gov.moj.sdt.cmc.consumers.response.JudgmentWarrantStatus.JUDGMENT_ACCEPTED_WARRANT_ACCEPTED_BY_CCBC;
import static uk.gov.moj.sdt.cmc.consumers.response.JudgmentWarrantStatus.WARRANT_ACCEPTED_BY_CCBC;
import static uk.gov.moj.sdt.cmc.consumers.response.ResponseStatus.ACCEPTED;

class ObjectToXmlTest extends BaseXmlTest {
    private static final String BREATHING_SPACE_RESPONSE = "BreathingSpaceResponse.xml";

    private static final String CLAIM_RESPONSE = "ClaimResponse.xml";
    private static final String CLAIM_RESPONSE_DETAIL = "ClaimResponseDetail.xml";

    private static final String CLAIM_STATUS_UPDATE_RESPONSE = "ClaimStatusUpdateResponse.xml";

    private static final String JUDGMENT_RESPONSE = "JudgementResponse.xml";
    private static final String JUDGMENT_RESPONSE_DETAIL = "JudgementResponseDetail.xml";

    private static final String JUDGMENT_WARRANT_RESPONSE = "JudgementWarrantResponse.xml";
    private static final String JUDGMENT_WARRANT_RESPONSE_DETAIL = "JudgementWarrantResponseDetail.xml";

    private static final String WARRANT_RESPONSE = "WarrantResponse.xml";
    private static final String WARRANT_RESPONSE_DETAIL = "WarrantResponseDetail.xml";

    private final XmlConverter xmlToObject = new XmlConverter();

    @Test
    void shouldConvertBreathingSpaceResponse() throws IOException {
        BreathingSpaceResponse breathingSpaceResponse = new BreathingSpaceResponse();
        breathingSpaceResponse.setResponseStatus(ACCEPTED);

        String result = xmlToObject.convertObjectToXml(breathingSpaceResponse);
        assertResponseXml(BREATHING_SPACE_RESPONSE, result);
    }

    @Test
    void shouldConvertClaimResponse() throws IOException {
        ClaimResponseDetail claimResponseDetail = createClaimResponseDetail();

        ClaimResponse claimResponse = new ClaimResponse();
        claimResponse.setResponseStatus(ACCEPTED);
        claimResponse.setClaimResponseDetail(claimResponseDetail);

        String result = xmlToObject.convertObjectToXml(claimResponse);
        assertResponseXml(CLAIM_RESPONSE, result);
    }

    @Test
    void shouldConvertClaimResponseDetail() throws IOException {
        ClaimResponseDetail claimResponseDetail = createClaimResponseDetail();

        String result = xmlToObject.convertObjectToXml(claimResponseDetail);
        assertResponseXml(CLAIM_RESPONSE_DETAIL, result);
    }

    @Test
    void shouldConvertClaimStatusUpdateResponse() throws IOException {
        ClaimStatusUpdateResponse claimStatusUpdateResponse = new ClaimStatusUpdateResponse();
        claimStatusUpdateResponse.setResponseStatus(ACCEPTED);

        String result = xmlToObject.convertObjectToXml(claimStatusUpdateResponse);
        assertResponseXml(CLAIM_STATUS_UPDATE_RESPONSE, result);
    }

    @Test
    void shouldConvertJudgementResponse() throws IOException {
        JudgementResponseDetail judgementResponseDetail = createJudgementResponseDetail();

        JudgementResponse judgementResponse = new JudgementResponse();
        judgementResponse.setResponseStatus(ACCEPTED);
        judgementResponse.setJudgementResponseDetail(judgementResponseDetail);

        String result = xmlToObject.convertObjectToXml(judgementResponse);
        assertResponseXml(JUDGMENT_RESPONSE, result);
    }

    @Test
    void shouldConvertJudgementResponseDetail() throws IOException {
        JudgementResponseDetail judgementResponseDetail = createJudgementResponseDetail();

        String result = xmlToObject.convertObjectToXml(judgementResponseDetail);
        assertResponseXml(JUDGMENT_RESPONSE_DETAIL, result);
    }

    @Test
    void shouldConvertJudgementWarrantResponse() throws IOException {
        JudgementWarrantResponseDetail judgementWarrantResponseDetail = createJudgementWarrantResponseDetail();

        JudgementWarrantResponse judgementWarrantResponse = new JudgementWarrantResponse();
        judgementWarrantResponse.setResponseStatus(ACCEPTED);
        judgementWarrantResponse.setJudgementWarrantResponseDetail(judgementWarrantResponseDetail);

        String result = xmlToObject.convertObjectToXml(judgementWarrantResponse);
        assertResponseXml(JUDGMENT_WARRANT_RESPONSE, result);
    }

    @Test
    void shouldConvertJudgementWarrantResponseDetail() throws IOException {
        JudgementWarrantResponseDetail judgementWarrantResponseDetail = createJudgementWarrantResponseDetail();

        String result = xmlToObject.convertObjectToXml(judgementWarrantResponseDetail);
        assertResponseXml(JUDGMENT_WARRANT_RESPONSE_DETAIL, result);
    }

    @Test
    void shouldConvertWarrantResponse() throws IOException {
        WarrantResponseDetail warrantResponseDetail = createWarrantResponseDetail();

        WarrantResponse warrantResponse = new WarrantResponse();
        warrantResponse.setResponseStatus(ACCEPTED);
        warrantResponse.setWarrantResponseDetail(warrantResponseDetail);

        String result = xmlToObject.convertObjectToXml(warrantResponse);
        assertResponseXml(WARRANT_RESPONSE, result);
    }

    @Test
    void shouldConvertWarrantResponseDetail() throws IOException {
        WarrantResponseDetail warrantResponseDetail = createWarrantResponseDetail();

        String result = xmlToObject.convertObjectToXml(warrantResponseDetail);
        assertResponseXml(WARRANT_RESPONSE_DETAIL, result);
    }

    private ClaimResponseDetail createClaimResponseDetail() {
        ClaimResponseDetail claimResponseDetail = new ClaimResponseDetail();

        claimResponseDetail.setClaimNumber("0000-0000-0000-0001");
        claimResponseDetail.setIssueDate(createDate(2024, 1, 1));
        claimResponseDetail.setServiceDate(createDate(2025, 2, 2));
        claimResponseDetail.setFee(100L);

        return claimResponseDetail;
    }

    private JudgementResponseDetail createJudgementResponseDetail() {
        JudgementResponseDetail judgementResponseDetail = new JudgementResponseDetail();

        judgementResponseDetail.setJudgmentEnteredDate(createDate(2024, 10, 1));
        judgementResponseDetail.setFirstPaymentDate(createDate(2024, 10, 4));
        judgementResponseDetail.setJudgmentWarrantStatus(JUDGMENT_ACCEPTED_BY_CCBC);

        return judgementResponseDetail;
    }

    private JudgementWarrantResponseDetail createJudgementWarrantResponseDetail() {
        JudgementWarrantResponseDetail judgementWarrantResponseDetail = new JudgementWarrantResponseDetail();

        judgementWarrantResponseDetail.setIssueDate(createDate(2024, 9, 1));
        judgementWarrantResponseDetail.setJudgmentEnteredDate(createDate(2024, 9, 30));
        judgementWarrantResponseDetail.setFirstPaymentDate(createDate(2024, 10, 1));
        judgementWarrantResponseDetail.setWarrantNumber("W12345678");
        judgementWarrantResponseDetail.setEnforcingCourtCode("123");
        judgementWarrantResponseDetail.setEnforcingCourtName("Test Court");
        judgementWarrantResponseDetail.setFee(100L);
        judgementWarrantResponseDetail.setJudgmentWarrantStatus(JUDGMENT_ACCEPTED_WARRANT_ACCEPTED_BY_CCBC);

        return judgementWarrantResponseDetail;
    }

    private WarrantResponseDetail createWarrantResponseDetail() {
        WarrantResponseDetail warrantResponseDetail = new WarrantResponseDetail();

        warrantResponseDetail.setIssueDate(createDate(2024, 9, 30));
        warrantResponseDetail.setWarrantNumber("W12345678");
        warrantResponseDetail.setEnforcingCourtCode("123");
        warrantResponseDetail.setEnforcingCourtName("Test Court");
        warrantResponseDetail.setFee(100L);
        warrantResponseDetail.setJudgmentWarrantStatus(WARRANT_ACCEPTED_BY_CCBC);

        return warrantResponseDetail;
    }

    private void assertResponseXml(String expectedResultFileName, String result) {
        // Use trim to remove any extra line break(s) from end of file
        String expectedResult = readXmlAsString(expectedResultFileName).trim();

        assertNotNull(result, "Response XML should not be null");
        assertEquals(expectedResult, result, "Response XML has unexpected value");
    }

    private Date createDate(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
