package uk.gov.moj.sdt.config;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.moj.sdt.dao.ServiceRequestDao;
import uk.gov.moj.sdt.interceptors.in.PerformanceLoggerInboundInterceptor;
import uk.gov.moj.sdt.interceptors.in.SdtUnmarshallInterceptor;
import uk.gov.moj.sdt.interceptors.in.ServiceRequestInboundInterceptor;
import uk.gov.moj.sdt.interceptors.in.XmlInboundInterceptor;
import uk.gov.moj.sdt.interceptors.out.CacheEndOutboundInterceptor;
import uk.gov.moj.sdt.interceptors.out.CacheSetupOutboundInterceptor;
import uk.gov.moj.sdt.interceptors.out.FaultOutboundInterceptor;
import uk.gov.moj.sdt.interceptors.out.PerformanceLoggerOutboundInterceptor;
import uk.gov.moj.sdt.interceptors.out.ServiceRequestOutboundInterceptor;
import uk.gov.moj.sdt.interceptors.out.XmlOutboundInterceptor;
import uk.gov.moj.sdt.producers.sdtws.SdtEndpointPortType;
import uk.gov.moj.sdt.producers.sdtws.SdtInternalEndpointPortType;
import uk.gov.moj.sdt.producers.sdtws.config.ProducersConfig;

@Configuration
@EnableAutoConfiguration
@ComponentScan("uk.gov.moj.sdt")
@EnableCaching(proxyTargetClass = true)
@Import(ProducersConfig.class)
public class CxfConfig {

    @Bean
    public ServletRegistrationBean<CXFServlet> cxfServlet() {
        return new ServletRegistrationBean<>(new CXFServlet(), "/producers/service/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus(LoggingFeature loggingFeature) {
        SpringBus cxfBus = new SpringBus();
        cxfBus.getFeatures().add(loggingFeature);
        return cxfBus;
    }

    @Bean
    public LoggingFeature loggingFeature() {
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true);
        return loggingFeature;
    }

    @Bean
    public Endpoint sdtEndpoint(@Qualifier("ISdtEndpointPortType")
                                SdtEndpointPortType sdtEndpointPortType, ServiceRequestDao serviceRequestDao) {
        ServiceRequestInboundInterceptor serviceRequestInboundInterceptor = new ServiceRequestInboundInterceptor(serviceRequestDao);
        PerformanceLoggerInboundInterceptor performanceLoggerInboundInterceptor = new PerformanceLoggerInboundInterceptor();
        XmlInboundInterceptor xmlInboundInterceptor = new XmlInboundInterceptor();
        SdtUnmarshallInterceptor sdtUnmarshallInterceptor = new SdtUnmarshallInterceptor();
        PerformanceLoggerOutboundInterceptor performanceLoggerOutboundInterceptor = new PerformanceLoggerOutboundInterceptor(serviceRequestDao);
        EndpointImpl endpoint = new EndpointImpl(springBus(loggingFeature()), sdtEndpointPortType);
        endpoint.setInInterceptors(Lists.newArrayList(performanceLoggerInboundInterceptor,
                                                xmlInboundInterceptor,
                                                sdtUnmarshallInterceptor,
                                                serviceRequestInboundInterceptor));
        CacheSetupOutboundInterceptor cacheSetupOutboundInterceptor = new CacheSetupOutboundInterceptor();
        XmlOutboundInterceptor xmlOutboundInterceptor = new XmlOutboundInterceptor();
        ServiceRequestOutboundInterceptor serviceRequestOutboundInterceptor = new ServiceRequestOutboundInterceptor(serviceRequestDao);
        CacheEndOutboundInterceptor cacheEndOutboundInterceptor = new CacheEndOutboundInterceptor();
        FaultOutboundInterceptor faultOutboundInterceptor = new FaultOutboundInterceptor(serviceRequestDao);

        endpoint.setOutInterceptors(Lists.newArrayList(cacheSetupOutboundInterceptor,
                                                       xmlOutboundInterceptor,
                                                       serviceRequestOutboundInterceptor,
                                                       performanceLoggerOutboundInterceptor,
                                                       cacheEndOutboundInterceptor));

        endpoint.setOutFaultInterceptors(Lists.newArrayList(faultOutboundInterceptor));

        Map<String, Object> properties = new HashMap<>();
        properties.put("schema-validation-enabled", true);
        properties.put("disable.outputstream.optimization", true);
        Map<String, Object> nsMap = new HashMap<>();
        properties.put("soap.env.ns.map", nsMap);
        nsMap.put("base", "http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema");
        nsMap.put("breq", "http://ws.sdt.moj.gov.uk/2013/sdt/BulkRequestSchema");
        nsMap.put("bresp", "http://ws.sdt.moj.gov.uk/2013/sdt/BulkResponseSchema");
        nsMap.put("bfreq", "http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackRequestSchema");
        nsMap.put("bfresp", "http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema");
        nsMap.put("qreq", "http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryRequestSchema");
        nsMap.put("qresp", "http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryResponseSchema");
        endpoint.setProperties(properties);
        endpoint.publish("/sdtapi");
        return endpoint;
    }

    @Bean
    public Endpoint sdtInternalEndpoint(@Qualifier("ISdtInternalEndpointPortType")
                                            SdtInternalEndpointPortType sdtInternalEndpointPortType, ServiceRequestDao serviceRequestDao) {
        EndpointImpl endpoint = new EndpointImpl(springBus(loggingFeature()), sdtInternalEndpointPortType);
        PerformanceLoggerInboundInterceptor performanceLoggerInboundInterceptor = new PerformanceLoggerInboundInterceptor();
        XmlInboundInterceptor xmlInboundInterceptor = new XmlInboundInterceptor();
        SdtUnmarshallInterceptor sdtUnmarshallInterceptor = new SdtUnmarshallInterceptor();
        endpoint.setInInterceptors(Lists.newArrayList(performanceLoggerInboundInterceptor,
                                                      xmlInboundInterceptor,
                                                      sdtUnmarshallInterceptor));

        PerformanceLoggerOutboundInterceptor performanceLoggerOutboundInterceptor = new PerformanceLoggerOutboundInterceptor(serviceRequestDao);
        endpoint.setOutInterceptors(Lists.newArrayList(performanceLoggerOutboundInterceptor));

        FaultOutboundInterceptor faultOutboundInterceptor = new FaultOutboundInterceptor(serviceRequestDao);
        endpoint.setOutFaultInterceptors(Lists.newArrayList(faultOutboundInterceptor));
        List<LoggingFeature> features = new ArrayList<>();
        features.add(new LoggingFeature());
        endpoint.setFeatures(features);

        Map<String, Object> properties = new HashMap<>();
        properties.put("schema-validation-enabled", false);
        endpoint.setProperties(properties);
        endpoint.publish("/sdtinternalapi");
        return endpoint;
    }
}
