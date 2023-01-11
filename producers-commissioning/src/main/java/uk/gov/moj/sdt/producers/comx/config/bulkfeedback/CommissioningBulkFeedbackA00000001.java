package uk.gov.moj.sdt.producers.comx.config.bulkfeedback;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.producers.comx.utils.BulkFeedbackFactory;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class CommissioningBulkFeedbackA00000001 {
    @Bean
    @Qualifier("IBulkSumissionA00000001")
    private static IBulkSubmission bulkSumissionA00000001() {
        BulkSubmission bulkSubmission = new BulkSubmission();
        bulkSubmission.setCustomerReference("USER_FILE_REFERENCE_A1");
        bulkSubmission.setCreatedDate(LocalDateTime.of(2013, 7, 22, 13, 0));
        bulkSubmission.setNumberOfRequest(1);
        bulkSubmission.markAsCompleted();
        return bulkSubmission;
    }

    @Bean
    @Qualifier("BulkFeedbackFactoryA00000001")
    private static BulkFeedbackFactory bulkFeedbackFactoryA00000001(@Qualifier("IBulkSumissionA00000001")
                                                                   IBulkSubmission bulkSubmission) {
        return new BulkFeedbackFactory(bulkSubmission);
    }

    @Bean
    @Qualifier("createIndividualRequestA00000001")
    public static MethodInvokingFactoryBean createIndividualRequestA00000001() {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setTargetObject(bulkFeedbackFactoryA00000001(bulkSumissionA00000001()));
        methodInvokingFactoryBean.setTargetMethod("createIndividualRequest");
        List<String> arguments = List.of("USER_REQUEST_ID_A1",
                                         "mcolClaim",
                                         "Rejected",
                                         "DUP_CUST_REQID",
                                         "Duplicate User Request Identifier submitted USER_REQUEST_ID_A1.",
                                         "");
        methodInvokingFactoryBean.setArguments(arguments);
        return methodInvokingFactoryBean;
    }
}
