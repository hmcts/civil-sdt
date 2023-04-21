package uk.gov.moj.sdt.producers.config;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import uk.gov.moj.sdt.producers.sdtws.config.ProducersConfig;
import uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType;
import uk.gov.moj.sdt.ws._2013.sdt.sdtinternalendpoint.ISdtInternalEndpointPortType;

import javax.xml.ws.Endpoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ComponentScan("uk.gov.moj.sdt")
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
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

    @Bean("JaxWsProxyFactoryBean")
    public JaxWsProxyFactoryBean jaxWsProxyFactoryBean() {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        String address =  "http://"+serverHostName+":"+serverPortNumber+"/producers/service/sdtapi";
//        jaxWsProxyFactoryBean.setAddress(OVERRIDDEN_DYNAMICALLY);
        jaxWsProxyFactoryBean.setAddress(address);
        jaxWsProxyFactoryBean.setBindingId(SOAP_BINDINGS_HTTP);
        Map<String, Object> properties = new HashMap<>();
        properties.put("serviceClass", "uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType");
        properties.put("address", address);
        jaxWsProxyFactoryBean.setProperties(properties);
        return jaxWsProxyFactoryBean;
    }

    @Bean("JaxWsInternalProxyFactoryBean")
    public JaxWsProxyFactoryBean jaxWsInternalProxyFactoryBean() {
        JaxWsProxyFactoryBean jaxWsInternalProxyFactoryBean = new JaxWsProxyFactoryBean();
        String address =  "http://"+serverHostName+":"+serverPortNumber+"/producers/service/sdtinternalapi";
//        jaxWsInternalProxyFactoryBean.setAddress(OVERRIDDEN_DYNAMICALLY);
        jaxWsInternalProxyFactoryBean.setAddress(address);
        jaxWsInternalProxyFactoryBean.setBindingId(SOAP_BINDINGS_HTTP);
        Map<String, Object> properties = new HashMap<>();
        properties.put("serviceClass", "uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtInternalEndpointPortType");
        properties.put("address", address);
        jaxWsInternalProxyFactoryBean.setProperties(properties);
        return jaxWsInternalProxyFactoryBean;
    }

    @Bean("ISdtEndpointPortType")
    @Scope("prototype")
    public ISdtEndpointPortType createSdtEndpointPortType(@Qualifier("JaxWsProxyFactoryBean")
                                                              JaxWsProxyFactoryBean jaxWsProxyFactoryBean) {
        LOGGER.debug("createSdtEndpointPortType - starting");

        Map<String, Object> properties = new HashMap<>();
        properties.put("disable.outputstream.optimization", true);
        Map<String, String> soapEnvNsMap = new HashMap<>();
        properties.put("soap.env.ns.map", soapEnvNsMap);
        jaxWsProxyFactoryBean.setProperties(properties);

        LOGGER.debug("createSdtEndpointPortType - return bean");
        return jaxWsProxyFactoryBean.create(ISdtEndpointPortType.class);
    }

    @Bean("ISdtInternalEndpointPortType")
    @Scope("prototype")
    public ISdtInternalEndpointPortType createSdtInternalEndpointPortType(@Qualifier("JaxWsInternalProxyFactoryBean")
                                                                            JaxWsProxyFactoryBean jaxWsInternalProxyFactoryBean) {
        LOGGER.debug("createSdtInternalEndpointPortType - starting");

        Map<String, Object> properties = new HashMap<>();
        properties.put("disable.outputstream.optimization", true);
        Map<String, String> soapEnvNsMap = new HashMap<>();
        properties.put("soap.env.ns.map", soapEnvNsMap);

        jaxWsInternalProxyFactoryBean.setProperties(properties);

        LOGGER.debug("createSdtInternalEndpointPortType - return bean");
        return jaxWsInternalProxyFactoryBean.create(ISdtInternalEndpointPortType.class);
    }

    @Bean
    public Endpoint sdtEndpoint(@Qualifier("ISdtEndpointPortType")
                                ISdtEndpointPortType sdtEndpointPortType) {
        LOGGER.debug("sdtEndpoint - starting");
        EndpointImpl endpoint = new EndpointImpl(new SpringBus(), sdtEndpointPortType);

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
        EndpointImpl endpoint = new EndpointImpl(new SpringBus(), sdtInternalEndpointPortType);
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
