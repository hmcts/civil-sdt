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
import uk.gov.moj.sdt.validators.exception.CustomerNotSetupException;
import uk.gov.moj.sdt.validators.exception.CustomerReferenceNotFoundException;
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
     * Check that the bulk customer exists has access to the target application.
     * 
     * @param sdtCustomerId bulk customer
     * @param targetApplicationCode target application
     */
    public void checkCustomerHasAccess (final long sdtCustomerId, final String targetApplicationCode)
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

                // TODO - Confirm contact information
                throw new CustomerNotSetupException (AbstractBusinessException.ErrorCode.CUST_NOT_SETUP.toString (),
                        "The Bulk Customer organisation is not set up to send Service Request messages to the {0}. "
                                + "Please contact <TBC> for assistance.", replacements);
            }
        }
        else
        {
            replacements = new ArrayList<String> ();
            replacements.add (targetApplicationCode);
            // TODO - Confirm contact information
            throw new CustomerReferenceNotFoundException (
                    AbstractBusinessException.ErrorCode.CUST_REF_MISSING.toString (),
                    "The Bulk Customer organisation does not have a Customer Reference set up for {0}. "
                            + "Please contact <TBC> for assistance", replacements);

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
