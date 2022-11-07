package uk.gov.moj.sdt.services.messaging;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@Data
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "ms.servicebus.internal")
public class QueueConfig {

    private Map<String, String> queueConfig;
}
