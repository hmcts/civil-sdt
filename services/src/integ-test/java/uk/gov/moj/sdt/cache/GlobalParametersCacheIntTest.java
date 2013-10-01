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
package uk.gov.moj.sdt.cache;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.test.util.DBUnitUtility;

/**
 * Integration test for the Global Parameters Cache.
 * 
 * @author Manoj Kulkarni
 * 
 */
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (locations = {"classpath*:**/applicationContext.xml", "/uk/gov/moj/sdt/dao/spring.context.xml",
        "classpath*:/**/spring*.xml", "/uk/gov/moj/sdt/dao/spring*.xml"})
public class GlobalParametersCacheIntTest extends AbstractTransactionalJUnit4SpringContextTests
{
    /**
     * Logger object.
     */
    private static final Logger LOG = LoggerFactory.getLogger (GlobalParametersCacheIntTest.class);

    /**
     * Test subject.
     */
    private ICacheable globalParameterCache;

    /**
     * Setup the test.
     */
    @Before
    public void setUp ()
    {
        DBUnitUtility.loadDatabase (this.getClass (), true);
        globalParameterCache =
                (ICacheable) this.applicationContext.getBean ("uk.gov.moj.sdt.cache.api.IGlobalParametersCache");

    }

    /**
     * Test getValue method where parameter is found.
     */
    @Test
    public void testGetValueSuccess ()
    {
        final IGlobalParameter globalParameter =
                globalParameterCache.getValue (IGlobalParameter.class,
                        IGlobalParameter.ParameterKey.DATA_RETENTION_PERIOD.name ());

        assertNotNull (globalParameter);

        assertTrue (globalParameter.getName ().equals (IGlobalParameter.ParameterKey.DATA_RETENTION_PERIOD.name ()));

        LOG.debug ("Global Parameter value is " + globalParameter.getValue ());
    }

    /**
     * Test getValue method where parameter is not found.
     */
    @Test
    public void testGetValueNotFound ()
    {
        // Negative test
        final IGlobalParameter globalParameter = globalParameterCache.getValue (IGlobalParameter.class, "NO_PARAMETER");

        assertNull (globalParameter);
    }

}
