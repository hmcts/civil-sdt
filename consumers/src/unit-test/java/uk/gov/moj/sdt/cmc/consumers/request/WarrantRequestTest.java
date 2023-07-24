package uk.gov.moj.sdt.cmc.consumers.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.request.common.SotSignature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WarrantRequestTest {

    private static final String STATEMENT_OF_TRUTH_NAME = "Test SoT Name";

    private WarrantRequest warrantRequest;

    @BeforeEach
    void setup() {
        warrantRequest = new WarrantRequest();
    }

    @Test
    void testGetSotName() {
        SotSignature sotSignature = new SotSignature();
        sotSignature.setName(STATEMENT_OF_TRUTH_NAME);

        warrantRequest.setSotSignature(sotSignature);

        assertEquals(STATEMENT_OF_TRUTH_NAME,
                     warrantRequest.getSotName(),
                     "WarrantRequest has unexpected statement of truth name");
    }

    @Test
    void testGetSotNameNull() {
        warrantRequest.setSotSignature(null);

        String sotName = warrantRequest.getSotName();
        assertNotNull(sotName, "WarrantRequest statement of truth name should not be null");
        assertEquals("", sotName, "WarrantRequest should have an empty string for statement of truth name");
    }
}
