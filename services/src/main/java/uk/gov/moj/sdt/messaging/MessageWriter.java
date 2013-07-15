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

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

import uk.gov.moj.sdt.messaging.api.IMessageWriter;

/**
 * Message writer that handles writing messages to a message queue.
 * 
 * @author Manoj Kulkarni
 * 
 */
public class MessageWriter implements IMessageWriter
{
    /**
     * Logger for this class.
     */
    private final Log logger = LogFactory.getLog (getClass ());

    /**
     * The JmsTemplate object from Spring framework.
     */
    private final JmsTemplate jmsTemplate;

    /**
     * The queue name configurable as a bean property.
     */
    private final String queueName;

    /**
     * Creates a message sender with the JmsTemplate.
     * 
     * @param jmsTemplate The JMS template
     * @param queueName The JMS queue name
     */
    public MessageWriter (final JmsTemplate jmsTemplate, final String queueName)
    {
        this.jmsTemplate = jmsTemplate;
        this.queueName = queueName;
    }

    @Override
    public String queueMessage (final String message)
    {
        logger.debug ("Sending message");

        final String msgUniqueId = String.valueOf (System.currentTimeMillis ());
        this.jmsTemplate.convertAndSend (queueName, message, new MessagePostProcessor ()
        {

            @Override
            public Message postProcessMessage (final Message message) throws JMSException
            {
                message.setJMSCorrelationID (msgUniqueId);
                return message;
            }

        });
        logger.debug ("Message Sent");
        return msgUniqueId;
    }

    /**
     * The queue name where the messages are sent.
     * 
     * @return the name of the queue that the messages are to be sent.
     */
    public String getQueueName ()
    {
        return this.queueName;
    }
}
