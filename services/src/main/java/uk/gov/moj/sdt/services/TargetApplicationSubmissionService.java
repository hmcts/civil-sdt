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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.gov.moj.sdt.consumers.api.IConsumerGateway;
import uk.gov.moj.sdt.consumers.exception.InvalidRequestTypeException;
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
import uk.gov.moj.sdt.services.api.ITargetApplicationSubmissionService;
import uk.gov.moj.sdt.services.messaging.SdtMessage;
import uk.gov.moj.sdt.services.messaging.api.IMessageWriter;
import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;
import uk.gov.moj.sdt.services.utils.GenericXmlParser;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean;
import uk.gov.moj.sdt.validators.CCDReferenceValidator;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import javax.xml.ws.WebServiceException;

import static uk.gov.moj.sdt.domain.RequestType.CLAIM_STATUS_UPDATE;
import static uk.gov.moj.sdt.domain.RequestType.JUDGMENT;
import static uk.gov.moj.sdt.domain.RequestType.JUDGMENT_WARRANT;
import static uk.gov.moj.sdt.domain.RequestType.WARRANT;
import static uk.gov.moj.sdt.domain.api.IIndividualRequest.IndividualRequestStatus.FORWARDED;
import static uk.gov.moj.sdt.domain.api.IIndividualRequest.IndividualRequestStatus.REJECTED;

/**
 * Service class that implements the TargetApplicationSubmissionService.
 *
 * @author Manoj Kulkarni
 */
@Service("TargetApplicationSubmissionService")
public class TargetApplicationSubmissionService extends AbstractSdtService implements
        ITargetApplicationSubmissionService {

    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TargetApplicationSubmissionService.class);

    /**
     * The maximum allowed characters for the soap fault error description.
     */
    private static final int MAX_ALLOWED_CHARS = 2000;

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
     * The consumer gateway that will perform the call to the cmc target application
     * web service.
     */
    private IConsumerGateway cmcRequestConsumer;

    /**
     * The ICacheable reference to the global parameters cache.
     */
    @Lazy
    @Qualifier("GlobalParametersCache")
    @Autowired
    private ICacheable globalParametersCache;

    /**
     * This variable holding the message writer reference.
     */
    private IMessageWriter messageWriter;

    /**
     * The ICacheable reference to the error messages cache.
     */
    @Lazy
    @Qualifier("ErrorMessagesCache")
    @Autowired
    private ICacheable errorMessagesCache;

    @Autowired
    public TargetApplicationSubmissionService(@Qualifier("IndividualRequestDao")
                                                  IIndividualRequestDao individualRequestDao,
                                              @Qualifier("IndividualResponseXmlParser")
                                                  GenericXmlParser individualResponseXmlParser,
                                              @Qualifier("ConsumerGateway")
                                                  IConsumerGateway requestConsumer,
                                              @Qualifier("ConsumerGateway")
                                                  IConsumerGateway cmcRequestConsumer,
                                              @Qualifier("MessageWriter")
                                                  IMessageWriter messageWriter) {
        super(individualRequestDao, individualResponseXmlParser);
        this.individualRequestDao = individualRequestDao;
        this.requestConsumer = requestConsumer;
        this.cmcRequestConsumer = cmcRequestConsumer;
        this.messageWriter = messageWriter;
    }

    @Override
    public void processRequestToSubmit(final String sdtRequestReference) {
        // Look for the individual request matching this unique request reference.
        final IIndividualRequest individualRequest = this.getIndRequestBySdtReference(sdtRequestReference);

        // Proceed ahead if the Individual Request is found.
        if (individualRequest != null) {
            LOGGER.debug("Process individual request [" + individualRequest.getSdtBulkReference() + "].");

            // Check the configurable delay from system parameter and delay the consumer call for that time.
            this.delayRequestProcessing(individualRequest);

            // Update the status of the requested to Forwarded before calling the consumer
            this.updateForwardingRequest(individualRequest);

            // Make call to consumer to submit the request to target application.
            try {
                this.sendRequestToTargetApp(individualRequest);

                this.updateCompletedRequest(individualRequest);
            } catch (final TimeoutException e) {
                LOGGER.error("Timeout exception for SDT reference [" + individualRequest.getSdtRequestReference() +
                        "]");

                // Update the individual request with the reason code for error REQ_NOT_ACK
                this.updateRequestTimeOut(individualRequest);

                // Re-queue message again
                this.reQueueRequest(individualRequest);
            } catch (final SoapFaultException e) {
                // Update the individual request with the soap fault reason
                this.handleSoapFaultAndWebServiceException(individualRequest, e.getMessage());
            } catch (final WebServiceException e) {

                LOGGER.error("Exception calling target application for SDT reference [" +
                        individualRequest.getSdtRequestReference() + "] - " + e.getMessage());

                this.handleSoapFaultAndWebServiceException(individualRequest, e.getMessage());

            } catch (final InvalidRequestTypeException irte) {
                LOGGER.error("Exception calling target application for SDT reference [" +
                                 individualRequest.getSdtRequestReference() + "] - " + irte.getMessage());

                updateRequestRejected(individualRequest);
                updateCompletedRequest(individualRequest);
            }
        } else {
            LOGGER.error("SDT Reference " + sdtRequestReference +
                    " read from message queue not found in database for individual request.");
        }

        return;
    }

    @Override
    public void processDLQRequest(final IIndividualRequest individualRequest, final String requestStatus) {
        // Format the request status parameter to the value acceptable by database.
        final String requestStatusVal = StringUtils.capitalize(requestStatus.toLowerCase());

        // Re-set the Dead Letter flag to false.
        individualRequest.setDeadLetter(false);

        if (REJECTED.getStatus().equals(requestStatusVal)) {
            // If the request status is rejected, add an error log entry, mark as
            // REJECTED and persist the record. Update the bulk submission status
            // to COMPLETED by checking if all the individual requests are either
            // REJECTED or ACCEPTED.
            updateRequestRejected(individualRequest);

        } else {
            // If the request status is FORWARDED, update the status and
            // persist the record in database.
            individualRequest.setRequestStatus(FORWARDED.getStatus());
            individualRequest.setUpdatedDate(LocalDateTime.now());
            this.getIndividualRequestDao().persist(individualRequest);
        }

    }

    /**
     * Updates the request object. This method updates the request status to Rejected
     * and creates an entry in the error log table for the CUST_XML_ERR error message.
     * This method is called after the service team has established that the DLQ request
     * is caused by the Client's data error.
     *
     * @param individualRequest the individual request to be marked.
     */
    private void updateRequestRejected(final IIndividualRequest individualRequest) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Update individual request [" + individualRequest.getSdtBulkReference() +
                    "] as status REJECTED following Service team's investigation of DLQ Request.");
        }

        final IErrorMessage errorMessage =
                this.getErrorMessagesCache().getValue(IErrorMessage.class,
                        IErrorMessage.ErrorCode.CUST_XML_ERR.name());

        // Get the global parameter value for the Contact name
        final IGlobalParameter contactNameParameter =
                this.getGlobalParametersCache().getValue(IGlobalParameter.class,
                        IGlobalParameter.ParameterKey.CONTACT_DETAILS.name());

        final String contactName = contactNameParameter != null ? contactNameParameter.getValue() : "";

        // Now create an ErrorLog object with the ErrorMessage object and the
        // IndividualRequest object
        final IErrorLog errorLog =
                new ErrorLog(errorMessage.getErrorCode(), MessageFormat.format(errorMessage.getErrorText(),
                        contactName));

        // Please note that the SDT internal error field has already been set in this
        // request object when the error was caught during the request evaluation by the
        // MDB queue processing.

        individualRequest.markRequestAsRejected(errorLog);

        // now complete the request. We don't want to extract the raw target app response here
        // so we set the second parameter as false.
        this.updateCompletedRequest(individualRequest, false);
    }

    /**
     * Update the request object. This method is to be called to update the request object
     * to bring it in the request status state of FORWARDED
     *
     * @param individualRequest the IndividualRequest object that contains the
     *                          response payload from the target application.
     */
    private void updateForwardingRequest(final IIndividualRequest individualRequest) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Update individual request [" + individualRequest.getSdtBulkReference() +
                    "] with status FORWARDED.");
        }

        individualRequest.incrementForwardingAttempts();
        this.getIndividualRequestDao().persist(individualRequest);

        // Setup raw XML associated with a single request so that it can be picked up by the outbound
        // interceptor and injected into the outbound XML.
        SdtContext.getContext().setRawOutXml(individualRequest.getRequestPayload());
    }

    /**
     * Updates the request object. This method is called when the send request to the target application
     * times out.
     *
     * @param individualRequest the individual request to be marked with reason as not acknowledged
     */
    private void updateRequestTimeOut(final IIndividualRequest individualRequest) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Update individual request [" + individualRequest.getSdtBulkReference() + "] as timed out.");
        }

        // Set the updated date.
        // We've already incremented the forwarding attempts and set the status to forwarded so
        // no need to do that here again.
        individualRequest.setUpdatedDate(LocalDateTime.now());

        // Get the Error message to indicate that call to target application has timed out
        final IErrorMessage errorMessageParam =
                this.getErrorMessagesCache().getValue(IErrorMessage.class,
                        IErrorMessage.ErrorCode.REQ_NOT_ACK.name());

        // Set the error message text in the internal error field of the individual request
        individualRequest.setInternalSystemError(errorMessageParam.getErrorText());

        // now persist the request.
        this.getIndividualRequestDao().persist(individualRequest);
    }

    /**
     * Send the individual request to target application for submission.
     *
     * @param individualRequest the individual request to be sent to target application.
     * @throws OutageException  when the target web service is not responding.
     * @throws TimeoutException when the target web service does not respond back in time.
     */
    private void sendRequestToTargetApp(final IIndividualRequest individualRequest)
            throws OutageException, TimeoutException {
        LOGGER.debug("Send individual request [" + individualRequest.getSdtBulkReference() +
                "] to target application.");

        final IGlobalParameter connectionTimeOutParam =
                this.globalParametersCache.getValue(IGlobalParameter.class,
                        IGlobalParameter.ParameterKey.TARGET_APP_TIMEOUT.name());
        final IGlobalParameter requestTimeOutParam =
                this.globalParametersCache.getValue(IGlobalParameter.class,
                        IGlobalParameter.ParameterKey.TARGET_APP_RESP_TIMEOUT.name());

        long requestTimeOut = 0;
        long connectionTimeOut = 0;

        if (requestTimeOutParam != null) {
            requestTimeOut = Long.valueOf(requestTimeOutParam.getValue());
        }

        if (connectionTimeOutParam != null) {
            connectionTimeOut = Long.valueOf(connectionTimeOutParam.getValue());
        }

        this.getRequestConsumer(individualRequest).individualRequest(individualRequest, connectionTimeOut, requestTimeOut);
    }

    /**
     * Store exception message as internal error. Mark the request as dead message and write to corresponding target
     * application's dead letter queue (DLQ).
     *
     * @param individualRequest the individual request to be marked.
     * @param errorMessage      the exception message when the request has failed.
     */
    // CHECKSTYLE:OFF
    private void handleSoapFaultAndWebServiceException(final IIndividualRequest individualRequest,
                                                       final String errorMessage)
    // CHECKSTYLE:ON
    {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Update individual request [" + individualRequest.getSdtBulkReference() +
                    "] with internal error and send to dead letter queue.");
        }

        // Truncate the error message to fit database column length
        String internalError = errorMessage;
        if (errorMessage != null && errorMessage.length() > MAX_ALLOWED_CHARS) {
            internalError = errorMessage.substring(0, MAX_ALLOWED_CHARS);
        }
        individualRequest.setInternalSystemError(internalError);
        individualRequest.setDeadLetter(true);

        // now persist the request.
        this.getIndividualRequestDao().persist(individualRequest);

        // Create a new message for DLQ.
        final ISdtMessage messageObj = new SdtMessage();
        messageObj.setSdtRequestReference(individualRequest.getSdtRequestReference());

        final String targetAppCode =
                individualRequest.getBulkSubmission().getTargetApplication().getTargetApplicationCode();

        // Write to dead letter queue.
        this.getMessageWriter().queueMessage(messageObj, targetAppCode, true);
    }

    /**
     * @return individual request dao
     */
    public IIndividualRequestDao getIndividualRequestDao() {
        return individualRequestDao;
    }

    /**
     * @param individualRequestDao individual request dao
     */
    public void setIndividualRequestDao(final IIndividualRequestDao individualRequestDao) {
        this.individualRequestDao = individualRequestDao;
    }

    /**
     * @return the request consumer.
     */
    private IConsumerGateway getRequestConsumer(IIndividualRequest individualRequest) {
        if (isCCDReference(individualRequest)) {
            if (!isValidRequestType(individualRequest)) {
                throw new InvalidRequestTypeException(individualRequest.getRequestType());
            }
            return cmcRequestConsumer;
        }
        return requestConsumer;
    }

    private boolean isValidRequestType(IIndividualRequest individualRequest) {
        String requestType = individualRequest.getRequestType();
        return JUDGMENT.getRequestType().equalsIgnoreCase(requestType)
            || WARRANT.getRequestType().equalsIgnoreCase(requestType)
            || CLAIM_STATUS_UPDATE.getRequestType().equalsIgnoreCase(requestType)
            || JUDGMENT_WARRANT.getRequestType().equalsIgnoreCase(requestType);
    }

    private boolean isCCDReference(IIndividualRequest individualRequest) {
//        individualRequest.getBulkSubmission().getSdtBulkReference()

        return CCDReferenceValidator.isValidCCDReference("");
    }

    /**
     * Get the global parameters cache.
     *
     * @return the ICacheable interface for the Global parameters
     */
    private ICacheable getGlobalParametersCache() {
        return globalParametersCache;
    }

    /**
     * Sets the global parameters cache.
     *
     * @param globalParametersCache the global parameters cache.
     */
    public void setGlobalParametersCache(final ICacheable globalParametersCache) {
        this.globalParametersCache = globalParametersCache;
    }

    /**
     * Reads an system parameter to add delay to the request processing if the
     * system parameter has non null value.
     *
     * @param request IndividualRequest object
     */
    private void delayRequestProcessing(final IIndividualRequest request) {
        // Get the target Application code.
        final String targetApplication =
                request.getBulkSubmission().getTargetApplication().getTargetApplicationCode();
        // Pre-fix the target application code to the global parameter key for the request delay
        final String delayParamName =
                targetApplication.toUpperCase() + "_" + IGlobalParameter.ParameterKey.INDV_REQ_DELAY.name();

        // Get the global parameter for the individual request delay
        final String individualReqProcessingDelay = this.getSystemParameter(delayParamName);

        if (individualReqProcessingDelay != null) {
            // If the global parameter is available, proceed with delaying the request processing
            final long delay = Long.valueOf(individualReqProcessingDelay);
            try {
                LOGGER.debug("Delay request processing for " + delay + " milliseconds.");

                Thread.sleep(delay);
            } catch (final InterruptedException ie) {
                LOGGER.warn("Delay operation interrupted by interrupt exception " + ie.getMessage());
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
    private String getSystemParameter(final String parameterName) {
        final IGlobalParameter globalParameter =
                this.getGlobalParametersCache().getValue(IGlobalParameter.class, parameterName);

        if (globalParameter == null) {
            return null;
        }

        return globalParameter.getValue();

    }

    /**
     * @param individualRequest the individual request.
     */
    private void reQueueRequest(final IIndividualRequest individualRequest) {
        // Check the forwarding attempts has not exceeded the max forwarding attempts count.
        if (this.canRequestBeRequeued(individualRequest)) {
            LOGGER.debug("Re-queuing request for SDT reference [" + individualRequest.getSdtRequestReference() + "]");

            // Create a new message to enqueue.
            final ISdtMessage messageObj = new SdtMessage();
            messageObj.setSdtRequestReference(individualRequest.getSdtRequestReference());

            SdtMetricsMBean.getMetrics().upRequestRequeues();

            final String targetAppCode =
                    individualRequest.getBulkSubmission().getTargetApplication().getTargetApplicationCode();

            this.getMessageWriter().queueMessage(messageObj, targetAppCode, false);
        } else {
            LOGGER.error("Maximum forwarding attempts exceeded for request " +
                    individualRequest.getSdtRequestReference());
        }
    }

    /**
     * @param individualRequest the individual request object.
     * @return true if the request forwarding attempts is less than the maximum forwarding attempts parameter.
     */
    private boolean canRequestBeRequeued(final IIndividualRequest individualRequest) {
        // Get the max forwarding attempts system parameter.
        final String maxForwardingAttemptStr =
                this.getSystemParameter(IGlobalParameter.ParameterKey.MAX_FORWARDING_ATTEMPTS.name());

        if (individualRequest.getForwardingAttempts() <= Integer.valueOf(maxForwardingAttemptStr)) {
            return true;
        }

        return false;
    }

    /**
     * @return messageWriter the MessageWriter for the messaging API.
     */
    private IMessageWriter getMessageWriter() {
        return messageWriter;
    }

    /**
     * @param messageWriter the message writer.
     */
    public void setMessageWriter(final IMessageWriter messageWriter) {
        this.messageWriter = messageWriter;
    }

    /**
     * @return cacheable interface for the error messages cache.
     */
    public ICacheable getErrorMessagesCache() {
        return errorMessagesCache;
    }

    /**
     * @param errorMessagesCache the error messages cache.
     */
    public void setErrorMessagesCache(final ICacheable errorMessagesCache) {
        this.errorMessagesCache = errorMessagesCache;
    }

}
