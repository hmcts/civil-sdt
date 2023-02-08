package uk.gov.moj.sdt.services.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class SdtMessageTest {

    private static final String TEST_REFERENCE = "Test_Reference";
    @Test
    void getSdtRequestReferenceTest(){
        SdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference(TEST_REFERENCE);

        assertEquals(sdtMessage.getSdtRequestReference(),TEST_REFERENCE);
    }
    @Test
    void getMessageSentTimestampTest(){
        SdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setMessageSentTimestamp(Instant.now().getEpochSecond());

        assertNotNull(sdtMessage.getMessageSentTimestamp());
    }

    @Test
    void getEnqueueLoggingIdTest(){
        SdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setEnqueueLoggingId(1L);

        assertNotNull(sdtMessage.getEnqueueLoggingId());
    }


    @Test
    void toStringTest(){

        SdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference(TEST_REFERENCE);
        boolean actual = sdtMessage.toString().contains(TEST_REFERENCE);
        assertTrue(actual,"Should contain Test_Reference");
    }
}
