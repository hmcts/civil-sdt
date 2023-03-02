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
 * Unit tests for {@link ServiceRequest}.
 *
 * @author Ollie Smith
 */
package uk.gov.moj.sdt.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.domain.api.IServiceRequest;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Service Request Test")
class ServiceRequestTest extends AbstractSdtUnitTestBase {

    /**
     * Set up test data.
     */

    private IServiceRequest serviceRequest;

    @BeforeEach
    @Override
    public void setUpLocalTests() {

        serviceRequest = new ServiceRequest();
        serviceRequest.setBulkCustomerId("1234");
        serviceRequest.setBulkReference("BulkRef01");
        serviceRequest.setRequestType("ReqType");
        serviceRequest.setRequestPayload("Request Payload");
        serviceRequest.setRequestDateTime(LocalDateTime.now());
        serviceRequest.setResponseDateTime(LocalDateTime.now());
        serviceRequest.setResponsePayload("Response Payload");
        serviceRequest.setServerHostName("SDT_MOCK_HOSTNAME");
        serviceRequest.setId(1L);
    }


    @DisplayName("Test Service Request")
    @Test
    void testServiceRequest() {

        String expected = "BulkRef01";
        String actual = serviceRequest.toString();
        assertTrue(actual.contains(expected), "Should contain something");

        assertNotNull(serviceRequest, "ServiceRequest Object should be populated");
        assertNotNull(serviceRequest.getBulkCustomerId(), "BulkCustomerId should be populated");
        assertNotNull(serviceRequest.getBulkReference(), "Bulk Reference should be populated");
        assertNotNull(serviceRequest.getRequestType(), "Request Type should be populated");
        assertNotNull(serviceRequest.getRequestPayload(), "Request Payload should be populated");
        assertNotNull(serviceRequest.getRequestDateTime(), "Request Date Time should be populated");
        assertNotNull(serviceRequest.getResponseDateTime(), "Response Date Time should be populated");
        assertNotNull(serviceRequest.getResponsePayload(), "Response Payload should be populated");
        assertNotNull(serviceRequest.getServerHostName(), "Server Host Name should be populated");
        assertNotNull(serviceRequest.getId());
    }

}



