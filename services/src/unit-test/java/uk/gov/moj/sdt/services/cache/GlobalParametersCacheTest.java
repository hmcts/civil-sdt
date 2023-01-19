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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.domain.GlobalParameter;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.mbeans.api.ISdtManagementMBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for global parameters cache.
 *
 * @author d130680
 */
@ExtendWith(MockitoExtension.class)
class GlobalParametersCacheTest extends AbstractSdtUnitTestBase {

    /**
     * Generic dao.
     */
    @Mock
    private IGenericDao mockGenericDao;

    /**
     * Global parameters cache.
     */
    private GlobalParametersCache cache;

    /**
     * Dao query results.
     */
    private GlobalParameter[] result;

    private static final String PARAM_1 = "param1";
    private static final String PARAM_2 = "param2";
    private static final String PARAM_3 = "param3";
    private static final String PARAMETER_1 = "parameter 1";
    private ISdtManagementMBean managementMBean;

    /**
     * Setup mock objects and inject DAO dependencies into our class.
     */
    @BeforeEach
    @Override
    public void setUp() {
        managementMBean = Mockito.mock(ISdtManagementMBean.class);
        cache = new GlobalParametersCache(managementMBean, mockGenericDao);

        // Setup some results
        result = new GlobalParameter[3];
        result[0] = new GlobalParameter();
        result[0].setName(PARAM_1);
        result[0].setValue("one");
        result[0].setDescription(PARAMETER_1);
        result[1] = new GlobalParameter();
        result[1].setName(PARAM_2);
        result[1].setValue("two");
        result[1].setDescription("parameter 2");
        result[2] = new GlobalParameter();
        result[2].setName(PARAM_3);
        result[2].setValue("three");
        result[2].setDescription("parameter 3");
    }

    /**
     * Test the getValue method.
     */
    @Test
    void testGetErrorMessage() {

        // Activate the mock generic dao
        when(mockGenericDao.query(GlobalParameter.class)).thenReturn(result);

        // Get some values
        IGlobalParameter param = cache.getValue(IGlobalParameter.class, PARAM_1);
        assertEquals(PARAM_1, param.getName());
        assertEquals("one", param.getValue());
        assertEquals(PARAMETER_1, param.getDescription());

        param = cache.getValue(IGlobalParameter.class, PARAM_3);
        assertEquals(PARAM_3, param.getName());
        assertEquals("three", param.getValue());
        assertEquals("parameter 3", param.getDescription());

        param = cache.getValue(IGlobalParameter.class, PARAM_2);
        assertEquals(PARAM_2, param.getName());
        assertEquals("two", param.getValue());
        assertEquals("parameter 2", param.getDescription());

        verify(mockGenericDao).query(GlobalParameter.class);
    }

    /**
     * Test key not found.
     */
    @Test
    void testParamNotFound() {
        // Activate the mock generic dao
        when(mockGenericDao.query(GlobalParameter.class)).thenReturn(result);

        // Get some values
        final IGlobalParameter param = cache.getValue(IGlobalParameter.class, "dont_exist");
        assertNull(param);

        verify(mockGenericDao).query(GlobalParameter.class);
        verify(managementMBean).getCacheResetControl();
    }

    /**
     * Test uncache works.
     */
    @Test
    void testUncache() {

        // Activate the mock generic dao
        when(mockGenericDao.query(GlobalParameter.class)).thenReturn(result);

        // Get some values to prove the cache is not empty
        final IGlobalParameter param = cache.getValue(IGlobalParameter.class, PARAM_1);
        assertEquals(PARAM_1, param.getName());
        assertEquals("one", param.getValue());
        assertEquals(PARAMETER_1, param.getDescription());

        cache.uncache();

        assertEquals(0, cache.getGlobalParameters().size());

        verify(mockGenericDao).query(GlobalParameter.class);
    }

}
