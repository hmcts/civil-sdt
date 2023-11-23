package uk.gov.moj.sdt.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlConverter;
import uk.gov.moj.sdt.cmc.consumers.response.JudgmentWarrantStatus;
import uk.gov.moj.sdt.cmc.consumers.response.ProcessingStatus;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.request.CMCFeedback;
import uk.gov.moj.sdt.utils.cmc.RequestType;

import java.io.IOException;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    IIndividualRequestDao individualRequestDao;

    @Mock
    XmlConverter xmlToObject;

    private FeedbackService feedbackService;

    private static final String SDT_REQUEST_ID = "MCOL-10012013010101-100099999";

    private final String CLAIM_ERROR = "Claim Error";

    private final String WARRANT_NUMBER = "123457";

    private final String ENFORCING_COURT_NAME = "London Court";

    private final String ENFORCING_COURT_CODE = "COURT-1234";

    private final String ERROR_CODE = "1234";

    IndividualRequest individualRequest;

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
        when(xmlToObject.convertObjectToXml(any())).thenReturn("");
        CMCFeedback cmcFeedback = new CMCFeedback();
        cmcFeedback.setErrorCode(ERROR_CODE);
        cmcFeedback.setErrorText(CLAIM_ERROR);
        cmcFeedback.setClaimNumber(CLAIM_NUMBER);
        cmcFeedback.setIssueDate(Calendar.getInstance().getTime());
        cmcFeedback.setServiceDate(Calendar.getInstance().getTime());
        feedbackService.feedback(SDT_REQUEST_ID, cmcFeedback);
        assertEquals(ERROR_CODE, individualRequest.getErrorLog().getErrorCode());
        verify(individualRequestDao).persist(individualRequest);
    }

    @Test
    void shouldPersistJudgment() throws IOException {
        when(individualRequestDao.getRequestBySdtReference(SDT_REQUEST_ID)).thenReturn(individualRequest);
        individualRequest.setRequestType(RequestType.JUDGMENT.getType());
        when(xmlToObject.convertObjectToXml(any())).thenReturn("");
        CMCFeedback cmcFeedback = new CMCFeedback();
        cmcFeedback.setErrorCode(ERROR_CODE);
        cmcFeedback.setErrorText(CLAIM_ERROR);
        cmcFeedback.setJudgmentEnteredDate(Calendar.getInstance().getTime());
        cmcFeedback.setFirstPaymentDate(Calendar.getInstance().getTime());
        feedbackService.feedback(SDT_REQUEST_ID, cmcFeedback);
        assertEquals(ERROR_CODE, individualRequest.getErrorLog().getErrorCode());
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
        assertEquals(ProcessingStatus.PROCESSED.name(), individualRequest.getRequestStatus());
        verify(individualRequestDao).persist(individualRequest);
    }

    @Test
    void shouldPersistWarrant() throws IOException {
        when(individualRequestDao.getRequestBySdtReference(SDT_REQUEST_ID)).thenReturn(individualRequest);
        individualRequest.setRequestType(RequestType.WARRANT.getType());
        when(xmlToObject.convertObjectToXml(any())).thenReturn("");
        CMCFeedback cmcFeedback = new CMCFeedback();
        cmcFeedback.setFee(100L);
        cmcFeedback.setWarrantNumber(WARRANT_NUMBER);
        cmcFeedback.setEnforcingCourtCode(ENFORCING_COURT_CODE);
        cmcFeedback.setEnforcingCourtName(ENFORCING_COURT_NAME);
        feedbackService.feedback(SDT_REQUEST_ID, cmcFeedback);
        verify(individualRequestDao).persist(individualRequest);
    }

    @Test
    void shouldPersistJudgementWarrant() throws IOException {
        when(individualRequestDao.getRequestBySdtReference(SDT_REQUEST_ID)).thenReturn(individualRequest);
        individualRequest.setRequestType(RequestType.JUDGMENT_WARRANT.getType());
        when(xmlToObject.convertObjectToXml(any())).thenReturn("");
        CMCFeedback cmcFeedback = new CMCFeedback();
        cmcFeedback.setJudgmentWarrantStatus(JudgmentWarrantStatus.JUDGMENT_ACCEPTED_WARRANT_ACCEPTED_BY_CCBC.name());
        cmcFeedback.setFee(100L);
        cmcFeedback.setWarrantNumber(WARRANT_NUMBER);
        cmcFeedback.setEnforcingCourtCode(ENFORCING_COURT_CODE);
        cmcFeedback.setEnforcingCourtName(ENFORCING_COURT_NAME);
        cmcFeedback.setJudgmentEnteredDate(Calendar.getInstance().getTime());
        cmcFeedback.setFirstPaymentDate(Calendar.getInstance().getTime());
        cmcFeedback.setIssueDate(Calendar.getInstance().getTime());
        feedbackService.feedback(SDT_REQUEST_ID, cmcFeedback);
        verify(individualRequestDao).persist(individualRequest);
    }
}
