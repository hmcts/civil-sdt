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

package uk.gov.moj.sdt.producer.comx.sdtws;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.producers.api.IWsCreateBulkRequestHandler;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.CreateStatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.CreateStatusType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.ErrorType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.RequestTypeType;
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
        targetNamespace = "http://ws.sdt.moj.gov.uk/2013/sdt/SdtEndpoint", wsdlLocation = "wsdl/SdtEndpoint.wsdl",
        endpointInterface = "uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType")
public class SdtEndpointPortType implements ISdtEndpointPortType
{

    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (SdtEndpointPortType.class);

    /**
     * Handles bulk submission request.
     */
    private IWsCreateBulkRequestHandler wsCreateBulkRequestHandler;

    @Override
    public BulkFeedbackResponseType getBulkFeedback (final BulkFeedbackRequestType bulkFeedbackRequest)
    {
        LOGGER.debug (this.getClass ().getName () + " endpoint called, getBulkFeedback=" +
                bulkFeedbackRequest.getHeader ().getSdtCustomerId ());

        final BulkFeedbackResponseType response = new BulkFeedbackResponseType ();
        final BulkRequestStatusType bulkStatus = new BulkRequestStatusType ();

        final Integer requestCount = 12;
        bulkStatus.setRequestCount (BigInteger.valueOf (requestCount));
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

        mcolResponses.getMcolResponse ().add (getResponseAcceptedClaim ());
        mcolResponses.getMcolResponse ().add (getResponseAcceptedJudgment ());
        mcolResponses.getMcolResponse ().add (getResponseAcceptJudgmentForth ());
        mcolResponses.getMcolResponse ().add (getResponseAcceptedWarrant ());
        mcolResponses.getMcolResponse ().add (getResponseAcceptedPaid ());

        mcolResponses.getMcolResponse ().add (getResponseInitAcceptedClaim ());
        mcolResponses.getMcolResponse ().add (getResponseInitAcceptedJudgm ());
        mcolResponses.getMcolResponse ().add (getResponseInitAcceptJudgForth ());
        mcolResponses.getMcolResponse ().add (getResponseInitAcceptedWarrant ());
        mcolResponses.getMcolResponse ().add (getResponseInitAcceptedPaid ());
        mcolResponses.getMcolResponse ().add (getResponseRejectedClaim ());
        mcolResponses.getMcolResponse ().add (getResponseErrorClaim ());

        responses.setMcolResponses (mcolResponses);

        return responses;
    }

    /**
     * Method to generate the response for an Erroneous claim.
     * 
     * @return the Erroneous claim.
     */
    private McolResponseType getResponseErrorClaim ()
    {
        // Instantiate a new response
        final McolResponseType response = new McolResponseType ();

        // Required Fields.
        response.setRequestId ("12");
        response.setRequestType (RequestTypeType.MCOL_CLAIM);
        response.setClaimNumber ("11111112");

        final Calendar cal = new GregorianCalendar ();
        response.setIssueDate (cal);

        final CreateStatusType status = new CreateStatusType ();
        status.setCode (CreateStatusCodeType.ERROR);
        response.setStatus (status);

        // Required for erroneous cases
        final ErrorType errorType = new ErrorType ();

        errorType.setCode ("The ERROR CODE");
        errorType.setDescription ("The description of the error.");
        status.setError (errorType);

        return response;
    }

    /**
     * Method to generate the response for a Reject Claim.
     * 
     * @return the Reject Claim.
     */
    private McolResponseType getResponseRejectedClaim ()
    {
        // Instantiate a new response
        final McolResponseType response = new McolResponseType ();

        // Required Fields.
        response.setRequestId ("11");
        response.setRequestType (RequestTypeType.MCOL_CLAIM);
        response.setClaimNumber ("11111111");
        final Calendar cal = new GregorianCalendar ();
        response.setIssueDate (cal);

        final CreateStatusType status = new CreateStatusType ();
        status.setCode (CreateStatusCodeType.REJECTED);
        response.setStatus (status);

        // Required for rejected cases
        final ErrorType errorType = new ErrorType ();

        errorType.setCode ("Rejected CODE");
        errorType.setDescription ("The reason why the claim was rejected.");
        status.setError (errorType);

        return response;

    }

    /* Initially Accepted */
    /**
     * Method to generate the response for an Initially Accepted Paid.
     * 
     * @return the Accepted Paid.
     */
    private McolResponseType getResponseInitAcceptedPaid ()
    {
        // Instantiate a new response
        final McolResponseType response = new McolResponseType ();

        // Required Fields.
        response.setRequestId ("10");
        response.setRequestType (RequestTypeType.MCOL_PAID);
        response.setClaimNumber ("11111110");
        final Calendar cal = new GregorianCalendar ();
        response.setIssueDate (cal);

        final CreateStatusType status = new CreateStatusType ();
        status.setCode (CreateStatusCodeType.INITIALLY_ACCEPTED);
        response.setStatus (status);

        // Required for accepted cases
        response.setServiceDate (cal);
        return response;

    }

    /**
     * Method to generate the response for an Initially Accepted Warrant.
     * 
     * @return the Accepted Warrant.
     */
    private McolResponseType getResponseInitAcceptedWarrant ()
    {
        // Instantiate a new response
        final McolResponseType response = new McolResponseType ();

        // Required Fields.
        response.setRequestId ("9");
        response.setRequestType (RequestTypeType.MCOL_WARRANT);
        response.setClaimNumber ("11111109");
        final Calendar cal = new GregorianCalendar ();
        response.setIssueDate (cal);

        final CreateStatusType status = new CreateStatusType ();
        status.setCode (CreateStatusCodeType.INITIALLY_ACCEPTED);
        response.setStatus (status);

        // Required for accepted cases
        response.setServiceDate (cal);

        // Required for accepted Warrants
        response.setWarrantNumber ("22222202");
        final Integer courtCode = 942;
        response.setEnforcingCourtCode (BigInteger.valueOf (courtCode));
        response.setEnforcingCourtName ("CourtNameB");
        final Integer fee = 258;
        response.setFee (fee);
        return response;

    }

    /**
     * Method to generate the response for an Initially Accepted JudgmentForthwith.
     * 
     * @return the Accepted JudgmentForthwith.
     */
    private McolResponseType getResponseInitAcceptJudgForth ()
    {
        // Instantiate a new response
        final McolResponseType response = new McolResponseType ();

        // Required Fields.
        response.setRequestId ("8");
        response.setRequestType (RequestTypeType.MCOL_JUDGMENT_FORTHWITH);
        response.setClaimNumber ("11111108");
        final Calendar cal = new GregorianCalendar ();
        response.setIssueDate (cal);

        final CreateStatusType status = new CreateStatusType ();
        status.setCode (CreateStatusCodeType.INITIALLY_ACCEPTED);
        response.setStatus (status);

        // Required for accepted cases
        response.setServiceDate (cal);
        return response;

    }

    /**
     * Method to generate the response for an Initially Accepted Judgment.
     * 
     * @return the Accepted Judgment.
     */
    private McolResponseType getResponseInitAcceptedJudgm ()
    {
        // Instantiate a new response
        final McolResponseType response = new McolResponseType ();

        // Required Fields.
        response.setRequestId ("7");
        response.setRequestType (RequestTypeType.MCOL_JUDGMENT);
        response.setClaimNumber ("11111107");
        final Calendar cal = new GregorianCalendar ();
        response.setIssueDate (cal);

        final CreateStatusType status = new CreateStatusType ();
        status.setCode (CreateStatusCodeType.INITIALLY_ACCEPTED);
        response.setStatus (status);

        // Required for accepted cases
        response.setServiceDate (cal);
        return response;

    }

    /**
     * Method to generate the response for an Initially Accepted Claim.
     * 
     * @return the Accepted Claim.
     */
    private McolResponseType getResponseInitAcceptedClaim ()
    {
        // Instantiate a new response
        final McolResponseType response = new McolResponseType ();

        // Required Fields.
        response.setRequestId ("6");
        response.setRequestType (RequestTypeType.MCOL_CLAIM);
        response.setClaimNumber ("11111106");
        final Calendar cal = new GregorianCalendar ();
        response.setIssueDate (cal);

        final CreateStatusType status = new CreateStatusType ();
        status.setCode (CreateStatusCodeType.INITIALLY_ACCEPTED);
        response.setStatus (status);

        // Required for accepted cases
        response.setServiceDate (cal);

        // Required for Claims
        final Integer fee = 500;
        response.setFee (fee);
        return response;
    }

    /* Accepted */

    /**
     * Method to generate the response for an Accepted Paid.
     * 
     * @return the Accepted Paid.
     */
    private McolResponseType getResponseAcceptedPaid ()
    {
        // Instantiate a new response
        final McolResponseType response = new McolResponseType ();

        // Required Fields.
        response.setRequestId ("5");
        response.setRequestType (RequestTypeType.MCOL_PAID);
        response.setClaimNumber ("11111105");
        final Calendar cal = new GregorianCalendar ();
        response.setIssueDate (cal);

        final CreateStatusType status = new CreateStatusType ();
        status.setCode (CreateStatusCodeType.ACCEPTED);
        response.setStatus (status);

        // Required for accepted cases
        response.setServiceDate (cal);
        return response;

    }

    /**
     * Method to generate the response for an Accepted Warrant.
     * 
     * @return the Accepted Warrant.
     */
    private McolResponseType getResponseAcceptedWarrant ()
    {
        // Instantiate a new response
        final McolResponseType response = new McolResponseType ();

        // Required Fields.
        response.setRequestId ("4");
        response.setRequestType (RequestTypeType.MCOL_WARRANT);
        response.setClaimNumber ("11111104");
        final Calendar cal = new GregorianCalendar ();
        response.setIssueDate (cal);

        final CreateStatusType status = new CreateStatusType ();
        status.setCode (CreateStatusCodeType.ACCEPTED);
        response.setStatus (status);

        // Required for accepted cases
        response.setServiceDate (cal);

        // Required for accepted Warrants
        response.setWarrantNumber ("22222201");
        final Integer couretCode = 517;
        response.setEnforcingCourtCode (BigInteger.valueOf (couretCode));
        response.setEnforcingCourtName ("CourtNameA");
        final Integer fee = 258;
        response.setFee (fee);
        return response;

    }

    /**
     * Method to generate the response for an Accepted JudgmentForthwith.
     * 
     * @return the Accepted JudgmentForthwith.
     */
    private McolResponseType getResponseAcceptJudgmentForth ()
    {
        // Instantiate a new response
        final McolResponseType response = new McolResponseType ();

        // Required Fields.
        response.setRequestId ("3");
        response.setRequestType (RequestTypeType.MCOL_JUDGMENT_FORTHWITH);
        response.setClaimNumber ("11111103");
        final Calendar cal = new GregorianCalendar ();
        response.setIssueDate (cal);

        final CreateStatusType status = new CreateStatusType ();
        status.setCode (CreateStatusCodeType.ACCEPTED);
        response.setStatus (status);

        // Required for accepted cases
        response.setServiceDate (cal);
        return response;

    }

    /**
     * Method to generate the response for an Accepted Judgment.
     * 
     * @return the Accepted Judgment.
     */
    private McolResponseType getResponseAcceptedJudgment ()
    {
        // Instantiate a new response
        final McolResponseType response = new McolResponseType ();

        // Required Fields.
        response.setRequestId ("2");
        response.setRequestType (RequestTypeType.MCOL_JUDGMENT);
        response.setClaimNumber ("11111102");
        final Calendar cal = new GregorianCalendar ();
        response.setIssueDate (cal);

        final CreateStatusType status = new CreateStatusType ();
        status.setCode (CreateStatusCodeType.ACCEPTED);
        response.setStatus (status);

        // Required for accepted cases
        response.setServiceDate (cal);
        return response;

    }

    /**
     * Method to generate the response for an Accepted Claim.
     * 
     * @return the Accepted Claim.
     */
    private McolResponseType getResponseAcceptedClaim ()
    {
        // Instantiate a new response
        final McolResponseType response = new McolResponseType ();

        // Required Fields.
        response.setRequestId ("1");
        response.setRequestType (RequestTypeType.MCOL_CLAIM);
        response.setClaimNumber ("11111101");
        final Calendar cal = new GregorianCalendar ();
        response.setIssueDate (cal);

        final CreateStatusType status = new CreateStatusType ();
        status.setCode (CreateStatusCodeType.ACCEPTED);
        response.setStatus (status);

        // Required for accepted cases
        response.setServiceDate (cal);

        // Required for Claims
        final Integer fee = 500;
        response.setFee (fee);
        return response;
    }

    @Override
    public BulkResponseType submitBulk (final BulkRequestType bulkRequest)
    {
        LOGGER.debug (this.getClass ().getName () + " endpoint called, submitBulk=" +
                bulkRequest.getHeader ().getSdtCustomerId ());

        final BulkResponseType response = wsCreateBulkRequestHandler.submitBulk (bulkRequest);
        return response;
    }

    @Override
    public DefenceResponseType getDefenceDetails (final DefenceRequestType defenceRequest)
    {
        LOGGER.debug (this.getClass ().getName () + " endpoint called, getDefenceDetails=" +
                defenceRequest.getHeader ().getSdtCustomerId ());

        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param wsCreateBulkRequestHandler the wsCreateBulkRequestHandler to set
     */
    public void setWsCreateBulkRequestHandler (final IWsCreateBulkRequestHandler wsCreateBulkRequestHandler)
    {
        this.wsCreateBulkRequestHandler = wsCreateBulkRequestHandler;
    }
}
