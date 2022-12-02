package uk.gov.moj.sdt.services.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("uk.gov.moj.sdt")
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableTransactionManagement
public class ServicesTestConfig {

}
