/* Copyrights and Licenses
 * 
 * Copyright (c) 2012-2013 by the Ministry of Justice. All rights reserved.
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

package uk.gov.moj.sdt.domain;

import org.joda.time.LocalDateTime;

import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IMessageLog;

/**
 * Message Log.
 * 
 * @author d130680
 * 
 */
public class MessageLog extends AbstractDomainObject implements IMessageLog
{

    /**
     * One of SBR, RBF, RDF.
     * 
     * SBR - Submit Bulk Request
     * RBF - Request Bulk Feedback
     * RDF - Submit Query (previously known Request Defence)
     */
    private String loggedEvent;

    /**
     * One of I or O.
     */
    private String direction;

    /**
     * Bulk Submission.
     */
    private IBulkSubmission bulkSubmission;

    /**
     * Effectively another FK from bulk_submissions.
     */
    private String sdtBulkReference;

    /**
     * External reference supplied by the submitting system.
     */
    private String customerReference;

    /**
     * Number of component requests in the bulk submission.
     */
    private int numberOfRequests;

    /**
     * Date record was created.
     */
    private LocalDateTime createdDate;

    /**
     * Date record was updated.
     */
    private LocalDateTime updatedDate;

    /**
     * Y to indicate the record should be excluded from any purges.
     */
    private String userInvestigation;

    /**
     * XML payload.
     */
    private String payload;

    @Override
    public String getLoggedEvent ()
    {
        return loggedEvent;
    }

    @Override
    public void setLoggedEvent (final String loggedEvent)
    {
        this.loggedEvent = loggedEvent;
    }

    @Override
    public String getDirection ()
    {
        return direction;
    }

    @Override
    public void setDirection (final String direction)
    {
        this.direction = direction;
    }

    @Override
    public IBulkSubmission getBulkSubmission ()
    {
        return bulkSubmission;
    }

    @Override
    public void setBulkSubmission (final IBulkSubmission bulkSubmission)
    {
        this.bulkSubmission = bulkSubmission;
    }

    @Override
    public String getSdtBulkReference ()
    {
        return sdtBulkReference;
    }

    @Override
    public void setSdtBulkReference (final String sdtBulkReference)
    {
        this.sdtBulkReference = sdtBulkReference;
    }

    @Override
    public String getCustomerReference ()
    {
        return customerReference;
    }

    @Override
    public void setCustomerReference (final String customerReference)
    {
        this.customerReference = customerReference;
    }

    @Override
    public int getNumberOfRequests ()
    {
        return numberOfRequests;
    }

    @Override
    public void setNumberOfRequests (final int numberOfRequests)
    {
        this.numberOfRequests = numberOfRequests;
    }

    @Override
    public LocalDateTime getCreatedDate ()
    {
        return createdDate;
    }

    @Override
    public void setCreatedDate (final LocalDateTime createdDate)
    {
        this.createdDate = createdDate;
    }

    @Override
    public LocalDateTime getUpdatedDate ()
    {
        return updatedDate;
    }

    @Override
    public void setUpdatedDate (final LocalDateTime updatedDate)
    {
        this.updatedDate = updatedDate;
    }

    @Override
    public String getUserInvestigation ()
    {
        return userInvestigation;
    }

    @Override
    public void setUserInvestigation (final String userInvestigation)
    {
        this.userInvestigation = userInvestigation;
    }

    @Override
    public String getPayload ()
    {
        return payload;
    }

    @Override
    public void setPayload (final String payload)
    {
        this.payload = payload;
    }

}
