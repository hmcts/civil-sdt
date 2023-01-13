package uk.gov.moj.sdt.producers.comx.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.assembler.MethodNameBasedMBeanInfoAssembler;
import org.springframework.jmx.support.RegistrationPolicy;
import uk.gov.moj.sdt.utils.mbeans.api.ISdtMetricsMBean;

import java.util.HashMap;
import java.util.Map;

@ComponentScan("uk.gov.moj.sdt")
@Configuration
public class CommissioningMbeansConfig {
    @Bean
    @Qualifier("exporter")
    public MBeanExporter mBeanExporter(ISdtMetricsMBean sdtMetricsMBean) {
        MBeanExporter mBeanExporter = new MBeanExporter();
        Map<String, Object> beans = new HashMap<>();
        beans.put("bean:name=sdtProducersMetrics", sdtMetricsMBean);
        mBeanExporter.setBeans(beans);
        MethodNameBasedMBeanInfoAssembler methodNameBasedMBeanInfoAssembler = new MethodNameBasedMBeanInfoAssembler();
        methodNameBasedMBeanInfoAssembler.setManagedMethods(
            "getTime",
            "getOsStats",
            "getBulkSubmitStats",
            "getBulkFeedbackStats",
            "getSubmitQueryStats",
            "getStatusUpdateStats",
            "getDomainObjectsStats",
            "getDatabaseCallsStats",
            "getDatabaseReadsStats",
            "getDatabaseWritesStats",
            "getActiveCustomersStats",
            "getRequestQueueStats",
            "getTargetAppStats",
            "getErrorStats",
            "getLastRefStats",
            "getPerformanceLoggingString",
            "uncache",
            "setPerformanceLoggingFlags",
            "reset",
            "dumpMetrics"
        );
        mBeanExporter.setAssembler(methodNameBasedMBeanInfoAssembler);
        mBeanExporter.setRegistrationPolicy(RegistrationPolicy.REPLACE_EXISTING);
        return mBeanExporter;
    }
}
