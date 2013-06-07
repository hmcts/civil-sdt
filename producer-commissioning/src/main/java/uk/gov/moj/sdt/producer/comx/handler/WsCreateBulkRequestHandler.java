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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.producers.api.AbstractWsCreateHandler;
import uk.gov.moj.sdt.producers.api.IWsCreateBulkRequestHandler;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.ErrorType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusCode;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.BulkRequestType;
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
    public BulkResponseType submitBulk (final BulkRequestType bulkRequest)
    {
        LOGGER.info ("[submitBulk] started");

        final BulkResponseType response = new BulkResponseType ();
        response.setCustomerReference (bulkRequest.getHeader ().getCustomerReference ());
        response.setRequestCount (bulkRequest.getHeader ().getRequestCount ());
        response.setSdtBulkReference ("sdtreference");
        final StatusType status = new StatusType ();
        response.setStatus (status);
        status.setCode (StatusCode.OK);

        transformToDomainType (bulkRequest);

        final ErrorType error = validate (bulkRequest);

        if (error != null)
        {
            status.setCode (StatusCode.OK);
            status.setError (error);
        }

        LOGGER.info ("[submitBulk] completed");

        return response;
    }

    /**
     * Transform Web service object to Domain object.
     * 
     * @param bulkRequest bulk request
     */
    private void transformToDomainType (final BulkRequestType bulkRequest)
    {
        // TODO Auto-generated method stub
        LOGGER.info ("transform to domain type");

    }

    /**
     * Validate bulk request.
     * 
     * @param request bulk request
     * @return {@link ErrorType}
     */
    private ErrorType validate (final BulkRequestType request)
    {
        LOGGER.info ("[validate] started");

        LOGGER.info ("validate field combinations");

        LOGGER.info ("validate SDT Customer id");

        LOGGER.info ("validate Target application id");

        LOGGER.info ("validate request count");

        LOGGER.info ("validate customer reference is unique");

        LOGGER.info ("validate request type for each request");

        LOGGER.info ("validate customer reference for each request");

        return null;
    }

}
