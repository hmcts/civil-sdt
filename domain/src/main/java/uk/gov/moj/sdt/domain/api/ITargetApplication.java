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

package uk.gov.moj.sdt.domain.api;

import java.util.Set;

import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.ServiceRouting;

/**
 * Interface for classes implementing {@link ITargetApplication} .
 * 
 * @author Manoj Kulkarni
 * 
 */
public interface ITargetApplication extends IDomainObject
{

    /**
     * Retrieve a set of all customers who can work against this application.
     * 
     * @return a set of bulk customers who can use this application.
     */
    Set<BulkCustomer> getBulkCustomers ();

    /**
     * Set the list of customers who can work against this application.
     * 
     * @param bulkCustomers the list of all customers who can work on this application.
     */
    void setBulkCustomers (Set<BulkCustomer> bulkCustomers);

    /**
     * Get target application code.
     * 
     * @return target application code
     */
    String getTargetApplicationCode ();

    /**
     * Set target application code.
     * 
     * @param targetApplicationCode target application code
     */
    void setTargetApplicationCode (final String targetApplicationCode);

    /**
     * Get target application name.
     * 
     * @return target application name
     */
    String getTargetApplicationName ();

    /**
     * Set target application name.
     * 
     * @param targetApplicationName target application name
     */
    void setTargetApplicationName (final String targetApplicationName);

    /**
     * Retrieve a set of all customers who can work against this application.
     * 
     * @return a set of bulk customers who can use this application.
     */
    Set<ServiceRouting> getServiceRoutings ();

    /**
     * Set the list of service types which work with this application.
     * 
     * @param serviceRoutings the list of all serviceRoutings who can work with this application.
     */
    void setServiceRoutings (Set<ServiceRouting> serviceRoutings);

}