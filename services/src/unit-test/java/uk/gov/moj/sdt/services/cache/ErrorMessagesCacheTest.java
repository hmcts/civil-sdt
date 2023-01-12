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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
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
 */
@ExtendWith(MockitoExtension.class)
class ErrorMessagesCacheTest extends AbstractSdtUnitTestBase {

    /**
     * Generic dao.
     */
    @Mock
    private IGenericDao mockGenericDao;

    /**
     * Error messages cache.
     */
    private ErrorMessagesCache cache;

    /**
     * Dao query results.
     */
    private ErrorMessage[] result;

    private static final String ERROR_DESCRIPTION_1 = "errorDescription 1";
    private static final String ERROR_DESCRIPTION_2 = "errorDescription 2";
    private static final String ERROR_DESCRIPTION_3 = "errorDescription 3";

    /**
     * Setup mock objects and inject DAO dependencies into our class.
     */
    @BeforeEach
    @Override
    public void setUp() {
         MockitoAnnotations.openMocks(this);

        cache = new ErrorMessagesCache();
        cache.setGenericDao(mockGenericDao);

        ISdtManagementMBean managementMBean = new SdtManagementMBean();
        cache.setManagementMBean(managementMBean);

        // Setup some results
        result = new ErrorMessage[3];
        result[0] = new ErrorMessage();
        result[0].setErrorCode("1");
        result[0].setErrorDescription(ERROR_DESCRIPTION_1);
        result[1] = new ErrorMessage();
        result[1].setErrorCode("2");
        result[1].setErrorDescription(ERROR_DESCRIPTION_2);
        result[2] = new ErrorMessage();
        result[2].setErrorCode("3");
        result[2].setErrorDescription(ERROR_DESCRIPTION_3);
    }

    /**
     * Test the getValue method.
     */
    @Test
    void testGetErrorMessage() {

        // Activate the mock generic dao
        when(mockGenericDao.query(IErrorMessage.class)).thenReturn(result);

        // Get some values
        IErrorMessage errorMessage = cache.getValue(IErrorMessage.class, "1");
        assertEquals( "1", errorMessage.getErrorCode());
        assertEquals(ERROR_DESCRIPTION_1, errorMessage.getErrorDescription());

        errorMessage = cache.getValue(IErrorMessage.class, "3");
        assertEquals("3", errorMessage.getErrorCode());
        assertEquals(ERROR_DESCRIPTION_3, errorMessage.getErrorDescription());

        errorMessage = cache.getValue(IErrorMessage.class, "2");
        assertEquals("2", errorMessage.getErrorCode());
        assertEquals(ERROR_DESCRIPTION_2, errorMessage.getErrorDescription());

        verify(mockGenericDao).query(IErrorMessage.class);
    }

    /**
     * Test key not found.
     */
    @Test
    void testKeyNotFound() {

        // Activate the mock generic dao
        when(mockGenericDao.query(IErrorMessage.class)).thenReturn(result);

        IErrorMessage errorMessage = null;
        try {
            // Get some values
            errorMessage = cache.getValue(IErrorMessage.class, "dont_exist");
            fail("IllegalException should have been thrown");
        } catch (final IllegalStateException e) {
            assertNull(errorMessage);
        }

        verify(mockGenericDao).query(IErrorMessage.class);
    }

    /**
     * Test uncache works.
     */
    @Test
    void testUncache() {

        // Activate the mock generic dao
        when(mockGenericDao.query(IErrorMessage.class)).thenReturn(result);

        // Get some values to prove the cache is not empty
        final IErrorMessage errorMessage = cache.getValue(IErrorMessage.class, "1");
        assertEquals("1", errorMessage.getErrorCode());
        assertEquals(ERROR_DESCRIPTION_1, errorMessage.getErrorDescription());

        cache.uncache();

        assertEquals(0, cache.getErrorMessages().size());

        verify(mockGenericDao).query(IErrorMessage.class);
    }
}
