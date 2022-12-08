package uk.gov.moj.sdt.consumers;

import java.math.BigInteger;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import uk.gov.moj.sdt.consumers.api.IIndividualRequestConsumer;
import uk.gov.moj.sdt.consumers.exception.OutageException;
import uk.gov.moj.sdt.consumers.exception.SoapFaultException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.submitqueryresponseschema.SubmitQueryResponseType;

import javax.xml.soap.SOAPException;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Test class for the consumer gateway.
 *
 * @author Mark Dathorne
 */
@ExtendWith(MockitoExtension.class)
class ConsumerGatewayTest extends SubmitQueryConsumerTest {

    @Mock
    IIndividualRequestConsumer individualRequestConsumer;

    ConsumerGateway consumerGateway;

    /**
     * Method to do any pre-test set-up.
     */
    @BeforeEach
    @Override
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        submitQueryConsumer = new SubQueryConsumer();
        submitQueryConsumer.setTransformer(mockTransformer);
        // submitQueryConsumer.setRethrowOnFailureToConnect (true);

        submitQueryRequest = this.createSubmitQueryRequest();
        submitQueryRequestType = this.createRequestType(submitQueryRequest);

        consumerGateway = new ConsumerGateway();
        consumerGateway.setIndividualRequestConsumer(individualRequestConsumer);
        consumerGateway.setSubmitQueryConsumer(submitQueryConsumer);
    }

    /**
     * Test to verify submit query consumer does throw expected exception.
     */
    @Test
    @Override
    void testSubmitQueryRequestTimeout() {
        when(mockTransformer.transformDomainToJaxb(submitQueryRequest)).thenReturn(submitQueryRequestType);

        final WebServiceException wsException = new WebServiceException();
        wsException.initCause(new SocketTimeoutException("Timed out waiting for response"));

        when(mockClient.submitQuery(submitQueryRequestType)).thenThrow(wsException);

        TimeoutException timeoutException = assertThrows(TimeoutException.class, () ->
                this.consumerGateway.submitQuery(submitQueryRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT));

        assertEquals(timeoutException.getErrorCode(), "TIMEOUT_ERROR");
        assertEquals(timeoutException.getErrorDescription(), "Read time out error sending [null]");
        assertEquals(timeoutException.getCause(), null);
    }

    /**
     * Test to verify submit query consumer does throw expected exception.
     *
     * @throws SOAPException exception
     */
    @Test
    @Override
    void testSubmitQueryRequestSoapFault() throws SOAPException {
        when(mockTransformer.transformDomainToJaxb(submitQueryRequest)).thenReturn(submitQueryRequestType);

        final WebServiceException wsException = new WebServiceException();
        soapFault.setFaultCode("REQ_FAULT");
        soapFault.setFaultString("Invalid request");

        wsException.initCause(new SOAPFaultException(soapFault));

        when(mockClient.submitQuery(submitQueryRequestType)).thenThrow(wsException);

        SoapFaultException soapFaultException = assertThrows(SoapFaultException.class, () -> {
            this.consumerGateway.submitQuery(submitQueryRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);
            assertEquals(this.soapFault.getFaultCode(),"REQ_FAULT");
            assertEquals(this.soapFault.getFaultString(), "Invalid request");
        });

        assertEquals(soapFaultException.getErrorCode(), "SOAP_FAULT");
        assertEquals(soapFaultException.getErrorDescription(), null);
        assertEquals(soapFaultException.getCause(), null);
    }

    /**
     * Test to verify submit query consumer does throw expected exception.
     */
    @Test
    @Override
    void testSubmitQueryRequestOutage() {
        when(mockTransformer.transformDomainToJaxb(submitQueryRequest)).thenReturn(submitQueryRequestType);

        final WebServiceException wsException = new WebServiceException();
        wsException.initCause(new ConnectException());

        when(mockClient.submitQuery(submitQueryRequestType)).thenThrow(wsException);

        OutageException outageException = assertThrows(OutageException.class, () ->
                this.consumerGateway.submitQuery(submitQueryRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT));

        assertEquals(outageException.getErrorCode(), "OUTAGE_ERROR");
        assertEquals(outageException.getErrorDescription(), null);
    }

    /**
     * Test method for successful processing of submit query.
     */
    @Test
    @Override
    void testSubmitQuerySuccess() {
        final SubmitQueryResponseType submitQueryResponseType = new SubmitQueryResponseType();

        when(mockTransformer.transformDomainToJaxb(submitQueryRequest)).thenReturn(submitQueryRequestType);
        when(mockClient.submitQuery(submitQueryRequestType)).thenReturn(submitQueryResponseType);
        mockTransformer.transformJaxbToDomain(submitQueryResponseType, submitQueryRequest);

        doAnswer (new Answer<Void>()
        {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                ((SubmitQueryResponseType) invocation.getArgument(0)).setResultCount(BigInteger.valueOf(1));
                final StatusType statusType = new StatusType ();
                statusType.setCode (StatusCodeType.OK);
                ((SubmitQueryResponseType) invocation.getArgument(0)).setStatus (statusType);
                // required to be null for a void method
                return null;
            }
        }).when(mockTransformer).transformJaxbToDomain(submitQueryResponseType, submitQueryRequest);

        this.consumerGateway.submitQuery(submitQueryRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        verify(mockTransformer).transformDomainToJaxb(any());
        verify(mockClient).submitQuery(any());

        Assertions.assertEquals(submitQueryResponseType.getStatus().getCode().value(),
                submitQueryRequest.getStatus(), "Status code is not equal.");
        Assertions.assertEquals(submitQueryResponseType.getResultCount().intValue(),
                submitQueryRequest.getResultCount(), "Result count is not equal.");

    }

}
