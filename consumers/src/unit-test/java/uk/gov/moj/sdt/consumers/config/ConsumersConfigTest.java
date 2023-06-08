package uk.gov.moj.sdt.consumers.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.interceptors.enricher.BulkFeedbackEnricher;
import uk.gov.moj.sdt.interceptors.enricher.GenericEnricher;
import uk.gov.moj.sdt.interceptors.enricher.SubmitQueryEnricher;
import uk.gov.moj.sdt.ws._2013.sdt.targetappinternalendpoint.ITargetAppInternalEndpointPortType;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ConsumersConfigTest {

    ConsumersConfig consumersConfig = new ConsumersConfig();


    @Mock
    private SubmitQueryEnricher submitQueryEnricher;

    @Mock
    private BulkFeedbackEnricher bulkFeedbackEnricher;

    @Mock
    private GenericEnricher submitQueryRequestEnricher;

    @Mock
    private GenericEnricher individualRequestEnricher;

    @Test
    void testCreateTargetAppInternalEndpointPortType() {
        ITargetAppInternalEndpointPortType result =
            consumersConfig.createTargetAppInternalEndpointPortType(
                submitQueryEnricher,
                bulkFeedbackEnricher,
                submitQueryRequestEnricher,
                submitQueryRequestEnricher
            );

        assertNotNull(result);
    }

}
