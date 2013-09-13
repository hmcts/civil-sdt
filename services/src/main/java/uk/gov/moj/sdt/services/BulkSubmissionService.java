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

import java.util.List;

import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.messaging.api.IMessageWriter;
import uk.gov.moj.sdt.misc.IndividualRequestStatus;
import uk.gov.moj.sdt.services.api.IBulkSubmissionService;
import uk.gov.moj.sdt.utils.SdtContext;

/**
 * Implementation of the IBulkSubmissionService interface providing methods
 * to do the tasks related to bulk submission.
 * 
 * @author Manoj Kulkarni
 * 
 */
public class BulkSubmissionService implements IBulkSubmissionService
{
    /**
     * Generic Dao property for doing the task of persisting the domain objects.
     */
    private IGenericDao genericDao;

    /**
     * Message writer for queueing messages to the messaging server.
     */
    private IMessageWriter messageWriter;

    @Override
    public void saveBulkSubmission (final IBulkSubmission bulkSubmission)
    {

        // Get the Raw XML from the ThreadLocal and insert in the BulkSubmission
        bulkSubmission.setPayload (SdtContext.getContext ().getRawInXml ());

        // Now persist the bulk submissions.
        this.getGenericDao ().persist (bulkSubmission);

        // Iterate through each of the individual request for the bulk submission
        // and set the raw xml from the interceptor.

        // Persist the individual request with the payload.
        final List<IndividualRequest> individualRequests = bulkSubmission.getIndividualRequests ();
        for (IndividualRequest iRequest : individualRequests)
        {
            // TODO : Get the payload for individual request when the interface
            // is ready.
            iRequest.setPayload ("To be added");

            // TODO : This method call is not efficient for inserts, need to
            // investigate the bulk insertion mechanism for the GenericDao.
            this.getGenericDao ().persist (iRequest);
        }

        // Enqueue the SDT request id of each individual request to the message
        // server.
        for (IndividualRequest iRequest : individualRequests)
        {
            if (iRequest.getRequestStatus ().equals (IndividualRequestStatus.RECEIVED.getStatus ()))
            {
                this.getMessageWriter ().queueMessage (iRequest.getSdtRequestReference ());
            }
        }

    }

    /**
     * 
     * @return the Generic Dao
     */
    public IGenericDao getGenericDao ()
    {
        return genericDao;
    }

    /**
     * 
     * @param genericDao the GenericDao implementation
     */
    public void setGenericDao (final IGenericDao genericDao)
    {
        this.genericDao = genericDao;
    }

    /**
     * 
     * @return the Message Writer
     */
    public IMessageWriter getMessageWriter ()
    {
        return messageWriter;
    }

    /**
     * 
     * @param messageWriter the Message writer implementation.
     */
    public void setMessageWriter (final IMessageWriter messageWriter)
    {
        this.messageWriter = messageWriter;
    }

}
