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
package uk.gov.moj.sdt.messaging;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * IntegrationTest class for testing the MessageWriter implementation.
 * 
 * @author Manoj Kulkarni
 * 
 */
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (locations = {"classpath*:**/applicationContext.xml", "/uk/gov/moj/sdt/dao/spring.context.xml",
        "classpath*:/**/spring*.xml", "/uk/gov/moj/sdt/dao/spring*.xml"})
public class MessageWriterIntTest extends AbstractJUnit4SpringContextTests
{
    /**
     * Logger object.
     */
    private static final Logger LOG = LoggerFactory.getLogger (MessageWriterIntTest.class);

    /**
     * Test method to test the sending of message.
     * 
     * @throws JMSException exception
     * 
     */
    @Test
    public void testQueueMessage () throws JMSException
    {
        // Get message writer from Spring.
        final MessageWriter messageWriter =
                (MessageWriter) this.applicationContext.getBean ("uk.gov.moj.sdt.messaging.api.IMessageWriter");

        final SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyyMMddHHmmss");

        final JmsTemplate jmsTemplate = (JmsTemplate) this.applicationContext.getBean ("jmsTemplate");

        // Clear any old messages off the queue.
        jmsTemplate.setReceiveTimeout (1);
        while (true)
        {
            // Read any old messages.
            final Message message = jmsTemplate.receive ("JMSTestQueue");
            final TextMessage txtmessage = (TextMessage) message;
            if (txtmessage == null)
            {
                break;
            }
        }

        // Send the first message.
        final String strMessage1 =
                "TestMessage1" + dateFormat.format (new java.util.Date (System.currentTimeMillis ()));
        messageWriter.queueMessage (strMessage1);

        // Send the second message.
        final String strMessage2 =
                "TestMessage2" + dateFormat.format (new java.util.Date (System.currentTimeMillis ()));
        messageWriter.queueMessage (strMessage2);

        // Limit time on receive for the sake of the test to prevent it hanging.
        jmsTemplate.setReceiveTimeout (5000);

        // Read the two messages and ensure they are read back in the same order.
        Message message = jmsTemplate.receive ("JMSTestQueue");
        TextMessage txtmessage = (TextMessage) message;
        if (txtmessage == null)
        {
            Assert.fail ("Test failed because JMS receive timed out.");
        }
        LOG.debug ("Message Receieved 1 - " + txtmessage.getText ());
        assertTrue (txtmessage.getText ().equals (strMessage1));

        message = jmsTemplate.receive ("JMSTestQueue");
        txtmessage = (TextMessage) message;
        LOG.debug ("Message Receieved2 - " + txtmessage.getText ());
        assertTrue (txtmessage.getText ().equals (strMessage2));
    }

    /**
     * Test method to test failure behaviour when ACTIVE MQ not running.
     * 
     * @throws JMSException exception
     * 
     */
    @Test
    public void testActiveMqDown () throws JMSException
    {
        // Get message writer from Spring.
        final MessageWriter messageWriter =
                (MessageWriter) this.applicationContext.getBean ("uk.gov.moj.sdt.messaging.api.IMessageWriterBad");

        final SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyyMMddHHmmss");

        // Send the message.
        final String strMessage1 = "TestMessage" + dateFormat.format (new java.util.Date (System.currentTimeMillis ()));
        try
        {
            messageWriter.queueMessage (strMessage1);
            Assert.fail ("Expected exception not thrown.");
        }
        catch (final UncategorizedJmsException e)
        {
            // Test has worked - swallow exception.
            Assert.assertTrue (true);
        }
    }
}
