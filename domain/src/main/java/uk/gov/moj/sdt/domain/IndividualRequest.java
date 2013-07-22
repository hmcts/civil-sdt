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
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.IRequestType;

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
     * Request type.
     */
    private IRequestType requestType;

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
    // private LocalDateTime serviceDate;

    /**
     * Warrant Number associated to Request if applicable. Warrant Numbers for new warrant requests will
     * be allocated by the online Case Management application (MCOL) for Warrant Requests only.
     */
    // private int warrantNumber;

    /**
     * Where the request type is warrant and the request has been successfully processed by the court case
     * management system this field will be populated. Where a warrant request is rejected the enforcing court
     * code field will be blank.
     */
    // private String enforcingCourtCode;

    /**
     * Where the request type is warrant and the request has been successfully processed
     * by the court case management system this field will be populated. Where a warrant request is rejected
     * the enforcing court code field will be blank.
     */
    // private String enforcingCourtName;

    /**
     * Fee associated to Request if applicable. This is calculated by the Online Case Management application
     * (MCOL) and passed back to SDT.
     */
    // private BigDecimal fee;

    /**
     * XML payload.
     */
    private String payload;

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
    public IRequestType getRequestType ()
    {
        return requestType;
    }

    @Override
    public void setRequestType (final IRequestType requestType)
    {
        this.requestType = requestType;
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

    // @Override
    // public String getCaseNumber ()
    // {
    // return caseNumber;
    // }

    // @Override
    // public void setCaseNumber (final String caseNumber)
    // {
    // this.caseNumber = caseNumber;
    // }

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

    // @Override
    // public LocalDateTime getIssuedDate ()
    // {
    // return issuedDate;
    // }

    // @Override
    // public void setIssuedDate (final LocalDateTime issuedDate)
    // {
    // this.issuedDate = issuedDate;
    // }

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

    // @Override
    // public LocalDateTime getServiceDate ()
    // {
    // return serviceDate;
    // }

    // @Override
    // public void setServiceDate (final LocalDateTime serviceDate)
    // {
    // this.serviceDate = serviceDate;
    // }

    // @Override
    // public int getWarrantNumber ()
    // {
    // return warrantNumber;
    // }

    // @Override
    // public void setWarrantNumber (final int warrantNumber)
    // {
    // this.warrantNumber = warrantNumber;
    // }

    // @Override
    // public String getEnforcingCourtCode ()
    // {
    // return enforcingCourtCode;
    // }

    // @Override
    // public void setEnforcingCourtCode (final String enforcingCourtCode)
    // {
    // this.enforcingCourtCode = enforcingCourtCode;
    // }

    // @Override
    // public String getEnforcingCourtName ()
    // {
    // return enforcingCourtName;
    // }

    // @Override
    // public void setEnforcingCourtName (final String enforcingCourtName)
    // {
    // this.enforcingCourtName = enforcingCourtName;
    // }

    // @Override
    // public BigDecimal getFee ()
    // {
    // return fee;
    // }

    // @Override
    // public void setFee (final BigDecimal fee)
    // {
    // this.fee = fee;
    // }

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

}
