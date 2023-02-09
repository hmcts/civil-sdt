package uk.gov.moj.sdt.services.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class SdtMessageTest {

    private static final String TEST_REFERENCE = "Test_Reference";
    @Test
    void getSdtRequestReferenceTest(){
        SdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference(TEST_REFERENCE);

        assertEquals(TEST_REFERENCE, sdtMessage.getSdtRequestReference());
    }
    @Test
    void getMessageSentTimestampTest(){
        SdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setMessageSentTimestamp(1L);

        assertEquals(sdtMessage.getMessageSentTimestamp(),1L);
    }

    @Test
    void getEnqueueLoggingIdTest(){
        SdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setEnqueueLoggingId(1L);

        assertEquals(sdtMessage.getEnqueueLoggingId(),1L);
    }


    @Test
    void toStringTest(){

        SdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference(TEST_REFERENCE);
        boolean actual = sdtMessage.toString().contains(TEST_REFERENCE);
        assertTrue(actual,"Should contain Test_Reference");
    }
}
