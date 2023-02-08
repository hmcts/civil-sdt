package uk.gov.moj.sdt.services.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.jms.ConnectionFactory;
import java.util.HashMap;
import java.util.Map;

import uk.gov.moj.sdt.services.messaging.QueueConfig;
import uk.gov.moj.sdt.services.utils.GenericXmlParser;
import uk.gov.moj.sdt.services.utils.IndividualRequestsXmlParser;

@ExtendWith(MockitoExtension.class)
class ServicesConfigTest {

@Mock
    private ConnectionFactory mockConnectionFactory;
@Mock
    private PlatformTransactionManager mockPlatformTransactionManager;
@Spy
    private QueueConfig mockQueueConfig;
@Mock
    private MessageListenerAdapter mockMessageListenerAdaptor;

    private DefaultMessageListenerContainer defaultMessageListenerContainer;

    @Test
    void testMessageListenerContainer(){

        ServicesConfig mockServicesConfig = Mockito.spy(ServicesConfig.class);

        Map<String, String> targetAppQueue = new HashMap<>();
        targetAppQueue.put("MCOL","MCOL");

        when(mockQueueConfig.getTargetAppQueue()).thenReturn(targetAppQueue);

        defaultMessageListenerContainer = mockServicesConfig.messageListenerContainer(
            mockConnectionFactory, mockPlatformTransactionManager, mockQueueConfig, mockMessageListenerAdaptor);

        verify(mockServicesConfig).messageListenerContainer(
            mockConnectionFactory, mockPlatformTransactionManager, mockQueueConfig, mockMessageListenerAdaptor);
        assertNotNull(defaultMessageListenerContainer);
    }

    @Test
    void testSubmitQueryResponseXmlParser(){

        GenericXmlParser genericXmlParser = null;
        ServicesConfig servicesConfig = new ServicesConfig();
        genericXmlParser = servicesConfig.submitQueryResponseXmlParser();

        assertNotNull(genericXmlParser);

    }
    @Test
    void testSubmitQueryRequestXmlParser(){

        GenericXmlParser genericXmlParser = null;
        ServicesConfig servicesConfig = new ServicesConfig();
        genericXmlParser = servicesConfig.submitQueryRequestXmlParser();

        assertNotNull(genericXmlParser);

    }

    @Test
    void testIndividualResponseXmlParser(){

        GenericXmlParser genericXmlParser = null;
        ServicesConfig servicesConfig = new ServicesConfig();
        genericXmlParser = servicesConfig.individualResponseXmlParser();

        assertNotNull(genericXmlParser);

    }

    @Test
    void testIndividualRequestsXmlParser(){

        IndividualRequestsXmlParser genericXmlParser = null;
        ServicesConfig servicesConfig = new ServicesConfig();
        genericXmlParser = servicesConfig.individualRequestsXmlParser();

        assertNotNull(genericXmlParser);

    }
}
