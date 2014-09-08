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

package uk.gov.moj.sdt.utils.parser;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import uk.gov.moj.sdt.utils.SdtUnitTestBase;
import uk.gov.moj.sdt.utils.parsing.XmlNamespaceUtils;

/**
 * A base test class that compares XML file output.
 * 
 * @author Robin Compston
 */
public class XmlNamespaceUtilsTest extends SdtUnitTestBase
{
    /**
     * Constructs a new {@link XmlNamespaceUtilsTest}.
     * 
     * @param testName name of this test.
     */
    public XmlNamespaceUtilsTest (final String testName)
    {
        super (testName);
    }

    /**
     * Test the extraction of single namespace values.
     */
    @Test
    public void testSingleNamespace ()
    {
        // Define text raw xml.
        final String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +

                "<beans xmlns=\"http://www.springframework.org/schema/beans\""
                        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                        + "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans"
                        + "       http://www.springframework.org/schema/beans/spring-beans-3.1.xsd"
                        + "       http://www.springframework.org/schema/aop"
                        + "       http://www.springframework.org/schema/aop/spring-aop-2.5.xsd\">" +

                        "   <!-- Note all ids should be based on fully qualified names (interfaces where"
                        + "       this is not ambiguous) and all classes should have an interface. -->" +

                        "   <bean id=\"uk.gov.moj.sdt.utils.mbeans.api.ISdtMetricsMBean\" "
                        + "class=\"uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean\" />" +

                        "</beans>";

        final Map<String, String> map = XmlNamespaceUtils.extractAllNamespaces (xml, null);
        Assert.assertEquals ("Missing namespace", "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
                map.get ("xsi"));
        Assert.assertEquals ("Incorrect number of namespaces", map.size (), 1);
    }

    /**
     * Test the extraction of multiple namespace values.
     */
    @Test
    public void testMultipleNamespace ()
    {
        // Define text raw xml.
        final String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +

                "<beans xmlns=\"http://www.springframework.org/schema/beans\""
                        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                        + "xmlns:aop=\"http://www.springframework.org/schema/aop\""
                        + "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans"
                        + "       http://www.springframework.org/schema/beans/spring-beans-3.1.xsd"
                        + "       http://www.springframework.org/schema/aop"
                        + "       http://www.springframework.org/schema/aop/spring-aop-2.5.xsd\">" +

                        "   <!-- Note all ids should be based on fully qualified names (interfaces where"
                        + "       this is not ambiguous) and all classes should have an interface. -->" +

                        "   <bean id=\"uk.gov.moj.sdt.utils.mbeans.api.ISdtMetricsMBean\" "
                        + "class=\"uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean\" />" +

                        "</beans>";

        final Map<String, String> map = XmlNamespaceUtils.extractAllNamespaces (xml, null);
        Assert.assertEquals ("Missing namespace", "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
                map.get ("xsi"));
        Assert.assertEquals ("Missing namespace", "xmlns:aop=\"http://www.springframework.org/schema/aop\"",
                map.get ("aop"));
        Assert.assertEquals ("Incorrect number of namespaces", map.size (), 2);
    }

    /**
     * Test the extraction of multiple namespace values alongwith replacement of one namespace.
     */
    @Test
    public void testMultipleNamespaceWithReplacement ()
    {
        // Define text raw xml.
        final String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +

                "<beans xmlns=\"http://www.springframework.org/schema/beans\""
                        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                        + "xmlns:aop=\"http://www.springframework.org/schema/aop\""
                        + "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans"
                        + "       http://www.springframework.org/schema/beans/spring-beans-3.1.xsd"
                        + "       http://www.springframework.org/schema/aop"
                        + "       http://www.springframework.org/schema/aop/spring-aop-2.5.xsd\">" +

                        "   <!-- Note all ids should be based on fully qualified names (interfaces where"
                        + "       this is not ambiguous) and all classes should have an interface. -->" +

                        "   <bean id=\"uk.gov.moj.sdt.utils.mbeans.api.ISdtMetricsMBean\" "
                        + "class=\"uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean\" />" +

                        "</beans>";

        final Map<String, String> replacements = new HashMap<String, String> ();
        replacements.put ("http://www.w3.org/2001/XMLSchema-instance", "http://replaced/value");

        final Map<String, String> map = XmlNamespaceUtils.extractAllNamespaces (xml, replacements);
        Assert.assertEquals ("Missing namespace", "xmlns:xsi=\"http://replaced/value\"", map.get ("xsi"));
        Assert.assertEquals ("Missing namespace", "xmlns:aop=\"http://www.springframework.org/schema/aop\"",
                map.get ("aop"));
        Assert.assertEquals ("Incorrect number of namespaces", map.size (), 2);
    }

    /**
     * Test the application of a single namespace to xml fragment.
     */
    @Test
    public void testMatchingSingleNamespace ()
    {
        // Define text raw xml.
        final String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +

                "<beans xmlns=\"http://www.springframework.org/schema/beans\""
                        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                        + "xmlns:aop=\"http://www.springframework.org/schema/aop\""
                        + "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans"
                        + "       http://www.springframework.org/schema/beans/spring-beans-3.1.xsd"
                        + "       http://www.springframework.org/schema/aop"
                        + "       http://www.springframework.org/schema/aop/spring-aop-2.5.xsd\">" +

                        "   <!-- Note all ids should be based on fully qualified names (interfaces where"
                        + "       this is not ambiguous) and all classes should have an interface. -->" +

                        "   <bean id=\"uk.gov.moj.sdt.utils.mbeans.api.ISdtMetricsMBean\" "
                        + "class=\"uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean\" />" +

                        "   <xsi:some-tag some-attribute=\"some value\">"
                        + "       <xsi:some-other-tag some-attribute=\"some value\"/>"
                        + "       <xsi:some-other-tag some-attribute=\"some value\">" + "       </xsi:some-other-tag>"
                        + "   </xsi:some-tag>" + "</beans>";

        final Map<String, String> allNamespaces = XmlNamespaceUtils.extractAllNamespaces (xml, null);

        final Map<String, String> map =
                XmlNamespaceUtils.findMatchingNamespaces ("   <xsi:some-tag some-attribute=\"some value\">"
                        + "       <xsi:some-other-tag some-attribute=\"some value\"/>"
                        + "       <xsi:some-other-tag some-attribute=\"some value\">" + "       </xsi:some-other-tag>"
                        + "   </xsi:some-tag>", allNamespaces);

        Assert.assertEquals ("Missing namespace", "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
                map.get ("xsi"));
        Assert.assertEquals ("Incorrect number of namespaces matching fragment", map.size (), 1);
    }

    /**
     * Test the application of multiple namespaces to xml fragment.
     */
    @Test
    public void testMatchingMultipleNamespace ()
    {
        // Define text raw xml.
        final String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +

                "<beans xmlns=\"http://www.springframework.org/schema/beans\""
                        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                        + "xmlns:aop=\"http://www.springframework.org/schema/aop\""
                        + "xmlns:abc=\"http://www.springframework.org/schema/abc\""
                        + "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans"
                        + "       http://www.springframework.org/schema/beans/spring-beans-3.1.xsd"
                        + "       http://www.springframework.org/schema/aop"
                        + "       http://www.springframework.org/schema/aop/spring-aop-2.5.xsd\">"
                        + "       http://www.springframework.org/schema/abc"
                        + "       http://www.springframework.org/schema/abc/spring-abc-2.5.xsd\">"

                        + "   <!-- Note all ids should be based on fully qualified names (interfaces where"
                        + "       this is not ambiguous) and all classes should have an interface. -->" +

                        "   <bean id=\"uk.gov.moj.sdt.utils.mbeans.api.ISdtMetricsMBean\" "
                        + "class=\"uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean\" />" +

                        "   <xsi:some-tag some-attribute=\"some value\">"
                        + "       <aop:some-other-tag some-attribute=\"some value\"/>"
                        + "       <aop:some-other-tag some-attribute=\"some value\">" + "       </aop:some-other-tag>"
                        + "   </xsi:some-tag>" + "</beans>";

        final Map<String, String> allNamespaces = XmlNamespaceUtils.extractAllNamespaces (xml, null);

        final Map<String, String> map =
                XmlNamespaceUtils.findMatchingNamespaces ("   <xsi:some-tag some-attribute=\"some value\">"
                        + "       <aop:some-other-tag some-attribute=\"some value\"/>"
                        + "       <aop:some-other-tag some-attribute=\"some value\">" + "       </aop:some-other-tag>"
                        + "   </xsi:some-tag>", allNamespaces);

        Assert.assertEquals ("Missing fragment namespace", "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
                map.get ("xsi"));
        Assert.assertEquals ("Missing fragment namespace", "xmlns:aop=\"http://www.springframework.org/schema/aop\"",
                map.get ("aop"));
        Assert.assertEquals ("Incorrect number of namespaces matching fragment", map.size (), 2);
    }

    /**
     * Test the reporting of a missing namespace.
     */
    @Test
    public void testMissingNamespace ()
    {
        // Define text raw xml.
        final String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +

                "<beans xmlns=\"http://www.springframework.org/schema/beans\""
                        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                        + "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans"
                        + "       http://www.springframework.org/schema/beans/spring-beans-3.1.xsd"
                        + "       http://www.springframework.org/schema/aop"
                        + "       http://www.springframework.org/schema/aop/spring-aop-2.5.xsd\">" +

                        "   <!-- Note all ids should be based on fully qualified names (interfaces where"
                        + "       this is not ambiguous) and all classes should have an interface. -->" +

                        "   <bean id=\"uk.gov.moj.sdt.utils.mbeans.api.ISdtMetricsMBean\" "
                        + "class=\"uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean\" />" +

                        "   <xsi:some-tag some-attribute=\"some value\">"
                        + "       <aop:some-other-tag some-attribute=\"some value\"/>"
                        + "       <aop:some-other-tag some-attribute=\"some value\">" + "       </aop:some-other-tag>"
                        + "   </xsi:some-tag>" + "</beans>";

        final Map<String, String> allNamespaces = XmlNamespaceUtils.extractAllNamespaces (xml, null);
        try
        {
            @SuppressWarnings ("unused") final Map<String, String> map =
                    XmlNamespaceUtils.findMatchingNamespaces ("   <xsi:some-tag some-attribute=\"some value\">"
                            + "       <aop:some-other-tag some-attribute=\"some value\"/>"
                            + "       <aop:some-other-tag some-attribute=\"some value\">"
                            + "       </aop:some-other-tag>" + "   </xsi:some-tag>", allNamespaces);

            Assert.fail ("Failed to throw expected RuntimeException due to missing tag namespace.");
        }
        catch (final RuntimeException e)
        {
            Assert.assertEquals ("Unrecognised exception message:",
                    e.getMessage (),
                    // CHECKSTYLE:OFF
                    "Namespace [aop] missing from incoming raw xml[   <xsi:some-tag some-attribute=\"some value\">       <aop:some-other-tag some-attribute=\"some value\"/>       <aop:some-other-tag some-attribute=\"some value\">       </aop:some-other-tag>   </xsi:some-tag>]");
            // CHECKSTYLE:ON
        }
    }

    /**
     * Test the combining of individual namespaces.
     */
    @Test
    public void testCombinedNamespace ()
    {
        final Map<String, String> map1 = new HashMap<String, String> ();
        final Map<String, String> map2 = new HashMap<String, String> ();

        // Setup individual maps.
        map1.put ("xsi", "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        map2.put ("xsi", "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        map2.put ("aop", "xmlns:aop=\"http://www.springframework.org/schema/aop\"");

        Map<String, String> combinedMap = new HashMap<String, String> ();
        // Combine together the two maps.
        combinedMap = XmlNamespaceUtils.combineNamespaces (map1, combinedMap);
        combinedMap = XmlNamespaceUtils.combineNamespaces (map2, combinedMap);

        Assert.assertEquals ("Missing combined namespace", "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
                combinedMap.get ("xsi"));
        Assert.assertEquals ("Missing combined namespace", "xmlns:aop=\"http://www.springframework.org/schema/aop\"",
                combinedMap.get ("aop"));
        Assert.assertEquals ("Incorrect number of namespaces matching fragment", combinedMap.size (), 2);
    }

    /**
     * Test addition of namespace to xml.
     */
    @Test
    public void testAddNamespaces ()
    {

        // Define text raw xml.
        final String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +

                "<beans xmlns=\"http://www.springframework.org/schema/beans\""
                        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                        + "xmlns:aop=\"http://www.springframework.org/schema/aop\""
                        + "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans"
                        + "       http://www.springframework.org/schema/beans/spring-beans-3.1.xsd"
                        + "       http://www.springframework.org/schema/aop"
                        + "       http://www.springframework.org/schema/aop/spring-aop-2.5.xsd\">" +

                        "   <!-- Note all ids should be based on fully qualified names (interfaces where"
                        + "       this is not ambiguous) and all classes should have an interface. -->" +

                        "   <bean id=\"uk.gov.moj.sdt.utils.mbeans.api.ISdtMetricsMBean\" "
                        + "class=\"uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean\" />" +

                        "   <xsi:some-tag some-attribute=\"some value\">"
                        + "       <aop:some-other-tag some-attribute=\"some value\"/>"
                        + "       <aop:some-other-tag some-attribute=\"some value\">" + "       </aop:some-other-tag>"
                        + "   </xsi:some-tag>" + "</beans>";

        final Map<String, String> allNamespaces = XmlNamespaceUtils.extractAllNamespaces (xml, null);

        String xmlFragment =
                "   <!--Comment--><xsi:some-tag some-attribute=\"some value\">"
                        + "       <aop:some-other-tag some-attribute=\"some value\"/>"
                        + "       <aop:some-other-tag some-attribute=\"some value\">" + "       </aop:some-other-tag>"
                        + "   </xsi:some-tag>";

        final Map<String, String> matched = XmlNamespaceUtils.findMatchingNamespaces (xmlFragment, allNamespaces);

        // Remove namespaces to make test realistic as this is what production code does.
        xmlFragment = XmlNamespaceUtils.removeEmbeddedNamespaces (xmlFragment);

        final String result = XmlNamespaceUtils.addNamespaces (xmlFragment, matched);

        // CHECKSTYLE:OFF
        final String expected =
                "   <!--Comment--><xsi:some-tag xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" some-attribute=\"some value\">"
                        + "       <aop:some-other-tag xmlns:aop=\"http://www.springframework.org/schema/aop\" some-attribute=\"some value\"/>"
                        + "       <aop:some-other-tag xmlns:aop=\"http://www.springframework.org/schema/aop\" some-attribute=\"some value\">"
                        + "       </aop:some-other-tag>" + "   </xsi:some-tag>";

        Assert.assertEquals ("Generated xml fragment is incorrect", expected, result);

        // CHECKSTYLE:ON

    }

    /**
     * Test addition of namespace to xml which already has namespaces present. These must be removed first.
     */
    @Test
    public void testAddNamespacesAlreadyPresent ()
    {

        // Define text raw xml.
        final String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +

                "<beans xmlns=\"http://www.springframework.org/schema/beans\""
                        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                        + "xmlns:aop=\"http://www.springframework.org/schema/aop\""
                        + "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans"
                        + "       http://www.springframework.org/schema/beans/spring-beans-3.1.xsd"
                        + "       http://www.springframework.org/schema/aop"
                        + "       http://www.springframework.org/schema/aop/spring-aop-2.5.xsd\">" +

                        "   <!-- Note all ids should be based on fully qualified names (interfaces where"
                        + "       this is not ambiguous) and all classes should have an interface. -->" +

                        "   <bean id=\"uk.gov.moj.sdt.utils.mbeans.api.ISdtMetricsMBean\" "
                        + "class=\"uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean\" />" +

                        "   <xsi:some-tag some-attribute=\"some value\">"
                        + "       <aop:some-other-tag some-attribute=\"some value\"/>"
                        + "       <aop:some-other-tag some-attribute=\"some value\">" + "       </aop:some-other-tag>"
                        + "   </xsi:some-tag>" + "</beans>";

        final Map<String, String> allNamespaces = XmlNamespaceUtils.extractAllNamespaces (xml, null);

        String xmlFragment =
                "   <!--Comment--><xsi:some-tag   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  "
                        + "xmlns:aop=\"http://www.springframework.org/schema/aop\" some-attribute=\"some value\">"
                        + "       <aop:some-other-tag some-attribute=\"some value\"/>"
                        + "       <aop:some-other-tag some-attribute=\"some value\">" + "       </aop:some-other-tag>"
                        + "   </xsi:some-tag>";

        final Map<String, String> matched = XmlNamespaceUtils.findMatchingNamespaces (xmlFragment, allNamespaces);

        // Remove namespaces to make test realistic as this is what production code does.
        xmlFragment = XmlNamespaceUtils.removeEmbeddedNamespaces (xmlFragment);

        final String result = XmlNamespaceUtils.addNamespaces (xmlFragment, matched);

        // CHECKSTYLE:OFF
        final String expected =
                "   <!--Comment--><xsi:some-tag xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" some-attribute=\"some value\">"
                        + "       <aop:some-other-tag xmlns:aop=\"http://www.springframework.org/schema/aop\" some-attribute=\"some value\"/>"
                        + "       <aop:some-other-tag xmlns:aop=\"http://www.springframework.org/schema/aop\" some-attribute=\"some value\">"
                        + "       </aop:some-other-tag>" + "   </xsi:some-tag>";

        Assert.assertEquals ("Generated xml fragment is incorrect", expected, result);

        // CHECKSTYLE:ON

    }

    /**
     * Test addition of namespace to xml where namespaces already present on request (which CLX puts there) and with no
     * attributes on request.
     */
    @Test
    public void testAddNamespacesAlreadyPresentNoAttributes ()
    {
        // Define text raw xml.
        final String xml =
                "<bul:bulkRequest xmlns:bul=\"http://ws.sdt.moj.gov.uk/2013/sdt/BulkRequestSchema\""
                        + "    xmlns:cla=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimSchema\" "
                        + "    xmlns:bas=\"http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema\""
                        + "    xmlns:jud=\"http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema\""
                        + "    xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\""
                        + "    xmlns:war=\"http://ws.sdt.moj.gov.uk/2013/mcol/WarrantSchema\">" + "    <bul:header>"
                        + "        <bul:sdtCustomerId>12345678</bul:sdtCustomerId>"
                        + "        <bul:targetApplicationId>mcol</bul:targetApplicationId>"
                        + "        <bul:requestCount>1</bul:requestCount>"
                        + "        <bul:customerReference>1</bul:customerReference>" + "    </bul:header>"
                        + "    <bul:requests>" + "        <bul:request requestType=\"mcolClaimStatusUpdate\""
                        + "            requestId=\"1-1\">" + "            <bul:mcolClaimStatusUpdate>"
                        + "                <cla1:claimNumber>claim123</cla1:claimNumber>"
                        + "                <cla1:defendantId>1</cla1:defendantId>"
                        + "                <cla1:notificationType>MP</cla1:notificationType>"
                        + "                <cla1:paidInFullDate>2012-01-01</cla1:paidInFullDate>"
                        + "            </bul:mcolClaimStatusUpdate>" + "        </bul:request>" + "    </bul:requests>"
                        + "</bul:bulkRequest>";

        final Map<String, String> allNamespaces = XmlNamespaceUtils.extractAllNamespaces (xml, null);

        String xmlFragment =
                "       <bul:mcolClaimStatusUpdate > " + "  <cla1:claimNumber>claim123</cla1:claimNumber>"
                        + "<cla1:defendantId>1</cla1:defendantId>"
                        + "<cla1:notificationType>MP</cla1:notificationType>"
                        + "<cla1:paidInFullDate>2012-01-01</cla1:paidInFullDate>" + "</bul:mcolClaimStatusUpdate>";

        final Map<String, String> matched = XmlNamespaceUtils.findMatchingNamespaces (xmlFragment, allNamespaces);

        // Remove namespaces to make test realistic as this is what production code does.
        xmlFragment = XmlNamespaceUtils.removeEmbeddedNamespaces (xmlFragment);

        final String result = XmlNamespaceUtils.addNamespaces (xmlFragment, matched);

        final String expected =
                "       <bul:mcolClaimStatusUpdate "
                        + "xmlns:bul=\"http://ws.sdt.moj.gov.uk/2013/sdt/BulkRequestSchema\" >"
                        + "   <cla1:claimNumber xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/"
                        + "ClaimStatusUpdateSchema\"" + ">claim123</cla1:claimNumber>"
                        + "<cla1:defendantId xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\""
                        + ">1</cla1:defendantId>"
                        + "<cla1:notificationType xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/"
                        + "ClaimStatusUpdateSchema\">MP</cla1:notificationType>"
                        + "<cla1:paidInFullDate xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/"
                        + "ClaimStatusUpdateSchema\">2012-01-01</cla1:paidInFullDate>" + "</bul:mcolClaimStatusUpdate>";

        Assert.assertEquals ("Generated xml fragment is incorrect", expected, result);
    }

    /**
     * Test addition of namespace to xml where namespaces already present on request (which CLX puts there) and with
     * attributes on request.
     */
    @Test
    public void testAddNamespacesAlreadyPresentAndAttributes ()
    {
        // Define text raw xml.
        final String xml =
                "<bul:bulkRequest xmlns:bul=\"http://ws.sdt.moj.gov.uk/2013/sdt/BulkRequestSchema\""
                        + "    xmlns:cla=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimSchema\" "
                        + "    xmlns:bas=\"http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema\""
                        + "    xmlns:jud=\"http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema\""
                        + "    xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\""
                        + "    xmlns:war=\"http://ws.sdt.moj.gov.uk/2013/mcol/WarrantSchema\">" + "    <bul:header>"
                        + "        <bul:sdtCustomerId>12345678</bul:sdtCustomerId>"
                        + "        <bul:targetApplicationId>mcol</bul:targetApplicationId>"
                        + "        <bul:requestCount>1</bul:requestCount>"
                        + "        <bul:customerReference>1</bul:customerReference>" + "    </bul:header>"
                        + "    <bul:requests>" + "        <bul:request requestType=\"mcolClaimStatusUpdate\""
                        + "            requestId=\"1-1\">" + "            <bul:mcolClaimStatusUpdate>"
                        + "                <cla1:claimNumber>claim123</cla1:claimNumber>"
                        + "                <cla1:defendantId>1</cla1:defendantId>"
                        + "                <cla1:notificationType>MP</cla1:notificationType>"
                        + "                <cla1:paidInFullDate>2012-01-01</cla1:paidInFullDate>"
                        + "            </bul:mcolClaimStatusUpdate>" + "        </bul:request>" + "    </bul:requests>"
                        + "</bul:bulkRequest>";

        final Map<String, String> allNamespaces = XmlNamespaceUtils.extractAllNamespaces (xml, null);

        String xmlFragment =
                "       <bul:mcolClaimStatusUpdate "
                        + "xmlns:bul=\"http://ws.sdt.moj.gov.uk/2013/sdt/BulkRequestSchema\"  "
                        + "xmlns:war=\"http://ws.sdt.moj.gov.uk/2013/mcol/WarrantSchema\" somAttribute=\"abcd\">"
                        + "  <cla1:claimNumber>claim123</cla1:claimNumber>" + "<cla1:defendantId>1</cla1:defendantId>"
                        + "<cla1:notificationType>MP</cla1:notificationType>"
                        + "<cla1:paidInFullDate>2012-01-01</cla1:paidInFullDate>" + "</bul:mcolClaimStatusUpdate>";

        final Map<String, String> matched = XmlNamespaceUtils.findMatchingNamespaces (xmlFragment, allNamespaces);

        // Remove namespaces to make test realistic as this is what production code does.
        xmlFragment = XmlNamespaceUtils.removeEmbeddedNamespaces (xmlFragment);

        final String result = XmlNamespaceUtils.addNamespaces (xmlFragment, matched);

        final String expected =
                "       <bul:mcolClaimStatusUpdate "
                        + "xmlns:bul=\"http://ws.sdt.moj.gov.uk/2013/sdt/BulkRequestSchema\" "
                        + "somAttribute=\"abcd\">"
                        + "  <cla1:claimNumber xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/"
                        + "ClaimStatusUpdateSchema\">claim123</cla1:claimNumber>"
                        + "<cla1:defendantId xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/"
                        + "ClaimStatusUpdateSchema\">1</cla1:defendantId><cla1:notificationType "
                        + "xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">"
                        + "MP</cla1:notificationType><cla1:paidInFullDate "
                        + "xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">"
                        + "2012-01-01</cla1:paidInFullDate></bul:mcolClaimStatusUpdate>";

        Assert.assertEquals ("Generated xml fragment is incorrect", expected, result);
    }

    /**
     * Test addition of namespace to xml where namespaces already present on request (which CLX puts there) and with
     * attributes on request and an awkward space.
     */
    @Test
    public void testAddNamespacesAlreadyPresentAndAttributesWithSpace ()
    {
        // Define text raw xml.
        final String xml =
                "<bul:bulkRequest xmlns:bul=\"http://ws.sdt.moj.gov.uk/2013/sdt/BulkRequestSchema\""
                        + "    xmlns:cla=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimSchema\" "
                        + "    xmlns:bas=\"http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema\""
                        + "    xmlns:jud=\"http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema\""
                        + "    xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\""
                        + "    xmlns:war=\"http://ws.sdt.moj.gov.uk/2013/mcol/WarrantSchema\">" + "    <bul:header>"
                        + "        <bul:sdtCustomerId>12345678</bul:sdtCustomerId>"
                        + "        <bul:targetApplicationId>mcol</bul:targetApplicationId>"
                        + "        <bul:requestCount>1</bul:requestCount>"
                        + "        <bul:customerReference>1</bul:customerReference>" + "    </bul:header>"
                        + "    <bul:requests>" + "        <bul:request requestType=\"mcolClaimStatusUpdate\""
                        + "            requestId=\"1-1\">" + "            <bul:mcolClaimStatusUpdate>"
                        + "                <cla1:claimNumber>claim123</cla1:claimNumber>"
                        + "                <cla1:defendantId>1</cla1:defendantId>"
                        + "                <cla1:notificationType>MP</cla1:notificationType>"
                        + "                <cla1:paidInFullDate>2012-01-01</cla1:paidInFullDate>"
                        + "            </bul:mcolClaimStatusUpdate>" + "        </bul:request>" + "    </bul:requests>"
                        + "</bul:bulkRequest>";

        final Map<String, String> allNamespaces = XmlNamespaceUtils.extractAllNamespaces (xml, null);

        String xmlFragment =
                "       <bul:mcolClaimStatusUpdate "
                        + "xmlns:bul=\"http://ws.sdt.moj.gov.uk/2013/sdt/BulkRequestSchema\"  "
                        + "xmlns:war=\"http://ws.sdt.moj.gov.uk/2013/mcol/WarrantSchema\" somAttribute=\"abcd\" >"
                        + "  <cla1:claimNumber>claim123</cla1:claimNumber>" + "<cla1:defendantId>1</cla1:defendantId>"
                        + "<cla1:notificationType>MP</cla1:notificationType>"
                        + "<cla1:paidInFullDate>2012-01-01</cla1:paidInFullDate>" + "</bul:mcolClaimStatusUpdate>";

        final Map<String, String> matched = XmlNamespaceUtils.findMatchingNamespaces (xmlFragment, allNamespaces);

        // Remove namespaces to make test realistic as this is what production code does.
        xmlFragment = XmlNamespaceUtils.removeEmbeddedNamespaces (xmlFragment);

        final String result = XmlNamespaceUtils.addNamespaces (xmlFragment, matched);

        // CHECKSTYLE:OFF
        final String expected =
                "       <bul:mcolClaimStatusUpdate xmlns:bul=\"http://ws.sdt.moj.gov.uk/2013/sdt/BulkRequestSchema\" somAttribute=\"abcd\" >  <cla1:claimNumber xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">claim123</cla1:claimNumber><cla1:defendantId xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">1</cla1:defendantId><cla1:notificationType xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">MP</cla1:notificationType><cla1:paidInFullDate xmlns:cla1=\"http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema\">2012-01-01</cla1:paidInFullDate></bul:mcolClaimStatusUpdate>";
        // CHECKSTYLE:ON

        Assert.assertEquals ("Generated xml fragment is incorrect", expected, result);
    }
}