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
package uk.gov.moj.sdt.transformers;

import java.math.BigInteger;

import uk.gov.moj.sdt.domain.SubmitQueryRequest;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.ISubmitQueryResponse;
import uk.gov.moj.sdt.transformers.api.ITransformer;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusType;
import uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema.SubmitQueryRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.submitqueryresponseschema.ResultsType;
import uk.gov.moj.sdt.ws._2013.sdt.submitqueryresponseschema.SubmitQueryResponseType;

/**
 * Maps bulk request JAXB object tree to domain object tree and vice versa.
 * 
 * @author d130680
 * 
 */
public final class SubmitQueryTransformer extends AbstractTransformer implements
        ITransformer<SubmitQueryRequestType, SubmitQueryResponseType, ISubmitQueryRequest, ISubmitQueryResponse>
{

    /**
     * Private constructor.
     */
    private SubmitQueryTransformer ()
    {

    }

    @Override
    public ISubmitQueryRequest transformJaxbToDomain (final SubmitQueryRequestType submitQueryRequestType)
    {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest ();
        final HeaderType header = submitQueryRequestType.getHeader ();

        submitQueryRequest.setSdtCustomerId (header.getSdtCustomerId ());

        // Map the target application
        final TargetApplication targetApplication = new TargetApplication ();
        targetApplication.setTargetApplicationCode (header.getTargetApplicationId ());
        submitQueryRequest.setTargetApplication (targetApplication);

        return submitQueryRequest;
    }

    @Override
    public SubmitQueryResponseType transformDomainToJaxb (final ISubmitQueryResponse submitQueryResponse)
    {
        final SubmitQueryResponseType submitQueryResponseType = new SubmitQueryResponseType ();

        // Maps some values.
        submitQueryResponseType.setSdtCustomerId (submitQueryResponse.getSdtCustomerId ());
        submitQueryResponseType.setResultCount (BigInteger.valueOf (submitQueryResponse.getResultCount ()));

        // Set dummy results so the tags we need are written.
        final ResultsType resultsType = new ResultsType ();
        submitQueryResponseType.setResults (resultsType);

        // Set the sdt service to show the response was sent from the commissioning poject
        submitQueryResponseType.setSdtService (AbstractTransformer.SDT_SERVICE);

        // Set the status
        final StatusType status = new StatusType ();
        submitQueryResponseType.setStatus (status);
        status.setCode (StatusCodeType.OK);

        return submitQueryResponseType;
    }
}
