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

import uk.gov.moj.sdt.producer.comx.sdtws.BulkResponseFailure;
import uk.gov.moj.sdt.producer.comx.sdtws.IResponseFactory;
import uk.gov.moj.sdt.producers.api.AbstractWsCreateHandler;
import uk.gov.moj.sdt.producers.api.IWsCreateBulkRequestHandler;
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

    /**
     * Factory to produce mock responses.
     */
    private IResponseFactory responseFactory;

    @Override
    public BulkResponseType submitBulk (final BulkRequestType bulkRequest)
    {
        LOGGER.info ("[submitBulk] started");

        final BulkResponseType response = validate (bulkRequest);

        LOGGER.info ("[submitBulk] completed");

        return response;
    }

    /**
     * Validate bulk request type.
     * 
     * @param request bulk request
     * @return {@link BulkResponseType}
     */
    private BulkResponseType validate (final BulkRequestType request)
    {
        LOGGER.debug ("[validate] started");

        LOGGER.debug ("validate request count");
        final BigInteger headerReqCount = request.getHeader ().getRequestCount ();
        final int actualRequestCount = request.getRequests ().getMcolRequests ().getMcolRequest ().size ();
        if (headerReqCount.intValue () != actualRequestCount)
        {
            return responseFactory.createFailResponse (BulkResponseFailure.REQUEST_COUNT_MISMATCH, request);
        }

        LOGGER.debug ("validate SDT Customer id");
        if ("TEST_100".equalsIgnoreCase (request.getHeader ().getCustomerReference ()))
        {
            return responseFactory.createFailResponse (BulkResponseFailure.SDT_CUSTOMER_NOT_FOUND, request);
        }

        LOGGER.debug ("validate Target application id");
        if ("TEST_300".equalsIgnoreCase (request.getHeader ().getCustomerReference ()))
        {
            return responseFactory.createFailResponse (BulkResponseFailure.INVALID_TARGET_APPLICATION, request);
        }

        LOGGER.debug ("validate customer reference is unique");
        if ("TEST_200".equalsIgnoreCase (request.getHeader ().getCustomerReference ()))
        {
            return responseFactory.createFailResponse (BulkResponseFailure.BULK_CUSTOMER_REFERENCE_NOT_UNIQUE, request);
        }

        LOGGER.debug ("validate request type matches request content for each request");
        for(McolRequestType mcolRequest : request.getRequests ().getMcolRequests ().getMcolRequest ()) {
            if ( !isValidRequestType (mcolRequest))
            {
                return responseFactory.createFailResponse (BulkResponseFailure.INVALID_REQUEST_TYPE, request);
            }
        }

        LOGGER.debug ("validate customer reference for each request");
        if ("TEST_600".equalsIgnoreCase (request.getHeader ().getCustomerReference ()))
        {
            return responseFactory.createFailResponse (BulkResponseFailure.IND_REQUEST_CUSTOMER_REFERENCE_NOT_UNIQUE,
                    request);
        }

        return responseFactory.createSuccessResponse (request);

    }

    /**
     * Validates that request type matches with the request content.
     * 
     * @param mcolRequestType MCOL request type
     * @return true if request type matches content else false
     */
    private boolean isValidRequestType (final McolRequestType mcolRequestType)
    {
        boolean valid = false;
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

            case MCOL_JUDGMENT_FORTHWITH:
                valid = (mcolRequestType.getMcolJudgmentForthwith () != null) ? true : false;
                break;

            case MCOL_CLAIM_UPDATE:
                valid = (mcolRequestType.getMcolClaimUpdate () != null) ? true : false;
                break;

            default:
                valid = false;
        }

        return valid;

    }

    /**
     * @param responseFactory the responseFactory to set
     */
    public void setResponseFactory (final IResponseFactory responseFactory)
    {
        this.responseFactory = responseFactory;
    }
}
