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
package uk.gov.moj.sdt.dao.api;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.springframework.dao.DataAccessException;

import uk.gov.moj.sdt.domain.api.IDomainObject;

/**
 * core interface of GenericDao.
 * 
 * @see IGenericDaoSupport
 * 
 * @author David Turner
 */
public interface IGenericDao
{
    /**
     * Loads a business domain entity model hierarchy as of the given
     * businessTime and using the given id.
     * 
     * <p>
     * This method loads the persistent entity representing the given domainType from the database assuming the given
     * businessTime. The resultant persistent entity is then transformed into the business domain entity. The
     * transformation is done deeply meaning that all associations are followed, however it stops at lazy load
     * boundaries, regardless of whether the objects referenced got loaded already.
     * </p>
     * 
     * @param <DomainType> of entity to load
     * @param domainType
     *            of entity to load.
     * @param id
     *            of entity to load.
     * @return business domain entity model hierarchy root object.
     * 
     * @throws DataAccessException
     *             on any I/O related error.
     */
    <DomainType extends IDomainObject> DomainType fetch (final Class<DomainType> domainType, final long id)
        throws DataAccessException;

    // /**
    // * Continues reading data from the database at the point of one or more
    // properties that were lazily loaded by a
    // * previous fetch. Associates these retrieved properties with the domain
    // entity object to which they belong and
    // then
    // * returns that domain object with its now fetched properties. It takes a
    // zero or more property names which are
    // the
    // * properties to be eagerly fatched. It does this at a particular point of
    // time.
    // *
    // * <p>
    // * This method continues reading an object graph (retrieved by fetch() of
    // a particular id) at a lazy load
    // position.
    // * Client code calls this to force loading an association that was marked
    // as lazy. The method follows the
    // * association given by property and continues construction of the object
    // graph up to lazy load boundaries.
    // * </p>
    // *
    // * This does the same thing as fetch() above, but looks up the objects to
    // be processed by following the property
    // * on the persistent entity corresponding to domainObject.
    // *
    // * @param <DomainType> of entity to load.
    // * @param properties of domainObject to load.
    // * @param domainObject to load.
    // * @return very same domainObject reference, for convenience only.
    // *
    // * @throws DataAccessException on any I/O related error.
    // */
    // <DomainType extends IDomainObject> DomainType fetch (final DomainType
    // domainObject, final String... properties)
    // throws DataAccessException;

    /**
     * Loads an array of domain object model hierarchies with a set of
     * restrictions. This constructs a Hibernate query from the domainType and
     * restrictions given and uses Session.createCriteria() to retrieve the
     * persistent entities. Then the query is executed.
     * 
     * <p>
     * This can be used to load a user by name, for example.
     * </p>
     * 
     * @param <DomainType> of entity to load.
     * @param domainType of entity to load.
     * @param restrictions of the entities to load.
     * @return business domain entity model hierarchy root object.
     * 
     * @throws DataAccessException
     *             on any I/O related error.
     */
    <DomainType extends IDomainObject> DomainType[] query (final Class<DomainType> domainType,
                                                           final Criterion... restrictions) throws DataAccessException;

    /**
     * Returns a list of domain object model hierarchies with a set of
     * restrictions. This constructs a Hibernate query from the domainType and
     * restrictions given and uses Session.createCriteria() to retrieve the
     * persistent entities. Then the query is executed.
     * 
     * <p>
     * This can be used to load a user by name, for example.
     * </p>
     * 
     * @param <DomainType> of entity to load.
     * @param domainType of entity to load.
     * @param restrictions of the entities to load.
     * @return business domain entity model hierarchy root object.
     * 
     * @throws DataAccessException
     *             on any I/O related error.
     */
    <DomainType extends IDomainObject> List<DomainType> queryAsList (final Class<DomainType> domainType,
                                                                     final Criterion... restrictions);

    /**
     * Returns a list of domain object model hierarchies with criteria prepared by the specific DAO. Then the query is
     * executed.
     * 
     * @param <DomainType> of entity to load.
     * @param domainType of entity to load.
     * @param criteria the criteria to use in the query.
     * @return business domain entity model hierarchy root object.
     * 
     * @throws DataAccessException
     *             on any I/O related error.
     */
    <DomainType extends IDomainObject> DomainType uniqueResult (final Class<DomainType> domainType,
                                                                final Criteria criteria);

    /**
     * Returns unique domain object model hierarchy with a set of
     * restrictions. This constructs a Hibernate query from the domainType and
     * restrictions given and uses Session.createCriteria() to retrieve the
     * persistent entity. Then the query is executed.
     * 
     * <p>
     * This can be used to load a user by name, for example.
     * </p>
     * 
     * @param <DomainType> of entity to load.
     * @param domainType of entity to load.
     * @param restrictions of the entities to load.
     * @return business domain entity model hierarchy root object.
     * 
     * @throws DataAccessException
     *             on any I/O related error.
     */
    <DomainType extends IDomainObject> DomainType uniqueResult (final Class<DomainType> domainType,
                                                                final Criterion... restrictions);

    /**
     * Stores a domainObject in the database, either doing an insert or an
     * update.
     * 
     * <p>
     * This method persists or updates an object depending upon the presence of an id (if there is one it is an update
     * operation).
     * </p>
     * 
     * <p>
     * Since the passed in object will be persisted its immediate state after this method finishes will be to have an id
     * either assigned as part of a insert operation or its previous value if this is an update.
     * </p>
     * 
     * @param domainObject
     *            instance to insert or update.
     * 
     * @throws DataAccessException
     *             on any I/O related error.
     * 
     * @see #persist(IDomainObject)
     */
    void persist (final Object domainObject) throws DataAccessException;

    /**
     * Stores a list of domainObject in the database, either doing an bulk insert or an
     * bulk update.
     * 
     * <p>
     * This method persists or updates list of object(s) depending upon the presence of an id (if there is one it is an
     * update operation).
     * </p>
     * 
     * <p>
     * Since the passed in object will be persisted its immediate state after this method finishes will be to have an id
     * either assigned as part of a insert operation or its previous value if this is an update.
     * </p>
     * 
     * @param <DomainType> of entity to persist.
     * @param domainObjectList the list of instance to insert or update.
     * @throws DataAccessException on any I/O related error.
     */
    <DomainType extends IDomainObject> void persistBulk (final List<DomainType> domainObjectList)
        throws DataAccessException;

    /**
     * This method returns a sequence number associated with the sequence name.
     * 
     * @param sequenceName The name of the sequence
     * @return the sequence number
     * @throws DataAccessException on any I/O related error
     */
    long getNextSequenceValue (final String sequenceName) throws DataAccessException;

    /**
     * Returns a count of domain object model hierarchies with a set of
     * restrictions. This constructs a Hibernate query from the domainType and
     * restrictions given and uses Session.createCriteria() to retrieve the
     * persistent entities. Then the Projection of row count is applied on the criteria
     * to return an unique count of the domain objects.
     * 
     * <p>
     * This can be used where there is no use for the actual objects, for example.
     * </p>
     * 
     * @param <DomainType> of entity to load.
     * @param domainType of entity to load.
     * @param restrictions of the entities to load.
     * @return the unique count of the domain object heirarchy matching the criteria
     * 
     * @throws DataAccessException
     *             on any I/O related error.
     */
    <DomainType extends IDomainObject> long queryAsCount (final Class<DomainType> domainType,
                                                          final Criterion... restrictions);

}