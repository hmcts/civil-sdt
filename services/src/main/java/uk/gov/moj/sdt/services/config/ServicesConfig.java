package uk.gov.moj.sdt.services.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.gov.moj.sdt.services.messaging.IndividualRequestMdb;
import uk.gov.moj.sdt.services.messaging.api.IMessageDrivenBean;
import uk.gov.moj.sdt.services.utils.GenericXmlParser;
import uk.gov.moj.sdt.services.utils.IndividualRequestsXmlParser;

import java.util.HashMap;
import java.util.Map;
import javax.jms.ConnectionFactory;

@Configuration
@EnableTransactionManagement
public class ServicesConfig {

    @Value("${MCOL.Queue}")
    private String destinationName;

    @Autowired
    private ConnectionFactory jmsConnectionFactory;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    @Qualifier("IndividualRequestMdb")
    private IMessageDrivenBean individualRequestMdb;

    @Qualifier("messageListenerContainer")
    @Bean
    @Lazy
    public DefaultMessageListenerContainer messageListenerContainer() {
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setConnectionFactory(jmsConnectionFactory);
        defaultMessageListenerContainer.setDestinationName(destinationName);
        defaultMessageListenerContainer.setMessageListener(messageListenerContainer(individualRequestMdb));
        defaultMessageListenerContainer.setTransactionManager(transactionManager);
        defaultMessageListenerContainer.setConcurrentConsumers(1);
        defaultMessageListenerContainer.setMaxConcurrentConsumers(5);
        defaultMessageListenerContainer.setReceiveTimeout(30000);
        defaultMessageListenerContainer.setIdleTaskExecutionLimit(10);
        defaultMessageListenerContainer.setIdleConsumerLimit(5);
        return defaultMessageListenerContainer;
    }

    @Bean
    public MessageListenerAdapter messageListenerContainer(IMessageDrivenBean individualRequestMdb) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(individualRequestMdb);
        messageListenerAdapter.setMessageConverter(null);
        messageListenerAdapter.setDefaultListenerMethod("readMessage");
        return messageListenerAdapter;
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
