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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.moj.sdt.services.config.ServicesTestConfig;
import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;
import uk.gov.moj.sdt.test.utils.AbstractIntegrationTest;
import uk.gov.moj.sdt.test.utils.TestConfig;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * IntegrationTest class for testing the MessageWriter implementation.
 *
 * @author Manoj Kulkarni
 */
@ActiveProfiles("integ")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestConfig.class, ServicesTestConfig.class})
class MessageWriterIntTest extends AbstractIntegrationTest {

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
        final String strMessage1 =
                "TestMessage1" + dateFormat.format(new java.util.Date(System.currentTimeMillis()));
        final ISdtMessage message1 = createSdtMessage(strMessage1);
        messageWriter.queueMessage(message1, TARGET_APP_CODE);

        // Send the second message.
        final String strMessage2 =
                "TestMessage2" + dateFormat.format(new java.util.Date(System.currentTimeMillis()));
        final ISdtMessage message2 = createSdtMessage(strMessage2);
        messageWriter.queueMessage(message2, TARGET_APP_CODE);

        // Check messages retrieved from queue in expected order
        checkQueueMessage(strMessage1);
        checkQueueMessage(strMessage2);
    }

    /**
     * Test method to test failure behaviour when Azure Service Bus not running.
     */
    @Test
    void testAzureServiceBusDown() {
        // Send the message.
        final String strMessage = "Test message";
        final ISdtMessage message = createSdtMessage(strMessage);

        messageWriterBad.queueMessage(message, TARGET_APP_CODE);
        assertTrue(true, "Test completed");

        // Remove message from queue so other tests aren't affected
        checkQueueMessage(strMessage);
    }

    private ISdtMessage createSdtMessage(String requestRef) {
        ISdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference(requestRef);

        return sdtMessage;
    }

    private void checkQueueMessage(String expectedRequestRef) {
        // Set timeout to 1 second in case queue doesn't contain a message
        jmsTemplate.setReceiveTimeout(1000);

        Message message = jmsTemplate.receive(queueConfig.getTargetAppQueue().get(TARGET_APP_CODE));

        if(message != null) {
            try {
                ObjectMessage objectMessage = (ObjectMessage) message;
                ISdtMessage sdtMessage = (ISdtMessage) objectMessage.getObject();
                assertEquals(expectedRequestRef, sdtMessage.getSdtRequestReference(),
                             "Unexpected message retrieved from queue");
            } catch (JMSException e) {
                fail("Unable to get message from queue");
            }
        } else {
            fail("No message retrieved from queue");
        }
    }
}
