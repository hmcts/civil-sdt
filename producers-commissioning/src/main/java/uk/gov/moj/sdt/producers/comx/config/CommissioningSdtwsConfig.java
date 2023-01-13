package uk.gov.moj.sdt.producers.comx.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import uk.gov.moj.sdt.handlers.api.IWsCreateBulkRequestHandler;
import uk.gov.moj.sdt.handlers.api.IWsReadBulkRequestHandler;
import uk.gov.moj.sdt.handlers.api.IWsReadSubmitQueryHandler;
import uk.gov.moj.sdt.producers.comx.sdtws.SdtEndpointPortType;
import uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType;

@ComponentScan("uk.gov.moj.sdt")
@Configuration
public class CommissioningSdtwsConfig {
    @Bean
    @Qualifier("ISdtEndpointPortType")
    public ISdtEndpointPortType sdtEndpointPortType(@Qualifier("wsCreateBulkRequestHandler")
                                                    IWsCreateBulkRequestHandler wsCreateBulkRequestHandler,
                                                    @Qualifier("wsReadBulkRequestHandler")
                                                    IWsReadBulkRequestHandler wsReadBulkRequestHandler,
                                                    @Qualifier("wsReadSubmitQueryHandler")
                                                    IWsReadSubmitQueryHandler wsReadSubmitQueryHandler) {
        SdtEndpointPortType sdtEndpointPortType = new SdtEndpointPortType();
        sdtEndpointPortType.setWsCreateBulkRequestHandler(wsCreateBulkRequestHandler);
        sdtEndpointPortType.setWsReadBulkRequestHandler(wsReadBulkRequestHandler);
        sdtEndpointPortType.setWsReadSubmitQueryHandler(wsReadSubmitQueryHandler);
        return sdtEndpointPortType;
    }
}
