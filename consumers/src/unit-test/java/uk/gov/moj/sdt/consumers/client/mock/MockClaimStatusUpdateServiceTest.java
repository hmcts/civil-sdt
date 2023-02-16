package uk.gov.moj.sdt.consumers.client.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.ClaimStatusUpdate;
import uk.gov.moj.sdt.cmc.consumers.client.mock.MockClaimStatusUpdateService;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class MockClaimStatusUpdateServiceTest extends AbstractSdtUnitTestBase {

    private MockClaimStatusUpdateService mockClaimStatusUpdateService;

    private ClaimStatusUpdate claimStatusUpdateObj;

    @BeforeEach
    @Override
    public void setUp() {

        mockClaimStatusUpdateService = new MockClaimStatusUpdateService();

        claimStatusUpdateObj = new ClaimStatusUpdate();
        claimStatusUpdateObj.setUpdateType("WD");
        claimStatusUpdateObj.setCaseManRef("CaseManRef0101");
        claimStatusUpdateObj.setPaidInFullDate("01/10/2021");
        claimStatusUpdateObj.setRespondantId("1");
        claimStatusUpdateObj.setSection38Compliancy(true);
    }

    @Test
    void getClient() {
        final String idAmId = "";
        final String sdtRequestId = "";

        Object returnValue = mockClaimStatusUpdateService.claimStatusUpdate(claimStatusUpdateObj, idAmId, sdtRequestId);

        assertNull(returnValue);
    }
}
