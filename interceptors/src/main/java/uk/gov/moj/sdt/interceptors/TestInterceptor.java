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
package uk.gov.moj.sdt.interceptors;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.cxf.binding.soap.Soap11;
import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.interceptor.EndpointSelectionInterceptor;
import org.apache.cxf.binding.soap.interceptor.ReadHeadersInterceptor;
import org.apache.cxf.binding.soap.model.SoapOperationInfo;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.model.BindingOperationInfo;

/**
 * Test interceptor class.
 * 
 * @author d6997
 * 
 */
public class TestInterceptor extends AbstractSoapInterceptor
{
    /**
     * Test interceptor to prove concept.
     */
    public TestInterceptor ()
    {
        super (Phase.READ);
        addAfter (ReadHeadersInterceptor.class.getName ());
        addAfter (EndpointSelectionInterceptor.class.getName ());
    }

    /**
     * Handle the message passed by CXF.
     * 
     * @param message message received.
     * @throws Fault some nasty error.
     */
    public void handleMessage (final SoapMessage message) throws Fault
    {
        if (message.getVersion () instanceof Soap11)
        {
            @SuppressWarnings ("unchecked")
            final Map<String, List<String>> headers =
                    CastUtils.cast ((Map<String, List<String>>) message.get (Message.PROTOCOL_HEADERS));
            if (headers != null)
            {
                final List<String> sa = headers.get ("SOAPAction");
                if (sa != null && sa.size () > 0)
                {
                    String action = sa.get (0);
                    if (action.startsWith ("\""))
                    {
                        action = action.substring (1, action.length () - 1);
                    }
                    getAndSetOperation (message, action);
                }
            }
        }
        else if (message.getVersion () instanceof Soap12)
        {
            final int i = 0;
        }
    }

    /**
     * Get and set operation.
     * 
     * @param message the message to process.
     * @param action the action to do on it.
     */
    private void getAndSetOperation (final SoapMessage message, final String action)
    {
        if ("".equals (action))
        {
            return;
        }
        
        final Exchange ex = message.getExchange ();
        final Endpoint ep = ex.get (Endpoint.class);
        final BindingOperationInfo bindingOp = null;
        final Collection<BindingOperationInfo> bops = ep.getBinding ().getBindingInfo ().getOperations ();
        for (BindingOperationInfo boi : bops)
        {
            final SoapOperationInfo soi = (SoapOperationInfo) boi.getExtensor (SoapOperationInfo.class);
            
            if (soi != null && soi.getAction ().equals (action))
            {
                if (bindingOp != null)
                {
                    // more than one op with the same action, will need to parse normally return; } bindingOp = boi; } }
                    // if (bindingOp != null) { ex.put(BindingOperationInfo.class, bindingOp);
                    // ex.put(OperationInfo.class,
                    // bindingOp.getOperationInfo()); } } }
                    final int i = 0;
                }
            }
        }
    }
}
