package uk.gov.moj.sdt.producers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.moj.sdt.cmc.model.CMCUpdateRequest;
import uk.gov.moj.sdt.handlers.api.ICMCFeedbackHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class CMCFeedbackController {

    private final ICMCFeedbackHandler cmcFeedbackHandler;

    @Autowired
    public CMCFeedbackController(@Qualifier("cmcFeedbackHandler") ICMCFeedbackHandler cmcFeedbackHandler) {
        this.cmcFeedbackHandler = cmcFeedbackHandler;
    }

    @PostMapping(value = "/CMCFeedback", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> cmcFeedback(@RequestHeader("sdtRequestId") String sdtRequestId,
                                            @RequestBody CMCUpdateRequest cmcUpdateRequest) {
        cmcFeedbackHandler.cmcFeedback(sdtRequestId, cmcUpdateRequest);

        return ResponseEntity.ok().build();
    }
}
