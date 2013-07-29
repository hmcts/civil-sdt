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
 * $Id: $
 * $LastChangedRevision: $
 * $LastChangedDate: $
 * $LastChangedBy: $ */

package uk.gov.moj.sdt.producer.comx.dao.mock;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for Mock DAO classes containing helper methods for mock dao sub classes.
 * 
 * Contains a static list of data for testing with the commissioning system.
 * 
 * @author d130680
 * 
 */
public class MockGenericDao
{
    /**
     * Valid sdt customer id.
     */
    public static final int VALID_SDT_CUSTOMER_ID1 = 123;

    /**
     * Valid sdt customer id.
     */
    public static final int VALID_SDT_CUSTOMER_ID2 = 200;

    /**
     * Valid sdt customer id.
     */
    public static final int VALID_SDT_CUSTOMER_ID3 = 201;

    /**
     * Check whether a sdt customer id is valid or not.
     * 
     * @param l sdtCustomerId to check
     * @return true or false
     */
    protected boolean isSDTCustomerIdValid (final long l)
    {
        final List<Long> validSDTCustomers = new ArrayList<Long> ();
        validSDTCustomers.add (new Long (VALID_SDT_CUSTOMER_ID1));
        validSDTCustomers.add (new Long (VALID_SDT_CUSTOMER_ID2));
        validSDTCustomers.add (new Long (VALID_SDT_CUSTOMER_ID3));

        return validSDTCustomers.contains (new Long (l));
    }

    /**
     * Check the customer reference is valid for a bulk submission against a static list.
     * 
     * @param customerReference customer reference
     * 
     * @return true or false
     */
    protected boolean isCustomerReferenceValid (final String customerReference)
    {

        final List<String> validCustomerReference = new ArrayList<String> ();

        validCustomerReference.add ("abc1223456");
        validCustomerReference.add ("abc1112222");
        validCustomerReference.add ("xyz1112222");

        return validCustomerReference.contains (customerReference);

    }
}
