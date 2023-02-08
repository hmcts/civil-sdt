package uk.gov.moj.sdt.services.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.moj.sdt.domain.api.*;
import uk.gov.moj.sdt.services.messaging.MessageWriter;

import uk.gov.moj.sdt.services.messaging.SdtMessage;
import uk.gov.moj.sdt.services.messaging.api.IMessageWriter;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.transaction.synchronizer.MessageSynchronizer;
import uk.gov.moj.sdt.utils.transaction.synchronizer.api.IMessageSynchronizer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessagingUtilityTest extends AbstractSdtUnitTestBase {

    @BeforeEach
    @Override
    public void setUp(){
    }

    @Test
    public void testEnqueueRequestNew() {
        // Given
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        IBulkSubmission bulkSubmission = mock(IBulkSubmission.class);
        ITargetApplication targetApplication = mock(ITargetApplication.class);
        IMessageSynchronizer messageSynchronizer = mock(IMessageSynchronizer.class);
        IMessageWriter messageWriter = mock(IMessageWriter.class);

        when(individualRequest.getBulkSubmission()).thenReturn(bulkSubmission);
        when(bulkSubmission.getTargetApplication()).thenReturn(targetApplication);
        when(targetApplication.getTargetApplicationCode()).thenReturn("targetAppCode");
        when(individualRequest.getSdtRequestReference()).thenReturn("sdtRequestReference");

        MessagingUtility messagingUtility = new MessagingUtility(messageWriter,messageSynchronizer);
        messagingUtility.setMessageSynchronizer(messageSynchronizer);
        messagingUtility.setMessageWriter(messageWriter);

        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

        //When
        messagingUtility.enqueueRequest(individualRequest);

        // Then
        verify(messageSynchronizer).execute(runnableCaptor.capture());
        Runnable runnable = runnableCaptor.getValue();
        runnable.run();

        verify(messageWriter).queueMessage(any(SdtMessage.class), eq("targetAppCode"), eq(false));
    }



    @Test
    void testMessageSynchronizer(){
        IMessageWriter messagingWriter = mock(MessageWriter.class);
        IMessageSynchronizer messageSynchronizer = mock(MessageSynchronizer.class);
        MessagingUtility messagingUtility = new MessagingUtility(messagingWriter,messageSynchronizer);

        messagingUtility.setMessageSynchronizer(messageSynchronizer);

        assertNotNull(messagingUtility.getMessageSynchronizer(),"should have returned MessageSync");

    }

    @Test
    void testMessageWriter(){
        IMessageWriter messagingWriter = mock(MessageWriter.class);
        IMessageSynchronizer messageSynchronizer = mock(MessageSynchronizer.class);
        MessagingUtility messagingUtility = new MessagingUtility(messagingWriter,messageSynchronizer);

        messagingUtility.setMessageWriter(messagingWriter);

        assertNotNull(messagingUtility.getMessageWriter(),"should have returned MessageWriter");

    }

}
