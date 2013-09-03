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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.dao.MockGenericDao;
import uk.gov.moj.sdt.domain.api.IBulkFeedbackRequest;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.producers.comx.utils.BulkFeedbackFactory;
import uk.gov.moj.sdt.services.api.IBulkFeedbackService;

/**
 * Implementation for mocking of SDT Get Bulk Feedback service.
 * This class will provide a static list of responses based on the SDT Bulk Reference.
 * 
 * @author d130680
 * 
 */
public class MockBulkFeedbackService implements IBulkFeedbackService
{
    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (MockBulkFeedbackService.class);

    /**
     * Injected sample feedback for validated bulk submission.
     */
    private BulkFeedbackFactory bulkFeedbackValidated;

    /**
     * Injected sample feedback for uploaded bulk submission.
     */
    private BulkFeedbackFactory bulkFeedbackUploaded;

    /**
     * Injected sample feedback for completed bulk submission.
     */
    private BulkFeedbackFactory bulkFeedbackCompleted;

    /**
     * Injected sample feedback for error bulk submission.
     */
    private BulkFeedbackFactory bulkFeedbackError;

    @Override
    public IBulkSubmission getBulkFeedback (final IBulkFeedbackRequest bulkFeedbackRequest)
    {
        // Determine which feedback sample to return based on the SDT bulk reference
        final String sdtBulkReference = bulkFeedbackRequest.getSdtBulkReference ();
        if (MockGenericDao.SDT_FEEDBACK_VALIDATED.equals (sdtBulkReference))
        {

            return populateBulkSubmission (bulkFeedbackValidated, sdtBulkReference);
        }
        else if (MockGenericDao.SDT_FEEDBACK_UPLOADED.equals (sdtBulkReference))
        {
            return populateBulkSubmission (bulkFeedbackUploaded, sdtBulkReference);
        }
        else if (MockGenericDao.SDT_FEEDBACK_COMPLETED.equals (sdtBulkReference))
        {
            return populateBulkSubmission (bulkFeedbackCompleted, sdtBulkReference);
        }
        else
        {
            // Return this as the default, BulkFeedbackRequestValidator will throw
            // an error if the bulk reference does not match
            return populateBulkSubmission (bulkFeedbackError, sdtBulkReference);
        }

    }

    /**
     * Populate the bulk submission with the sdt bulk reference.
     * 
     * @param bulkFeedbackFactory bulk feedback factory containing bulk submission
     * @param sdtBulkReference sdt bulk reference
     * @return populate bulk submission
     */
    private IBulkSubmission populateBulkSubmission (final BulkFeedbackFactory bulkFeedbackFactory,
                                                    final String sdtBulkReference)
    {
        final IBulkSubmission bulkSubmission = bulkFeedbackFactory.getBulkSubmission ();
        bulkSubmission.setSdtBulkReference (sdtBulkReference);

        return bulkSubmission;

    }

    /**
     * Inject bulk feedback factory containing bulk submission with validated individual request.
     * 
     * @param bulkFeedbackValidated bulk feedback factory
     */
    public void setBulkFeedbackValidated (final BulkFeedbackFactory bulkFeedbackValidated)
    {
        this.bulkFeedbackValidated = bulkFeedbackValidated;
    }

    /**
     * Inject bulk feedback factory containing bulk submission with uploaded individual request.
     * 
     * @param bulkFeedbackUploaded bulk feedback factory
     */
    public void setBulkFeedbackUploaded (final BulkFeedbackFactory bulkFeedbackUploaded)
    {
        this.bulkFeedbackUploaded = bulkFeedbackUploaded;
    }

    /**
     * Inject bulk feedback factory containing bulk submission with completed individual request.
     * 
     * @param bulkFeedbackCompleted bulk feedback factory
     */
    public void setBulkFeedbackCompleted (final BulkFeedbackFactory bulkFeedbackCompleted)
    {
        this.bulkFeedbackCompleted = bulkFeedbackCompleted;
    }

    /**
     * Inject bulk feedback factory containing bulk submission with error individual request.
     * 
     * @param bulkFeedbackError bulk feedback factory
     */
    public void setBulkFeedbackError (final BulkFeedbackFactory bulkFeedbackError)
    {
        this.bulkFeedbackError = bulkFeedbackError;
    }

}
