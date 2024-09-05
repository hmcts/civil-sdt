package uk.gov.moj.sdt.services.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.connection.CachingConnectionFactory;

import javax.jms.ConnectionFactory;
import java.util.ArrayList;

@TestConfiguration
@Slf4j
@ConditionalOnProperty("enable-new-queue-process")
public class ConnectionFactoryTestConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        log.debug("Creating CachingConnectionFactory to use with ActiveMQ");
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL("vm://localhost?broker.persistent=false");

        ArrayList<String> trustedPackages = new ArrayList<>();
        trustedPackages.add("uk.gov.moj.sdt.services.messaging");
        activeMQConnectionFactory.setTrustedPackages(trustedPackages);

        cachingConnectionFactory.setTargetConnectionFactory(activeMQConnectionFactory);

        return cachingConnectionFactory;
    }
}
