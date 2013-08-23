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
package uk.gov.moj.sdt.validators;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.dao.api.IBulkSubmissionDao;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.utils.visitor.api.ITree;
import uk.gov.moj.sdt.validators.api.IBulkSubmissionValidator;
import uk.gov.moj.sdt.validators.exception.AbstractBusinessException;
import uk.gov.moj.sdt.validators.exception.CustomerReferenceNotUniqueException;
import uk.gov.moj.sdt.validators.exception.RequestCountMismatchException;

/**
 * Implementation of {@link IBulkSubmissionValidator}.
 * 
 * @author Saurabh Agarwal
 * 
 */
public class BulkSubmissionValidator extends AbstractSdtValidator implements IBulkSubmissionValidator
{
    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (BulkSubmissionValidator.class);

    /**
     * Bulk submission dao.
     */
    private IBulkSubmissionDao bulkSubmissionDao;

    /**
     * No-argument Constructor.
     */
    public BulkSubmissionValidator ()
    {
    }

    @Override
    public void visit (final IBulkSubmission bulkSubmission, final ITree tree)
    {

        // Do validation of bulk submission.
        LOGGER.info ("started visit(BulkSubmission)");

        // Validate SDT Customer ID and target application
        final IBulkCustomer bulkCustomer = bulkSubmission.getBulkCustomer ();
        final long sdtCustomerId = bulkCustomer.getSdtCustomerId ();

        final ITargetApplication targetApplication = bulkSubmission.getTargetApplication ();
        checkCustomerHasAccess (sdtCustomerId, targetApplication.getTargetApplicationCode ());

        // Validate customer reference is unique across data retention period for bulk submission
        final String sdtCustomerReference = bulkSubmission.getCustomerReference ();
        List<String> replacements = null;
        if ( !bulkSubmissionDao.isCustomerReferenceUnique (bulkCustomer, sdtCustomerReference))
        {
            replacements = new ArrayList<String> ();
            replacements.add (String.valueOf (sdtCustomerReference));
            // CHECKSTYLE:OFF
            throw new CustomerReferenceNotUniqueException (
                    AbstractBusinessException.ErrorCode.DUP_CUST_FILEID.toString (),
                    "Duplicate User File Reference {0} supplied.", replacements);
            // CHECKSTYLE:ON
        }

        // Validate customer reference is within the list of individual requests
        // final Set<String> customerReferenceSet = new HashSet<String> ();
        //
        // for (IIndividualRequest individualRequest : bulkSubmission.getIndividualRequests ())
        // {
        //
        // final String customerRequestReference = individualRequest.getCustomerRequestReference ();
        // final boolean success = customerReferenceSet.add (customerRequestReference);
        // // Check that the user file reference is unique within the current list of individual requests
        // if ( !success)
        // {
        // replacements = new ArrayList<String> ();
        // replacements.add (customerRequestReference);
        //
        // // CHECKSTYLE:OFF
        // throw new DuplicateUserFileReferenceException (AbstractBusinessException.ErrorCode.DUP_CUST_REQID.toString
        // (),
        // "Duplicate Unique Request Identifier submitted {0}.", replacements);
        // // // CHECKSTYLE:ON
        // }
        // }

        // Check the request count matches
        if (bulkSubmission.getNumberOfRequest () != bulkSubmission.getIndividualRequests ().size ())
        {
            replacements = new ArrayList<String> ();
            replacements.add (Integer.valueOf (bulkSubmission.getIndividualRequests ().size ()).toString ());
            replacements.add (Integer.valueOf (bulkSubmission.getNumberOfRequest ()).toString ());
            replacements.add (bulkSubmission.getCustomerReference ());
            throw new RequestCountMismatchException (
                    AbstractBusinessException.ErrorCode.REQ_COUNT_MISMATCH.toString (),
                    "Unexpected Total Number of Requests identified. {0} requested identified, "
                            + "{1} requests expected in Bulk Request {2}.", replacements);

        }

        LOGGER.debug ("finished visit(BulkSubmission)");

    }

    /**
     * Set bulk submission dao.
     * 
     * @param bulkSubmissionDao bulk submission dao
     */
    public void setBulkSubmissionDao (final IBulkSubmissionDao bulkSubmissionDao)
    {
        this.bulkSubmissionDao = bulkSubmissionDao;
    }

}
