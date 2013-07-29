/* Copyrights and Licenses
 * 
 * Copyright (c) 2012-2014 by the Ministry of Justice. All rights reserved.
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

package uk.gov.moj.sdt.producer.comx.utils;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.gov.moj.sdt.utils.api.ISdtBulkReferenceGenerator;

/**
 * @author D303894
 * 
 *         This class generates the bulk SDT reference number for the SDT commissioning application
 */
public class MockSdtBulkReferenceGenerator implements ISdtBulkReferenceGenerator
{

    /**
     * The index of the mockBulkIds array that is to be selected for current call.
     */
    private int currIndexToSelect = -1;

    /**
     * The mock bulk id array that holds hard-coded unique bulk id reference numbers.
     */
    private String[] mockBulkIds = new String[] {"000000001", "000000002", "000000003", "000000004", "000000005"};

    @Override
    public synchronized String getSdtBulkReference (final String targetApplication)
    {
        final int i = this.getCurrIndexToSelect ();
        final String bulkId = this.mockBulkIds[i];
        final String referenceNumber = this.generateReferenceNumber (targetApplication, bulkId);

        return referenceNumber;
    }

    /**
     * Get the next available index so that we can use the available hard coded bulk ids in a round robin fashion.
     * 
     * @return the current index of bulk id array to be selected.
     */
    private int getCurrIndexToSelect ()
    {
        // We have fixed number of bulk ids available, so cycle if limit reached.
        if (++this.currIndexToSelect >= this.mockBulkIds.length)
        {
            this.currIndexToSelect = 0;
        }

        return this.currIndexToSelect;
    }

    /**
     * Generate the commissioning system bulk reference.
     * 
     * @param targetApplication the target application for the bulk request submission
     * @param bulkId the unique bulk id number
     * @return the full SDT bulk reference number formatted as per the specifications
     */
    private String generateReferenceNumber (final String targetApplication, final String bulkId)
    {
        final String refNumberFormat = "{0}-{1}-{2}";
        final SimpleDateFormat dateFormat = new SimpleDateFormat ();
        dateFormat.applyLocalizedPattern ("yyyyMMddHHmmss");

        final int totalArgs = 3;
        final Object[] args = new Object[totalArgs];
        args[0] = targetApplication;
        args[1] = dateFormat.format (new Date (System.currentTimeMillis ()));
        args[2] = bulkId;

        // Fill in the reference according to the reference format.
        final String refNumber = MessageFormat.format (refNumberFormat, args[0], args[1], args[2]);

        return refNumber;
    }
}
