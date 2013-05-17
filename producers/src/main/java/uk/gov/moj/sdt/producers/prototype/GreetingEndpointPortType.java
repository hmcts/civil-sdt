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

package uk.gov.moj.sdt.producers.prototype;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.producers.prototype.api.IDiscZoneBusinessObject;
import uk.gov.moj.sdt.producers.prototype.common.disc_types.GetGreetingRequestType;
import uk.gov.moj.sdt.producers.prototype.common.disc_types.GetGreetingResponseType;
import uk.gov.moj.sdt.producers.prototype.greetingendpoint.IGreetingEndpointPortType;

/**
 * Implementation of {@link IGreetingEndpointPortType}.
 * 
 * @author Robin Compston
 */

@WebService (serviceName = "GreetingEndpoint", portName = "GreetingEndpointPort", 
targetNamespace = "http://prototype.producers.sdt.moj.gov.uk/GreetingEndpoint", 
wsdlLocation = "wsdl/GreetingEndpoint.wsdl", 
endpointInterface = "uk.gov.moj.sdt.producers.prototype.greetingendpoint.IGreetingEndpointPortType")
public class GreetingEndpointPortType implements IGreetingEndpointPortType
{
    /**
     * Logger for this class.
     */
    private static final Log LOGGER = LogFactory.getLog (GreetingEndpointPortType.class);

    /**
     * Business object to implement web service, dependency injected by Spring.
     */
    private IDiscZoneBusinessObject discZoneBusinessObject;

    /**
     * Setter for {@link discZoneBusinessObject}.
     * 
     * @param discZoneBusinessObject new value of discZoneBusinessObject.
     */
    public void setDiscZoneBusinessObject (final IDiscZoneBusinessObject discZoneBusinessObject)
    {
        this.discZoneBusinessObject = discZoneBusinessObject;
    }

    /**
     * Implementation of {@link IGreetingEndpointPortType.getGreetingRequestType ()}.
     * 
     * @param getGreetingRequestType type of greeting object.
     * @return type of greeting response.
     */
    public GetGreetingResponseType getGreeting (final GetGreetingRequestType getGreetingRequestType)
    {
        LOGGER.debug (this.getClass ().getName () + " endpoint called, getGreetingRequestType=" +
                getGreetingRequestType.getArg0 ());

        final String response = discZoneBusinessObject.getGreeting (getGreetingRequestType.getArg0 ());
        final GetGreetingResponseType getGreetingResponseType = new GetGreetingResponseType ();
        getGreetingResponseType.setReturn (response);
        return getGreetingResponseType;
    }
}
