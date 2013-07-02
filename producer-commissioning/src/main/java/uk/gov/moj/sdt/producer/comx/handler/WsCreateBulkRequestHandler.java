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
package uk.gov.moj.sdt.producer.comx.handler;

import java.math.BigInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.producers.api.AbstractWsCreateHandler;
import uk.gov.moj.sdt.producers.api.IWsCreateBulkRequestHandler;
import uk.gov.moj.sdt.producers.resolver.BulkRequestToDomainResolver;
import uk.gov.moj.sdt.validators.exception.AbstractBusinessException;
import uk.gov.moj.sdt.visitor.VisitableTreeWalker;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.ErrorType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.BulkRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.McolRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkresponseschema.BulkResponseType;

/**
 * Handles bulk submission request flow.
 * 
 * @author d276205
 * 
 */
public class WsCreateBulkRequestHandler extends AbstractWsCreateHandler implements IWsCreateBulkRequestHandler
{

    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (WsCreateBulkRequestHandler.class);

    @Override
    public BulkResponseType submitBulk (final BulkRequestType bulkRequestType)
    {
        LOGGER.info ("[submitBulk] started");

        // Initialise response;
        final BulkResponseType bulkResponseType = intialiseResponse (bulkRequestType);

        try
        {

            // Log incoming message to database - could be possible to implement in interceptor.
            logIncomingRequest (bulkRequestType);

            // Validate request to ensure that fields contain correct data. e.g. request count.
            final ErrorType wsErrorType = validateWsType (bulkRequestType);

            if (wsErrorType != null)
            {
                populateError (bulkResponseType.getStatus (), wsErrorType, StatusCodeType.ERROR);
                return bulkResponseType;
            }

            // Transform web service object to domain object(s)
            final IBulkSubmission bulkSubmission = transformToDomainType (bulkRequestType);

            // Validate domain
            validateDomain (bulkSubmission);

            // Process validated request
            processBulkSubmission (bulkSubmission);

        }
        catch (final AbstractBusinessException be)
        {
            handleBusinessException (be, bulkResponseType.getStatus ());

        }
        // CHECKSTYLE:OFF
        catch (final Exception e)
        // CHECKSTYLE:ON
        {

            handleException (e, bulkResponseType.getStatus ());
        }
        finally
        {
            LOGGER.info ("[submitBulk] completed");
        }

        return bulkResponseType;
    }

    /**
     * Log raw request object.
     * 
     * @param bulkRequest request instance.
     */
    private void logIncomingRequest (final BulkRequestType bulkRequest)
    {
        LOGGER.info ("[logIncomingRequest] - " + bulkRequest);
    }

    /**
     * Process bulk submission instance.
     * 
     * @param bulkSubmission bulk submission instance.
     */
    private void processBulkSubmission (final IBulkSubmission bulkSubmission)
    {
        LOGGER.info ("Service called to persist bulk request details");
    }

    /**
     * Initialise response.
     * 
     * @param bulkRequest request instance.
     * @return created BulkResponse instance.
     */
    private BulkResponseType intialiseResponse (final BulkRequestType bulkRequest)
    {

        LOGGER.debug ("setup initial response");
        final BulkResponseType response = new BulkResponseType ();
        response.setCustomerReference (bulkRequest.getHeader ().getCustomerReference ());
        response.setRequestCount (bulkRequest.getHeader ().getRequestCount ());
        final StatusType status = new StatusType ();
        response.setStatus (status);
        status.setCode (StatusCodeType.OK);

        return response;
    }

    /**
     * Validate to ensure integrity of bulk request.
     * 
     * @param bulkRequestType bulk request
     * @return {@link ErrorType}
     */
    private ErrorType validateWsType (final BulkRequestType bulkRequestType)
    {
        LOGGER.debug ("[validateWsType] started");

        LOGGER.debug ("validate request count");
        final BigInteger headerReqCount = bulkRequestType.getHeader ().getRequestCount ();
        final int actualRequestCount = bulkRequestType.getRequests ().getMcolRequests ().getMcolRequest ().size ();
        if (headerReqCount.intValue () != actualRequestCount)
        {
            return createErrorType (AbstractBusinessException.ErrorCode.REQ_COUNT_MISMATCH,
                    "Number of requests do not match request count in header");
        }

        LOGGER.debug ("validate request type matches request content for each request");
        for (McolRequestType mcolRequest : bulkRequestType.getRequests ().getMcolRequests ().getMcolRequest ())
        {
            if ( !isValidRequestType (mcolRequest))
            {
                return createErrorType (AbstractBusinessException.ErrorCode.REQ_TYPE_INCORRECT,
                        "Request type does not match with specified request details");
            }
        }

        return null;

    }

    /**
     * Validates that request type matches with the request content.
     * 
     * @param mcolRequestType MCOL request type
     * @return true if request type matches content else false
     */
    private boolean isValidRequestType (final McolRequestType mcolRequestType)
    {
        boolean valid = true;
        switch (mcolRequestType.getRequestType ())
        {
            case MCOL_CLAIM:
                valid = (mcolRequestType.getMcolClaim () != null) ? true : false;
                break;

            case MCOL_JUDGMENT:
                valid = (mcolRequestType.getMcolJudgment () != null) ? true : false;
                break;

            case MCOL_WARRANT:
                valid = (mcolRequestType.getMcolWarrant () != null) ? true : false;
                break;

            case MCOL_JUDGMENT_WARRANT:
                valid = (mcolRequestType.getMcolJudgmentWarrant () != null) ? true : false;
                break;

            case MCOL_CLAIM_STATUS_UPDATE:
                valid = (mcolRequestType.getMcolClaimStatusUpdate () != null) ? true : false;
                break;

            default:
                valid = false;
        }

        return valid;

    }

    /**
     * Transform Web service object to Domain object.
     * 
     * @param bulkRequestType bulk request
     * @return {@link IBulkSubmission}
     */
    private IBulkSubmission transformToDomainType (final BulkRequestType bulkRequestType)
    {
        LOGGER.debug ("transform to domain type");

        return BulkRequestToDomainResolver.mapToBulkSubmission (bulkRequestType);

    }

    /**
     * Validate domain object - {@link IBulkSubmission}.
     * 
     * @param bulkSubmission domain instance to validate.
     * @throws AbstractBusinessException in case of any business rule validation failure.
     */
    private void validateDomain (final IBulkSubmission bulkSubmission) throws AbstractBusinessException
    {
        LOGGER.debug ("[validateDomain] started");

        // LOGGER.debug ("validate SDT Customer id");
        //
        // LOGGER.debug ("validate Target application id");
        //
        // LOGGER.debug ("validate customer reference is unique across data retention period");
        //
        // LOGGER.debug ("validate customer reference for each request is unique across data retention period");
        VisitableTreeWalker.walkTree (bulkSubmission, "Validator");

    }

}
