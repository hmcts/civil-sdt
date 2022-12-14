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

import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.IServiceRequest;
import uk.gov.moj.sdt.domain.api.ITargetApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * For a Submit Bulk Request, the SDT application records the
 * Bulk Submissions detail when processing a Bulk Request.
 *
 * @author d130680
 */
public class BulkSubmission extends AbstractDomainObject implements IBulkSubmission {

    /**
     * Bulk customer.
     */
    private IBulkCustomer bulkCustomer;

    /**
     * The target application that SDT routes the Bulk Request onto for subsequent processing.
     */
    private ITargetApplication targetApplication;

    /**
     * A unique SDT Reference (unique within the SDT Data Retention Period)
     * generated by SDT and assigned to a single Bulk Request.
     */
    private String sdtBulkReference;

    /**
     * A unique End User specified Reference (unique within the SDT Data Retention Period)
     * assigned to a single Bulk Request.
     */
    private String customerReference;

    /**
     * Date record was created.
     */
    private LocalDateTime createdDate;

    /**
     * The total number of requests expected to be contained within the Bulk Request.
     */
    private long numberOfRequest;

    /**
     * The status of the Bulk Request - one of "Uploaded", "Failed", "Validated", or "Completed"
     * to reflect the current status of SDT processing. Maintained by SDT.
     */
    private String submissionStatus;

    /**
     * Date/Time the Bulk Request completed processing.
     */
    private LocalDateTime completedDate;

    /**
     * Date record was updated.
     */
    private LocalDateTime updatedDate;

    /**
     * The error code.
     */
    private String errorCode;

    /**
     * The error text.
     */
    private String errorText;

    /**
     * XML payload.
     */
    private String payload;

    /**
     * List of individual requests.
     */
    private List<IIndividualRequest> individualRequests = new ArrayList<>();

    /**
     * Service request is an audit log for incoming and outgoing request.
     */
    private IServiceRequest serviceRequest;

    /**
     * Constructor for {@link BulkSubmission}.
     */
    public BulkSubmission() {
        super();
    }

    @Override
    public IBulkCustomer getBulkCustomer() {
        return bulkCustomer;
    }

    @Override
    public void setBulkCustomer(final IBulkCustomer bulkCustomer) {
        this.bulkCustomer = bulkCustomer;
    }

    @Override
    public ITargetApplication getTargetApplication() {
        return targetApplication;
    }

    @Override
    public void setTargetApplication(final ITargetApplication targetApplication) {
        this.targetApplication = targetApplication;
    }

    @Override
    public String getSdtBulkReference() {
        return sdtBulkReference;
    }

    @Override
    public void setSdtBulkReference(final String sdtBulkReference) {
        this.sdtBulkReference = sdtBulkReference;
    }

    @Override
    public String getCustomerReference() {
        return customerReference;
    }

    @Override
    public void setCustomerReference(final String customerReference) {
        this.customerReference = customerReference;
    }

    @Override
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public long getNumberOfRequest() {
        return numberOfRequest;
    }

    @Override
    public void setNumberOfRequest(final long numberOfRequest) {
        this.numberOfRequest = numberOfRequest;
    }

    @Override
    public String getSubmissionStatus() {
        return submissionStatus;
    }

    @Override
    public void setSubmissionStatus(final String submissionStatus) {
        this.submissionStatus = submissionStatus;
    }

    @Override
    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    @Override
    public void setCompletedDate(final LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }

    @Override
    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    @Override
    public void setUpdatedDate(final LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public void setPayload(final String payload) {
        this.payload = payload;
    }

    @Override
    public List<IIndividualRequest> getIndividualRequests() {
        return individualRequests;
    }

    @Override
    public void setIndividualRequests(final List<IIndividualRequest> individualRequests) {
        this.individualRequests = individualRequests;
    }

    @Override
    public void addIndividualRequest(final IIndividualRequest individualRequest) {
        individualRequest.setBulkSubmission(this);
        this.individualRequests.add(individualRequest);
    }

    @Override
    public IServiceRequest getServiceRequest() {
        return serviceRequest;
    }

    @Override
    public void setServiceRequest(final IServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    @Override
    public String getErrorText() {
        return errorText;
    }

    @Override
    public void setErrorText(final String errorText) {
        this.errorText = errorText;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public boolean hasError() {
        return errorCode != null;
    }

    @Override
    public void markAsValidated() {
        if (!getSubmissionStatus().equals(IBulkSubmission.BulkRequestStatus.COMPLETED.getStatus())) {
            setSubmissionStatus(IBulkSubmission.BulkRequestStatus.VALIDATED.getStatus());
        }
    }

    @Override
    public void markAsCompleted() {
        setSubmissionStatus(IBulkSubmission.BulkRequestStatus.COMPLETED.getStatus());
        setUpdatedDate(LocalDateTime.now());
        setCompletedDate(LocalDateTime.now());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(getHashId(this) + "[");
        sb.append(super.toString());
        sb.append(", bulkCustomer=").append(this.getBulkCustomer());
        sb.append(", targetApplication=").append(this.getTargetApplication());
        sb.append(", sdtBulkReference=").append(this.getSdtBulkReference());
        sb.append(", customerReference=").append(this.getCustomerReference());
        sb.append(", createdDate=").append(this.getCreatedDate());
        sb.append(", numberOfRequest=").append(this.getNumberOfRequest());
        sb.append(", submissionStatus=").append(this.getSubmissionStatus());
        sb.append(", completedDate=").append(this.getCompletedDate());
        sb.append(", updatedDate=").append(this.getUpdatedDate());
        sb.append(", errorCode=").append(this.getErrorCode());
        sb.append(", errorText=").append(this.getErrorText());
        sb.append(", payload=").append(getHashId(this.getPayload()));
        sb.append("]");
        return sb.toString();
    }
}
