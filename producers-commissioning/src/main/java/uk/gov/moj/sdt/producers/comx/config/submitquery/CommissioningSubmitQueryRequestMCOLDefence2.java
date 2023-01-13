package uk.gov.moj.sdt.producers.comx.config.submitquery;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.SubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;

@Configuration
public class CommissioningSubmitQueryRequestMCOLDefence2 {
    @Bean
    @Qualifier("ISubmitQueryRequestMCOLDefence2")
    public static ISubmitQueryRequest submitQueryRequestMCOLDefence2() {
        SubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();
        submitQueryRequest.setResultCount(0);
        submitQueryRequest.setErrorLog(errorLogMCOLDefence2());
        return submitQueryRequest;
    }

    @Bean
    @Qualifier("IErrorLogMCOLDefence2")
    private static IErrorLog errorLogMCOLDefence2() {
        return new ErrorLog("77", "No defence notifications found for requested period.");
    }
}
