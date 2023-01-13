package uk.gov.moj.sdt.producers.comx.config.submitquery;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.SubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;

@Configuration
public class CommissioningSubmitQueryRequestMCOLDefence6 {
    @Bean
    @Qualifier("ISubmitQueryRequestMCOLDefence6")
    public static ISubmitQueryRequest submitQueryRequestMCOLDefence6() {
        SubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();
        submitQueryRequest.setResultCount(0);
        submitQueryRequest.setErrorLog(errorLogMCOLDefence6());
        return submitQueryRequest;
    }

    @Bean
    @Qualifier("IErrorLogMCOLDefence6")
    private static IErrorLog errorLogMCOLDefence6() {
        return new ErrorLog("74", "To Date and Time must be later than From Date and Time.");
    }
}
