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

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.test.util.DBUnitUtility;
import uk.gov.moj.sdt.utils.SpringApplicationContext;

/**
 * Test {@link BulkCustomerDao} query methods.
 * 
 * @author Robin Compston
 * 
 */
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (locations = {"classpath*:**/applicationContext.xml", "classpath*:**/spring*.xml"})
public class BulkCustomerDaoTest extends TestCase
{
    /**
     * Logger object.
     */
    private static final Logger LOG = LoggerFactory.getLogger (BulkCustomerDaoTest.class);

    /**
     * Default constructor for {@link BulkCustomerDaoTest}.
     */
    public BulkCustomerDaoTest ()
    {
        super ();
        DBUnitUtility.loadDatabase (this.getClass (), false);
    }

    /**
     * Tests {@link uk.gov.moj.sdt.dao.GenericDao} fetch.
     */
    @Test
    public void testGetBulkCustomerBySdtId ()
    {
        final IBulkCustomerDao bulkCustomersDao =
                (IBulkCustomerDao) SpringApplicationContext.getBean ("uk.gov.moj.sdt.dao.api.IBulkCustomerDao");

        final IBulkCustomer[] bulkCustomers = bulkCustomersDao.getBulkCustomerBySdtId (123);
        if (bulkCustomers.length > 0)
        {
            LOG.debug ("Retrieved bulk customer id [" + Long.toString (bulkCustomers[0].getId ()) + "]");
        }
        return;
    }
}
