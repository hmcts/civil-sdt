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
package uk.gov.moj.sdt.interceptors.in;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
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
 * Class to intercept incoming messages and log them to the database.
 * 
 * <p> Generates <code>ServiceDomain</code> domain objects and by means of
 * a Thread Local <SdtContext> instance keeps track of the persisted entity.
 * 
 * Outbound message interceptors in the same thread can then access the persisted entity to update as required.
 * </p>
 * 
 * @author d195274
 * 
 */
public class ServiceRequestInboundInterceptor extends AbstractServiceRequest
{

    /**
     * Logger for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger (ServiceRequestInboundInterceptor.class);

    /**
     * The service request.
     */
    private IServiceRequest serviceRequest;
    
    /**
     * Create.
     */
    public ServiceRequestInboundInterceptor ()
    {
        super (Phase.RECEIVE);
    }

    /**
     * Create.
     * 
     * @param phase
     *            phase.
     */
    public ServiceRequestInboundInterceptor (final String phase)
    {
        super (phase);
    }

   

    @Override
    @Transactional
    public void handleMessage (final SoapMessage soapMessage) throws Fault
    {
        serviceRequest = new ServiceRequest ();
        LOG.info("[ServiceRequestInboundInterceptor:handleMessage] incoming message being processed");
        final String inputMessage = readInputMessage (soapMessage);
        SdtContext.getContext ().setInboundMessage (inputMessage);
        //setMessage();
        logMessage ();
    }  
   

    /**
     * Log the message to the persistence target.
     * <p>
     * Evaluates whether this is an incoming message (no persistence generated id) or an outgoing message (ThreadLocal
     * stored id exists) and hands on to appropriate processing method.
     * </p>
     * <p>
     * Incoming messages, once persisted, will generate an id which is stored on a ThreadLocal class to make it
     * available when the thread returns.
     * </p>
     * 
     */
    private void logMessage ()
    {
        final IGenericDao serviceRequestDao = this.getServiceRequestDao ();
        serviceRequest.setBulkCustomerId (extractBulkCustomerId ());
        serviceRequest.setRequestPayload (SdtContext.getContext ().getInboundMessage ());
        serviceRequest.setRequestDateTime (new LocalDateTime ());
        serviceRequest.setRequestType (extractRequestType ());
        serviceRequestDao.persist (serviceRequest);
        final Long serviceRequestID = serviceRequest.getId ();
        if (LOG.isInfoEnabled()) {
            LOG.info("[ServiceRequestInboundInterceptor:logMessage] service request :"
                    + serviceRequestID + " has been persisted");
        }
        SdtContext.getContext ().setServiceRequestId (serviceRequestID);
    }

    /**
     * Retrieve the Request Type from the inbound xmlMessage.
     * 
     * @return the request type (for example 'bulkRequest')
     */
    private String extractRequestType ()
    {
        String requestType = "";
        final String requestTypePattern = "requestType=\"";
        final String message = SdtContext.getContext ().getInboundMessage ();
        final int startPos = message.indexOf (requestTypePattern);
        // the next node contains the request type
        final String content = message.substring (startPos + requestTypePattern.length ());
        
        if (content.length ()>0)
        {
            final int endPos = content.indexOf ("\"");
            requestType = content.substring (0, endPos);
        }
        return requestType;
    }

    /**
     * Getter for the domain object representing the audit record.
     * @return the service request.
     */
    public IServiceRequest getServiceRequest ()
    {
        return serviceRequest;
    }

    /**
     * The setter for the domain object representing the audit record.
     * @param serviceRequest the IServiceRequest.
     */
    public void setServiceRequest (final IServiceRequest serviceRequest)
    {
        this.serviceRequest = serviceRequest;
    }

    /**
     * Extract the bulk customer id (sdt customer id - synonymous).
     * <p>
     * Always available as part of a submission
     * </p>
     * 
     * @return the bulk customer id
     */
    private String extractBulkCustomerId ()
    {
        return extractValue ("sdtCustomerId");
    }
}