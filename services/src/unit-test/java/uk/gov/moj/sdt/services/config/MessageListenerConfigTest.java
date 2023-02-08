package uk.gov.moj.sdt.services.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import uk.gov.moj.sdt.services.messaging.api.IMessageDrivenBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class MessageListenerConfigTest {

    @Mock
    private IMessageDrivenBean mockIndividualRequestMdb;


    @Test
    void messageListenerAdapterTest(){

        MessageListenerConfig messageListenerConfig = new MessageListenerConfig();

        MessageListenerAdapter messageListenerAdapter = messageListenerConfig.messageListenerAdapter(mockIndividualRequestMdb);

        assertNotNull(messageListenerAdapter);

    }
}
