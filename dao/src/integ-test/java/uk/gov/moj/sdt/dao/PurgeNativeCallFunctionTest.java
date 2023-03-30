package uk.gov.moj.sdt.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
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
@Sql(scripts = {"classpath:db/migration/V0002__purge.sql",
        "classpath:uk/gov/moj/sdt/dao/sql/PurgeDaoTest.sql"})
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

        purgeNativeCallFunction.executePurgeStoredProc(1000);

        LOGGER.info("TEST COMPLETED: the Purge has been run");
    }
}
