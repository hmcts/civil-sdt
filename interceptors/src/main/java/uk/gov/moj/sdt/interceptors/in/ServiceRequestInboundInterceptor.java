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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.domain.ServiceRequest;
import uk.gov.moj.sdt.domain.api.IServiceRequest;
import uk.gov.moj.sdt.interceptors.AbstractServiceRequest;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.ServerHostName;

/**
 * Class to intercept incoming messages and log them to the database.
 * 
 * <p>
 * Generates <code>ServiceDomain</code> domain objects and by means of a Thread Local <SdtContext> instance keeps track
 * of the persisted entity.
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
     * Create.
     */
    public ServiceRequestInboundInterceptor ()
    {
        super (Phase.RECEIVE);
        addAfter (XmlInboundInterceptor.class.getName ());
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
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void handleMessage (final SoapMessage soapMessage) throws Fault
    {
        LOG.debug ("ServiceRequestInboundInterceptor handle incoming message being processed");

        logMessage ();
    }

    /**
     * Log the message to the persistence target.
     * <p>
     * Incoming messages, once persisted, will generate an id which is stored on a ThreadLocal class to make it
     * available when the outbound service request interceptor runs.
     * </p>
     * 
     */
    private void logMessage ()
    {
        final IGenericDao serviceRequestDao = this.getServiceRequestDao ();

        // Prepare log message for Hibernate.
        final IServiceRequest serviceRequest = new ServiceRequest ();
        serviceRequest.setBulkCustomerId (extractBulkCustomerId ());
        serviceRequest.setRequestPayload (SdtContext.getContext ().getRawInXml ());
        serviceRequest.setRequestDateTime (new LocalDateTime ());
        serviceRequest.setRequestType (extractRequestType ());
        
        //Get the server Host Name
        final String hostName = ServerHostName.getHostName ();
        serviceRequest.setServerHostName (hostName);

        serviceRequestDao.persist (serviceRequest);

        // Retrieve the id assigned in by Hibernate and store it for the outbound service interceptor to use later.
        final Long serviceRequestID = serviceRequest.getId ();
        SdtContext.getContext ().setServiceRequestId (serviceRequestID);
    }

    /**
     * Retrieve the Request Type from the inbound xmlMessage. Assumes inbound message has already been setup by the
     * XmlInboundInterceptor.
     * 
     * @return the request type (for example 'bulkRequest')
     */
    private String extractRequestType ()
    {
        String requestType = "";
        final String requestTypePattern = "requestType=\"";
        final String message = SdtContext.getContext ().getRawInXml ();
        final int startPos = message.indexOf (requestTypePattern);

        // the next node contains the request type
        final String content = message.substring (startPos + requestTypePattern.length ());

        if (content.length () > 0)
        {
            final int endPos = content.indexOf ("\"");
            requestType = content.substring (0, endPos);
        }
        return requestType;
    }

    /**
     * Extract the bulk customer id (sdt customer id - synonymous). Assumes inbound message has already been setup by
     * the XmlInboundInterceptor.
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

    /**
     * Simple method to extract a text value from a String of xml.
     * <p>
     * This method does not use the whole framework of generating a DOM and parsing that. Instead it uses simple String
     * functionality to locate and extract a String.
     * </p>
     * 
     * @param nodeName The name of an xml node e.g. 'customerId'
     * @return the content of the node or null
     */
    private String extractValue (final String nodeName)
    {
        final String xmlMessage = SdtContext.getContext ().getRawInXml ();

        String nodeContent = "";
        if (nodeName == null || null == xmlMessage || xmlMessage.length () < nodeName.length ())
        {
            return "";
        }
        
        int startPos = xmlMessage.indexOf (nodeName);
        if (startPos != -1)
        {
            startPos += nodeName.length ();
            nodeContent = xmlMessage.substring (startPos);
            final int endPos = nodeContent.indexOf ("<");
            nodeContent = nodeContent.substring (1, endPos);
        }
        
        return nodeContent;
    }
}