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
package uk.gov.moj.sdt.services.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.Utilities;import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for the IndividualRequestsXmlParser.
 *
 * @author D303894
 */
class IndividualRequestsXmlParserTest extends AbstractSdtUnitTestBase {

    private static final String FORWARDED = "Forwarded";

    /**
     * The individual request xml parser instance for testing.
     */
    private IndividualRequestsXmlParser individualRequestsXmlParser;

    /**
     * Set up the classes and data required for testing.
     */
    @BeforeEach
    @Override
    public void setUp() {
        this.individualRequestsXmlParser = new IndividualRequestsXmlParser();

        final Map<String, String> replacementNamespaces = new HashMap<>();
        replacementNamespaces.put("http://ws.sdt.moj.gov.uk/2013/sdt/BulkRequestSchema",
                "http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/IndvRequestSchema");

        individualRequestsXmlParser.setReplacementNamespaces(replacementNamespaces);
    }

    /**
     * Test the method to get individual requests raw xml map.
     *
     * @throws Exception if there is any IO problems
     */
    @Test
    void getIndividualRequestsRawXmlMap() throws Exception {
        // Load xml into SdtContext as if the inbound interceptor had run.
        final String rawXml = Utilities.getRawXml("src/unit-test/resources/", "testXMLValid.xml");

        SdtContext.getContext().setRawInXml(rawXml);

        final List<IIndividualRequest> requests = new ArrayList<>();

        // Create array list of individual requests as if these had been created by CXF in parsing the inbound SOAP
        // message and then transformed into domain objects.
        final IIndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setCustomerRequestReference("1");
        individualRequest.setRequestStatus(FORWARDED);

        final IIndividualRequest individualRequest2 = new IndividualRequest();
        individualRequest2.setCustomerRequestReference("2");
        individualRequest2.setRequestStatus(FORWARDED);

        final IIndividualRequest individualRequest3 = new IndividualRequest();
        individualRequest3.setCustomerRequestReference("3");
        individualRequest3.setRequestStatus(FORWARDED);

        requests.add(individualRequest);
        requests.add(individualRequest2);
        requests.add(individualRequest3);

        // Now call the parser to add the xml fragments into the payload of the individual reauests.
        this.individualRequestsXmlParser.populateRawRequest(requests);

        // CHECKSTYLE:OFF
        assertEquals(
                "<bul:mcolClaimStatusUpdatexmlns:bul=\"http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/IndvRequestSchema\"><cla1:claimNumberxmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">claim123</cla1:claimNumber><cla1:defendantIdxmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">1</cla1:defendantId><cla1:notificationTypexmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">MP</cla1:notificationType><cla1:paidInFullDatexmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">2012-01-01</cla1:paidInFullDate></bul:mcolClaimStatusUpdate>",
                requests.get(0).getRequestPayload().replaceAll("\\s+", ""),
                "Failed to find correct payload for request 1");
        assertEquals(
                "<bul:mcolClaimStatusUpdatexmlns:bul=\"http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/IndvRequestSchema\"><cla1:claimNumberxmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">claim124</cla1:claimNumber><cla1:defendantIdxmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">1</cla1:defendantId><cla1:notificationTypexmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">MP</cla1:notificationType><cla1:paidInFullDatexmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">2012-02-01</cla1:paidInFullDate></bul:mcolClaimStatusUpdate>",
                requests.get(1).getRequestPayload().replaceAll("\\s+", ""),
                "Failed to find correct payload for request 2");
        assertEquals(
                "<bul:mcolClaimxmlns:bul=\"http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/IndvRequestSchema\"><cla1:claimNumberxmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">claim125</cla1:claimNumber><cla1:defendantIdxmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">1</cla1:defendantId><cla1:notificationTypexmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">MP</cla1:notificationType><cla1:paidInFullDatexmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">2012-03-01</cla1:paidInFullDate></bul:mcolClaim>",
                requests.get(2).getRequestPayload().replaceAll("\\s+", ""),
                "Failed to find correct payload for request 3");
        // CHECKSTYLE:ON
    }

    /**
     * Tests performance of parsing large xml request.
     *
     * @throws IOException if unable to read file.
     */
    @Test
    @Timeout(30000)
    void testParserPerformance() throws IOException {
        // Load xml into SdtContext as if the inbound interceptor had run.
        final String rawXml = Utilities.getRawXml("src/unit-test/resources/", "testLargeBulkRequest.xml");

        SdtContext.getContext().setRawInXml(rawXml);

        final List<IIndividualRequest> requests = new ArrayList<>();

        // Create array list of individual requests as if these had been created by CXF in parsing the inbound SOAP
        // message and then transformed into domain objects.
        for (int i = 1; i <= 2000; i++) {

            final IIndividualRequest individualRequest = new IndividualRequest();
            individualRequest.setCustomerRequestReference("Req0-" + i);
            individualRequest.setRequestStatus(FORWARDED);
            requests.add(individualRequest);

        }

        // Now call the parser to add the xml fragments into the payload of the individual requests.
        this.individualRequestsXmlParser.populateRawRequest(requests);

        int requestIndex = 0;
        for (IIndividualRequest request : requests) {
            requestIndex++;
            assertNotNull(request.getRequestPayload(),
                    "Payload should have been populated in request#" + requestIndex);
        }
    }

}
