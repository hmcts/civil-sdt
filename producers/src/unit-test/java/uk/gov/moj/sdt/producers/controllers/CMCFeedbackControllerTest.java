package uk.gov.moj.sdt.producers.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import uk.gov.moj.sdt.cmc.model.CMCUpdateRequest;
import uk.gov.moj.sdt.handlers.api.ICMCFeedbackHandler;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CMCFeedbackControllerTest extends AbstractSdtUnitTestBase {

    @Mock
    private ICMCFeedbackHandler mockCMCFeedbackHandler;

    private CMCFeedbackController cmcFeedbackController;

    @Override
    protected void setUpLocalTests() {
        cmcFeedbackController = new CMCFeedbackController(mockCMCFeedbackHandler);
    }

    @Test
    void cmcFeedback() {
        String sdtRequestId = "MCOL-20240724120000-000000001-0000001";
        CMCUpdateRequest cmcUpdateRequest = new CMCUpdateRequest();

        ResponseEntity<Void> response = cmcFeedbackController.cmcFeedback(sdtRequestId, cmcUpdateRequest);

        assertEquals(200, response.getStatusCode().value(), "Response has unexpected HTTP status");
        assertFalse(response.hasBody(), "Response should not have a body");

        verify(mockCMCFeedbackHandler).cmcFeedback(sdtRequestId, cmcUpdateRequest);
    }
}
