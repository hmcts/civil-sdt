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

import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IErrorMessage;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.services.api.ITargetApplicationSubmissionService;

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
    private static final Logger LOG = LoggerFactory.getLogger (TargetApplicationSubmissionService.class);

    /**
     * Standard error code if the request is not acknowledged.
     */
    private static final String ERROR_CODE_REQ_NOT_ACK = "REQ_NOT_ACK";

    /**
     * Individual Request Dao to perform operations on the individual request object.
     */
    private IIndividualRequestDao individualRequestDao;

    @Override
    public IIndividualRequest getRequestToSubmit (final String sdtRequestReference)
    {
        final IIndividualRequest individualRequest =
                this.getIndividualRequestDao ().getRequestBySdtReference (sdtRequestReference);

        if (individualRequest == null)
        {
            LOG.error ("No Individual Request found for SDT Request Reference Id " + sdtRequestReference);
        }

        return individualRequest;
    }

    @Override
    public void updateForwardingRequest (final IIndividualRequest individualRequest)
    {
        individualRequest.incrementForwardingAttempts ();
        this.getIndividualRequestDao ().persist (individualRequest);
    }

    @Override
    public void updateCompletedRequest (final IIndividualRequest individualRequest)
    {
        LOG.debug ("Updating completed request " + individualRequest.getSdtRequestReference ());
        final String requestStatus = individualRequest.getRequestStatus ();
        LOG.debug ("Request status is " + requestStatus);
        final IIndividualRequest.IndividualRequestStatus requestStatusEnum =
                IIndividualRequest.IndividualRequestStatus.valueOf (requestStatus.toUpperCase ());

        LOG.debug ("Request status is " + requestStatusEnum.name ());

        if (requestStatusEnum == IIndividualRequest.IndividualRequestStatus.ACCEPTED)
        {
            this.updateAcceptedRequest (individualRequest);
        }

        if (requestStatusEnum == IIndividualRequest.IndividualRequestStatus.INITIALLY_ACCEPTED)
        {
            this.updateInitiallyAcceptedRequest (individualRequest);
        }

        if (requestStatusEnum == IIndividualRequest.IndividualRequestStatus.REJECTED)
        {
            this.updateRejectedRequest (individualRequest);
        }

        // now persist the request.
        LOG.debug ("Persisting the Individual Request " + individualRequest.getSdtRequestReference ());
        this.getIndividualRequestDao ().persist (individualRequest);
        LOG.debug ("Now Persisted the Individual Request " + individualRequest.getSdtRequestReference ());

    }

    /**
     * Mark the request with status as "Accepted".
     * 
     * @param individualRequest the individual request object.
     */
    private void updateAcceptedRequest (final IIndividualRequest individualRequest)
    {
        individualRequest.markRequestAsAccepted ();

        LOG.debug ("Request completed date is set to " + individualRequest.getCompletedDate ());

        LOG.debug ("Request updated date is set to " + individualRequest.getUpdatedDate ());
    }

    /**
     * Mark the request with status as "Rejected".
     * 
     * @param individualRequest the individual request object.
     */
    private void updateRejectedRequest (final IIndividualRequest individualRequest)
    {
        individualRequest.markRequestAsRejected ();

        LOG.debug ("Request completed date is set to " + individualRequest.getCompletedDate ());

        LOG.debug ("Request updated date is set to " + individualRequest.getUpdatedDate ());

        // Check if the Error message is defined as an internal system error
        final IErrorMessage[] errorMessages =
                this.getIndividualRequestDao ().query (IErrorMessage.class,
                        Restrictions.eq ("errorCode", individualRequest.getErrorLog ().getErrorCode ()));
        IErrorMessage errorMessage = null;
        if (errorMessages != null)
        {
            // We got an internal system error, so assign it to the error message that
            // should be recorded
            errorMessage = errorMessages[0];

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

        LOG.debug ("Request updated date is set to " + individualRequest.getUpdatedDate ());
    }

    @Override
    public void updateRequestNotAcknowledged (final IIndividualRequest individualRequest)
    {
        // Set the updated date .
        // We've already incremented the forwarding attempts and set the status to forwarded so
        // no need to do that here again.
        individualRequest
                .setUpdatedDate (LocalDateTime.fromDateFields (new java.util.Date (System.currentTimeMillis ())));

        // Get the Error message for the code ERROR_CODE_REQ_NOT_ACK
        final IErrorMessage[] errorMessages =
                this.getIndividualRequestDao ().query (IErrorMessage.class,
                        Restrictions.eq ("errorCode", ERROR_CODE_REQ_NOT_ACK));

        final IErrorMessage errorMessage = errorMessages[0];

        // Now create an ErrorLog object with the ErrorMessage object and the
        // IndividualRequest object
        final IErrorLog errorLog = new ErrorLog ();
        final java.util.Date currentDate = new java.util.Date (System.currentTimeMillis ());
        errorLog.setCreatedDate (LocalDateTime.fromDateFields (currentDate));
        errorLog.setErrorCode (errorMessage.getErrorCode ());
        errorLog.setErrorText (errorMessage.getErrorText ());
        errorLog.setIndividualRequest (individualRequest);

        // Set the error log in the individual request
        individualRequest.setErrorLog (errorLog);

        // now persist the request.
        this.getIndividualRequestDao ().persist (individualRequest);
    }

    @Override
    public void updateRequestNotResponding (final IIndividualRequest individualRequest)
    {
        // Set the updated date .
        individualRequest
                .setUpdatedDate (LocalDateTime.fromDateFields (new java.util.Date (System.currentTimeMillis ())));

        // Set the status to Received
        individualRequest.setRequestStatus (IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus ());

        // Forwarding attempts have already been incremented.

        // Get the Error message for the code ERROR_CODE_REQ_NOT_ACK
        final IErrorMessage[] errorMessages =
                this.getIndividualRequestDao ().query (IErrorMessage.class,
                        Restrictions.eq ("errorCode", ERROR_CODE_REQ_NOT_ACK));

        final IErrorMessage errorMessage = errorMessages[0];

        // Now create an ErrorLog object with the ErrorMessage object and the
        // IndividualRequest object
        final IErrorLog errorLog = new ErrorLog ();
        final java.util.Date currentDate = new java.util.Date (System.currentTimeMillis ());
        errorLog.setCreatedDate (LocalDateTime.fromDateFields (currentDate));
        errorLog.setErrorCode (errorMessage.getErrorCode ());
        errorLog.setErrorText (errorMessage.getErrorText ());
        errorLog.setIndividualRequest (individualRequest);

        individualRequest.setErrorLog (errorLog);

        // now persist the request.
        this.getIndividualRequestDao ().persist (individualRequest);

    }

    /**
     * 
     * @return individual request dao
     */
    public IIndividualRequestDao getIndividualRequestDao ()
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

}
