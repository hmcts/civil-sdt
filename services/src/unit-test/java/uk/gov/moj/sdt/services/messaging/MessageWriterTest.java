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

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.replay;

/**
 * Test class for testing the MessageWriter implementation.
 *
 * @author Manoj Kulkarni
 */
public class MessageWriterTest extends AbstractSdtUnitTestBase {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageWriterTest.class);

    /**
     * JMS Template for mocking.
     */
    private JmsTemplate jmsTemplate;
    private QueueConfig queueConfig;

    /**
     * MessageWriter for mocking.
     */
    private MessageWriter messageWriter;

    /**
     * Set up the variables.
     */
    @Before
    public void setUp() {
        // Nicemock returns default values
        jmsTemplate = EasyMock.createMock(JmsTemplate.class);
        queueConfig = new QueueConfig();
        queueConfig.setTargetAppQueue(new HashMap<>());
        messageWriter = new MessageWriter(jmsTemplate, queueConfig);
    }

    /**
     * Test method to test the sending of message.
     */
    @Test
    public void testQueueMessageWithEmptyTargetApp() {
        // Setup finished, now tell the mock what to expect.
        final ISdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference("Test");

        jmsTemplate.convertAndSend("UnitTestQueue", sdtMessage);
        EasyMock.expectLastCall();

        // Get ready to call the mock.
        replay(jmsTemplate);

        // Send the message.
        try {
            messageWriter.queueMessage(sdtMessage, null, false);
            Assert.fail("Should have thrown an Illegal Argument Exception as the target app code is null.");
        } catch (final IllegalArgumentException e) {
            Assert.assertTrue("Illegal Argument specified for the target application", true);
        }
    }

    /**
     * Test message with an invalid queue name i.e. where the target application
     * to queue name map is not valid
     */
    @Test
    public void testQueueMessageWithInvalidTarget() {
        // Setup finished, now tell the mock what to expect.
        final ISdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference("Test");

        jmsTemplate.convertAndSend("UnitTestQueue", sdtMessage);
        EasyMock.expectLastCall();

        // Get ready to call the mock.
        replay(jmsTemplate);

        // Send the message.
        try {
            messageWriter.queueMessage(sdtMessage, "UnitTest", false);
            Assert.fail("Should have thrown an Illegal Argument exception here as the target app code is invalid");
        } catch (final IllegalArgumentException e) {
            Assert.assertTrue("Target application code does not have a mapped queue name", true);
        }
    }

    /**
     * Test for a valid queue message on the MessageWriter.
     */
    @Test
    public void testQueueMessage() {
        // Setup finished, now tell the mock what to expect.
        final ISdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference("Test");

        jmsTemplate.convertAndSend("UnitTestQueue", sdtMessage);
        EasyMock.expectLastCall();

        // Get ready to call the mock.
        replay(jmsTemplate);

        // Send the message.
        try {
            final Map<String, String> queueNameMap = new HashMap<String, String>();
            queueNameMap.put("UNITTEST", "UnitTestQueue");

            messageWriter.setQueueNameMap(queueNameMap);

            messageWriter.queueMessage(sdtMessage, "UNITTEST", false);
            Assert.assertTrue("Success", true);
        } catch (final IllegalArgumentException e) {
            LOGGER.error("Error", e);
            Assert.fail("Not Expected to fail");
        }

        EasyMock.verify(jmsTemplate);
    }

    /**
     * Test for a valid queue message on the MessageWriter for dead letter.
     */
    @Test
    public void testQueueMessageForDeadLetter() {
        // Setup finished, now tell the mock what to expect.
        final ISdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference("Test");

        jmsTemplate.convertAndSend("UnitTestQueue.DLQ", sdtMessage);
        EasyMock.expectLastCall();

        // Get ready to call the mock.
        replay(jmsTemplate);

        // Send the message.
        try {
            final Map<String, String> queueNameMap = new HashMap<String, String>();
            queueNameMap.put("UNITTEST", "UnitTestQueue");

            messageWriter.setQueueNameMap(queueNameMap);

            messageWriter.queueMessage(sdtMessage, "UNITTEST", true);
            Assert.assertTrue("Success", true);
        } catch (final IllegalArgumentException e) {
            LOGGER.error("Error", e);
            Assert.fail("Not Expected to fail");
        }

        EasyMock.verify(jmsTemplate);
    }
}
