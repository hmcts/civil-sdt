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

package uk.gov.moj.sdt.dao;

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
     * SDT Bulk reference used to return feedback for validated request.
     */
    public static final String SDT_FEEDBACK_VALIDATED = "MCOL_20130722_A00000001";

    /**
     * SDT Bulk reference used to return feedback for validated and uploaded request.
     */
    public static final String SDT_FEEDBACK_UPLOADED = "MCOL_20130722_B00000001";

    /**
     * SDT Bulk reference used to return feedback for successfully completed request.
     */
    public static final String SDT_FEEDBACK_COMPLETED = "MCOL_20130722_B00000002";

    /**
     * SDT Bulk reference used to return feedback with all defined error messages.
     */
    public static final String SDT_FEEDBACK_ERROR = "MCOL_20130722_C00000001";

    /**
     * Pre-defined values for valid customer references.
     */
    private static final List<String> DUPLICATE_REFERENCE;

    /**
     * Pre-defined values for valid bulk references.
     */
    private static final List<String> BULK_REFERENCE;

    static
    {

        // Invalid customer reference
        DUPLICATE_REFERENCE = new ArrayList<String> ();
        DUPLICATE_REFERENCE.add ("duplicate");

        // Valid SDT bulk references
        BULK_REFERENCE = new ArrayList<String> ();
        BULK_REFERENCE.add (SDT_FEEDBACK_VALIDATED);
        BULK_REFERENCE.add (SDT_FEEDBACK_UPLOADED);
        BULK_REFERENCE.add (SDT_FEEDBACK_COMPLETED);
        BULK_REFERENCE.add (SDT_FEEDBACK_ERROR);

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
        return !DUPLICATE_REFERENCE.contains (customerReference.toLowerCase ());
    }

    /**
     * Check the bulk reference is valid for a bulk feedback request against a static list.
     * 
     * @param bulkReference bulk reference
     * @return true or false
     */
    protected boolean isBulkReferenceValid (final String bulkReference)
    {
        return BULK_REFERENCE.contains (bulkReference);
    }
}
