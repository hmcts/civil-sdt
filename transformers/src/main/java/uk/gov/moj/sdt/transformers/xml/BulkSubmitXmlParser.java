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
 * $Id: ClaimXsdTest.java 16414 2013-05-29 10:56:45Z agarwals $
 * $LastChangedRevision: 16414 $
 * $LastChangedDate: 2013-05-29 11:56:45 +0100 (Wed, 29 May 2013) $
 * $LastChangedBy: holmessm $ */
package uk.gov.moj.sdt.transformers.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.transformers.xml.api.IBulkSubmitXmlParser;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.visitor.VisitableTreeWalker;

/**
 * Implements {@link IBulkSubmitXmlParser}.
 * 
 * Parser to take raw XML captured by interceptor (based on {@link org.apache.cxf.interceptor.Interceptor}) and extract
 * particular fragments of it which are used to populate domain objects with raw XML fragments. This is done to ensure
 * that SDT only handles generic XML content and the non generic content (Case management system specific) is stored in
 * handled in domain objects and in the database as raw XML.
 * 
 * @author Robin Compston
 * 
 */
public class BulkSubmitXmlParser implements IBulkSubmitXmlParser
{
    /**
     * Static logging object.
     */
    private static final Logger LOG = LoggerFactory.getLogger (VisitableTreeWalker.class);

    @Override
    public List<String> getRequests ()
    {
        // Get the value of the raw input XML for this thread.
        final String rawXml = SdtContext.getContext ().getRawInXml ();

        // Parse it to pull out the individual requests.
        // TODO correct tag which will become generic.
        return getRepeatingGroup (rawXml, "mcolRequests");
    }

    /**
     * Parse the given XML to pull out a repeating group identified by the given tag name.
     * 
     * @param rawXml the XML in which to look for the repeating group.
     * @param tagName the tag name of the repeating group.
     * @return a list of the repeating group.
     */
    private List<String> getRepeatingGroup (final String rawXml, final String tagName)
    {
        LOG.debug ("Matching XML on tag name [" + tagName + "]");

        // List of discovered matching groups to return to caller.
        final List<String> requests = new ArrayList<String> ();

        // Get rid of line feeds in XML.
        String xml = rawXml.replace ('\n', ' ');
        xml = xml.replace ('\r', ' ');

        // Define pattern to match and extract portion of XML between given start and end tag name.
        final Pattern pattern = Pattern.compile ("<\\w+:" + tagName + "[ >].*</\\w+:" + tagName + ">");

        // Create matcher to match given XML on basis of pattern above.
        final Matcher matcher = pattern.matcher (xml);

        // Check all occurances
        while (matcher.find ())
        {
            final String group = matcher.group ();

            LOG.debug ("Found matched group [" + group + "]");

            requests.add (group);
        }

        return requests;
    }
}
