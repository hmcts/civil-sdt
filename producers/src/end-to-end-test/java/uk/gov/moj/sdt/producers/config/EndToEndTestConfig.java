package uk.gov.moj.sdt.producers.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

import com.google.common.collect.Lists;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import uk.gov.moj.sdt.producers.sdtws.config.ProducersConfig;
import uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType;
import uk.gov.moj.sdt.ws._2013.sdt.sdtinternalendpoint.ISdtInternalEndpointPortType;

import uk.gov.moj.sdt.interceptors.in.IdamIdInboundInterceptor;
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

@ComponentScan("uk.gov.moj.sdt")
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableCaching(proxyTargetClass = true)
@Import(ProducersConfig.class)
public class EndToEndTestConfig {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EndToEndTestConfig.class);

    @Value("${server.portnumber}")
    private int serverPortNumber = 8080;

    @Value("${server.hostname}")
    private String serverHostName = "localhost";

    private static final String DISABLE_OUTPUTSTREAM_OPTIMIZATION = "disable.outputstream.optimization";
    private static final String SOAP_ENV_NS_MAP = "soap.env.ns.map";

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

    @Bean("ISdtEndpointPortType")
    @Scope("prototype")
    public ISdtEndpointPortType createSdtEndpointPortType() {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        String address =  "http://"+ serverHostName + ":" + serverPortNumber + "/producers/service/sdtapi";
        jaxWsProxyFactoryBean.setAddress(address);
        jaxWsProxyFactoryBean.setBindingId(SOAPBinding.SOAP11HTTP_BINDING);
        Map<String, Object> properties = new HashMap<>();
        properties.put(DISABLE_OUTPUTSTREAM_OPTIMIZATION, true);
        Map<String, String> soapEnvNsMap = new HashMap<>();
        properties.put(SOAP_ENV_NS_MAP, soapEnvNsMap);
        jaxWsProxyFactoryBean.setProperties(properties);
        LOGGER.info("createSdtEndpointPortType ... done");
        return jaxWsProxyFactoryBean.create(ISdtEndpointPortType.class);
    }

    @Bean("ISdtInternalEndpointPortType")
    @Scope("prototype")
    public ISdtInternalEndpointPortType createSdtInternalEndpointPortType() {
        JaxWsProxyFactoryBean jaxWsInternalProxyFactoryBean = new JaxWsProxyFactoryBean();
        String address =  "http://"+ serverHostName + ":" + serverPortNumber + "/producers/service/sdtinternalapi";
        jaxWsInternalProxyFactoryBean.setAddress(address);
        jaxWsInternalProxyFactoryBean.setBindingId(SOAPBinding.SOAP11HTTP_BINDING);
        Map<String, Object> properties = new HashMap<>();
        properties.put(DISABLE_OUTPUTSTREAM_OPTIMIZATION, true);

        jaxWsInternalProxyFactoryBean.setProperties(properties);
        LOGGER.info("createSdtInternalEndpointPortType ... done");
        return jaxWsInternalProxyFactoryBean.create(ISdtInternalEndpointPortType.class);
    }

    @Bean
    public Endpoint sdtEndpoint(@Qualifier("ISdtEndpointPortType")
                                ISdtEndpointPortType sdtEndpointPortType,
                                ServiceRequestInboundInterceptor serviceRequestInboundInterceptor,
                                PerformanceLoggerInboundInterceptor performanceLoggerInboundInterceptor,
                                XmlInboundInterceptor xmlInboundInterceptor,
                                SdtUnmarshallInterceptor sdtUnmarshallInterceptor,
                                IdamIdInboundInterceptor idamIdInboundInterceptor,
                                PerformanceLoggerOutboundInterceptor performanceLoggerOutboundInterceptor,
                                CacheSetupOutboundInterceptor cacheSetupOutboundInterceptor,
                                XmlOutboundInterceptor xmlOutboundInterceptor,
                                ServiceRequestOutboundInterceptor serviceRequestOutboundInterceptor,
                                CacheEndOutboundInterceptor cacheEndOutboundInterceptor,
                                FaultOutboundInterceptor faultOutboundInterceptor
    ) {
        LOGGER.debug("sdtEndpoint - starting");
        EndpointImpl endpoint = new EndpointImpl(springBus(loggingFeature()), sdtEndpointPortType);

        endpoint.setInInterceptors(Lists.newArrayList(performanceLoggerInboundInterceptor,
                xmlInboundInterceptor,
                sdtUnmarshallInterceptor,
                serviceRequestInboundInterceptor,
                idamIdInboundInterceptor));

        endpoint.setOutInterceptors(Lists.newArrayList(cacheSetupOutboundInterceptor,
                xmlOutboundInterceptor,
                serviceRequestOutboundInterceptor,
                performanceLoggerOutboundInterceptor,
                cacheEndOutboundInterceptor));

        endpoint.setOutFaultInterceptors(Lists.newArrayList(faultOutboundInterceptor));

        List<LoggingFeature> features = new ArrayList<>();
        features.add(new LoggingFeature());
        endpoint.setFeatures(features);

        Map<String, Object> properties = new HashMap<>();
        properties.put("systemPropertiesModeName", "SYSTEM_PROPERTIES_MODE_OVERRIDE");
        properties.put("ignoreResourceNotFound", "true");
        properties.put("ignoreUnresolvablePlaceholders", "true");
        Map<String, Object> mapLocations = new HashMap<>();
        properties.put("locations", mapLocations);

        properties.put("schema-validation-enabled", true);
        properties.put(DISABLE_OUTPUTSTREAM_OPTIMIZATION, true);
        Map<String, Object> nsMap = new HashMap<>();
        nsMap.put("base", "http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema");
        nsMap.put("breq", "http://ws.sdt.moj.gov.uk/2013/sdt/BulkRequestSchema");
        nsMap.put("bresp", "http://ws.sdt.moj.gov.uk/2013/sdt/BulkResponseSchema");
        nsMap.put("mresp", "http://ws.sdt.moj.gov.uk/2013/sdt/ResponseDetailSchema");
        nsMap.put("bfreq", "http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackRequestSchema");
        nsMap.put("bfresp", "http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema");
        nsMap.put("qreq", "http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryRequestSchema");
        nsMap.put("qresp", "http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryResponseSchema");
        nsMap.put("mqry", "http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema");
        properties.put(SOAP_ENV_NS_MAP, nsMap);
        endpoint.setProperties(properties);
        endpoint.publish("/sdtapi");
        LOGGER.info("sdtEndpoint - return endpoint");
        return endpoint;
    }

    @Bean
    public Endpoint sdtInternalEndpoint(@Qualifier("ISdtInternalEndpointPortType")
                                        ISdtInternalEndpointPortType sdtInternalEndpointPortType,
                                        PerformanceLoggerInboundInterceptor performanceLoggerInboundInterceptor,
                                        XmlInboundInterceptor xmlInboundInterceptor,
                                        SdtUnmarshallInterceptor sdtUnmarshallInterceptor,
                                        PerformanceLoggerOutboundInterceptor performanceLoggerOutboundInterceptor,
                                        FaultOutboundInterceptor faultOutboundInterceptor) {
        LOGGER.debug("sdtInternalEndpoint - starting");
        EndpointImpl endpoint = new EndpointImpl(springBus(loggingFeature()), sdtInternalEndpointPortType);

        endpoint.setInInterceptors(Lists.newArrayList(performanceLoggerInboundInterceptor,
                xmlInboundInterceptor,
                sdtUnmarshallInterceptor));

        endpoint.setOutInterceptors(Lists.newArrayList(performanceLoggerOutboundInterceptor));

        endpoint.setOutFaultInterceptors(Lists.newArrayList(faultOutboundInterceptor));

        List<LoggingFeature> features = new ArrayList<>();
        features.add(new LoggingFeature());
        endpoint.setFeatures(features);

        Map<String, Object> properties = new HashMap<>();
        properties.put("schema-validation-enabled", false);
        endpoint.setProperties(properties);
        endpoint.publish("/sdtinternalapi");
        LOGGER.info("sdtInternalEndpoint - return endpoint");
        return endpoint;
    }
}
