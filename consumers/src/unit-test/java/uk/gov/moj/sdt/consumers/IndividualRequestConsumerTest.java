/* Copyrights and Licenses
 *
 * Copyright (c) 2012-2013 by the Ministry of Justice. All rights reserved.
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

package uk.gov.moj.sdt.consumers;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import uk.gov.moj.sdt.consumers.exception.OutageException;
import uk.gov.moj.sdt.consumers.exception.SoapFaultException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.transformers.api.IConsumerTransformer;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.CreateStatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.CreateStatusType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvrequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvrequestschema.IndividualRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvresponseschema.IndividualResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.targetappinternalendpoint.ITargetAppInternalEndpointPortType;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * Test class for the individual request consumer.
 *
 * @author Manoj Kulkarni
 */
class IndividualRequestConsumerTest extends ConsumerTestBase {

    /**
     * Consumer transformer for submit query.
     */
    @Mock
    protected IConsumerTransformer<IndividualResponseType, IndividualRequestType, IIndividualRequest,
            IIndividualRequest> mockTransformer;

    /**
     * Mock Client instance.
     */
    @Mock
    ITargetAppInternalEndpointPortType mockClient;

    @Mock
    SOAPFault soapFault;

    /**
     * Individual Request Consumer instance of the inner class under test.
     */
    IndividualRequestConsumer individualRequestConsumer;

    private static final String TEST_FINISHED_SUCCESSFULLY = "Test finished successfully";
    private static final String GOT_THE_EXCEPTION_AS_EXPECTED = "Got the exception as expected";

    /**
     * Individual Request instance for testing in the methods.
     */
    private IIndividualRequest individualRequest;

    /**
     * Individual Request type for the individual request.
     */
    private IndividualRequestType individualRequestType;

    /**
     * Method to do any pre-test set-up.
     */
    @BeforeEach
    @Override
    public void setUpLocalTests() {
        MockitoAnnotations.openMocks(this);

        individualRequestConsumer = new IndConsumerGateway();
        individualRequestConsumer.setTransformer(mockTransformer);
        individualRequestConsumer.setRethrowOnFailureToConnect(true);

        individualRequest = this.createIndividualRequest();
        individualRequestType = this.createRequestType(individualRequest);
    }

    @Test
    void getClient() {
        final String targetApplicationCode = "";
        final String serviceType = "";
        final String webServiceEndPoint = "";
        final long connectionTimeOut = 0L;
        final long receiveTimeOut = 0L;
        ITargetAppInternalEndpointPortType portType = individualRequestConsumer.getClient(targetApplicationCode,
                serviceType, webServiceEndPoint, connectionTimeOut, receiveTimeOut);
        assertNotNull(portType);
    }

    @Test
    void getTransformer() {
        assertNotNull(individualRequestConsumer.getTransformer());
    }

    /**
     * Test method for successful processing of individual request.
     */
    @Test
    void processIndividualRequestSuccess() {
        final IndividualResponseType individualResponseType = generateResponse();

        when(mockTransformer.transformDomainToJaxb(individualRequest)).thenReturn(individualRequestType);

        when(mockClient.submitIndividual(individualRequestType)).thenReturn(individualResponseType);

        mockTransformer.transformJaxbToDomain(individualResponseType, individualRequest);

        doAnswer((Answer<Void>) invocation -> {
            ((IndividualRequest) invocation.getArgument(0))
                    .setRequestStatus (IIndividualRequest.IndividualRequestStatus.ACCEPTED.getStatus ());
            // required to be null for a void method
            return null;
        }).when(mockTransformer).transformDomainToJaxb(individualRequest);

        individualRequestConsumer.processIndividualRequest(individualRequest, CONNECTION_TIME_OUT,
                RECEIVE_TIME_OUT);

        verify(mockTransformer).transformDomainToJaxb(any());
        verify(mockClient).submitIndividual(any());

        assertTrue(true, TEST_FINISHED_SUCCESSFULLY);

    }

    /**
     * Test method for processing of individual request outage error.
     */
    @Test
    void processIndividualRequestOutage ()
    {
        when(mockTransformer.transformDomainToJaxb(individualRequest)).thenReturn(individualRequestType);

        final WebServiceException wsException = new WebServiceException ();
        wsException.initCause (new ConnectException("Server down error"));

        when(mockClient.submitIndividual(individualRequestType)).thenThrow (wsException);

        try {
            this.individualRequestConsumer.processIndividualRequest (individualRequest, CONNECTION_TIME_OUT,
                    RECEIVE_TIME_OUT);

            fail("Expecting an OutageException here.");
        } catch (final OutageException toe) {
            assertTrue(true, GOT_THE_EXCEPTION_AS_EXPECTED);
        }

        verify(mockTransformer).transformDomainToJaxb(individualRequest);
        verify(mockClient).submitIndividual(individualRequestType);

        assertTrue(true, TEST_FINISHED_SUCCESSFULLY);

    }

    /**
     * Test method for processing of individual request timeout error.
     */
    @Test
    void processIndividualRequestTimeout() {
        when(mockTransformer.transformDomainToJaxb(individualRequest)).thenReturn(individualRequestType);

        final WebServiceException wsException = new WebServiceException();
        wsException.initCause(new SocketTimeoutException("Timed out waiting for response"));

        when(mockClient.submitIndividual(individualRequestType)).thenThrow(wsException);

        try {
            this.individualRequestConsumer.processIndividualRequest(individualRequest, CONNECTION_TIME_OUT,
                    RECEIVE_TIME_OUT);

            fail("Expecting a Timeout here.");
        } catch (final TimeoutException toe) {
            assertTrue(true, GOT_THE_EXCEPTION_AS_EXPECTED);
        }

        verify(mockTransformer).transformDomainToJaxb(any());
        verify(mockClient).submitIndividual(any());

        assertTrue(true, TEST_FINISHED_SUCCESSFULLY);
    }

    /**
     * Test method for processing of individual request soap fault error.
     *
     * @throws SOAPException exception
     */
    @Test
    void processIndividualRequestSoapFault() throws SOAPException {
        when(mockTransformer.transformDomainToJaxb(individualRequest)).thenReturn(individualRequestType);

        final WebServiceException wsException = new WebServiceException();
        soapFault.setFaultCode("REQ_FAULT");
        soapFault.setFaultString("Invalid request");

        wsException.initCause(new SOAPFaultException(soapFault));

        when(mockClient.submitIndividual(individualRequestType)).thenThrow(wsException);

        try {
            this.individualRequestConsumer.processIndividualRequest(individualRequest, CONNECTION_TIME_OUT,
                    RECEIVE_TIME_OUT);

            fail("Expecting an Soap Fault here.");
        } catch (final SoapFaultException toe) {
            assertTrue(true, GOT_THE_EXCEPTION_AS_EXPECTED);
        }

        verify(mockTransformer).transformDomainToJaxb(any());
        verify(mockClient).submitIndividual(any());

        assertTrue(true, TEST_FINISHED_SUCCESSFULLY);
    }

    /**
     * @param domainObject the individual request domain object.
     * @return the Jaxb individual request type.
     */
    private IndividualRequestType createRequestType(final IIndividualRequest domainObject) {
        final IndividualRequestType requestType = new IndividualRequestType();
        final HeaderType headerType = new HeaderType();
        headerType.setRequestType("testRequestType");
        headerType.setSdtRequestId(domainObject.getSdtRequestReference());
        headerType.setTargetAppCustomerId("TestCust");

        requestType.setHeader(headerType);

        return requestType;
    }

    /**
     * @return the individual response type
     */
    private IndividualResponseType generateResponse() {
        final IndividualResponseType responseType = new IndividualResponseType();

        final uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvresponseschema.HeaderType headerType =
                new uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvresponseschema.HeaderType();
        headerType.setSdtRequestId("Test");

        responseType.setHeader(headerType);

        final CreateStatusType status = new CreateStatusType();
        status.setCode(CreateStatusCodeType.ACCEPTED);

        responseType.setStatus(status);

        return responseType;
    }

    /**
     * Need to extend the consumer class under test for overriding base class methods
     * of the getClient as it is abstract method.
     */
    protected class IndConsumerGateway extends IndividualRequestConsumer
    {
        /**
         * Get the client for the specified target application. If the client is not cached already, a new client
         * connection is created otherwise the already cached client is returned.
         *
         * @param targetApplicationCode the target application code
         * @param serviceType the service type associated with the target application code
         * @param webServiceEndPoint the Web Service End Point URL
         * @param connectionTimeOut the connection time out value
         * @param receiveTimeOut the acknowledgement time out value
         * @return the target application end point port bean i.e. the client interface.
         */
        @Override
        public ITargetAppInternalEndpointPortType getClient(final String targetApplicationCode,
                                                             final String serviceType, final String webServiceEndPoint,
                                                             final long connectionTimeOut, final long receiveTimeOut) {
            return mockClient;
        }
    }

}
