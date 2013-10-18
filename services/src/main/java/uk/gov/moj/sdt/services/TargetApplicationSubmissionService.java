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
package uk.gov.moj.sdt.services;

import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.consumers.api.IConsumerGateway;
import uk.gov.moj.sdt.consumers.exception.OutageException;
import uk.gov.moj.sdt.consumers.exception.SoapFaultException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IErrorMessage;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.messaging.SdtMessage;
import uk.gov.moj.sdt.messaging.api.IMessageWriter;
import uk.gov.moj.sdt.messaging.api.ISdtMessage;
import uk.gov.moj.sdt.services.api.ITargetApplicationSubmissionService;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean;

/**
 * Service class that implements the TargetApplicationSubmissionService.
 * 
 * @author Manoj Kulkarni
 * 
 */
public class TargetApplicationSubmissionService implements ITargetApplicationSubmissionService
{

    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (TargetApplicationSubmissionService.class);

    /**
     * Individual Request Dao to perform operations on the individual request object.
     */
    private IIndividualRequestDao individualRequestDao;

    /**
     * The consumer gateway that will perform the call to the target application
     * web service.
     */
    private IConsumerGateway requestConsumer;

    /**
     * The ICacheable reference to the global parameters cache.
     */
    private ICacheable globalParametersCache;

    /**
     * This variable holding the message writer reference.
     */
    private IMessageWriter messageWriter;

    @Override
    public void processRequestToSubmit (final String sdtRequestReference)
    {
        // Look for the individual request matching this unique request reference.
        final IIndividualRequest individualRequest =
                this.getIndividualRequestDao ().getRequestBySdtReference (sdtRequestReference);

        // Proceed ahead if the Individual Request is found.
        if (individualRequest != null)
        {
            // Check the configurable delay from system parameter and delay the consumer call for that time.
            this.delayRequestProcessing (individualRequest);

            // Update the status of the requested to Forwarded before calling the consumer
            this.updateForwardingRequest (individualRequest);

            // Make call to consumer to submit the request to target application.
            try
            {
                this.sendRequestToTargetApp (individualRequest);

                this.updateCompletedRequest (individualRequest);
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
            catch (final SoapFaultException e)
            {
                // Update the individual request with the soap fault reason
                this.updateRequestSoapError (individualRequest, e.getMessage ());
            }

            LOGGER.debug ("Individual request " + sdtRequestReference + " processing completed.");

        }
        else
        {
            LOGGER.error ("SDT Reference " + sdtRequestReference +
                    " read from message queue not found in database for individual request.");
        }

        return;
    }

    /**
     * Update the request object. This method is to be called to update the request object
     * to bring it in the request status state of FORWARDED
     * 
     * @param individualRequest the IndividualRequest object that contains the
     *            response payload from the target application.
     */
    private void updateForwardingRequest (final IIndividualRequest individualRequest)
    {
        individualRequest.incrementForwardingAttempts ();
        this.getIndividualRequestDao ().persist (individualRequest);

        // Setup raw XML associated with a single request so that it can be picked up by the outbound
        // interceptor and injected into the outbound XML.
        SdtContext.getContext ().setRawOutXml (individualRequest.getRequestPayload ());
    }

    /**
     * Update the request object. This method is to be called to update the request object on
     * completion i.e. successful response is received from the target application.
     * 
     * @param individualRequest the individual request to be marked as completed
     */
    private void updateCompletedRequest (final IIndividualRequest individualRequest)
    {
        final String requestStatus = individualRequest.getRequestStatus ();

        if (requestStatus.equals (IIndividualRequest.IndividualRequestStatus.ACCEPTED))
        {
            this.updateAcceptedRequest (individualRequest);
        }
        else if (requestStatus.equals (IIndividualRequest.IndividualRequestStatus.INITIALLY_ACCEPTED))
        {
            this.updateInitiallyAcceptedRequest (individualRequest);
        }
        else if (requestStatus.equals (IIndividualRequest.IndividualRequestStatus.REJECTED))
        {
            this.updateRejectedRequest (individualRequest);
        }

        // now persist the request.
        this.getIndividualRequestDao ().persist (individualRequest);
    }

    /**
     * Mark the request with status as "Accepted".
     * 
     * @param individualRequest the individual request object.
     */
    private void updateAcceptedRequest (final IIndividualRequest individualRequest)
    {
        individualRequest.markRequestAsAccepted ();
    }

    /**
     * Mark the request with status as "Rejected".
     * 
     * @param individualRequest the individual request object.
     */
    private void updateRejectedRequest (final IIndividualRequest individualRequest)
    {
        individualRequest.markRequestAsRejected (null);

        // Check if the Error message is defined as an internal system error
        // TODO use cached error message
        final IErrorMessage[] errorMessages =
                this.getIndividualRequestDao ().query (IErrorMessage.class,
                        Restrictions.eq ("errorCode", individualRequest.getErrorLog ().getErrorCode ()));
        if (errorMessages != null)
        {
            // We got an internal system error, so assign it to the error message that
            // should be recorded
            final IErrorMessage errorMessage = errorMessages[0];

            // Now set the error description from the standard error
            individualRequest.getErrorLog ().setErrorText (errorMessage.getErrorText ());
        }
    }

    /**
     * Mark the request as initially accepted.
     * 
     * @param individualRequest - the individual request object.
     */
    private void updateInitiallyAcceptedRequest (final IIndividualRequest individualRequest)
    {
        individualRequest.markRequestAsInitiallyAccepted ();
    }

    /**
     * Updates the request object. This method is called when the send request to the target application
     * times out.
     * 
     * @param individualRequest the individual request to be marked with reason as not acknowledged
     */
    private void updateRequestTimeOut (final IIndividualRequest individualRequest)
    {
        // Set the updated date .
        // We've already incremented the forwarding attempts and set the status to forwarded so
        // no need to do that here again.
        individualRequest
                .setUpdatedDate (LocalDateTime.fromDateFields (new java.util.Date (System.currentTimeMillis ())));

        // Get the Error message to indicate that call to target application has timed out
        // TODO use cached error code
        final IErrorMessage[] errorMessages =
                this.getIndividualRequestDao ().query (IErrorMessage.class,
                        Restrictions.eq ("errorCode", IErrorMessage.ErrorCode.REQ_NOT_ACK.name ()));

        // Assume that there is only one error message
        assert errorMessages.length == 1;
        final IErrorMessage errorMessage = errorMessages[0];

        // Now create an ErrorLog object with the ErrorMessage object and the IndividualRequest object
        final IErrorLog errorLog = new ErrorLog (errorMessage.getErrorCode (), errorMessage.getErrorText ());

        // Set the error log in the individual request
        individualRequest.setErrorLog (errorLog);

        // now persist the request.
        this.getIndividualRequestDao ().persist (individualRequest);
    }

    /**
     * Updates the request object. This method is called when the send request to target application
     * returns an server error.
     * 
     * @param individualRequest the individual request to be marked with reason as not responding
     */
    private void updateTargetAppUnavailable (final IIndividualRequest individualRequest)
    {
        // Set the updated date .
        individualRequest
                .setUpdatedDate (LocalDateTime.fromDateFields (new java.util.Date (System.currentTimeMillis ())));

        // Set the status to Received
        individualRequest.setRequestStatus (IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus ());

        // Forwarding attempts have already been incremented.

        // Get the Error message for the code ERROR_CODE_REQ_NOT_ACK
        // TODO get error code from cache
        final IErrorMessage[] errorMessages =
                this.getIndividualRequestDao ().query (IErrorMessage.class,
                        Restrictions.eq ("errorCode", IErrorMessage.ErrorCode.REQ_NOT_ACK.name ()));

        final IErrorMessage errorMessage = errorMessages[0];

        // Now create an ErrorLog object with the ErrorMessage object and the
        // IndividualRequest object
        final IErrorLog errorLog = new ErrorLog (errorMessage.getErrorCode (), errorMessage.getErrorText ());

        individualRequest.setErrorLog (errorLog);

        // now persist the request.
        this.getIndividualRequestDao ().persist (individualRequest);
    }

    /**
     * Send the individual request to target application for submission.
     * 
     * @param individualRequest the individual request to be sent to target application.
     * @throws OutageException when the target web service is not responding.
     * @throws TimeoutException when the target web service does not respond back in time.
     */
    private void sendRequestToTargetApp (final IIndividualRequest individualRequest)
        throws OutageException, TimeoutException
    {
        final IGlobalParameter connectionTimeOutParam =
                this.globalParametersCache.getValue (IGlobalParameter.class,
                        IGlobalParameter.ParameterKey.TARGET_APP_TIMEOUT.name ());
        final IGlobalParameter requestTimeOutParam =
                this.globalParametersCache.getValue (IGlobalParameter.class, "TARGET_APP_RESP_TIMEOUT");

        long requestTimeOut = 0;
        long connectionTimeOut = 0;

        if (requestTimeOutParam != null)
        {
            requestTimeOut = Long.valueOf (requestTimeOutParam.getValue ());
        }

        if (connectionTimeOutParam != null)
        {
            connectionTimeOut = Long.valueOf (connectionTimeOutParam.getValue ());
        }

        this.getRequestConsumer ().individualRequest (individualRequest, connectionTimeOut, requestTimeOut);
    }

    /**
     * Updates the request object. This method updates the request status to Rejected
     * and sets the soap fault message in the internal error field of the request.
     * 
     * @param individualRequest the individual request to be marked.
     * @param soapFaultError the soap fault message when the request has failed.
     */
    private void updateRequestSoapError (final IIndividualRequest individualRequest, final String soapFaultError)
    {
        final IErrorMessage[] errorMessages =
                this.getIndividualRequestDao ().query (IErrorMessage.class,
                        Restrictions.eq ("errorCode", IErrorMessage.ErrorCode.SDT_INT_ERR.name ()));

        final IErrorMessage errorMessage = errorMessages[0];

        // Now create an ErrorLog object with the ErrorMessage object and the
        // IndividualRequest object
        final IErrorLog errorLog = new ErrorLog ();
        final java.util.Date currentDate = new java.util.Date (System.currentTimeMillis ());
        errorLog.setCreatedDate (LocalDateTime.fromDateFields (currentDate));
        errorLog.setErrorCode (errorMessage.getErrorCode ());
        errorLog.setErrorText (errorMessage.getErrorText ());

        // Truncate the soap fault error to 1,000 characters to fit
        // inside the database column of length varchar2(4,000 bytes)
        // TODO to use global param to get the value below.
        final int maxAllowedChars = 1000;
        String internalError = soapFaultError;
        if (soapFaultError != null && soapFaultError.length () > maxAllowedChars)
        {
            internalError = soapFaultError.substring (0, maxAllowedChars);
        }
        individualRequest.setInternalSystemError (internalError);

        individualRequest.markRequestAsRejected (errorLog);

        // now persist the request.
        this.getIndividualRequestDao ().persist (individualRequest);
    }

    /**
     * 
     * @return individual request dao
     */
    private IIndividualRequestDao getIndividualRequestDao ()
    {
        return individualRequestDao;
    }

    /**
     * 
     * @param individualRequestDao individual request dao
     */
    public void setIndividualRequestDao (final IIndividualRequestDao individualRequestDao)
    {
        this.individualRequestDao = individualRequestDao;
    }

    /**
     * 
     * @return the request consumer.
     */
    private IConsumerGateway getRequestConsumer ()
    {
        return requestConsumer;
    }

    /**
     * Sets the consumer gateway.
     * 
     * @param requestConsumer the request consumer.
     */
    public void setRequestConsumer (final IConsumerGateway requestConsumer)
    {
        this.requestConsumer = requestConsumer;
    }

    /**
     * Get the global parameters cache.
     * 
     * @return the ICacheable interface for the Global parameters
     */
    private ICacheable getGlobalParametersCache ()
    {
        return globalParametersCache;
    }

    /**
     * Sets the global parameters cache.
     * 
     * @param globalParametersCache the global parameters cache.
     */
    public void setGlobalParametersCache (final ICacheable globalParametersCache)
    {
        this.globalParametersCache = globalParametersCache;
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
     * 
     * @param individualRequest the individual request.
     */
    private void reQueueRequest (final IIndividualRequest individualRequest)
    {
        // Check the forwarding attempts has not exceeded the max forwarding attempts count.
        if (this.canRequestBeRequeued (individualRequest))
        {
            LOGGER.debug ("Re-queuing request for SDT reference [" + individualRequest.getSdtRequestReference () + "]");

            // Create a new message to enqueue.
            final ISdtMessage messageObj = new SdtMessage ();
            messageObj.setSdtRequestReference (individualRequest.getSdtRequestReference ());

            SdtMetricsMBean.getSdtMetrics ().upRequestRequeues ();

            this.getMessageWriter ().queueMessage (messageObj);
        }
        else
        {
            LOGGER.error ("Maximum forwarding attempts exceeded for request " +
                    individualRequest.getSdtRequestReference ());
        }
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
     * @return messageWriter the MessageWriter for the messaging API.
     */
    private IMessageWriter getMessageWriter ()
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
}
