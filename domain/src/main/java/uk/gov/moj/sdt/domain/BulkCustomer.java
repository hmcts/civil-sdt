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

package uk.gov.moj.sdt.domain;


/**
 * Bulk Customer Information manually set up and maintained
 * for the SDT application for each registered Target Application.
 * 
 * @author d130680
 * 
 */
public class BulkCustomer extends AbstractDomainObject
{

    /**
     * Unique code for each Bulk Customer organisation.
     */
    private String customerCaseCode;

    /**
     * This is a manually allocated and maintained value.
     */
    private int sdtCustomerId;

    /**
     * The National Coded Party for the organisation, between 1000 and 9999.
     */
    private int customerIdentifier;

    /**
     * Get Customer case code.
     * 
     * @return customer case code
     */
    public String getCustomerCaseCode ()
    {
        return customerCaseCode;
    }

    /**
     * Set customer case code.
     * 
     * @param customerCaseCode customer case code
     */
    public void setCustomerCaseCode (final String customerCaseCode)
    {
        this.customerCaseCode = customerCaseCode;
    }

    /**
     * Get SDT customer id.
     * 
     * @return SDT customer id
     */
    public int getSdtCustomerId ()
    {
        return sdtCustomerId;
    }

    /**
     * Set SDT customer id.
     * 
     * @param sdtCustomerId SDT customer id
     */
    public void setSdtCustomerId (final int sdtCustomerId)
    {
        this.sdtCustomerId = sdtCustomerId;
    }

    /**
     * Get customer identifier.
     * 
     * @return customer identifier
     */
    public int getCustomerIdentifier ()
    {
        return customerIdentifier;
    }

    /**
     * Set customer identifier.
     * 
     * @param customerIdentifier customer identifier
     */
    public void setCustomerIdentifier (final int customerIdentifier)
    {
        this.customerIdentifier = customerIdentifier;
    }

}
