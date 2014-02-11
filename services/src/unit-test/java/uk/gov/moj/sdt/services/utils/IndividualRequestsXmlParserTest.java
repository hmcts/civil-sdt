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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.Utilities;

/**
 * Test class for the IndividualRequestsXmlParser.
 * 
 * @author D303894
 * 
 */
public class IndividualRequestsXmlParserTest
{
    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (IndividualRequestsXmlParserTest.class);

    /**
     * The individual request xml parser instance for testing.
     */
    private IndividualRequestsXmlParser individualRequestsXmlParser;

    /**
     * Set up the classes and data required for testing.
     */
    @Before
    public void setUp ()
    {
        this.individualRequestsXmlParser = new IndividualRequestsXmlParser ();

        final Map<String, String> replacementNamespaces = new HashMap<String, String> ();
        replacementNamespaces.put ("http://ws.sdt.moj.gov.uk/2013/sdt/BulkRequestSchema",
                "http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/IndvRequestSchema");

        individualRequestsXmlParser.setReplacementNamespaces (replacementNamespaces);
    }

    /**
     * Test the method to get individual requests raw xml map.
     * 
     * @throws Exception if there is any IO problems
     */
    @Test
    public void getIndividualRequestsRawXmlMap () throws Exception
    {
        // Load xml into SdtContext as if the inbound interceptor had run.
        final String rawXml = Utilities.getRawXml ("src/unit-test/resources/", "testXMLValid.xml");

        SdtContext.getContext ().setRawInXml (rawXml);

        final List<IIndividualRequest> requests = new ArrayList<IIndividualRequest> ();

        // Create array list of individual requests as if these had been created by CXF in parsing the inbound SOAP
        // message and then transformed into domain objects.
        final IIndividualRequest individualRequest = new IndividualRequest ();
        individualRequest.setCustomerRequestReference ("1");
        individualRequest.setRequestStatus ("Forwarded");

        final IIndividualRequest individualRequest2 = new IndividualRequest ();
        individualRequest2.setCustomerRequestReference ("2");
        individualRequest2.setRequestStatus ("Forwarded");

        final IIndividualRequest individualRequest3 = new IndividualRequest ();
        individualRequest3.setCustomerRequestReference ("3");
        individualRequest3.setRequestStatus ("Forwarded");

        requests.add (individualRequest);
        requests.add (individualRequest2);
        requests.add (individualRequest3);

        // Now call the parser to add the xml fragments into the payload of the individual reauests.
        this.individualRequestsXmlParser.populateRawRequest (requests);

        // CHECKSTYLE:OFF
        Assert.assertEquals (
                "Failed to find correct payload for request 1",
                "<bul:mcolClaimStatusUpdatexmlns:bul=\"http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/IndvRequestSchema\"xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\"><cla1:claimNumber>claim123</cla1:claimNumber><cla1:defendantId>1</cla1:defendantId><cla1:notificationType>MP</cla1:notificationType><cla1:paidInFullDate>2012-01-01</cla1:paidInFullDate></bul:mcolClaimStatusUpdate>",
                requests.get (0).getRequestPayload ().replaceAll ("\\s+", ""));
        Assert.assertEquals (
                "Failed to find correct payload for request 2",
                "<bul:mcolClaimStatusUpdatexmlns:bul=\"http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/IndvRequestSchema\"xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\"><cla1:claimNumber>claim124</cla1:claimNumber><cla1:defendantId>1</cla1:defendantId><cla1:notificationType>MP</cla1:notificationType><cla1:paidInFullDate>2012-02-01</cla1:paidInFullDate></bul:mcolClaimStatusUpdate>",
                requests.get (1).getRequestPayload ().replaceAll ("\\s+", ""));
        Assert.assertEquals (
                "Failed to find correct payload for request 3",
                "<bul:mcolClaimxmlns:bul=\"http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/IndvRequestSchema\"xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\"><cla1:claimNumber>claim125</cla1:claimNumber><cla1:defendantId>1</cla1:defendantId><cla1:notificationType>MP</cla1:notificationType><cla1:paidInFullDate>2012-03-01</cla1:paidInFullDate></bul:mcolClaim>",
                requests.get (2).getRequestPayload ().replaceAll ("\\s+", ""));
        // CHECKSTYLE:ON
    }

    /**
     * Tests performance of parsing large xml request.
     * 
     * @throws IOException if unable to read file.
     */
    @Test (timeout = 2000)
    public void testParserPerformance () throws IOException
    {
        // Load xml into SdtContext as if the inbound interceptor had run.
        final String rawXml = Utilities.getRawXml ("src/unit-test/resources/", "testLargeBulkRequest.xml");

        SdtContext.getContext ().setRawInXml (rawXml);

        final List<IIndividualRequest> requests = new ArrayList<IIndividualRequest> ();

        // Create array list of individual requests as if these had been created by CXF in parsing the inbound SOAP
        // message and then transformed into domain objects.
        for (int i = 1; i <= 2000; i++)
        {

            final IIndividualRequest individualRequest = new IndividualRequest ();
            individualRequest.setCustomerRequestReference ("Req0-" + i);
            individualRequest.setRequestStatus ("Forwarded");
            requests.add (individualRequest);

        }

        LOGGER.info ("Start parsing for " + requests.size () + " requests");

        // Now call the parser to add the xml fragments into the payload of the individual requests.
        this.individualRequestsXmlParser.populateRawRequest (requests);

        LOGGER.info ("Completed parsing for " + requests.size () + " requests");

        int requestIndex = 0;
        for (IIndividualRequest request : requests)
        {
            requestIndex++;
            Assert.assertNotNull ("Payload should have been populated in request#" + requestIndex,
                    request.getRequestPayload ());
        }
    }

}
