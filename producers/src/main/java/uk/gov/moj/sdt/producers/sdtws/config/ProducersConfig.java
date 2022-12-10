package uk.gov.moj.sdt.producers.sdtws.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.assembler.MethodNameBasedMBeanInfoAssembler;
import uk.gov.moj.sdt.handlers.api.IWsCreateBulkRequestHandler;
import uk.gov.moj.sdt.handlers.api.IWsReadBulkRequestHandler;
import uk.gov.moj.sdt.handlers.api.IWsReadSubmitQueryHandler;
import uk.gov.moj.sdt.handlers.api.IWsUpdateItemHandler;
import uk.gov.moj.sdt.producers.sdtws.SdtEndpointPortType;
import uk.gov.moj.sdt.producers.sdtws.SdtInternalEndpointPortType;
import uk.gov.moj.sdt.utils.mbeans.api.ISdtManagementMBean;
import uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType;
import uk.gov.moj.sdt.ws._2013.sdt.sdtinternalendpoint.ISdtInternalEndpointPortType;

import java.util.HashMap;
import java.util.Map;

@ComponentScan("uk.gov.moj.sdt")
@Configuration
public class ProducersConfig {

    @Bean
    public MBeanExporter mBeanExporter(ISdtManagementMBean sdtManagementMBean) {
        MBeanExporter mBeanExporter = new MBeanExporter();
        Map<String, Object> beans = new HashMap<>();
        beans.put("bean:name=sdtProducersManagement", sdtManagementMBean);
        mBeanExporter.setBeans(beans);
        MethodNameBasedMBeanInfoAssembler methodNameBasedMBeanInfoAssembler = new MethodNameBasedMBeanInfoAssembler();
        methodNameBasedMBeanInfoAssembler.setManagedMethods(
            "uncache",
            "setMdbPoolSize",
            "requeueOldIndividualRequests",
            "processDLQRequest"
        );
        mBeanExporter.setAssembler(methodNameBasedMBeanInfoAssembler);
        return mBeanExporter;
    }

    @Bean
    @Qualifier("ISdtEndpointPortType")
    public ISdtEndpointPortType sdtEndpointPortType(@Qualifier("WsReadSubmitQueryHandler")
                                                    IWsReadSubmitQueryHandler wsReadSubmitQueryHandler,
                                                    @Qualifier("WsReadBulkFeedbackRequestHandler")
                                                    IWsReadBulkRequestHandler wsReadBulkRequestHandler,
                                                    @Qualifier("WsCreateBulkRequestHandler")
                                                    IWsCreateBulkRequestHandler wsCreateBulkRequestHandler) {
        SdtEndpointPortType sdtEndpointPortType = new SdtEndpointPortType();
        sdtEndpointPortType.setWsCreateBulkRequestHandler(wsCreateBulkRequestHandler);
        sdtEndpointPortType.setWsReadBulkRequestHandler(wsReadBulkRequestHandler);
        sdtEndpointPortType.setWsReadSubmitQueryHandler(wsReadSubmitQueryHandler);
        return sdtEndpointPortType;
    }

    @Bean
    @Qualifier("ISdtInternalEndpointPortType")
    public ISdtInternalEndpointPortType sdtInternalEndpointPortType(@Qualifier("WsUpdateItemHandler")
                                                                    IWsUpdateItemHandler wsUpdateItemHandler) {
        SdtInternalEndpointPortType sdtEndpointPortType = new SdtInternalEndpointPortType();
        sdtEndpointPortType.setUpdateItemHandler(wsUpdateItemHandler);
        return sdtEndpointPortType;
    }
}
