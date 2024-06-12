package uk.gov.moj.sdt.cmc.consumers.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi;
import uk.gov.hmcts.reform.idam.client.IdamApi;
import uk.gov.moj.sdt.cmc.consumers.api.CMCApi;

@Configuration
@EnableFeignClients(basePackageClasses = {CMCApi.class, IdamApi.class, ServiceAuthorisationApi.class})
public class CMCConfig {
}
