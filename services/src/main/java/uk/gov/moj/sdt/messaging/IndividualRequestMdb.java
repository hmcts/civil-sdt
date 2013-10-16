/* Copyrights and Licenses
 * 
 * Copyright (c) 2012-2013 by the Ministry of Justice. All rights reserved.
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
import javax.jms.ObjectMessage;

import jline.internal.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.gov.moj.sdt.consumers.exception.OutageException;
import uk.gov.moj.sdt.consumers.exception.SoapFaultException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.messaging.api.IMessageDrivenBean;
import uk.gov.moj.sdt.messaging.api.IMessageWriter;
import uk.gov.moj.sdt.services.api.ITargetApplicationSubmissionService;

/**
 * Implementation of the IMessageReader interface.
 * This class is implemented using the Spring JMS 1.1 support and is invoked by the
 * MessageListenerAdapter class that is registered with the DefaultMessageListener.
 * 
 * @author Manoj Kulkarni
 * 
 */
@Transactional (propagation = Propagation.REQUIRES_NEW)
public class IndividualRequestMdb implements IMessageDrivenBean
{
    /**
     * Logger for logging messages.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (IndividualRequestMdb.class);

    /**
     * Target Application Submission Service.
     */
    private ITargetApplicationSubmissionService targetAppSubmissionService;

    /**
     * The variable holding the global parameters cache bean.
     */
    private ICacheable globalParametersCache;

    /**
     * This variable holding the message writer reference.
     */
    private IMessageWriter messageWriter;

    @Override
    @Transactional (propagation = Propagation.REQUIRED)
    public void readMessage (final Message message)
    {
        // All expected messages are object messages written by the message writer.
        if (message instanceof ObjectMessage)
        {
            final ObjectMessage objectMessage = (ObjectMessage) message;
            String sdtReference = null;
            try
            {
                final boolean isMessageReDelivered = message.getJMSRedelivered ();
                LOGGER.debug ("Message re-delivered status [" + isMessageReDelivered + "]");
                sdtReference = ((SdtMessage) objectMessage.getObject ()).getSdtRequestReference ();
            }
            catch (final JMSException e)
            {
                LOGGER.error (e.getMessage (), e);
                throw new RuntimeException (e);
            }
            LOGGER.debug ("Received message, SDT reference [" + sdtReference + "]");

            // Get the Individual Request matching the SDT Request Reference
            final IIndividualRequest individualRequest = this.findIndividualRequest (sdtReference);

            // Proceed ahead if the Individual Request is found
            if (individualRequest != null)
            {

                // Check the configurable delay from system parameter and delay the consumer call for that time.
                this.delayRequestProcessing (individualRequest);

                // Update the status of the requested to Forwarded before calling the consumer
                this.updateAndMarkRequestForwarded (individualRequest);

                // Make call to consumer to submit the request to target application.
                try
                {
                    this.forwardToTargetApplication (individualRequest);

                    this.updateAndMarkRequestCompleted (individualRequest);
                }
                catch (final TimeoutException e)
                {
                    LOGGER.error ("Timeout exception for SDT reference " + individualRequest.getSdtRequestReference () +
                            "]", e);

                    // Update the individual request with the reason code for error REQ_NOT_ACK
                    this.updateRequestTimeOut (individualRequest);

                    // Re-queue message again
                    this.reQueueRequest (individualRequest);
                }
                catch (final OutageException e)
                {
                    LOGGER.error ("Outage exception for SDT reference " + individualRequest.getSdtRequestReference () +
                            "]", e);

                    // Update the individual request with reason code for not responding
                    this.updateTargetAppUnavailable (individualRequest);

                    // Re-queue message again
                    this.reQueueRequest (individualRequest);
                }
                catch (final SoapFaultException e)
                {
                    // Update the individual request with the soap fault reason
                    this.getTargetAppSubmissionService ().updateRequestSoapError (individualRequest, e.getMessage ());
                }

                LOGGER.debug ("Individual request " + sdtReference + " processing completed.");

            }
            else
            {
                LOGGER.error ("SDT Reference " + sdtReference +
                        " read from message queue not found in database for individual request.");
            }
        }
    }

    /**
     * 
     * @return the target application submission service.
     */
    public ITargetApplicationSubmissionService getTargetAppSubmissionService ()
    {
        return targetAppSubmissionService;
    }

    /**
     * 
     * @param targetAppSubmissionService the target submission service
     */
    public void setTargetAppSubmissionService (final ITargetApplicationSubmissionService targetAppSubmissionService)
    {
        this.targetAppSubmissionService = targetAppSubmissionService;
    }

    /**
     * 
     * @return cacheable interface representing the GlobalParametersCache object
     */
    public ICacheable getGlobalParametersCache ()
    {
        return globalParametersCache;
    }

    /**
     * 
     * @param globalParametersCache the cache for the global parameters.
     */
    public void setGlobalParametersCache (final ICacheable globalParametersCache)
    {
        this.globalParametersCache = globalParametersCache;
    }

    /**
     * 
     * @return messageWriter the MessageWriter for the messaging API.
     */
    public IMessageWriter getMessageWriter ()
    {
        return messageWriter;
    }

    /**
     * 
     * @param messageWriter the message writer.
     */
    public void setMessageWriter (final IMessageWriter messageWriter)
    {
        this.messageWriter = messageWriter;
    }

    /**
     * 
     * @param sdtRequestReference the SDT request reference
     * @return an Individual Request matching the SDT request reference.
     */
    private IIndividualRequest findIndividualRequest (final String sdtRequestReference)
    {
        return this.getTargetAppSubmissionService ().getRequestToSubmit (sdtRequestReference);
    }

    /**
     * Forwards the individual request to target application and updates the
     * same with the response pay-load from the target application.
     * 
     * @param individualRequest the individual request object.
     * @throws TimeoutException the consumer may throw a timeout exception.
     */
    private void forwardToTargetApplication (final IIndividualRequest individualRequest) throws TimeoutException
    {
        LOGGER.debug ("Calling the consumer to call the target application for SDT reference [" +
                individualRequest.getSdtRequestReference () + "]");

        this.getTargetAppSubmissionService ().sendRequestToTargetApp (individualRequest);

    }

    /**
     * 
     * @param individualRequest the individualrequest object to be updated.
     */
    private void updateAndMarkRequestForwarded (final IIndividualRequest individualRequest)
    {
        LOGGER.debug ("Updating the request status as 'Forwarded' for SDT reference [" +
                individualRequest.getSdtRequestReference () + "]");

        this.getTargetAppSubmissionService ().updateForwardingRequest (individualRequest);
    }

    /**
     * 
     * @param individualRequest the individual request to be marked as completed
     */
    private void updateAndMarkRequestCompleted (final IIndividualRequest individualRequest)
    {
        LOGGER.debug ("Marking the request as completed for SDT reference [" +
                individualRequest.getSdtRequestReference () + "]");

        this.getTargetAppSubmissionService ().updateCompletedRequest (individualRequest);
    }

    /**
     * 
     * @param individualRequest the individual request to be marked as request no acknowledged
     */
    private void updateRequestTimeOut (final IIndividualRequest individualRequest)
    {
        LOGGER.debug ("Individual request timed out for SDT reference " + individualRequest.getSdtRequestReference () +
                "]");

        this.getTargetAppSubmissionService ().updateRequestTimeOut (individualRequest);
    }

    /**
     * 
     * @param individualRequest the individual request to be marked as request not responding
     */
    private void updateTargetAppUnavailable (final IIndividualRequest individualRequest)
    {
        LOGGER.debug ("Failed to send individual request for SDT reference " +
                individualRequest.getSdtRequestReference () + "]");

        this.getTargetAppSubmissionService ().updateTargetAppUnavailable (individualRequest);
    }

    /**
     * 
     * @param individualRequest the individual request object.
     * @return true if the request forwarding attempts is less than the maximum forwarding attempts parameter.
     */
    private boolean canRequestBeRequeued (final IIndividualRequest individualRequest)
    {
        // Get the max forwarding attempts system parameter.
        final String maxForwardingAttemptStr =
                this.getSystemParameter (IGlobalParameter.ParameterKey.MAX_FORWARDING_ATTEMPTS.name ());

        if (individualRequest.getForwardingAttempts () <= Integer.valueOf (maxForwardingAttemptStr))
        {
            return true;
        }

        return false;
    }

    /**
     * 
     * @param individualRequest the individual request.
     */
    private void reQueueRequest (final IIndividualRequest individualRequest)
    {
        // Check the forwarding attempts has not exceeded the max forwarding attempts count.
        if (this.canRequestBeRequeued (individualRequest))
        {
            LOGGER.debug ("Re-queuing request for SDT reference [" + individualRequest.getSdtRequestReference () + "]");

            this.getMessageWriter ().queueMessage (individualRequest.getSdtRequestReference ());
        }
        else
        {
            Log.error ("Maximum forwarding attempts exceeded for request " +
                    individualRequest.getSdtRequestReference ());
        }
    }

    /**
     * Return the named parameter value from global parameters.
     * 
     * @param parameterName the name of the parameter.
     * @return value of the parameter name as stored in the database.
     */
    private String getSystemParameter (final String parameterName)
    {
        final IGlobalParameter globalParameter =
                this.getGlobalParametersCache ().getValue (IGlobalParameter.class, parameterName);

        if (globalParameter == null)
        {
            return null;
        }

        return globalParameter.getValue ();

    }

    /**
     * Reads an system parameter to add delay to the request processing if the
     * system parameter has non null value.
     * 
     * @param request IndividualRequest object
     */
    private void delayRequestProcessing (final IIndividualRequest request)
    {
        // Get the target Application code.
        final String targetApplication =
                request.getBulkSubmission ().getTargetApplication ().getTargetApplicationCode ();
        // Pre-fix the target application code to the global parameter key for the request delay
        final String delayParamName =
                targetApplication.toUpperCase () + "_" + IGlobalParameter.ParameterKey.INDV_REQ_DELAY.name ();

        // Get the global parameter for the individual request delay
        final String individualReqProcessingDelay = this.getSystemParameter (delayParamName);

        if (individualReqProcessingDelay != null)
        {
            // If the global parameter is available, proceed with delaying the request processing
            final long delay = Long.valueOf (individualReqProcessingDelay);
            try
            {
                LOGGER.debug ("Delay request processing for " + delay + " milliseconds.");

                Thread.sleep (delay);
            }
            catch (final InterruptedException ie)
            {
                LOGGER.warn ("Delay operation interrupted by interrupt exception " + ie.getMessage ());
            }
        }

        return;

    }

}
