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

import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.jms.core.JmsTemplate;

import uk.gov.moj.sdt.messaging.api.IMessageWriter;
import uk.gov.moj.sdt.messaging.api.ISdtMessage;
import uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean;

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
    private static final Log LOGGER = LogFactory.getLog (MessageWriter.class);

    /**
     * The last id used for a queued message.
     */
    private static long lastQueueId;

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
    public void queueMessage (final ISdtMessage sdtMessage)
    {
        LOGGER.debug ("Sending message [" + sdtMessage.toString () + "] to queue [" + queueName + "]");

        // Set meta data in message.
        sdtMessage.setMessageSentTimestamp (new GregorianCalendar ().getTimeInMillis ());

        SdtMetricsMBean.getSdtMetrics ().upRequestQueueCount ();
        SdtMetricsMBean.getSdtMetrics ().upRequestQueueLength ();

        try
        {
            this.jmsTemplate.convertAndSend (sdtMessage);
        }
        catch (final UncategorizedJmsException e)
        {
            // We failed to send the message to the queue: this will be detected by the recovery mechanism which will
            // periodically check the database and requeue any messages that are stuck on a state indicating that they
            // have not been sent to the case management system.
            LOGGER.error ("Failed to connect to the ActivceMQ queue [" + getQueueName () + "]", e);
            throw e;
        }

        return;
    }

    /**
     * The queue name where the messages are sent.
     * 
     * @return the name of the queue that the messages are to be sent.
     */
    private String getQueueName ()
    {
        return this.queueName;
    }

    /**
     * Get the last queue id of a queued message.
     * 
     * @return the last queue id of a queued message.
     */
    public static synchronized long getLastQueueId ()
    {
        MessageWriter.lastQueueId++;
        return MessageWriter.lastQueueId;
    }
}
