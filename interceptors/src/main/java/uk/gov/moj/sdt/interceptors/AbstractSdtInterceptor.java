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
import java.io.Reader;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedWriter;
import org.apache.cxf.io.DelegatingInputStream;

/**
 * Abstract class holding common code for all SDT Interceptors.
 * 
 * @author Robin Compston
 * 
 */
public abstract class AbstractSdtInterceptor extends AbstractSoapInterceptor
{
    /**
     * Constructor for {@link AbstractSdtInterceptor}.
     * 
     * @param phase the phase at which interceptor is to run.
     */
    public AbstractSdtInterceptor (final String phase)
    {
        super (phase);
    }

    /**
     * Change the raw XML in the outbound message.
     * 
     * @param currentEnvelope the raw outbound SOAP XML to be changed.
     * @return the changed raw SOAP XML.
     */
    protected String changeOutboundMessage (final String currentEnvelope)
    {
        final String newCurrentEnvelope = currentEnvelope + "<this_has_been_hacked/>";
        return newCurrentEnvelope;
    }

    /**
     * Change the raw XML in the inbound message.
     * 
     * @param currentEnvelope the raw inbound SOAP XML to be changed.
     * @return the changed raw SOAP XML.
     */
    protected String changeInboundMessage (final String currentEnvelope)
    {
        final String newCurrentEnvelope = currentEnvelope + "<this_has_been_hacked/>";
        return newCurrentEnvelope;
    }

    /**
     * Allow inbound or outbound raw XML to be changed in flight.
     * 
     * @param message the message holding the output stream into which the XML is to be inserted.
     * @param xml the raw non generic XML to be inserted into the output stream XML.
     * @param insertionTagName the name of the tag in the generic XML into which the additional non generic XML is to be
     *            inserted.
     * @throws Fault exception encountered while reading content.
     */
    public void modifyMessage (final SoapMessage message, final String xml, final String insertionTagName)
            throws Fault
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
                org.apache.commons.io.IOUtils.closeQuietly (cs);
                final CachedOutputStream csnew = (CachedOutputStream) message.getContent (OutputStream.class);

                final String currentEnvelopeMessage = IOUtils.toString (csnew.getInputStream (), "UTF-8");
                csnew.flush ();
                org.apache.commons.io.IOUtils.closeQuietly (csnew);

                String res = changeOutboundMessage (currentEnvelopeMessage);
                res = res != null ? res : currentEnvelopeMessage;

                final InputStream replaceInStream = org.apache.commons.io.IOUtils.toInputStream (res, "UTF-8");

                org.apache.commons.io.IOUtils.copy (replaceInStream, os);
                replaceInStream.close ();
                org.apache.commons.io.IOUtils.closeQuietly (replaceInStream);

                os.flush ();
                message.setContent (OutputStream.class, os);
                org.apache.commons.io.IOUtils.closeQuietly (os);

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
                org.apache.commons.io.IOUtils.closeQuietly (is);

                String res = changeInboundMessage (currentEnvelopeMessage);
                res = res != null ? res : currentEnvelopeMessage;

                is = org.apache.commons.io.IOUtils.toInputStream (res, "UTF-8");
                message.setContent (InputStream.class, is);
                org.apache.commons.io.IOUtils.closeQuietly (is);
            }
            catch (final IOException ioe)
            {
                throw new RuntimeException (ioe);
            }
        }
    }

    /**
     * Read the contents of the input stream non-destructively so that it is still available for further interceptors.
     * 
     * @param message the SOAP message to be read from.
     * @return contents of payload.
     * @throws Fault exception encountered while reading content.
     */
    protected String readInputMessage (final SoapMessage message) throws Fault
    {
        // Contents to return to caller.
        String payload = "";

        // Get the stream from which to read the raw XML.
        final InputStream is = message.getContent (InputStream.class);
        if (is != null)
        {
            final CachedOutputStream bos = new CachedOutputStream ();

            try
            {
                // Use the appropriate input stream and restore it later.
                InputStream bis =
                        is instanceof DelegatingInputStream ? ((DelegatingInputStream) is).getInputStream () : is;

                // Copy the input stream holding the message to a cached stream so that we can work on it.
                org.apache.cxf.helpers.IOUtils.copyAndCloseInput (bis, bos);

                // Force the copied stream to make data available.
                bos.flush ();

                // Get hold of the cached input stream so that we can restore it.
                bis = bos.getInputStream ();

                // Restore the delegating input stream or the input stream.
                if (is instanceof DelegatingInputStream)
                {
                    ((DelegatingInputStream) is).setInputStream (bis);
                }
                else
                {
                    message.setContent (InputStream.class, bis);
                }

                // Get hold of the message contents.
                final StringBuilder sb = new StringBuilder ();
                bos.writeCacheTo (sb);

                // Convert to String.
                payload = sb.toString ();

                bos.close ();
            }
            catch (final IOException e)
            {
                throw new Fault (e);
            }
        }
        else
        {
            final Reader reader = message.getContent (Reader.class);
            if (reader != null)
            {
                try
                {
                    final CachedWriter writer = new CachedWriter ();
                    org.apache.cxf.helpers.IOUtils.copyAndCloseInput (reader, writer);
                    message.setContent (Reader.class, writer.getReader ());

                    // Get hold of the message contents.
                    writer.write (payload);
                }
                catch (final IOException e)
                {
                    throw new Fault (e);
                }
            }
        }

        return payload;
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