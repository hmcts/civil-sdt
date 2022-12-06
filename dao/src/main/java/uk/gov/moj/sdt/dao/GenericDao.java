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
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.domain.api.IDomainObject;
import uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean;

import java.lang.reflect.Array;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Implements generic DAO functionality based on {@link IGenericDao} allowing a single DAO class to provide access to
 * all database entities. The DAO an object tree of domain objects. Business logic implemented in service layer acts
 * upon domain objects returned by JPA. These are translated into JAXB objects before being returned to the SOAP
 * layer.
 *
 * <p>
 * Subclasses of this class may build upon this to provide additional functionality encapsulating custom queries and
 * custom SQLs.
 * </p>
 *
 * @author Robin Compston
 */
// @Transactional (propagation = Propagation.MANDATORY)
// @Transactional
public class GenericDao<DT extends IDomainObject> implements IGenericDao {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericDao.class);

    private final CrudRepository<DT, Long> crudRepository;

    private EntityManager entityManager;

    private LongSupplier longSupplier = new LongSupplier() {

        Long value = 0L;

        @Override
        public long getAsLong() {
            return value++;
        }
    };

    public GenericDao(final CrudRepository<DT, Long> crudRepository) {
        super();
        this.crudRepository = crudRepository;
    }

    /**
     * Default constructor for {@link GenericDao}.
     */
    public GenericDao(final CrudRepository<DT, Long> crudRepository,
                      EntityManager entityManager) {
        super();
        this.crudRepository = crudRepository;
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public <DomainType extends IDomainObject> DomainType fetch(final Class<DomainType> domainType, final long id)
            throws DataAccessException {
        // Record start time.
        final long startTime = new GregorianCalendar().getTimeInMillis();

        LOGGER.debug("fetch(): domainType={}, id=", domainType, id);

        // Domain object of type asked for by caller.
        DomainType domainObject = null;

        // Get unique result using criteria and assign to domain object. This will be a tree of objects up to lazy
        // boundaries
        final Optional<DT> value = this.crudRepository.findById(id);
        if (value.isPresent()) {
            domainObject = domainType.cast(value.get());
        }

        // Calculate time in JPA/database.
        final long endTime = new GregorianCalendar().getTimeInMillis();
        SdtMetricsMBean.getMetrics().addDatabaseReadsTime(endTime - startTime);
        SdtMetricsMBean.getMetrics().upDatabaseReadsCount();

        // Validate results.
        if (domainObject == null) {
            return null;
        }

        if (domainObject.getId() != id) {
            throw new IllegalStateException();
        }

        return domainObject;
    }

    public <DomainType extends IDomainObject> DomainType[] query(final Class<DomainType> domainType) throws DataAccessException {
        return query(domainType, () -> createSelectQuery(domainType));
    }

    @Override
    public final <DomainType extends IDomainObject> DomainType[] query(final Class<DomainType> domainType,
                                                                       Supplier<CriteriaQuery<DomainType>> criteriaQuerySupplier)
            throws DataAccessException {
        LOGGER.debug("query(): domainType={}", domainType);

        // Get a list of results from JPA.
        final List<?> domainObjects = queryAsList(domainType, criteriaQuerySupplier);

        @SuppressWarnings("unchecked") final DomainType[] results =
                (DomainType[]) Array.newInstance(domainType, domainObjects.size());

        return domainObjects.toArray(results);
    }

    @Override
    public final <DomainType extends IDomainObject> List<DomainType> queryAsList(final Class<DomainType> domainType,
                                                                                 Supplier<CriteriaQuery<DomainType>> criteriaQuerySupplier)
            throws DataAccessException {
        // Record start time.
        final long startTime = new GregorianCalendar().getTimeInMillis();

        LOGGER.debug("queryAsList(): domainType={}", domainType);

        TypedQuery<DomainType> typedQuery = getEntityManager().createQuery(criteriaQuerySupplier.get());

        // Get a list of results from JPA.
        @SuppressWarnings("unchecked") final List<DomainType> domainObjects = typedQuery.getResultList();

        // Calculate time in JPA/database.
        final long endTime = new GregorianCalendar().getTimeInMillis();
        SdtMetricsMBean.getMetrics().addDatabaseReadsTime(endTime - startTime);
        SdtMetricsMBean.getMetrics().upDatabaseReadsCount();

        return domainObjects;
    }

    public <T extends IDomainObject> void persist(final IDomainObject domainObject,
                                                  CrudRepository<IDomainObject, Long> repository) throws DataAccessException {
        repository.save(domainObject);
    }

    @Override
    public void persist(final Object domainObject) throws DataAccessException {
        // Record start time.
        final long startTime = new GregorianCalendar().getTimeInMillis();

        LOGGER.debug("Persist domain object " + domainObject.toString());

        crudRepository.save((DT) domainObject);

        // Calculate time in JPA/database.
        final long endTime = new GregorianCalendar().getTimeInMillis();
        SdtMetricsMBean.getMetrics().addDatabaseWritesTime(endTime - startTime);
        SdtMetricsMBean.getMetrics().upDatabaseWritesCount();
    }

    @Override
    public <DomainType extends IDomainObject> void persistBulk(final List<DomainType> domainObjectList)
            throws DataAccessException {
        // Record start time.
        final long startTime = new GregorianCalendar().getTimeInMillis();

        LOGGER.debug("Persist domain object list {}", domainObjectList.toString());

        int i = 1;
        final int maxBatchSize = 20;
        for (Object domainObject : domainObjectList) {
            crudRepository.save((DT) domainObject);
            if (i % maxBatchSize == 0) {
                getEntityManager().flush();
            }
            i++;
        }

        // Calculate time in JPA/database.
        final long endTime = new GregorianCalendar().getTimeInMillis();
        SdtMetricsMBean.getMetrics().addDatabaseWritesTime(endTime - startTime);
        SdtMetricsMBean.getMetrics().upDatabaseWritesCount();
    }

    @Override
    public long getNextSequenceValue(final String sequenceName) throws DataAccessException {
        // Record start time.
        final long startTime = new GregorianCalendar().getTimeInMillis();

        LOGGER.debug("Sequence generation for " + sequenceName);

        if (sequenceName == null || sequenceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid sequence name");
        }

//        Long result = Long.parseLong(getEntityManager().createNativeQuery(String.format("SELECT nextval('%s')", sequenceName))
//                                         .getSingleResult().toString());

        Long result = longSupplier.getAsLong();

        // Calculate time in JPA/database.
        final long endTime = new GregorianCalendar().getTimeInMillis();
        SdtMetricsMBean.getMetrics().addDatabaseReadsTime(endTime - startTime);
        SdtMetricsMBean.getMetrics().upDatabaseReadsCount();

        return result;
    }

    public <DomainType extends IDomainObject> DomainType uniqueResult(final Class<DomainType> domainType,
                                                                      Supplier<CriteriaQuery<DomainType>> criteriaQuerySupplier) {

        final long startTime = new GregorianCalendar().getTimeInMillis();
        LOGGER.debug("uniqueResult(): domainType={}", domainType);

        TypedQuery<DomainType> typedQuery = getEntityManager().createQuery(criteriaQuerySupplier.get());
        // Get unique result from JPA.
        final DomainType domainObject = typedQuery.getSingleResult();

        // Calculate time in JPA/database.
        final long endTime = new GregorianCalendar().getTimeInMillis();
        SdtMetricsMBean.getMetrics().addDatabaseReadsTime(endTime - startTime);
        SdtMetricsMBean.getMetrics().upDatabaseReadsCount();

        return domainObject;
    }

    @Override
    public <DomainType extends IDomainObject> long queryAsCount(final Class<DomainType> domainType,
                                                                Supplier<CriteriaQuery<DomainType>> criteriaQuerySupplier) {
        // Record start time.
        final long startTime = new GregorianCalendar().getTimeInMillis();

        LOGGER.debug("queryAsCount(): domainType={}", domainType);

        TypedQuery<DomainType> query = getEntityManager().createQuery(criteriaQuerySupplier.get());
        // Get unique result from JPA.
        final Number countOfObjects = query.getResultList().size();

        // Calculate time in JPA/database.
        final long endTime = new GregorianCalendar().getTimeInMillis();
        SdtMetricsMBean.getMetrics().addDatabaseReadsTime(endTime - startTime);
        SdtMetricsMBean.getMetrics().upDatabaseReadsCount();

        return countOfObjects != null ? countOfObjects.longValue() : 0;
    }

    private <DomainType extends IDomainObject> CriteriaQuery<DomainType> createSelectQuery(Class<DomainType> domainType) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DomainType> criteriaQuery = criteriaBuilder.createQuery(domainType);
        Root<DomainType> root = criteriaQuery.from(domainType);
        return criteriaQuery.select(root);
    }
}
