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

package uk.gov.moj.sdt.enricher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.utils.SdtContext;

/**
 * Generic xml enricher, used to enrich outbound messages.
 * 
 * @author d276205
 * 
 */
public class GenericEnricher extends AbstractSdtEnricher
{

    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (GenericEnricher.class);

    @Override
    public String enrichXml (final String message)
    {
        if (LOGGER.isDebugEnabled ())
        {
            LOGGER.debug ("Message before enrichment [" + message + "]");
        }

        String newXml = message;
        // Remove linefeeds as they stop the regular expression working.
        newXml = newXml.replace ('\n', ' ');
        newXml = newXml.replace ('\r', ' ');
        
        // Check to ensure the parent tag can be found in the message.
        if (super.findParentTag (message))
        {
            LOGGER.debug("Parent tag [" + this.getParentTag () +  "] found...performing enrichment.");

            // Get the system specific response from thread local to inject into the outbound message
            String replacementXml = SdtContext.getContext ().getRawOutXml ();

            //Build search pattern for insertion point.
            final Pattern pattern = Pattern.compile ("<[\\w]+:" + getInsertionTag () + "/>");
            final Matcher matcher = pattern.matcher (newXml);

            if (matcher.find ())
            {
                LOGGER.debug ("Found matching group[" + matcher.group () + "]");

                // Get the string matching the regular expression.
                final String singleLineTag = matcher.group ();
                
                // Add the start and end tag to the replacement xml.
                replacementXml = changeToStartTag (singleLineTag) + replacementXml + changeToEndTag (singleLineTag);

                LOGGER.debug ("Replacement string[" + replacementXml + "]");

                // Inject the system specific response into the current envelope.
                newXml = matcher.replaceFirst (replacementXml);
            }

            // TODO - need to manipulate the namespace declaration
        }
        else
        {
            // Failure to find matching request in outgoing XML.
            LOGGER.debug("Parent tag [" + this.getParentTag () +
                    "] not found...skipping enrichment.");
        }


        if (LOGGER.isDebugEnabled ())
        {
            LOGGER.debug ("Message after enrichment [" + newXml + "]");
        }

        return newXml;

    }
    
    //CHECKSTYLE:OFF
    /**
     * Parse. This is test code.
     * @return string
     */
    public String parse () {
        
        String result="";
        
        // Match it against the result of all previous match replacements.
        String rawXml = SdtContext.getContext ().getRawInXml ();

        // Remove linefeeds as they stop the regular expression working.
        rawXml = rawXml.replace ('\n', ' ');
        rawXml = rawXml.replace ('\r', ' ');

        //Build search pattern for insertion point.
        final Pattern pattern = Pattern.compile ("<[\\w]+:" + getInsertionTag () + "(.*?)>(.*?)</[\\w]+:" + getInsertionTag () + ">");
        final Matcher matcher = pattern.matcher (rawXml);

        if (matcher.find ())
        {
            LOGGER.debug ("Found matching group[" + matcher.group () + "]");

            // Get the string matching the regular expression...Not sure about group ID value.
            result=matcher.group (2);
            
        }
        
        return result;

    }
    //CHECKSTYLE:ON

}
