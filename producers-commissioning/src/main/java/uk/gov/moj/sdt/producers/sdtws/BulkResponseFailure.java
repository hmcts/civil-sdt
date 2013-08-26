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
package uk.gov.moj.sdt.producers.sdtws;


/**
 * Enumeration for permissible failure response type.
 * 
 * @author d130680
 *
 */
public enum BulkResponseFailure {

    /**
     * SDT Customer Id not recognised.
     */
    SDT_CUSTOMER_NOT_FOUND ("100", "The SDT Customer Id is not recognized."),

    /**
     * Customer reference for bulk request not unique.
     */
    BULK_CUSTOMER_REFERENCE_NOT_UNIQUE ("200", "The customer reference for bulk request is not unique"),
	
	/**
	 * Invalid target application.
	 */
	INVALID_TARGET_APPLICATION("300", "Invalid target application."),

    /**
     * Request count mismatch failure response.
     */
    REQUEST_COUNT_MISMATCH ("400", "The request count does not match."),

	/**
	 * Invalid request type.
	 */
    INVALID_REQUEST_TYPE ("500", "Invalid request type."),
	
    /**
     * Customer reference for individual request not unique.
     */
    IND_REQUEST_CUSTOMER_REFERENCE_NOT_UNIQUE ("600", "The customer reference for individual request is not unique"),

    /**
     * Unexpected system error.
     */
    SYSTEM_ERROR ("999", "System error.");
	
	/**
	 * Error code.
	 */
	private String errorCode;
	
	/**
	 * Error description.
	 */
	private String errorDescription;
	
	/**
	 * Set the error code and description.
	 * 
	 * @param code error code
	 * @param desc error description
	 */
	private BulkResponseFailure(final String code, final String desc) {
		errorCode = code;
		errorDescription = desc;
	}
 
	
	/**
	 * Get the error code.
	 * 
	 * @return error code
	 */
	public String getErrorCode() {
		return errorCode;
	}
	
	/**
	 * Get the error description.
	 * 
	 * @return error code
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

}
