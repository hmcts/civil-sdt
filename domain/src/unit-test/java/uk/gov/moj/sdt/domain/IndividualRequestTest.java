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
 * $Id$
 * $LastChangedRevision$
 * $LastChangedDate$
 * $LastChangedBy$ */

package uk.gov.moj.sdt.domain;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.IIndividualRequest.IndividualRequestStatus;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

/**
 * Unit tests for {@link IndividualRequest}.
 * 
 * @author Saurabh Agarwal
 * 
 */
public class IndividualRequestTest extends AbstractSdtUnitTestBase
{
    /**
     * Test subject.
     */
    private IIndividualRequest individualRequest;

    /**
     * Set up test data.
     */
    @Before
    public void setUp ()
    {
        individualRequest = new IndividualRequest ();
    }

    /**
     * Tests that forwarding attempts is incremented correctly.
     */
    @Test
    public void testIncrementForwardingAttempts ()
    {
        individualRequest.incrementForwardingAttempts ();

        Assert.assertEquals ("Status is incorrect", IndividualRequestStatus.FORWARDED.getStatus (),
                individualRequest.getRequestStatus ());
        Assert.assertEquals ("Forwarding attempt count is incorrect", 1, individualRequest.getForwardingAttempts ());
        Assert.assertNotNull ("Updated date should be populated", individualRequest.getUpdatedDate ());

    }

    /**
     * Tests that request is marked as accepted correctly.
     */
    @Test
    public void testMarkRequestAsAccepted ()
    {
        individualRequest.markRequestAsAccepted ();

        Assert.assertEquals ("Status is incorrect", IndividualRequestStatus.ACCEPTED.getStatus (),
                individualRequest.getRequestStatus ());
        Assert.assertEquals ("Forwarding attempt count is incorrect", 0, individualRequest.getForwardingAttempts ());
        Assert.assertNotNull ("Updated date should be populated", individualRequest.getUpdatedDate ());
        Assert.assertNotNull ("Completed date should be populated", individualRequest.getCompletedDate ());

    }

    /**
     * Tests that request is marked as initially accepted correctly.
     */
    @Test
    public void testMarkRequestAsInitiallyAccepted ()
    {
        individualRequest.markRequestAsInitiallyAccepted ();

        Assert.assertEquals ("Status is incorrect", IndividualRequestStatus.INITIALLY_ACCEPTED.getStatus (),
                individualRequest.getRequestStatus ());
        Assert.assertEquals ("Forwarding attempt count is incorrect", 0, individualRequest.getForwardingAttempts ());
        Assert.assertNotNull ("Updated date should be populated", individualRequest.getUpdatedDate ());

    }

    /**
     * Tests that request is marked as awaiting data correctly.
     */
    @Test
    public void testMarkRequestAsAwaitingData ()
    {
        individualRequest.markRequestAsAwaitingData ();

        Assert.assertEquals ("Status is incorrect", IndividualRequestStatus.AWAITING_DATA.getStatus (),
                individualRequest.getRequestStatus ());
        Assert.assertEquals ("Forwarding attempt count is incorrect", 0, individualRequest.getForwardingAttempts ());
        Assert.assertNotNull ("Updated date should be populated", individualRequest.getUpdatedDate ());

    }

    /**
     * Tests that request is marked as rejected correctly.
     */
    @Test
    public void testMarkRequestAsRejected ()
    {
        final IErrorLog errorLog = new ErrorLog ();
        individualRequest.markRequestAsRejected (errorLog);

        Assert.assertEquals ("Status is incorrect", IndividualRequestStatus.REJECTED.getStatus (),
                individualRequest.getRequestStatus ());
        Assert.assertEquals ("Forwarding attempt count is incorrect", 0, individualRequest.getForwardingAttempts ());
        Assert.assertNotNull ("Updated date should be populated", individualRequest.getUpdatedDate ());
        Assert.assertNotNull ("Completed date should be populated", individualRequest.getCompletedDate ());
        Assert.assertEquals ("Error log should be populated", errorLog, individualRequest.getErrorLog ());
        Assert.assertEquals ("Individual request should be associated with error log", individualRequest,
                errorLog.getIndividualRequest ());
    }

    /**
     * Tests that forward attempt is reset correctly.
     */
    @Test
    public void testResetForwardingAttempts ()
    {
        individualRequest.resetForwardingAttempts ();

        Assert.assertEquals ("Status is incorrect", IndividualRequestStatus.RECEIVED.getStatus (),
                individualRequest.getRequestStatus ());
        Assert.assertEquals ("Forwarding attempt count is incorrect", 0, individualRequest.getForwardingAttempts ());
        Assert.assertNotNull ("Updated date should be populated", individualRequest.getUpdatedDate ());

    }

    /**
     * Tests that check for enqueuing request correct.
     */
    @Test
    public void testIsEnqueueable ()
    {
        individualRequest.setRequestStatus (IndividualRequestStatus.RECEIVED.getStatus ());
        Assert.assertTrue ("Request should be enqueueable", individualRequest.isEnqueueable ());

        individualRequest.setRequestStatus (IndividualRequestStatus.FORWARDED.getStatus ());
        Assert.assertFalse ("Request should not be enqueueable", individualRequest.isEnqueueable ());
    }

    /**
     * Tests that request reference is populated correctly.
     */
    @Test
    public void testPopulateSdtRequestReference ()
    {
        final IBulkSubmission bulkSubmission = new BulkSubmission ();
        bulkSubmission.setSdtBulkReference ("BULK-REF");

        individualRequest.setBulkSubmission (bulkSubmission);
        individualRequest.setLineNumber (1);
        individualRequest.populateReferences ();

        Assert.assertEquals ("Request reference is incorrect", "BULK-REF-0000001",
                individualRequest.getSdtRequestReference ());
    }
}
