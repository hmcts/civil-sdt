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

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import uk.gov.moj.sdt.consumers.exception.SoapFaultException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.ServiceRouting;
import uk.gov.moj.sdt.domain.ServiceType;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.IServiceRouting;
import uk.gov.moj.sdt.domain.api.IServiceType;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
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

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * Test class for the individual request consumer.
 *
 * @author Manoj Kulkarni
 */
class IndividualRequestConsumerTest {

    /**
     * Consumer transformer for individual request.
     */
    @Mock
    private IConsumerTransformer<IndividualResponseType, IndividualRequestType, IIndividualRequest, IIndividualRequest> mockTransformer;

    /**
     * Mock Client instance.
     */
    @Mock
    private ITargetAppInternalEndpointPortType mockClient;

    @Mock
    private SOAPFault soapFault;

    /**
     * Individual Request Consumer instance of the inner class under test.
     */
    private IndRequestConsumer individualRequestConsumer;

    /**
     * Connection time out constant.
     */
    private static final long CONNECTION_TIME_OUT = 30000;

    /**
     * Received time out constant.
     */
    private static final long RECEIVE_TIME_OUT = 60000;

    private static final String TEST_FINISHED_SUCCESSFULLY = "Test finished successfully";

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
    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        individualRequestConsumer = new IndRequestConsumer();
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
        Assertions.assertTrue(null != portType);
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
        }).when(mockTransformer).transformDomainToJaxb(any());

        individualRequestConsumer.processIndividualRequest(individualRequest, CONNECTION_TIME_OUT,
                RECEIVE_TIME_OUT);

        verify(mockTransformer).transformDomainToJaxb(any());
        verify(mockClient).submitIndividual(any());

        Assertions.assertTrue(true, TEST_FINISHED_SUCCESSFULLY);

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

            Assertions.fail("Expecting a Timeout here.");
        } catch (final TimeoutException toe) {
            Assertions.assertTrue(true, "Got the exception as expected");
        }

        verify(mockTransformer).transformDomainToJaxb(any());
        verify(mockClient).submitIndividual(any());

        Assertions.assertTrue(true, TEST_FINISHED_SUCCESSFULLY);

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

            Assertions.fail("Expecting an Soap Fault here.");
        } catch (final SoapFaultException toe) {
            Assertions.assertTrue(true, "Got the exception as expected");
        }

        verify(mockTransformer).transformDomainToJaxb(any());
        verify(mockClient).submitIndividual(any());

        Assertions.assertTrue(true, TEST_FINISHED_SUCCESSFULLY);

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
     * @return individual request domain object
     */
    private IIndividualRequest createIndividualRequest() {
        final IBulkSubmission bulkSubmission = new BulkSubmission();
        final IBulkCustomer bulkCustomer = new BulkCustomer();
        final ITargetApplication targetApp = new TargetApplication();

        targetApp.setId(1L);
        targetApp.setTargetApplicationCode("MCOL");
        targetApp.setTargetApplicationName("TEST_TargetApp");
        final Set<IServiceRouting> serviceRoutings = new HashSet<>();

        final ServiceRouting serviceRouting = new ServiceRouting();
        serviceRouting.setId(1L);
        serviceRouting.setWebServiceEndpoint("MCOL_END_POINT");

        final IServiceType serviceType = new ServiceType();
        serviceType.setId(1L);
        serviceType.setName(IServiceType.ServiceTypeName.SUBMIT_INDIVIDUAL.name());
        serviceType.setDescription("RequestTestDesc1");
        serviceType.setStatus("RequestTestStatus");

        serviceRouting.setServiceType(serviceType);
        serviceRouting.setTargetApplication(targetApp);

        serviceRoutings.add(serviceRouting);

        targetApp.setServiceRoutings(serviceRoutings);

        bulkSubmission.setTargetApplication(targetApp);

        bulkCustomer.setId(1L);
        bulkCustomer.setSdtCustomerId(10L);

        bulkSubmission.setBulkCustomer(bulkCustomer);
        bulkSubmission.setCustomerReference("TEST_CUST_REF");
        bulkSubmission.setId(1L);
        bulkSubmission.setNumberOfRequest(1);

        final IIndividualRequest iindividualRequest = new IndividualRequest();
        iindividualRequest.setSdtRequestReference("Test");

        final List<IIndividualRequest> requests = new ArrayList<>();
        requests.add(iindividualRequest);

        bulkSubmission.setIndividualRequests(requests);

        iindividualRequest.setBulkSubmission(bulkSubmission);

        return iindividualRequest;

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
    private class IndRequestConsumer extends IndividualRequestConsumer {
        /**
         * Get the client for the specified target application. If the client is not cached already, a new client
         * connection is created otherwise the already cached client is returned.
         *
         * @param targetApplicationCode the target application code
         * @param serviceType           the service type associated with the target application code
         * @param webServiceEndPoint    the Web Service End Point URL
         * @param connectionTimeOut     the connection time out value
         * @param receiveTimeOut        the acknowledgement time out value
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
