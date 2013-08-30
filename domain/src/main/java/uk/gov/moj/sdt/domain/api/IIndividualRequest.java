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

package uk.gov.moj.sdt.domain.api;

import org.joda.time.LocalDateTime;

import uk.gov.moj.sdt.domain.ErrorLog;

/**
 * 
 * Interface for all classes implementing {@link IIndividualRequest}.
 * 
 * @author Manoj Kulkarni
 * 
 */
public interface IIndividualRequest extends IDomainObject
{

    /**
     * Get the Bulk Submission.
     * 
     * @return bulk submission
     */
    IBulkSubmission getBulkSubmission ();

    /**
     * Set the Bulk Submission.
     * 
     * @param bulkSubmission bulk submission
     */
    void setBulkSubmission (final IBulkSubmission bulkSubmission);

    /**
     * Get the Request Type.
     * 
     * @return request type
     */
    IRequestType getRequestType ();

    /**
     * Set the Request Type.
     * 
     * @param requestType request type
     */
    void setRequestType (final IRequestType requestType);

    /**
     * Get customer request reference.
     * 
     * @return customer request reference
     */
    String getCustomerRequestReference ();

    /**
     * Set customer request reference.
     * 
     * @param customerRequestReference customer request reference
     */
    void setCustomerRequestReference (final String customerRequestReference);

    /**
     * Get case number.
     * 
     * @return case number
     */
    // String getCaseNumber ();

    /**
     * Set case number.
     * 
     * @param caseNumber case number
     */
    // void setCaseNumber (final String caseNumber);

    /**
     * Get request status.
     * 
     * @return request status
     */
    String getRequestStatus ();

    /**
     * Set request status.
     * 
     * @param requestStatus request status
     */
    void setRequestStatus (final String requestStatus);

    /**
     * Get issued date.
     * 
     * @return issued date
     */
    // LocalDateTime getIssuedDate ();

    /**
     * Set issued date.
     * 
     * @param issuedDate issued date
     */
    // void setIssuedDate (final LocalDateTime issuedDate);

    /**
     * Get SDT bulk reference.
     * 
     * @return SDT bulk reference
     */
    String getSdtBulkReference ();

    /**
     * Set SDT bulk reference.
     * 
     * @param sdtBulkReference SDT bulk reference
     */
    void setSdtBulkReference (final String sdtBulkReference);

    /**
     * Get line number.
     * 
     * @return line number
     */
    int getLineNumber ();

    /**
     * Set line number.
     * 
     * @param lineNumber line number
     */
    void setLineNumber (final int lineNumber);

    /**
     * Get SDT request reference.
     * 
     * @return SDT request reference
     */
    String getSdtRequestReference ();

    /**
     * Set SDT request reference.
     * 
     * @param sdtRequestReference SDT request reference
     */
    void setSdtRequestReference (final String sdtRequestReference);

    /**
     * Get created date.
     * 
     * @return created date
     */
    LocalDateTime getCreatedDate ();

    /**
     * Set created date.
     * 
     * @param createdDate created dated
     */
    void setCreatedDate (final LocalDateTime createdDate);

    /**
     * Get updated date.
     * 
     * @return updated date
     */
    LocalDateTime getUpdatedDate ();

    /**
     * Set updated date.
     * 
     * @param updatedDate updated date
     */
    void setUpdatedDate (final LocalDateTime updatedDate);

    /**
     * Get completed date.
     * 
     * @return completed date
     */
    LocalDateTime getCompletedDate ();

    /**
     * Set completed date.
     * 
     * @param completedDate completed date
     */
    void setCompletedDate (final LocalDateTime completedDate);

    /**
     * Get service date.
     * 
     * @return service date
     */
    // LocalDateTime getServiceDate ();

    /**
     * Set service date.
     * 
     * @param serviceDate service date
     */
    // void setServiceDate (final LocalDateTime serviceDate);

    /**
     * Get warrant number.
     * 
     * @return warrant number
     */
    // int getWarrantNumber ();

    /**
     * Set warrant number.
     * 
     * @param warrantNumber warrant number
     */
    // void setWarrantNumber (final int warrantNumber);

    /**
     * Get enforcing court code.
     * 
     * @return enforcing court code
     */
    // String getEnforcingCourtCode ();

    /**
     * Set enforcing court code.
     * 
     * @param enforcingCourtCode enforcing court code
     */
    // void setEnforcingCourtCode (final String enforcingCourtCode);

    /**
     * Get enforcing court name.
     * 
     * @return enforcing court name
     */
    // String getEnforcingCourtName ();

    /**
     * Set enforcing court name.
     * 
     * @param enforcingCourtName enforcing court name
     */
    // void setEnforcingCourtName (final String enforcingCourtName);

    /**
     * Get fee.
     * 
     * @return fee
     */
    // BigDecimal getFee ();

    /**
     * Set fee.
     * 
     * @param fee fee
     */
    // void setFee (final BigDecimal fee);

    /**
     * Get the XML payload.
     * 
     * @return xml payload
     */
    String getPayload ();

    /**
     * Set the XML payload.
     * 
     * @param payload xml payload
     */
    void setPayload (final String payload);

    /**
     * Set request retry count.
     * 
     * @return request retry count
     */
    int getRequestRetryCount ();

    /**
     * Set request retry count.
     * 
     * @param requestRetryCount request retry count
     */
    void setRequestRetryCount (final int requestRetryCount);

    /**
     * Get reason rejection code.
     * 
     * @return reason rejection code
     */
    String getRejectionReasonCode ();

    /**
     * Set reason rejection code.
     * 
     * @param rejectionReasonCode reason rejection code
     */
    void setRejectionReasonCode (final String rejectionReasonCode);

    /**
     * Get reason rejection description.
     * 
     * @return reason rejection description
     */
    String getRejectionReasonDescription ();

    /**
     * Set reason rejection description.
     * 
     * @param rejectionReasonDescription reason rejection description
     */
    void setRejectionReasonDescription (final String rejectionReasonDescription);

    /**
     * Get forwarding attempts.
     * 
     * @return forwarding attempts
     */
    int getForwardingAttempts ();

    /**
     * Set forwarding attempts.
     * 
     * @param forwardingAttempts forwarding attempts
     */
    void setForwardingAttempts (final int forwardingAttempts);

    /**
     * Get target application status.
     * 
     * @return target application status
     */
    String getTargetApplicationStatus ();

    /**
     * Set target application status.
     * 
     * @param targetApplicationStatus target application status
     */
    void setTargetApplicationStatus (final String targetApplicationStatus);

    /**
     * Get target application response.
     * 
     * @return target application response
     */
    String getTargetApplicationResponse ();

    /**
     * Set target application response.
     * 
     * @param targetApplicationResponse target application response
     */
    void setTargetApplicationResponse (final String targetApplicationResponse);

    /**
     * Get internal system error.
     * 
     * @return internal system error
     */
    String getInternalSystemError ();

    /**
     * Set internal system error.
     * 
     * @param internalSystemError internal system error
     */
    void setInternalSystemError (final String internalSystemError);

    /**
     * Get error log.
     * 
     * @return error log
     */
    ErrorLog getErrorLog ();

    /**
     * Set error log.
     * 
     * @param errorLog error log
     */
    void setErrorLog (final ErrorLog errorLog);

}