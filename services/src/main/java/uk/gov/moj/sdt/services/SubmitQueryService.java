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

import uk.gov.moj.sdt.consumers.api.IConsumerGateway;
import uk.gov.moj.sdt.consumers.exception.OutageException;
import uk.gov.moj.sdt.consumers.exception.SoapFaultException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.services.api.ISubmitQueryService;

/**
 * Implementation class for submit query service.
 * 
 * @author d130680
 * 
 */
public class SubmitQueryService implements ISubmitQueryService {

	/**
	 * Logger object.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SubmitQueryService.class);

	/**
	 * The consumer gateway that will perform the call to the target application
	 * web service.
	 */
	private IConsumerGateway requestConsumer;

	/**
	 * The ICacheable reference to the global parameters cache.
	 */
	private ICacheable globalParametersCache;

	@Override
	public void submitQuery(final ISubmitQueryRequest submitQueryRequest) {
		// TODO - Implementation is in progress...
		// Initialise request.getBulkCustomer ();
		// Initialise request.getTargetApplication ();
		// Read raw soap message from threadlocal and extract criteria element.
		// Write to threadlocal for use by outbound
		// interceptor.
		// Concurrent and delay processing
		// Read timeout and connection parameters.
		// Call consumer gateway
		// Delegate to Submit Query

		// TODO
		// 1. Data Enrichment. (Need target application read from DB,
		// bulk customer)
		enrich(submitQueryRequest);
		// 2. Parse rawRequest
		// 3. write to outbound - would be covered inside parser
		// throttling - need to think

		// Make call to consumer to submit the request to target
		// application.
		try {
			this.sendRequestToTargetApp(submitQueryRequest);

			// this.updateCompletedRequest(submitQueryRequest);
		} catch (final TimeoutException e) {
			LOGGER.error("Timeout exception for SDT Bulk Customer reference "
					+ submitQueryRequest.getBulkCustomer().getSdtCustomerId()
					+ "]", e);

			// Create new instance of submitQueryResponse, populate error log
			// property - error code, description
			// and return newly created instance

		} catch (final SoapFaultException e) {
			// Create new instance of submitQueryResponse, populate error log
			// property with different msg - error code, description
			// and return newly created instance
			// Log the exception
		}

		LOGGER.debug("Submit query request " + submitQueryRequest.getId()
				+ " processing completed.");

	}

	/**
	 * Enrich submit query instance to prepare for persistence.
	 * 
	 * @param submitQueryRequest
	 *            submit query request
	 */
	private void enrich(final ISubmitQueryRequest submitQueryRequest) {
		LOGGER.debug("enrich bulk submission instance");
	}

	/**
	 * Send the submit query request to target application for submission.
	 * 
	 * @param submitQueryRequest
	 *            the submit query request to be sent to target application.
	 * @throws OutageException
	 *             when the target web service is not responding.
	 * @throws TimeoutException
	 *             when the target web service does not respond back in time.
	 */
	private void sendRequestToTargetApp(
			final ISubmitQueryRequest submitQueryRequest)
			throws OutageException, TimeoutException {
		final IGlobalParameter connectionTimeOutParam = this.globalParametersCache
				.getValue(IGlobalParameter.class,
						IGlobalParameter.ParameterKey.TARGET_APP_TIMEOUT.name());
		final IGlobalParameter requestTimeOutParam = this.globalParametersCache
				.getValue(IGlobalParameter.class, "TARGET_APP_RESP_TIMEOUT");

		long requestTimeOut = 0;
		long connectionTimeOut = 0;

		if (requestTimeOutParam != null) {
			requestTimeOut = Long.valueOf(requestTimeOutParam.getValue());
		}

		if (connectionTimeOutParam != null) {
			connectionTimeOut = Long.valueOf(connectionTimeOutParam.getValue());
		}

		this.requestConsumer.submitQuery(submitQueryRequest, connectionTimeOut,
				requestTimeOut);
	}

}
