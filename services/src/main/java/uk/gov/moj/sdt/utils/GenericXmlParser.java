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
package uk.gov.moj.sdt.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class reads xml and parses it to extract target application specific xml.
 * 
 * @author d276205
 * 
 */
public class GenericXmlParser
{
    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (GenericXmlParser.class);

    /**
     * Name of enclosing element.
     */
    private String enclosingTag;

    /**
     * Parses xml to extract target app specific xml.
     * 
     * @return string
     */
    public String parse ()
    {

        String result = "";

        String rawXml = SdtContext.getContext ().getRawInXml ();

        // Remove linefeeds as they stop the regular expression working.
        rawXml = rawXml.replace ('\n', ' ');
        rawXml = rawXml.replace ('\r', ' ');

        // Build search pattern for extraction.
        final Pattern pattern =
                Pattern.compile ("<[\\w]+:" + getEnclosingTag () + "(.*?)>(.*?)</[\\w]+:" + getEnclosingTag () + ">");
        final Matcher matcher = pattern.matcher (rawXml);

        if (matcher.find ())
        {
            LOGGER.debug ("Found matching group[" + matcher.group () + "]");

            // Get the string matching the regular expression.
            result = matcher.group (2);

        }

        return result;

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

}
