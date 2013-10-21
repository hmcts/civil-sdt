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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.utils.parsing.api.IXmlNamespaceUtils;

/**
 * This class reads a bulk submission xml and parses it into individual raw xml requests.
 * 
 * @author D303894
 * 
 */
public class XmlNamespaceUtils implements IXmlNamespaceUtils
{
    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (XmlNamespaceUtils.class);

    /**
     * Namespaces extracted out of a piece of raw xml.
     */
    private Map<String, String> namespaces;

    /**
     * Combined map of individual namespace maps.
     */
    private Map<String, String> combinedNamespaces = new HashMap<String, String> ();

    @Override
    public void extractNamespaces (final String rawXml)
    {
        LOGGER.debug ("Raw xml is " + rawXml);

        // Clear out previous namespaces.
        namespaces = new HashMap<String, String> ();

        // Build a search pattern to find all namespaces.
        final Pattern pattern = Pattern.compile ("xmlns:(.*?)=\".*?\"");

        final Matcher matcher = pattern.matcher (rawXml);

        while (matcher.find ())
        {
            // Capture the raw XML associated with this request.
            final String namespaceKey = matcher.group (1);
            final String namespaceValue = matcher.group (0);

            // Form the replacement string from the matched groups and the extra XML.
            LOGGER.debug ("Namespace key[" + namespaceKey + "], value[" + namespaceValue + "]");

            namespaces.put (namespaceKey, namespaceValue);
        }
    }

    @Override
    public Map<String, String> getNamespaces ()
    {
        return namespaces;
    }

    @Override
    public Map<String, String> findMatchingNamespaces (final String xmlFragment)
    {
        LOGGER.debug ("Raw xml fragment is " + xmlFragment);

        // Clear out previous namespaces.
        final Map<String, String> matchingNamespaces = new HashMap<String, String> ();

        // Build a search pattern to find all start tag namespaces used in this xml fragment.
        final Pattern pattern = Pattern.compile ("<([a-zA-Z0-9]+?):");

        final Matcher matcher = pattern.matcher (xmlFragment);

        while (matcher.find ())
        {
            // Capture the raw XML associated with this request.
            final String namespaceKey = matcher.group (1);

            // Form the replacement string from the matched groups and the extra XML.
            LOGGER.debug ("Matching namespace[" + namespaceKey + "]");

            if ( !matchingNamespaces.containsKey (namespaceKey))
            {
                // Make sure this namespace has been defined.
                if ( !namespaces.containsKey (namespaceKey))
                {
                    throw new RuntimeException ("Namespace [" + namespaceKey + "] missing from incoming raw xml[" +
                            xmlFragment + "]");
                }

                // Copy namespace to the set of matching namespaces for this fragment.
                matchingNamespaces.put (namespaceKey, namespaces.get (namespaceKey));
            }
        }

        return matchingNamespaces;
    }

    @Override
    public Map<String, String> combineNamespaces (final Map<String, String> namespaceMap)
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
}
