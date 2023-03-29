package uk.gov.moj.sdt.consumers.config;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.phase.PhaseInterceptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.interceptors.in.SdtUnmarshallInterceptor;
import uk.gov.moj.sdt.interceptors.in.XmlInboundInterceptor;
import uk.gov.moj.sdt.interceptors.out.CacheEndOutboundInterceptor;
import uk.gov.moj.sdt.interceptors.out.CacheSetupOutboundInterceptor;
import uk.gov.moj.sdt.interceptors.out.XmlOutboundInterceptor;
import uk.gov.moj.sdt.ws._2013.sdt.targetappinternalendpoint.ITargetAppInternalEndpointPortType;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ConsumersConfigTest {

    @Spy
    ConsumersConfig consumersConfig = new ConsumersConfig();

    @Mock
    XmlOutboundInterceptor xmlOutboundInterceptor;

    @Mock
    CacheSetupOutboundInterceptor cacheSetupOutboundInterceptor;

    @Mock
    CacheEndOutboundInterceptor performanceLoggerOutboundInterceptor;

    @Mock
    CacheEndOutboundInterceptor cacheEndOutboundInterceptor;

    @Mock
    PhaseInterceptor<SoapMessage> performanceLoggerInboundInterceptor;

    @Mock
    XmlInboundInterceptor xmlInboundInterceptor;

    @Mock
    SdtUnmarshallInterceptor sdtUnmarshallInterceptor;

    @Test
    void testCreateTargetAppInternalEndpointPortType() {
        ITargetAppInternalEndpointPortType result =
            consumersConfig.createTargetAppInternalEndpointPortType(
                xmlOutboundInterceptor,
                cacheSetupOutboundInterceptor,
                performanceLoggerOutboundInterceptor,
                cacheEndOutboundInterceptor,
                performanceLoggerInboundInterceptor,
                xmlInboundInterceptor,
                sdtUnmarshallInterceptor
            );

        verify(consumersConfig).createTargetAppInternalEndpointPortType(
            xmlOutboundInterceptor,
            cacheSetupOutboundInterceptor,
            performanceLoggerOutboundInterceptor,
            cacheEndOutboundInterceptor,
            performanceLoggerInboundInterceptor,
            xmlInboundInterceptor,
            sdtUnmarshallInterceptor
        );

        assertNotNull(result);
    }

}
