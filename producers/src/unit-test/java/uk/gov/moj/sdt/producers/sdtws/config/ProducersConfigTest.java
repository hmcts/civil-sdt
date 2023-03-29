package uk.gov.moj.sdt.producers.sdtws.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.assembler.MBeanInfoAssembler;
import org.springframework.jmx.export.assembler.MethodNameBasedMBeanInfoAssembler;
import org.springframework.jmx.support.RegistrationPolicy;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.mbeans.api.ISdtManagementMBean;
import uk.gov.moj.sdt.utils.mbeans.api.ISdtMetricsMBean;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProducersConfigTest extends AbstractSdtUnitTestBase {

    private ProducersConfig producersConfig;

    @Mock
    ISdtManagementMBean mockSdtManagementMBean;

    @Mock
    ISdtMetricsMBean mockSdtMetricsMBean;

    @BeforeEach
    @Override
    public void setUp() {
        producersConfig = new ProducersConfig();
    }

    @Test
    void testMBeanExporter() {
        MBeanExporter result = producersConfig.mBeanExporter(mockSdtManagementMBean);

        Map<String, Object> resultBeans = (Map<String, Object>) getAccessibleField(
            MBeanExporter.class,
            "beans",
            Map.class,
            result);
        MBeanInfoAssembler resultAssembler = (MBeanInfoAssembler) getAccessibleField(
            MBeanExporter.class,
            "assembler",
            MBeanInfoAssembler.class,
            result);
        Set<String> resultManagedMethods = (Set<String>) getAccessibleField(
            MethodNameBasedMBeanInfoAssembler.class,
            "managedMethods",
            Set.class,
            resultAssembler);
        RegistrationPolicy resultRegistrationPolicy = (RegistrationPolicy) getAccessibleField(
            MBeanExporter.class,
            "registrationPolicy",
            RegistrationPolicy.class,
            result);

        assertEquals(1, resultBeans.size());
        assertEquals(4, resultManagedMethods.size());
        assertEquals(RegistrationPolicy.REPLACE_EXISTING, resultRegistrationPolicy);
    }

    @Test
    void testMBeanExporterProducerMetrics() {
        MBeanExporter result = producersConfig.mBeanExporterProducerMetrics(mockSdtMetricsMBean);

        Map<String, Object> resultBeans = (Map<String, Object>) getAccessibleField(
            MBeanExporter.class,
            "beans",
            Map.class,
            result);
        MBeanInfoAssembler resultAssembler = (MBeanInfoAssembler) getAccessibleField(
            MBeanExporter.class,
            "assembler",
            MBeanInfoAssembler.class,
            result);
        Set<String> resultManagedMethods = (Set<String>) getAccessibleField(
            MethodNameBasedMBeanInfoAssembler.class,
            "managedMethods",
            Set.class,
            resultAssembler);
        RegistrationPolicy resultRegistrationPolicy = (RegistrationPolicy) getAccessibleField(
            MBeanExporter.class,
            "registrationPolicy",
            RegistrationPolicy.class,
            result);

        assertEquals(1, resultBeans.size());
        assertEquals(20, resultManagedMethods.size());
        assertEquals(RegistrationPolicy.REPLACE_EXISTING, resultRegistrationPolicy);
    }
}
