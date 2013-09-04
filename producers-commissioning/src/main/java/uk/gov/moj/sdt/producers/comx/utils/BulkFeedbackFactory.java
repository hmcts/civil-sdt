/* Copyrights and Licenses
 * 
 * Copyright (c) 2012-2014 by the Ministry of Justice. All rights reserved.
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

package uk.gov.moj.sdt.producers.comx.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.ErrorMessage;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.RequestType;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.utils.SdtContext;

/**
 * Custom class used to generate bulk submissions and individual requests.
 * The class is defined as bean and the sample data is also read from the spring XML.
 * 
 * @author d130680
 * 
 */
public class BulkFeedbackFactory
{

    /**
     * String index for customer reference.
     */
    private static final int CUSTOMER_REFERENCE = 0;

    /**
     * String index for request type.
     */
    private static final int REQUEST_TYPE = 1;

    /**
     * String index for request status.
     */
    private static final int REQUEST_STATUS = 2;

    /**
     * String index for rejection reason code.
     */
    private static final int REJECTION_CODE = 3;

    /**
     * String index for rejection reason description.
     */
    private static final int REJECTION_DESCRIPTION = 4;

    /**
     * String index for target response.
     */
    private static final int TARGET_RESPONSE = 5;

    /**
     * The bulk submission.
     */
    private IBulkSubmission bulkSubmission;

    /**
     * The system specific target response defined in the spring context xml.
     */
    private String targetResponse;

    /**
     * Map to store whether the individual request should have the target response injected or not.
     */
    private Map<String, String> targetResponseMap = new HashMap<String, String> ();

    /**
     * Constructor.
     * 
     * @param bulkSubmission bulk submission
     */
    public BulkFeedbackFactory (final IBulkSubmission bulkSubmission)
    {
        this.bulkSubmission = bulkSubmission;
        // Initialise the individual requests
        bulkSubmission.setIndividualRequests (new ArrayList<IndividualRequest> ());
    }

    /**
     * Create an individual request and add it the list of individual requests.
     * 
     * @param customerRequestReference customer request reference from individual request domain object
     * @param requestType request type from individual request domain object
     * @param requestStatus request status from individual request domain object
     * @param rejectionReasonCode error code from error message domain object
     * @param rejectionReasonDescription error text from error message domain object
     * @param hasTargetResponse true is a target response should be populated
     */
    public void createIndividualRequest (final String customerRequestReference, final String requestType,
                                         final String requestStatus, final String rejectionReasonCode,
                                         final String rejectionReasonDescription, final boolean hasTargetResponse)
    {

        // Create the individual request
        final IndividualRequest individualRequest = new IndividualRequest ();
        individualRequest.setCustomerRequestReference (customerRequestReference);
        individualRequest.setRequestStatus (requestStatus);

        // Create the request type
        final RequestType type = new RequestType ();
        type.setName (requestType);
        individualRequest.setRequestType (type);

        if (StringUtils.isNotBlank (rejectionReasonCode) && StringUtils.isNotBlank (rejectionReasonDescription))
        {
            // Create the error and associate with the individual request
            final ErrorLog errorLog = new ErrorLog ();
            final ErrorMessage errorMessage = new ErrorMessage ();
            errorMessage.setErrorCode (rejectionReasonCode);
            errorMessage.setErrorText (rejectionReasonDescription);
            errorLog.setErrorMessage (errorMessage);
            individualRequest.setErrorLog (errorLog);
        }

        // In commissioning add the static response to the map for the outbound interceptor to use
        targetResponseMap.put (customerRequestReference, targetResponse);

        // Add to the list
        bulkSubmission.getIndividualRequests ().add (individualRequest);
    }

    /**
     * Create a list of individual request and add it the list.
     * This method is called from the spring configuration files with the list of params passed in.
     * 
     * 
     * @param params list of strings containing the information we need
     * 
     */
    public void createIndividualRequest (final List<List<String>> params)
    {

        for (List<String> individualRequestList : params)
        {

            // Get the strings based of the index
            final String customerRequestReference = individualRequestList.get (CUSTOMER_REFERENCE);
            final String requestType = individualRequestList.get (REQUEST_TYPE);
            final String requestStatus = individualRequestList.get (REQUEST_STATUS);
            final String rejectionReasonCode = individualRequestList.get (REJECTION_CODE);
            final String rejectionReasonDescription = individualRequestList.get (REJECTION_DESCRIPTION);
            final Boolean targetResponse = Boolean.valueOf (individualRequestList.get (TARGET_RESPONSE));

            // Create the individual request
            createIndividualRequest (customerRequestReference, requestType, requestStatus, rejectionReasonCode,
                    rejectionReasonDescription, targetResponse.booleanValue ());
        }

        // Set the target response map in threadlocal for the outbound interceptor to pick up
        SdtContext.getContext ().setRawXmlMap (targetResponseMap);
    }

    /**
     * Get the bulk submission.
     * 
     * @return bulk submission
     */
    public IBulkSubmission getBulkSubmission ()
    {
        return bulkSubmission;
    }

    /**
     * Set the bulk submission.
     * 
     * @param bulkSubmission bulk submission.
     */
    public void setBulkSubmission (final IBulkSubmission bulkSubmission)
    {
        this.bulkSubmission = bulkSubmission;
    }

    /**
     * Set the target response.
     * 
     * @param targetResponse target response
     */
    public void setTargetResponse (final String targetResponse)
    {
        this.targetResponse = targetResponse;
    }

}
