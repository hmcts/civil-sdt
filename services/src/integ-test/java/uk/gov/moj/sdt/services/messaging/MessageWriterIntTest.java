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

import com.azure.core.util.serializer.TypeReference;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;
import uk.gov.moj.sdt.services.messaging.asb.MessageReceiver;
import uk.gov.moj.sdt.test.utils.AbstractIntegrationTest;

import java.text.SimpleDateFormat;
import javax.jms.JMSException;

/**
 * IntegrationTest class for testing the MessageWriter implementation.
 *
 * @author Manoj Kulkarni
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/uk/gov/moj/sdt/services/spring.context.xml",
        "classpath:/uk/gov/moj/sdt/services/cache/spring.context.xml",
        "classpath:/uk/gov/moj/sdt/services/utils/spring.context.xml",
        "classpath:/uk/gov/moj/sdt/services/mbeans/spring.context.xml",
        "classpath:/uk/gov/moj/sdt/services/messaging/spring.hibernate.test.xml",
        "classpath:/uk/gov/moj/sdt/services/messaging/spring.context.test.xml",
        "classpath*:/uk/gov/moj/sdt/dao/**/spring*.xml",
        "classpath:/uk/gov/moj/sdt/consumers/spring.context.integ.test.xml",
        "classpath*:/uk/gov/moj/sdt/transformers/**/spring*.xml",
        "classpath*:/uk/gov/moj/sdt/interceptors/**/spring*.xml",
        "classpath*:/uk/gov/moj/sdt/validators/**/spring*.xml", "classpath*:/uk/gov/moj/sdt/utils/**/spring*.xml"})
public class MessageWriterIntTest extends AbstractIntegrationTest {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageWriterIntTest.class);

    private MessageReceiver messageReceiver;

    /**
     * Test method to test the sending of message.
     *
     * @throws JMSException         exception
     * @throws InterruptedException exception
     */
    @Test
    public void testQueueMessage() throws JMSException, InterruptedException {
        // Get message writer from Spring.
        final MessageWriter messageWriter =
                (MessageWriter) this.applicationContext
                        .getBean("uk.gov.moj.sdt.services.messaging.api.IMessageWriter");

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        // Clear any old messages off the queue.
        while (true) {
            // Read any old messages.
            final ServiceBusReceivedMessage message = messageReceiver.receiveMessage("Test1Queue");
            if (message == null) {
                break;
            }
        }

        // Send the first message.
        final ISdtMessage message1 = new SdtMessage();
        final String strMessage1 =
                "TestMessage1" + dateFormat.format(new java.util.Date(System.currentTimeMillis()));
        message1.setSdtRequestReference(strMessage1);
        messageWriter.queueMessage(message1, "TEST1", false);

        // Send the second message.
        final ISdtMessage message2 = new SdtMessage();
        final String strMessage2 =
                "TestMessage2" + dateFormat.format(new java.util.Date(System.currentTimeMillis()));
        message2.setSdtRequestReference(strMessage2);
        messageWriter.queueMessage(message2, "TEST1", false);

        // Read the two messages and ensure they are read back in the same order.
        ServiceBusReceivedMessage message = messageReceiver.receiveMessage("Test1Queue");
        if (message == null) {
            Assert.fail("Test failed because JMS receive timed out.");
        }

        ISdtMessage sdtMessage = message.getBody().toObject(new TypeReference<SdtMessage>() {});
        LOGGER.debug("Message Receieved 1 - " + sdtMessage.toString());
        Assert.assertTrue(sdtMessage.getSdtRequestReference().equals(strMessage1));

        message = messageReceiver.receiveMessage("Test1Queue");
        sdtMessage = message.getBody().toObject(new TypeReference<SdtMessage>() {});
        LOGGER.debug("Message Receieved2 - " + sdtMessage.toString());
        Assert.assertTrue(sdtMessage.getSdtRequestReference().equals(strMessage2));

    }

    /**
     * Test method to test failure behaviour when Azure Service Bus not running.
     *
     * @throws JMSException exception
     */
    @Test
    public void testAzureServiceBusDown() throws JMSException {
        // Get message writer from Spring.
        final MessageWriter messageWriter =
                (MessageWriter) this.applicationContext
                        .getBean("uk.gov.moj.sdt.services.messaging.api.IMessageWriterBad");

        // Send the message.
        final ISdtMessage message = new SdtMessage();
        message.setSdtRequestReference("Test message");

        messageWriter.queueMessage(message, "TEST1", false);
        Assert.assertTrue("Test completed", true);

    }
}
