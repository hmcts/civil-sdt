package uk.gov.moj.sdt.producers.comx.config.submitquery;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.SubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;

@Configuration
public class CommissioningSubmitQueryRequestMCOLDefence4 {
    @Bean
    @Qualifier("ISubmitQueryRequestMCOLDefence4")
    public static ISubmitQueryRequest submitQueryRequestMCOLDefence4() {
        SubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();
        submitQueryRequest.setResultCount(0);
        submitQueryRequest.setErrorLog(errorLogMCOLDefence4());
        return submitQueryRequest;
    }

    @Bean
    @Qualifier("IErrorLogMCOLDefence4")
    private static IErrorLog errorLogMCOLDefence4() {
        return new ErrorLog("1", "Unknown MCOL customer number specified.");
    }
}
