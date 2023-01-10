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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.IIndividualRequest.IndividualRequestStatus;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link IndividualRequest}.
 *
 * @author Saurabh Agarwal
 */
class IndividualRequestTest extends AbstractSdtUnitTestBase {
    /**
     * Test subject.
     */
    private IIndividualRequest individualRequest;

    private static final String STATUS_IS_INCORRECT = "Status is incorrect";
    private static final String FORWARDING_ATTEMPT_COUNT_IS_INCORRECT = "Forwarding attempt count is incorrect";
    private static final String UPDATED_DATE_SHOULD_BE_POPULATED = "Updated date should be populated";

    /**
     * Set up test data.
     */
    @BeforeEach
    @Override
    public void setUp() {
        individualRequest = new IndividualRequest();
    }

    private void assertsCommon(IndividualRequestStatus expectedStatus, Integer expectedForwardingAttempts,
                               LocalDateTime updatedDate) {
        assertEquals(expectedStatus.getStatus(), individualRequest.getRequestStatus(), STATUS_IS_INCORRECT);
        assertEquals(expectedForwardingAttempts, individualRequest.getForwardingAttempts(),
                FORWARDING_ATTEMPT_COUNT_IS_INCORRECT);
        assertNotNull(updatedDate, UPDATED_DATE_SHOULD_BE_POPULATED);
    }

    /**
     * Tests that forwarding attempts is incremented correctly.
     */
    @Test
    void testIncrementForwardingAttempts() {
        individualRequest.incrementForwardingAttempts();

        assertsCommon(IndividualRequestStatus.FORWARDED, 1, individualRequest.getUpdatedDate());
    }

    /**
     * Tests that request is marked as accepted correctly.
     */
    @Test
    void testMarkRequestAsAccepted() {
        individualRequest.markRequestAsAccepted();

        assertsCommon(IndividualRequestStatus.ACCEPTED, 0, individualRequest.getUpdatedDate());
        assertNotNull(individualRequest.getCompletedDate(), "Completed date should be populated");
    }

    /**
     * Tests that request is marked as initially accepted correctly.
     */
    @Test
    void testMarkRequestAsInitiallyAccepted() {
        individualRequest.markRequestAsInitiallyAccepted();

        assertsCommon(IndividualRequestStatus.INITIALLY_ACCEPTED, 0, individualRequest.getUpdatedDate());
    }

    /**
     * Tests that request is marked as awaiting data correctly.
     */
    @Test
    void testMarkRequestAsAwaitingData() {
        individualRequest.markRequestAsAwaitingData();

        assertsCommon(IndividualRequestStatus.AWAITING_DATA, 0, individualRequest.getUpdatedDate());
    }

    /**
     * Tests that request is marked as rejected correctly.
     */
    @Test
    void testMarkRequestAsRejected() {
        final IErrorLog errorLog = new ErrorLog();
        individualRequest.markRequestAsRejected(errorLog);

        assertsCommon(IndividualRequestStatus.REJECTED, 0, individualRequest.getUpdatedDate());

        assertNotNull(individualRequest.getCompletedDate(), "Completed date should be populated");
        assertEquals(errorLog, individualRequest.getErrorLog(), "Error log should be populated");
        assertEquals(individualRequest, errorLog.getIndividualRequest(),
                "Individual request should be associated with error log");
    }

    /**
     * Tests that forward attempt is reset correctly.
     */
    @Test
    void testResetForwardingAttempts() {
        individualRequest.resetForwardingAttempts();

        assertsCommon(IndividualRequestStatus.RECEIVED, 0, individualRequest.getUpdatedDate());
    }

    /**
     * Tests that check for enqueuing request correct.
     */
    @Test
    void testIsEnqueueable() {
        individualRequest.setRequestStatus(IndividualRequestStatus.RECEIVED.getStatus());
        assertTrue(individualRequest.isEnqueueable(), "Request should be enqueueable");

        individualRequest.setRequestStatus(IndividualRequestStatus.FORWARDED.getStatus());
        assertFalse(individualRequest.isEnqueueable(), "Request should not be enqueueable");
    }

    /**
     * Tests that request reference is populated correctly.
     */
    @Test
    void testPopulateSdtRequestReference() {
        final IBulkSubmission bulkSubmission = new BulkSubmission();
        bulkSubmission.setSdtBulkReference("BULK-REF");

        individualRequest.setBulkSubmission(bulkSubmission);
        individualRequest.setLineNumber(1);
        individualRequest.populateReferences();

        assertEquals("BULK-REF-0000001", individualRequest.getSdtRequestReference(),
                "Request reference is incorrect");
    }

}
