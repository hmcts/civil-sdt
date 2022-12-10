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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.dao.repository.BulkCustomerRepository;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Implements specific DAO functionality based on {@link IBulkCustomerDao}. This is a derived DAO extending
 * {@link GenericDao} which provides generic JPA access. This specific DAO exists in order to construct domain
 * specific selections where column matches are needed on columns other than the id. For each domain specific query, it
 * constructs an array of JPA Predicate which are passed to the generic method
 *
 * @author Robin Compston
 */
@Component("BulkCustomerDao")
public class BulkCustomerDao extends GenericDao<BulkCustomer> implements IBulkCustomerDao {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BulkCustomerDao.class);

    private final Root<BulkCustomer> root;

    private final CriteriaBuilder criteriaBuilder;

    private final CriteriaQuery<BulkCustomer> criteriaQuery;

    @Autowired
    public BulkCustomerDao(final BulkCustomerRepository crudRepository,
                           EntityManager entityManager) {
        super(crudRepository, entityManager);
        criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery(BulkCustomer.class);
        root = criteriaQuery.from(BulkCustomer.class);
    }

    @Override
    public IBulkCustomer getBulkCustomerBySdtId(final long sdtCustomerId) throws DataAccessException {
        LOGGER.debug("Get bulk customer matching sdt customer id [{}]", sdtCustomerId);

        // Call the generic dao to perform this query.
        return this.uniqueResult(BulkCustomer.class, () -> {
            Predicate sdtCustomerPredicate = criteriaBuilder.equal(root.get("sdtCustomerId"), sdtCustomerId);
            return criteriaQuery.select(root).where(sdtCustomerPredicate);
        });
    }
}
