package uk.gov.moj.sdt.producers.comx.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import uk.gov.moj.sdt.dao.api.*;
import uk.gov.moj.sdt.producers.comx.dao.*;

import java.util.List;

@ComponentScan("uk.gov.moj.sdt")
@Configuration
public class CommissioningDaoConfig {
    @Bean
    @Qualifier("BulkCustomerDao")
    public IBulkCustomerDao bulkCustomerDao() {
        MockBulkCustomerDao bulkCustomerDao = new MockBulkCustomerDao();
        List<String> targetAppCodes = List.of("MCOL");
        bulkCustomerDao.setTargetAppCodes(targetAppCodes);
        return bulkCustomerDao;
    }

    @Bean
    @Qualifier("MockGenericDao")
    public IGenericDao genericDao() {
        return new MockGenericDao();
    }

    @Bean
    @Qualifier("BulkSubmissionDao")
    public IBulkSubmissionDao bulkSubmissionDao() {
        MockBulkSubmissionDao bulkSubmissionDao = new MockBulkSubmissionDao();
        List<String> bulkReferenceList = List.of("MCOL_20130722000000_A00000001",
                                                 "MCOL_20130722000000_B00000001",
                                                 "MCOL_20130722000000_B00000002",
                                                 "MCOL_20130722000000_C00000001");
        bulkSubmissionDao.setBulkReferenceList(bulkReferenceList);
        return bulkSubmissionDao;
    }

    @Bean
    @Qualifier("IndividualRequestDao")
    public IIndividualRequestDao individualRequestDao() {
        return new MockIndividualRequestDao();
    }

    @Bean
    @Qualifier("TargetApplicationDao")
    public ITargetApplicationDao targetApplicationDao() {
        return new MockTargetApplicationDao();
    }
}
