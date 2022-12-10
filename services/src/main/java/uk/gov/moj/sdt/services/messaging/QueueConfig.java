package uk.gov.moj.sdt.services.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
@ConfigurationProperties(prefix = "spring.jms.servicebus.internal")
public class QueueConfig {

    private Map<String, String> queueConfig = new HashMap<>();

    public Map<String, String> getQueueConfig() {
        return queueConfig;
    }

    public void setQueueConfig(Map<String, String> queueConfig) {
         this.queueConfig = queueConfig;
    }
}
