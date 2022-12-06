package uk.gov.moj.sdt.services.messaging.asb;

import com.azure.core.util.IterableStream;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceiverClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageReceiver {

    @Value("${spring.jms.servicebus.connection-string}")
    private String connectionString;

    @Autowired
    public MessageReceiver() {
    }

    public ServiceBusReceivedMessage receiveMessage(String queueName) {
        IterableStream<ServiceBusReceivedMessage> messages = receiveMessage(queueName, 1);
        return messages.stream()
            .limit(1)
            .findAny().orElse(null);
    }

    public IterableStream<ServiceBusReceivedMessage> receiveMessage(String queueName, int maxMessages) {
        final ServiceBusReceiverClient receiverClient = new ServiceBusClientBuilder()
            .connectionString(connectionString)
            .receiver()
            .queueName(queueName)
            .buildClient();
        log.debug("Connected to queue {}", queueName);
        return receiverClient.receiveMessages(maxMessages);
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
}
