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

package uk.gov.moj.sdt.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class that replaces a token with a string.
 * 
 * @author d130680
 * 
 */
public final class TokenReplacer
{

    /**
     * Private constructer.
     */
    private TokenReplacer ()
    {

    }

    /**
     * Replaces tokens with specific string in a piece of text.
     * 
     * e.g. The quick {1} jumped {2} the lazy {3}
     * 
     * replacements map would consist of:
     * 
     * 1, fox
     * 2, over
     * 3, dog
     * 
     * @param text the text to tokenise
     * @param replacements map of token and string
     * @return tokenised text
     */
    public static String replaceTokens (final String text, final Map<String, String> replacements)
    {
        final Pattern pattern = Pattern.compile ("\\{([^}]*)\\}");
        final Matcher matcher = pattern.matcher (text);
        final StringBuffer buffer = new StringBuffer ();
        while (matcher.find ())
        {
            final String replacement = replacements.get (matcher.group (1));
            if (replacement != null)
            {
                matcher.appendReplacement (buffer, "");
                buffer.append (replacement);
            }
        }
        matcher.appendTail (buffer);
        return buffer.toString ();
    }

    // CHECKSTYLE:OFF
    public static void main (final String[] args)
    {
        Map<String, String> m = new HashMap<String, String> ();

        m.put ("1", "Albert");
        m.put ("2", "Bob");
        System.out.print (TokenReplacer.replaceTokens ("Hello {1} Hi {2}", m));

    }
    // CHECKSTYLE:ON
}
