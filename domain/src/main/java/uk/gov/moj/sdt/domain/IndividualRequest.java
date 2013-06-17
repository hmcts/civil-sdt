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

import java.math.BigDecimal;

import org.joda.time.LocalDateTime;

/**
 * When processing an Individual Request within the Bulk Request, the SDT application records
 * the following information as part of the Individual Request details.
 * 
 * @author d130680
 * 
 */
public class IndividualRequest
{
    /**
     * Primary Key.
     */
    private int id;
    /**
     * Bulk submission.
     */
    private BulkSubmission bulkSubmission;
    /**
     * Request type.
     */
    private RequestType requestType;
    /**
     * Identifier defined by End User to uniquely (unique for the End User within
     * the SDT Data Retention Period) identify individual requests within a Bulk Request.
     */
    private String customerRequestReference;
    /**
     * Case Number associated to Request where available. Case Numbers for new claim requests
     * will be allocated by the online Case Management application (MCOL) and passed back to SDT.
     */
    private String caseNumber;
    /**
     * The status of the Individual Request - one of "Submitted", "Held", "Rejected", "Initially Accepted" or
     * "Accepted".
     */
    private String requestStatus;
    /**
     * For claim requests this will be the date of issue. For judgment requests this will be the date of judgment.
     * For paid/written-off/discontinued/withdrawn requests this will be the date the relevant action was taken.
     * For warrant requests this will be the date of issue.
     * If the request was not successfully processed this field will be blank. Formatted as DDMMYYYY.
     */
    private LocalDateTime issuedDate;
    /**
     * SDT bulk reference.
     */
    private String sdtBulkReference;
    /**
     * Line number.
     */
    private int lineNumber;
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
     * Where the request type is claim and the request has been successfully processed by
     * the online case management system this field will be populated.
     * In all other instances this field will be blank. Formatted as DDMMYYYY.
     */
    private LocalDateTime serviceDate;
    /**
     * Warrant Number associated to Request if applicable. Warrant Numbers for new warrant requests will
     * be allocated by the online Case Management application (MCOL) for Warrant Requests only.
     */
    private int warrantNumber;
    /**
     * Where the request type is warrant and the request has been successfully processed by the court case
     * management system this field will be populated. Where a warrant request is rejected the enforcing court
     * code field will be blank.
     */
    private String enforcingCourtCode;
    /**
     * Where the request type is warrant and the request has been successfully processed
     * by the court case management system this field will be populated. Where a warrant request is rejected
     * the enforcing court code field will be blank.
     */
    private String enforcingCourtName;
    /**
     * Fee associated to Request if applicable. This is calculated by the Online Case Management application
     * (MCOL) and passed back to SDT.
     */
    private BigDecimal fee;
    /**
     * Hibernate version number.
     */
    private int version;
    /**
     * XML payload.
     */
    private String payload;

    /**
     * Get primary key.
     * 
     * @return primary key
     */
    public int getId ()
    {
        return id;
    }

    /**
     * Set primary key.
     * 
     * @param id primary key
     */

    public void setId (final int id)
    {
        this.id = id;
    }

    /**
     * Get the Bulk Submission.
     * 
     * @return bulk submission
     */
    public BulkSubmission getBulkSubmission ()
    {
        return bulkSubmission;
    }

    /**
     * Set the Bulk Submission.
     * 
     * @param bulkSubmission bulk submission
     */
    public void setBulkSubmission (final BulkSubmission bulkSubmission)
    {
        this.bulkSubmission = bulkSubmission;
    }

    /**
     * Get the Request Type.
     * 
     * @return request type
     */
    public RequestType getRequestType ()
    {
        return requestType;
    }

    /**
     * Set the Request Type.
     * 
     * @param requestType request type
     */
    public void setRequestType (final RequestType requestType)
    {
        this.requestType = requestType;
    }

    /**
     * Get customer request reference.
     * 
     * @return customer request reference
     */
    public String getCustomerRequestReference ()
    {
        return customerRequestReference;
    }

    /**
     * Set customer request reference.
     * 
     * @param customerRequestReference customer request reference
     */
    public void setCustomerRequestReference (final String customerRequestReference)
    {
        this.customerRequestReference = customerRequestReference;
    }

    /**
     * Get case number.
     * 
     * @return case number
     */
    public String getCaseNumber ()
    {
        return caseNumber;
    }

    /**
     * Set case number.
     * 
     * @param caseNumber case number
     */
    public void setCaseNumber (final String caseNumber)
    {
        this.caseNumber = caseNumber;
    }

    /**
     * Get request status.
     * 
     * @return request status
     */
    public String getRequestStatus ()
    {
        return requestStatus;
    }

    /**
     * Set request status.
     * 
     * @param requestStatus request status
     */
    public void setRequestStatus (final String requestStatus)
    {
        this.requestStatus = requestStatus;
    }

    /**
     * Get issued date.
     * 
     * @return issued date
     */
    public LocalDateTime getIssuedDate ()
    {
        return issuedDate;
    }

    /**
     * Set issued date.
     * 
     * @param issuedDate issued date
     */
    public void setIssuedDate (final LocalDateTime issuedDate)
    {
        this.issuedDate = issuedDate;
    }

    /**
     * Get SDT bulk reference.
     * 
     * @return SDT bulk reference
     */
    public String getSdtBulkReference ()
    {
        return sdtBulkReference;
    }

    /**
     * Set SDT bulk reference.
     * 
     * @param sdtBulkReference SDT bulk reference
     */
    public void setSdtBulkReference (final String sdtBulkReference)
    {
        this.sdtBulkReference = sdtBulkReference;
    }

    /**
     * Get line number.
     * 
     * @return line number
     */
    public int getLineNumber ()
    {
        return lineNumber;
    }

    /**
     * Set line number.
     * 
     * @param lineNumber line number
     */
    public void setLineNumber (final int lineNumber)
    {
        this.lineNumber = lineNumber;
    }

    /**
     * Get SDT request reference.
     * 
     * @return SDT request reference
     */
    public String getSdtRequestReference ()
    {
        return sdtRequestReference;
    }

    /**
     * Set SDT request reference.
     * 
     * @param sdtRequestReference SDT request reference
     */
    public void setSdtRequestReference (final String sdtRequestReference)
    {
        this.sdtRequestReference = sdtRequestReference;
    }

    /**
     * Get created date.
     * 
     * @return created date
     */
    public LocalDateTime getCreatedDate ()
    {
        return createdDate;
    }

    /**
     * Set created date.
     * 
     * @param createdDate created dated
     */
    public void setCreatedDate (final LocalDateTime createdDate)
    {
        this.createdDate = createdDate;
    }

    /**
     * Get updated date.
     * 
     * @return updated date
     */
    public LocalDateTime getUpdatedDate ()
    {
        return updatedDate;
    }

    /**
     * Set updated date.
     * 
     * @param updatedDate updated date
     */
    public void setUpdatedDate (final LocalDateTime updatedDate)
    {
        this.updatedDate = updatedDate;
    }

    /**
     * Get completed date.
     * 
     * @return completed date
     */
    public LocalDateTime getCompletedDate ()
    {
        return completedDate;
    }

    /**
     * Set completed date.
     * 
     * @param completedDate completed date
     */
    public void setCompletedDate (final LocalDateTime completedDate)
    {
        this.completedDate = completedDate;
    }

    /**
     * Get service date.
     * 
     * @return service date
     */
    public LocalDateTime getServiceDate ()
    {
        return serviceDate;
    }

    /**
     * Set service date.
     * 
     * @param serviceDate service date
     */
    public void setServiceDate (final LocalDateTime serviceDate)
    {
        this.serviceDate = serviceDate;
    }

    /**
     * Get warrant number.
     * 
     * @return warrant number
     */
    public int getWarrantNumber ()
    {
        return warrantNumber;
    }

    /**
     * Set warrant number.
     * 
     * @param warrantNumber warrant number
     */
    public void setWarrantNumber (final int warrantNumber)
    {
        this.warrantNumber = warrantNumber;
    }

    /**
     * Get enforcing court code.
     * 
     * @return enforcing court code
     */
    public String getEnforcingCourtCode ()
    {
        return enforcingCourtCode;
    }

    /**
     * Set enforcing court code.
     * 
     * @param enforcingCourtCode enforcing court code
     */
    public void setEnforcingCourtCode (final String enforcingCourtCode)
    {
        this.enforcingCourtCode = enforcingCourtCode;
    }

    /**
     * Get enforcing court name.
     * 
     * @return enforcing court name
     */
    public String getEnforcingCourtName ()
    {
        return enforcingCourtName;
    }

    /**
     * Set enforcing court name.
     * 
     * @param enforcingCourtName enforcing court name
     */
    public void setEnforcingCourtName (final String enforcingCourtName)
    {
        this.enforcingCourtName = enforcingCourtName;
    }

    /**
     * Get fee.
     * 
     * @return fee
     */
    public BigDecimal getFee ()
    {
        return fee;
    }

    /**
     * Set fee.
     * 
     * @param fee fee
     */
    public void setFee (final BigDecimal fee)
    {
        this.fee = fee;
    }

    /**
     * Get the XML payload.
     * 
     * @return xml payload
     */
    public String getPayload ()
    {
        return payload;
    }

    /**
     * Set the XML payload.
     * 
     * @param payload xml payload
     */
    public void setPayload (final String payload)
    {
        this.payload = payload;
    }

    /**
     * Get Hibernate version id.
     * 
     * @return Hibernate version id
     */
    public int getVersion ()
    {
        return version;
    }

}
