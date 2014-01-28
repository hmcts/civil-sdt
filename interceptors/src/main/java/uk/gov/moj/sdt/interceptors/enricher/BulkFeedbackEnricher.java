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
 * $Id: $
 * $LastChangedRevision: $
 * $LastChangedDate: $
 * $LastChangedBy: $ */
package uk.gov.moj.sdt.interceptors.enricher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.utils.SdtContext;

/**
 * Bulk feedback enricher used to enrich outbound messages for get bulk feedback.
 * 
 * @author d130680
 * 
 */
public class BulkFeedbackEnricher extends AbstractSdtEnricher
{
    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (BulkFeedbackEnricher.class);

    @Override
    public String enrichXml (final String message)
    {
        if (LOGGER.isDebugEnabled ())
        {
            LOGGER.debug ("Message before enrichment [" + message + "]");
        }

        // Assume no change to input.
        String newXml = message;

        // Check to ensure the parent tag can be found in the message.
        if (super.findParentTag (message))
        {
            // Buffer to hold the developing result.
            newXml = message.replace ('\n', ' ');
            newXml = newXml.replace ('\r', ' ');

            // Get the map created by the service which contains the fragments of response to be inserted into the
            // outgoing XML.
            final Map<String, String> targetApplicationRespMap =
                    SdtContext.getContext ().getTargetApplicationRespMap ();

            // Get the map created by the service which contains the fragments of response to be inserted into the
            // outgoing XML.
            final Map<String, String> matchedRequestIdMap = new HashMap<String, String> ();
            SdtContext.getContext ().getTargetApplicationRespMap ();

            if (LOGGER.isDebugEnabled ())
            {
                LOGGER.debug ("Started enriching feedback response for " + targetApplicationRespMap.size () +
                        " request(s)");
            }

            // The XML to be assembled with replacement groups.
            final StringBuilder replacementXml = new StringBuilder ();

            // Current position in rawXml to copy from.
            int rawXmlPos = 0;

            // Build a search pattern with this request id. Allow for any order of requestId and requestType
            // attributes.
            Pattern pattern =
                    Pattern.compile ("(<[\\w]+:response[ \\w\"=]*requestId=\"([\\w-]+)\"[ \\w\"=]*>)\\s*<([\\w]+:)"
                            + "(responseDetail)/>\\s*(<[\\w]+:status)");

            // Match it against the result of all previous match replacements.
            Matcher matcher = pattern.matcher (newXml);

            while (matcher.find ())
            {
                if (LOGGER.isDebugEnabled ())
                {
                    LOGGER.debug ("Found matching group[" + matcher.group () + "]");
                }

                // Extract the request ID in the matching group.
                final String requestId = matcher.group (2);

                // CHECKSTYLE:OFF
                // Form the replacement string from the matched groups and the extra XML - populate with target
                // application specific content.
                String replacementGroup =
                        new StringBuilder ().append (matcher.group (1)).append ("<").append (matcher.group (3))
                                .append (matcher.group (4)).append (">")
                                .append (targetApplicationRespMap.get (requestId)).append ("</")
                                .append (matcher.group (3)).append (matcher.group (4)).append (">")
                                .append (matcher.group (5)).toString ();

                if ( !(targetApplicationRespMap.containsKey (requestId)))
                {
                    // Target application key does not exist - restore original content.
                    replacementGroup = matcher.group ();
                }
                // CHECKSTYLE:ON

                replacementGroup = replacementGroup.replace ('\n', ' ');
                replacementGroup = replacementGroup.replace ('\r', ' ');

                if (LOGGER.isDebugEnabled ())
                {
                    LOGGER.debug ("Replacement string[" + replacementGroup + "]");
                }

                // Inject the system specific response into the current envelope.
                replacementXml.append (newXml.substring (rawXmlPos, matcher.start ()));
                replacementXml.append (replacementGroup);
                rawXmlPos = matcher.end ();

                matchedRequestIdMap.put (requestId, requestId);
            }

            if ( !(rawXmlPos > newXml.length ()))
            {
                // Copy any remainder of the uncopied rawXml.
                replacementXml.append (newXml.substring (rawXmlPos));
            }

            newXml = replacementXml.toString ();

            final Iterator<String> iter = targetApplicationRespMap.keySet ().iterator ();
            while (iter.hasNext ())
            {
                final String requestId = iter.next ();

                // Get next target application entry.
                if ( !(matchedRequestIdMap.containsKey (requestId)))
                {
                    // Failure to find matching request in outgoing XML.
                    LOGGER.error ("Failure to find matching request in outgoing bulk feedback XML for request id[" +
                            requestId + "].");
                    throw new UnsupportedOperationException (
                            "Failure to find matching request in outgoing bulk feedback XML for request id[" +
                                    requestId + "].");
                }
            }

            // Now check that there are no responses without case management specific content inserted.
            pattern =
                    Pattern.compile ("<[\\w]+:response[ \\w\"=]*requestId=\"[ \\w]*\">\\s*<[\\w]+:responseDetail/>\\s*"
                            + "<[\\w]+:status code=\"([ \\w]+)\"");
            matcher = pattern.matcher (newXml);
            if (matcher.find ())
            {
                // Ignore rejected responses which do not need to be enhanced.
                if ( !matcher.group (1).equals (IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus ()))
                {
                    // We found a response that has not been enriched. Failure to find matching request in outgoing XML.
                    LOGGER.error ("Detected unenriched response tag[" + matcher.group () +
                            "] within bulk feedback response XML.");
                    throw new UnsupportedOperationException ("Detected unenriched response tag[" + matcher.group () +
                            "] within bulk feedback response XML.");
                }
            }

            if (LOGGER.isDebugEnabled ())
            {
                LOGGER.debug ("Message after enrichment [" + newXml + "]");
            }
        }
        else
        {
            if (LOGGER.isDebugEnabled ())
            {
                // Failure to find matching request in outgoing XML.
                LOGGER.debug ("Parent tag [" + this.getParentTag () + "] not found...skipping enrichment.");
            }
        }

        return newXml;
    }
}
