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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;
import uk.gov.moj.sdt.services.messaging.asb.MessageSender;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

/**
 * Test class for testing the MessageWriter implementation.
 *
 * @author Manoj Kulkarni
 */
@ExtendWith(MockitoExtension.class)
class MessageWriterTest extends AbstractSdtUnitTestBase {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageWriterTest.class);

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
        MockitoAnnotations.openMocks(this);


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

        messageSender.sendMessage(UNIT_TEST_QUEUE, sdtMessage);

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

        messageSender.sendMessage(UNIT_TEST_QUEUE, sdtMessage);

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

        messageSender.sendMessage(UNIT_TEST_QUEUE, sdtMessage);

        // Send the message.
        try {
            final Map<String, String> queueNameMap = new HashMap<>();
            queueNameMap.put(UNIT_TEST, UNIT_TEST_QUEUE);

            messageWriter.setQueueNameMap(queueNameMap);

            messageWriter.queueMessage(sdtMessage, UNIT_TEST, false);
            assertTrue(true, "Success");
        } catch (final IllegalArgumentException e) {
            LOGGER.error("Error", e);
            fail("Not Expected to fail");
        }

        verify(messageSender, times(2)).sendMessage(any(), any());
    }

    /**
     * Test for a valid queue message on the MessageWriter for dead letter.
     */
    @Test
    void testQueueMessageForDeadLetter() {
        // Setup finished, now tell the mock what to expect.
        final ISdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference("Test");

        messageSender.sendMessage("UnitTestQueue.DLQ", sdtMessage);

        // Send the message.
        try {
            final Map<String, String> queueNameMap = new HashMap<>();
            queueNameMap.put(UNIT_TEST, UNIT_TEST_QUEUE);

            messageWriter.setQueueNameMap(queueNameMap);

            messageWriter.queueMessage(sdtMessage, UNIT_TEST, true);
            assertTrue(true, "Success");
        } catch (final IllegalArgumentException e) {
            LOGGER.error("Error", e);
            fail("Not Expected to fail");
        }

        verify(messageSender, times(2)).sendMessage(any(), any());
    }
}
