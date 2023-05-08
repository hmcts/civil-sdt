package uk.gov.moj.sdt.consumers.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.interceptors.in.SdtUnmarshallInterceptor;
import uk.gov.moj.sdt.interceptors.in.XmlInboundInterceptor;
import uk.gov.moj.sdt.interceptors.out.CacheEndOutboundInterceptor;
import uk.gov.moj.sdt.interceptors.out.CacheSetupOutboundInterceptor;
import uk.gov.moj.sdt.interceptors.out.XmlOutboundInterceptor;
import uk.gov.moj.sdt.ws._2013.sdt.targetappinternalendpoint.ITargetAppInternalEndpointPortType;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ConsumersConfigTest {

    ConsumersConfig consumersConfig = new ConsumersConfig();

    @Mock
    XmlOutboundInterceptor xmlOutboundInterceptor;

    @Mock
    CacheSetupOutboundInterceptor cacheSetupOutboundInterceptor;

    @Mock
    CacheEndOutboundInterceptor cacheEndOutboundInterceptor;

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
                cacheEndOutboundInterceptor,
                xmlInboundInterceptor,
                sdtUnmarshallInterceptor
            );

        assertNotNull(result);
    }

}
