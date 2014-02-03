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
 * $Id$
 * $LastChangedRevision$
 * $LastChangedDate$
 * $LastChangedBy$ */

package uk.gov.moj.sdt.consumers;

import java.math.BigInteger;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.gov.moj.sdt.consumers.exception.OutageException;
import uk.gov.moj.sdt.consumers.exception.SoapFaultException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkCustomerApplication;
import uk.gov.moj.sdt.domain.ServiceRouting;
import uk.gov.moj.sdt.domain.ServiceType;
import uk.gov.moj.sdt.domain.SubmitQueryRequest;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkCustomerApplication;
import uk.gov.moj.sdt.domain.api.IServiceRouting;
import uk.gov.moj.sdt.domain.api.IServiceType;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.transformers.api.IConsumerTransformer;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.submitqueryrequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.submitqueryrequestschema.SubmitQueryRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.submitqueryresponseschema.SubmitQueryResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.targetappinternalendpoint.ITargetAppInternalEndpointPortType;

/**
 * Test class for the submit query consumer.
 * 
 * @author Amit Nigam
 * 
 */
public class SubmitQueryConsumerTest
{
    /**
     * Connection time out constant.
     */
    private static final long CONNECTION_TIME_OUT = 30000;

    /**
     * Received time out constant.
     */
    private static final long RECEIVE_TIME_OUT = 60000;

    /**
     * Consumer transformer for submit query.
     */
    // CHECKSTYLE:OFF
    private IConsumerTransformer<SubmitQueryResponseType, SubmitQueryRequestType, ISubmitQueryRequest, ISubmitQueryRequest> mockTransformer;
    // CHECKSTYLE:ON

    /**
     * Submit Query Consumer instance of the inner class under test.
     */
    private SubQueryConsumer submitQueryConsumer;

    /**
     * Mock Client instance.
     */
    private ITargetAppInternalEndpointPortType mockClient;

    /**
     * Submit query request instance for testing in the methods.
     */
    private ISubmitQueryRequest submitQueryRequest;

    /**
     * Submit query Request type for the query request.
     */
    private SubmitQueryRequestType submitQueryRequestType;

    /**
     * Method to do any pre-test set-up.
     */
    @SuppressWarnings ("unchecked")
    @Before
    public void setUp ()
    {
        mockTransformer = EasyMock.createMock (IConsumerTransformer.class);
        mockClient = EasyMock.createMock (ITargetAppInternalEndpointPortType.class);
        submitQueryConsumer = new SubQueryConsumer ();
        submitQueryConsumer.setTransformer (mockTransformer);
        // submitQueryConsumer.setRethrowOnFailureToConnect (true);

        submitQueryRequest = this.createSubmitQueryRequest ();
        submitQueryRequestType = this.createRequestType (submitQueryRequest);
    }

    /**
     * Test to verify submit query consumer does throw expected exception.
     */
    @Test (expected = TimeoutException.class)
    public void testSubmitQueryRequestTimeout ()
    {
        EasyMock.expect (mockTransformer.transformDomainToJaxb (submitQueryRequest)).andReturn (submitQueryRequestType);

        final WebServiceException wsException = new WebServiceException ();
        wsException.initCause (new SocketTimeoutException ("Timed out waiting for response"));

        EasyMock.expect (mockClient.submitQuery (submitQueryRequestType)).andThrow (wsException);

        EasyMock.replay (mockTransformer);
        EasyMock.replay (mockClient);

        this.submitQueryConsumer.processSubmitQuery (submitQueryRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);
    }

    /**
     * Test to verify submit query consumer does throw expected exception.
     * 
     * @throws SOAPException exception
     */
    @Test (expected = SoapFaultException.class)
    public void testSubmitQueryRequestSoapFault () throws SOAPException
    {
        EasyMock.expect (mockTransformer.transformDomainToJaxb (submitQueryRequest)).andReturn (submitQueryRequestType);

        final WebServiceException wsException = new WebServiceException ();
        final SOAPFault fault = EasyMock.createMock (SOAPFault.class);
        fault.setFaultCode ("REQ_FAULT");
        fault.setFaultString ("Invalid request");

        wsException.initCause (new SOAPFaultException (fault));

        EasyMock.expect (mockClient.submitQuery (submitQueryRequestType)).andThrow (wsException);

        EasyMock.replay (mockTransformer);
        EasyMock.replay (mockClient);

        this.submitQueryConsumer.processSubmitQuery (submitQueryRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);
    }

    /**
     * Test to verify submit query consumer does throw expected exception.
     * 
     * @throws SOAPException exception
     */
    @Test (expected = OutageException.class)
    public void testSubmitQueryRequestOutage () throws SOAPException
    {
        EasyMock.expect (mockTransformer.transformDomainToJaxb (submitQueryRequest)).andReturn (submitQueryRequestType);

        final WebServiceException wsException = new WebServiceException ();
        wsException.initCause (new ConnectException ());

        EasyMock.expect (mockClient.submitQuery (submitQueryRequestType)).andThrow (wsException);

        EasyMock.replay (mockTransformer);
        EasyMock.replay (mockClient);

        this.submitQueryConsumer.processSubmitQuery (submitQueryRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);
    }

    /**
     * Test method for successful processing of submit query.
     */
    @Test
    public void testSubmitQuerySuccess ()
    {
        final SubmitQueryResponseType submitQueryResponseType = new SubmitQueryResponseType ();

        EasyMock.expect (mockTransformer.transformDomainToJaxb (submitQueryRequest)).andReturn (submitQueryRequestType);
        EasyMock.expect (mockClient.submitQuery (submitQueryRequestType)).andReturn (submitQueryResponseType);
        mockTransformer.transformJaxbToDomain (submitQueryResponseType, submitQueryRequest);

        EasyMock.expectLastCall ().andAnswer (new IAnswer<Object> ()
        {
            @Override
            public Object answer () throws Throwable
            {
                ((SubmitQueryResponseType) EasyMock.getCurrentArguments ()[0]).setResultCount (new BigInteger ("1"));
                final StatusType statusType = new StatusType ();
                statusType.setCode (StatusCodeType.OK);
                ((SubmitQueryResponseType) EasyMock.getCurrentArguments ()[0]).setStatus (statusType);
                // required to be null for a void method
                return null;
            }
        });

        EasyMock.replay (mockTransformer);
        EasyMock.replay (mockClient);

        this.submitQueryConsumer.processSubmitQuery (submitQueryRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        EasyMock.verify (mockTransformer);
        EasyMock.verify (mockClient);

        Assert.assertEquals ("Status code is not equal.", submitQueryResponseType.getStatus ().getCode ().value (),
                submitQueryRequest.getStatus ());
        Assert.assertEquals ("Result count is not equal.", submitQueryResponseType.getResultCount ().intValue (),
                submitQueryRequest.getResultCount ());

    }

    /**
     * 
     * @param domainObject the submit query domain object.
     * @return the Jaxb submit query request type.
     */
    private SubmitQueryRequestType createRequestType (final ISubmitQueryRequest domainObject)
    {
        final SubmitQueryRequestType requestType = new SubmitQueryRequestType ();
        final HeaderType headerType = new HeaderType ();
        headerType.setTargetAppCustomerId ("TestCust");
        headerType.setCriteriaType ("TEST_CRITERIA");
        requestType.setHeader (headerType);

        return requestType;
    }

    /**
     * 
     * @return submit query request domain object
     */
    private ISubmitQueryRequest createSubmitQueryRequest ()
    {
        // final IBulkSubmission bulkSubmission = new BulkSubmission ();
        final IBulkCustomer bulkCustomer = new BulkCustomer ();
        bulkCustomer.setId (1L);
        bulkCustomer.setSdtCustomerId (10L);

        final ITargetApplication targetApp = new TargetApplication ();

        targetApp.setId (1L);
        targetApp.setTargetApplicationCode ("MCOL");
        targetApp.setTargetApplicationName ("TEST_TargetApp");
        final Set<IServiceRouting> serviceRoutings = new HashSet<IServiceRouting> ();

        final ServiceRouting serviceRouting = new ServiceRouting ();
        serviceRouting.setId (1L);
        serviceRouting.setWebServiceEndpoint ("MCOL_END_POINT");

        final IServiceType serviceType = new ServiceType ();
        serviceType.setId (1L);
        serviceType.setName (IServiceType.ServiceTypeName.SUBMIT_QUERY.name ());
        serviceType.setDescription ("RequestTestDesc1");
        serviceType.setStatus ("RequestTestStatus");

        serviceRouting.setServiceType (serviceType);
        serviceRoutings.add (serviceRouting);
        targetApp.setServiceRoutings (serviceRoutings);

        final Set<IBulkCustomerApplication> bulkCustomerApplications = new HashSet<IBulkCustomerApplication> ();
        final BulkCustomerApplication bulkCustomerApplication = new BulkCustomerApplication ();
        bulkCustomerApplication.setBulkCustomer (bulkCustomer);
        bulkCustomerApplication.setTargetApplication (targetApp);
        bulkCustomerApplications.add (bulkCustomerApplication);
        bulkCustomer.setBulkCustomerApplications (bulkCustomerApplications);

        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest ();
        submitQueryRequest.setBulkCustomer (bulkCustomer);
        submitQueryRequest.setTargetApplication (targetApp);
        submitQueryRequest.setResultCount (1);
        return submitQueryRequest;

    }

    /**
     * Need to extend the consumer class under test for overriding base class methods
     * of the getClient as it is abstract method.
     */
    private class SubQueryConsumer extends SubmitQueryConsumer
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
        public ITargetAppInternalEndpointPortType getClient (final String targetApplicationCode,
                                                             final String serviceType, final String webServiceEndPoint,
                                                             final long connectionTimeOut, final long receiveTimeOut)
        {
            return mockClient;
        }
    }

}
