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

package uk.gov.moj.sdt.producers.resolver;

import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.HeaderType;

/**
 * Maps incoming request objects to equivalent domain objects.
 *  
 * @author d130680
 *
 */
public final class BulkRequestToDomainResolver
{
 
    
    /**
     * Private constructor.
     */
    private BulkRequestToDomainResolver() {
        
    }
    
    /**
     * Maps the header to a Bulk Submission.
     * 
     * @param headerType header type
     * @return bulk submission
     */
    public static BulkSubmission mapToBulkSubmission(final HeaderType headerType) {
        final BulkSubmission bulkSubmission = new BulkSubmission ();
        final TargetApplication targetApplication = new TargetApplication ();
        
        bulkSubmission.setCustomerReference (headerType.getCustomerReference ());
        bulkSubmission.setNumberOfRequest (headerType.getRequestCount ().intValue ());
        
        targetApplication.setTargetApplicationCode (headerType.getTargetApplicationId ().value ());
        
        bulkSubmission.setTargetApplication (targetApplication);
        
        return bulkSubmission;
        
    }
    
    /**
     * Maps the header to a Bulk Customer.
     * 
     * @param headerType header type
     * @return bulk customer
     */
    public static BulkCustomer mapToBulkCustomer(final HeaderType headerType) {
        final BulkCustomer bulkCustomer = new BulkCustomer ();
        
        bulkCustomer.setSdtCustomerId (headerType.getSdtCustomerId ().intValue ());
        return bulkCustomer;
        
    }
}
