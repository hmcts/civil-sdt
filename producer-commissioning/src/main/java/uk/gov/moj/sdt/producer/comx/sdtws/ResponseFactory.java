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

package uk.gov.moj.sdt.producer.comx.sdtws;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.ws._2013.sdt.baseschema.ErrorType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.BulkRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkresponseschema.BulkResponseType;


/**
 * Generates a success or fail bulk response type.
 * 
 * @author d130680
 *
 */
public class ResponseFactory implements IResponseFactory {
	
    /**
     * Logger for this class.
     */
    private static final Log LOGGER = LogFactory.getLog (ResponseFactory.class);
	
	/**
	 * Generates a successful response.
	 * 
	 * @param bulkRequestType the bulk request information
	 * @return success response
	 */
	public BulkResponseType createSuccessResponse(final BulkRequestType bulkRequestType) {
				
		final BulkResponseType successResponse = new BulkResponseType();
		final HeaderType header =  bulkRequestType.getHeader();
		final StatusType status = new StatusType();
				
		status.setCode(StatusCodeType.OK);
				
		successResponse.setCustomerReference(header.getCustomerReference());
		successResponse.setRequestCount(header.getRequestCount());
		successResponse.setStatus(status);
		successResponse.setSubmittedDate(Calendar.getInstance());	
        successResponse.setSdtBulkReference ("COMMISSIONING-12345678");
		
		return successResponse;
		
	}

	/**
	 * Return a failure response type.
	 * 
	 * @param f the type of failure to return
	 * @param b the bulk request information
	 * @return failed response
	 */
	public BulkResponseType createFailResponse (final BulkResponseFailure f, final BulkRequestType b) {
		final BulkResponseType failureResponse = new BulkResponseType();
		final HeaderType header =  b.getHeader();
		final StatusType status = new StatusType();
		final ErrorType errorType = new ErrorType();
		
		errorType.setCode(f.getErrorCode());
		errorType.setDescription(f.getErrorDescription());
		status.setCode(StatusCodeType.ERROR);
		status.setError(errorType);
		
		failureResponse.setCustomerReference(header.getCustomerReference());
		failureResponse.setRequestCount(header.getRequestCount());
		failureResponse.setStatus(status);
		failureResponse.setSubmittedDate(Calendar.getInstance());	
        failureResponse.setSdtBulkReference ("COMMISSIONING-12345678");

        LOGGER.debug ("Returning failure response code[" + f.getErrorCode () 
                + "] message[" + f.getErrorDescription () +  "]");
		return failureResponse;
		
	}
}
