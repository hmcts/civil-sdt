package uk.gov.moj.sdt.services.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.jms.ConnectionFactory;

@Configuration
@ComponentScan("uk.gov.moj.sdt")
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableTransactionManagement
@ImportResource(locations = {"classpath:/uk/gov/moj/sdt/services/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/services/cache/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/services/utils/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/services/mbeans/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/services/messaging/spring.hibernate.test.xml",
    "classpath:/uk/gov/moj/sdt/services/messaging/spring.context.test.xml",
    "classpath*:/uk/gov/moj/sdt/dao/**/spring*.xml",
    "classpath:/uk/gov/moj/sdt/consumers/spring.context.integ.test.xml",
    "classpath*:/uk/gov/moj/sdt/transformers/**/spring*.xml",
    "classpath*:/uk/gov/moj/sdt/interceptors/**/spring*.xml",
    "classpath*:/uk/gov/moj/sdt/validators/**/spring*.xml", "classpath*:/uk/gov/moj/sdt/utils/**/spring*.xml"})
public class ServicesConfig {


    @Value("${MCOL.Queue}")
    private String destinationName;

    @Autowired
    private ConnectionFactory jmsConnectionFactory;

    @Autowired
    private MessageListenerAdapter messageListener;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Bean
    @Lazy
    public DefaultMessageListenerContainer messageListenerContainer() {
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setConnectionFactory(jmsConnectionFactory);
        defaultMessageListenerContainer.setDestinationName(destinationName);
        defaultMessageListenerContainer.setMessageListener(messageListener);
        defaultMessageListenerContainer.setTransactionManager(transactionManager);
        defaultMessageListenerContainer.setConcurrentConsumers(1);
        defaultMessageListenerContainer.setMaxConcurrentConsumers(5);
        defaultMessageListenerContainer.setReceiveTimeout(30000);
        defaultMessageListenerContainer.setIdleTaskExecutionLimit(10);
        defaultMessageListenerContainer.setIdleConsumerLimit(5);
        return defaultMessageListenerContainer;
    }
}
