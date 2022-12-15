package uk.gov.moj.sdt.consumers;

import java.math.BigInteger;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import uk.gov.moj.sdt.consumers.api.IIndividualRequestConsumer;
import uk.gov.moj.sdt.consumers.exception.OutageException;
import uk.gov.moj.sdt.consumers.exception.SoapFaultException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.transformers.api.IConsumerTransformer;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.submitqueryrequestschema.SubmitQueryRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.submitqueryresponseschema.SubmitQueryResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.targetappinternalendpoint.ITargetAppInternalEndpointPortType;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * Test class for the consumer gateway.
 *
 * @author Mark Dathorne
 */
@ExtendWith(MockitoExtension.class)
class ConsumerGatewayTest extends ConsumerTestBase {

    /**
     * Consumer transformer for submit query.
     */
    @Mock
    protected IConsumerTransformer<SubmitQueryResponseType, SubmitQueryRequestType, ISubmitQueryRequest, ISubmitQueryRequest> mockTransformer;

    /**
     * Mock Client instance.
     */
    @Mock
    ITargetAppInternalEndpointPortType mockClient;

    /**
     * Mock Individual Request Consumer.
     */
    @Mock
    IIndividualRequestConsumer individualRequestConsumer;

    /**
     * Mock Soap Fault.
     */
    @Mock
    SOAPFault soapFault;

    ConsumerGateway consumerGateway;

    SubQueryConsumer submitQueryConsumer;

    /**
     * Method to do any pre-test set-up.
     */
    @BeforeEach
    @Override
    public void setUpLocalTests() {
        MockitoAnnotations.openMocks(this);

        submitQueryConsumer = new SubQueryConsumer();
        submitQueryConsumer.setTransformer(mockTransformer);

        submitQueryRequest = this.createSubmitQueryRequest();
        submitQueryRequestType = this.createRequestType(submitQueryRequest);

        consumerGateway = new ConsumerGateway();
        consumerGateway.setIndividualRequestConsumer(individualRequestConsumer);
        consumerGateway.setSubmitQueryConsumer(submitQueryConsumer);
    }

    @Test
    void shouldProcessAndGetNotNullIndividualRequest() {
        IIndividualRequest individualRequest = createIndividualRequest();
        consumerGateway.individualRequest(individualRequest,
                CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);
        assertNotNull(consumerGateway.getIndividualRequestConsumer());
    }

    @Test
    void shouldGetNotNullIndividualRequest() {
        IIndividualRequestConsumer iIndividualRequestConsumer = consumerGateway.getIndividualRequestConsumer();
        assertNotNull(iIndividualRequestConsumer);
    }


    /**
     * Test to verify submit query consumer does throw expected exception.
     */
    @Test
    void shouldFailWithTimeoutException() {
        when(mockTransformer.transformDomainToJaxb(submitQueryRequest)).thenReturn(submitQueryRequestType);

        final WebServiceException wsException = new WebServiceException();
        wsException.initCause(new SocketTimeoutException("Timed out waiting for response"));

        when(mockClient.submitQuery(submitQueryRequestType)).thenThrow(wsException);

        TimeoutException timeoutException = assertThrows(TimeoutException.class, () ->
                this.consumerGateway.submitQuery(submitQueryRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT));

        assertEquals("TIMEOUT_ERROR", timeoutException.getErrorCode());
        assertEquals("Read time out error sending [null]", timeoutException.getErrorDescription());
        assertNull(timeoutException.getCause());
    }

    /**
     * Test to verify submit query consumer does throw expected exception.
     *
     * @throws SOAPException exception
     */
    @Test
    void shouldGetSoapFaultExceptionOnSubmitQuery() throws SOAPException {
        when(mockTransformer.transformDomainToJaxb(submitQueryRequest)).thenReturn(submitQueryRequestType);

        final WebServiceException wsException = new WebServiceException();
        soapFault.setFaultCode("REQ_FAULT");
        soapFault.setFaultString("Invalid request");

        wsException.initCause(new SOAPFaultException(soapFault));

        when(mockClient.submitQuery(submitQueryRequestType)).thenThrow(wsException);

        SoapFaultException soapFaultException = assertThrows(SoapFaultException.class, () -> {
            this.consumerGateway.submitQuery(submitQueryRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);
        });

        assertEquals("SOAP_FAULT", soapFaultException.getErrorCode());
        assertNull(soapFaultException.getErrorDescription());
        assertNull(soapFaultException.getCause());
    }

    /**
     * Test to verify submit query consumer does throw expected exception.
     */
    @Test
    void shouldGetOutageExceptionOnSubmitQuery() {
        when(mockTransformer.transformDomainToJaxb(submitQueryRequest)).thenReturn(submitQueryRequestType);

        final WebServiceException wsException = new WebServiceException();
        wsException.initCause(new ConnectException());

        when(mockClient.submitQuery(submitQueryRequestType)).thenThrow(wsException);

        OutageException outageException = assertThrows(OutageException.class, () ->
                this.consumerGateway.submitQuery(submitQueryRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT));

        assertEquals("OUTAGE_ERROR", outageException.getErrorCode());
        assertNull(outageException.getErrorDescription());
    }

    /**
     * Test method for successful processing of submit query.
     */
    @Test
    void shouldSuccesssfullySubmitQuery() {
        final SubmitQueryResponseType submitQueryResponseType = new SubmitQueryResponseType();

        when(mockTransformer.transformDomainToJaxb(submitQueryRequest)).thenReturn(submitQueryRequestType);
        when(mockClient.submitQuery(submitQueryRequestType)).thenReturn(submitQueryResponseType);
        mockTransformer.transformJaxbToDomain(submitQueryResponseType, submitQueryRequest);

        doAnswer ((Answer<Void>) invocation -> {
            ((SubmitQueryResponseType) invocation.getArgument(0)).setResultCount(BigInteger.valueOf(1));
            final StatusType statusType = new StatusType();
            statusType.setCode(StatusCodeType.OK);
            ((SubmitQueryResponseType) invocation.getArgument(0)).setStatus (statusType);
            // required to be null for a void method
            return null;
        }).when(mockTransformer).transformJaxbToDomain(submitQueryResponseType, submitQueryRequest);

        this.consumerGateway.submitQuery(submitQueryRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        verify(mockTransformer).transformDomainToJaxb(any());
        verify(mockClient).submitQuery(any());

        assertEquals(submitQueryResponseType.getStatus().getCode().value(),
                submitQueryRequest.getStatus(), "Status code is not equal.");
        assertEquals(submitQueryResponseType.getResultCount().intValue(),
                submitQueryRequest.getResultCount(), "Result count is not equal.");
    }

    /**
     * Need to extend the consumer class under test for overriding base class methods
     * of the getClient as it is abstract method.
     */
    protected class SubQueryConsumer extends SubmitQueryConsumer
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
                                                             final long connectionTimeOut, final long receiveTimeOut) {
            return mockClient;
        }
    }

}
