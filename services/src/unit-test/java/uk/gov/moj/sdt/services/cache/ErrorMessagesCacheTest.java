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
package uk.gov.moj.sdt.services.cache;

import static org.junit.Assert.fail;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.domain.ErrorMessage;
import uk.gov.moj.sdt.domain.api.IErrorMessage;
import uk.gov.moj.sdt.services.mbeans.SdtManagementMBean;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.mbeans.api.ISdtManagementMBean;

/**
 * Test class for error messages cache.
 * 
 * @author d130680
 * 
 */
public class ErrorMessagesCacheTest extends AbstractSdtUnitTestBase
{

    /**
     * Error messages cache.
     */
    private ErrorMessagesCache cache;

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
    private ErrorMessage[] result;

    /**
     * Setup mock objects and inject DAO dependencies into our class.
     * 
     */
    @Before
    public void setUp ()
    {
        cache = new ErrorMessagesCache ();
        mockGenericDao = EasyMock.createMock (IGenericDao.class);
        cache.setGenericDao (mockGenericDao);
        managementMBean = new SdtManagementMBean ();
        cache.setManagementMBean (managementMBean);

        // Setup some results
        result = new ErrorMessage[3];
        result[0] = new ErrorMessage ();
        result[0].setErrorCode ("1");
        result[0].setErrorDescription ("errorDescription 1");
        result[1] = new ErrorMessage ();
        result[1].setErrorCode ("2");
        result[1].setErrorDescription ("errorDescription 2");
        result[2] = new ErrorMessage ();
        result[2].setErrorCode ("3");
        result[2].setErrorDescription ("errorDescription 3");

    }

    /**
     * Test the getValue method.
     */
    @Test
    public void testGetErrorMessage ()
    {

        // Activate the mock generic dao
        EasyMock.expect (mockGenericDao.query (IErrorMessage.class)).andReturn (result);
        EasyMock.replay (mockGenericDao);

        // Get some values
        IErrorMessage errorMessage = cache.getValue (IErrorMessage.class, "1");
        Assert.assertEquals (errorMessage.getErrorCode (), "1");
        Assert.assertEquals (errorMessage.getErrorDescription (), "errorDescription 1");

        errorMessage = cache.getValue (IErrorMessage.class, "3");
        Assert.assertEquals (errorMessage.getErrorCode (), "3");
        Assert.assertEquals (errorMessage.getErrorDescription (), "errorDescription 3");

        errorMessage = cache.getValue (IErrorMessage.class, "2");
        Assert.assertEquals (errorMessage.getErrorCode (), "2");
        Assert.assertEquals (errorMessage.getErrorDescription (), "errorDescription 2");

        EasyMock.verify (mockGenericDao);

    }

    /**
     * Test key not found.
     */
    @Test
    public void testKeyNotFound ()
    {

        // Activate the mock generic dao
        EasyMock.expect (mockGenericDao.query (IErrorMessage.class)).andReturn (result);
        EasyMock.replay (mockGenericDao);

        IErrorMessage errorMessage = null;
        try
        {
            // Get some values
            errorMessage = cache.getValue (IErrorMessage.class, "dont_exist");
            fail ("IllegalException should have been thrown");
        }
        catch (final IllegalStateException e)
        {
            Assert.assertNull (errorMessage);
        }

        EasyMock.verify (mockGenericDao);

    }

    /**
     * Test uncache works.
     */
    @Test
    public void testUncache ()
    {

        // Activate the mock generic dao
        EasyMock.expect (mockGenericDao.query (IErrorMessage.class)).andReturn (result);
        EasyMock.replay (mockGenericDao);

        // Get some values to prove the cache is not empty
        final IErrorMessage errorMessage = cache.getValue (IErrorMessage.class, "1");
        Assert.assertEquals (errorMessage.getErrorCode (), "1");
        Assert.assertEquals (errorMessage.getErrorDescription (), "errorDescription 1");

        cache.uncache ();

        Assert.assertEquals (cache.getErrorMessages ().size (), 0);

        EasyMock.verify (mockGenericDao);

    }
}
