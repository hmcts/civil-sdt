/* Copyrights and Licenses
 * 
 * Copyright (c) 2012-2014 by the Ministry of Justice. All rights reserved.
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
package uk.gov.moj.sdt.interceptors.out;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.phase.Phase;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.domain.ServiceRequest;
import uk.gov.moj.sdt.domain.api.IServiceRequest;
import uk.gov.moj.sdt.interceptors.AbstractServiceRequest;
import uk.gov.moj.sdt.utils.SdtContext;

/**
 * Class to intercept outgoing messages to audit them.
 * 
 * @author d195274
 * 
 */
public class ServiceRequestOutboundInterceptor extends AbstractServiceRequest 
{

    /**
     * Logger for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger (ServiceRequestOutboundInterceptor.class);

   

    /**
     * Create.
     */ 
    public ServiceRequestOutboundInterceptor ()
    {
        super (Phase.POST_STREAM);
    }

    /**
     * Create.
     * 
     * @param phase
     *            phase.
     */
    public ServiceRequestOutboundInterceptor (final String phase)
    {
        super (phase);
    }   

    @Override
    @Transactional
    public void handleMessage (final SoapMessage soapMessage) throws Fault
    {
        readOutputMessage(soapMessage);
    }    

    /**
     * Extract the bulk reference.
     * <p>
     * Bulk reference is only applicable on certain requests:
     * <ul>
     * <li>Bulk submission - not available till response</li>
     * <li>Submit Query - not applicable</li>
     * <li>Bulk report - available on both request and response.</li>
     * </ul>
     * 
     * @return the value of the bulk reference
     */
    private String extractBulkReference ()
    {
        return extractValue ("sdtBulkReference");
    }
   

    /**
     * When the application closes the message output stream the service request
     * must also be persisted. Since Hibernate wraps everything up into
     * transactional units trying to write the output stream value anywhere else
     * in the stack seems to miss the Hibernate persist ending up with an empty
     * response payload being persisted.
     * 
     * @param envelope the String envelope value of the output soap message.
     */    
    public void persistEnvelope(final String envelope) {
        final SdtContext sdtContext = SdtContext.getContext();
        try {
            //if we don't deregister here then further interceptors will keep calling this. 
           
            final IGenericDao serviceRequestDao = this.getServiceRequestDao();

            if (LOG.isInfoEnabled()) {
                LOG.info("[ServiceRequestOutputboundInterceptor: onClose] - " +
                        "creating outbound payload database log for ServiceRequest: "
                        + sdtContext.getServiceRequestId());
            }
            final IServiceRequest serviceRequest = serviceRequestDao.fetch(
                    ServiceRequest.class, sdtContext.getServiceRequestId());            
            this.setMessage (envelope);            
            serviceRequest.setResponsePayload(envelope);
            serviceRequest.setResponseDateTime(new LocalDateTime());
            final String bulkReference = extractBulkReference ();
            if(bulkReference.length ()>0) {
                serviceRequest.setBulkReference (bulkReference);
            }
            serviceRequestDao.persist(serviceRequest);
        } finally {
            sdtContext.remove();
        }
    }

    /**
     * Read the contents of the output stream non-destructively so that it is
     * still available for further interceptors.
     * 
     * @param message
     *            the SOAP message to be read from.
     * @throws Fault
     *             exception encountered while reading content.
     */
    protected void readOutputMessage(final SoapMessage message) throws Fault {   
        // Outbound message.

        // Get the original stream which is due to be written from the message.
        final OutputStream os = message.getContent (OutputStream.class);

        try
        {
            // Get the cached stream put in the message earlier.
            final CachedOutputStream csnew = (CachedOutputStream) message.getContent (OutputStream.class);

            // Get the data waiting to be written on the stream.
            final String currentEnvelopeMessage = IOUtils.toString (csnew.getInputStream (), "UTF-8");
            csnew.flush ();
            org.apache.commons.io.IOUtils.closeQuietly (csnew);
            this.persistEnvelope(currentEnvelopeMessage);

            // Turn the modified data into a new input stream.
            final InputStream replaceInStream =
                    org.apache.commons.io.IOUtils.toInputStream (currentEnvelopeMessage, "UTF-8");

            // Copy the new input stream to the output stream and close the input stream.
            org.apache.commons.io.IOUtils.copy (replaceInStream, os);
            replaceInStream.close ();
            org.apache.commons.io.IOUtils.closeQuietly (replaceInStream);

            // Flush the output stream, set it in the message and close it.
            os.flush ();
            message.setContent (OutputStream.class, os);
            org.apache.commons.io.IOUtils.closeQuietly (os);

        }
        catch (final IOException ioe)
        {
            throw new RuntimeException (ioe);
        }
    }
                 
}


