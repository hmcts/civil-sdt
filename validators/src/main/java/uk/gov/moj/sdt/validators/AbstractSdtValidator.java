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

package uk.gov.moj.sdt.validators;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.dao.api.ITargetApplicationDao;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.validators.exception.AbstractBusinessException;
import uk.gov.moj.sdt.validators.exception.SdtCustomerIdNotFoundException;
import uk.gov.moj.sdt.validators.exception.SdtCustomerReferenceNotUniqueException;
import uk.gov.moj.sdt.visitor.AbstractDomainObjectVisitor;

/**
 * Base class for validators providing common methods.
 * 
 * @author d130680
 * 
 */
public abstract class AbstractSdtValidator extends AbstractDomainObjectVisitor
{
    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (AbstractSdtValidator.class);

    /**
     * Bulk customer dao.
     */
    private IBulkCustomerDao bulkCustomerDao;

    /**
     * Target application dao.
     */
    private ITargetApplicationDao targetApplicationtDao;

    /**
     * Validates the bulk customer exists.
     * 
     * @param sdtCustomerId bulk customer to check
     */
    public void checkBulkCustomerExist (final long sdtCustomerId)
    {
        // Validate SDT Customer ID from BulkCustomer
        LOGGER.info ("Validating SDT Customer ID [" + sdtCustomerId + "]");

        final IBulkCustomer customer = bulkCustomerDao.getBulkCustomerBySdtId (sdtCustomerId);
        List<String> replacements = null;

        if (customer == null)
        {
            replacements = new ArrayList<String> ();
            replacements.add (String.valueOf (sdtCustomerId));
            throw new SdtCustomerIdNotFoundException (
                    AbstractBusinessException.ErrorCode.SDT_CUSTOMER_ID_NOT_FOUND.toString (),
                    "SDT Customer Id [{0}] was not found.", replacements);
        }

    }

    /**
     * Check that the bulk customer exists has access to the target application.
     * 
     * @param sdtCustomerId bulk customer
     * @param targetApplicationCode target application
     */
    public void checkCustomerExistsHasAccess (final long sdtCustomerId, final String targetApplicationCode)
    {
        LOGGER.info ("Validating SDT Customer ID [" + sdtCustomerId + "] and target application + [" +
                targetApplicationCode + "]");
        final IBulkCustomer bulkCustomer = bulkCustomerDao.getBulkCustomerBySdtId (sdtCustomerId);
        List<String> replacements = null;

        if (bulkCustomer != null)
        {

            if ( !targetApplicationtDao.hasAccess (bulkCustomer, targetApplicationCode))
            {
                replacements = new ArrayList<String> ();
                replacements.add (targetApplicationCode);
                replacements.add (String.valueOf (sdtCustomerId));

                throw new SdtCustomerReferenceNotUniqueException (
                        AbstractBusinessException.ErrorCode.INVALID_TARGET_APPLICATION.toString (),
                        "SDT Customer Id [{1}] does not have access to Target Application [{0}].", replacements);
            }
        }
        else
        {
            replacements = new ArrayList<String> ();
            replacements.add (String.valueOf (sdtCustomerId));
            throw new SdtCustomerIdNotFoundException (
                    AbstractBusinessException.ErrorCode.SDT_CUSTOMER_ID_NOT_FOUND.toString (),
                    "SDT Customer Id [{0}] was not found.", replacements);

        }

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
     * Set target application dao.
     * 
     * @param targetApplicationtDao target application dao
     */
    public void setTargetApplicationtDao (final ITargetApplicationDao targetApplicationtDao)
    {
        this.targetApplicationtDao = targetApplicationtDao;
    }

}
