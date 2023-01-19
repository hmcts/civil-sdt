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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IServiceRequest;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.*;


public class BulkSubmissionTest extends AbstractSdtUnitTestBase {

    private IBulkSubmission bulkSubmission;

    private IServiceRequest mockServiceRequest;

    private  final String PAYLOAD = "Payload";

    @BeforeEach
    @Override
    public void setUpLocalTests() {
        MockitoAnnotations.openMocks(this);
        mockServiceRequest = Mockito.mock(ServiceRequest.class);
        bulkSubmission = new BulkSubmission();
        bulkSubmission.setErrorCode("ERROR");
        bulkSubmission.setServiceRequest(mockServiceRequest);
        bulkSubmission.setId(1L);
        bulkSubmission.setPayload(PAYLOAD);
        bulkSubmission.setErrorText("This is an Error");

    }
        @DisplayName("Test Bulk Submission")
        @Test
        public void testBulkSubmission(){
        //given
            bulkSubmission.setSubmissionStatus("Uploaded");
        //when
        bulkSubmission.markAsValidated();
        //then

            assertNotNull(bulkSubmission.getServiceRequest(),"Should return a ServiceRequest object");
            assertNotNull(bulkSubmission.getErrorCode(),"Error code should be set");
            assertNotEquals(
                bulkSubmission.getSubmissionStatus(),
                IBulkSubmission.BulkRequestStatus.COMPLETED.getStatus()
            );

        assertEquals(bulkSubmission.getSubmissionStatus(),"Validated");
        assertNotNull(bulkSubmission.getErrorText());
        assertTrue(bulkSubmission.hasError());

        }

    @DisplayName("Test Bulk Submission Status")
    @Test
    public void testBulkSubmissionStatus(){
        //given
        bulkSubmission.setSubmissionStatus("Completed");
        //when
        bulkSubmission.markAsValidated();
        //then
        assertNotEquals(bulkSubmission.getSubmissionStatus(),"Validated");
        assertEquals(bulkSubmission.getPayload(),PAYLOAD);

    }

    @DisplayName("Test Bulk Submission toString")
    @Test
    public void testBulkSubmissionToString(){
        assertNotNull(bulkSubmission.toString(), "Should contain something");
        assertEquals(bulkSubmission.getPayload(),PAYLOAD);
    }

    @DisplayName("Test Bulk Submission getId")
    @Test
    public void testBulkSubmissionGetId(){
        assertNotNull(bulkSubmission.getId());
    }

    @DisplayName("Test Bulk Submission hasError")
    @Test
    public void testBulkSubmissionHasError(){
        bulkSubmission.setErrorCode(null);
        assertFalse(bulkSubmission.hasError());
    }

}
