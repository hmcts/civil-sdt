/* Copyrights and Licenses
 *
 * Copyright (c) 2012-2014 by the Ministry of Justice. All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 * - All advertising materials mentioning features or use of this software must display the
 * following acknowledgment: "This product includes Money Claims OnLine."
 * - Products derived from this software may not be called "Money Claims OnLine" nor may
 * "Money Claims OnLine" appear in their names without prior written permission of the
 * Ministry of Justice.
 * - Redistributions of any form whatsoever must retain the following acknowledgment: "This
 * product includes Money Claims OnLine."
 * This software is provided "as is" and any expressed or implied warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the Ministry of Justice or its contributors be liable for any
 * direct, indirect, incidental, special, exemplary, or consequential damages (including, but
 * not limited to, procurement of substitute goods or services; loss of use, data, or profits;
 * or business interruption). However caused any on any theory of liability, whether in contract,
 * strict liability, or tort (including negligence or otherwise) arising in any way out of the use of this
 * software, even if advised of the possibility of such damage.
 *
 * $Id: $
 * $LastChangedRevision: $
 * $LastChangedDate: $
 * $LastChangedBy: $ */

package uk.gov.moj.sdt.services.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test class for testing the MessageWriter implementation.
 *
 * @author Manoj Kulkarni
 */
@ExtendWith(MockitoExtension.class)
class MessageWriterTest extends AbstractSdtUnitTestBase {

    private static final String DLQ_QUEUE_NAME = "UnitTestQueue.DLQ";

    private static final String UNIT_TEST_QUEUE = "UnitTestQueue";

    private static final String UNIT_TEST = "UNITTEST";

    @Mock
    private MessageSender messageSender;

    /**
     * MessageWriter for mocking.
     */
    private MessageWriter messageWriter;

    /**
     * Set up the variables.
     */
    @BeforeEach
    @Override
    public void setUp() {

        QueueConfig queueConfig = new QueueConfig();
        Map<String, String> mockedMap = new HashMap<>();
        queueConfig.setQueueConfig(mockedMap);
        messageWriter = new MessageWriter(messageSender, queueConfig);
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
        verify(messageSender, times(0)).sendMessage(any(), eq(sdtMessage));
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
        verify(messageSender, times(0)).sendMessage(any(), eq(sdtMessage));
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
            assertTrue(true, "Success");
        } catch (final IllegalArgumentException e) {
            fail("Not Expected to fail");
        }

        verify(messageSender, times(1)).sendMessage(UNIT_TEST_QUEUE, sdtMessage);
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
            assertTrue(true, "Success");
        } catch (final IllegalArgumentException e) {
            fail("Not Expected to fail");
        }

        verify(messageSender, times(1)).sendMessage(DLQ_QUEUE_NAME, sdtMessage);
    }
}
