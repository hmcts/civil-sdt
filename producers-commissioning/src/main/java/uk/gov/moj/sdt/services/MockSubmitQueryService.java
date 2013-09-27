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

import uk.gov.moj.sdt.domain.SubmitQueryResponse;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.ISubmitQueryResponse;
import uk.gov.moj.sdt.services.api.ISubmitQueryService;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusCodeType;

/**
 * Implementation for mocking of SDT Submit Query service.
 * This class will provide a static list of responses for submit query.
 * 
 * @author d130680
 * 
 */
public class MockSubmitQueryService implements ISubmitQueryService
{

    /**
     * Mock service will return a static response.
     * 
     * @param request request going to MCOL
     * @return response from MCOL converted to domain
     */
    @Override
    public ISubmitQueryResponse submitQuery (final ISubmitQueryRequest request)
    {

        final ISubmitQueryResponse response = new SubmitQueryResponse ();

        response.setBulkCustomer (request.getBulkCustomer ());
        // CHECKSTYLE:OFF
        response.setResultCount (3);
        // CHECKSTYLE:ON

        response.setStatus (StatusCodeType.OK.value ());
        response.setErrorMessage (null);

        // Write the result xml to threadlocal so the outbound interceptor can pick it up
        writeToThreadLocal ();

        return response;
    }

    /**
     * Write the static result to thread local.
     */
    private void writeToThreadLocal ()
    {

        final String result =
                "<ns3:mcolDefenceDetail><ns3:claimNumber>12345678</ns3:claimNumber>"
                        + "<ns3:defendant defendantId=\"1\"><ns3:filedDate>2001-12-31T12:00:00</ns3:filedDate>"
                        + "<ns3:responseType>PA</ns3:responseType></ns3:defendant></ns3:mcolDefenceDetail>"
                        + "<ns3:mcolDefenceDetail><ns3:claimNumber>12345678</ns3:claimNumber>"
                        + "<ns3:defendant defendantId=\"2\"><ns3:filedDate>2001-12-31T12:00:00</ns3:filedDate>"
                        + "<ns3:responseType>PA</ns3:responseType></ns3:defendant></ns3:mcolDefenceDetail>"
                        + "<ns3:mcolDefenceDetail>"
                        + "<ns3:claimNumber>22345678</ns3:claimNumber><ns3:defendant defendantId=\"1\">"
                        + "<ns3:filedDate>2001-12-31T12:00:00</ns3:filedDate><ns3:responseType>PA</ns3:responseType>"
                        + "</ns3:defendant></ns3:mcolDefenceDetail>";

        SdtContext.getContext ().setRawOutXml (result);
    }
}
