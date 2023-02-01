package uk.gov.moj.sdt.producers.comx.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import uk.gov.moj.sdt.domain.SubmitQueryRequest;
import uk.gov.moj.sdt.producers.comx.services.MockBulkFeedbackService;
import uk.gov.moj.sdt.producers.comx.services.MockBulkSubmissionService;
import uk.gov.moj.sdt.producers.comx.services.MockSubmitQueryService;
import uk.gov.moj.sdt.producers.comx.services.MockUpdateRequestService;
import uk.gov.moj.sdt.producers.comx.utils.BulkFeedbackFactory;
import uk.gov.moj.sdt.services.api.IBulkFeedbackService;
import uk.gov.moj.sdt.services.api.IBulkSubmissionService;
import uk.gov.moj.sdt.services.api.ISubmitQueryService;
import uk.gov.moj.sdt.services.api.IUpdateRequestService;
import uk.gov.moj.sdt.services.utils.api.ISdtBulkReferenceGenerator;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static uk.gov.moj.sdt.producers.comx.config.bulkfeedback.CommissioningBulkFeedbackA00000001.createIndividualRequestA00000001;
import static uk.gov.moj.sdt.producers.comx.config.bulkfeedback.CommissioningBulkFeedbackB00000001.createIndividualRequestB00000001;
import static uk.gov.moj.sdt.producers.comx.config.bulkfeedback.CommissioningBulkFeedbackB00000002.createIndividualRequestB00000002;
import static uk.gov.moj.sdt.producers.comx.config.bulkfeedback.CommissioningBulkFeedbackC00000001.createIndividualRequestC00000001;
import static uk.gov.moj.sdt.producers.comx.config.submitquery.CommissioningSubmitQueryRequestMCOLDefence1.submitQueryRequestMCOLDefence1;
import static uk.gov.moj.sdt.producers.comx.config.submitquery.CommissioningSubmitQueryRequestMCOLDefence2.submitQueryRequestMCOLDefence2;
import static uk.gov.moj.sdt.producers.comx.config.submitquery.CommissioningSubmitQueryRequestMCOLDefence3.submitQueryRequestMCOLDefence3;
import static uk.gov.moj.sdt.producers.comx.config.submitquery.CommissioningSubmitQueryRequestMCOLDefence4.submitQueryRequestMCOLDefence4;
import static uk.gov.moj.sdt.producers.comx.config.submitquery.CommissioningSubmitQueryRequestMCOLDefence5.submitQueryRequestMCOLDefence5;
import static uk.gov.moj.sdt.producers.comx.config.submitquery.CommissioningSubmitQueryRequestMCOLDefence6.submitQueryRequestMCOLDefence6;

@ComponentScan("uk.gov.moj.sdt")
@Configuration
public class CommissioningServicesConfig {
    @Bean
    @Qualifier("SubmitQueryService")
    public ISubmitQueryService submitQueryService() {
        MockSubmitQueryService submitQueryService = new MockSubmitQueryService();
        Map<String, SubmitQueryRequest> responseContentMap = new HashMap<>();
        responseContentMap.put("MCOLDefence1", (SubmitQueryRequest) submitQueryRequestMCOLDefence1());
        responseContentMap.put("MCOLDefence2", (SubmitQueryRequest) submitQueryRequestMCOLDefence2());
        responseContentMap.put("MCOLDefence3", (SubmitQueryRequest) submitQueryRequestMCOLDefence3());
        responseContentMap.put("MCOLDefence4", (SubmitQueryRequest) submitQueryRequestMCOLDefence4());
        responseContentMap.put("MCOLDefence5", (SubmitQueryRequest) submitQueryRequestMCOLDefence5());
        responseContentMap.put("MCOLDefence6", (SubmitQueryRequest) submitQueryRequestMCOLDefence6());

        submitQueryService.setResponseContentMap(responseContentMap);

        return submitQueryService;
    }

    @Bean
    @Qualifier("BulkSubmissionService")
    public IBulkSubmissionService bulkSubmissionService(@Qualifier("SdtBulkReferenceGenerator")
                                                        ISdtBulkReferenceGenerator sdtBulkReferenceGenerator) {
        MockBulkSubmissionService mockBulkSubmissionService = new MockBulkSubmissionService();
        mockBulkSubmissionService.setSdtBulkReferenceGenerator(sdtBulkReferenceGenerator);
        return mockBulkSubmissionService;
    }

    @Bean
    @Qualifier("BulkFeedbackService")
    public IBulkFeedbackService bulkFeedbackService() throws InvocationTargetException, IllegalAccessException {
        MockBulkFeedbackService mockBulkFeedbackService = new MockBulkFeedbackService();
        Map<String, BulkFeedbackFactory> bulkFeedbackFactoryMap = new HashMap<>();
        bulkFeedbackFactoryMap.put("MCOL_20130722000000_A00000001",
                                   (BulkFeedbackFactory) createIndividualRequestA00000001().invoke()
        );
        bulkFeedbackFactoryMap.put("MCOL_20130722000000_B00000001",
                                   (BulkFeedbackFactory) createIndividualRequestB00000001().invoke()
        );
        bulkFeedbackFactoryMap.put("MCOL_20130722000000_B00000002",
                                   (BulkFeedbackFactory) createIndividualRequestB00000002().invoke()
        );
        bulkFeedbackFactoryMap.put("MCOL_20130722000000_C00000001",
                                   (BulkFeedbackFactory) createIndividualRequestC00000001().invoke()
        );
        mockBulkFeedbackService.setBulkFeedbackFactoryMap(bulkFeedbackFactoryMap);
        return mockBulkFeedbackService;
    }

    @Bean
    @Qualifier("UpdateRequestService")
    public IUpdateRequestService updateRequestService() {
        return new MockUpdateRequestService();
    }
}
