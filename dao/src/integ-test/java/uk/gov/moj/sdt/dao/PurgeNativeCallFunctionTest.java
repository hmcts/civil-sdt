package uk.gov.moj.sdt.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.moj.sdt.dao.config.DaoTestConfig;
import uk.gov.moj.sdt.test.utils.AbstractIntegrationTest;
import uk.gov.moj.sdt.test.utils.TestConfig;

/**
 * Test {@link PurgeNativeCallFunction} query methods.
 *
 * @author Mark Dathorne
 */
@ActiveProfiles("integ")
@SpringBootTest(classes = { TestConfig.class, DaoTestConfig.class})
//@Sql(scripts = {"classpath:uk/gov/moj/sdt/dao/sql/PurgeDaoTest.sql"})
@Transactional
class PurgeNativeCallFunctionTest extends AbstractIntegrationTest {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PurgeNativeCallFunctionTest.class);

    private PurgeNativeCallFunction purgeNativeCallFunction;

    @BeforeEach
    public void setup() {
        purgeNativeCallFunction = this.applicationContext.getBean(PurgeNativeCallFunction.class);
    }

    /**
     * Tests {@link GenericDao} fetch.
     */
    @Test
    void runPurge() {
        LOGGER.info("TEST STARTING: run the Purge");

        Integer commitInterval = 1000;

        purgeNativeCallFunction.executePurgeStoredProc(commitInterval);

        LOGGER.info("TEST COMPLETED: the Purge has been run");
    }
}
