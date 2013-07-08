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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
     * Connection factory for all connections to ActiveMQ.
     */
    private ActiveMQConnectionFactory connectionFactory;

    /**
     * Connection for the message producing queue.
     */
    private Connection connection;

    /**
     * Session for the message producing queue.
     */
    private Session session;

    /**
     * Producers for the queues stored in a map with queue name as the key.
     */
    private Map<String, MessageProducer> producerMap = new HashMap<String, MessageProducer> ();

    @Override
    public void queueMessage (final String message, final String queueName) throws Exception
    {
        // Get the producer matching to the queue name
        final MessageProducer producer = getMessageProducer (queueName);

        try
        {

            final TextMessage txtMessage = session.createTextMessage ();
            txtMessage.setText (message);

            logger.debug ("Sending message " + message + " on queue " + queueName);
            producer.send (txtMessage);

        }
        catch (final JMSException e)
        {
            logger.error (e);
            throw new Exception (e);
        }

    }

    /**
     * Method to create and cache a message producer for the specified queue name.
     * 
     * @param queueName name of the queue
     * @return message producer
     * @throws Exception if there is any error in creation of the producer
     */
    private synchronized MessageProducer getMessageProducer (final String queueName) throws Exception
    {
        if (producerMap.containsKey (queueName))
        {
            return producerMap.get (queueName);
        }

        logger.debug ("Creating new producer for queue [" + queueName + "]");
        MessageProducer producer = null;

        // Initialise session once only for the class
        try
        {
            if (connectionFactory == null)
            {
                // TODO Discuss whether the connection factory needs to be injected with Spring configuration
                connectionFactory = new ActiveMQConnectionFactory ("failover:tcp://localhost:61616");
                // TODO Discuss what properties are required and set them to the connectionFactory
                final int socketTimeOutMs = 60000;
                final Properties connectionProperties = new Properties ();
                connectionProperties.put ("useLocalHost", true);
                connectionProperties.put ("keepAlive", true);
                connectionProperties.put ("soTimeout", socketTimeOutMs);
                connectionFactory.setProperties (connectionProperties);
            }

            connection = connectionFactory.createConnection ();

            // JMS messages are sent and received using a Session. We will
            // create here a non-transactional session object. If you want
            // to use transactions you should set the first parameter to 'true'
            session = connection.createSession (false, Session.AUTO_ACKNOWLEDGE);

            // Destination represents here our queue on the
            // JMS server. You don't have to do anything special on the
            // server to create it, it will be created automatically.
            final Destination destination = session.createQueue (queueName);

            producer = session.createProducer (destination);

            // Cache the producer
            producerMap.put (queueName, producer);
        }
        catch (final JMSException e)
        {
            logger.error (e);
            throw new Exception (e);
        }

        return producer;
    }

    @Override
    public synchronized void closeQueue (final String queueName) throws Exception
    {
        final MessageProducer producer = producerMap.get (queueName);

        try
        {
            logger.debug ("Attempting to release connection for queue [" + queueName + "]");

            if (producer != null)
            {
                // In Close queue Send an empty message to indicate that this is the final message
                producer.send (session.createMessage ());
                producer.close ();
                producerMap.remove (queueName);
            }

            // Check to see if there are any producers left in the map, if not then close the connection
            if (producerMap.isEmpty ())
            {
                session.close ();
                connection.close ();
            }

        }
        catch (final JMSException e)
        {
            logger.error (e);
            throw new Exception (e);
        }
    }

}
