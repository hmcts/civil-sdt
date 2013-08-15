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
 * $Id: ClaimXsdTest.java 16414 2013-05-29 10:56:45Z agarwals $
 * $LastChangedRevision: 16414 $
 * $LastChangedDate: 2013-05-29 11:56:45 +0100 (Wed, 29 May 2013) $
 * $LastChangedBy: holmessm $ */
package uk.gov.moj.sdt.validators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.utils.visitor.api.ITree;
import uk.gov.moj.sdt.validators.api.IBulkCustomerValidator;
import uk.gov.moj.sdt.validators.exception.AbstractBusinessException;
import uk.gov.moj.sdt.validators.exception.CustomerNotSetupException;

/**
 * Implementation of bulk customer validation.
 * 
 * @author Simon Holmes
 * 
 */
public class BulkCustomerValidator extends AbstractSdtValidator implements IBulkCustomerValidator
{

    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (BulkCustomerValidator.class);

    /**
     * SDT Customer Id valid start.
     */
    private static final long SDT_ID_START = 10000000;

    /**
     * SDT Customer Id valid end.
     */
    private static final long SDT_ID_END = 99999999;

    /**
     * No-argument Constructor.
     */
    public BulkCustomerValidator ()
    {
    }

    @Override
    public void visit (final BulkCustomer bulkCustomer, final ITree tree)
    {

        final long sdtCustomerId = bulkCustomer.getSdtCustomerId ();

        LOGGER.info ("Bulk customer id [" + sdtCustomerId + "].");

        // Validate that the customer exists and can access the target application
        // Get the target application, the first entry should be the one we want to test access against.
        final TargetApplication targetApplication = bulkCustomer.getTargetApplications ().iterator ().next ();

        if (targetApplication != null && sdtCustomerId > 0)
        {
            //TODO - waiting for Shu-Yee to confirm whether these start and end Id's need to be enforced. 
            if (sdtCustomerId < SDT_ID_START || sdtCustomerId > SDT_ID_END)
            {
                throw new CustomerNotSetupException (AbstractBusinessException.ErrorCode.CUST_NOT_SETUP.toString (),
                        "SDT Customer Id not between " + SDT_ID_START + " and " + SDT_ID_END + ", actual value was [" +
                                sdtCustomerId + "]");
            }
            else
            {
                checkCustomerHasAccess (sdtCustomerId, targetApplication.getTargetApplicationCode ());
            }
        }
        else
        {
            throw new CustomerNotSetupException (AbstractBusinessException.ErrorCode.CUST_NOT_SETUP.toString (),
                    "SDTCustomerId and/or TargetApplicationCode is not valid!");
        }

    }

}
