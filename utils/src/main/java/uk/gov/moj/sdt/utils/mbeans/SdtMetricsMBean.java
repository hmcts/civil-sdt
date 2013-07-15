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
package uk.gov.moj.sdt.utils.mbeans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.utils.mbeans.api.ISdtMetricsMBean;

/**
 * A class to gather online metrics for SDT. This POJO is configured by Spring to be available as an MBean visible via
 * jconsole in the application server JVM.
 * 
 * The following metrics are available:
 * 
 * service calls counts of each type,
 * - times,
 * - avg,
 * - max,
 * - min,
 * domain objects count,
 * database calls count,
 * - times,
 * - avg,
 * - min,
 * - max,
 * customer counts,
 * bulk counts,
 * request counts,
 * numbers of complete bulks (all requests) sent,
 * request queue lengths,
 * requests on queue,
 * - times,
 * - avg,
 * - max,
 * - min,
 * timeouts,
 * requeues,
 * status updates
 * on bulk
 * on requests,
 * errors count,
 * XML validation failure counts,
 * validation errors,
 * business exceptions,
 * reset stats to zero on demand.
 * function to reload database global data on demand.
 * function to reload log4j config on demand.
 * 
 * @author Robin Compston
 * 
 */

public final class SdtMetricsMBean implements ISdtMetricsMBean
{
    /**
     * Static logging object.
     */
    private static final Logger LOG = LoggerFactory.getLogger (SdtMetricsMBean.class);

    /**
     * The singleton instance of this class created by Spring.
     */
    private static SdtMetricsMBean thisBean;

    /**
     * Value which determines the current value of a flag which controls whether individual {@link AbstractCacheControl}
     * instances need to be uncached.
     */
    private int cacheResetControl;

    /**
     * Count of all bulk submits.
     */
    private long bulkSubmitCounts;

    /**
     * Total processing time of all bulk submits.
     */
    private long bulkSubmitTime;

    /**
     * Minimum processing time of all bulk submits.
     */
    private long bulkSubmitTimeMin;

    /**
     * Maxiumum processing time of all bulk submits.
     */
    private long bulkSubmitTimeMax;

    /**
     * Count of all bulk feedback.
     */
    private long bulkFeedbackCounts;

    /**
     * Total processing time of all bulk feedbacks.
     */
    private long bulkFeedbackTime;

    /**
     * Minimum processing time of all bulk feedbacks.
     */
    private long bulkFeedbackTimeMin;

    /**
     * Maxiumum processing time of all bulk feedbacks.
     */
    private long bulkFeedbackTimeMax;

    /**
     * Count of all defence feedback.
     */
    private long defenceFeedbackCounts;

    /**
     * Total processing time of all defence feedbacks.
     */
    private long defenceFeedbackTime;

    /**
     * Minimum processing time of all defence feedbacks.
     */
    private long defenceFeedbackTimeMin;

    /**
     * Maxiumum processing time of all defence feedbacks.
     */
    private long defenceFeedbackTimeMax;

    /**
     * Bulk status update count (as a result of all requests on a bulk being updated).
     */
    private long bulkStatusUpdateCount;

    /**
     * Request status update count (from case management system).
     */
    private long requestStatusUpdateCount;

    /**
     * Maxiumum processing time of all defence feedbacks.
     */
    private long completedBulkSubmitCount;

    /**
     * Number of requests in bulk submits.
     */
    private long requestCount;

    /**
     * Number of domain objects in existence.
     */
    private long domainObjectsCount;

    /**
     * Number of DAO calls.
     */
    private long databaseCallsCount;

    /**
     * Time calling database.
     */
    private long databaseCallsTime;

    /**
     * Maxiumum time calling database.
     */
    private long databaseCallsTimeMin;

    /**
     * Maxiumum time calling database.
     */
    private long databaseCallsTimeMax;

    /**
     * Number of DAO reads.
     */
    private long databaseReadsCount;

    /**
     * Time reading database.
     */
    private long databaseReadsTime;

    /**
     * Maxiumum time reading database.
     */
    private long databaseReadsTimeMin;

    /**
     * Maxiumum time reading database.
     */
    private long databaseReadsTimeMax;

    /**
     * Number of DAO writes.
     */
    private long databaseWritesCount;

    /**
     * Time writing database.
     */
    private long databaseWritesTime;

    /**
     * Maxiumum writing reading database.
     */
    private long databaseWritesTimeMin;

    /**
     * Maxiumum writing reading database.
     */
    private long databaseWritesTimeMax;

    /**
     * Number of active customers.
     */
    private long activeCustomers;

    /**
     * Number of requests queued to case management system.
     */
    private long requestQueuedCount;

    /**
     * Time queued waiting for request to be processed.
     */
    private long requestQueuedTime;

    /**
     * Maxiumum time queued waiting for request to be processed.
     */
    private long requestQueuedTimeMin;

    /**
     * Maxiumum time queued waiting for request to be processed.
     */
    private long requestQueuedTimeMax;

    /**
     * Current request queue length.
     */
    private long requestQueueLength;

    /**
     * Maxiumum request queue length.
     */
    private long requestQueueLengthMax;

    /**
     * Number of request requeues following timeout or during recovery.
     */
    private long requestRequeues;

    /**
     * Number of case management system responses.
     */
    private long caseMgmtResponseCount;

    /**
     * Time for case management system to respond.
     */
    private long caseMgmtResponseTime;

    /**
     * Minimum time for case management system to respond.
     */
    private long caseMgmtResponseTimeMin;

    /**
     * Maximum time for case management system to respond.
     */
    private long caseMgmtResponseTimeMax;

    /**
     * Number of timeouts waiting for case management system to respond.
     */
    private long caseMgmtResponseTimeouts;

    /**
     * Number of errors encountered.
     */
    private long errorsCount;

    /**
     * Number of XML validation errors of incoming messages.
     */
    private long xmlValidationFailureCount;

    /**
     * Number of business exceptions.
     */
    private long businessExceptionCount;

    /**
     * The last bulk submit reference assigned.
     */
    private String lastBulkSubmitRef;

    /**
     * The last bulk request reference assigned.
     */
    private String lastBulkRequestRef;

    /**
     * Constructor for {@link SdtMetricsMBean}.
     */
    private SdtMetricsMBean ()
    {
        SdtMetricsMBean.thisBean = this;
    }

    // BULK SUBMISSION

    /**
     * Get current count of bulks submits since last reset.
     * 
     * @return count of bulk submits.
     */
    private long getBulkSubmitCounts ()
    {
        return this.bulkSubmitCounts;
    }

    @Override
    public void upBulkSubmitCounts ()
    {
        this.bulkSubmitCounts += 1;
    }

    /**
     * Get the number of individual requests.
     * 
     * @return the number of individual requests.
     */
    private long getRequestCount ()
    {
        return this.requestCount;
    }

    /**
     * Get total time for bulks submits since last reset.
     * 
     * @return total time (milliseconds) for bulk submits.
     */
    private long getBulkSubmitTime ()
    {
        return this.bulkSubmitTime;
    }

    /* (non-Javadoc)
     * 
     * @see uk.gov.moj.sdt.utils.mbeans.IFred#addBulkSubmitTime(long) */
    @Override
    public void addBulkSubmitTime (final long bulkSubmitTime)
    {
        this.bulkSubmitTime += bulkSubmitTime;

        // Update the minimum if needed.
        if (this.bulkSubmitTimeMin == 0 || bulkSubmitTime < this.bulkSubmitTimeMin)
        {
            this.bulkSubmitTimeMin = bulkSubmitTime;
        }

        // Update the maximum if needed.
        if (this.bulkSubmitTimeMax == 0 || bulkSubmitTime > this.bulkSubmitTimeMax)
        {
            this.bulkSubmitTimeMax = bulkSubmitTime;
        }
    }

    /**
     * Get average time for bulks submits since last reset.
     * 
     * @return average time (milliseconds) for bulk submits.
     */
    private long getBulkSubmitTimeAvg ()
    {
        return (this.bulkSubmitCounts == 0) ? 0 : this.bulkSubmitTime / this.bulkSubmitCounts;
    }

    /**
     * Get minimum time for bulks submits since last reset.
     * 
     * @return minimum time (milliseconds) for bulk submits.
     */
    private long getBulkSubmitTimeMin ()
    {
        return this.bulkSubmitTimeMin;
    }

    /**
     * Get maximum time for bulks submits since last reset.
     * 
     * @return maximum time (milliseconds) for bulk submits.
     */
    private long getBulkSubmitTimeMax ()
    {
        return this.bulkSubmitTimeMax;
    }

    // BULK FEEDBACK

    /**
     * Get current count of bulks Feedbacks since last reset.
     * 
     * @return count of bulk Feedbacks.
     */
    private long getBulkFeedbackCounts ()
    {
        return this.bulkFeedbackCounts;
    }

    @Override
    public void upBulkFeedbackCounts ()
    {
        this.bulkFeedbackCounts += 1;
    }

    /**
     * Get total time for bulks Feedbacks since last reset.
     * 
     * @return total time (milliseconds) for bulk Feedbacks.
     */
    private long getBulkFeedbackTime ()
    {
        return this.bulkFeedbackTime;
    }

    @Override
    public void addBulkFeedbackTime (final long bulkFeedbackTime)
    {
        this.bulkFeedbackTime += bulkFeedbackTime;

        // Update the minimum if needed.
        if (this.bulkFeedbackTimeMin == 0 || bulkFeedbackTime < this.bulkFeedbackTimeMin)
        {
            this.bulkFeedbackTimeMin = bulkFeedbackTime;
        }

        // Update the maximum if needed.
        if (this.bulkFeedbackTimeMax == 0 || bulkFeedbackTime > this.bulkFeedbackTimeMax)
        {
            this.bulkFeedbackTimeMax = bulkFeedbackTime;
        }
    }

    /**
     * Get average time for bulks Feedbacks since last reset.
     * 
     * @return average time (milliseconds) for bulk Feedbacks.
     */
    private long getBulkFeedbackTimeAvg ()
    {
        return (this.bulkFeedbackCounts == 0) ? 0 : this.bulkFeedbackTime / this.bulkFeedbackCounts;
    }

    /**
     * Get minimum time for bulks Feedbacks since last reset.
     * 
     * @return minimum time (milliseconds) for bulk Feedbacks.
     */
    private long getBulkFeedbackTimeMin ()
    {
        return this.bulkFeedbackTimeMin;
    }

    /**
     * Get maximum time for bulks Feedbacks since last reset.
     * 
     * @return maximum time (milliseconds) for bulk Feedbacks.
     */
    private long getBulkFeedbackTimeMax ()
    {
        return this.bulkFeedbackTimeMax;
    }

    // DEFENCE FEEDBACK

    /**
     * Get current count of defences feedbacks since last reset.
     * 
     * @return count of defence feedbacks.
     */
    private long getDefenceFeedbackCounts ()
    {
        return this.defenceFeedbackCounts;
    }

    @Override
    public void upDefenceFeedbackCounts ()
    {
        this.defenceFeedbackCounts += 1;
    }

    /**
     * Get total time for defences feedbacks since last reset.
     * 
     * @return total time (milliseconds) for defence feedbacks.
     */
    private long getDefenceFeedbackTime ()
    {
        return this.defenceFeedbackTime;
    }

    @Override
    public void addDefenceFeedbackTime (final long defenceFeedbackTime)
    {
        this.defenceFeedbackTime += defenceFeedbackTime;

        // Update the minimum if needed.
        if (this.defenceFeedbackTimeMin == 0 || defenceFeedbackTime < this.defenceFeedbackTimeMin)
        {
            this.defenceFeedbackTimeMin = defenceFeedbackTime;
        }

        // Update the maximum if needed.
        if (this.defenceFeedbackTimeMax == 0 || defenceFeedbackTime > this.defenceFeedbackTimeMax)
        {
            this.defenceFeedbackTimeMax = defenceFeedbackTime;
        }
    }

    /**
     * Get average time for defences feedbacks since last reset.
     * 
     * @return average time (milliseconds) for defence feedbacks.
     */
    private long getDefenceFeedbackTimeAvg ()
    {
        return (this.defenceFeedbackCounts == 0) ? 0 : this.defenceFeedbackTime / this.defenceFeedbackCounts;
    }

    /**
     * Get minimum time for defences feedbacks since last reset.
     * 
     * @return minimum time (milliseconds) for defence feedbacks.
     */
    private long getDefenceFeedbackTimeMin ()
    {
        return this.defenceFeedbackTimeMin;
    }

    /**
     * Get maximum time for defences feedbacks since last reset.
     * 
     * @return maximum time (milliseconds) for defence feedbacks.
     */
    private long getDefenceFeedbackTimeMax ()
    {
        return this.defenceFeedbackTimeMax;
    }

    /**
     * Number of bulk status updates from case management systems.
     * 
     * @return number of bulk status updates.
     */
    private long getBulkStatusUpdateCount ()
    {
        return this.bulkStatusUpdateCount;
    }

    /**
     * Get the number of requests status updates (from case managament system).
     * 
     * @return the number of requests status updates.
     */
    private long getRequestStatusUpdateCount ()
    {
        return this.requestStatusUpdateCount;
    }

    /**
     * Get the number of completed bulk submits (all requests sent to case managment system).
     * 
     * @return the number of completed bulk submits.
     */
    private long getCompletedBulkSubmitCount ()
    {
        return this.completedBulkSubmitCount;
    }

    /**
     * Get the number of domain objects.
     * 
     * @return the number of domain objects.
     */
    private long getDomainObjectsCount ()
    {
        return this.domainObjectsCount;
    }

    /**
     * Get the number of database (Hibernate) calls.
     * 
     * @return the number of database (Hibernate) calls.
     */
    private long getDatabaseCallsCount ()
    {
        return this.databaseCallsCount;
    }

    /**
     * Get the time spent calling the database (Hibernate).
     * 
     * @return the time spent calling the database (Hibernate).
     */
    private long getDatabaseCallsTime ()
    {
        return this.databaseCallsTime;
    }

    /**
     * Get average time for defences feedbacks since last reset.
     * 
     * @return average time (milliseconds) for defence feedbacks.
     */
    private long getDatabaseCallsTimeAvg ()
    {
        return (this.databaseCallsCount == 0) ? 0 : this.databaseCallsTime / this.databaseCallsCount;
    }

    /**
     * Get the minimum time spent calling the database (Hibernate).
     * 
     * @return the minimum time spent calling the database (Hibernate).
     */
    private long getDatabaseCallsTimeMin ()
    {
        return this.databaseCallsTimeMin;
    }

    /**
     * Get the maximum time spent calling the database (Hibernate).
     * 
     * @return the maximum time spent calling the database (Hibernate).
     */
    private long getDatabaseCallsTimeMax ()
    {
        return this.databaseCallsTimeMax;
    }

    /**
     * Get the number of database (Hibernate) Reads.
     * 
     * @return the number of database (Hibernate) Reads.
     */
    private long getDatabaseReadsCount ()
    {
        return this.databaseReadsCount;
    }

    /**
     * Get the time spent reading the database (Hibernate).
     * 
     * @return the time spent reading the database (Hibernate).
     */
    private long getDatabaseReadsTime ()
    {
        return this.databaseReadsTime;
    }

    /**
     * Get average time for defences feedbacks since last reset.
     * 
     * @return average time (milliseconds) for defence feedbacks.
     */
    private long getDatabaseReadsTimeAvg ()
    {
        return (this.databaseReadsCount == 0) ? 0 : this.databaseReadsTime / this.databaseReadsCount;
    }

    /**
     * Get the minimum time spent reading the database (Hibernate).
     * 
     * @return the minimum time spent reading the database (Hibernate).
     */
    private long getDatabaseReadsTimeMin ()
    {
        return this.databaseReadsTimeMin;
    }

    /**
     * Get the maximum time spent reading the database (Hibernate).
     * 
     * @return the maximum time spent reading the database (Hibernate).
     */
    private long getDatabaseReadsTimeMax ()
    {
        return this.databaseReadsTimeMax;
    }

    /**
     * Get the number of database (Hibernate) Writes.
     * 
     * @return the number of database (Hibernate) Writes.
     */
    private long getDatabaseWritesCount ()
    {
        return this.databaseWritesCount;
    }

    /**
     * Get the time spent Writeing the database (Hibernate).
     * 
     * @return the time spent Writeing the database (Hibernate).
     */
    private long getDatabaseWritesTime ()
    {
        return this.databaseWritesTime;
    }

    /**
     * Get average time for defences feedbacks since last reset.
     * 
     * @return average time (milliseconds) for defence feedbacks.
     */
    private long getDatabaseWritesTimeAvg ()
    {
        return (this.databaseWritesCount == 0) ? 0 : this.databaseWritesTime / this.databaseWritesCount;
    }

    /**
     * Get the minimum time spent Writeing the database (Hibernate).
     * 
     * @return the minimum time spent Writeing the database (Hibernate).
     */
    private long getDatabaseWritesTimeMin ()
    {
        return this.databaseWritesTimeMin;
    }

    /**
     * Get the maximum time spent Writeing the database (Hibernate).
     * 
     * @return the maximum time spent Writeing the database (Hibernate).
     */
    private long getDatabaseWritesTimeMax ()
    {
        return this.databaseWritesTimeMax;
    }

    /**
     * Get the number of active customers using the system.
     * 
     * @return the number of active customers using the system.
     */
    private long getActiveCustomers ()
    {
        return this.activeCustomers;
    }

    /**
     * Get the number of requests queued.
     * 
     * @return the number of requests queued.
     */
    private long getRequestQueueCount ()
    {
        return this.requestQueuedCount;
    }

    /**
     * Get the total time spent on request queued.
     * 
     * @return the total time spent on request queued.
     */
    private long getRequestQueueTime ()
    {
        return this.requestQueuedTime;
    }

    /**
     * Get average time for defences feedbacks since last reset.
     * 
     * @return average time (milliseconds) for defence feedbacks.
     */
    private long getRequestQueueTimeAvg ()
    {
        return (this.requestQueuedCount == 0) ? 0 : this.requestQueuedTime / this.requestQueuedCount;
    }

    /**
     * Get the minimum time spent on request queued.
     * 
     * @return the minimum time spent on request queued.
     */
    private long getRequestQueueTimeMin ()
    {
        return this.requestQueuedTimeMin;
    }

    /**
     * Get the maximum time spent on request queued.
     * 
     * @return the maximum time spent on request queued.
     */
    private long getRequestQueueTimeMax ()
    {
        return this.requestQueuedTimeMax;
    }

    /**
     * Get the current request queue length.
     * 
     * @return the current request queue length.
     */
    private long getRequestQueueLength ()
    {
        return this.requestQueueLength;
    }

    /**
     * Get the maximum request queue length.
     * 
     * @return the maximum request queue length.
     */
    private long getRequestQueueLengthMax ()
    {
        return this.requestQueueLengthMax;
    }

    /**
     * Get the number of requests requeues.
     * 
     * @return the number of requests requeues.
     */
    private long getRequestRequeues ()
    {
        return this.requestRequeues;
    }

    /**
     * Get the number of responses received from the case management system.
     * 
     * @return the number of responses received from the case management system.
     */
    private long getCaseMgmtResponseCount ()
    {
        return this.caseMgmtResponseCount;
    }

    /**
     * Get the total response time for the case management system.
     * 
     * @return the total response time for the case management system.
     */
    private long getCaseMgmtResponseTime ()
    {
        return this.caseMgmtResponseTime;
    }

    /**
     * Get average time for case management reponse since last reset.
     * 
     * @return average time for case management reponse.
     */
    private long getCaseMgmtResponseTimeAvg ()
    {
        return (this.defenceFeedbackCounts == 0) ? 0 : this.caseMgmtResponseTime / this.defenceFeedbackCounts;
    }

    /**
     * Get the minimum response time for the case management system.
     * 
     * @return the minimum response time for the case management system.
     */
    private long getCaseMgmtResponseTimeMin ()
    {
        return this.caseMgmtResponseTimeMin;
    }

    /**
     * Get the maximum response time for the case management system.
     * 
     * @return the maximum response time for the case management system.
     */
    private long getCaseMgmtResponseTimeMax ()
    {
        return this.caseMgmtResponseTimeMax;
    }

    /**
     * Get the number of case management response timeouts.
     * 
     * @return the number of case management response timeouts.
     */
    private long getCaseMgmtResponseTimeouts ()
    {
        return this.caseMgmtResponseTimeouts;
    }

    /**
     * Get the number of errors.
     * 
     * @return the number of errors.
     */
    private long getErrorsCount ()
    {
        return this.errorsCount;
    }

    /**
     * Get the number of XML validation errors.
     * 
     * @return the number of XML validation errors.
     */
    private long getXmlValidationFailureCount ()
    {
        return this.xmlValidationFailureCount;
    }

    /**
     * Get the number of business exceptions.
     * 
     * @return the number of business exceptions.
     */
    private long getBusinessExceptionCount ()
    {
        return this.businessExceptionCount;
    }

    @Override
    public void upBulkStatusUpdateCount ()
    {
        this.bulkStatusUpdateCount += 1;
    }

    @Override
    public void upRequestStatusUpdateCount ()
    {
        this.requestStatusUpdateCount += 1;
    }

    @Override
    public void upDomainObjectsCount ()
    {
        this.domainObjectsCount += 1;
    }

    @Override
    public void downDomainObjectsCount ()
    {
        this.domainObjectsCount -= 1;

        // Time problems means that this can go negative since beans created before Spring had finished initialising are
        // never recorded. Avoid this.
        if (this.domainObjectsCount < 0)
        {
            this.domainObjectsCount = 0;
        }
    }

    @Override
    public void upDatabaseReadsCount ()
    {
        this.databaseCallsCount += 1;
        this.databaseReadsCount += 1;
    }

    @Override
    public void addDatabaseReadsTime (final long databaseReadsTime)
    {
        this.databaseReadsTime += databaseReadsTime;

        // Update the minimum if needed.
        if (this.databaseReadsTimeMin == 0 || databaseReadsTime < this.databaseReadsTimeMin)
        {
            this.databaseReadsTimeMin = databaseReadsTime;
        }

        // Update the maximum if needed.
        if (this.databaseReadsTimeMax == 0 || databaseReadsTime > this.databaseReadsTimeMax)
        {
            this.databaseReadsTimeMax = databaseReadsTime;
        }

        this.databaseCallsTime += databaseCallsTime;

        // Update the minimum if needed.
        if (this.databaseCallsTimeMin == 0 || databaseCallsTime < this.databaseCallsTimeMin)
        {
            this.databaseCallsTimeMin = databaseCallsTime;
        }

        // Update the maximum if needed.
        if (this.databaseCallsTimeMax == 0 || databaseCallsTime > this.databaseCallsTimeMax)
        {
            this.databaseCallsTimeMax = databaseCallsTime;
        }
    }

    @Override
    public void upDatabaseWritesCount ()
    {
        this.databaseCallsCount += 1;
        this.databaseWritesCount += 1;
    }

    @Override
    public void addDatabaseWritesTime (final long databaseWritesTime)
    {
        this.databaseWritesTime += databaseWritesTime;

        // Update the minimum if needed.
        if (this.databaseWritesTimeMin == 0 || databaseWritesTime < this.databaseWritesTimeMin)
        {
            this.databaseWritesTimeMin = databaseWritesTime;
        }

        // Update the maximum if needed.
        if (this.databaseWritesTimeMax == 0 || databaseWritesTime > this.databaseWritesTimeMax)
        {
            this.databaseWritesTimeMax = databaseWritesTime;
        }

        this.databaseCallsTime += databaseCallsTime;

        // Update the minimum if needed.
        if (this.databaseCallsTimeMin == 0 || databaseCallsTime < this.databaseCallsTimeMin)
        {
            this.databaseCallsTimeMin = databaseCallsTime;
        }

        // Update the maximum if needed.
        if (this.databaseCallsTimeMax == 0 || databaseCallsTime > this.databaseCallsTimeMax)
        {
            this.databaseCallsTimeMax = databaseCallsTime;
        }
    }

    @Override
    public void upActiveCustomers ()
    {
        this.activeCustomers += 1;
    }

    @Override
    public void upCompletedBulkSubmitCount ()
    {
        this.completedBulkSubmitCount += 1;
    }

    @Override
    public void upRequestCount ()
    {
        this.requestCount += 1;
    }

    @Override
    public void upRequestQueuedCount ()
    {
        this.requestQueuedCount += 1;
    }

    @Override
    public void addRequestQueuedTime (final long requestQueuedTime)
    {
        this.requestQueuedTime = requestQueuedTime;

        // Update the minimum if needed.
        if (this.requestQueuedTimeMin == 0 || requestQueuedTime < this.requestQueuedTimeMin)
        {
            this.requestQueuedTimeMin = requestQueuedTime;
        }

        // Update the maximum if needed.
        if (this.requestQueuedTimeMax == 0 || requestQueuedTime > this.requestQueuedTimeMax)
        {
            this.requestQueuedTimeMax = requestQueuedTime;
        }
    }

    @Override
    public void upRequestQueueLength ()
    {
        this.requestQueueLength += 1;

        if (this.requestQueueLength > this.requestQueueLengthMax)
        {
            this.requestQueueLengthMax = this.requestQueueLength;
        }
    }

    @Override
    public void decrementRequestQueueLength ()
    {
        this.requestQueueLength -= 1;
    }

    @Override
    public void upRequestRequeues ()
    {
        this.requestRequeues += 1;
    }

    @Override
    public void addCaseMgmtResponseTime (final long caseMgmtResponseTime)
    {
        this.caseMgmtResponseTime += caseMgmtResponseTime;

        // Update the minimum if needed.
        if (this.caseMgmtResponseTimeMin == 0 || caseMgmtResponseTime < this.caseMgmtResponseTimeMin)
        {
            this.caseMgmtResponseTimeMin = caseMgmtResponseTime;
        }

        // Update the maximum if needed.
        if (this.caseMgmtResponseTimeMax == 0 || caseMgmtResponseTime > this.caseMgmtResponseTimeMax)
        {
            this.caseMgmtResponseTimeMax = caseMgmtResponseTime;
        }
    }

    @Override
    public void upCaseMgmtResponseTimeouts ()
    {
        this.caseMgmtResponseTimeouts += 1;
    }

    @Override
    public void upErrorsCount ()
    {
        this.errorsCount += 1;
    }

    @Override
    public void upXmlValidationFailureCount ()
    {
        this.xmlValidationFailureCount += 1;
    }

    @Override
    public void upBusinessExceptionCount ()
    {
        this.businessExceptionCount += 1;
    }

    @Override
    public void setLastBulkSubmitRef (final String lastBulkSubmitRef)
    {
        this.lastBulkSubmitRef = lastBulkSubmitRef;
    }

    @Override
    public void setLastBulkRequestRef (final String lastBulkRequestRef)
    {
        this.lastBulkRequestRef = lastBulkRequestRef;
    }

    @Override
    public void reset ()
    {
        LOG.debug ("Resetting SDT metrics");

        this.bulkSubmitCounts = 0;
        this.bulkSubmitTime = 0;
        this.bulkSubmitTimeMin = 0;
        this.bulkSubmitTimeMax = 0;
        this.bulkFeedbackCounts = 0;
        this.bulkFeedbackTime = 0;
        this.bulkFeedbackTimeMin = 0;
        this.bulkFeedbackTimeMax = 0;
        this.defenceFeedbackCounts = 0;
        this.defenceFeedbackTime = 0;
        this.defenceFeedbackTimeMin = 0;
        this.defenceFeedbackTimeMax = 0;
        this.bulkStatusUpdateCount = 0;
        this.requestStatusUpdateCount = 0;
        this.completedBulkSubmitCount = 0;
        this.requestCount = 0;
        this.domainObjectsCount = 0;
        this.databaseCallsCount = 0;
        this.databaseCallsTime = 0;
        this.databaseCallsTimeMin = 0;
        this.databaseCallsTimeMax = 0;
        this.activeCustomers = 0;
        this.requestQueuedCount = 0;
        this.requestQueuedTime = 0;
        this.requestQueuedTimeMin = 0;
        this.requestQueuedTimeMax = 0;
        this.requestQueueLength = 0;
        this.requestQueueLengthMax = 0;
        this.requestRequeues = 0;
        this.caseMgmtResponseCount = 0;
        this.caseMgmtResponseTime = 0;
        this.caseMgmtResponseTimeMin = 0;
        this.caseMgmtResponseTimeMax = 0;
        this.caseMgmtResponseTimeouts = 0;
        this.errorsCount = 0;
        this.xmlValidationFailureCount = 0;
        this.businessExceptionCount = 0;
        this.lastBulkSubmitRef = "";
        this.lastBulkRequestRef = "";
    }

    @Override
    public String getBulkSubmitStats ()
    {
        return "Bulk submits: count[" + this.getBulkSubmitCounts () + "], requests[" + this.getRequestCount () +
                "], time[" + this.getBulkSubmitTime () + "], average[" + this.getBulkSubmitTimeAvg () + "], minimum[" +
                this.getBulkSubmitTimeMin () + "], maximum[" + this.getBulkSubmitTimeMax () + "]";
    }

    @Override
    public String getBulkFeedbackStats ()
    {
        return "Bulk feedbacks: count[" + this.getBulkFeedbackCounts () + "], time[" + this.getBulkFeedbackTime () +
                "], average[" + this.getBulkFeedbackTimeAvg () + "], minimum[" + this.getBulkFeedbackTimeMin () +
                "], maximum[" + this.getBulkFeedbackTimeMax () + "]";
    }

    @Override
    public String getDefenceFeedbackStats ()
    {
        return "Defence feedback: count[" + this.getDefenceFeedbackCounts () + "], time[" +
                this.getDefenceFeedbackTime () + "], average[" + this.getDefenceFeedbackTimeAvg () + "], minimum[" +
                this.getDefenceFeedbackTimeMin () + "], maximum[" + this.getDefenceFeedbackTimeMax () + "]";
    }

    @Override
    public String getStatusStats ()
    {
        return "Status update: bulk count[" + this.getBulkStatusUpdateCount () + "], request count[" +
                this.getRequestStatusUpdateCount () + "], completed bulk count[" + this.getCompletedBulkSubmitCount () +
                "]";
    }

    @Override
    public String getDomainObjectsStats ()
    {
        return "Domain objects: count[" + this.getDomainObjectsCount () + "]";
    }

    @Override
    public String getDatabaseCallsStats ()
    {
        return "Database calls: count[" + this.getDatabaseCallsCount () + "], time[" + this.getDatabaseCallsTime () +
                "], average[" + this.getDatabaseCallsTimeAvg () + "], minimum[" + this.getDatabaseCallsTimeMin () +
                "], maximum[" + this.getDatabaseCallsTimeMax () + "]";
    }

    @Override
    public String getDatabaseReadsStats ()
    {
        return "Database reads: count[" + this.getDatabaseReadsCount () + "], time[" + this.getDatabaseReadsTime () +
                "], average[" + this.getDatabaseReadsTimeAvg () + "], minimum[" + this.getDatabaseReadsTimeMin () +
                "], maximum[" + this.getDatabaseReadsTimeMax () + "]";
    }

    @Override
    public String getDatabaseWritesStats ()
    {
        return "Database writes: count[" + this.getDatabaseWritesCount () + "], time[" + this.getDatabaseWritesTime () +
                "], average[" + this.getDatabaseWritesTimeAvg () + "], minimum[" + this.getDatabaseWritesTimeMin () +
                "], maximum[" + this.getDatabaseWritesTimeMax () + "]";
    }

    @Override
    public String getActiveCustomersStats ()
    {
        return "Active customers: count[" + this.getActiveCustomers () + "]";
    }

    @Override
    public String getRequestQueueStats ()
    {
        return "Request queue: count[" + this.getRequestQueueCount () + "], time[" + this.getRequestQueueTime () +
                "], average[" + this.getRequestQueueTimeAvg () + "], minimum[" + this.getRequestQueueTimeMin () +
                "], maximum[" + this.getRequestQueueTimeMax () + "], queue length[" + this.getRequestQueueLength () +
                "], max queue length[" + this.getRequestQueueLengthMax () + "], requeues[" +
                this.getRequestRequeues () + "]";

    }

    @Override
    public String getCaseMgmtStats ()
    {
        return "Case Mgmt Response: count[" + this.getCaseMgmtResponseCount () + "], time[" +
                this.getCaseMgmtResponseTime () + "], average[" + this.getCaseMgmtResponseTimeAvg () + "], minimum[" +
                this.getCaseMgmtResponseTimeMin () + "], maximum[" + this.getCaseMgmtResponseTimeMax () +
                "], timeouts[" + this.getCaseMgmtResponseTimeouts () + "]";
    }

    @Override
    public String getErrorStats ()
    {
        return "Errors and exceptions: error counts[" + this.getErrorsCount () + "], xml validation errors[" +
                this.getXmlValidationFailureCount () + "], business exceptions[" + this.getBusinessExceptionCount () +
                "]";
    }

    @Override
    public String getLastBulkSubmitRef ()
    {
        return this.lastBulkSubmitRef;
    }

    @Override
    public String getLastBulkRequestRef ()
    {
        return this.lastBulkRequestRef;
    }

    @Override
    public int getCacheResetControl ()
    {
        return this.cacheResetControl;
    }

    @Override
    public void uncache ()
    {
        this.cacheResetControl++;
    }

    // TODO - add function to reload log4j config on demand.

    /**
     * Get singleton instance of this bean - used by callers to update statistics.
     * 
     * @return singleton instance of this bean.
     */
    public static SdtMetricsMBean getSdtMetrics ()
    {
        return SdtMetricsMBean.thisBean;
    }

}
