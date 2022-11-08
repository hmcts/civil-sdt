package uk.gov.moj.sdt.services.messaging.asb;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;

@Slf4j
@Component
public class MessageSender {

    private String connectionString;

    private JsonConverter jsonConverter;

    @Autowired
    public MessageSender(@Value("${jms.servicebus.internal.queues.inbound.connection-string}") String connectionString,
                         JsonConverter jsonConverter) {
        this.connectionString = connectionString;
        this.jsonConverter = jsonConverter;
    }

    public void sendMessage(String queueName, ISdtMessage sdtMessage) {
        final ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
            .connectionString(connectionString)
            .sender()
            .queueName(queueName)
            .buildClient();
        log.debug("Connected to queue {}", queueName);
        String message = jsonConverter.convertToString(sdtMessage);
        ServiceBusMessage serviceBusMessage = new ServiceBusMessage(message);
        senderClient.sendMessage(serviceBusMessage);
        log.debug("Message has been sent to the queue {}", queueName);
    }
}
