package uk.gov.moj.sdt.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlConverter;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimResponse;
import uk.gov.moj.sdt.cmc.consumers.response.JudgementWarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.JudgmentWarrantStatus;
import uk.gov.moj.sdt.cmc.consumers.response.ProcessingStatus;
import uk.gov.moj.sdt.cmc.consumers.response.WarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponse;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.request.CMCFeedback;
import uk.gov.moj.sdt.utils.cmc.RequestType;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private IIndividualRequestDao individualRequestDao;

    private final XmlConverter xmlToObject = new XmlConverter();

    private FeedbackService feedbackService;

    private static final String SDT_REQUEST_ID = "MCOL-10012013010101-100099999";

    private final String CLAIM_ERROR = "Claim Error";

    private final String WARRANT_NUMBER = "123457";

    private final String ENFORCING_COURT_NAME = "London Court";

    private final String ENFORCING_COURT_CODE = "COURT-1234";

    private final String ERROR_CODE = "1234";

    private IndividualRequest individualRequest;

    @BeforeEach
    public void setUp() {
        feedbackService = new FeedbackService(individualRequestDao, xmlToObject);
        individualRequest = new IndividualRequest();
    }

    @Test
    void shouldPersistClaim() throws IOException {
        String CLAIM_NUMBER = "123456";
        when(individualRequestDao.getRequestBySdtReference(SDT_REQUEST_ID)).thenReturn(individualRequest);
        individualRequest.setRequestType(RequestType.CLAIM.getType());
        CMCFeedback cmcFeedback = new CMCFeedback();
        cmcFeedback.setErrorCode(ERROR_CODE);
        cmcFeedback.setErrorText(CLAIM_ERROR);
        cmcFeedback.setClaimNumber(CLAIM_NUMBER);
        Date serviceDateAndIssueDate = getDate();
        cmcFeedback.setIssueDate(serviceDateAndIssueDate);
        cmcFeedback.setServiceDate(serviceDateAndIssueDate);
        feedbackService.feedback(SDT_REQUEST_ID, cmcFeedback);

        assertEquals(ERROR_CODE, individualRequest.getErrorLog().getErrorCode());
        assertEquals(CLAIM_ERROR, individualRequest.getErrorLog().getErrorText());
        byte[] targetApplicationResponse = individualRequest.getTargetApplicationResponse();
        ClaimResponse claimResponse = xmlToObject.convertXmlToObject(
            new String(targetApplicationResponse), ClaimResponse.class);
        assertEquals(CLAIM_NUMBER, claimResponse.getClaimNumber());
        assertEquals(serviceDateAndIssueDate, claimResponse.getServiceDate());
        assertEquals(serviceDateAndIssueDate, claimResponse.getIssueDate());
        verify(individualRequestDao).persist(individualRequest);
    }

    @Test
    void shouldPersistJudgment() throws IOException {
        when(individualRequestDao.getRequestBySdtReference(SDT_REQUEST_ID)).thenReturn(individualRequest);
        individualRequest.setRequestType(RequestType.JUDGMENT.getType());
        CMCFeedback cmcFeedback = new CMCFeedback();
        cmcFeedback.setErrorCode(ERROR_CODE);
        cmcFeedback.setErrorText(CLAIM_ERROR);
        Date date = getDate();
        cmcFeedback.setJudgmentEnteredDate(date);
        cmcFeedback.setFirstPaymentDate(date);
        feedbackService.feedback(SDT_REQUEST_ID, cmcFeedback);

        assertEquals(ERROR_CODE, individualRequest.getErrorLog().getErrorCode());
        assertEquals(CLAIM_ERROR, individualRequest.getErrorLog().getErrorText());
        byte[] targetApplicationResponse = individualRequest.getTargetApplicationResponse();
        JudgementResponse judgementResponse = xmlToObject.convertXmlToObject(
            new String(targetApplicationResponse), JudgementResponse.class);
        assertEquals(date, judgementResponse.getJudgmentEnteredDate());
        assertEquals(date, judgementResponse.getFirstPaymentDate());
        verify(individualRequestDao).persist(individualRequest);
    }

    @Test
    void shouldPersistBreathingSpace() {
        when(individualRequestDao.getRequestBySdtReference(SDT_REQUEST_ID)).thenReturn(individualRequest);
        individualRequest.setRequestType(RequestType.BREATHING_SPACE.getType());
        CMCFeedback cmcFeedback = new CMCFeedback();
        cmcFeedback.setErrorCode(ERROR_CODE);
        cmcFeedback.setErrorText(CLAIM_ERROR);
        cmcFeedback.setProcessingStatus(ProcessingStatus.PROCESSED.name());
        feedbackService.feedback(SDT_REQUEST_ID, cmcFeedback);

        assertEquals(ERROR_CODE, individualRequest.getErrorLog().getErrorCode());
        assertEquals(CLAIM_ERROR, individualRequest.getErrorLog().getErrorText());
        assertEquals(ProcessingStatus.PROCESSED.name(), individualRequest.getRequestStatus());
        verify(individualRequestDao).persist(individualRequest);
    }

    @Test
    void shouldPersistClaimStatusUpdate() {
        when(individualRequestDao.getRequestBySdtReference(SDT_REQUEST_ID)).thenReturn(individualRequest);
        individualRequest.setRequestType(RequestType.CLAIM_STATUS_UPDATE.getType());
        CMCFeedback cmcFeedback = new CMCFeedback();
        cmcFeedback.setErrorCode(ERROR_CODE);
        cmcFeedback.setErrorText(CLAIM_ERROR);
        cmcFeedback.setProcessingStatus(ProcessingStatus.PROCESSED.name());
        feedbackService.feedback(SDT_REQUEST_ID, cmcFeedback);

        assertEquals(ERROR_CODE, individualRequest.getErrorLog().getErrorCode());
        assertEquals(CLAIM_ERROR, individualRequest.getErrorLog().getErrorText());
        assertEquals(ProcessingStatus.PROCESSED.name(), individualRequest.getRequestStatus());
        verify(individualRequestDao).persist(individualRequest);
    }

    @Test
    void shouldPersistWarrant() throws IOException {
        when(individualRequestDao.getRequestBySdtReference(SDT_REQUEST_ID)).thenReturn(individualRequest);
        individualRequest.setRequestType(RequestType.WARRANT.getType());
        CMCFeedback cmcFeedback = new CMCFeedback();
        cmcFeedback.setErrorCode(ERROR_CODE);
        cmcFeedback.setErrorText(CLAIM_ERROR);
        cmcFeedback.setFee(100L);
        cmcFeedback.setWarrantNumber(WARRANT_NUMBER);
        cmcFeedback.setEnforcingCourtCode(ENFORCING_COURT_CODE);
        cmcFeedback.setEnforcingCourtName(ENFORCING_COURT_NAME);
        feedbackService.feedback(SDT_REQUEST_ID, cmcFeedback);

        assertEquals(ERROR_CODE, individualRequest.getErrorLog().getErrorCode());
        assertEquals(CLAIM_ERROR, individualRequest.getErrorLog().getErrorText());
        byte[] targetApplicationResponse = individualRequest.getTargetApplicationResponse();
        WarrantResponse warrantResponse = xmlToObject.convertXmlToObject(
            new String(targetApplicationResponse), WarrantResponse.class);
        assertEquals(WARRANT_NUMBER, warrantResponse.getWarrantNumber());
        assertEquals(ENFORCING_COURT_CODE, warrantResponse.getEnforcingCourtCode());
        assertEquals(ENFORCING_COURT_NAME, warrantResponse.getEnforcingCourtName());
        assertEquals(100L, warrantResponse.getFee());
        verify(individualRequestDao).persist(individualRequest);
    }

    @Test
    void shouldPersistJudgementWarrant() throws IOException {
        when(individualRequestDao.getRequestBySdtReference(SDT_REQUEST_ID)).thenReturn(individualRequest);
        individualRequest.setRequestType(RequestType.JUDGMENT_WARRANT.getType());
        CMCFeedback cmcFeedback = new CMCFeedback();
        cmcFeedback.setErrorCode(ERROR_CODE);
        cmcFeedback.setErrorText(CLAIM_ERROR);
        cmcFeedback.setJudgmentWarrantStatus(JudgmentWarrantStatus.JUDGMENT_ACCEPTED_WARRANT_ACCEPTED_BY_CCBC.getMessage());
        cmcFeedback.setFee(100L);
        cmcFeedback.setWarrantNumber(WARRANT_NUMBER);
        cmcFeedback.setEnforcingCourtCode(ENFORCING_COURT_CODE);
        cmcFeedback.setEnforcingCourtName(ENFORCING_COURT_NAME);
        Date date = getDate();
        cmcFeedback.setJudgmentEnteredDate(date);
        cmcFeedback.setFirstPaymentDate(date);
        cmcFeedback.setIssueDate(date);
        feedbackService.feedback(SDT_REQUEST_ID, cmcFeedback);

        assertEquals(ERROR_CODE, individualRequest.getErrorLog().getErrorCode());
        assertEquals(CLAIM_ERROR, individualRequest.getErrorLog().getErrorText());
        byte[] targetApplicationResponse = individualRequest.getTargetApplicationResponse();
        JudgementWarrantResponse response = xmlToObject.convertXmlToObject(
            new String(targetApplicationResponse), JudgementWarrantResponse.class);
        assertEquals(WARRANT_NUMBER, response.getWarrantNumber());
        assertEquals(ENFORCING_COURT_CODE, response.getEnforcingCourtCode());
        assertEquals(ENFORCING_COURT_NAME, response.getEnforcingCourtName());
        assertEquals(date, response.getFirstPaymentDate());
        assertEquals(date, response.getJudgmentEnteredDate());
        assertEquals(date, response.getIssueDate());
        assertEquals(100L, response.getFee());
        assertEquals(JudgmentWarrantStatus.JUDGMENT_ACCEPTED_WARRANT_ACCEPTED_BY_CCBC, response.getJudgmentWarrantStatus());
        verify(individualRequestDao).persist(individualRequest);
    }

    private Date getDate() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.parse("2023-11-23");
        } catch (ParseException e) {
            return Calendar.getInstance().getTime();
        }
    }
}
