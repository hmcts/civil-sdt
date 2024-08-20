package uk.gov.moj.sdt.services.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.connection.CachingConnectionFactory;

import javax.jms.ConnectionFactory;
import java.util.ArrayList;

@TestConfiguration
public class ConnectionFactoryTestConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
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
