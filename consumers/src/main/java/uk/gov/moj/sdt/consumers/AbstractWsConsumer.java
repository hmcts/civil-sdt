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

import javax.xml.ws.BindingProvider;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import uk.gov.moj.sdt.ws._2013.sdt.targetappinternalendpoint.ITargetAppInternalEndpointPortType;

/**
 * 
 * @author Manoj Kulkarni
 * 
 */
public abstract class AbstractWsConsumer
{

    /**
     * 
     * @param webServiceEndPoint the Web Service End Point URL
     * @param connectionTimeOut the connection time out value
     * @param receiveTimeOut the acknowledgement time out value
     * @return the target application end point port bean i.e. the client interface.
     */
    public ITargetAppInternalEndpointPortType createClient (final String webServiceEndPoint,
                                                            final long connectionTimeOut, final long receiveTimeOut)
    {
        // Get the SOAP proxy client.
        final ITargetAppInternalEndpointPortType client = this.createTargetAppEndPoint ();

        final Client clientProxy = ClientProxy.getClient (client);

        // Set endpoint address
        final BindingProvider provider = (BindingProvider) client;
        provider.getRequestContext ().put (BindingProvider.ENDPOINT_ADDRESS_PROPERTY, webServiceEndPoint);

        final HTTPConduit httpConduit = (HTTPConduit) clientProxy.getConduit ();
        final HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy ();
        // Specifies the amount of time, in milliseconds, that the client will attempt to establish a connection before
        // it times out
        httpClientPolicy.setConnectionTimeout (connectionTimeOut);
        // Specifies the amount of time, in milliseconds, that the client will wait for a response before it times out.
        httpClientPolicy.setReceiveTimeout (receiveTimeOut);
        httpConduit.setClient (httpClientPolicy);

        return client;

    }

    /**
     * Method injection method that will be invoked by Spring container
     * to give a new instance of the bean every time.
     * 
     * @return ITargetAppInternalEndpointPortType the end point interface.
     */
    protected ITargetAppInternalEndpointPortType createTargetAppEndPoint ()
    {
        // IMPORTANT: Please do not add any implementation here. The method intentionally returns null. At run-time,
        // Spring will
        // override this method with
        // implementation code that will return an new instance of the end point bean when this method is called.

        return null;
    }

}
