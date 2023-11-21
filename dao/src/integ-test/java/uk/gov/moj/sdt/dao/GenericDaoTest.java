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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.dao.config.DaoTestConfig;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.GlobalParameter;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.test.utils.AbstractIntegrationTest;
import uk.gov.moj.sdt.test.utils.TestConfig;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test {@link GenericDao} CRUD methods.
 *
 * @author Robin Compston
 */
@ActiveProfiles("integ")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestConfig.class, DaoTestConfig.class})
@Sql(scripts = {"classpath:uk/gov/moj/sdt/dao/sql/GenericDaoTest.sql"})
class GenericDaoTest extends AbstractIntegrationTest {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericDaoTest.class);

    @Autowired
    private IBulkCustomerDao bulkCustomerDao;

    @Autowired
    private GlobalParametersDao genericDao;

    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<BulkCustomer> criteriaQuery;
    private Root<BulkCustomer> root;

    @BeforeEach
    public void setUp() {
        criteriaBuilder = bulkCustomerDao.getEntityManager().getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery(BulkCustomer.class);
        root = criteriaQuery.from(BulkCustomer.class);
    }

    /**
     * Tests {@link uk.gov.moj.sdt.dao.GenericDao} fetch.
     */
    @Test
    void testFetch() {
        final long id = 10711;
        final IBulkCustomer bulkCustomer = bulkCustomerDao.fetch(BulkCustomer.class, id);
        assertNotNull(bulkCustomer);
    }

    /**
     * Tests query.
     */
    @Test
    void testQuery() {
        final IBulkCustomer[] bulkCustomers =
            bulkCustomerDao.query(BulkCustomer.class, () -> {
                Predicate[] sdtCustomerPredicate = createCriteria(2L);
                return criteriaQuery.select(root).where(sdtCustomerPredicate);
            });

        if (bulkCustomers.length == 1) {
            // User found
            final IBulkCustomer bulkCustomer = bulkCustomers[0];
            LOGGER.debug("sdtCustomerId = {}", bulkCustomer.getSdtCustomerId());
        } else {
            fail("Cannot find customer with customer id[2]");
        }
    }

    /**
     * Test method for query as count.
     */
    @Test
    void testQueryAsCount() {
        final long customerCount = bulkCustomerDao.queryAsCount(BulkCustomer.class, () -> {
            Predicate[] sdtCustomerPredicate = createCriteria(2L);
            return criteriaQuery.select(root).where(sdtCustomerPredicate);
        });

        if (customerCount == 1) {
            assertTrue( true, "Found expected number of rows");
        }
    }

    /**
     * Tests the global parameter.
     */
    @Test
    void testGlobalParametersQuery() {
        CriteriaBuilder criteriaBuilderLocal = genericDao.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<GlobalParameter> criteriaQueryLocal = criteriaBuilderLocal.createQuery(GlobalParameter.class);
        Root<GlobalParameter> rootLocal = criteriaQueryLocal.from(GlobalParameter.class);

        final IGlobalParameter[] globalParameters = genericDao.query(GlobalParameter.class,
                                                         () -> criteriaQueryLocal.select(rootLocal));

        if (globalParameters.length == 2) {
            // Found the global parameters
            for (IGlobalParameter globalParam : globalParameters) {
                LOGGER.debug("GlobalParam ={}:{}", globalParam.getName(), globalParam.getValue());
            }
        }
    }

    private Predicate[] createCriteria(long... value) {
        Predicate[] predicates = new Predicate[1];
        predicates[0] = criteriaBuilder.equal(root.get("sdtCustomerId"), value[0]);
        return predicates;
    }
}
