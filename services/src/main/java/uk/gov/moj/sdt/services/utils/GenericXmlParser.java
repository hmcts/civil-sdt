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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.parsing.XmlNamespaceUtils;

/**
 * This class reads xml and parses it to extract target application specific xml.
 * 
 * @author d276205
 * 
 */
public class GenericXmlParser
{
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (GenericXmlParser.class);

    /**
     * Mapping of namespaces to be replaced.
     */
    private Map<String, String> replacementNamespaces = new HashMap<String, String> ();

    /**
     * Name of enclosing element.
     */
    private String enclosingTag;

    /**
     * Parses raw xml to extract target application specific fragment. The fragment is decorated with applicable
     * namespace details.
     * 
     * @return target app specific fragment
     */
    public String parse2 ()
    {

        String xmlResult = "";

        String rawXml = SdtContext.getContext ().getRawInXml ();

        // Remove linefeeds as they stop the regular expression working.
        rawXml = rawXml.replace ('\n', ' ');
        rawXml = rawXml.replace ('\r', ' ');

        // Retrieve all namespaces
        final Map<String, String> allNamespaces =
                XmlNamespaceUtils.extractAllNamespaces (rawXml, replacementNamespaces);

        // Build search pattern for extraction.
        final Pattern pattern =
                Pattern.compile ("<[\\w]+:" + getEnclosingTag () + "(.*?)>(.*?)</[\\w]+:" + getEnclosingTag () + ">");
        final Matcher matcher = pattern.matcher (rawXml);

        if (matcher.find ())
        {
            LOGGER.debug ("Found matching group[" + matcher.group () + "]");

            // Capture raw xml for element
            xmlResult = matcher.group (2).trim ();

            LOGGER.debug ("result XML[" + xmlResult + "]");

            // Find namespaces applicable for fragment
            final Map<String, String> matchingNamespaces =
                    XmlNamespaceUtils.findMatchingNamespaces (xmlResult, allNamespaces);

            // Embed namespaces within fragment
            xmlResult = XmlNamespaceUtils.addNamespaces (xmlResult, matchingNamespaces);

            LOGGER.debug ("result XML with namespaces[" + xmlResult + "]");
        }

        return xmlResult;

    }

    /**
     * Parses raw xml to extract target application specific fragment. The fragment is decorated with applicable
     * namespace details.
     * 
     * @return target app specific fragment.
     */
    public String parse ()
    {

        String xmlResult = "";

        String rawXml = SdtContext.getContext ().getRawInXml ();

        // Remove linefeeds as they stop the regular expression working.
        rawXml = rawXml.replace ('\n', ' ');
        rawXml = rawXml.replace ('\r', ' ');

        // Retrieve all namespaces
        final Map<String, String> allNamespaces =
                XmlNamespaceUtils.extractAllNamespaces (rawXml, replacementNamespaces);

        // Build search pattern for extraction.
        final Pattern pattern =
                Pattern.compile ("<[\\w]+:" + getEnclosingTag () + "(.*?)>(.*?)</[\\w]+:" + getEnclosingTag () + ">");
        final Matcher matcher = pattern.matcher (rawXml);

        if (matcher.find ())
        {
            LOGGER.debug ("Found matching group[" + matcher.group () + "]");

            // Capture raw xml for element
            xmlResult = matcher.group (2).trim ();

            LOGGER.debug ("Result XML[" + xmlResult + "]");

            // Find namespaces applicable for fragment
            final Map<String, String> matchingNamespaces =
                    XmlNamespaceUtils.findMatchingNamespaces (xmlResult, allNamespaces);

            xmlResult = addNamespaces (xmlResult, matchingNamespaces);

            LOGGER.debug ("result XML with namespaces[" + xmlResult + "]");
        }

        return xmlResult;

    }

    /**
     * Prepare xml fragment with given namespaces embedded. When given xml fragment contains multiple occurrences of
     * target application specific tag then namespace(s) are embedded in each occurrence.
     * 
     * @param xmlFragment fragment of xml that doesn't have namespace information.
     * @param matchingNamespaces namespaces that need to be added.
     * @return xml fragment with namespaces added.
     */
    private String addNamespaces (final String xmlFragment, final Map<String, String> matchingNamespaces)
    {
        // Find the target application specific tag which will occur one or more times.
        Pattern pattern = Pattern.compile ("<([\\w]+:[\\w-]+)>");
        Matcher matcher = pattern.matcher (xmlFragment);
        String iteratingTag = null;

        if (matcher.find ())
        {
            LOGGER.debug ("Found matching group[" + matcher.group () + "]");

            // Capture raw xml for element
            iteratingTag = matcher.group (1).trim ();
        }

        if (iteratingTag == null)
        {
            throw new IllegalStateException ("Target specific tag not found in xml fragment[" + xmlFragment + "]");
        }

        // Build search pattern for extracting content for repeating tag.
        pattern = Pattern.compile ("<" + iteratingTag + ">.*?</" + iteratingTag + ">");
        matcher = pattern.matcher (xmlFragment);

        final StringBuilder result = new StringBuilder ();

        while (matcher.find ())
        {
            LOGGER.debug ("Found matching group[" + matcher.group () + "]");
            final String xmlResult = XmlNamespaceUtils.addNamespaces (matcher.group (), matchingNamespaces);
            LOGGER.debug ("result XML[" + xmlResult + "]");
            result.append (xmlResult);
        }

        return result.toString ();
    }

    /**
     * Setter for enclosingTag.
     * 
     * @param enclosingTag enclosing tag.
     */
    public void setEnclosingTag (final String enclosingTag)
    {
        this.enclosingTag = enclosingTag;
    }

    /**
     * Getter for enclosingTag.
     * 
     * @return enclosing tag
     */
    public String getEnclosingTag ()
    {
        return enclosingTag;
    }

    /**
     * Setter for replacementNamespaces property.<BR>
     * In the map, key contains source namespace to be replaced and value contains target namespace.
     * 
     * @param replacementNamespaces namespaces to be replaced.
     */
    public void setReplacementNamespaces (final Map<String, String> replacementNamespaces)
    {
        this.replacementNamespaces = replacementNamespaces;
    }
}
