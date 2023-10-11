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
package uk.gov.moj.sdt.services.cache;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.moj.sdt.dao.GlobalParametersDao;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.services.config.ServicesTestConfig;
import uk.gov.moj.sdt.test.utils.AbstractIntegrationTest;
import uk.gov.moj.sdt.test.utils.TestConfig;
import uk.gov.moj.sdt.utils.mbeans.api.ISdtManagementMBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Integration test for the Global Parameters Cache.
 *
 * @author Manoj Kulkarni
 */
@ActiveProfiles("integ")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestConfig.class, ServicesTestConfig.class})
@Sql(scripts = {"classpath:uk/gov/moj/sdt/services/sql/RefData.sql", "classpath:uk/gov/moj/sdt/services/sql/GlobalParametersCacheIntTest.sql"})
@Transactional
public class GlobalParametersCacheIntTest extends AbstractIntegrationTest {
    /**
     * Test subject.
     */
    @Autowired
    @Qualifier("GlobalParametersCache")
    private ICacheable globalParameterCache;

    @Autowired
    @Qualifier("SdtManagementMBean")
    private ISdtManagementMBean sdtManagementMBean;

    @Autowired
    private GlobalParametersDao globalParametersDao;

    /**
     * Test getValue method where parameter is found.
     */
    @Test
    public void testGetValueSuccess() {
        final IGlobalParameter globalParameter =
                globalParameterCache.getValue(IGlobalParameter.class,
                        IGlobalParameter.ParameterKey.MAX_FORWARDING_ATTEMPTS.name());

        assertNotNull(globalParameter);
        assertEquals(IGlobalParameter.ParameterKey.MAX_FORWARDING_ATTEMPTS.name(),
                     globalParameter.getName(),
                     "Wrong global parameter name retrieved for " + IGlobalParameter.ParameterKey.MAX_FORWARDING_ATTEMPTS.name());
        assertEquals("3",
                     globalParameter.getValue(),
                     "Wrong global parameter value retrieved for " + IGlobalParameter.ParameterKey.MAX_FORWARDING_ATTEMPTS.name());
    }

    /**
     * Test getValue method where parameter is not found.
     */
    @Test
    public void testGetValueNotFound() {
        // Negative test
        final IGlobalParameter globalParameter = globalParameterCache.getValue(IGlobalParameter.class, "NO_PARAMETER");

        assertNull(globalParameter);
    }

    /**
     * Test getValue method where parameter is found after uncache.
     */
    @Test
    public void testUncache() {
        // Try to retrieve the bean
        IGlobalParameter globalParameter =
                globalParameterCache.getValue(IGlobalParameter.class,
                        IGlobalParameter.ParameterKey.DATA_RETENTION_PERIOD.name());

        assertNotNull(globalParameter);
        assertEquals(IGlobalParameter.ParameterKey.DATA_RETENTION_PERIOD.name(),
                     globalParameter.getName(),
                     "Wrong global parameter name retrieved for " + IGlobalParameter.ParameterKey.DATA_RETENTION_PERIOD.name());
        assertEquals("90",
                     globalParameter.getValue(),
                     "Wrong global parameter value retrieved for " + IGlobalParameter.ParameterKey.DATA_RETENTION_PERIOD.name());

        // Do uncache operation.
        sdtManagementMBean.uncache();

        // Change the value in the database.
        globalParameter.setValue("91");
        globalParametersDao.persist(globalParameter);

        // Try to retrieve the bean again with new value.
        globalParameter = null;
        globalParameter =
                globalParameterCache.getValue(IGlobalParameter.class,
                        IGlobalParameter.ParameterKey.DATA_RETENTION_PERIOD.name());

        assertNotNull(globalParameter);
        assertEquals(IGlobalParameter.ParameterKey.DATA_RETENTION_PERIOD.name(),
                     globalParameter.getName(),
                     "Wrong global parameter name retrieved for " + IGlobalParameter.ParameterKey.DATA_RETENTION_PERIOD.name());
        assertEquals("91",
                     globalParameter.getValue(),
                     "Wrong global parameter value retrieved for " + IGlobalParameter.ParameterKey.DATA_RETENTION_PERIOD.name());
    }
}
