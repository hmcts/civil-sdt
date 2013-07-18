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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.dao.api.IBulkSubmissionDao;
import uk.gov.moj.sdt.dao.api.ITargetApplicationDao;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.validators.api.IBulkSubmissionValidator;
import uk.gov.moj.sdt.validators.exception.AbstractBusinessException;
import uk.gov.moj.sdt.validators.exception.RequestCountMismatchException;
import uk.gov.moj.sdt.validators.exception.SdtCustomerIdNotFoundException;
import uk.gov.moj.sdt.validators.exception.SdtCustomerReferenceNotUniqueException;
import uk.gov.moj.sdt.visitor.AbstractDomainObjectVisitor;

/**
 * Implementation of {@link IBulkSubmissionValidator}.
 * 
 * @author Saurabh Agarwal
 * 
 */
public class BulkSubmissionValidator extends AbstractDomainObjectVisitor implements IBulkSubmissionValidator
{
    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (BulkSubmissionValidator.class);

    /**
     * Bulk customer dao.
     */
    private IBulkCustomerDao bulkCustomerDao;

    /**
     * Bulk submission dao.
     */
    private IBulkSubmissionDao bulkSubmissionDao;

    /**
     * Target application dao.
     */
    private ITargetApplicationDao targetApplicationtDao;

    /**
     * No-argument Constructor.
     */
    public BulkSubmissionValidator ()
    {
    }

    @Override
    public void visit (final BulkSubmission bulkSubmission)
    {

        // Do validation of bulk submission.
        LOGGER.info ("visit(BulkSubmission)");

        // TODO - A generic method in AbstractDomainObject will do this check now
        // Validate SDT Customer ID from BulkCustomer
        LOGGER.debug ("Validating SDT Customer ID");
        final int sdtCustomerId = bulkSubmission.getBulkCustomer ().getSdtCustomerId ();

        final IBulkCustomer bulkCustomer = bulkCustomerDao.getBulkCustomerBySdtId (sdtCustomerId);
        List<String> replacements = null;

        if (bulkCustomer == null)
        {
            replacements = new ArrayList<String> ();
            replacements.add (String.valueOf (sdtCustomerId));
            throw new SdtCustomerIdNotFoundException (
                    AbstractBusinessException.ErrorCode.SDT_CUSTOMER_ID_NOT_FOUND.toString (),
                    "SDT Customer Id [{0}] was not found.", replacements);
        }

        // TODO - A generic method in AbstractDomainObject will do this check now
        // Validate target application
        final String targetApplicationCode = bulkSubmission.getTargetApplication ().getTargetApplicationCode ();
        if ( !targetApplicationtDao.hasAccess (bulkCustomer, targetApplicationCode))
        {
            replacements = new ArrayList<String> ();
            replacements.add (targetApplicationCode);
            replacements.add (String.valueOf (sdtCustomerId));

            throw new SdtCustomerReferenceNotUniqueException (
                    AbstractBusinessException.ErrorCode.INVALID_TARGET_APPLICATION.toString (),
                    "SDT Customer Id [{1}] does not have access to Target Application [{0}].", replacements);
        }

        // Validate customer reference is unique across data retention period for bulk submission
        final String sdtCustomerReference = bulkSubmission.getCustomerReference ();
        if ( !bulkSubmissionDao.isCustomerReferenceUnique (bulkCustomer, sdtCustomerReference))
        {
            replacements = new ArrayList<String> ();
            replacements.add (String.valueOf (sdtCustomerReference));
            replacements.add (String.valueOf (bulkCustomer.getSdtCustomerId ()));
            // CHECKSTYLE:OFF
            throw new SdtCustomerReferenceNotUniqueException (
                    AbstractBusinessException.ErrorCode.SDT_CUSTOMER_REFRENCE_NOT_UNIQUE.toString (),
                    "SDT Customer Reference [{0}] was not unique across the data retention period for the Bulk Submission and SDT Customer Id [{1}].",
                    replacements);
            // CHECKSTYLE:ON
        }

        // Validate customer reference is within the list of individual requests
        final Set<String> customerReferenceSet = new HashSet<String> ();
        for (IIndividualRequest individualRequest : bulkSubmission.getIndividualRequests ())
        {

            final String customerRequestReference = individualRequest.getCustomerRequestReference ();
            final boolean success = customerReferenceSet.add (customerRequestReference);
            // Check that the customer request reference is unique within the current list of individual requests
            if ( !success)
            {
                replacements = new ArrayList<String> ();
                replacements.add (customerRequestReference);
                replacements.add (String.valueOf (sdtCustomerId));

                // CHECKSTYLE:OFF
                throw new SdtCustomerReferenceNotUniqueException (
                        AbstractBusinessException.ErrorCode.SDT_CUSTOMER_REFRENCE_NOT_UNIQUE.toString (),
                        "Individual Request Id [{0}] was not unique within the list of Invidiual Requests for SDT Customer Id [{1}].",
                        replacements);
                // CHECKSTYLE:ON
            }
        }

        // Check the request count matches
        if (bulkSubmission.getNumberOfRequest () != bulkSubmission.getIndividualRequests ().size ())
        {
            replacements = new ArrayList<String> ();
            replacements.add (bulkSubmission.getCustomerReference ());
            throw new RequestCountMismatchException (
                    AbstractBusinessException.ErrorCode.REQ_COUNT_MISMATCH.toString (),
                    "Request count mismatch for Bulk Submission [{0}].", replacements);

        }

        LOGGER.info ("finished visit(BulkSubmission)");

    }

    /**
     * Set bulk customer dao.
     * 
     * @param bulkCustomerDao bulk customer dao
     */
    public void setBulkCustomerDao (final IBulkCustomerDao bulkCustomerDao)
    {
        this.bulkCustomerDao = bulkCustomerDao;
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

    /**
     * Set target application dao.
     * 
     * @param targetApplicationtDao target application dao
     */
    public void setTargetApplicationtDao (final ITargetApplicationDao targetApplicationtDao)
    {
        this.targetApplicationtDao = targetApplicationtDao;
    }

}
