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

/**
 * Error log.
 * 
 * @author d130680
 *
 */
public class ErrorLog {
	
	/**
	 * Primary key.
	 */
	private int id;
	
	/**
	 * Bulk submission. 
	 */
	private BulkSubmission bulkSubmission;
	
	/**
	 * Individual request, null for error raised on bulk file.
	 */
	private IndividualRequest individualRequest;
	
	/**
	 * Error message.
	 */
	private ErrorMessage errorMessage;	

	/**
	 * Date record was created.
	 */
	private LocalDateTime createdDate;
	
	/**
	 * Date record was updated.
	 */
	private LocalDateTime updatedDate;
	
	/**
	 * Hibernate version number.
	 */
	private int version;
	
	/**
	 * The error text.
	 */
	private String errorText;

	/**
	 * Get primary key.
	 * @return primary key
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set primary key.
	 * 
	 * @param id primary key
	 */
	public void setId(final int id) {
		this.id = id;
	}
	/**
	 * Get the Bulk Submission.
	 * @return bulk submission
	 */
	public BulkSubmission getBulkSubmission() {
		return bulkSubmission;
	}

	/**
	 * Set the Bulk Submission.
	 * @param bulkSubmission bulk submission
	 */
	public void setBulkSubmission(final BulkSubmission bulkSubmission) {
		this.bulkSubmission = bulkSubmission;
	}

	/**
	 * Get the Individual Request.
	 * @return individual request
	 */
	public IndividualRequest getIndividualRequest() {
		return individualRequest;
	}

	/**
	 * Set the Individual Request.
	 * @param individualRequest individual request
	 */
	public void setIndividualRequest(final IndividualRequest individualRequest) {
		this.individualRequest = individualRequest;
	}

	/**
	 * Get the Error Message.
	 * @return error message
	 */
	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Set the Error Message.
	 * @param errorMessage error message
	 */
	public void setErrorMessage(final ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Get created date.
	 * @return created date
	 */
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	/**
	 * Set created date.
	 * @param createdDate created dated
	 */
	public void setCreatedDate(final LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Get updated date.
	 * @return updated date
	 */
	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}
	/**
	 * Set updated date.
	 * @param updatedDate updated date
	 */
	public void setUpdatedDate(final LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * Get error Text.
	 * @return error text
	 */
	public String getErrorText() {
		return errorText;
	}

	/**
	 * Set error Text.
	 * @param errorText error text
	 */
	public void setErrorText(final String errorText) {
		this.errorText = errorText;
	}

	/**
	 * Get Hibernate version id.
	 * @return Hibernate version id
	 */
	public int getVersion() {
		return version;
	}

}
