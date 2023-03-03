package uk.gov.moj.sdt.cmc.consumers.client.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class MockClaimStatusUpdateServiceTest extends AbstractSdtUnitTestBase {

    private MockClaimStatusUpdateService mockClaimStatusUpdateService;

    @BeforeEach
    @Override
    public void setUp() {
        mockClaimStatusUpdateService = new MockClaimStatusUpdateService();
    }

    @Test
    void getClient() {
        ClaimStatusUpdateRequest claimStatusUpdateRequest = mock(ClaimStatusUpdateRequest.class);
        mockClaimStatusUpdateService.claimStatusUpdate(claimStatusUpdateRequest);
    }
}
