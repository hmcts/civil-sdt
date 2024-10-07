package uk.gov.moj.sdt.services.messaging;

import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


class SdtMessageTest extends AbstractSdtUnitTestBase {

    private SdtMessage sdtMessage;

    private static final String TEST_REFERENCE = "Test_Reference";

    @Override
    protected void setUpLocalTests() {
        sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference(TEST_REFERENCE);
        sdtMessage.setMessageSentTimestamp(1L);
        sdtMessage.setEnqueueLoggingId(1L);
    }

    @Test
    void getSdtRequestReferenceTest() {
        assertEquals(TEST_REFERENCE, sdtMessage.getSdtRequestReference());
    }
    @Test
    void getMessageSentTimestampTest() {
        assertEquals(1L, sdtMessage.getMessageSentTimestamp());
    }

    @Test
    void getEnqueueLoggingIdTest() {
        assertEquals(1L, sdtMessage.getEnqueueLoggingId());
    }


    @Test
    void toStringTest() {
        boolean actualToString = sdtMessage.toString().equals(
            "SdtMessage [sdtRequestReference=Test_Reference, messageSentTimestamp=1, enqueueLoggingId=1]");
        assertTrue(actualToString,"Should contain call parameters set");
    }
}
