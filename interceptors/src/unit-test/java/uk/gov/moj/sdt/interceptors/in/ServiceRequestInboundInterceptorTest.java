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

import java.io.InputStream;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.message.ExchangeImpl;
import org.apache.cxf.message.MessageImpl;
import org.easymock.EasyMock;
import org.junit.Test;

import uk.gov.moj.sdt.dao.GenericDao;
import uk.gov.moj.sdt.domain.ServiceRequest;
import uk.gov.moj.sdt.utils.SdtUnitTestBase;
/**
 * Test class.
 * @author d195274
 *
 */
public class ServiceRequestInboundInterceptorTest extends SdtUnitTestBase
{

    /**
     * Constructor.
     * @param testName ronseal
     */
    public ServiceRequestInboundInterceptorTest (final String testName)
    {
        super (testName);
    }

    /**
     * Build a mocked dao.
     * 
     * @param serviceRequest
     *            possible superfluous object.
     * @return the mocked dao.
     */
    private GenericDao getMockedGenericDao(final ServiceRequest serviceRequest) {
        final GenericDao mockServiceRequestDao = EasyMock
                .createNiceMock(GenericDao.class);
        mockServiceRequestDao.persist(serviceRequest);
        EasyMock.expectLastCall();
        EasyMock.replay(mockServiceRequestDao);
        return mockServiceRequestDao;
    }

    

    /**
     * Builds a dummy soap message.
     * 
     * @param resourcePath
     *            the path to the xml file containing the soap message.
     * @return a soap message
     */
    private SoapMessage getDummySoapMessage(final String resourcePath) {
        final SoapMessage soapMessage = new SoapMessage(new MessageImpl());
        soapMessage.setExchange(new ExchangeImpl());
        soapMessage.setContent(InputStream.class,
                ServiceRequestInboundInterceptorTest.class
                        .getResourceAsStream(resourcePath));
        return soapMessage;
    }

    /**
     * Test that the process correctly works via mocked out extensions.
     */
    @Test
    public void testHandleMessage() {
        try {
            
            final ServiceRequestInboundInterceptor sRII = new ServiceRequestInboundInterceptor();
            sRII.setServiceRequestDao(getMockedGenericDao(new ServiceRequest()));
            sRII.handleMessage (getDummySoapMessage ("inRequest.xml"));
//            final IServiceRequest serviceRequest = sRII..getServiceRequest();
//            final Long iDField = (Long) getAccesibleField(ServiceRequest.class, "id",
//                    Long.class, serviceRequest);
//            assertNull ("id should be null",iDField);
//            final LocalDateTime requestDateTimeField = (LocalDateTime) getAccesibleField(
//                    ServiceRequest.class, "requestDateTime", LocalDateTime.class,
//                    serviceRequest);
//            assertTrue("requestDateTime should not be null",
//                    null != requestDateTimeField);
//            final LocalDateTime responseDateTimeField = (LocalDateTime) getAccesibleField(
//                    ServiceRequest.class, "responseDateTime", LocalDateTime.class,
//                    serviceRequest);
//            assertTrue("responseDateTime should be null",
//                    null == responseDateTimeField);
//            final String customerIdField = (String) getAccesibleField(
//                    ServiceRequest.class, "bulkCustomerId", String.class,
//                    serviceRequest);
//            assertTrue("bulkCustomerId should be 1234",
//                    "1234".equals(customerIdField));
//            final String sdtRequestIdField = (String) getAccesibleField(
//                    ServiceRequest.class, "bulkReference", String.class,
//                    serviceRequest);
//            assertTrue("bulkReference should be null",
//                    null == sdtRequestIdField);
//            final String requestTypeField = (String) getAccesibleField(
//                    ServiceRequest.class, "requestType", String.class,
//                    serviceRequest);
//            assertTrue("requestType should be Claim",
//                    "Claim".equals(requestTypeField));
//            final String requestPayloadField = (String) getAccesibleField(
//                    ServiceRequest.class, "requestPayload", String.class,
//                    serviceRequest);
//            assertTrue("requestPayload should not be null",
//                    null != requestPayloadField);
//            final String responsePayloadField = (String) getAccesibleField(
//                    ServiceRequest.class, "responsePayload", String.class,
//                    serviceRequest);
//            assertTrue("responsePayload should be null",
//                    null == responsePayloadField);
//            
            
        } catch (final SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
}
