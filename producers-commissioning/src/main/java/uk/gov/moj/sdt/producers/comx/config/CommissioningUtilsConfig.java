package uk.gov.moj.sdt.producers.comx.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import uk.gov.moj.sdt.producers.comx.utils.MockSdtBulkReferenceGenerator;
import uk.gov.moj.sdt.services.utils.api.ISdtBulkReferenceGenerator;

@ComponentScan("uk.gov.moj.sdt")
@Configuration
public class CommissioningUtilsConfig {
    @Bean
    @Qualifier("MockSdtBulkReferenceGenerator")
    public ISdtBulkReferenceGenerator sdtBulkReferenceGenerator() {
        return new MockSdtBulkReferenceGenerator();
    }
}
