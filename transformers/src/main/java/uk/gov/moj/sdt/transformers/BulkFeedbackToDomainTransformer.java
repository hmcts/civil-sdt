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
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.domain.BulkFeedbackRequest;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IBulkFeedbackRequest;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IRequestType;
import uk.gov.moj.sdt.transformers.api.ITransformer;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.BulkStatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.BulkStatusType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.ErrorType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.IndividualStatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.IndividualStatusType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.RequestTypeType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackrequestschema.BulkFeedbackRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackrequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema.BulkFeedbackResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema.BulkRequestStatusType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema.McolResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema.McolResponsesType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema.ResponsesType;

/**
 * Maps bulk feedback JAXB object tree to domain object tree and vice versa.
 * 
 * @author d130680
 * 
 */
public final class BulkFeedbackToDomainTransformer extends AbstractTransformer implements
        ITransformer<BulkFeedbackRequestType, BulkFeedbackResponseType, IBulkFeedbackRequest, IBulkSubmission>
{
    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (BulkRequestToDomainTransformer.class);

    /**
     * Private constructor.
     */
    private BulkFeedbackToDomainTransformer ()
    {
    }

    @Override
    public IBulkFeedbackRequest transformJaxbToDomain (final BulkFeedbackRequestType bulkFeedbackRequest)
    {
        // Grab the request details from the header
        final HeaderType header = bulkFeedbackRequest.getHeader ();
        final IBulkFeedbackRequest bulkFeedback = new BulkFeedbackRequest ();

        bulkFeedback.setSdtBulkReference (header.getSdtBulkReference ());
        bulkFeedback.setSdtCustomerId (header.getSdtCustomerId ().toString ());

        return bulkFeedback;
    }

    @Override
    public BulkFeedbackResponseType transformDomainToJaxb (final IBulkSubmission bulkSubmission)
    {
        // Create target JAXB response object.
        final BulkFeedbackResponseType bulkFeedbackResponseType = new BulkFeedbackResponseType ();

        // Create target status JAXB object.
        final BulkRequestStatusType bulkRequestStatusType = new BulkRequestStatusType ();

        // Set bulk status information
        bulkRequestStatusType.setRequestCount (BigInteger.valueOf (bulkSubmission.getNumberOfRequest ()));
        bulkRequestStatusType.setSdtBulkReference (bulkSubmission.getSdtBulkReference ());
        bulkRequestStatusType.setSdtService (AbstractTransformer.SDT_COMX_SERVICE);
        bulkRequestStatusType.setCustomerReference (bulkSubmission.getCustomerReference ());
        final Calendar createdDate = Calendar.getInstance ();
        createdDate.setTime (bulkSubmission.getCreatedDate ().toDate ());
        bulkRequestStatusType.setSubmittedDate (createdDate);

        // Set bulk status type
        final BulkStatusType bulkStatusType = new BulkStatusType ();
        bulkStatusType.setCode (BulkStatusCodeType.fromValue (bulkSubmission.getSubmissionStatus ()));
        bulkRequestStatusType.setBulkStatus (bulkStatusType);

        bulkFeedbackResponseType.setBulkRequestStatus (bulkRequestStatusType);

        // Instantiate a new response
        final ResponsesType responsesType = new ResponsesType ();
        final McolResponsesType mcolResponses = new McolResponsesType ();

        // Map individual request domain object(s) to the response(s)
        final List<IndividualRequest> individualRequestList = bulkSubmission.getIndividualRequests ();

        McolResponseType mcolResponseType = null;
        for (IndividualRequest individualRequest : individualRequestList)
        {
            mcolResponseType = new McolResponseType ();

            // Set the customer request reference
            mcolResponseType.setRequestId (individualRequest.getCustomerRequestReference ());

            // Set the request type, e.g. mcolClaim, mcolJudgment etc.
            final IRequestType requestType = individualRequest.getRequestType ();
            mcolResponseType.setRequestType (RequestTypeType.fromValue (requestType.getName ()));

            // Set the individual request type
            final IndividualStatusType status = new IndividualStatusType ();
            status.setCode (IndividualStatusCodeType.fromValue (individualRequest.getRequestStatus ()));
            mcolResponseType.setStatus (status);

            mcolResponses.getMcolResponse ().add (mcolResponseType);

            // Set errors if any
            final IErrorLog errorLog = individualRequest.getErrorLog ();
            if (errorLog != null)
            {
                // Required for rejected cases
                final ErrorType errorType = new ErrorType ();
                errorType.setCode (errorLog.getErrorMessage ().getErrorCode ());
                errorType.setDescription (errorLog.getErrorMessage ().getErrorText ());
                status.setError (errorType);
            }
        }

        // Set the individual request responses
        responsesType.setMcolResponses (mcolResponses);
        bulkFeedbackResponseType.setResponses (responsesType);

        return bulkFeedbackResponseType;
    }
}
