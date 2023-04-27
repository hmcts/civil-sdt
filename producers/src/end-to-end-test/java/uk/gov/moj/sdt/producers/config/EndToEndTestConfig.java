package uk.gov.moj.sdt.producers.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.ws.Endpoint;

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

@ComponentScan("uk.gov.moj.sdt")
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
//@EnableCaching(proxyTargetClass = true)
@Import(ProducersConfig.class)
public class EndToEndTestConfig {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EndToEndTestConfig.class);
    public static final String OVERRIDDEN_DYNAMICALLY = "http://dummhost/overridden/dynamically";
    public static final String SOAP_BINDINGS_HTTP = "http://www.w3.org/2003/05/soap/bindings/HTTP/";

    @Value("${server.portnumber}")
    private int serverPortNumber = 8080;

    @Value("${server.hostname}")
    private String serverHostName = "localhost";

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
        jaxWsProxyFactoryBean.setBindingId(SOAP_BINDINGS_HTTP);
        Map<String, Object> properties = new HashMap<>();
//        properties.put("serviceClass", "uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType");
//        properties.put("address", address);
        properties.put("disable.outputstream.optimization", true);
        Map<String, String> soapEnvNsMap = new HashMap<>();
        properties.put("soap.env.ns.map", soapEnvNsMap);
        jaxWsProxyFactoryBean.setProperties(properties);
        return jaxWsProxyFactoryBean.create(ISdtEndpointPortType.class);
    }

    @Bean("ISdtInternalEndpointPortType")
    @Scope("prototype")
    public ISdtInternalEndpointPortType createSdtInternalEndpointPortType() {
        JaxWsProxyFactoryBean jaxWsInternalProxyFactoryBean = new JaxWsProxyFactoryBean();
        String address =  "http://"+ serverHostName + ":" + serverPortNumber + "/producers/service/sdtinternalapi";
        jaxWsInternalProxyFactoryBean.setAddress(address);
        jaxWsInternalProxyFactoryBean.setBindingId(SOAP_BINDINGS_HTTP);
        Map<String, Object> properties = new HashMap<>();
//        properties.put("serviceClass", "uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtInternalEndpointPortType");
//        properties.put("address", address);
        properties.put("disable.outputstream.optimization", true);
        Map<String, String> soapEnvNsMap = new HashMap<>();
        properties.put("soap.env.ns.map", soapEnvNsMap);

        jaxWsInternalProxyFactoryBean.setProperties(properties);
        return jaxWsInternalProxyFactoryBean.create(ISdtInternalEndpointPortType.class);
    }

    @Bean
    public Endpoint sdtEndpoint(@Qualifier("ISdtEndpointPortType")
                                ISdtEndpointPortType sdtEndpointPortType) {
        LOGGER.debug("sdtEndpoint - starting");

        EndpointImpl endpoint = new EndpointImpl(springBus(loggingFeature()), sdtEndpointPortType);

        Map<String, Object> properties = new HashMap<>();

        properties.put("systemPropertiesModeName", "SYSTEM_PROPERTIES_MODE_OVERRIDE");
        properties.put("ignoreResourceNotFound", "true");
        properties.put("ignoreUnresolvablePlaceholders", "true");
        Map<String, Object> mapLocations = new HashMap<>();
        properties.put("locations", mapLocations);

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
        LOGGER.debug("sdtEndpoint - return endpoint");
        return endpoint;
    }

    @Bean
    public Endpoint sdtInternalEndpoint(@Qualifier("ISdtInternalEndpointPortType")
                                        ISdtInternalEndpointPortType sdtInternalEndpointPortType) {
        LOGGER.debug("sdtInternalEndpoint - starting");
        EndpointImpl endpoint = new EndpointImpl(springBus(loggingFeature()), sdtInternalEndpointPortType);
        List<LoggingFeature> features = new ArrayList<>();
        features.add(new LoggingFeature());
        endpoint.setFeatures(features);

        Map<String, Object> properties = new HashMap<>();
        properties.put("schema-validation-enabled", false);
        endpoint.setProperties(properties);
        endpoint.publish("/sdtinternalapi");
        LOGGER.debug("sdtInternalEndpoint - return endpoint");
        return endpoint;
    }
}
