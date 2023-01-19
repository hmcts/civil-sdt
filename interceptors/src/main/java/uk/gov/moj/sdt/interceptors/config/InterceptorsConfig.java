package uk.gov.moj.sdt.interceptors.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.moj.sdt.interceptors.enricher.GenericEnricher;

@Configuration
public class InterceptorsConfig {

    @Bean
    @Qualifier("IndividualRequestEnricher")
    public GenericEnricher individualRequestEnricher() {
        GenericEnricher genericEnricher = new GenericEnricher();
        genericEnricher.setParentTag("individualRequest");
        genericEnricher.setInsertionTag("targetAppDetail");
        return genericEnricher;
    }

    @Bean
    @Qualifier("SubmitQueryRequestEnricher")
    public GenericEnricher submitQueryRequestEnricher() {
        GenericEnricher genericEnricher = new GenericEnricher();
        genericEnricher.setParentTag("submitQueryRequest");
        genericEnricher.setInsertionTag("targetAppDetail");
        return genericEnricher;
    }
}
