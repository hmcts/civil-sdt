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
package uk.gov.moj.sdt.interceptors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.interceptor.SoapPreProtocolOutInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.phase.Phase;

/**
 * Test class.
 * 
 * @author RC
 * 
 */
public abstract class AbstractSdtInterceptor2 extends AbstractSoapInterceptor
{
    /**
     * Class.
     */
    public AbstractSdtInterceptor2 ()
    {
        super (Phase.PRE_STREAM);
        addBefore (SoapPreProtocolOutInterceptor.class.getName ());
    }

    /**
     * Change the raw XML in the outbound message.
     * 
     * @param currentEnvelope the raw outbound SOAP XML to be changed.
     * @return the changed raw SOAP XML.
     */
    protected abstract String changeOutboundMessage(String currentEnvelope);

    /**
     * Change the raw XML in the inbound message.
     * 
     * @param currentEnvelope the raw inbound SOAP XML to be changed.
     * @return the changed raw SOAP XML.
     */
    protected abstract String changeInboundMessage(String currentEnvelope);

    /**
     * Allow inbound or outbound raw XML to be changed in flight.
     * 
     * @param message message.
     * @throws Fault trouble.
     */
    public void modifyMessage (final SoapMessage message) throws Fault
    {
        boolean isOutbound = false;
        isOutbound =
                message == message.getExchange ().getOutMessage () ||
                        message == message.getExchange ().getOutFaultMessage ();

        if (isOutbound)
        {
            final OutputStream os = message.getContent (OutputStream.class);

            final CachedStream cs = new CachedStream ();
            message.setContent (OutputStream.class, cs);

            message.getInterceptorChain ().doIntercept (message);

            try
            {
                cs.flush ();
                IOUtils.closeQuietly (cs);
                final CachedOutputStream csnew = (CachedOutputStream) message.getContent (OutputStream.class);

                final String currentEnvelopeMessage = IOUtils.toString (csnew.getInputStream (), "UTF-8");
                csnew.flush ();
                IOUtils.closeQuietly (csnew);

                String res = changeOutboundMessage (currentEnvelopeMessage);
                res = res != null ? res : currentEnvelopeMessage;

                final InputStream replaceInStream = IOUtils.toInputStream (res, "UTF-8");

                IOUtils.copy (replaceInStream, os);
                replaceInStream.close ();
                IOUtils.closeQuietly (replaceInStream);

                os.flush ();
                message.setContent (OutputStream.class, os);
                IOUtils.closeQuietly (os);

            }
            catch (final IOException ioe)
            {
                throw new RuntimeException (ioe);
            }
        }
        else
        {
            try
            {
                InputStream is = message.getContent (InputStream.class);
                final String currentEnvelopeMessage = IOUtils.toString (is, "UTF-8");
                IOUtils.closeQuietly (is);

                String res = changeInboundMessage (currentEnvelopeMessage);
                res = res != null ? res : currentEnvelopeMessage;

                is = IOUtils.toInputStream (res, "UTF-8");
                message.setContent (InputStream.class, is);
                IOUtils.closeQuietly (is);
            }
            catch (final IOException ioe)
            {
                throw new RuntimeException (ioe);
            }
        }
    }

    /**
     * Cached stream implementation to do ....
     * 
     * @author Robin Compston
     *
     */
    private class CachedStream extends CachedOutputStream
    {
        /**
         * Stream.
         */
        public CachedStream ()
        {
            super ();
        }

        /**
         * Do Flush.
         * 
         * @throws IOException trouble.
         */
        protected void doFlush () throws IOException
        {
            currentStream.flush ();
        }

        /**
         * Do Flush.
         * 
         * @throws IOException trouble.
         */
        protected void doClose () throws IOException
        {
        }

        /**
         * Do Flush.
         * 
         * @throws IOException trouble.
         */
        protected void onWrite () throws IOException
        {
        }
    }
}