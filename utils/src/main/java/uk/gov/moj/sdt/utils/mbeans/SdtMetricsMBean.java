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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

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
 * - last web service time
 * - rate
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
 * target application unavailable,
 * requeues,
 * status updates
 * on bulk
 * on requests,
 * XML validation failure counts,
 * validation errors,
 * business exceptions,
 * last business exception,
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
    private static final Logger LOGGER = LoggerFactory.getLogger (SdtMetricsMBean.class);

    /**
     * The singleton instance of this class created by Spring.
     */
    private static SdtMetricsMBean thisBean;

    /**
     * The singleton instance of this class created by Spring.
     */
    private static final double MILLISECONDS = 1000;

    /**
     * Value which determines the current value of a flag which controls whether individual {@link AbstractCacheControl}
     * instances need to be uncached.
     */
    private int cacheResetControl;

    /**
     * Current active value of performance logging flags which control what performance logging points are active.
     */
    private short performanceLoggingFlags;

    /**
     * Count of all bulk submits.
     */
    private long bulkSubmitCounts;

    /**
     * Time of last bulk submits web service call.
     */
    private long bulkSubmitLastTime;

    /**
     * Total processing time of all bulk submits.
     */
    private long bulkSubmitTime;

    /**
     * Minimum processing time of all bulk submits.
     */
    private long bulkSubmitTimeMin = Long.MAX_VALUE;

    /**
     * Maxiumum processing time of all bulk submits.
     */
    private long bulkSubmitTimeMax;

    /**
     * Count of all bulk feedback.
     */
    private long bulkFeedbackCounts;

    /**
     * Time of last bulk Feedback web service call.
     */
    private long bulkFeedbackLastTime;

    /**
     * Total processing time of all bulk feedbacks.
     */
    private long bulkFeedbackTime;

    /**
     * Minimum processing time of all bulk feedbacks.
     */
    private long bulkFeedbackTimeMin = Long.MAX_VALUE;

    /**
     * Maxiumum processing time of all bulk feedbacks.
     */
    private long bulkFeedbackTimeMax;

    /**
     * Count of all submit query.
     */
    private long submitQueryCounts;

    /**
     * Time of last submit query web service call.
     */
    private long submitQueryLastTime;

    /**
     * Total processing time of all submit querys.
     */
    private long submitQueryTime;

    /**
     * Minimum processing time of all submit querys.
     */
    private long submitQueryTimeMin = Long.MAX_VALUE;

    /**
     * Maxiumum processing time of all submit querys.
     */
    private long submitQueryTimeMax;

    /**
     * Bulk status update count (as a result of all requests on a bulk being updated).
     */
    private long bulkStatusUpdateCount;

    /**
     * Request status update count (from target application).
     */
    private long requestStatusUpdateCount;

    /**
     * Maxiumum processing time of all submit querys.
     */
    private long completedBulkSubmitCount;

    /**
     * Number of requests in bulk submits.
     */
    private long requestCount;

    /**
     * Time of start or last reset.
     */
    private long resetTime;

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
    private long databaseCallsTimeMin = Long.MAX_VALUE;

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
    private long databaseReadsTimeMin = Long.MAX_VALUE;

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
    private long databaseWritesTimeMin = Long.MAX_VALUE;

    /**
     * Maxiumum writing reading database.
     */
    private long databaseWritesTimeMax;

    /**
     * Number of active customers.
     */
    private long activeCustomers;

    /**
     * Number of requests queue count to target application.
     */
    private long requestQueueCount;

    /**
     * Time on queue waiting for request to be processed.
     */
    private long requestQueueTime;

    /**
     * Maxiumum time on queue waiting for request to be processed.
     */
    private long requestQueueTimeMin = Long.MAX_VALUE;

    /**
     * Maxiumum time on queue waiting for request to be processed.
     */
    private long requestQueueTimeMax;

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
     * Number of calls to target applications.
     */
    private long targetAppCallCount;

    /**
     * Time for target application to respond.
     */
    private long targetAppResponseTime;

    /**
     * Minimum time for target application to respond.
     */
    private long targetAppResponseTimeMin = Long.MAX_VALUE;

    /**
     * Maximum time for target application to respond.
     */
    private long targetAppResponseTimeMax;

    /**
     * Number of times the target application was unavailable.
     */
    private long targetAppUnavailable;

    /**
     * Number of times the target application returned a miscellaneous error.
     */
    private long targetAppMiscErrors;

    /**
     * Number of times the target application returned a SOAP error.
     */
    private long targetAppSoapErrors;

    /**
     * Number of timeouts waiting for target application to respond.
     */
    private long targetAppResponseTimeouts;

    /**
     * Number of XML validation errors of incoming messages.
     */
    private long xmlValidationFailureCount;

    /**
     * Number of business exceptions.
     */
    private long businessExceptionCount;

    /**
     * Last business exceptions code.
     */
    private String lastBusinessException;

    /**
     * The last bulk submit reference assigned.
     */
    private String lastBulkSubmitRef;

    /**
     * The last bulk request reference assigned.
     */
    private String lastBulkRequestRef;

    /**
     * Date formatter for all dates.
     */
    private DateFormat formatter = new SimpleDateFormat ("yyyy.MM.dd_HH:mm:ss.SSS");

    /**
     * Constructor for {@link SdtMetricsMBean}.
     */
    private SdtMetricsMBean ()
    {
        SdtMetricsMBean.thisBean = this;

        // Set start time.
        this.resetTime = new GregorianCalendar ().getTimeInMillis ();
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

        this.bulkSubmitLastTime = new GregorianCalendar ().getTimeInMillis ();
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
        if (bulkSubmitTime < this.bulkSubmitTimeMin)
        {
            this.bulkSubmitTimeMin = bulkSubmitTime;
        }

        // Update the maximum if needed.
        if (bulkSubmitTime > this.bulkSubmitTimeMax)
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

        this.bulkFeedbackLastTime = new GregorianCalendar ().getTimeInMillis ();
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
        if (bulkFeedbackTime < this.bulkFeedbackTimeMin)
        {
            this.bulkFeedbackTimeMin = bulkFeedbackTime;
        }

        // Update the maximum if needed.
        if (bulkFeedbackTime > this.bulkFeedbackTimeMax)
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

    // SUBMIT QUERY

    /**
     * Get current count of submit queries since last reset.
     * 
     * @return count of submit querys.
     */
    private long getSubmitQueryCounts ()
    {
        return this.submitQueryCounts;
    }

    @Override
    public void upSubmitQueryCounts ()
    {
        this.submitQueryCounts += 1;

        this.submitQueryLastTime = new GregorianCalendar ().getTimeInMillis ();
    }

    /**
     * Get total time for submit queries since last reset.
     * 
     * @return total time (milliseconds) for submit querys.
     */
    private long getSubmitQueryTime ()
    {
        return this.submitQueryTime;
    }

    @Override
    public void addSubmitQueryTime (final long submitQueryTime)
    {
        this.submitQueryTime += submitQueryTime;

        // Update the minimum if needed.
        if (submitQueryTime < this.submitQueryTimeMin)
        {
            this.submitQueryTimeMin = submitQueryTime;
        }

        // Update the maximum if needed.
        if (submitQueryTime > this.submitQueryTimeMax)
        {
            this.submitQueryTimeMax = submitQueryTime;
        }
    }

    /**
     * Get average time for submit query feedbacks since last reset.
     * 
     * @return average time (milliseconds) for submit querys.
     */
    private long getSubmitQueryTimeAvg ()
    {
        return (this.submitQueryCounts == 0) ? 0 : this.submitQueryTime / this.submitQueryCounts;
    }

    /**
     * Get minimum time for submit query feedbacks since last reset.
     * 
     * @return minimum time (milliseconds) for submit querys.
     */
    private long getSubmitQueryTimeMin ()
    {
        return this.submitQueryTimeMin;
    }

    /**
     * Get maximum time for submit query feedbacks since last reset.
     * 
     * @return maximum time (milliseconds) for submit querys.
     */
    private long getSubmitQueryTimeMax ()
    {
        return this.submitQueryTimeMax;
    }

    /**
     * Number of bulk status updates from target application.
     * 
     * @return number of bulk status updates.
     */
    private long getBulkStatusUpdateCount ()
    {
        return this.bulkStatusUpdateCount;
    }

    /**
     * Get the number of requests status updates (from target application).
     * 
     * @return the number of requests status updates.
     */
    private long getRequestStatusUpdateCount ()
    {
        return this.requestStatusUpdateCount;
    }

    /**
     * Get the number of completed bulk submits (all requests sent to target application).
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
     * Get average time for submit queries since last reset.
     * 
     * @return average time (milliseconds) for submit querys.
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
     * Get average time for submit queries since last reset.
     * 
     * @return average time (milliseconds) for submit querys.
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
     * Get average time for submit queries since last reset.
     * 
     * @return average time (milliseconds) for submit querys.
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
     * Get the request queue count.
     * 
     * @return the request queue count.
     */
    private long getRequestQueueCount ()
    {
        return this.requestQueueCount;
    }

    /**
     * Get the total time spent on request queue.
     * 
     * @return the total time spent on request queue.
     */
    private long getRequestQueueTime ()
    {
        return this.requestQueueTime;
    }

    /**
     * Get average time for submit queries since last reset.
     * 
     * @return average time (milliseconds) for submit querys.
     */
    private long getRequestQueueTimeAvg ()
    {
        return (this.requestQueueCount == 0) ? 0 : this.requestQueueTime / this.requestQueueCount;
    }

    /**
     * Get the minimum time spent on request queue.
     * 
     * @return the minimum time spent on request queue.
     */
    private long getRequestQueueTimeMin ()
    {
        return this.requestQueueTimeMin;
    }

    /**
     * Get the maximum time spent on request queue.
     * 
     * @return the maximum time spent on request queue.
     */
    private long getRequestQueueTimeMax ()
    {
        return this.requestQueueTimeMax;
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
     * Get the number of calls to target applications.
     * 
     * @return the number of calls to target applications.
     */
    private long getTargetAppCallCount ()
    {
        return this.targetAppCallCount;
    }

    /**
     * Get the total response time for the target application.
     * 
     * @return the total response time for the target application.
     */
    private long getTargetAppResponseTime ()
    {
        return this.targetAppResponseTime;
    }

    /**
     * Get average time for target application response since last reset.
     * 
     * @return average time for target application response.
     */
    private long getTargetAppResponseTimeAvg ()
    {
        return (this.targetAppCallCount == 0) ? 0 : this.targetAppResponseTime / this.targetAppCallCount;
    }

    /**
     * Get the minimum response time for the target application.
     * 
     * @return the minimum response time for the target application.
     */
    private long getTartegAppResponseTimeMin ()
    {
        return this.targetAppResponseTimeMin;
    }

    /**
     * Get the maximum response time for the target application.
     * 
     * @return the maximum response time for the target application.
     */
    private long getTargetAppResponseTimeMax ()
    {
        return this.targetAppResponseTimeMax;
    }

    /**
     * Get the number of times the target application was unavailable.
     * 
     * @return the number of times the target application was unavailable.
     */
    private long getTargetAppUnavailable ()
    {
        return this.targetAppUnavailable;
    }

    /**
     * Get the number of times the target application errored.
     * 
     * @return the number of times the target application errored.
     */
    private long getTargetAppMiscErrors ()
    {
        return this.targetAppMiscErrors;
    }

    /**
     * Get the number of times the target application encountered SOAP error.
     * 
     * @return the number of times the target application encountered SOAP error.
     */
    private long getTargetAppSoapErrors ()
    {
        return this.targetAppSoapErrors;
    }

    /**
     * Get the number of target application response timeouts.
     * 
     * @return the number of target application response timeouts.
     */
    private long getTargetAppResponseTimeouts ()
    {
        return this.targetAppResponseTimeouts;
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

    /**
     * Get the number of seconds since start or the last reset.
     * 
     * @return the number of business exceptions.
     */
    private long getElapsedTime ()
    {
        // Get current time
        final long now = new GregorianCalendar ().getTimeInMillis ();

        return now - this.getResetTime ();
    }

    /**
     * Get the number of seconds since start or the last reset.
     * 
     * @return the number of business exceptions.
     */
    private long getResetTime ()
    {
        return this.resetTime;
    }

    /**
     * Get the formatted rate for the count over the elapsed time.
     * 
     * @param count the count during the the elapsed time.
     * @param elapsedTime the period in which to measure the rate.
     * @return the formatted rate for the count over the elapsed time.
     */
    private String getRate (final long count, final long elapsedTime)
    {
        final DecimalFormat df = new DecimalFormat ("#0.00000");

        return df.format (count / (elapsedTime / MILLISECONDS));
    }

    /**
     * Get the number of business exceptions.
     * 
     * @return the number of business exceptions.
     */
    private String getLastBusinessException ()
    {
        return this.lastBusinessException;
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
        if (databaseReadsTime < this.databaseReadsTimeMin)
        {
            this.databaseReadsTimeMin = databaseReadsTime;
        }

        // Update the maximum if needed.
        if (databaseReadsTime > this.databaseReadsTimeMax)
        {
            this.databaseReadsTimeMax = databaseReadsTime;
        }

        this.databaseCallsTime += databaseReadsTime;

        // Update the minimum if needed.
        if (databaseReadsTime < this.databaseCallsTimeMin)
        {
            this.databaseCallsTimeMin = databaseReadsTime;
        }

        // Update the maximum if needed.
        if (databaseReadsTime > this.databaseCallsTimeMax)
        {
            this.databaseCallsTimeMax = databaseReadsTime;
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
        if (databaseWritesTime < this.databaseWritesTimeMin)
        {
            this.databaseWritesTimeMin = databaseWritesTime;
        }

        // Update the maximum if needed.
        if (databaseWritesTime > this.databaseWritesTimeMax)
        {
            this.databaseWritesTimeMax = databaseWritesTime;
        }

        this.databaseCallsTime += databaseWritesTime;

        // Update the minimum if needed.
        if (databaseWritesTime < this.databaseCallsTimeMin)
        {
            this.databaseCallsTimeMin = databaseWritesTime;
        }

        // Update the maximum if needed.
        if (databaseWritesTime > this.databaseCallsTimeMax)
        {
            this.databaseCallsTimeMax = databaseWritesTime;
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
    public void upRequestQueueCount ()
    {
        this.requestQueueCount += 1;
    }

    @Override
    public void addRequestQueueTime (final long requestQueueWriteTime)
    {
        // Find how long the message has been on the queue.
        final long requestQueueReadTime = new GregorianCalendar ().getTimeInMillis ();
        final long elapsedTime = requestQueueReadTime - requestQueueWriteTime;

        // Add to total time on queue of all messages.
        this.requestQueueTime += elapsedTime;

        // Update the minimum if needed.
        if (elapsedTime < this.requestQueueTimeMin)
        {
            this.requestQueueTimeMin = elapsedTime;
        }

        // Update the maximum if needed.
        if (elapsedTime > this.requestQueueTimeMax)
        {
            this.requestQueueTimeMax = elapsedTime;
        }
    }

    @Override
    public void upRequestQueueLength ()
    {
        this.requestQueueLength += 1;
        LOGGER.debug ("Upping queue length to " + this.requestQueueLength);

        if (this.requestQueueLength > this.requestQueueLengthMax)
        {
            this.requestQueueLengthMax = this.requestQueueLength;
        }
    }

    @Override
    public void decrementRequestQueueLength ()
    {
        this.requestQueueLength -= 1;
        LOGGER.debug ("Decrementing queue length to " + this.requestQueueLength);
    }

    @Override
    public void upRequestRequeues ()
    {
        this.requestRequeues += 1;
    }

    @Override
    public void addTargetAppResponseTime (final long targetAppResponseTime)
    {
        this.targetAppResponseTime += targetAppResponseTime;

        // Update the minimum if needed.
        if (targetAppResponseTime < this.targetAppResponseTimeMin)
        {
            this.targetAppResponseTimeMin = targetAppResponseTime;
        }

        // Update the maximum if needed.
        if (targetAppResponseTime > this.targetAppResponseTimeMax)
        {
            this.targetAppResponseTimeMax = targetAppResponseTime;
        }
    }

    @Override
    public void upTargetAppCallCount ()
    {
        this.targetAppCallCount++;
    }

    @Override
    public void upTargetAppUnavailable ()
    {
        this.targetAppUnavailable += 1;
    }

    @Override
    public void upTargetAppMiscErrors ()
    {
        this.targetAppMiscErrors += 1;
    }

    @Override
    public void upTargetAppSoapErrors ()
    {
        this.targetAppSoapErrors += 1;
    }

    @Override
    public void upTargetAppResponseTimeouts ()
    {
        this.targetAppResponseTimeouts += 1;
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
    public void setLastBusinessException (final String lastBusinessException)
    {
        this.lastBusinessException = lastBusinessException;
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
        LOGGER.debug ("Resetting SDT metrics");

        this.bulkSubmitCounts = 0;
        this.bulkSubmitLastTime = 0;
        this.bulkSubmitTime = 0;
        this.bulkSubmitTimeMin = Long.MAX_VALUE;
        this.bulkSubmitTimeMax = 0;
        this.bulkFeedbackCounts = 0;
        this.bulkFeedbackLastTime = 0;
        this.bulkFeedbackTime = 0;
        this.bulkFeedbackTimeMin = Long.MAX_VALUE;
        this.bulkFeedbackTimeMax = 0;
        this.submitQueryCounts = 0;
        this.submitQueryLastTime = 0;
        this.submitQueryTime = 0;
        this.submitQueryTimeMin = Long.MAX_VALUE;
        this.submitQueryTimeMax = 0;
        this.bulkStatusUpdateCount = 0;
        this.requestStatusUpdateCount = 0;
        this.completedBulkSubmitCount = 0;
        this.requestCount = 0;
        this.resetTime = 0;
        this.domainObjectsCount = 0;
        this.databaseCallsCount = 0;
        this.databaseCallsTime = 0;
        this.databaseCallsTimeMin = Long.MAX_VALUE;
        this.databaseCallsTimeMax = 0;
        this.databaseReadsCount = 0;
        this.databaseReadsTime = 0;
        this.databaseReadsTimeMin = Long.MAX_VALUE;
        this.databaseReadsTimeMax = 0;
        this.databaseWritesCount = 0;
        this.databaseWritesTime = 0;
        this.databaseWritesTimeMin = Long.MAX_VALUE;
        this.databaseWritesTimeMax = 0;
        this.activeCustomers = 0;
        this.requestQueueCount = 0;
        this.requestQueueTime = 0;
        this.requestQueueTimeMin = Long.MAX_VALUE;
        this.requestQueueTimeMax = 0;
        this.requestQueueLength = 0;
        this.requestQueueLengthMax = 0;
        this.requestRequeues = 0;
        this.targetAppCallCount = 0;
        this.targetAppResponseTime = 0;
        this.targetAppResponseTimeMin = Long.MAX_VALUE;
        this.targetAppResponseTimeMax = 0;
        this.targetAppUnavailable = 0;
        this.targetAppMiscErrors = 0;
        this.targetAppSoapErrors = 0;
        this.targetAppResponseTimeouts = 0;
        this.xmlValidationFailureCount = 0;
        this.businessExceptionCount = 0;
        this.lastBusinessException = "";
        this.lastBulkSubmitRef = "";
        this.lastBulkRequestRef = "";
        this.resetTime = new GregorianCalendar ().getTimeInMillis ();
    }

    @Override
    public long getActiveCustomers ()
    {
        return this.activeCustomers;
    }

    @Override
    public String getBulkSubmitStats ()
    {

        return "Bulk submits: count[" + this.getBulkSubmitCounts () + "], last[" +
                this.formatter.format (bulkSubmitLastTime) + "], rate[" +
                this.getRate (this.getBulkSubmitCounts (), this.getElapsedTime ()) + "], requests[" +
                this.getRequestCount () + "], time[" + this.getBulkSubmitTime () + "], average[" +
                this.getBulkSubmitTimeAvg () + "], minimum[" + this.getBulkSubmitTimeMin () + "], maximum[" +
                this.getBulkSubmitTimeMax () + "]";
    }

    @Override
    public String getTime ()
    {
        // Format time.
        final Date date = new Date ();
        return "Current time[" + this.formatter.format (date) + "], last reset time[" +
                this.formatter.format (this.getResetTime ()) + "]";
    }

    @Override
    public String getBulkFeedbackStats ()
    {
        return "Bulk feedbacks: count[" + this.getBulkFeedbackCounts () + "], last[" +
                this.formatter.format (bulkFeedbackLastTime) + "], rate[" +
                this.getRate (this.getBulkFeedbackCounts (), this.getElapsedTime ()) + "], time[" +
                this.getBulkFeedbackTime () + "], average[" + this.getBulkFeedbackTimeAvg () + "], minimum[" +
                this.getBulkFeedbackTimeMin () + "], maximum[" + this.getBulkFeedbackTimeMax () + "]";
    }

    @Override
    public String getSubmitQueryStats ()
    {
        return "Submit queries: count[" + this.getSubmitQueryCounts () + "], last[" +
                this.formatter.format (submitQueryLastTime) + "], rate[" +
                this.getRate (this.getSubmitQueryCounts (), this.getElapsedTime ()) + "], time[" +
                this.getSubmitQueryTime () + "], average[" + this.getSubmitQueryTimeAvg () + "], minimum[" +
                this.getSubmitQueryTimeMin () + "], maximum[" + this.getSubmitQueryTimeMax () + "]";
    }

    @Override
    public String getStatusUpdateStats ()
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
                "], maximum[" + this.getRequestQueueTimeMax () + "], current queue length[" +
                this.getRequestQueueLength () + "], max queue length[" + this.getRequestQueueLengthMax () +
                "], requeues[" + this.getRequestRequeues () + "]";

    }

    @Override
    public String getTargetAppStats ()
    {
        return "Target App Calls: count[" + this.getTargetAppCallCount () + "], rate[" +
                this.getRate (this.getTargetAppCallCount (), this.getElapsedTime ()) + "], time[" +
                this.getTargetAppResponseTime () + "], average[" + this.getTargetAppResponseTimeAvg () + "], minimum[" +
                this.getTartegAppResponseTimeMin () + "], maximum[" + this.getTargetAppResponseTimeMax () +
                "], timeouts[" + this.getTargetAppResponseTimeouts () + "], unavailable[" +
                this.getTargetAppUnavailable () + "], misc errors[" + this.getTargetAppMiscErrors () +
                "], soap errors[" + this.getTargetAppSoapErrors () + "]";
    }

    @Override
    public String getErrorStats ()
    {
        return "Errors and exceptions: xml validation errors[" + this.getXmlValidationFailureCount () +
                "], business exceptions[" + this.getBusinessExceptionCount () + "], last business exceptions[" +
                this.getLastBusinessException () + "]";
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
    public short getPerformanceLoggingFlags ()
    {
        return this.performanceLoggingFlags;
    }

    @Override
    public void uncache ()
    {
        this.cacheResetControl++;
    }

    @Override
    public void setPerformanceLoggingFlags (final short performanceLoggingFlags)
    {
        this.performanceLoggingFlags = performanceLoggingFlags;
    }

    // TODO - add function to reload log4j config on demand.

    /**
     * Get singleton instance of this bean - used by callers to update statistics.
     * 
     * @return singleton instance of this bean.
     */
    public static SdtMetricsMBean getSdtMetrics ()
    {
        if (thisBean == null)
        {
            thisBean = new SdtMetricsMBean ();
            LOGGER.debug ("Initialised SdtMetricsMBean");
        }
        return SdtMetricsMBean.thisBean;
    }
}
