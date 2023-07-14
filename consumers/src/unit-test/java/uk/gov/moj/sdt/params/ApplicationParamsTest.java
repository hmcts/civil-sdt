package uk.gov.moj.sdt.params;

import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationParamsTest extends AbstractSdtUnitTestBase {

    private static final String SDT_SYSTEM_USER_ID = "Test SDT system user id";
    private static final String SDT_SYSTEM_USER_PASSWORD = "Test SDT system user password";

    private ApplicationParams applicationParams;

    @Override
    protected void setUpLocalTests() {
        applicationParams = new ApplicationParams();
    }
    @Test
    void testGetSdtSystemUserId() {
        setAccessibleField(ApplicationParams.class,
                           "sdtSystemUserId",
                           String.class,
                           applicationParams,
                           SDT_SYSTEM_USER_ID);

        assertEquals(SDT_SYSTEM_USER_ID,
                     applicationParams.getSdtSystemUserId(),
                     "ApplicationParams has unexpected SDT system user id");
    }

    @Test
    void testGetSdtSystemUserPassword() {
        setAccessibleField(ApplicationParams.class,
                           "sdtSystemUserPassword",
                           String.class,
                           applicationParams,
                           SDT_SYSTEM_USER_PASSWORD);

        assertEquals(SDT_SYSTEM_USER_PASSWORD,
                     applicationParams.getSdtSystemUserPassword(),
                     "ApplicationParams has unexpected SDT system user password");
    }
}
