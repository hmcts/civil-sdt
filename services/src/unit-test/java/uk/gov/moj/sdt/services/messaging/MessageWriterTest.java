package uk.gov.moj.sdt.services.messaging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    private static final String NOT_EXPECTED_TO_FAIL = "Not Expected to fail";

    private static final String ILLEGAL_STATE_EXCEPTION_MESSAGE =
        "The MessageProducer was closed due to an unrecoverable error.; nested exception is " +
            "javax.jms.IllegalStateException: The MessageProducer was closed due to an unrecoverable error.";

    private MessageWriter messageWriter;

    @Mock
    private JmsTemplate mockJmsTemplate;

    @Mock
    private IIndividualRequestDao individualRequestDao;

    private ISdtMessage sdtMessage;

    /**
     * Set up the variables.
     */
    @BeforeEach
    @Override
    public void setUp() {
        QueueConfig queueConfig = new QueueConfig();
        Map<String, String> targetAppQueueMap = new HashMap<>();
        targetAppQueueMap.put(UNIT_TEST, UNIT_TEST_QUEUE);
        queueConfig.setTargetAppQueue(targetAppQueueMap);
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        when(individualRequestDao.getRequestBySdtReference(anyString())).thenReturn(individualRequest);
        messageWriter = new MessageWriter(mockJmsTemplate, queueConfig, individualRequestDao);

        sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference("Test");
    }

    @ParameterizedTest
    @MethodSource("invalidTargetAppCodes")
    void testQueueMessageInvalidArgumentException(String targetAppCode, String exceptionMessage) {
        try {
            messageWriter.queueMessage(sdtMessage, targetAppCode);
            fail("Should have thrown an Illegal Argument exception as the target app code is invalid");
        } catch (final IllegalArgumentException e) {
            // Expected exception thrown, continue with test
            assertEquals(exceptionMessage, e.getMessage(), "Unexpected exception message");
        }

        verify(mockJmsTemplate, never()).convertAndSend(anyString(), any(ISdtMessage.class));
        verify(individualRequestDao, never()).getRequestBySdtReference(any(String.class));
        verify(individualRequestDao, never()).persist(any(IIndividualRequestDao.class));
    }

    static Stream<Arguments> invalidTargetAppCodes() {
        return Stream.of(
            arguments(null, "Target application code must be supplied."),
            arguments("", "Target application code must be supplied."),
            arguments("UnitTest", "Target application code [UnitTest] does not have a JMS queue mapped.")
        );
    }

    /**
     * Test for a valid queue message on the MessageWriter.
     */
    @Test
    void testQueueMessage() {
        // Send the message.
        try {
            messageWriter.queueMessage(sdtMessage, UNIT_TEST);
            assertNotEquals(0L, sdtMessage.getMessageSentTimestamp());
            verify(mockJmsTemplate).convertAndSend(UNIT_TEST_QUEUE, sdtMessage);
            verify(individualRequestDao).getRequestBySdtReference(any(String.class));
            verify(individualRequestDao).persist(any(IIndividualRequestDao.class));
        } catch (final IllegalArgumentException e) {
            fail(NOT_EXPECTED_TO_FAIL);
        }
    }

    @Test
    void testQueueMessageUncategorizedJmsException() {
        UncategorizedJmsException uncategorizedJmsException = new UncategorizedJmsException("Some JMS exception");

        doThrow(uncategorizedJmsException).
            when(mockJmsTemplate).convertAndSend(UNIT_TEST_QUEUE, sdtMessage);

        Logger messageWriterLogger = (Logger) LoggerFactory.getLogger(MessageWriter.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        messageWriterLogger.addAppender(listAppender);

        messageWriter.queueMessage(sdtMessage, UNIT_TEST);

        List<ILoggingEvent> logsList = listAppender.list;
        ILoggingEvent lastLogEntry = logsList.get(logsList.size() - 1);

        assertEquals(Level.ERROR, lastLogEntry.getLevel(), "Last log entry does not have expected level");
        assertEquals("Failed to connect to the ServiceBus queue [UnitTestQueue] while queueing message request reference [Test]",
                     lastLogEntry.getFormattedMessage());

        listAppender.stop();
        messageWriterLogger.detachAndStopAllAppenders();

        verify(mockJmsTemplate).convertAndSend(UNIT_TEST_QUEUE, sdtMessage);
        verify(individualRequestDao).getRequestBySdtReference(any(String.class));
        verify(individualRequestDao).persist(any(IIndividualRequestDao.class));
    }

    @Test
    void testResetConnectionAndQueueMessage() {
        // Using two exception classes with the same name so need to qualify declarations
        javax.jms.IllegalStateException javaxIllegalStateException =
            new javax.jms.IllegalStateException(ILLEGAL_STATE_EXCEPTION_MESSAGE);
        org.springframework.jms.IllegalStateException springIllegalStateException =
            new org.springframework.jms.IllegalStateException(javaxIllegalStateException);

        // Set up behaviour of void convertAndSend() method.  Raise an exception the first time it's called,
        // then do nothing the second time.
        doThrow(springIllegalStateException).
            doNothing().
            when(mockJmsTemplate).convertAndSend(UNIT_TEST_QUEUE, sdtMessage);

        CachingConnectionFactory mockCachingConnectionFactory = mock(CachingConnectionFactory.class);
        when(mockJmsTemplate.getConnectionFactory()).thenReturn(mockCachingConnectionFactory);

        messageWriter.queueMessage(sdtMessage, UNIT_TEST);

        verify(mockJmsTemplate).getConnectionFactory();
        verify(mockCachingConnectionFactory).resetConnection();
        verify(mockJmsTemplate, times(2)).convertAndSend(UNIT_TEST_QUEUE, sdtMessage);
        verify(individualRequestDao).getRequestBySdtReference(any(String.class));
        verify(individualRequestDao).persist(any(IIndividualRequestDao.class));
    }

    @Test
    void testResetConnectionAndQueueMessageNoConnectionFactory() {
        javax.jms.IllegalStateException javaxIllegalStateException =
            new javax.jms.IllegalStateException(ILLEGAL_STATE_EXCEPTION_MESSAGE);
        org.springframework.jms.IllegalStateException springIllegalStateException =
            new org.springframework.jms.IllegalStateException(javaxIllegalStateException);

        doThrow(springIllegalStateException).
            when(mockJmsTemplate).convertAndSend(UNIT_TEST_QUEUE, sdtMessage);

        when(mockJmsTemplate.getConnectionFactory()).thenReturn(null);

        try {
            messageWriter.queueMessage(sdtMessage, UNIT_TEST);
            fail("IllegalStateException (java.lang) should be thrown");
        }
        catch (IllegalStateException e) {
            // Expected exception thrown, continue with test
            assertEquals("JmsTemplate has no connection factory", e.getMessage(), "Unexpected exception message");
        }

        verify(mockJmsTemplate).getConnectionFactory();
        verify(mockJmsTemplate).convertAndSend(UNIT_TEST_QUEUE, sdtMessage);
        verify(individualRequestDao).getRequestBySdtReference(any(String.class));
        verify(individualRequestDao).persist(any(IIndividualRequestDao.class));
    }

    @Test
    void testResetConnectionAndQueueMessageUncategorizedJmsException() {
        javax.jms.IllegalStateException javaxIllegalStateException =
            new javax.jms.IllegalStateException(ILLEGAL_STATE_EXCEPTION_MESSAGE);
        org.springframework.jms.IllegalStateException springIllegalStateException =
            new org.springframework.jms.IllegalStateException(javaxIllegalStateException);

        UncategorizedJmsException uncategorizedJmsException = new UncategorizedJmsException("Some JMS exception");

        // Set up behaviour of void convertAndSend() method.  Raise an IllegalStateException the first time it's called,
        // then an UncategorizedJmsException the second time.
        doThrow(springIllegalStateException).
            doThrow(uncategorizedJmsException).
            when(mockJmsTemplate).convertAndSend(UNIT_TEST_QUEUE, sdtMessage);

        CachingConnectionFactory mockCachingConnectionFactory = mock(CachingConnectionFactory.class);
        when(mockJmsTemplate.getConnectionFactory()).thenReturn(mockCachingConnectionFactory);

        Logger messageWriterLogger = (Logger) LoggerFactory.getLogger(MessageWriter.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        messageWriterLogger.addAppender(listAppender);

        messageWriter.queueMessage(sdtMessage, UNIT_TEST);

        List<ILoggingEvent> logsList = listAppender.list;
        ILoggingEvent lastLogEntry = logsList.get(logsList.size() - 1);

        assertEquals(Level.ERROR, lastLogEntry.getLevel(), "Last log entry does not have expected level");
        assertEquals("Failed to connect to the ServiceBus queue [UnitTestQueue] while queueing message request reference [Test]",
                     lastLogEntry.getFormattedMessage());

        listAppender.stop();
        messageWriterLogger.detachAndStopAllAppenders();

        verify(mockJmsTemplate).getConnectionFactory();
        verify(mockCachingConnectionFactory).resetConnection();
        verify(mockJmsTemplate, times(2)).convertAndSend(UNIT_TEST_QUEUE, sdtMessage);
        verify(individualRequestDao).getRequestBySdtReference(any(String.class));
        verify(individualRequestDao).persist(any(IIndividualRequestDao.class));
    }

    @Test
    void testResetConnectionAndQueueMessageOtherException() {
        javax.jms.IllegalStateException javaxIllegalStateException =
            new javax.jms.IllegalStateException(ILLEGAL_STATE_EXCEPTION_MESSAGE);
        org.springframework.jms.IllegalStateException springIllegalStateException =
            new org.springframework.jms.IllegalStateException(javaxIllegalStateException);

        javax.jms.InvalidClientIDException javaxInvalidClientIDException =
            new javax.jms.InvalidClientIDException("");
        org.springframework.jms.InvalidClientIDException jmsInvalidClientIDException =
            new org.springframework.jms.InvalidClientIDException(javaxInvalidClientIDException);

        // Set up behaviour of void convertAndSend() method.  Raise an IllegalStateException the first time it's called,
        // then an InvalidClientIDException the second time.
        doThrow(springIllegalStateException).
            doThrow(jmsInvalidClientIDException).
            when(mockJmsTemplate).convertAndSend(UNIT_TEST_QUEUE, sdtMessage);

        CachingConnectionFactory mockCachingConnectionFactory = mock(CachingConnectionFactory.class);
        when(mockJmsTemplate.getConnectionFactory()).thenReturn(mockCachingConnectionFactory);

        try {
            messageWriter.queueMessage(sdtMessage, UNIT_TEST);
            fail("InvalidClientIDException should be thrown");
        } catch (org.springframework.jms.InvalidClientIDException e) {
            // Expected exception thrown, continue with test
        }

        verify(mockJmsTemplate).getConnectionFactory();
        verify(mockCachingConnectionFactory).resetConnection();
        verify(mockJmsTemplate, times(2)).convertAndSend(UNIT_TEST_QUEUE, sdtMessage);
        verify(individualRequestDao).getRequestBySdtReference(any(String.class));
        verify(individualRequestDao).persist(any(IIndividualRequestDao.class));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"Other exception message"})
    void testOtherIllegalStateExceptionMessage(String exceptionMessage) {
        javax.jms.IllegalStateException javaxIllegalStateException =
            new javax.jms.IllegalStateException(exceptionMessage);
        org.springframework.jms.IllegalStateException springIllegalStateException =
            new org.springframework.jms.IllegalStateException(javaxIllegalStateException);

        doThrow(springIllegalStateException).
            when(mockJmsTemplate).convertAndSend(UNIT_TEST_QUEUE, sdtMessage);

        try {
            messageWriter.queueMessage(sdtMessage, UNIT_TEST);
            fail("IllegalStateException (org.springframework.jms) should be thrown");
        }
        catch(org.springframework.jms.IllegalStateException e) {
            // Expected exception thrown, continue with test
        }

        verify(mockJmsTemplate).convertAndSend(UNIT_TEST_QUEUE, sdtMessage);
        verify(individualRequestDao).getRequestBySdtReference(any(String.class));
        verify(individualRequestDao).persist(any(IIndividualRequestDao.class));
    }

    @Test
    void testLastQueueId() {
        long firstQueueId = MessageWriter.getLastQueueId();
        long secondQueueId = MessageWriter.getLastQueueId();

        assertTrue(secondQueueId > firstQueueId, "Last Queue Id not incremented");
    }

    @Test
    void testSetQueueNameMap() {
        String testQueueKey = "queueKey";
        String testQueueName = "queueName";

        final Map<String, String> testQueueNameMap = new HashMap<>();
        testQueueNameMap.put(testQueueKey, testQueueName);

        messageWriter.setQueueNameMap(testQueueNameMap);

        QueueConfig queueConfig = (QueueConfig) getAccessibleField(MessageWriter.class,
                                                                   "queueConfig",
                                                                   QueueConfig.class,
                                                                   messageWriter);

        Map<String, String> retrievedQueueNameMap = queueConfig.getTargetAppQueue();
        assertEquals(1, retrievedQueueNameMap.size(), "Unexpected number of items in queue name map");
        String retrievedQueueName = retrievedQueueNameMap.get(testQueueKey);
        assertEquals(testQueueName, retrievedQueueName, "Expected queue not found in queue name map");
    }
}
