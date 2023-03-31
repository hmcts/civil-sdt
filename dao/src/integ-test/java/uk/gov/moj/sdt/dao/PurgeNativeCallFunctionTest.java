package uk.gov.moj.sdt.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.moj.sdt.dao.api.IBulkSubmissionDao;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.dao.config.DaoTestConfig;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.GlobalParameter;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.PurgeJobAudit;
import uk.gov.moj.sdt.domain.PurgeJobAuditMessage;
import uk.gov.moj.sdt.domain.ServiceRequest;
import uk.gov.moj.sdt.test.utils.AbstractIntegrationTest;
import uk.gov.moj.sdt.test.utils.TestConfig;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test {@link PurgeNativeCallFunction} query methods.
 *
 * @author Mark Dathorne
 */
@ActiveProfiles("integ")
@SpringBootTest(classes = { TestConfig.class, DaoTestConfig.class})
@Sql(scripts = {"classpath:db/migration/V0002__purge.sql",
        "classpath:uk/gov/moj/sdt/dao/sql/PurgeDaoTest.sql"})
@Transactional
class PurgeNativeCallFunctionTest extends AbstractIntegrationTest {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PurgeNativeCallFunctionTest.class);

    private PurgeNativeCallFunction purgeNativeCallFunction;

    private PurgeJobAuditDao purgeJobAuditDao;
    private PurgeJobAuditMessageDao purgeJobAuditMessageDao;
    private ErrorLogDao errorLogDao;
    private GlobalParametersDao globalParametersDao;
    private ServiceRequestDao serviceRequestDao;
    private IBulkSubmissionDao bulkSubmissionDao;
    private IIndividualRequestDao individualRequestDao;

    @BeforeEach
    public void setup() {
        purgeNativeCallFunction = this.applicationContext.getBean(PurgeNativeCallFunction.class);

        purgeJobAuditDao = (PurgeJobAuditDao) this.applicationContext.getBean("PurgeJobAuditDao");
        purgeJobAuditMessageDao = (PurgeJobAuditMessageDao) this.applicationContext.getBean("PurgeJobAuditMessageDao");
        errorLogDao = (ErrorLogDao) this.applicationContext.getBean("ErrorLogDao");
        globalParametersDao = (GlobalParametersDao) this.applicationContext.getBean("GlobalParametersDao");
        serviceRequestDao = (ServiceRequestDao) this.applicationContext.getBean("ServiceRequestDao");
        bulkSubmissionDao = (IBulkSubmissionDao) this.applicationContext.getBean("BulkSubmissionDao");
        individualRequestDao = (IIndividualRequestDao) this.applicationContext.getBean("IndividualRequestDao");
    }

    /**
     * Tests {@link GenericDao} fetch.
     */
    @Test
    void runPurge() {
        LOGGER.info("TEST STARTING: run the Purge");

// TODO: Finish this test - it's the weekend!

        assertTrue(globalParameterIsPresentForId(1L));

        assertFalse(purgeJobAuditIsPresentForId(1L));
        assertFalse(purgeJobAuditMessageIsPresentForId(1L));
        assertTrue(serviceRequestIsPresentForId(3L));
        assertTrue(bulkSubmissionIsPresentForId(13L));
        assertTrue(individualRequestIsPresentForId(109L));
        assertTrue(errorLogIsPresentForId(1001L));

        purgeNativeCallFunction.executePurgeStoredProc(1000);

        assertTrue(purgeJobAuditIsPresentForId(1L));
        assertTrue(purgeJobAuditMessageIsPresentForId(1L));
//        assertFalse(serviceRequestIsPresentForId(3L));
//        assertFalse(bulkSubmissionIsPresentForId(13L));
//        assertFalse(individualRequestIsPresentForId(109L));
//        assertFalse(errorLogIsPresentForId(1001L));

        LOGGER.info("TEST COMPLETED: the Purge has been run");
    }

    private Boolean globalParameterIsPresentForId(Long id) {
        GlobalParameter globalParameter = globalParametersDao.fetch(GlobalParameter.class, id);
        return (null != globalParameter);
    }

    private Boolean serviceRequestIsPresentForId(Long id) {
        ServiceRequest serviceRequest = serviceRequestDao.fetch(ServiceRequest.class, id);
        return (null != serviceRequest);
    }

    private Boolean bulkSubmissionIsPresentForId(Long id) {
        BulkSubmission bulkSubmission = bulkSubmissionDao.fetch(BulkSubmission.class, id);
        return (null != bulkSubmission);
    }

    private Boolean individualRequestIsPresentForId(Long id) {
        IndividualRequest individualRequest = individualRequestDao.fetch(IndividualRequest.class, id);
        return (null != individualRequest);
    }

    private Boolean errorLogIsPresentForId(Long id) {
        ErrorLog errorLog = errorLogDao.fetch(ErrorLog.class, id);
        return (null != errorLog);
    }

    private Boolean purgeJobAuditIsPresentForId(Long id) {
        PurgeJobAudit purgeJobAudit = purgeJobAuditDao.fetch(PurgeJobAudit.class, id);
        return (null != purgeJobAudit);
    }

    private Boolean purgeJobAuditMessageIsPresentForId(Long id) {
        PurgeJobAuditMessage purgeJobAuditMessage = purgeJobAuditMessageDao.fetch(PurgeJobAuditMessage.class, id);
        return (null != purgeJobAuditMessage);
    }
}
