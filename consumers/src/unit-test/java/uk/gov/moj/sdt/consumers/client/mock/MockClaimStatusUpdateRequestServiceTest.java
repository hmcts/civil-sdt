package uk.gov.moj.sdt.consumers.client.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.client.mock.MockClaimStatusUpdateService;
import uk.gov.moj.sdt.cmc.consumers.model.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class MockClaimStatusUpdateRequestServiceTest extends AbstractSdtUnitTestBase {

    private MockClaimStatusUpdateService mockClaimStatusUpdateService;

    private ClaimStatusUpdateRequest claimStatusUpdateRequestObj;

    @BeforeEach
    @Override
    public void setUp() {

        mockClaimStatusUpdateService = new MockClaimStatusUpdateService();

        claimStatusUpdateRequestObj = new ClaimStatusUpdateRequest(
            "CaseManRef0101",
            "1",
            "WD",
            "01/10/2021",
            true
        );
    }

    @Test
    void getClient() {
        final String idAmId = "";
        final String sdtRequestId = "";

        Object returnValue = mockClaimStatusUpdateService.claimStatusUpdate(claimStatusUpdateRequestObj, idAmId, sdtRequestId);

        assertNull(returnValue);
    }
}
