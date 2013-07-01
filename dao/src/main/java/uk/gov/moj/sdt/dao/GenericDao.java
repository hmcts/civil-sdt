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

import java.lang.reflect.Array;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.domain.api.IDomainObject;

/**
 * Implements generic DAO functionality based on {@link IGenericDao} allowing a single DAO class to provide access to
 * all database entities. The DAO an object tree of domain objects. Business logic implemented in service layer acts
 * upon domain objects returned by Hibernate. These are translated into JAXB objects before being returned to the SOAP
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
@Transactional 
public class GenericDao implements IGenericDao
{
    /**
     * Logger object.
     */
    private static final Logger LOG = LoggerFactory.getLogger (GenericDao.class);

    /**
     * The Hibernate session factory needed to acquire the session.
     */
    private SessionFactory sessionFactory;

    /**
     * Default constructor for {@link GenericDaoTest}.
     */
    public GenericDao ()
    {
        super ();
    }

    /**
     * Get the Hibernate session factory.
     * 
     * @return the Hibernate session factory.
     */
    public SessionFactory getSessionFactory ()
    {
        return this.sessionFactory;
    }

    /**
     * Set the Hibernate session factory.
     * 
     * @param sessionFactory the Hibernate session factory to be injected into this object.
     */
    public void setSessionFactory (final SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public <DomainType extends IDomainObject> DomainType fetch (final Class<DomainType> domainType, final int id)
        throws DataAccessException
    {
        GenericDao.LOG.debug ("fetch(): domainType=" + domainType + ", id=" + id);

        // Domain object of type asked for by caller.
        DomainType domainObject = null;

        // Get active session.
        final Session session = this.getSessionFactory ().getCurrentSession ();

        // Setup criteria to retrieve given type of domain object using id.
        final Criteria criteria = session.createCriteria (domainType);
        criteria.add (Restrictions.eq ("id", id));

        // Get unique result using criteria and assign to domain object. This will be a tree of objects up to lazy
        // boundaries
        final Object o = criteria.uniqueResult ();
        domainObject = domainType.cast (o);

        // Validate results.
        if (domainObject == null)
        {
            return null;
        }

        if (domainObject.getId () != id)
        {
            throw new IllegalStateException ();
        }

        return domainObject;
    }

    // // CHECKSTYLE:OFF Complexity is acceptable
    // @Override
    // public <DomainType extends IDomainObject> DomainType fetch (final DomainType domainObject,
    // final String... properties) throws DataAccessException
    // // CHECKSTYLE:ON
    // {
    // // Narrow the domain Object to define it as its particular domain type rather than a generic type.
    // final Class<? extends IDomainObject> domainType =
    // domainObject.getBusinessInterfaceType ().asSubclass (IDomainObject.class);
    //
    // GenericDaoTest.LOG
    // .debug ("fetch(): domainType=" + domainType + ", for properties={" + properties.toString () + "}");
    //
    // try
    // {
    // // Use session as key to retrieve transaction time.
    // final Session session = this.getSession (false);
    //
    // // Retrieve the doomain object to which eagerly fetched properties are to be attached. We expect this to
    // // already be in Hibernate session.
    // final DomainType sessionObject = (DomainType) (session.get (domainType, domainObject.getId ()));
    //
    // // Follow property links.
    // try
    // {
    // // Get the list of property names known to Hibernate (not using reflection, since Hibernate may not know
    // // about all the properties and we can only retrieve those which Hibernate can see).
    // final String[] propertNames =
    // this.getSession ().getSessionFactory ()
    // .getClassMetadata (domainObject.getClass ().getSimpleName ()).getPropertyNames ();
    //
    // // Eagerly fetch and attach each given property.
    // for (final String property : properties)
    // {
    // // Set the source of the property - rv or parent.
    // final IPropertySupport source = inRV ? rvEntity : ((IBitemporalRvEntity) rvEntity).getParent ();
    //
    // // Retrieve the property value - accessing it via Hibernate proxy will cause Hibernate to fetch
    // // it from database.
    // final Object value = source.getPropertyValue (property);
    // Object clone = this.transformToDomain (session, bsTime, value);
    //
    // // When domain does not expect a set and parent contains a set with just a single entry, convert
    // // set into an instance of the type within the set and store in domain object.
    // if ( !inRV && clone instanceof Set<?> &&
    // !domainObject.getPropertyType (property).isInstance (clone))
    // {
    // // Unwrap sets of parents to plain references in business objects.
    // final Set<?> set = (Set<?>) clone;
    // final Iterator<?> iterator = set.iterator ();
    // if ( !iterator.hasNext ())
    // {
    // clone = null;
    // }
    // else
    // {
    // clone = iterator.next ();
    // if (iterator.hasNext ())
    // {
    // throw new IllegalStateException ("expected single object, but got " + set.size ());
    // }
    // }
    // }
    // domainObject.setPropertyValue (property, clone);
    // }
    // }
    //
    // if (IRunBasedRvEntity.class.isAssignableFrom (rvClass))
    // {
    // // run-based code
    // @SuppressWarnings ("unchecked") final Class<? extends IRunBasedRvEntity> runbasedRvClass =
    // (Class<? extends IRunBasedRvEntity>) rvClass;
    //
    // // Use session as key to retrieve relevant runs.
    // final Session session = this.getSession (false);
    // final Collection<Long> runOids = this.getState (session).getRunOids ();
    //
    // // construct key to retrieve rv object
    // final Class<? extends IRunBasedParentEntity> runbasedParentClass =
    // this.getMeta ().getParentImplementationType (domainType, IRunBasedParentEntity.class);
    // final IRunBasedParentEntity parentKey = this.instantiate (session, domainObject, runbasedParentClass);
    // final IRunBasedRvEntity rvKey = runbasedRvClass.newInstance ();
    // rvKey.setParent (parentKey);
    // rvKey.setRunOid (domainObject.getRunOid ());
    //
    // // Retrieve the rv object to which eagerly fetched properties are to be attached. We expect this to
    // // already be in Hibernate session.
    // final IRunBasedRvEntity rvEntity =
    // runbasedRvClass.cast (session.get (runbasedRvClass.getSimpleName (), rvKey));
    // if (rvEntity.getBusinessObject () != domainObject)
    // {
    // if (rvEntity.getBusinessObject () != null)
    // {
    // throw new IllegalStateException ();
    // }
    // else
    // {
    // rvEntity.setBusinessObject (domainObject);
    // }
    // }
    //
    // // Follow property links.
    // Filter filter = null;
    // try
    // {
    // final String[] propertNames =
    // this.getSession ().getSessionFactory ().getClassMetadata (runbasedRvClass.getSimpleName ())
    // .getPropertyNames ();
    //
    // final Set<String> rvProperties = new HashSet<String> (Arrays.asList (propertNames));
    //
    // // Eagerly fetch and attach each given property.
    // for (final String property : properties)
    // {
    // // Check if property is in RV or in Parent and remove to ensure each property is only used once.
    // final boolean inRV = rvProperties.remove (property);
    //
    // // Setup the run-based filter on first encountering a property in the parent record.
    // if ( !inRV && filter == null)
    // {
    // // Enable filter restricting time of rv entity to be retrieved.
    // filter = session.enableFilter ("runRead");
    // filter.setParameterList ("runOids", runOids);
    // }
    //
    // // Set the source of the property - rv or parent.
    // final IPropertySupport source = inRV ? rvEntity : ((IBaseRvEntity) rvEntity).getParent ();
    //
    // // Retrieve the property value - accessing it via Hibernate proxy will cause Hibernate to fetch
    // // it from database.
    // final Object value = source.getPropertyValue (property);
    // Object clone = this.transformToDomain (session, businessTime, value);
    //
    // // When domain does not expect a set and parent contains a set with just a single entry, convert
    // // set into an instance of the type within the set and store in domain object.
    // if ( !inRV && clone instanceof Set<?> &&
    // !domainObject.getPropertyType (property).isInstance (clone))
    // {
    // // Unwrap sets of parents to plain references in business objects.
    // final Set<?> set = (Set<?>) clone;
    // final Iterator<?> iterator = set.iterator ();
    // if ( !iterator.hasNext ())
    // {
    // clone = null;
    // }
    // else
    // {
    // clone = iterator.next ();
    // if (iterator.hasNext ())
    // {
    // throw new IllegalStateException ("expected single object, but got " + set.size ());
    // }
    // }
    // }
    // domainObject.setPropertyValue (property, clone);
    // }
    // }
    // finally
    // {
    // if (filter != null)
    // {
    // // Disable filter limiting time of reference.
    // session.disableFilter (filter.getName ());
    // filter = null;
    // }
    // }
    // }
    // else
    // {
    // // unsupported entity type
    // throw new UnsupportedOperationException ();
    // }
    // return domainObject;
    // }
    // catch (final ClassNotFoundException x)
    // {
    // throw new UnsupportedOperationException (x);
    // }
    // catch (final IllegalAccessException x)
    // {
    // throw new UnsupportedOperationException (x);
    // }
    // catch (final java.lang.InstantiationException x)
    // {
    // throw new UnsupportedOperationException (x);
    // }
    // catch (final HibernateException x)
    // {
    // throw this.convertHibernateAccessException (x);
    // }
    // }

    @Override
    public final <DomainType extends IDomainObject> DomainType[] query (final Class<DomainType> domainType,
                                                                        final Criterion... restrictions)
        throws DataAccessException
    {
        final Session session = this.getSessionFactory ().getCurrentSession ();

        // Add any restrictions passed by caller.
        final Criteria criteria = session.createCriteria (domainType);
        for (final Criterion restriction : restrictions)
        {
            // Added condition to check for null, if restriction null then it should not be added to criteria.
            if (restriction != null)
            {
                criteria.add (restriction);
            }
        }

        // Get a list of results from Hibernate.
        final List<?> domainObjects = criteria.list ();

        @SuppressWarnings ("unchecked") final DomainType[] results =
                (DomainType[]) Array.newInstance (domainType, domainObjects.size ());

        return domainObjects.toArray (results);
    }

    // // CHECKSTYLE:OFF Complexity is acceptable
    // @Override
    // public <DomainType extends IDomainObject> DomainType delete (final DomainType domainObject)
    // throws DataAccessException
    // // CHECKSTYLE:ON
    // {
    // // Eventually a delete statement is issued in case the transaction time range does not include a range, notice
    // // this can happen if we persist and then delete an entity within a single transaction.
    // // Use session as key to retrieve transaction time.
    // final Session session = this.getSessionFactory ().getCurrentSession ();
    //
    // // Prepare to read rv class and setup bi-temporal restrictions.
    //
    // final Criteria criteria = session.createCriteria (domainObject.getClass ().getSimpleName ());
    // criteria.add (Restrictions.eq ("id", id));
    // criteria.add (Restrictions.le ("created", txTime));
    //
    // // Get back list of domain objects.
    // final List<?> domainObjects = criteria.list ();
    // if ( !domainObjects.isEmpty ())
    // {
    // // Check whether any record found was marked as expired already, these can not be modified
    // // anymore...
    // for (final Object entry : rvEntities)
    // {
    // final IBitemporalRvEntity rvEntity = (IBitemporalRvEntity) entry;
    // if (rvEntity.getExpired ().before (IEntity.FOREVER))
    // {
    // throw new IllegalStateException ();
    // }
    // }
    //
    // // Adjust times to mark expiration point in time, notice we might have to split time for the first
    // // and
    // // last record found. For splitting we need to introduce a new record. This can be done in two ways,
    // // either we split in transaction time or in business time. Splitting in transaction time would
    // // result
    // // in a new historic record when querying a specific point in business time. Since this record would
    // // have identical working data users might find this awkward to see in history. Thus, we split in
    // // business time rather than transaction time, if necessary.
    // // If validFrom and validTo have been changed to span many previous records, then entities fully
    // // contained within the range really need to be logically deleted. This is done in the loop. In this
    // // case both if-clauses do not execute and the subsequent update expires the existing contained
    // // record.
    // for (final Object entry : rvEntities)
    // {
    // final IBitemporalRvEntity rvEntity = (IBitemporalRvEntity) entry;
    // final Date rvValidFrom = rvEntity.getValidFrom ();
    // final Date rvValidTo = rvEntity.getValidTo ();
    //
    // // Existing entity matches given business time but precedes validFrom of new entity, therefore a
    // // new
    // // entity is needed to cover time between rvValidFrom and bsFrom.
    // if (rvValidFrom.before (bsFrom))
    // {
    // // split at the beginning
    // final IBitemporalRvEntity rvClone = this.duplicate (session, rvEntity);
    // rvClone.setRvOid (IEntity.NULL_ID);
    // rvClone.setValidTo (rvValidFrom);
    // session.save (rvClass.getSimpleName (), rvClone);
    // rvEntity.setValidFrom (rvClone.getValidTo ());
    // }
    // // Existing entity matches given business time but comes after validTo of new entity, therefore
    // // a
    // // new entity is needed to cover time between bsTo and rvValidTo.
    // if (rvValidTo.after (bsTo))
    // {
    // // split at the end
    // final IBitemporalRvEntity rvClone = this.duplicate (session, rvEntity);
    // rvClone.setRvOid (IEntity.NULL_ID);
    // rvClone.setValidFrom (rvValidTo);
    // session.save (rvClass.getSimpleName (), rvClone);
    // rvEntity.setValidTo (rvClone.getValidFrom ());
    // }
    // session.update (rvClass.getSimpleName (), rvEntity);
    // }
    // }
    //
    // return domainObject;
    // }

    @Override
    public <DomainType extends IDomainObject> DomainType insert (final DomainType domainObject)
        throws DataAccessException
    {
        final Session session = this.getSessionFactory ().getCurrentSession ();

        domainObject.setId ((Integer) session.save (domainObject.getClass ().getSimpleName (), domainObject));

        return domainObject;
    }

    /**
     * Stores changes made to an existing domainObject into the database.
     * 
     * @param <DomainType> of entity to update.
     * @param domainObject instance to update.
     * @return domainObject, with transaction times and (RV)OIDs adjusted.
     * 
     * @throws DataAccessException on any I/O related error.
     * 
     * @see #persist(IDomainObject)
     */
    // CHECKSTYLE:OFF Complexity is acceptable
    public <DomainType extends IDomainObject> DomainType update (final DomainType domainObject)
        throws DataAccessException
    // CHECKSTYLE:ON
    {
        final Session session = this.getSessionFactory ().getCurrentSession ();

        domainObject.setId ((Integer) session.save (domainObject.getClass ().getSimpleName (), domainObject));

        return domainObject;
    }
}
