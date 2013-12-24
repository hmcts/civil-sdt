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
        // Get iterator so we can traverse the list of requests and the payload (raw XML) to each one.
        final Iterator<IIndividualRequest> iter = individualRequests.iterator ();

        // Match it against the result of all previous match replacements.
        final String rawXml = SdtContext.getContext ().getRawInXml ();

        // Retrieve all namespaces
        final Map<String, String> allNamespaces =
                XmlNamespaceUtils.extractAllNamespaces (rawXml, replacementNamespaces);

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

            // Build a search pattern with this request id. Allow for any order of requestId
            // attributes.
            final Pattern pattern =
                    Pattern.compile ("<[\\w]+:request [\\w]+=\"[\\w]+\" requestId=\"" +
                            individualRequest.getCustomerRequestReference () + "\">(.*?)</[\\w]+:request>");

            LOGGER.debug ("Raw Xml after linefeed stripping is " + rawXml);

            final Matcher matcher = pattern.matcher (rawXml);

            String individualRequestRawXml = null;

            while (matcher.find ())
            {
                LOGGER.debug ("Found matching group[" + matcher.group () + "]");

                // Capture the raw XML associated with this request.
                individualRequestRawXml = matcher.group (1).trim ();

                LOGGER.debug ("Individual request raw XML[" + individualRequestRawXml + "]");

                // Find namespaces applicable for fragment
                final Map<String, String> matchingNamespaces =
                        XmlNamespaceUtils.findMatchingNamespaces (individualRequestRawXml, allNamespaces);

                // Embed namespaces within fragment
                individualRequestRawXml = XmlNamespaceUtils.addNamespaces (individualRequestRawXml, matchingNamespaces);

                LOGGER.debug ("Individual request raw XML[" + individualRequestRawXml + "]");

                individualRequest.setRequestPayload (individualRequestRawXml);
            }
        }

    }

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
