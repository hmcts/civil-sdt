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
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.domain.api.IDomainObject;
import uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean;

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
// @Transactional
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
     * Default constructor for {@link GenericDao}.
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
    public <DomainType extends IDomainObject> DomainType fetch (final Class<DomainType> domainType, final long id)
        throws DataAccessException
    {
        // Record start time.
        final long startTime = new GregorianCalendar ().getTimeInMillis ();

        GenericDao.LOG.debug ("fetch(): domainType=" + domainType + ", id=" + id);

        // Domain object of type asked for by caller.
        DomainType domainObject = null;

        // Get active session.
        final Session session = this.getSessionFactory ().getCurrentSession ();

        // Setup criteria to retrieve given type of domain object using id.
        final Criteria criteria = session.createCriteria (domainType);
        criteria.add (Restrictions.eq ("id", id));

        // Update mbean stats.
        SdtMetricsMBean.getSdtMetrics ().upBulkSubmitCounts ();

        // Get unique result using criteria and assign to domain object. This will be a tree of objects up to lazy
        // boundaries
        final Object o = criteria.uniqueResult ();
        domainObject = domainType.cast (o);

        // Calculate time in hibernate/database.
        final long endTime = new GregorianCalendar ().getTimeInMillis ();
        SdtMetricsMBean.getSdtMetrics ().addDatabaseReadsTime (endTime - startTime);
        SdtMetricsMBean.getSdtMetrics ().upDatabaseReadsCount ();

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

    @Override
    public final <DomainType extends IDomainObject> DomainType[] query (final Class<DomainType> domainType,
                                                                        final Criterion... restrictions)
        throws DataAccessException
    {
        // Record start time.
        final long startTime = new GregorianCalendar ().getTimeInMillis ();

        GenericDao.LOG.debug ("query(): domainType=" + domainType);

        // Get a list of results from Hibernate.
        final List<?> domainObjects = queryAsList (domainType, restrictions);

        @SuppressWarnings ("unchecked") final DomainType[] results =
                (DomainType[]) Array.newInstance (domainType, domainObjects.size ());

        // Calculate time in hibernate/database.
        final long endTime = new GregorianCalendar ().getTimeInMillis ();
        SdtMetricsMBean.getSdtMetrics ().addDatabaseReadsTime (endTime - startTime);
        SdtMetricsMBean.getSdtMetrics ().upDatabaseReadsCount ();

        return domainObjects.toArray (results);
    }

    @Override
    public final <DomainType extends IDomainObject> List<DomainType> queryAsList (final Class<DomainType> domainType,
                                                                                  final Criterion... restrictions)
        throws DataAccessException
    {
        // Record start time.
        final long startTime = new GregorianCalendar ().getTimeInMillis ();

        GenericDao.LOG.debug ("queryAsList(): domainType=" + domainType);

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
        @SuppressWarnings ("unchecked") final List<DomainType> domainObjects = criteria.list ();

        // Calculate time in hibernate/database.
        final long endTime = new GregorianCalendar ().getTimeInMillis ();
        SdtMetricsMBean.getSdtMetrics ().addDatabaseReadsTime (endTime - startTime);
        SdtMetricsMBean.getSdtMetrics ().upDatabaseReadsCount ();

        return domainObjects;
    }

    @Override
    public void persist (final Object domainObject) throws DataAccessException
    {
        // Record start time.
        final long startTime = new GregorianCalendar ().getTimeInMillis ();

        final Session session = this.getSessionFactory ().getCurrentSession ();
        session.saveOrUpdate (domainObject);
        // Calculate time in hibernate/database.
        final long endTime = new GregorianCalendar ().getTimeInMillis ();
        SdtMetricsMBean.getSdtMetrics ().addDatabaseWritesTime (endTime - startTime);
        SdtMetricsMBean.getSdtMetrics ().upDatabaseWritesCount ();
    }

    @Override
    public long getNextSequenceValue (final String sequenceName) throws DataAccessException
    {
        // Record start time.
        final long startTime = new GregorianCalendar ().getTimeInMillis ();

        LOG.debug ("Sequence generation for " + sequenceName);

        if (sequenceName == null || sequenceName.trim ().isEmpty ())
        {
            throw new IllegalArgumentException ("Invalid sequence name");
        }

        final Session session = this.getSessionFactory ().getCurrentSession ();
        final Query query = session.createSQLQuery ("SELECT " + sequenceName + ".nextval FROM DUAL");
        final Object result = query.uniqueResult ();

        // Calculate time in hibernate/database.
        final long endTime = new GregorianCalendar ().getTimeInMillis ();
        SdtMetricsMBean.getSdtMetrics ().addDatabaseReadsTime (endTime - startTime);
        SdtMetricsMBean.getSdtMetrics ().upDatabaseReadsCount ();

        return ((BigDecimal) result).longValue ();
    }

    /**
     * Create a restriction for a date field that falls between two dates. We use the data retention period to work out
     * the start and end of the date.
     * 
     * @param field date field
     * @param dataRetentionPeriod data retention period
     * @return hibernate restriction
     */
    protected Criterion createDateRestriction (final String field, final int dataRetentionPeriod)
    {

        // Get today's date
        final Date today = new Date ();

        // Subtract the retention period from todays date and truncate the time part to get the floor of the date
        Date start = DateUtils.truncate (today, Calendar.DATE);
        start = DateUtils.addDays (start, dataRetentionPeriod * -1);

        // Truncate the time part then add 1 day to the ceiling of the date, i.e. the next day
        Date end = DateUtils.truncate (today, Calendar.DATE);
        end = DateUtils.addDays (end, 1);

        // Add date criteria and convert to LocalDateTime

        return Restrictions.between ("createdDate", LocalDateTime.fromDateFields (start),
                LocalDateTime.fromDateFields (end));

    }

    @Override
    public <DomainType extends IDomainObject> DomainType uniqueResult (final Class<DomainType> domainType,
                                                                       final Criterion... restrictions)
    {
        final long startTime = new GregorianCalendar ().getTimeInMillis ();

        GenericDao.LOG.debug ("uniqueResult(): domainType=" + domainType);

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

        // Get unique result from Hibernate.
        @SuppressWarnings ("unchecked") final DomainType domainObject = (DomainType) criteria.uniqueResult ();

        // Calculate time in hibernate/database.
        final long endTime = new GregorianCalendar ().getTimeInMillis ();
        SdtMetricsMBean.getSdtMetrics ().addDatabaseReadsTime (endTime - startTime);
        SdtMetricsMBean.getSdtMetrics ().upDatabaseReadsCount ();

        return domainObject;
    }
}
