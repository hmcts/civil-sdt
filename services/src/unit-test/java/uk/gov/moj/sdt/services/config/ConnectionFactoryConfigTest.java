package uk.gov.moj.sdt.services.config;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.apache.qpid.jms.policy.JmsDefaultPrefetchPolicy;
import org.junit.jupiter.api.Test;
import org.springframework.jms.connection.CachingConnectionFactory;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConnectionFactoryConfigTest extends AbstractSdtUnitTestBase {

    private static final String SERVICE_BUS_HOST = "destination1.servicebus.windows.net";
    private static final String SERVICE_BUS_ACCESS_KEY_NAME = "[KEYNAME]";
    private static final String SERVICE_BUS_ACCESS_KEY_VALUE = "[KEY]";
    private static final int IDLE_TIMEOUT = 123;

    private ConnectionFactoryConfig connectionFactoryConfig;

    @Override
    protected void setUpLocalTests() {
        connectionFactoryConfig = new ConnectionFactoryConfig();
    }

    @Test
    void testConnectionFactoryConfig() {
        String serviceBusConnecitonString =
            String.format("Endpoint=sb://%s;SharedAccessKeyName=%s;SharedAccessKey=%s",
                          SERVICE_BUS_HOST,
                          SERVICE_BUS_ACCESS_KEY_NAME,
                          SERVICE_BUS_ACCESS_KEY_VALUE
            );

        connectionFactoryConfig.setConnectionString(serviceBusConnecitonString);
        connectionFactoryConfig.setIdleTimeout(IDLE_TIMEOUT);

        CachingConnectionFactory connectionFactory =
            (CachingConnectionFactory) connectionFactoryConfig.connectionFactory();

        JmsConnectionFactory jmsConnectionFactory =
            (JmsConnectionFactory) connectionFactory.getTargetConnectionFactory();

        String serviceBusRemoteUri =
            String.format("failover:(amqps://%s?amqp.idleTimeout=%s)", SERVICE_BUS_HOST, IDLE_TIMEOUT);
        assertEquals(serviceBusRemoteUri,
                     jmsConnectionFactory.getRemoteURI(),
                     "JmsConnectionFactory has unexpected remote URI");

        assertEquals(SERVICE_BUS_ACCESS_KEY_NAME,
                     jmsConnectionFactory.getUsername(),
                     "JmsConnectionFactory has unexpected access key name");
        assertEquals(SERVICE_BUS_ACCESS_KEY_VALUE,
                     jmsConnectionFactory.getPassword(),
                     "JmsConnectionFactory has unexpected access key value");

        JmsDefaultPrefetchPolicy jmsDefaultPrefetchPolicy =
            (JmsDefaultPrefetchPolicy) jmsConnectionFactory.getPrefetchPolicy();

        assertPreFetchValue(jmsDefaultPrefetchPolicy.getQueuePrefetch(), "queue");
        assertPreFetchValue(jmsDefaultPrefetchPolicy.getTopicPrefetch(), "topic");
        assertPreFetchValue(jmsDefaultPrefetchPolicy.getDurableTopicPrefetch(), "durable topic");
        assertPreFetchValue(jmsDefaultPrefetchPolicy.getQueueBrowserPrefetch(), "queue browser");
    }

    private void assertPreFetchValue(int prefetchValue, String prefetchName) {
        assertEquals(0, prefetchValue, "JmsConnectionFactory has unexpected " + prefetchName + " prefetch value");
    }
}
