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
package uk.gov.moj.sdt.cache;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.domain.GlobalParameter;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.utils.mbeans.SdtManagementMBean;
import uk.gov.moj.sdt.utils.mbeans.api.ISdtManagementMBean;

/**
 * Test class for global parameters cache.
 * 
 * @author d130680
 * 
 */
public class GlobalParametersCacheTest
{

    /**
     * Global parameters cache.
     */
    private GlobalParametersCache cache;

    /**
     * Management bean holding cache information.
     */
    private ISdtManagementMBean managementMBean;

    /**
     * Generic dao.
     */
    private IGenericDao mockGenericDao;

    /**
     * Dao query results.
     */
    private GlobalParameter[] result;

    /**
     * Setup mock objects and inject DAO dependencies into our class.
     * 
     */
    @Before
    public void setUp ()
    {
        cache = new GlobalParametersCache ();
        mockGenericDao = EasyMock.createMock (IGenericDao.class);
        cache.setGenericDao (mockGenericDao);
        managementMBean = new SdtManagementMBean ();
        cache.setManagementMBean (managementMBean);

        // Setup some results
        result = new GlobalParameter[3];
        result[0] = new GlobalParameter ();
        result[0].setName ("param1");
        result[0].setValue ("one");
        result[0].setDescription ("parameter 1");
        result[1] = new GlobalParameter ();
        result[1].setName ("param2");
        result[1].setValue ("two");
        result[1].setDescription ("parameter 2");
        result[2] = new GlobalParameter ();
        result[2].setName ("param3");
        result[2].setValue ("three");
        result[2].setDescription ("parameter 3");

    }

    /**
     * Test the getValue method.
     */
    @Test
    public void testGetErrorMessage ()
    {

        // Activate the mock generic dao
        EasyMock.expect (mockGenericDao.query (IGlobalParameter.class)).andReturn (result);
        EasyMock.replay (mockGenericDao);

        // Get some values
        IGlobalParameter param = cache.getValue (IGlobalParameter.class, "param1");
        Assert.assertEquals (param.getName (), "param1");
        Assert.assertEquals (param.getValue (), "one");
        Assert.assertEquals (param.getDescription (), "parameter 1");

        param = cache.getValue (IGlobalParameter.class, "param3");
        Assert.assertEquals (param.getName (), "param3");
        Assert.assertEquals (param.getValue (), "three");
        Assert.assertEquals (param.getDescription (), "parameter 3");

        param = cache.getValue (IGlobalParameter.class, "param2");
        Assert.assertEquals (param.getName (), "param2");
        Assert.assertEquals (param.getValue (), "two");
        Assert.assertEquals (param.getDescription (), "parameter 2");

        EasyMock.verify (mockGenericDao);

    }

    /**
     * Test key not found.
     */
    @Test
    public void testParamNotFound ()
    {
        // Activate the mock generic dao
        EasyMock.expect (mockGenericDao.query (IGlobalParameter.class)).andReturn (result);
        EasyMock.replay (mockGenericDao);

        // Get some values
        final IGlobalParameter param = cache.getValue (IGlobalParameter.class, "dont_exist");
        Assert.assertNull (param);

        EasyMock.verify (mockGenericDao);

    }

    /**
     * Test uncache works.
     */
    @Test
    public void testUncache ()
    {

        // Activate the mock generic dao
        EasyMock.expect (mockGenericDao.query (IGlobalParameter.class)).andReturn (result);
        EasyMock.replay (mockGenericDao);

        // Get some values to prove the cache is not empty
        final IGlobalParameter param = cache.getValue (IGlobalParameter.class, "param1");
        Assert.assertEquals (param.getName (), "param1");
        Assert.assertEquals (param.getValue (), "one");
        Assert.assertEquals (param.getDescription (), "parameter 1");

        cache.uncache ();

        Assert.assertEquals (cache.getGlobalParameters ().size (), 0);

        EasyMock.verify (mockGenericDao);

    }

}
