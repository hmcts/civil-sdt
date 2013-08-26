/* Copyrights and Licenses
 * 
 * Copyright (c) 2013 by the Ministry of Justice. All rights reserved.
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
 * $Id$
 * $LastChangedRevision$
 * $LastChangedDate$
 * $LastChangedBy$ */
package uk.gov.moj.sdt.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.handlers.api.IWsCreateBulkRequestHandler;
import uk.gov.moj.sdt.producers.resolver.BulkRequestToDomainResolver;
import uk.gov.moj.sdt.utils.api.ISdtBulkReferenceGenerator;
import uk.gov.moj.sdt.validators.exception.AbstractBusinessException;
import uk.gov.moj.sdt.visitor.VisitableTreeWalker;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.BulkRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkresponseschema.BulkResponseType;

/**
 * Handles bulk submission request flow.
 * 
 * @author d276205
 * 
 */
public class WsCreateBulkRequestHandler extends AbstractWsCreateHandler
		implements IWsCreateBulkRequestHandler {

	/**
	 * Logger instance.
	 */
	private static final Log LOGGER = LogFactory
			.getLog(WsCreateBulkRequestHandler.class);

	/**
	 * SDT Bulk reference generator.
	 */
	private ISdtBulkReferenceGenerator sdtBulkReferenceGenerator;

	@Override
	public BulkResponseType submitBulk(final BulkRequestType bulkRequestType) {
		LOGGER.info("[submitBulk] started");

		// Initialise response;
		final BulkResponseType bulkResponseType = intialiseResponse(bulkRequestType);

		try {

			// Populate SDT Bulk reference in response.
			bulkResponseType.setSdtBulkReference(sdtBulkReferenceGenerator
					.getSdtBulkReference(bulkRequestType.getHeader()
							.getTargetApplicationId().toString()));

			// Transform web service object to domain object(s)
			final IBulkSubmission bulkSubmission = transformToDomainType(bulkRequestType);

			// Validate domain
			validateDomain(bulkSubmission);

			// Process validated request
			processBulkSubmission(bulkSubmission);

		} catch (final AbstractBusinessException be) {
			handleBusinessException(be, bulkResponseType.getStatus());

		}
		// CHECKSTYLE:OFF
		catch (final Exception e)
		// CHECKSTYLE:ON
		{

			handleException(e, bulkResponseType.getStatus());
		} finally {
			LOGGER.info("[submitBulk] completed");
		}

		return bulkResponseType;
	}

	/**
	 * Process bulk submission instance.
	 * 
	 * @param bulkSubmission
	 *            bulk submission instance.
	 */
	private void processBulkSubmission(final IBulkSubmission bulkSubmission) {
		LOGGER.info("Service called to persist bulk request details");
	}

	/**
	 * Initialise response.
	 * 
	 * @param bulkRequest
	 *            request instance.
	 * @return created BulkResponse instance.
	 */
	private BulkResponseType intialiseResponse(final BulkRequestType bulkRequest) {

		LOGGER.debug("setup initial response");
		final BulkResponseType response = new BulkResponseType();
		response.setSdtService(AbstractWsHandler.SDT_COMX_SERVICE);
		response.setCustomerReference(bulkRequest.getHeader()
				.getCustomerReference());
		response.setRequestCount(bulkRequest.getHeader().getRequestCount());
		final StatusType status = new StatusType();
		response.setStatus(status);
		status.setCode(StatusCodeType.OK);

		return response;
	}

	/**
	 * Transform Web service object to Domain object.
	 * 
	 * @param bulkRequestType
	 *            bulk request
	 * @return {@link IBulkSubmission}
	 */
	private IBulkSubmission transformToDomainType(
			final BulkRequestType bulkRequestType) {
		LOGGER.debug("transform to domain type");

		return BulkRequestToDomainResolver.mapToBulkSubmission(bulkRequestType);

	}

	/**
	 * Validate domain object - {@link IBulkSubmission}.
	 * 
	 * @param bulkSubmission
	 *            domain instance to validate.
	 * @throws AbstractBusinessException
	 *             in case of any business rule validation failure.
	 */
	private void validateDomain(final IBulkSubmission bulkSubmission)
			throws AbstractBusinessException {
		LOGGER.debug("[validateDomain] started");

		VisitableTreeWalker.walk(bulkSubmission, "Validator");

	}

	/**
	 * @param sdtBulkReferenceGenerator
	 *            the sdtBulkReferenceGenerator to set
	 */
	public void setSdtBulkReferenceGenerator(
			final ISdtBulkReferenceGenerator sdtBulkReferenceGenerator) {
		this.sdtBulkReferenceGenerator = sdtBulkReferenceGenerator;
	}

}
