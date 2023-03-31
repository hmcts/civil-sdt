package uk.gov.moj.sdt.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.dao.PurgeNativeCallFunction;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PurgeServiceTest extends AbstractSdtUnitTestBase {

    private PurgeService purgeService;
    @Mock
    PurgeNativeCallFunction purgeNativeCallFunction;

    @BeforeEach
    @Override
    public void setUp() {
        purgeService = new PurgeService(purgeNativeCallFunction);
    }

    @Test
    void shouldPerformTransactionalPurge() {
        purgeService.transactPurgeProc();
        assertTrue(true, "Expected to pass");
        verify(purgeNativeCallFunction).executePurgeStoredProc(anyInt());
    }

    @Test
    void shouldRetrieveDefaultCommitInterval() {
        Integer expectedValue = 500;
        Integer commitInterval =  purgeService.getCommitInterval();
        assertEquals(expectedValue, commitInterval, "commitInterval parameter is not expected value");
    }
}
