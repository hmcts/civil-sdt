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
package uk.gov.moj.sdt.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;

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
    private static final Log LOGGER = LogFactory.getLog (IndividualRequestsXmlParserTest.class);

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
        final String rawXml = this.getRawXml ();

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
                "<bul:mcolClaimStatusUpdate><cla1:claimNumber>claim123</cla1:claimNumber><cla1:defendantId>1</cla1:defendantId><cla1:notificationType>MP</cla1:notificationType><cla1:paidInFullDate>2012-01-01</cla1:paidInFullDate></bul:mcolClaimStatusUpdate>",
                requests.get (0).getRequestPayload ().replaceAll ("\\s+", ""));
        Assert.assertEquals (
                "Failed to find correct payload for request 2",
                "<bul:mcolClaimStatusUpdate><cla1:claimNumber>claim124</cla1:claimNumber><cla1:defendantId>1</cla1:defendantId><cla1:notificationType>MP</cla1:notificationType><cla1:paidInFullDate>2012-02-01</cla1:paidInFullDate></bul:mcolClaimStatusUpdate>",
                requests.get (1).getRequestPayload ().replaceAll ("\\s+", ""));
        Assert.assertEquals (
                "Failed to find correct payload for request 3",
                "<bul:mcolClaim><cla1:claimNumber>claim125</cla1:claimNumber><cla1:defendantId>1</cla1:defendantId><cla1:notificationType>MP</cla1:notificationType><cla1:paidInFullDate>2012-03-01</cla1:paidInFullDate></bul:mcolClaim>",
                requests.get (2).getRequestPayload ().replaceAll ("\\s+", ""));
        // CHECKSTYLE:ON
    }

    /**
     * 
     * @return rax xml from a test file
     * @throws IOException during the read operations
     */
    private String getRawXml () throws IOException
    {
        // Read the test xml file.
        File myFile;
        String message = "";

        // XPathHandler xmlHandler = new XPathHandler ();

        myFile = new File (Utilities.checkFileExists ("src/unit-test/resources/", "testXMLValid.xml", false));

        message = FileUtils.readFileToString (myFile);

        // Remove linefeeds as they stop the regular expression working.
        message = message.replace ('\n', ' ');
        message = message.replace ('\r', ' ');

        return message;

    }

}
