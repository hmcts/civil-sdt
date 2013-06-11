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

package uk.gov.moj.sdt.producers.sdtws;

import java.math.BigInteger;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.ws._2013.sdt.baseschema.CreateStatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.CreateStatusType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackrequestschema.BulkFeedbackRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema.BulkFeedbackResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema.BulkRequestStatusType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema.McolResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema.McolResponsesType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema.ResponsesType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.BulkRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkresponseschema.BulkResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.defencefeedbackrequestschema.DefenceRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.defencefeedbackresponseschema.DefenceResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType;

/**
 * Implementation of {@link ISdtEndpointPortType}.
 * 
 * @author Saurabh Agarwal
 */

@WebService (serviceName = "SdtEndpoint", portName = "SdtEndpointPort",
 targetNamespace = "http://ws.sdt.moj.gov.uk/2013/sdt/SdtEndpoint", 
 wsdlLocation = "wsdl/SdtEndpoint.wsdl",
 endpointInterface = "uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType")
public class SdtEndpointPortType implements ISdtEndpointPortType
{
    /**
     * Logger for this class.
     */
    private static final Log LOGGER = LogFactory.getLog (SdtEndpointPortType.class);

    @Override
    public BulkFeedbackResponseType getBulkFeedback (final BulkFeedbackRequestType bulkFeedbackRequest)
    {
        LOGGER.debug (this.getClass ().getName () + " endpoint called, getBulkFeedback=" +
                bulkFeedbackRequest.getHeader ().getSdtCustomerId ());

        final BulkFeedbackResponseType response = new BulkFeedbackResponseType ();
        final BulkRequestStatusType bulkStatus = new BulkRequestStatusType ();
        bulkStatus.setRequestCount (BigInteger.ONE);
        bulkStatus.setSdtBulkReference ("sdtreference");
        response.setBulkRequestStatus (bulkStatus);
        response.setResponses (getResponses ());
        return response;
    }

    /**
     * Dummy impl.
     * 
     * @return responses
     */
    private ResponsesType getResponses ()
    {
        final ResponsesType responses = new ResponsesType ();
        final McolResponsesType mcolResponses = new McolResponsesType ();
        final McolResponseType r1 = new McolResponseType ();
        r1.setClaimNumber ("claim123");
        final CreateStatusType status = new CreateStatusType ();
        status.setCode (CreateStatusCodeType.ACCEPTED);
        r1.setStatus (status);
        mcolResponses.getMcolResponse ().add (r1);
        responses.setMcolResponses (mcolResponses);

        return responses;
    }

    @Override
    public BulkResponseType submitBulk (final BulkRequestType bulkRequest)
    {
        LOGGER.debug (this.getClass ().getName () + " endpoint called, submitBulk=" +
                bulkRequest.getHeader ().getSdtCustomerId ());

        final BulkResponseType response = new BulkResponseType ();
        response.setCustomerReference (bulkRequest.getHeader ().getCustomerReference ());
        response.setRequestCount (bulkRequest.getHeader ().getRequestCount ());
        response.setSdtBulkReference ("sdtreference");
        final StatusType status = new StatusType ();
        status.setCode (StatusCodeType.OK);
        response.setStatus (status);
        return response;
    }

    @Override
    public DefenceResponseType getDefenceDetails (final DefenceRequestType defenceRequest)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
