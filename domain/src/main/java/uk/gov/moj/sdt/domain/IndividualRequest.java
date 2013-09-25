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

package uk.gov.moj.sdt.domain;

import org.joda.time.LocalDateTime;

import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;

/**
 * When processing an Individual Request within the Bulk Request, the SDT application records
 * the following information as part of the Individual Request details.
 * 
 * @author d130680
 * 
 */
public class IndividualRequest extends AbstractDomainObject implements IIndividualRequest
{
    /**
     * Bulk submission.
     */
    private IBulkSubmission bulkSubmission;

    /**
     * Identifier defined by End User to uniquely (unique for the End User within
     * the SDT Data Retention Period) identify individual requests within a Bulk Request.
     */
    private String customerRequestReference;

    /**
     * Case Number associated to Request where available. Case Numbers for new claim requests
     * will be allocated by the online Case Management application (MCOL) and passed back to SDT.
     */
    // private String caseNumber;

    /**
     * The status of the Individual Request - one of "Forwarded", "Received", "Rejected", "Initially Accepted" or
     * "Accepted".
     */
    private String requestStatus;

    /**
     * For claim requests this will be the date of issue. For judgment requests this will be the date of judgment.
     * For paid/written-off/discontinued/withdrawn requests this will be the date the relevant action was taken.
     * For warrant requests this will be the date of issue.
     * If the request was not successfully processed this field will be blank. Formatted as DDMMYYYY.
     */
    // private LocalDateTime issuedDate;

    /**
     * SDT bulk reference.
     */
    private String sdtBulkReference;

    /**
     * Line number.
     */
    private int lineNumber;

    /**
     * Number of attempts made to submit request.
     */
    private int requestRetryCount;

    /**
     * SDT request reference.
     */
    private String sdtRequestReference;

    /**
     * Date record was created.
     */
    private LocalDateTime createdDate;

    /**
     * Date record was updated.
     */
    private LocalDateTime updatedDate;

    /**
     * Date/Time the Individual Request processing was completed. This is the Date/Time that the final outcome
     * was received from the Court Case Management system following full processing
     */
    private LocalDateTime completedDate;

    /**
     * Error code associated with Rejection Reason Description.
     * Note: this can be populated following initial SDT Service processing or subsequent
     * Target Application processing.
     */
    private String rejectionReasonCode;

    /**
     * Text description of Request Status, e.g. Request error description for a request with status
     * "Rejected". Note: this can be populated following initial SDT Service processing or subsequent
     * Target Application processing.
     */
    private String rejectionReasonDescription;

    /**
     * An Individual Request can be forwarded for up to the defined maximum forwarding attempts.
     * This value indicates the number of attempts to forward the Individual Request to date.
     */
    private int forwardingAttempts;

    /**
     * Target Application status for Individual Request processing.
     */
    private String targetApplicationStatus;

    /**
     * Target Application Response for Individual Request processing.
     */
    private String targetApplicationResponse;

    /**
     * 
     * Error log.
     */
    private IErrorLog errorLog;

    /**
     * XML payload.
     */
    private String payload;

    /**
     * Internal system error.
     */
    private String internalSystemError;

    @Override
    public IBulkSubmission getBulkSubmission ()
    {
        return bulkSubmission;
    }

    @Override
    public void setBulkSubmission (final IBulkSubmission bulkSubmission)
    {
        this.bulkSubmission = bulkSubmission;
    }

    @Override
    public String getCustomerRequestReference ()
    {
        return customerRequestReference;
    }

    @Override
    public void setCustomerRequestReference (final String customerRequestReference)
    {
        this.customerRequestReference = customerRequestReference;
    }

    @Override
    public String getRequestStatus ()
    {
        return requestStatus;
    }

    @Override
    public void setRequestStatus (final String requestStatus)
    {
        this.requestStatus = requestStatus;
    }

    @Override
    public String getSdtBulkReference ()
    {
        return sdtBulkReference;
    }

    @Override
    public void setSdtBulkReference (final String sdtBulkReference)
    {
        this.sdtBulkReference = sdtBulkReference;
    }

    @Override
    public int getLineNumber ()
    {
        return lineNumber;
    }

    @Override
    public void setLineNumber (final int lineNumber)
    {
        this.lineNumber = lineNumber;
    }

    @Override
    public String getSdtRequestReference ()
    {
        return sdtRequestReference;
    }

    @Override
    public void setSdtRequestReference (final String sdtRequestReference)
    {
        this.sdtRequestReference = sdtRequestReference;
    }

    @Override
    public LocalDateTime getCreatedDate ()
    {
        return createdDate;
    }

    @Override
    public void setCreatedDate (final LocalDateTime createdDate)
    {
        this.createdDate = createdDate;
    }

    @Override
    public LocalDateTime getUpdatedDate ()
    {
        return updatedDate;
    }

    @Override
    public void setUpdatedDate (final LocalDateTime updatedDate)
    {
        this.updatedDate = updatedDate;
    }

    @Override
    public LocalDateTime getCompletedDate ()
    {
        return completedDate;
    }

    @Override
    public void setCompletedDate (final LocalDateTime completedDate)
    {
        this.completedDate = completedDate;
    }

    @Override
    public String getPayload ()
    {
        return payload;
    }

    @Override
    public void setPayload (final String payload)
    {
        this.payload = payload;
    }

    @Override
    public int getRequestRetryCount ()
    {
        return requestRetryCount;
    }

    @Override
    public void setRequestRetryCount (final int requestRetryCount)
    {
        this.requestRetryCount = requestRetryCount;
    }

    @Override
    public String getRejectionReasonCode ()
    {
        return rejectionReasonCode;
    }

    @Override
    public void setRejectionReasonCode (final String rejectionReasonCode)
    {
        this.rejectionReasonCode = rejectionReasonCode;
    }

    @Override
    public String getRejectionReasonDescription ()
    {
        return rejectionReasonDescription;
    }

    @Override
    public void setRejectionReasonDescription (final String rejectionReasonDescription)
    {
        this.rejectionReasonDescription = rejectionReasonDescription;
    }

    @Override
    public int getForwardingAttempts ()
    {
        return forwardingAttempts;
    }

    @Override
    public void setForwardingAttempts (final int forwardingAttempts)
    {
        this.forwardingAttempts = forwardingAttempts;
    }

    @Override
    public String getTargetApplicationStatus ()
    {
        return targetApplicationStatus;
    }

    @Override
    public void setTargetApplicationStatus (final String targetApplicationStatus)
    {
        this.targetApplicationStatus = targetApplicationStatus;
    }

    @Override
    public String getTargetApplicationResponse ()
    {
        return targetApplicationResponse;
    }

    @Override
    public void setTargetApplicationResponse (final String targetApplicationResponse)
    {
        this.targetApplicationResponse = targetApplicationResponse;
    }

    @Override
    public String getInternalSystemError ()
    {
        return internalSystemError;
    }

    @Override
    public void setInternalSystemError (final String internalSystemError)
    {
        this.internalSystemError = internalSystemError;
    }

    @Override
    public IErrorLog getErrorLog ()
    {
        return errorLog;
    }

    @Override
    public void setErrorLog (final IErrorLog errorLog)
    {
        this.errorLog = errorLog;
    }

}
