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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.helpers.LoadingByteArrayOutputStream;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedWriter;
import org.apache.cxf.io.DelegatingInputStream;

import uk.gov.moj.sdt.enricher.AbstractSdtEnricher;
import uk.gov.moj.sdt.enricher.api.ISdtEnricher;

/**
 * Abstract class holding common code for all SDT Interceptors.
 * 
 * @author Robin Compston
 * 
 */
public abstract class AbstractSdtInterceptor extends AbstractSoapInterceptor
{
    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (AbstractSdtInterceptor.class);

    /**
     * List of enrichers to use. An enricher is a class which adds content to the outgoing XML before sending it.
     */
    private List<AbstractSdtEnricher> enricherList;

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
        LOGGER.debug ("Start - running through list of enrichers");

        // Loop through the list of enrichers and hope one of them produces an enriched message. Enrichers will enrich
        // messages where a parent tag can be found in the xml message otherwise the message will not be updated.
        String enrichedEnvelope = currentEnvelope;

        for (ISdtEnricher enricher : enricherList)
        {
            // Set the out bound message
            enrichedEnvelope = enricher.enrichXml (enrichedEnvelope);
        }

        LOGGER.debug ("completed - running through list of enrichers");

        return enrichedEnvelope;

    }

    /**
     * Read the contents of the input stream non-destructively so that it is still available for further interceptors.
     * 
     * @param message the SOAP message to be read from.
     * @return contents of payload.
     * @throws Fault exception encountered while reading content.
     */
    protected String readInputMessage (final SoapMessage message)
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
     * Read the contents of the output stream non-destructively so that it is still available for further interceptors.
     * 
     * @param message the SOAP message to be read from.
     * @param payload the new payload to add to the message.
     */
    protected void replaceOutputMessage (final SoapMessage message, final String payload)
    {
        try
        {
            // Get the existing output stream.
            final OutputStream os = message.getContent (OutputStream.class);

            // Make sure it is a CacheAndWriteOutputStream otherwise we cannot replace the contents.
            if ( !CacheAndWriteOutputStream.class.isAssignableFrom (os.getClass ()))
            {
                throw new IllegalStateException (
                        "Interceptors require  CXF to setup CacheAndWriteOutputStream so that contents "
                                + "can be read non destructively.");
            }

            // Cast to a stream using which we can replace the contents.
            final CacheAndWriteOutputStream cacheAndWriteOutputStream = CacheAndWriteOutputStream.class.cast (os);
            cacheAndWriteOutputStream.flush ();

            // Setup inner stream with correct content.
            final ByteArrayOutputStream replacementStream = new LoadingByteArrayOutputStream();
            final byte[] byteArray = payload.getBytes();
            replacementStream.write (byteArray, 0, byteArray.length);
            
            // Reset the inner stream which contains the output contents.
            cacheAndWriteOutputStream.resetOut (replacementStream, false);
        }
        catch (final IOException e)
        {
            throw new RuntimeException (e);
        }
    }

    /**
     * Read the contents of the output stream non-destructively so that it is still available for further interceptors.
     * 
     * @param message the SOAP message to be read from.
     * @return contents of payload.
     */
    protected String readOutputMessage (final SoapMessage message)
    {
        // Contents to return to caller.
        String payload = "";

        try
        {
            // Get the output stream produced so far by CXF.
            final OutputStream os = message.getContent (OutputStream.class);

            // Make sure it is a CachedOutputStream otherwise we cannot see the contents.
            if ( !CachedOutputStream.class.isAssignableFrom (os.getClass ()))
            {
                throw new IllegalStateException (
                        "Interceptors require  CXF to setup CachedOutputStream so that contents "
                                + "can be read non destructively.");
            }

            // Get the cached stream put in the message earlier.
            final CachedOutputStream csnew = CachedOutputStream.class.cast (os);
            csnew.flush ();

            // Get the data waiting to be written on the stream.
            payload = IOUtils.toString (csnew.getInputStream (), "UTF-8");

            // org.apache.commons.io.IOUtils.closeQuietly (csnew);
        }
        catch (final IOException e)
        {
            throw new RuntimeException (e);
        }

        return payload;
    }

    /**
     * Set enricher list.
     * 
     * @param enricherList enricher list
     */
    public void setEnricherList (final List<AbstractSdtEnricher> enricherList)
    {
        this.enricherList = enricherList;
    }
}