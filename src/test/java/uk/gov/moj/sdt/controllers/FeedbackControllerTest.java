package uk.gov.moj.sdt.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.request.CMCFeedback;
import uk.gov.moj.sdt.service.FeedbackService;

import java.util.Calendar;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FeedbackControllerTest {

    @Mock
    protected FeedbackService feedbackService;

    @InjectMocks
    private FeedbackController feedbackController;

    private static final String SDT_REQUEST_ID = "MCOL-10012013010101-100099999";

    private String CLAIM_ERROR = "Claim Error";

    private String CLAIM_NUMBER = "123456";

    private int ERROR_CODE = 1234;

    @Test
    void shouldInvokeFeedbackService() {
        CMCFeedback cmcFeedback = new CMCFeedback();
        cmcFeedback.setErrorCode(ERROR_CODE);
        cmcFeedback.setErrorText(CLAIM_ERROR);
        cmcFeedback.setClaimNumber(CLAIM_NUMBER);
        cmcFeedback.setIssueDate(Calendar.getInstance().getTime());
        cmcFeedback.setServiceDate(Calendar.getInstance().getTime());
        feedbackController.feedback(SDT_REQUEST_ID, cmcFeedback);
        verify(feedbackService).feedback(SDT_REQUEST_ID, cmcFeedback);
    }
}
