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

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.moj.sdt.services.config.ConnectionFactoryTestConfig;
import uk.gov.moj.sdt.services.config.ServicesTestConfig;
import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;
import uk.gov.moj.sdt.test.utils.AbstractIntegrationTest;
import uk.gov.moj.sdt.test.utils.TestConfig;

import javax.jms.Message;
import javax.jms.ObjectMessage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * IntegrationTest class for testing the MessageWriter implementation.
 *
 * @author Manoj Kulkarni
 */
@ActiveProfiles("integ-asb")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestConfig.class, ServicesTestConfig.class, ConnectionFactoryTestConfig.class})
@Disabled("Preview environments don't support Azure Service bus queues")
class MessageWriterIntAsbTest extends AbstractIntegrationTest {

    private static final String TARGET_APP_CODE = "TEST1";

    @Autowired
    @Qualifier("MessageWriter")
    private MessageWriter messageWriter;

    @Autowired
    @Qualifier("IMessageWriterBad")
    private MessageWriter messageWriterBad;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private QueueConfig queueConfig;

    /**
     * Test method to test the sending of message.
     */
    @Test
    void testQueueMessage() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        // Send the first message.
        final ISdtMessage message1 = new SdtMessage();
        final String strMessage1 =
                "TestMessage1" + dateFormat.format(new java.util.Date(System.currentTimeMillis()));
        message1.setSdtRequestReference(strMessage1);
        messageWriter.queueMessage(message1, TARGET_APP_CODE);

        // Send the second message.
        final ISdtMessage message2 = new SdtMessage();
        final String strMessage2 =
                "TestMessage2" + dateFormat.format(new java.util.Date(System.currentTimeMillis()));
        message2.setSdtRequestReference(strMessage2);
        messageWriter.queueMessage(message2, TARGET_APP_CODE);

        readMessageFromQueue(Lists.newArrayList(strMessage1, strMessage2));
    }

    /**
     * Test method to test failure behaviour when Azure Service Bus not running.
     */
    @Test
    void testAzureServiceBusDown() {
        // Send the message.
        final ISdtMessage message = new SdtMessage();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String requestReference =  "Test message" + dateFormat.format(new java.util.Date(System.currentTimeMillis()));
        message.setSdtRequestReference(requestReference);

        messageWriterBad.queueMessage(message, TARGET_APP_CODE);
        assertTrue(true, "Test completed");

        readMessageFromQueue(Lists.newArrayList(requestReference));
    }

    private List<ISdtMessage> readMessageFromQueue() {
        List<ISdtMessage> listMessages = new ArrayList<>();
        jmsTemplate.browse(queueConfig.getTargetAppQueue().get(TARGET_APP_CODE), (session, browser) -> {
            Enumeration<Message> messages = browser.getEnumeration();
            while (messages.hasMoreElements()) {
                Message message = messages.nextElement();
                ObjectMessage objectMessage = (ObjectMessage) message;
                ISdtMessage sdtMessage = (ISdtMessage) objectMessage.getObject();
                listMessages.add(sdtMessage);
            }
            return listMessages;
        });
        return listMessages;
    }

    private void readMessageFromQueue(List<String> messagesToValidate) {
        List<ISdtMessage> listMessages = readMessageFromQueue();

        assertTrue(listMessages.stream().map(ISdtMessage::getSdtRequestReference).collect(Collectors.toSet())
                       .containsAll(messagesToValidate));
    }
}
