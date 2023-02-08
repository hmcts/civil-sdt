package uk.gov.moj.sdt.services.messaging;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.services.TargetApplicationSubmissionService;

import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Test class for Message reader implementation.
 *
 * @author Manoj Kulkarni
 */
@ExtendWith(MockitoExtension.class)
public class IndividualRequestMdbTest extends AbstractSdtUnitTestBase {
    /**
     * Mock service for the message processing.
     */
    @Mock
    private TargetApplicationSubmissionService mockTargetSubmissionService;

    /**
     * Individual Request Mdb Test instance.
     */
    private IndividualRequestMdb individualRequestMdb;

    private static final String TEST_SUCCESSFULLY_COMPLETED = "Test successfully completed";

    /**
     * Pre-testing initialization.
     */
    @BeforeEach
    @Override
    public void setUp() {
        mockTargetSubmissionService = mock(TargetApplicationSubmissionService.class);
    }

    /**
     * Method to test successful scenario for readMessage.
     *
     * @throws JMSException during the test setup
     */
    @Test
    public void readMessageSuccessTest() throws JMSException {
        individualRequestMdb = new IndividualRequestMdb(mockTargetSubmissionService);

        // Create the actual message to send.
        final ISdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference("ReadTest1");

        // Wrap the message in the ObjectMessage
        final ObjectMessage objMessage = mock(ObjectMessage.class);
        when(objMessage.getObject()).thenReturn((Serializable) sdtMessage);

        mockTargetSubmissionService.processRequestToSubmit(sdtMessage.getSdtRequestReference());

        verify(mockTargetSubmissionService).processRequestToSubmit(sdtMessage.getSdtRequestReference());

        individualRequestMdb.readMessage(objMessage);

        assertTrue(true,TEST_SUCCESSFULLY_COMPLETED);
    }

    /**
     * Test method to test the scenario where the service throws a Data Access Exception.
     *
     * @throws JMSException if any during the test setup
     */

    @Test
    public void readMessageDataAccessExceptionTest() throws JMSException {
        individualRequestMdb = new IndividualRequestMdb(mockTargetSubmissionService);

        // Create the actual message to send
        final ISdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference("ReadTest1");

        // Wrap the message in the ObjectMessage
        final ObjectMessage objMessage = mock(ObjectMessage.class);
        when(objMessage.getObject()).thenReturn((Serializable) sdtMessage);

        doThrow(new RuntimeException("Data error occurred"))
            .when(mockTargetSubmissionService).processRequestToSubmit(sdtMessage.getSdtRequestReference());

        try {
            individualRequestMdb.readMessage(objMessage);
            fail("RuntimeException was expected to be thrown");
        } catch (final RuntimeException re) {
            assertTrue(true,"Expected to throw the exception");
        }

        Mockito.verify(mockTargetSubmissionService).processRequestToSubmit(sdtMessage.getSdtRequestReference());
        Mockito.verify(objMessage).getObject();

        assertTrue(true,TEST_SUCCESSFULLY_COMPLETED);
    }


    @Test
    public void readMessageJMSExceptionTest() throws JMSException {
        individualRequestMdb = new IndividualRequestMdb(mockTargetSubmissionService);

        // Create the spy message to send
        ISdtMessage sdtMessageMock = mock(SdtMessage.class);

        // Wrap the message in the ObjectMessage
        final ObjectMessage objMessage = mock(ObjectMessage.class);

        try {
            when(sdtMessageMock.getSdtRequestReference()).thenThrow(new JMSException("JMS error occurred"));
            individualRequestMdb.readMessage(objMessage);
            fail("JMS was expected to be thrown");
        } catch (final RuntimeException e) {
            assertTrue(true,"Expected to throw the exception");
        }
    }

    /**
     * Test method to test the scenario where the message is some other object other than
     * the ObjectMessage.
     *
     * @throws JMSException if any during the test setup
     */
    @Test
    public void readMessageInvalidObject() throws JMSException {
        individualRequestMdb = new IndividualRequestMdb(mockTargetSubmissionService);

        final Message message = mock(Message.class);

        individualRequestMdb.readMessage(message);

        assertTrue(true,TEST_SUCCESSFULLY_COMPLETED);
    }

    @Test
    void setMockTargetSubmissionServiceTest(){
        IndividualRequestMdb individualRequestMdbMock = mock(IndividualRequestMdb.class);

        individualRequestMdbMock.setTargetAppSubmissionService(mockTargetSubmissionService);

        verify(individualRequestMdbMock).setTargetAppSubmissionService(mockTargetSubmissionService);
    }

}
