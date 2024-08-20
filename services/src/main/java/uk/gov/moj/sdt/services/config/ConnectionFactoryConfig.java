package uk.gov.moj.sdt.services.config;

import com.microsoft.azure.servicebus.jms.ConnectionStringBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.apache.qpid.jms.policy.JmsDefaultPrefetchPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

import javax.jms.ConnectionFactory;

/**
 * Configures a connection factory to use with the service bus.
 *
 * Unable to use connection factory created by Spring auto-configuration as this has
 * issues with the mechanism used by the application to send messages to the service bus.
 */
@Configuration
@Getter
@Setter
@Slf4j
public class ConnectionFactoryConfig {

    @Value("${spring.jms.servicebus.connection-string}")
    private String connectionString;

    @Value("${spring.jms.servicebus.idle-timeout}")
    private int idleTimeout;

    @Bean
    public ConnectionFactory connectionFactory() {
        log.debug("Creating CachingConnectionFactory to use with service bus");

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();

        // Use ConnectionStringBuilder to parse the connection string
        ConnectionStringBuilder connectionStringBuilder = new ConnectionStringBuilder(connectionString);

        String remoteUri =
            "failover:(amqps://" + connectionStringBuilder.getEndpoint().getHost()
                + "?amqp.idleTimeout=" + idleTimeout + ")";
        String username = connectionStringBuilder.getSasKeyName();
        String password = connectionStringBuilder.getSasKey();

        JmsConnectionFactory jmsConnectionFactory = createJmsConnectionFactory(remoteUri, username, password);
        cachingConnectionFactory.setTargetConnectionFactory(jmsConnectionFactory);

        return cachingConnectionFactory;
    }

    private JmsConnectionFactory createJmsConnectionFactory(String remoteUri, String username, String password) {
        log.debug("Creating JmsConnectionFactory for remoteUri [{}]", remoteUri);

        JmsConnectionFactory jmsConnectionFactory = new JmsConnectionFactory();

        jmsConnectionFactory.setRemoteURI(remoteUri);
        jmsConnectionFactory.setUsername(username);
        jmsConnectionFactory.setPassword(password);

        // Set all prefetch policy values to zero
        JmsDefaultPrefetchPolicy jmsDefaultPrefetchPolicy =
            (JmsDefaultPrefetchPolicy) jmsConnectionFactory.getPrefetchPolicy();
        jmsDefaultPrefetchPolicy.setAll(0);
        jmsConnectionFactory.setPrefetchPolicy(jmsDefaultPrefetchPolicy);

        return jmsConnectionFactory;
    }
}
