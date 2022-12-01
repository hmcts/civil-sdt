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

package uk.gov.moj.sdt.producers.comx.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import org.springframework.stereotype.Repository;
import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.ServiceRequest;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IDomainObject;
import uk.gov.moj.sdt.domain.api.IServiceRequest;

/**
 * Base class for Mock DAO classes containing helper methods for mock dao sub classes.
 *
 * Contains a static list of data for testing with the commissioning system.
 *
 * @author d130680
 *
 */
@Repository("MockGenericDao")
public class MockGenericDao implements IGenericDao
{
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (MockGenericDao.class);

    /**
     * Incrementing unique value for service request id, simulating value returned by Hibernate.
     */
    private static Long nextServiceRequestId = 0L;

    /**
     * Pre-defined values for valid customer references.
     */
    private static final List<String> DUPLICATE_REFERENCE;

    static
    {

        // Invalid customer reference
        DUPLICATE_REFERENCE = new ArrayList<String> ();
        DUPLICATE_REFERENCE.add ("duplicate");

    }

    /**
     * Check the customer reference is valid for a bulk submission against a static list.
     *
     * @param customerReference customer reference
     *
     * @return BulkSubmission if not valid null otherwise
     */
    protected IBulkSubmission getBulkSubmission (final String customerReference)
    {
        if (DUPLICATE_REFERENCE.contains (customerReference.toLowerCase ()))
        {
            final IBulkSubmission bulkSubmission = new BulkSubmission ();
            bulkSubmission.setCustomerReference (customerReference.toLowerCase ());
            bulkSubmission.setSdtBulkReference ("MCOL_20130722000000_B00000001");
            // CHECKSTYLE:OFF
            bulkSubmission.setCreatedDate (LocalDateTime.of (2013, 7, 22, 13, 0));
            // CHECKSTYLE:ON
            return bulkSubmission;
        }
        else
        {
            return null;
        }
    }

    @Override
    public <DomainType extends IDomainObject> DomainType fetch (final Class<DomainType> domainType, final long id)
        throws DataAccessException
    {
        // Create dummy service request to satisfy logging of outbound request.
        if (IServiceRequest.class.isAssignableFrom (domainType))
        {
            final ServiceRequest serviceRequest = new ServiceRequest ();
            serviceRequest.setId (id);
            return domainType.cast (serviceRequest);
        }

        return null;
    }

    @Override
    public <DomainType extends IDomainObject> DomainType[] query (final Class<DomainType> domainType,
                                                                  final Criterion... restrictions)
        throws DataAccessException
    {
        return null;
    }

    @Override
    public void persist (final Object domainObject) throws DataAccessException
    {
        if (IServiceRequest.class.isAssignableFrom (domainObject.getClass ()))
        {
            final IServiceRequest serviceRequest = IServiceRequest.class.cast (domainObject);

            // Fake the request id if not set.
            if (serviceRequest.getId () == null)
            {
                serviceRequest.setId (++nextServiceRequestId);
            }

            if (LOGGER.isWarnEnabled ())
            {
                // Note that log level is unusually set to warn to force this message to be logged under normal logging
                // configuration as required by NFR which says we should log incoming and outgoing messages on SDT
                // Commissioning. Since cannot log to the database, we log instead to the log4j file.
                LOGGER.warn ("SDT Commissioning: service request id [" + serviceRequest.getId () +
                        "], bulk customer [" + serviceRequest.getBulkCustomerId () + "], bulk reference [" +
                        serviceRequest.getBulkReference () + "], request type [" + serviceRequest.getRequestType () +
                        "], hostname [" + serviceRequest.getServerHostName () + "], request payload [" +
                        serviceRequest.getRequestPayload () + "], response payload [" +
                        serviceRequest.getResponsePayload () + "]");
            }
        }
    }

    @Override
    public long getNextSequenceValue (final String sequenceName) throws DataAccessException
    {
        return 0;
    }

    @Override
    public <DomainType extends IDomainObject> List<DomainType> queryAsList (final Class<DomainType> domainType,
                                                                            final Criterion... restrictions)
    {
        return null;
    }

    @Override
    public <DomainType extends IDomainObject> DomainType uniqueResult (final Class<DomainType> domainType,
                                                                       final Criterion... restrictions)
    {
        return null;
    }

    @Override
    public <DomainType extends IDomainObject> DomainType uniqueResult (final Class<DomainType> domainType,
                                                                       final Criteria criteria)
    {
        return null;
    }

    @Override
    public <DomainType extends IDomainObject> long queryAsCount (final Class<DomainType> domainType,
                                                                 final Criterion... restrictions)
    {
        return 0;
    }

    @Override
    public void persistBulk (@SuppressWarnings ("rawtypes") final List domainObjectList) throws DataAccessException
    {

    }

}
