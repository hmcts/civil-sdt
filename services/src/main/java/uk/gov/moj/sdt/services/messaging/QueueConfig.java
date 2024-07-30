package uk.gov.moj.sdt.services.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import java.util.HashMap;
import java.util.Map;
import javax.jms.ConnectionFactory;
import javax.jms.Session;


@Configuration
@ConfigurationProperties(prefix = "spring.jms.servicebus.internal")
public class QueueConfig {

    private Map<String, String> targetAppQueue = new HashMap<>();

    public Map<String, String> getTargetAppQueue() {
        return targetAppQueue;
    }

    public void setTargetAppQueue(Map<String, String> targetAppQueue) {
         this.targetAppQueue = targetAppQueue;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setSessionTransacted(true);
        jmsTemplate.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        return jmsTemplate;
    }
}
