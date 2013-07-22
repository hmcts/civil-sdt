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

import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.domain.ServiceRequest;
import uk.gov.moj.sdt.domain.api.IServiceRequest;
import uk.gov.moj.sdt.interceptors.AbstractSdtInterceptor;
import uk.gov.moj.sdt.interceptors.api.IServiceRequestInterceptor;


/**
 * Class to intercept incoming and outgoing messages to audit them.
 * 
 * @author d195274
 * 
 */
public class ServiceRequestInterceptor extends AbstractSdtInterceptor implements IServiceRequestInterceptor
{

    /**
     * Logger for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger (ServiceRequestInterceptor.class);

    /**
     * The persistence class for this interceptor.
     */
    private IGenericDao serviceRequestDao;
    
    /**
     * Domain object representing a service request.
     */
    private IServiceRequest serviceRequest;
    
    /**
     * The soap message as a String.
     */
    private String xmlMessage;
    
    /**
     * Create.
     */
    public ServiceRequestInterceptor ()
    {
        super (Phase.RECEIVE);
    }

    /**
     * Create.
     * 
     * @param phase
     *            phase.
     */
    public ServiceRequestInterceptor (final String phase)
    {
        super (phase);
    }

    /**
     * get.
     * @return the xml message representing the entire Soap message.
     */
    public String getMessage ()
    {
        return xmlMessage;
    }

    /**
     * set.
     * @param xmlMessage the original Soap Message expressed as a String.
     */
    public void setMessage (final String xmlMessage)
    {
        this.xmlMessage = xmlMessage;
    }

    @Override
    public void handleMessage (final SoapMessage soapMessage) throws Fault
    {
        xmlMessage = readInputMessage (soapMessage);
        logMessage();
    }

    /**
     * log the message to the persistence target.
     * <p>
     * Evaluates whether this is an incoming message (no persistence generated id)
     * or an outgoing message (ThreadLocal stored id exists) and hands on
     * to appropriate processing method.
     * </p>
     * <p>
     * Incoming messages, once persisted, will generate an id which is
     * stored on a ThreadLocal class to make it available when the thread returns.
     * </p>
     * 
     */
    private void logMessage ()
    {
        if(null != (Object)Storage.getId ()) {
            logOutgoingMessage();
        } else {
            logIncomingMessage();
        }
    }
    
    /**
     * The message is incoming and needs to be added to the database.
     * <p>
     * There are three different types of incoming requests and they have different information available.
     * In this method we try and record all information whether present or not. 
     * </p>
     */
    private void logIncomingMessage() {
        
        serviceRequest.setBulkCustomerId (extractBulkCustomerId());
        serviceRequest.setRequestPayload (xmlMessage);
        serviceRequest.setRequestDateTime (new LocalDateTime());
        serviceRequest.setBulkReference (extractBulkReference());
        serviceRequest.setRequestType (extractRequestType());
        serviceRequestDao.persist (serviceRequest);
        Storage.setId ((int)serviceRequest.getId ());
    }
    
    /**
     * The message is outgoing and needs to be added to the existing incoming ServiceRequest record.
     */
    private void logOutgoingMessage() {
        serviceRequest = serviceRequestDao.fetch (ServiceRequest.class, Storage.getId ());
        serviceRequest.setBulkReference (extractBulkReference());
        serviceRequest.setResponsePayload (xmlMessage);
        serviceRequest.setResponseDateTime (new LocalDateTime());
        serviceRequestDao.persist (serviceRequest);
    }
    
    /**
     * Retrieve the Request Type from the xmlMessage.
     * @return the request type (for example 'bulkRequest')
     */
    private String extractRequestType() {
        String requestType = "";
        final String body = ":Body>";
        int startPos = xmlMessage.indexOf (body); 
        //the next node contains the request type
        final String content = xmlMessage.substring (startPos+body.length ());
        startPos = content.indexOf (":");
        if(startPos != -1){
            final int endPos = content.indexOf (">");
            requestType = content.substring (startPos, endPos);
        }
        return requestType;        
    }
    
    /**
     * Extract the bulk customer id / sdt customer id (synonymous).
     * <p>
     * Always available as part of a submission
     * </p>
     * @return the bulk customer id
     */
    private String extractBulkCustomerId() {
        return extractValue("sdtCustomerId");
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
     * @return the value of the bulk reference
     */
    private String extractBulkReference() {
        return extractValue("sdtBulkReference");
    }
      
    
    /**
     * Simple method to extract a text value from this ServiceRequest's xmlMessage String.
     * <p>
     * This method does not use the whole framework of generating a DOM and
     * parsing that. Instead it uses simple String functionality
     * to locate and extract a String.
     * </p>
     * @param nodeName The name of an xml node e.g. 'customerId'
     * @return the content of the node or null
     */
    private String extractValue(final String nodeName) {
        String nodeContent = "";
        final int startPos = xmlMessage.indexOf (nodeName);       
        if (startPos != -1) {
            nodeContent = xmlMessage.substring (startPos);
            final int endPos = nodeContent.indexOf ("<");
            nodeContent = nodeContent.substring(1,endPos);
        }
        return nodeContent;
    }
}
/**
 * Used to store the id of the newly created Service Request.
 * @author d195274
 *
 */
final class Storage {
    /**
     * Contains the id.
     */
    private static ThreadLocal <Integer> holder;
    
    /**
     * Private constructor to keep checkstyle happy.
     */
    private Storage(){}
    
    /**
     * Set id.
     * @param id to be saved
     */
    static void setId(final int id){
        holder.set (id);
    }
    /**
     * Retrieve id.
     * @return the id
     */
    static int getId() {
        return holder.get ();
    }
}
