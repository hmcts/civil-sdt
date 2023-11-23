package uk.gov.moj.sdt.services.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.services.messaging.SdtMessage;
import uk.gov.moj.sdt.services.messaging.api.IMessageWriter;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessagingUtilityTest extends AbstractSdtUnitTestBase {

    @Mock
    IMessageWriter messageWriter;

    @Test
    public void testEnqueueRequestNew() {
        // Given
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        IBulkSubmission bulkSubmission = mock(IBulkSubmission.class);
        ITargetApplication targetApplication = mock(ITargetApplication.class);

        when(individualRequest.getBulkSubmission()).thenReturn(bulkSubmission);
        when(bulkSubmission.getTargetApplication()).thenReturn(targetApplication);
        when(targetApplication.getTargetApplicationCode()).thenReturn("targetAppCode");
        when(individualRequest.getSdtRequestReference()).thenReturn("sdtRequestReference");

        MessagingUtility messagingUtility = new MessagingUtility(messageWriter);

        //When
        messagingUtility.enqueueRequest(individualRequest);

        verify(messageWriter).queueMessage(any(SdtMessage.class), eq("targetAppCode"));
    }

    @Test
    void testMessageWriter() {

        MessagingUtility messagingUtility = new MessagingUtility(messageWriter);

        IMessageWriter mockMessageWriter = mock(IMessageWriter.class);

        messagingUtility.setMessageWriter(mockMessageWriter);

        assertEquals(mockMessageWriter, messagingUtility.getMessageWriter());
    }

}
