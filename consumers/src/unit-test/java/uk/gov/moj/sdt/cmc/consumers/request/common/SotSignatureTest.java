package uk.gov.moj.sdt.cmc.consumers.request.common;

import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SotSignatureTest extends AbstractSdtUnitTestBase {

    private static final String STATEMENT_OF_TRUTH_NAME = "Test SoT Name";

    private SotSignature sotSignature;

    @Override
    protected void setUpLocalTests() {
        sotSignature = new SotSignature();
    }

    @Test
    void testGetName() {
        setAccessibleField(SotSignature.class, "name", String.class, sotSignature, STATEMENT_OF_TRUTH_NAME);

        assertEquals(STATEMENT_OF_TRUTH_NAME, sotSignature.getName(), "SotSignature does not have expected name");
    }

    @Test
    void testGetFlag() {
        setAccessibleField(SotSignature.class, "flag", Boolean.class, sotSignature, Boolean.TRUE);

        assertTrue(sotSignature.getFlag(), "SotSignature flag should be true");
    }
}
