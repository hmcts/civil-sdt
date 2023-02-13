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
package uk.gov.moj.sdt.producers.comx.services;

import java.util.Map;

import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.domain.SubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.services.api.ISubmitQueryService;
import uk.gov.moj.sdt.utils.SdtContext;

/**
 * Implementation for mocking of SDT Submit Query service.
 * This class will provide a static list of responses for submit query.
 *
 * @author d130680
 *
 */
@Service("MockSubmitQueryService")
public class MockSubmitQueryService implements ISubmitQueryService
{

    /**
     * Map of the response content by the criteria type as key.
     */
    private Map<String, SubmitQueryRequest> responseContentMap;

    @Override
    public void submitQuery (final ISubmitQueryRequest request)
    {

        final String queryReference = request.getQueryReference ();

        SubmitQueryRequest submitQueryRequest = this.getResponseContentMap ().get (queryReference);

        if (submitQueryRequest == null)
        {
            // If the query reference is not one of the recognised type, then
            // get the response for the default query reference.
            submitQueryRequest = this.getResponseContentMap ().get ("MCOLDefence1");
        }

        request.setResultCount (submitQueryRequest.getResultCount ());
        request.setStatus (submitQueryRequest.getStatus ());
        request.reject (submitQueryRequest.getErrorLog ());
        request.setTargetApplicationResponse (submitQueryRequest.getTargetApplicationResponse ());

        // Write the result xml to threadlocal so the outbound interceptor can pick it up
        writeToThreadLocal (request.getTargetApplicationResponse ());

    }

    /**
     * Write the result to thread local - the result is obtained from the
     * responseContentMap.
     *
     * @param targetAppResponse the target application response
     */
    private void writeToThreadLocal (final String targetAppResponse)
    {

        if (targetAppResponse != null)
        {

            SdtContext.getContext ().setRawOutXml (targetAppResponse);
        }
    }

    /**
     * This method will return the map of the SubmitQuery with the key as
     * the Query Reference.
     *
     * @return the response content map with the key as the criteria type
     */
    public Map<String, SubmitQueryRequest> getResponseContentMap ()
    {
        return responseContentMap;
    }

    /**
     * Setter for the response content map.
     *
     * @param responseContentMap the response content map.
     */
    public void setResponseContentMap (final Map<String, SubmitQueryRequest> responseContentMap)
    {
        this.responseContentMap = responseContentMap;
    }
}
