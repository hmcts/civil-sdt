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

import org.hibernate.criterion.Restrictions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.GlobalParameter;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.test.util.DBUnitUtility;

/**
 * Test {@link GenericDao} CRUD methods.
 * 
 * @author Robin Compston
 * 
 */
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (locations = {"classpath*:**/applicationContext.xml", "classpath*:**/spring*.xml"})
public class GenericDaoTest extends AbstractTransactionalJUnit4SpringContextTests
{
    /**
     * Logger object.
     */
    private static final Logger LOG = LoggerFactory.getLogger (GenericDaoTest.class);

    /**
     * Default constructor for {@link GenericDaoTest}.
     */
    public GenericDaoTest ()
    {
        super ();
        DBUnitUtility.loadDatabase (this.getClass (), false);
    }

    /**
     * Tests {@link uk.gov.moj.sdt.dao.GenericDao} fetch.
     */
    @Test
    public void testFetch ()
    {
        final IGenericDao genericDao =
                (IGenericDao) this.applicationContext.getBean ("uk.gov.moj.sdt.dao.api.IGenericDao");

        final long id = 10711;
        final IBulkCustomer bulkCustomer = genericDao.fetch (BulkCustomer.class, id);
        bulkCustomer.getId ();

    }

    /**
     * Tests query.
     */
    @Test
    public void testQuery ()
    {
        final IGenericDao genericDao =
                (IGenericDao) this.applicationContext.getBean ("uk.gov.moj.sdt.dao.api.IGenericDao");

        final IBulkCustomer[] bulkCustomers =
                genericDao.query (BulkCustomer.class, Restrictions.eq ("sdtCustomerId", 2L));

        if (bulkCustomers.length == 1)
        {
            // User found
            final IBulkCustomer bulkCustomer = bulkCustomers[0];
            LOG.debug ("sdtCustomerId = " + bulkCustomer.getSdtCustomerId ());
        }
    }

    /**
     * Test method for query as count.
     */
    @Test
    public void testQueryAsCount ()
    {
        final IGenericDao genericDao =
                (IGenericDao) this.applicationContext.getBean ("uk.gov.moj.sdt.dao.api.IGenericDao");

        final long customerCount = genericDao.queryAsCount (BulkCustomer.class, Restrictions.eq ("sdtCustomerId", 2L));

        if (customerCount == 1)
        {
            Assert.assertTrue ("Found expected number of rows", true);
        }
    }

    /**
     * Tests the global parameter.
     */
    @Test
    public void testGlobalParametersQuery ()
    {
        final IGenericDao genericDao =
                (IGenericDao) this.applicationContext.getBean ("uk.gov.moj.sdt.dao.api.IGenericDao");

        final IGlobalParameter[] globalParameters = genericDao.query (GlobalParameter.class);

        if (globalParameters.length == 2)
        {
            // Found the global parameters
            for (IGlobalParameter globalParam : globalParameters)
            {
                LOG.debug ("GlobalParam =" + globalParam.getName () + ":" + globalParam.getValue ());
            }
        }
    }

    /**
     * Tests insert.
     */
    @Test
    public void testInsert ()
    {
        final IGenericDao genericDao =
                (IGenericDao) this.applicationContext.getBean ("uk.gov.moj.sdt.dao.api.IGenericDao");

        final IBulkCustomer bulkCustomer = new BulkCustomer ();
        // bulkCustomer.setId (2);
        bulkCustomer.setSdtCustomerId (456);

        genericDao.persist (bulkCustomer);
    }

    /**
     * Tests the bulk insert.
     */
    @Test
    public void testBulkInsert ()
    {
        final IGenericDao genericDao =
                (IGenericDao) this.applicationContext.getBean ("uk.gov.moj.sdt.dao.api.IGenericDao");

        final List<IBulkCustomer> bulkObjectList = new ArrayList<IBulkCustomer> ();
        final IBulkCustomer bulkCustomer = new BulkCustomer ();
        // bulkCustomer.setId (2);
        bulkCustomer.setSdtCustomerId (456);

        final IBulkCustomer bulkCustomer2 = new BulkCustomer ();
        bulkCustomer2.setSdtCustomerId (457);

        bulkObjectList.add (bulkCustomer);
        bulkObjectList.add (bulkCustomer2);

        genericDao.persistBulk (bulkObjectList);

        final List<IBulkCustomer> savedBulkObjectList =
                genericDao
                        .queryAsList (IBulkCustomer.class, Restrictions.in ("sdtCustomerId", new Long[] {456L, 457L}));

        Assert.assertNotNull (savedBulkObjectList);
        Assert.assertEquals (2, savedBulkObjectList.size ());
        for (IBulkCustomer savedBulkCustomer : savedBulkObjectList)
        {
            Assert.assertNotNull (savedBulkCustomer);
        }

    }

    /**
     * Tests the bulk update.
     */
    @Test
    public void testBulkUpdate ()
    {
        final IGenericDao genericDao =
                (IGenericDao) this.applicationContext.getBean ("uk.gov.moj.sdt.dao.api.IGenericDao");

        final List<IBulkCustomer> bulkObjectList = new ArrayList<IBulkCustomer> ();
        final IBulkCustomer bulkCustomer = new BulkCustomer ();
        bulkCustomer.setId (10711L);
        bulkCustomer.setSdtCustomerId (456);

        bulkObjectList.add (bulkCustomer);

        genericDao.persistBulk (bulkObjectList);

        final List<IBulkCustomer> savedBulkObjectList =
                genericDao.queryAsList (IBulkCustomer.class, Restrictions.in ("sdtCustomerId", new Long[] {456L}));

        Assert.assertNotNull (savedBulkObjectList);
        Assert.assertEquals (1, savedBulkObjectList.size ());
        for (IBulkCustomer savedBulkCustomer : savedBulkObjectList)
        {
            Assert.assertNotNull (savedBulkCustomer);
            Assert.assertEquals (456, savedBulkCustomer.getSdtCustomerId ());
        }

    }

}
