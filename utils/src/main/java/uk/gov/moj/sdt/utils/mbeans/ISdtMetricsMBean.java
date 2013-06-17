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

/**
 * Interface for all classes implementing {@link CopyOfISdtMetricsMBean}.
 * 
 * @author Robin Compston
 */
public interface ISdtMetricsMBean
{

    /**
     * Get summary of bulk submit statistics.
     * 
     * @return summary of bulk submit statistics.
     */
    String getBulkSubmitStats ();

    /**
     * Get summary of bulk feedback statistics.
     * 
     * @return summary of bulk feedback statistics.
     */
    String getBulkFeedbackStats ();

    /**
     * Get summary of defence feedback  statistics.
     * 
     * @return summary of defence feedback statistics.
     */
    String getDefenceFeedbackStats ();

    /**
     * Get summary of status statistics.
     * 
     * @return summary of status statistics.
     */
    String getStatusStats ();

    /**
     * Get the domain objects statistics.
     * 
     * @return the domain objects statistics.
     */
    String getDomainObjectsStats ();

    /**
     * Get the database statistics.
     * 
     * @return the database statistics.
     */
    String getDatabaseStats ();

    /**
     * Get the active customers statistics.
     * 
     * @return the active customers statistics.
     */
    String getActiveCustomersStats ();

    /**
     * Get the request queue statistics.
     * 
     * @return the request queue statistics.
     */
    String getRequestQueueStats ();

    /**
     * Get the case management system response statistics.
     * 
     * @return the case management system response statistics.
     */
    String getCaseMgmtStats ();

    /**
     * Get the error and exception statistics.
     * 
     * @return the error and exception statistics.
     */
    String getErrorStats ();

    /**
     * Get the last bulk submit reference assiged. 
     * 
     * @return the last bulk submit reference.
     */
    String getLastBulkSubmitRef ();

    /**
     * Get the last bulk request reference assiged. 
     * 
     * @return the last bulk request reference.
     */
    String getLastBulkRequestRef ();

    /**
     * Reset all metrics to initial value.
     */
    void reset ();
    
    /**
     * Increment count of bulk submits.
     */
    void upBulkSubmitCounts ();

    /**
     * Add latest bulk submit time to total.
     * 
     * @param bulkSubmitTime time to add to total bulk submit time.
     */
    void addBulkSubmitTime (final long bulkSubmitTime);

    /**
     * Increment count of bulk Feedbacks.
     */
    void upBulkFeedbackCounts ();

    /**
     * Add latest bulk Feedback time to total.
     * 
     * @param bulkFeedbackTime time to add to total bulk feedback time.
     */
    void addBulkFeedbackTime (final long bulkFeedbackTime);

    /**
     * Increment count of defence feedbacks.
     */
    void upDefenceFeedbackCounts ();

    /**
     * Add latest defence feedback time to total.
     * 
     * @param defenceFeedbackTime time to add to total defence feedback time.
     */
    void addDefenceFeedbackTime (final long defenceFeedbackTime);

    /**
     * Increment the bulk status update count.
     */
    void upBulkStatusUpdateCount ();

    /**
     * Increment the request status update count.
     */
    void upRequestStatusUpdateCount ();

    /**
     * Increment the domain objects count.
     */
    void upDomainObjectsCount ();

    /**
     * Decrement the domain objects count.
     */
    void downDomainObjectsCount ();

    /**
     * Increment the database calls count.
     */
    void upDatabaseCallsCount ();

    /**
     * Add to the total database call time.
     * 
     * @param databaseCallsTime time to add to database calls time.
     */
    void addDatabaseCallsTime (final long databaseCallsTime);

    /**
     * Increment the active customers count.
     */
    void upActiveCustomers ();

    /**
     * Increment the completed bulk submit count.
     */
    void upCompletedBulkSubmitCount ();

    /**
     * Increment the request count.
     */
    void upRequestCount ();

    /**
     * Increment the queued request count.
     */
    void upRequestQueuedCount ();

    /**
     * Add latest request queued time to total.
     * 
     * @param requestQueuedTime latest request queued time to total.
     */
    void addRequestQueuedTime (final long requestQueuedTime);

    /**
     * Increment request length.
     */
    void upRequestQueueLength ();

    /**
     * Decrement request length.
     */
    void decrementRequestQueueLength ();

    /**
     * Increment the request requeues count.
     */
    void upRequestRequeues ();

    /**
     * Add latest case management response time to total.
     * 
     * @param caseMgmtResponseTime latest case management response.
     */
    void addCaseMgmtResponseTime (final long caseMgmtResponseTime);

    /**
     * Increment the case management response timeout count.
     */
    void upCaseMgmtResponseTimeouts ();

    /**
     * Increment the errors count.
     */
    void upErrorsCount ();

    /**
     * Increment the XML validation failure count.
     */
    void upXmlValidationFailureCount ();

    /**
     * Increment the business exception count.
     */
    void upBusinessExceptionCount ();

    /**
     * Set the last bulk submit reference assigned by the system.
     * 
     * @param lastBulkSubmitRef the last bulk submit reference assigned.
     */
    void setLastBulkSubmitRef (final String lastBulkSubmitRef);

    /**
     * Set the last bulk request reference assigned by the system.
     * 
     * @param lastBulkRequestRef the last bulk request reference assigned.
     */
    void setLastBulkRequestRef (final String lastBulkRequestRef);

}