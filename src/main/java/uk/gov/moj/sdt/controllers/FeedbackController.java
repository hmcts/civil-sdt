package uk.gov.moj.sdt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.moj.sdt.request.CMCFeedback;
import uk.gov.moj.sdt.service.FeedbackService;

@RestController
@Validated
public class FeedbackController {

    private FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/CMCFeedback")
    void feedback(@RequestHeader("SDTREQUESTID") String sdtRequestId,
                  @RequestBody CMCFeedback cmcFeedback) {
        feedbackService.feedback(sdtRequestId, cmcFeedback);
    }
}
