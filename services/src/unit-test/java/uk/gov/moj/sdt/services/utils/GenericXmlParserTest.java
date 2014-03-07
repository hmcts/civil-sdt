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
 * $Id$
 * $LastChangedRevision$
 * $LastChangedDate$
 * $LastChangedBy$ */
package uk.gov.moj.sdt.services.utils;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.Utilities;

/**
 * Test class for the GenericXmlParser.
 * 
 * @author d276205
 * 
 */
public class GenericXmlParserTest
{
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (GenericXmlParserTest.class);

    /**
     * The xml parser instance for testing.
     */
    private GenericXmlParser genericXmlParser;

    /**
     * Set up the classes and data required for testing.
     */
    @Before
    public void setUp ()
    {
        this.genericXmlParser = new GenericXmlParser ();
    }

    /**
     * Test the method to get target app response xml for submit query response.
     * 
     * @throws Exception if there is any IO problems
     */
    @Test
    public void testParseSubmitQueryResponseSuccess () throws Exception
    {
        LOGGER.debug ("test successful scenario for submit query response.");

        genericXmlParser.setEnclosingTag ("targetAppDetail");
        final Map<String, String> replacementNamespaces = new HashMap<String, String> ();
        replacementNamespaces.put ("http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/SubmitQueryResponseSchema",
                "http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryResponseSchema");

        genericXmlParser.setReplacementNamespaces (replacementNamespaces);
        // Load xml into SdtContext as if the inbound interceptor had run.
        final String rawXml = Utilities.getRawXml ("src/unit-test/resources/", "testSubmitQueryResponse.xml");

        SdtContext.getContext ().setRawInXml (rawXml);

        // Now call the parser.
        final String result = this.genericXmlParser.parse ();

        // CHECKSTYLE:OFF
        Assert.assertEquals (
                "Failed to find expected response",
                "<sub:mcolDefenceDetailxmlns:sub=\"http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryResponseSchema\"xmlns:mquer=\"http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema\"><mquer:claimNumber>13548968</mquer:claimNumber><mquer:defendantdefendantId=\"1\"><mquer:filedDate>2001-12-31T12:00:00</mquer:filedDate><mquer:responseType>DE</mquer:responseType></mquer:defendant></sub:mcolDefenceDetail>",
                result.replaceAll ("\\s+", ""));
        // CHECKSTYLE:ON
    }

    /**
     * Test the method to get target app response xml for submit query response when multiple records are returned in
     * result.
     * 
     * @throws Exception if there is any IO problems
     */
    @Test
    public void testParseSubmitQueryResponseSuccessForMultipleResults () throws Exception
    {
        LOGGER.debug ("scenario for submit query response when multiple records are returned in result.");

        genericXmlParser.setEnclosingTag ("targetAppDetail");
        final Map<String, String> replacementNamespaces = new HashMap<String, String> ();
        replacementNamespaces.put ("http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/SubmitQueryResponseSchema",
                "http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryResponseSchema");

        genericXmlParser.setReplacementNamespaces (replacementNamespaces);
        // Load xml into SdtContext as if the inbound interceptor had run.
        final String rawXml =
                Utilities.getRawXml ("src/unit-test/resources/", "testSubmitQueryResponseMultipleResult.xml");

        SdtContext.getContext ().setRawInXml (rawXml);

        // Now call the parser.
        final String result = this.genericXmlParser.parse ();

        // CHECKSTYLE:OFF
        Assert.assertEquals (
                "Failed to find expected response",
                "<sub:mcolDefenceDetailxmlns:sub=\"http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryResponseSchema\"xmlns:mquer=\"http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema\"><mquer:claimNumber>13548968</mquer:claimNumber><mquer:defendantdefendantId=\"1\"><mquer:filedDate>2001-12-31T12:00:00</mquer:filedDate><mquer:responseType>DE</mquer:responseType></mquer:defendant></sub:mcolDefenceDetail><sub:mcolDefenceDetailxmlns:sub=\"http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryResponseSchema\"xmlns:mquer=\"http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema\"><mquer:claimNumber>13548969</mquer:claimNumber><mquer:defendantdefendantId=\"2\"><mquer:filedDate>2001-12-31T12:00:00</mquer:filedDate><mquer:responseType>DE</mquer:responseType></mquer:defendant></sub:mcolDefenceDetail>",
                result.replaceAll ("\\s+", ""));
        // CHECKSTYLE:ON
    }

    /**
     * Test the method to get target app response xml for individual request that has been rejected.
     * 
     * @throws Exception if there is any IO problems
     */
    @Test
    public void testParseIndividualResponseRejected () throws Exception
    {
        genericXmlParser.setEnclosingTag ("targetAppDetail");
        final Map<String, String> replacementNamespaces = new HashMap<String, String> ();
        replacementNamespaces.put ("http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/IndvResponseSchema",
                "http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema");

        genericXmlParser.setReplacementNamespaces (replacementNamespaces);

        // Load xml into SdtContext as if the inbound interceptor had run.
        final String rawXml = Utilities.getRawXml ("src/unit-test/resources/", "testIndividualResponseRejected.xml");

        SdtContext.getContext ().setRawInXml (rawXml);

        // Now call the parser to add the xml fragments into the payload of the individual reauests.
        final String result = this.genericXmlParser.parse ();

        // CHECKSTYLE:OFF
        Assert.assertEquals ("Failed to find expected response", "", result);
        // CHECKSTYLE:ON
    }

    /**
     * Test the method to get target app response xml for individual request.
     * 
     * @throws Exception if there is any IO problems
     */
    @Test
    public void testParseIndividualResponseSuccess () throws Exception
    {
        genericXmlParser.setEnclosingTag ("targetAppDetail");
        final Map<String, String> replacementNamespaces = new HashMap<String, String> ();
        replacementNamespaces.put ("http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/IndvResponseSchema",
                "http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema");

        genericXmlParser.setReplacementNamespaces (replacementNamespaces);

        // Load xml into SdtContext as if the inbound interceptor had run.
        final String rawXml = Utilities.getRawXml ("src/unit-test/resources/", "testIndividualResponse.xml");

        SdtContext.getContext ().setRawInXml (rawXml);

        // Now call the parser to add the xml fragments into the payload of the individual reauests.
        final String result = this.genericXmlParser.parse ();

        // CHECKSTYLE:OFF
        Assert.assertEquals (
                "Failed to find expected response",
                "<ind:mcolResponseDetailxmlns:ind=\"http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema\"xmlns:mresp=\"http://ws.sdt.moj.gov.uk/2013/mcol/ResponseDetailSchema\"><mresp:claimNumber>21346546</mresp:claimNumber><mresp:issueDate>2001-01-01</mresp:issueDate><mresp:serviceDate>2001-01-01</mresp:serviceDate><mresp:warrantNumber>12345678</mresp:warrantNumber><mresp:enforcingCourtCode>123</mresp:enforcingCourtCode><mresp:enforcingCourtName>CourtCode</mresp:enforcingCourtName><mresp:fee>0</mresp:fee><mresp:judgmentWarrantStatus>tns:additionalStatus</mresp:judgmentWarrantStatus></ind:mcolResponseDetail>",
                result.replaceAll ("\\s+", ""));
        // CHECKSTYLE:ON
    }

    /**
     * Test the method to extract target app response xml for individual request when target app detail is not present.
     * 
     * @throws Exception if there is any IO problems
     */
    @Test
    public void testParseIndividualResponseEmptyDetailSuccess () throws Exception
    {
        genericXmlParser.setEnclosingTag ("targetAppDetail");
        final Map<String, String> replacementNamespaces = new HashMap<String, String> ();
        replacementNamespaces.put ("http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/IndvResponseSchema",
                "http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema");

        genericXmlParser.setReplacementNamespaces (replacementNamespaces);

        // Load xml into SdtContext as if the inbound interceptor had run.
        final String rawXml = Utilities.getRawXml ("src/unit-test/resources/", "testIndividualResponseEmptyDetail.xml");

        SdtContext.getContext ().setRawInXml (rawXml);

        // Now call the parser to add the xml fragments into the payload of the individual reauests.
        final String result = this.genericXmlParser.parse ();

        // CHECKSTYLE:OFF
        Assert.assertEquals ("Failed to find expected response", "", result);
        // CHECKSTYLE:ON
    }

}
