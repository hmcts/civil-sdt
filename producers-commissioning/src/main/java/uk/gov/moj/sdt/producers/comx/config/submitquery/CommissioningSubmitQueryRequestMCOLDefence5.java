package uk.gov.moj.sdt.producers.comx.config.submitquery;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.SubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;

@Configuration
public class CommissioningSubmitQueryRequestMCOLDefence5 {
    @Bean
    @Qualifier("ISubmitQueryRequestMCOLDefence5")
    public static ISubmitQueryRequest submitQueryRequestMCOLDefence5() {
        SubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();
        submitQueryRequest.setResultCount(0);
        submitQueryRequest.setErrorLog(errorLogMCOLDefence5());
        return submitQueryRequest;
    }

    @Bean
    @Qualifier("IErrorLogMCOLDefence5")
    private static IErrorLog errorLogMCOLDefence5() {
        return new ErrorLog("2", "MCOL customer number specified has not been set up for SDT use on MCOL.");
    }
}
