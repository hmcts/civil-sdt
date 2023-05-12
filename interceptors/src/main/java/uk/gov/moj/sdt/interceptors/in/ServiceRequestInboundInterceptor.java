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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.moj.sdt.dao.ServiceRequestDao;
import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.domain.ServiceRequest;
import uk.gov.moj.sdt.domain.api.IServiceRequest;
import uk.gov.moj.sdt.interceptors.AbstractServiceRequest;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.ServerHostName;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to intercept incoming messages and log them to the database.
 *
 * <p>
 * Generates <code>ServiceDomain</code> domain objects and by means of a Thread Local <SdtContext> instance keeps track
 * of the persisted entity.
 * <p>
 * Outbound message interceptors in the same thread can then access the persisted entity to update as required.
 * </p>
 *
 * @author d195274
 */
@Component("ServiceRequestInboundInterceptor")
public class ServiceRequestInboundInterceptor extends AbstractServiceRequest {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRequestInboundInterceptor.class);

    /**
     * Create instance of {@link PerformanceLoggerInboundInterceptor}.
     */
    @Autowired
    public ServiceRequestInboundInterceptor(ServiceRequestDao serviceRequestDao) {
        super(Phase.RECEIVE);
        setServiceRequestDao(serviceRequestDao);
        addAfter(XmlInboundInterceptor.class.getName());
    }

    /**
     * Create instance of {@link PerformanceLoggerInboundInterceptor}.
     *
     * @param phase phase of the CXF interceptor chain in which this interceptor should run.
     */
    public ServiceRequestInboundInterceptor(final String phase) {
        super(phase);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMessage(final SoapMessage soapMessage) throws Fault {
        LOGGER.debug("ServiceRequestInboundInterceptor handle incoming message being processed");

        logMessage();
    }

    /**
     * Log the message to the persistence target.
     * <p>
     * Incoming messages, once persisted, will generate an id which is stored on a ThreadLocal class to make it
     * available when the outbound service request interceptor runs.
     * </p>
     */
    private void logMessage() {
        final IGenericDao serviceRequestDao = this.getServiceRequestDao();

        // Get the raw XML.
        final String rawXml = SdtContext.getContext().getRawInXml();

        // Prepare log message for Hibernate.
        final IServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setBulkCustomerId(extractBulkCustomerId(rawXml));
        serviceRequest.setRequestPayload(rawXml.getBytes());
        serviceRequest.setRequestDateTime(LocalDateTime.now());
        serviceRequest.setRequestType(extractRequestType(rawXml));

        // Get the server Host Name
        final String hostName = ServerHostName.getHostName();
        serviceRequest.setServerHostName(hostName);

        serviceRequestDao.persist(serviceRequest);

        // Retrieve the id assigned in by Hibernate and store it for the outbound service interceptor to use later.
        final Long serviceRequestID = serviceRequest.getId();
        SdtContext.getContext().setServiceRequestId(serviceRequestID);
    }

    /**
     * Retrieve the Request Type from the inbound xmlMessage. Assumes inbound message has already been setup by the
     * XmlInboundInterceptor.
     *
     * @param xml raw XML from which to extract bulk customer id.
     * @return the request type (for example 'bulkRequest')
     */
    private String extractRequestType(final String xml) {
        return extractNodeAttributeValue("request", "requestType", xml);
    }

    /**
     * Extract the bulk customer id (sdt customer id - synonymous). Assumes inbound message has already been setup by
     * the XmlInboundInterceptor.
     * <p>
     * Always available as part of a submission
     * </p>
     *
     * @param xml raw XML from which to extract bulk customer id.
     * @return the bulk customer id
     */
    private String extractBulkCustomerId(final String xml) {
        return extractNodeValue("sdtCustomerId", xml);
    }

    /**
     * Method to extract a text value belonging to a specified node from a string of xml.
     * <p>
     * This method does not use the whole framework of generating a DOM and parsing that. Instead it uses a regular
     * expression to locate and extract a String.
     * </p>
     *
     * @param nodeName the name of an xml node from which to extract value.
     * @param xml      raw XML from which to extract bulk customer id.
     * @return the content of the node or null
     */
    private String extractNodeValue(final String nodeName, final String xml) {
        String nodeValue = "";

        // Find given tag and extract its value.
        //
        // Search for:
        // optional namespace and given start tag name
        // any attributes before the end of the start tag
        // end of start tag
        // tag value
        // given end tag
        // an embedded default namespace
        //
        // Capture:
        // tag value
        final Pattern pattern =
                Pattern.compile("<[\\S&&[^>/]]*?" + nodeName + "[^>]*?>(.*?)</[\\S&&[^>/]]*?" + nodeName);
        final Matcher matcher = pattern.matcher(xml);

        if (matcher.find()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Found matching group[{}]", matcher.group());
            }

            // Copy the match.
            nodeValue = matcher.group(1);
        }

        return nodeValue;
    }

    /**
     * Method to extract an attribute value belonging to a specified node from a string of xml.
     * <p>
     * This method does not use the whole framework of generating a DOM and parsing that. Instead it uses a regular
     * expression to locate and extract a String.
     * </p>
     *
     * @param nodeName      the name of an xml node from which to extract value.
     * @param attributeName the name of the attribute from which to extract value.
     * @param xml           raw XML from which to extract bulk customer id.
     * @return the content of the node or null
     */
    private String extractNodeAttributeValue(final String nodeName, final String attributeName, final String xml) {
        String attributeValue = "";

        // Find given node and extract ivalue of named attribute.
        //
        // Search for:
        // optional namespace and given start tag name
        // any attributes before the end of the start tag
        // end of start tag
        // tag value
        // given end tag
        // an embedded default namespace
        //
        // Capture:
        // tag value
        final Pattern pattern =
                Pattern.compile("<[\\S&&[^>/]]*?" + nodeName + ".*?" + attributeName + "=[\"\']([^>\"\']*?)[\"\']");
        final Matcher matcher = pattern.matcher(xml);

        if (matcher.find()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Found matching group[{}]", matcher.group());
            }

            // Copy the match.
            attributeValue = matcher.group(1);
        }

        return attributeValue;
    }
}
