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

/**
 * Unit tests for {@link SubmitQueryRequest}.
 *
 * @author Ollie Smith
 */
package uk.gov.moj.sdt.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IServiceRequest;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Submit Query Request Test")
class SubmitQueryRequestTest extends AbstractSdtUnitTestBase {


    @Mock
    private IErrorLog mockErrorLog;

    private ISubmitQueryRequest submitQueryRequest;
    @BeforeEach
    @Override
    public void setUpLocalTests() {
        MockitoAnnotations.openMocks(this);
        submitQueryRequest = new SubmitQueryRequest();
        submitQueryRequest.setQueryReference("Ref");
        submitQueryRequest.setTargetApplicationResponse("mcol");
        submitQueryRequest.setErrorLog(mockErrorLog);
        submitQueryRequest.setCriteriaType("AType");
        submitQueryRequest.setStatus("Uploaded");
        submitQueryRequest.setResultCount(1);

        IServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setBulkCustomerId("1234");
        serviceRequest.setBulkReference("BulkRef01");
        serviceRequest.setRequestType("ReqType");
        serviceRequest.setRequestPayload("PayLoad");
        serviceRequest.setRequestDateTime(LocalDateTime.now());



    }


    @DisplayName("Test Submit Query Request")
    @Test
    void testSubmitQueryRequest(){submitQueryRequest.setErrorLog(mockErrorLog);
        assertNotNull(submitQueryRequest,"BulkCustomer Object should be populated");
        assertNotNull(submitQueryRequest.toString(),"Object toString should be populated");
        assertEquals(submitQueryRequest.getTargetApplicationResponse(),"mcol","TargetResponse is not equal");
        assertEquals(submitQueryRequest.getQueryReference(),"Ref","Query Reference is not equal");
        assertEquals(submitQueryRequest.getResultCount(), 1, "Result Count is not equal");
        assertTrue(submitQueryRequest.hasError(),"error log does exist");
        assertNotNull(submitQueryRequest.getStatus());
        assertNotNull(submitQueryRequest.getCriteriaType());

    }

    @DisplayName("Test Submit Query Request Error")
    @Test
    public void testSubmitQueryRequestError(){

        submitQueryRequest.setErrorLog(null);
        assertFalse(submitQueryRequest.hasError(),"error log should not exist");
    }

    @DisplayName("Test Submit Query Request Error Reject")
    @Test
    void testSubmitQueryRequestErrorReject(){

        submitQueryRequest.reject(mockErrorLog);
        assertTrue(submitQueryRequest.hasError(),"error log should not exist");
    }


}
