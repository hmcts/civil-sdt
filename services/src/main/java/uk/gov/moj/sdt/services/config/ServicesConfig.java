package uk.gov.moj.sdt.services.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.gov.moj.sdt.services.messaging.QueueConfig;
import uk.gov.moj.sdt.services.utils.GenericXmlParser;
import uk.gov.moj.sdt.services.utils.IndividualRequestsXmlParser;

import java.util.HashMap;
import java.util.Map;
import javax.jms.ConnectionFactory;

@ComponentScan("uk.gov.moj.sdt")
@Configuration
@EnableConfigurationProperties(QueueConfig.class)
@EnableTransactionManagement
public class ServicesConfig {

    @Autowired
    private ConnectionFactory jmsConnectionFactory;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    @Lazy
    private MessageListenerAdapter messageListenerAdapter;

    @Autowired
    private QueueConfig queueConfig;

    @Bean
    @Lazy
    @Qualifier("messageListenerContainer")
    public DefaultMessageListenerContainer messageListenerContainer() {
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setConnectionFactory(jmsConnectionFactory);
        defaultMessageListenerContainer.setDestinationName(queueConfig.getQueueConfig().get("MCOL"));
        defaultMessageListenerContainer.setMessageListener(messageListenerAdapter);
        defaultMessageListenerContainer.setTransactionManager(transactionManager);
        defaultMessageListenerContainer.setConcurrentConsumers(1);
        defaultMessageListenerContainer.setMaxConcurrentConsumers(5);
        defaultMessageListenerContainer.setReceiveTimeout(30000);
        defaultMessageListenerContainer.setIdleTaskExecutionLimit(10);
        defaultMessageListenerContainer.setIdleConsumerLimit(5);
        return defaultMessageListenerContainer;
    }

    @Bean
    @Qualifier("SubmitQueryResponseXmlParser")
    public GenericXmlParser submitQueryResponseXmlParser() {
        GenericXmlParser genericXmlParser = new GenericXmlParser();
        genericXmlParser.setEnclosingTag("targetAppDetail");
        Map<String, String> replacementNamespaces = new HashMap<>();
        replacementNamespaces.put("http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/SubmitQueryResponseSchema",
                                  "http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryResponseSchema");
        genericXmlParser.setReplacementNamespaces(replacementNamespaces);
        return genericXmlParser;
    }

    @Bean
    @Qualifier("SubmitQueryRequestXmlParser")
    public GenericXmlParser submitQueryRequestXmlParser() {
        GenericXmlParser genericXmlParser = new GenericXmlParser();
        genericXmlParser.setEnclosingTag("criterion");
        Map<String, String> replacementNamespaces = new HashMap<>();
        replacementNamespaces.put("http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryRequestSchema",
                                  "http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/SubmitQueryRequestSchema");
        genericXmlParser.setReplacementNamespaces(replacementNamespaces);
        return genericXmlParser;
    }

    @Bean
    @Qualifier("IndividualResponseXmlParser")
    public GenericXmlParser individualResponseXmlParser() {
        GenericXmlParser genericXmlParser = new GenericXmlParser();
        genericXmlParser.setEnclosingTag("targetAppDetail");
        Map<String, String> replacementNamespaces = new HashMap<>();
        replacementNamespaces.put("http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/IndvResponseSchema",
                                  "http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema");
        replacementNamespaces.put("http://ws.sdt.moj.gov.uk/2013/sdt/IndividualUpdateRequestSchema",
                                  "http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema");
        genericXmlParser.setReplacementNamespaces(replacementNamespaces);
        return genericXmlParser;
    }

    @Bean
    @Qualifier("IndividualRequestsXmlParser")
    public IndividualRequestsXmlParser individualRequestsXmlParser() {
        IndividualRequestsXmlParser genericXmlParser = new IndividualRequestsXmlParser();
        Map<String, String> replacementNamespaces = new HashMap<>();
        replacementNamespaces.put("http://ws.sdt.moj.gov.uk/2013/sdt/BulkRequestSchema",
                                  "http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/IndvRequestSchema");
        genericXmlParser.setReplacementNamespaces(replacementNamespaces);
        return genericXmlParser;
    }
}
