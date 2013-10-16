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
package uk.gov.moj.sdt.services.api;

import uk.gov.moj.sdt.consumers.exception.OutageException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;

/**
 * This interface is for target application prior-submission and post-submission operations on
 * the individual request.
 * 
 * @author Manoj Kulkarni
 * 
 */
public interface ITargetApplicationSubmissionService
{
    /**
     * Returns the IndividualRequest for the given SDT Request Reference.
     * 
     * @param sdtRequestReference the unique SDT Request Reference associated with individual request
     * @return IndividualRequest object associated with the matching individual request
     */
    IIndividualRequest getRequestToSubmit (final String sdtRequestReference);

    /**
     * Update the request object. This method is to be called to update the request object
     * to bring it in the request status state of FORWARDED
     * 
     * @param individualRequest the IndividualRequest object that contains the
     *            response payload from the target application.
     */
    void updateForwardingRequest (final IIndividualRequest individualRequest);

    /**
     * Update the request object. This method is to be called to update the request object on
     * completion i.e. successful response is received from the target application.
     * 
     * @param individualRequest the individual request to be marked as completed
     */
    void updateCompletedRequest (final IIndividualRequest individualRequest);

    /**
     * Updates the request object. This method is called when the send request to the target application
     * times out.
     * 
     * @param individualRequest the individual request to be marked with reason as not acknowledged
     */
    void updateRequestTimeOut (final IIndividualRequest individualRequest);

    /**
     * Updates the request object. This method is called when the send request to target application
     * returns an server error.
     * 
     * @param individualRequest the individual request to be marked with reason as not responding
     */
    void updateTargetAppUnavailable (final IIndividualRequest individualRequest);

    /**
     * Send the individual request to target application for submission.
     * 
     * @param individualRequest the individual request to be sent to target application.
     * @throws OutageException when the target web service is not responding.
     * @throws TimeoutException when the target web service does not respond back in time.
     */
    void sendRequestToTargetApp (final IIndividualRequest individualRequest) throws OutageException, TimeoutException;

    /**
     * Updates the request object. This method updates the request status to Rejected
     * and sets the soap fault message in the internal error field of the request.
     * 
     * @param individualRequest the individual request to be marked.
     * @param soapFaultError the soap fault message when the request has failed.
     */
    void updateRequestSoapError (final IIndividualRequest individualRequest, String soapFaultError);

}
