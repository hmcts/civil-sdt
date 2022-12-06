package uk.gov.moj.sdt.consumers;

import java.net.SocketTimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.consumers.api.IIndividualRequestConsumer;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;

import javax.xml.ws.WebServiceException;

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

    @Mock
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

        consumerGateway.setIndividualRequestConsumer(individualRequestConsumer);
        consumerGateway.setSubmitQueryConsumer(submitQueryConsumer);
    }


    /**
     * Test to verify submit query consumer does throw expected exception.
     */
    @Test
    @Disabled
    @Override
    void testSubmitQueryRequestTimeout() {
        when(mockTransformer.transformDomainToJaxb(submitQueryRequest)).thenReturn(submitQueryRequestType);

        final WebServiceException wsException = new WebServiceException();
        wsException.initCause(new SocketTimeoutException("Timed out waiting for response"));

        doThrow(wsException).when(consumerGateway)
                .submitQuery(submitQueryRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT);

        assertThrows(TimeoutException.class, () ->
                this.consumerGateway.submitQuery(submitQueryRequest, CONNECTION_TIME_OUT, RECEIVE_TIME_OUT));
    }

}
