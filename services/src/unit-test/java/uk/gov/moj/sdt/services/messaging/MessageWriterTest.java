package uk.gov.moj.sdt.services.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.jms.core.JmsTemplate;
import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.logging.PerformanceLogger;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Test class for testing the MessageWriter implementation.
 *
 * @author Manoj Kulkarni
 */
@ExtendWith(MockitoExtension.class)
class MessageWriterTest extends AbstractSdtUnitTestBase {

    private static final String UNIT_TEST_QUEUE = "UnitTestQueue";

    private static final String UNIT_TEST = "UNITTEST";

    /**
     * MessageWriter for mocking.
     */
    private MessageWriter messageWriter;

    private static final String SUCCESS = "Success";

    private static final String NOT_EXPECTED_TO_FAIL = "Not Expected to fail";

    /**
     * Set up the variables.
     */
    @BeforeEach
    @Override
    public void setUp() {
        JmsTemplate jmsTemplate = mock(JmsTemplate.class);
        QueueConfig queueConfig = new QueueConfig();
        Map<String, String> mockedMap = new HashMap<>();
        queueConfig.setTargetAppQueue(mockedMap);
        messageWriter = new MessageWriter(jmsTemplate, queueConfig);
    }

    /**
     * Test method to test the sending of message.
     */
    @Test
    void testQueueMessageWithEmptyTargetApp() {
        // Setup finished, now tell the mock what to expect.
        final ISdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference("Test");

        // Send the message.
        try {
            messageWriter.queueMessage(sdtMessage, null, false);
            fail("Should have thrown an Illegal Argument Exception as the target app code is null.");
        } catch (final IllegalArgumentException e) {
            assertTrue(true, "Illegal Argument specified for the target application");
        }
    }

    /**
     * Test message with an invalid queue name i.e. where the target application
     * to queue name map is not valid
     */
    @Test
    void testQueueMessageWithInvalidTarget() {
        // Setup finished, now tell the mock what to expect.
        final ISdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference("Test");

        // Send the message.
        try {
            messageWriter.queueMessage(sdtMessage, "UnitTest", false);
            fail("Should have thrown an Illegal Argument exception here as the target app code is invalid");
        } catch (final IllegalArgumentException e) {
            assertTrue(true, "Target application code does not have a mapped queue name");
        }
    }

    /**
     * Test for a valid queue message on the MessageWriter.
     */
    @Test
    void testQueueMessage() {
        // Setup finished, now tell the mock what to expect.
        final ISdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference("Test");

        // Send the message.
        try {
            final Map<String, String> queueNameMap = new HashMap<>();
            queueNameMap.put(UNIT_TEST, UNIT_TEST_QUEUE);

            messageWriter.setQueueNameMap(queueNameMap);

            messageWriter.queueMessage(sdtMessage, UNIT_TEST, false);
            assertTrue(true, SUCCESS);
        } catch (final IllegalArgumentException e) {
            fail(NOT_EXPECTED_TO_FAIL);
        }

    }

    /**
     * Test for a valid queue message on the MessageWriter for dead letter.
     */
    @Test
    void testQueueMessageForDeadLetter() {
            // Setup finished, now tell the mock what to expect.
            final ISdtMessage sdtMessage = new SdtMessage();
            sdtMessage.setSdtRequestReference("Test");

            // Send the message.
            try {
                final Map<String, String> queueNameMap = new HashMap<>();
                queueNameMap.put(UNIT_TEST, UNIT_TEST_QUEUE);

                messageWriter.setQueueNameMap(queueNameMap);

                messageWriter.queueMessage(sdtMessage, UNIT_TEST, true);
                assertTrue(true, SUCCESS);
            } catch (final IllegalArgumentException e) {
                fail(NOT_EXPECTED_TO_FAIL);
            }
    }
    @Test
    void testQueueMessageForDeadLetterPerformanceLogging() {

        try (
            MockedStatic<PerformanceLogger> mockStaticPerformanceLogger
                = Mockito.mockStatic(PerformanceLogger.class)
        ) {
            when(PerformanceLogger.isPerformanceEnabled(anyLong())).thenReturn(true);
            // Setup finished, now tell the mock what to expect.
            final ISdtMessage sdtMessage = new SdtMessage();
            sdtMessage.setSdtRequestReference("Test");

            // Send the message.
            try {
                final Map<String, String> queueNameMap = new HashMap<>();
                queueNameMap.put(UNIT_TEST, UNIT_TEST_QUEUE);

                messageWriter.setQueueNameMap(queueNameMap);

                messageWriter.queueMessage(sdtMessage, UNIT_TEST, true);
                assertTrue(true, SUCCESS);
            } catch (final IllegalArgumentException e) {
                fail(NOT_EXPECTED_TO_FAIL);
            }
            mockStaticPerformanceLogger.verify(
                () -> PerformanceLogger.log(any(),eq(PerformanceLogger.LOGGING_POINT_5),
                                            eq("Enqueue message"), eq("\n\n\tsdt request reference=Test\n")));
        }
    }
}
