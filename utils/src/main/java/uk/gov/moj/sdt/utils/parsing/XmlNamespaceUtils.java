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
package uk.gov.moj.sdt.utils.parsing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a utility class for dealing with namespaces for xml fragments.
 * 
 * @author D303894
 * 
 */
public final class XmlNamespaceUtils
{
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (XmlNamespaceUtils.class);

    /**
     * No-args constructor.
     */
    private XmlNamespaceUtils ()
    {

    }

    /**
     * Combine a number of namespace maps into a single namespace map without duplicates.
     * 
     * @param namespaceMap an individual namespace map which should be combined with any other individual namespace
     *            maps.
     * @param combinedNamespaces combined namespace map
     * @return map of individual namespace maps combined so far.
     */
    public static Map<String, String> combineNamespaces (final Map<String, String> namespaceMap,
                                                         final Map<String, String> combinedNamespaces)
    {
        final Set<String> keys = namespaceMap.keySet ();
        final Iterator<String> iter = keys.iterator ();
        while (iter.hasNext ())
        {
            final String key = iter.next ();

            // Check if we have already combined this one.
            if ( !combinedNamespaces.containsKey (key))
            {
                // Add to combined set of namespaces.
                combinedNamespaces.put (key, namespaceMap.get (key));
            }
        }

        return combinedNamespaces;
    }

    /**
     * Extract namespaces out of a piece of raw XML. Note that we find these both in the XML header, and as embedded
     * namespace definition attributes. Note that we deliberately do not find any embedded (attribute style) default
     * namespaces, since these do not have corresponding tags, and the map generated is specifically used to handle
     * tags with namespace prefixes.
     * 
     * @param rawXml raw xml.
     * @param replacementNamespaces map containing replacement namespaces.
     * @return map containing extracted namespaces
     */
    public static Map<String, String> extractAllNamespaces (final String rawXml,
                                                            final Map<String, String> replacementNamespaces)
    {
        final Map<String, String> namespaces = new HashMap<String, String> ();

        // Build a search pattern to find all namespaces.
        final Pattern pattern = Pattern.compile ("xmlns:([\\S&&[^>/]]*?)=\".*?\"");

        final Matcher matcher = pattern.matcher (rawXml);

        while (matcher.find ())
        {
            // Capture the raw XML associated with this request.
            final String namespaceKey = matcher.group (1);
            String namespaceValue = matcher.group (0);

            if (replacementNamespaces != null)
            {
                // Replace namespace value if mapping found in replacement namespaces map.
                final Set<String> keys = replacementNamespaces.keySet ();
                for (String key : keys)
                {
                    if (namespaceValue.contains (key))
                    {
                        namespaceValue = "xmlns:" + namespaceKey + "=\"" + replacementNamespaces.get (key) + "\"";
                    }
                }
            }

            if (LOGGER.isDebugEnabled ())
            {
                LOGGER.debug ("Extracting namespace with key [" + namespaceKey + "], value [" + namespaceValue + "]");
            }

            namespaces.put (namespaceKey, namespaceValue);
        }

        return namespaces;
    }

    /**
     * Find map of applicable namespaces for given xml fragment based on what namespaces have been used a tag prefixes.
     * Look at the entire xml.
     * 
     * @param xmlFragment fragment of xml to which namespaces should apply.
     * @param allNamespaces namespaces for entire xml
     * @return map of applicable namespaces.
     */
    public static Map<String, String> findMatchingNamespaces (final String xmlFragment,
                                                              final Map<String, String> allNamespaces)
    {
        // Clear out previous namespaces.
        final Map<String, String> matchingNamespaces = new HashMap<String, String> ();

        // Build a search pattern to find all start tag namespaces used in this xml fragment.
        final Pattern pattern = Pattern.compile ("<([\\S:&&[^>/]]+?):");

        final Matcher matcher = pattern.matcher (xmlFragment);

        while (matcher.find ())
        {
            // Capture the raw XML associated with this request.
            final String namespaceKey = matcher.group (1);

            // Form the replacement string from the matched groups and the extra XML.
            if (LOGGER.isDebugEnabled ())
            {
                LOGGER.debug ("Matching namespace for fragment [" + namespaceKey + "]");
            }

            if ( !matchingNamespaces.containsKey (namespaceKey))
            {
                // Make sure this namespace has been defined.
                if ( !allNamespaces.containsKey (namespaceKey))
                {
                    LOGGER.error ("Namespace[" + namespaceKey + "] missing from incoming raw xml[" + xmlFragment + "]");

                    throw new RuntimeException ("Namespace [" + namespaceKey + "] missing from incoming raw xml[" +
                            xmlFragment + "]");
                }

                // Copy namespace to the set of matching namespaces for this fragment.
                matchingNamespaces.put (namespaceKey, allNamespaces.get (namespaceKey));
            }
        }

        if (LOGGER.isDebugEnabled ())
        {
            LOGGER.debug ("Found matching namespaces for fragment [" + xmlFragment + "]:" + matchingNamespaces);
        }

        return matchingNamespaces;
    }

    /**
     * Adds namespaces to xml fragment. Assume that any embedded namespace definitions have been removed before this
     * method is called.
     * 
     * @param xmlFragment xml to be decorated.
     * @param matchingNamespaces namespaces to be added.
     * @return xml with namespace
     */
    public static String addNamespaces (final String xmlFragment, final Map<String, String> matchingNamespaces)
    {
        if (LOGGER.isDebugEnabled ())
        {
            LOGGER.debug ("Adding namespaces to fragment [" + xmlFragment + "]");
        }

        // Buffer for accumulating results.
        final StringBuilder result = new StringBuilder ();

        // Point up to which text has been copied so far.
        int copyPosition = 0;

        // Look for all tags with any namespace prefix (excluding any attributes).
        final Pattern pattern = Pattern.compile ("<([\\S&&[^>/]]+?):([\\w-]+)");
        final Matcher matcher = pattern.matcher (xmlFragment);
        while (matcher.find ())
        {
            if (LOGGER.isDebugEnabled ())
            {
                LOGGER.debug ("Found matching group[" + matcher.group () + "]");
            }

            // Find if there is any uncopied text before the start of the match.
            final int start = matcher.start ();
            if (start > copyPosition)
            {
                // Some uncopied text precedes the start of the match point - copy it to results.
                result.append (xmlFragment.substring (copyPosition, start));
            }

            // Copy the match.
            result.append (matcher.group (0));

            // Get prefix out of match (1st group).
            final String prefix = matcher.group (1);

            // Append the corresponding namespace.
            result.append (" " + matchingNamespaces.get (prefix));

            // Update position to take account of copy of matched substring.
            copyPosition = matcher.end ();
        }

        // Copy any remaining text not yet copied from original XML.
        if (copyPosition < xmlFragment.length ())
        {
            // Some uncopied text follows the end of the last match point - copy it to results.
            result.append (xmlFragment.substring (copyPosition));
        }

        if (LOGGER.isDebugEnabled ())
        {
            LOGGER.debug ("Enhanced fragment [" + result.toString () + "]");
        }

        return result.toString ();
    }

    /**
     * Removes all embedded namespaces so that they can be re-added. Unless they are removed, it is difficult to
     * distinguish them from other element attributes when adding embedded namespaces again (which is required before
     * sending to target application to keep XSD checking happy). Note: do not remove default namespaces (which have no
     * associated name) because there will be no corresponding prefix on the tag and these cannot be re-added as is
     * required.
     * 
     * @param xmlFragment xml to be cleaned.
     * @return xml without embedded namespaces.
     */
    public static String removeEmbeddedNamespaces (final String xmlFragment)
    {
        if (LOGGER.isDebugEnabled ())
        {
            LOGGER.debug ("Removing namespaces from fragment [" + xmlFragment + "]");
        }

        // Buffer for accumulating results.
        final StringBuilder result = new StringBuilder ();

        // Build pattern to find all embedded namespace attributes in XML fragment. Note; default namespaces have no
        // colon.
        final Pattern pattern = Pattern.compile ("xmlns:[\\S]+[\\s]*");
        final Matcher matcher = pattern.matcher (xmlFragment);

        // Position of XML copied into results so far.
        int copyPosition = 0;

        while (matcher.find ())
        {
            final int start = matcher.start ();
            if (start > copyPosition)
            {
                // Copy uncopied text preceding start of the match point to results.
                result.append (xmlFragment.substring (copyPosition, start));
            }

            if (LOGGER.isDebugEnabled ())
            {
                LOGGER.debug ("Found matching namespace[" + matcher.group () + "]");
            }

            // Update position to take account of copy of matched substring.
            copyPosition = matcher.end ();
        }

        // Copy any remaining text not yet copied from original XML.
        if (copyPosition < xmlFragment.length ())
        {
            // Some uncopied text follows the end of the last match point - copy it to results.
            result.append (xmlFragment.substring (copyPosition));
        }

        if (LOGGER.isDebugEnabled ())
        {
            LOGGER.debug ("Cleaned fragment [" + result.toString () + "]");
        }

        return result.toString ();
    }
}
