package uk.gov.moj.sdt.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.moj.sdt.dao.api.ITargetApplicationDao;
import uk.gov.moj.sdt.dao.config.DaoTestConfig;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.test.utils.AbstractIntegrationTest;
import uk.gov.moj.sdt.test.utils.TestConfig;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test {@link TargetApplicationDao} query methods.
 *
 * @author Mark Dathorne
 */
@ActiveProfiles("integ")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TestConfig.class, DaoTestConfig.class})
@Sql(scripts = {"classpath:uk/gov/moj/sdt/dao/sql/TargetApplicationDaoTest.sql"})
@Transactional
public class TargetApplicationDaoTest extends AbstractIntegrationTest {

    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TargetApplicationDaoTest.class);
    CriteriaBuilder criteriaBuilder;
    CriteriaQuery<TargetApplication> criteriaQuery;
    Root<TargetApplication> root;

    private static final String EXISTING_TARGET_APPLICATION_CODE = "1104";
    private static final String NON_EXISTENT_TARGET_APPLICATION_CODE = "AX04";

    @Before
    public void setup() {
        final ITargetApplicationDao targetApplicationDao = this.applicationContext.getBean(ITargetApplicationDao.class);
        criteriaBuilder = targetApplicationDao.getEntityManager().getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery(TargetApplication.class);
        root = criteriaQuery.from(TargetApplication.class);
    }

    @Test
    void successfullyGetTargetApplicationForExistingCode() {
        final ITargetApplicationDao targetApplicationDao = this.applicationContext.getBean(ITargetApplicationDao.class);

        final ITargetApplication targetApplication = targetApplicationDao.getTargetApplicationByCode(EXISTING_TARGET_APPLICATION_CODE);
        if (targetApplication != null) {
            LOGGER.debug("Retrieved target application for code [{}]", targetApplication.getTargetApplicationCode());
        } else {
            fail("Could not find target application with code [" + EXISTING_TARGET_APPLICATION_CODE + "]");
        }
    }

    @Test
    void failToGetTargetApplicationForNonExistentCode() {
        final ITargetApplicationDao targetApplicationDao = this.applicationContext.getBean(ITargetApplicationDao.class);

        final ITargetApplication targetApplication = targetApplicationDao.getTargetApplicationByCode(NON_EXISTENT_TARGET_APPLICATION_CODE);
        if (targetApplication == null) {
            LOGGER.debug("No target application found for code [{}]", NON_EXISTENT_TARGET_APPLICATION_CODE);
        } else {
            fail("Found target application with code [" + NON_EXISTENT_TARGET_APPLICATION_CODE + "]");
        }
    }
}
