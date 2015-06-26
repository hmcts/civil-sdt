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
/**
 * Test this class handles faults correctlty.
 */
package uk.gov.moj.sdt.interceptors.out;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.ExchangeImpl;
import org.apache.cxf.message.MessageImpl;
import org.easymock.EasyMock;
import org.junit.Test;

import uk.gov.moj.sdt.dao.GenericDao;
import uk.gov.moj.sdt.domain.ServiceRequest;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.SdtContext;

/**
 * Test that faults are correctly intercepted.
 * @author d195274
 *
 */
public class FaultOutboundInterceptorTest extends AbstractSdtUnitTestBase
{
    /**
     * Error message returned when a fault occurs.
     */
    private final String errorMessage = "A SDT system component error " +
            "has occurred. Please contact the SDT support team for assistance";
    /**
     * Builds a dummy soap message.
     * 
     *            the path to the xml file containing the soap message.
     * @return a soap message
     */
    private SoapMessage getDummySoapMessageWithFault() {
        final SoapMessage soapMessage = new SoapMessage(new MessageImpl());
        soapMessage.setExchange(new ExchangeImpl());
        final Fault fault = new Fault(new RuntimeException (errorMessage
                ));
        soapMessage.setContent (Exception.class, fault);
        return soapMessage;
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
        EasyMock.expect (mockServiceRequestDao.fetch (ServiceRequest.class, 1L)).andReturn (serviceRequest);        
        mockServiceRequestDao.persist(serviceRequest);
        EasyMock.expectLastCall();
        EasyMock.replay(mockServiceRequestDao);
        return mockServiceRequestDao;
    }
    /**
     * Test method for 
     * {@link uk.gov.moj.sdt.interceptors.out.FaultOutboundInterceptor
     * #handleMessage(org.apache.cxf.binding.soap.SoapMessage)}.
     */
    @Test
    public void testHandleMessage ()
    {
        SdtContext.getContext ().setServiceRequestId (1L);
        final SoapMessage soapMessage = getDummySoapMessageWithFault();
        final FaultOutboundInterceptor faultOutboundInterceptor = new FaultOutboundInterceptor();
        final ServiceRequest serviceRequest = new ServiceRequest();
        faultOutboundInterceptor.setServiceRequestDao(getMockedGenericDao(serviceRequest));
        assertNull (serviceRequest.getResponseDateTime ());
        assertNull(serviceRequest.getResponsePayload ());
        faultOutboundInterceptor.handleMessage (soapMessage);
        assertNotNull (serviceRequest.getResponseDateTime ());
        assertTrue(serviceRequest.getResponsePayload ().contains (errorMessage));
        
    }

}
