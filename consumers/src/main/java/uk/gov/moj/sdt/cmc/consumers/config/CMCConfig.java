package uk.gov.moj.sdt.cmc.consumers.config;

import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.moj.sdt.cmc.consumers.api.CMCApi;
import uk.gov.moj.sdt.cmc.consumers.decoder.CMCErrorDecoder;

@Configuration
@EnableFeignClients(clients = {CMCApi.class})
public class CMCConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CMCErrorDecoder();
    }
}
