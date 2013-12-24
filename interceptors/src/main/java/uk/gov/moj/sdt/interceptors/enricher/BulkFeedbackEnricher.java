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

        // Buffer to hold the developing result.
        String newXml = message;

        newXml = newXml.replace ('\n', ' ');
        newXml = newXml.replace ('\r', ' ');

        // Check to ensure the parent tag can be found in the message.
        if (super.findParentTag (message))
        {
            // Get the map created by the service which contains the fragments of response to be inserted into the
            // outgoing XML.
            final Map<String, String> targetApplicationRespMap =
                    SdtContext.getContext ().getTargetApplicationRespMap ();

            // Get iterator so we can look for all keys. Since order is undetermined we can assume nothing about finding
            // the requests in the same order and so must search the entire string each time.
            final Iterator<String> iter = targetApplicationRespMap.keySet ().iterator ();

            while (iter.hasNext ())
            {
                // Get the next request id.
                final String requestId = iter.next ();

                // Build a search pattern with this request id. Allow for any order of requestId and requestType
                // attributes.
                final Pattern pattern =
                        Pattern.compile ("(<[\\w]+:response[ \\w\"=]*requestId=\"" + requestId +
                                "[ \\w\"=]*>)\\s*<([\\w]+:)(responseDetail)/>\\s*(<[\\w]+:status)");

                // Match it against the result of all previous match replacements.
                final Matcher matcher = pattern.matcher (newXml);

                if (matcher.find ())
                {
                    if (LOGGER.isDebugEnabled ())
                    {
                        LOGGER.debug ("Found matching group[" + matcher.group () + "]");
                    }
                    
                    //CHECKSTYLE:OFF
                    // Form the replacement string from the matched groups and the extra XML.
                    String replacementXml = new StringBuilder().append (matcher.group (1))
                            .append ("<").append (matcher.group(2)).append(matcher.group(3)).append (">")
                            .append(targetApplicationRespMap.get (requestId))
                            .append ("</").append (matcher.group(2)).append(matcher.group(3)).append (">")
                            .append(matcher.group(4)).toString ();
                    //CHECKSTYLE:ON

                    replacementXml = replacementXml.replace ('\n', ' ');
                    replacementXml = replacementXml.replace ('\r', ' ');

                    if (LOGGER.isDebugEnabled ())
                    {
                        LOGGER.debug ("Replacement string[" + replacementXml + "]");
                    }

                    // Inject the system specific response into the current envelope
                    newXml = matcher.replaceFirst (replacementXml);
                }
                else
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
            final Pattern pattern =
                    Pattern.compile ("<[\\w]+:response[ \\w\"=]*requestId=\"[ \\w]*\">\\s*<[\\w]+:responseDetail/>\\s*"
                            + "<[\\w]+:status code=\"([ \\w]+)\"");
            final Matcher matcher = pattern.matcher (newXml);
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
            // Failure to find matching request in outgoing XML.
            LOGGER.debug("Parent tag [" + this.getParentTag () +
                    "] not found...skipping enrichment.");
        }

        return newXml;
    }
}
