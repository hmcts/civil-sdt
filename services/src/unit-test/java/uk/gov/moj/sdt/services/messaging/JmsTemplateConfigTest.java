package uk.gov.moj.sdt.services.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import javax.jms.ConnectionFactory;

import static javax.jms.Session.CLIENT_ACKNOWLEDGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class JmsTemplateConfigTest extends AbstractSdtUnitTestBase {

    @Mock
    ConnectionFactory mockConnectionFactory;

    private JmsTemplateConfig jmsTemplateConfig;

    @Override
    protected void setUpLocalTests() {
        jmsTemplateConfig = new JmsTemplateConfig();
    }

    @Test
    void testJmsTemplateConfig() {
        JmsTemplate jmsTemplate = jmsTemplateConfig.jmsTemplate(mockConnectionFactory);

        assertTrue(jmsTemplate.isSessionTransacted(), "JmsTemplate session transacted should be true");
        assertEquals(CLIENT_ACKNOWLEDGE,
                     jmsTemplate.getSessionAcknowledgeMode(),
                     "JmsTemplate has unexpected session acknowledge mode");
    }
}
