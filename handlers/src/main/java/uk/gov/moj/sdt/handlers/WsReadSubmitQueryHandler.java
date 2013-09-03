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
package uk.gov.moj.sdt.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.ISubmitQueryResponse;
import uk.gov.moj.sdt.handlers.api.IWsReadSubmitQueryHandler;
import uk.gov.moj.sdt.services.api.ISubmitQueryService;
import uk.gov.moj.sdt.transformers.SubmitQueryToDomainTransformer;
import uk.gov.moj.sdt.transformers.api.ITransformer;
import uk.gov.moj.sdt.validators.exception.AbstractBusinessException;
import uk.gov.moj.sdt.visitor.VisitableTreeWalker;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusType;
import uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema.SubmitQueryRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.submitqueryresponseschema.SubmitQueryResponseType;

/**
 * Implementation for handling submit query flow.
 * 
 * @author d130680
 * 
 */
public class WsReadSubmitQueryHandler extends AbstractWsReadHandler implements IWsReadSubmitQueryHandler
{

    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (WsReadSubmitQueryHandler.class);

    /**
     * Submit Query service to return response.
     * 
     */
    private ISubmitQueryService submitQueryService;

    /**
     * The transformer associated with this handler.
     */
    private ITransformer<SubmitQueryRequestType, SubmitQueryResponseType, ISubmitQueryRequest, 
            ISubmitQueryResponse> transformer;

    @Override
    public SubmitQueryResponseType submitQuery (final SubmitQueryRequestType requestType)
    {

        LOGGER.info ("[submitQuery] started");
        // Initialise response
        SubmitQueryResponseType response = new SubmitQueryResponseType ();
        response.setStatus (new StatusType ());

        try
        {

            // Transform to domain object
            final ISubmitQueryRequest submitQueryRequest =
                    SubmitQueryToDomainTransformer.mapToSubmitQueryRequest (requestType);

            // Validate domain
            validateDomain (submitQueryRequest);

            // Process validated request
            response = processSubmitQuery (submitQueryRequest);

        }
        catch (final AbstractBusinessException be)
        {
            handleBusinessException (be, response.getStatus ());
        }
        // CHECKSTYLE:OFF
        catch (final Exception e)
        // CHECKSTYLE:ON
        {
            handleException (e, response.getStatus ());
        }
        finally
        {
            LOGGER.info ("[submitQuery] completed");
        }

        return response;

    }

    /**
     * Validate to ensure integrity of submit query.
     * 
     * @param submitQueryRequest submit query criteria
     * @throws AbstractBusinessException business exception
     */
    private void validateDomain (final ISubmitQueryRequest submitQueryRequest) throws AbstractBusinessException
    {
        LOGGER.debug ("[validateDomain] started");
        VisitableTreeWalker.walk (submitQueryRequest, "Validator");
        LOGGER.debug ("[validateDomain] finished");
    }

    /**
     * Process submit query.
     * 
     * @param request submit query request
     * @return submit query response
     */
    private SubmitQueryResponseType processSubmitQuery (final ISubmitQueryRequest request)
    {
        LOGGER.info ("Service call to submit query");

        final ISubmitQueryResponse domainResponse = submitQueryService.submitQuery (request);

        final SubmitQueryResponseType response =
                SubmitQueryToDomainTransformer.mapToSubmitQueryResponseType (domainResponse);

        // Set the sdt service to show the response was sent from the commissioning poject
        response.setSdtService (AbstractWsHandler.SDT_COMX_SERVICE);

        // Set the status
        final StatusType status = new StatusType ();
        response.setStatus (status);
        status.setCode (StatusCodeType.OK);
        return response;
    }

    /**
     * Set the Submit Query Service.
     * 
     * @param submitQueryService submit query service
     */
    public void setSubmitQueryService (final ISubmitQueryService submitQueryService)
    {
        this.submitQueryService = submitQueryService;
    }

    /**
     * Getter for transformer.
     * 
     * @return the transformer associated with this class.
     */
    public ITransformer<SubmitQueryRequestType, SubmitQueryResponseType, ISubmitQueryRequest, ISubmitQueryResponse>
            getTransformer ()
    {
        return transformer;
    }

    /**
     * Setter for transformer.
     * 
     * @param transformer the transformer to be associated with this class.
     */
    public
            void
            setTransformer (final ITransformer<SubmitQueryRequestType, SubmitQueryResponseType, 
                            ISubmitQueryRequest, ISubmitQueryResponse> transformer)
    {
        this.transformer = transformer;
    }
}
