package uk.gov.moj.sdt.cmc.consumers.request.judgement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.request.common.SotSignature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JudgementRequestTest {

    private static final String STATEMENT_OF_TRUTH_NAME = "Test SoT name";

    private JudgementRequest judgementRequest;

    @BeforeEach
    void setUp() {
        judgementRequest = new JudgementRequest();
    }

    @Test
    void testGetSotName() {
        SotSignature sotSignature = new SotSignature();
        sotSignature.setName(STATEMENT_OF_TRUTH_NAME);

        judgementRequest.setSotSignature(sotSignature);

        assertEquals(STATEMENT_OF_TRUTH_NAME,
                     judgementRequest.getSotName(),
                     "JudgementRequest has unexpected statement of truth name");
    }

    @Test
    void testGetSotNameNull() {
        judgementRequest.setSotSignature(null);

        String sotName = judgementRequest.getSotName();

        assertNotNull(sotName, "JudgementRequest statement of truth name should not be null");
        assertEquals("", sotName, "JudgmentRequest should have an empty string for statement of truth name");
    }
}
