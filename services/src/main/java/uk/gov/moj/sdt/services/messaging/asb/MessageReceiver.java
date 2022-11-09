package uk.gov.moj.sdt.services.messaging.asb;

import com.azure.core.util.IterableStream;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceiverClient;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;

@Slf4j
@Component
public class MessageReceiver {

    private String connectionString;

    private String queueName;

    private JsonConverter jsonConverter;

    @Autowired
    public MessageReceiver(@Value("${jms.servicebus.internal.queues.inbound.connection-string}") String connectionString,
                           @Value("${jms.servicebus.internal.queues.inbound.queue-name}") String queueName,
                           JsonConverter jsonConverter) {
        this.connectionString = connectionString;
        this.queueName = queueName;
        this.jsonConverter = jsonConverter;
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
}
