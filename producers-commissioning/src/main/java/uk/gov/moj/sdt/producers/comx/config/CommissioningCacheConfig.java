package uk.gov.moj.sdt.producers.comx.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import uk.gov.moj.sdt.producers.comx.cache.MockErrorMessagesCache;
import uk.gov.moj.sdt.producers.comx.cache.MockGlobalParametersCache;
import uk.gov.moj.sdt.services.cache.api.IErrorMessagesCache;
import uk.gov.moj.sdt.services.cache.api.IGlobalParametersCache;

@ComponentScan("uk.gov.moj.sdt")
@Configuration
public class CommissioningCacheConfig {
    @Bean
    @Qualifier("GlobalParametersCache")
    public IGlobalParametersCache globalParametersCache() {
        return new MockGlobalParametersCache();
    }

    @Bean
    @Qualifier("ErrorMessagesCache")
    public IErrorMessagesCache errorMessagesCache() {
        return new MockErrorMessagesCache();
    }
}
