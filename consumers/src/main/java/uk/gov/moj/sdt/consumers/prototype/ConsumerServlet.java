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

package uk.gov.moj.sdt.consumers.prototype;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Test class to give access to greeting endpoint web service (example service) by providing a servlet that can be
 * called from a browser.
 * 
 * @author Robin Compston
 * 
 */
public class ConsumerServlet extends HttpServlet
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Message parameter from HTTP request.
     */
    private static final String MESSAGE_PARAMETER = "message=";

    /**
     * Implementation of {@link HttpServlet.service()}.
     * 
     * @param request the request from the browser.
     * @param response the response to the browser.
     */
    // CHECKSTYLE:OFF Complexity is acceptable
    public void service (HttpServletRequest request, HttpServletResponse response) throws IOException
    // CHECKSTYLE:ON
    {
        // Use "request" to read incoming HTTP headers (e.g. cookies)
        // and HTML form data (e.g. data the user entered and submitted)

        // Use "response" to specify the HTTP response line and headers
        // (e.g. specifying the content type, setting cookies).

        // Use "out" to send content to browser
        response.setContentType ("text/plain");
        final PrintWriter out = response.getWriter ();

        // Get consumer bean which we want to test.
        final Consumer consumer =
                (Consumer) WebApplicationContextUtils.getWebApplicationContext (
                        request.getSession ().getServletContext ()).getBean ("uk.gov.moj.prototype.consumer.Consumer");

        String message = "";

        final String requestQuery = request.getQueryString ();
        if (requestQuery != null && requestQuery.length () > 0)
        {
            final int nameParameterPos = requestQuery.indexOf (MESSAGE_PARAMETER);
            if (nameParameterPos != -1)
            {
                int nextParameterValuePos = requestQuery.indexOf ('&');
                if (nextParameterValuePos == -1 || nextParameterValuePos < nameParameterPos)
                {
                    nextParameterValuePos = requestQuery.length ();
                }

                message =
                        requestQuery.substring (nameParameterPos + MESSAGE_PARAMETER.length (), nextParameterValuePos);
            }

            if (message != null && message.length () > 0)
            {
                try
                {
                    message = URLDecoder.decode (message, "UTF-8");
                }
                catch (final UnsupportedEncodingException uee)
                {
                    // Do nothing for now.
                }
            }
        }

        if (request.getPathInfo ().equals ("/local"))
        {
            // Output the message returned by the remote endpoint.
            out.print ("Calling producer with message=" + message + "\n");

            // Local endpoint is called synchronously.
            out.print ("Endpoint response=" + consumer.getGreeting (message) + "\n");
        }
        else
        {
            // Output the message returned by the remote endpoint.
            out.print ("Path not recognised. Must be of form 'http://localhost:8888/consumer/test/local?message=...'"
                    + " or 'http://localhost:8888/consumer/test/mule?message=...'");
        }
    }
}
