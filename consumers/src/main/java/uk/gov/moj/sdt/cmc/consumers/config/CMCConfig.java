package uk.gov.moj.sdt.cmc.consumers.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import uk.gov.moj.sdt.cmc.consumers.api.CMCApi;

@Configuration
@EnableFeignClients(clients = {CMCApi.class})
public class CMCConfig {
}
