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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.parsing.XmlNamespaceUtils;

/**
 * This class reads a bulk submission xml and parses it into individual raw xml requests.
 * 
 * @author D303894
 * 
 */
public class IndividualRequestsXmlParser
{
    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (IndividualRequestsXmlParser.class);

    /**
     * Contains mapping of namespaces to be replaced.
     */
    private Map<String, String> replacementNamespaces = new HashMap<String, String> ();

    /**
     * Populate individual requests with the raw xml request.
     * 
     * @param individualRequests the individual requests to be populated with data from the raw XML.
     */
    public void populateRawRequest (final List<IIndividualRequest> individualRequests)
    {
        if (LOGGER.isDebugEnabled ())
        {
            LOGGER.debug ("Started parsing raw xml to extract payload for " + individualRequests.size () + " requests");
        }

        // Get iterator so we can traverse the list of requests and the payload (raw XML) to each one.
        final Iterator<IIndividualRequest> iter = individualRequests.iterator ();

        // Match it against the result of all previous match replacements.
        final String rawXml = SdtContext.getContext ().getRawInXml ();

        // Retrieve all namespaces
        final Map<String, String> allNamespaces =
                XmlNamespaceUtils.extractAllNamespaces (rawXml, replacementNamespaces);

        final Map<String, String> allRawIndividualRequests = new HashMap<String, String> ();

        // Build a search pattern with this request id. Allow for any order of requestId
        // attributes.
        final Pattern pattern =
                Pattern.compile ("<[\\w]+:request[ \\w\"=]*requestId=\"([\\w]+)\"[ \\w\"=]*>(.*?)</[\\w]+:request>");

        final Matcher matcher = pattern.matcher (rawXml);

        while (matcher.find ())
        {
            // Capture the request id associated with this request.
            final String requestId = matcher.group (1).trim ();

            // Capture the raw XML associated with this request.
            String individualRequestRawXml = matcher.group (2).trim ();

            if (LOGGER.isDebugEnabled ())
            {
                LOGGER.debug ("Found match: requestId [" + requestId + "], individualRequestRawXml [" +
                        individualRequestRawXml + "]");
            }

            // Find namespaces applicable for fragment
            final Map<String, String> matchingNamespaces =
                    XmlNamespaceUtils.findMatchingNamespaces (individualRequestRawXml, allNamespaces);

            // Embed namespaces within fragment
            individualRequestRawXml = XmlNamespaceUtils.addNamespaces (individualRequestRawXml, matchingNamespaces);

            if (LOGGER.isDebugEnabled ())
            {
                LOGGER.debug ("Raw XML enhanced with namespaces [" + individualRequestRawXml + "]");
            }

            allRawIndividualRequests.put (requestId, individualRequestRawXml);
        }

        while (iter.hasNext ())
        {
            // Get the next request.
            final IIndividualRequest individualRequest = iter.next ();

            if (individualRequest.getRequestStatus ().equals (
                    IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus ()))
            {
                // Do not get the payload for this one as it may have a duplicate customer reference and does not need
                // to be sent to the case management system.
                continue;
            }

            individualRequest.setRequestPayload (allRawIndividualRequests.get (individualRequest
                    .getCustomerRequestReference ()));
        }

        LOGGER.debug ("Finished parsing raw xml");
    }

    // /**
    // * Parse the given raw XML and extract the matching fragments returning them in a map.
    // *
    // * @param pattern the pattern used to extract fragments.
    // * @param rawXml the raw XML to extract fragments from.
    // * @return a map of the extracted fragments.
    // */
    // private Map<String, String> parseRawXml (final String pattern, final String rawXml)
    // {
    // final Map<String, String> fragmentMap = new HashMap<String, String> ();
    //
    // // Convert to char array for efficient manipulation.
    // final char[] patternArray = pattern.toCharArray ();
    // final char[] rawXmlArray = rawXml.toCharArray ();
    //
    // // Position indexes.
    // int patternPos = 0;
    // int rawXmlPos = 0;
    // final int patternEnd = patternArray.length - 1;
    // final int rawXmlEnd = rawXmlArray.length - 1;
    //
    // // Parse entire raw XML.
    // while ( !(rawXmlPos > rawXmlEnd))
    // {
    // // Get the next pattern character.
    // final boolean match = matchPattern (rawXmlArray[rawXmlPos], patternArray, patternPos);
    //
    // // Continue to increment pattern position while pattern matches, else reset to zero.
    // patternPos = match ? patternPos++ : 0;
    //
    // // Have we reached the end of the pattern?
    // if (patternPos == patternEnd)
    // {
    // fragmentMap.put (key, fragment);
    // }
    //
    // // Deal with next character.
    // rawXmlPos++;
    // }
    //
    // return fragmentMap;
    // }
    //
    // /**
    // * Deal with regular expression characters.
    // *
    // * @param targetChar the actual character to match against the pattern.
    // * @param pattern the whole pattern to be matched.
    // * @param patternPosition the current position in the pattern.
    // * @return true - matches pattern at this character, false - doe not match pattern at this character.
    // */
    // private boolean matchPattern (final char targetChar, final char[] pattern, final int patternPosition)
    // {
    // int pos = patternPosition;
    //
    // // Get the next pattern character.
    // char patternChar = pattern[patternPosition];
    //
    // // Handle special characters.
    // switch (patternChar)
    // {
    // case '\\':
    // // Escape character implies need to read the next character.
    // pos++;
    // if ( !(pos < pattern.length))
    // {
    // // Now gone too far to match.
    // return false;
    // }
    //
    // // Get escaped character.
    // patternChar = pattern[pos];
    //
    // // Process the escaped character.
    // switch (patternChar)
    // {
    // case 'w':
    // break;
    // default:
    // break;
    // }
    //
    // break;
    // default:
    // break;
    // }
    //
    // return targetChar == patternChar;
    // }
    //
    /**
     * Setter for replacementNamespaces property.
     * 
     * @param replacementNamespaces namespaces to be replaced.
     */
    public void setReplacementNamespaces (final Map<String, String> replacementNamespaces)
    {
        this.replacementNamespaces = replacementNamespaces;
    }
}
