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

package uk.gov.moj.sdt.consumers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.producers.prototype.common.disc_types.GetGreetingRequestType;
import uk.gov.moj.sdt.producers.prototype.common.disc_types.GetGreetingResponseType;
import uk.gov.moj.sdt.producers.prototype.greetingendpoint.IGreetingEndpointPortType;

/**
 * 
 * 
 * @author d6997
 *
 */
public class Consumer
{
    /**
     * Logger for this class.
     */
    private static final Log LOGGER = LogFactory.getLog (Consumer.class);

    /**
     * The proxy endpoint injected by Spring.
     */
    private IGreetingEndpointPortType greetingEndpointPortType;

    /**
     * Setter for greetingEndpointPortType.
     * 
     * @param greetingEndpointPortType
     *            new value of greetingEndpointPortType.
     */
    public void setGreetingEndpointPortType (final IGreetingEndpointPortType greetingEndpointPortType)
    {
        this.greetingEndpointPortType = greetingEndpointPortType;
    }

    /**
     * Call SOAP endpoint and report reponse.
     * 
     * @param name the name of the person to be greeted.
     * @return the greeting from the remote application.
     */
    public String getGreeting (final String name)
    {
        LOGGER.debug ("Call getGreeting endpoint, name=" + name);

        // Set up the argument for this endpoint.
        final GetGreetingRequestType getGreetingRequestType = new GetGreetingRequestType ();
        getGreetingRequestType.setArg0 (name);

        // Call the proxy to access the web service, passing the name of the
        // called as a parameter.
        final GetGreetingResponseType getGreetingResponseType =
                this.greetingEndpointPortType.getGreeting (getGreetingRequestType);

        // Return extracted return value out of the return object for this
        // endpoint.
        return getGreetingResponseType.getReturn ();
    }
}
