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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.IllegalStateException;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.services.messaging.api.IMessageWriter;
import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean;

import java.util.GregorianCalendar;
import java.util.Map;

/**
 * Message writer that handles writing messages to a message queue.
 *
 * @author Manoj Kulkarni
 */
@Component("MessageWriter")
public class MessageWriter implements IMessageWriter {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageWriter.class);

    /**
     * The last id used for a queued message.
     */
    private static long lastQueueId;

    /**
     * The JmsTemplate object from Spring framework.
     */
    private final JmsTemplate jmsTemplate;

    private final QueueConfig queueConfig;

    @Value("${enable-new-queue-process:false}")
    private boolean enableNewQueueProcess;

    /**
     * Creates a message sender with the JmsTemplate.
     *
     * @param jmsTemplate The JMS template
     */
    @Autowired
    public MessageWriter(final JmsTemplate jmsTemplate,
                         QueueConfig queueConfig) {
        this.jmsTemplate = jmsTemplate;
        this.queueConfig = queueConfig;
    }

    @Override
    public void queueMessage(final ISdtMessage sdtMessage, final String targetAppCode) {

        // Check the target application code is valid and return queue name.
        final String queueName = getQueueName(targetAppCode);

        LOGGER.debug("Sending message with SDT request reference [{}] to queue [{}]",
                     sdtMessage.getSdtRequestReference(),
                     queueName);

        // Set meta data in message.
        sdtMessage.setMessageSentTimestamp(new GregorianCalendar().getTimeInMillis());
        sdtMessage.setEnqueueLoggingId(SdtContext.getContext().getLoggingContext().getMajorLoggingId());

        SdtMetricsMBean.getMetrics().upRequestQueueCount();
        SdtMetricsMBean.getMetrics().upRequestQueueLength();

        try {
            if (!enableNewQueueProcess) {
                LOGGER.debug("Resetting queue connection");
                CachingConnectionFactory cachingConnectionFactory =
                    (CachingConnectionFactory) this.jmsTemplate.getConnectionFactory();
                if (cachingConnectionFactory != null) {
                    cachingConnectionFactory.resetConnection();
                }
            }
            this.jmsTemplate.convertAndSend(queueName, sdtMessage);
        } catch (final IllegalStateException e) {
            LOGGER.error("Error sending message to queue using jms template {}", e.getMessage());
            String message = e.getMessage();
            if (message != null && message.contains("MessageProducer was closed")) {
                // Link to queue timed out due to idle period expiring, reset and try again.
                // No need to update queue metrics as message was not queued.
                resetConnectionAndQueueMessage(sdtMessage, queueName);
            } else {
                throw e;
            }
        } catch (final UncategorizedJmsException e) {
            logQueueConnectFailure(sdtMessage, queueName, e);
        }
    }

    /**
     * Reset connection and try writing message to the queue again
     * @param sdtMessage The message object to be written to the message queue.
     * @param queueName The queue to write the message object to
     */
    private void resetConnectionAndQueueMessage(final ISdtMessage sdtMessage, final String queueName) {
        LOGGER.debug("Reset connection and resend SDT request reference [{}] to queue [{}]",
                     sdtMessage.getSdtRequestReference(),
                     queueName);

        CachingConnectionFactory cachingConnectionFactory =
            (CachingConnectionFactory) this.jmsTemplate.getConnectionFactory();
        if (cachingConnectionFactory == null) {
            throw new java.lang.IllegalStateException("JmsTemplate has no connection factory");
        }
        cachingConnectionFactory.resetConnection();

        try {
            this.jmsTemplate.convertAndSend(queueName, sdtMessage);
        } catch (final UncategorizedJmsException e) {
            logQueueConnectFailure(sdtMessage, queueName, e);
        }
    }

    private void logQueueConnectFailure(ISdtMessage sdtMessage, String queueName, Exception e) {
        // We failed to send the message to the queue: this will be detected by the recovery mechanism which will
        // periodically check the database and requeue any messages that are stuck on a state indicating that they
        // have not been sent to the case management system.
        LOGGER.error("Failed to connect to the ServiceBus queue [{}] while queueing message request reference [{}]",
                     queueName,
                     sdtMessage.getSdtRequestReference(),
                     e);
    }

    /**
     * Get the last queue id of a queued message.
     *
     * @return the last queue id of a queued message.
     */
    public static synchronized long getLastQueueId() {
        MessageWriter.lastQueueId++;
        return MessageWriter.lastQueueId;
    }

    /**
     * @return map containing the target application to queue name mapping.
     */
    private Map<String, String> getQueueNameMap() {
        return queueConfig.getTargetAppQueue();
    }

    /**
     * @param queueNameMap map containing the target application to queue name mapping
     *                     with the key as the the target application code and value as the queue name
     */
    public void setQueueNameMap(final Map<String, String> queueNameMap) {
        queueConfig.setTargetAppQueue(queueNameMap);
    }

    /**
     * Checks that the target application code is supplied and that it is
     * mapped to one of the queues in the queue map. Returns the queue name
     * that is mapped to the target application code.
     *
     * @param targetApplicationCode the target application code.
     * @return the queue name matching to the target application code.
     */
    private String getQueueName(final String targetApplicationCode) {
        // The target application code should be supplied.
        if (targetApplicationCode == null || targetApplicationCode.trim().length() == 0) {
            throw new IllegalArgumentException("Target application code must be supplied.");
        } else {
            // Check that the target application code is mapped to a queue name
            if (!this.getQueueNameMap().containsKey(targetApplicationCode)) {
                LOGGER.error("Attempting to write to unknown queue for target application [{}]", targetApplicationCode);
                throw new IllegalArgumentException("Target application code [" + targetApplicationCode +
                        "] does not have a JMS queue mapped.");
            } else {
                return this.getQueueNameMap().get(targetApplicationCode);
            }
        }
    }
}
