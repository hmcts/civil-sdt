package uk.gov.moj.sdt.services.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import uk.gov.moj.sdt.services.messaging.QueueConfig;
import uk.gov.moj.sdt.services.utils.GenericXmlParser;
import uk.gov.moj.sdt.services.utils.IndividualRequestsXmlParser;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import javax.jms.ConnectionFactory;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicesConfigTest extends AbstractSdtUnitTestBase {

    @Mock
    private ConnectionFactory mockConnectionFactory;
    @Mock
    private PlatformTransactionManager mockPlatformTransactionManager;
    @Mock
    private QueueConfig mockQueueConfig;

    @Test
    void testMessageListenerContainer(){

        //Can create a new actual instance rather then a spy
        ServicesConfig mockServicesConfig = spy(ServicesConfig.class);
        MessageListenerAdapter mockMessageListenerAdaptor = mock(MessageListenerAdapter.class);
        Map<String, String> targetAppQueue = new HashMap<>();
        targetAppQueue.put("MCOL","MCOL");

        when(mockQueueConfig.getTargetAppQueue()).thenReturn(targetAppQueue);

        DefaultMessageListenerContainer defaultMessageListenerContainer = mockServicesConfig.messageListenerContainer(
            mockConnectionFactory, mockPlatformTransactionManager, mockQueueConfig, mockMessageListenerAdaptor);

        verify(mockQueueConfig).getTargetAppQueue();
        assertEquals(defaultMessageListenerContainer.getActiveConsumerCount(),0);
        assertEquals(defaultMessageListenerContainer.getCacheLevel(),4);
        assertEquals(defaultMessageListenerContainer.getMaxConcurrentConsumers(),5);
        assertEquals(defaultMessageListenerContainer.getIdleConsumerLimit(),5);
        assertEquals(defaultMessageListenerContainer.getCacheLevel(),4);
        assertNull(defaultMessageListenerContainer.getSubscriptionName());
        assertEquals(defaultMessageListenerContainer.getDestinationName(),"MCOL");
        assertNotNull(defaultMessageListenerContainer);
        assertEquals(defaultMessageListenerContainer.getConnectionFactory(),mockConnectionFactory);
    }

    @Test
    void testSubmitQueryResponseXmlParser() {

        ServicesConfig servicesConfig = new ServicesConfig();
        GenericXmlParser genericXmlParser = servicesConfig.submitQueryResponseXmlParser();

        Map<String, String> replacementNamespaces =
            (Map<String, String>) getAccessibleField(GenericXmlParser.class,
                                                     "replacementNamespaces",
                                                     Map.class,
                                                     genericXmlParser);

        assertNotNull(replacementNamespaces);
        assertEquals(1, replacementNamespaces.size());
        assertEquals(ServicesConfig.SUBMIT_QUERY_RESPONSE_SCHEMA,
                     replacementNamespaces.get("http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/SubmitQueryResponseSchema"));

    }
    @Test
    void testSubmitQueryRequestXmlParser(){

        GenericXmlParser genericXmlParser = null;
        ServicesConfig servicesConfig = new ServicesConfig();
        genericXmlParser = servicesConfig.submitQueryRequestXmlParser();

        assertNotNull(genericXmlParser);
        assertEquals(genericXmlParser.getEnclosingTag(),"criterion");

    }

    @Test
    void testIndividualResponseXmlParser(){

        GenericXmlParser genericXmlParser = null;
        ServicesConfig servicesConfig = new ServicesConfig();
        genericXmlParser = servicesConfig.individualResponseXmlParser();

        assertNotNull(genericXmlParser);
        assertEquals(genericXmlParser.getEnclosingTag(),"targetAppDetail");
    }

    @Test
    void testIndividualRequestsXmlParser(){

        IndividualRequestsXmlParser genericXmlParser = null;
        ServicesConfig servicesConfig = new ServicesConfig();
        genericXmlParser = servicesConfig.individualRequestsXmlParser();

        assertNotNull(genericXmlParser);
    }

}
