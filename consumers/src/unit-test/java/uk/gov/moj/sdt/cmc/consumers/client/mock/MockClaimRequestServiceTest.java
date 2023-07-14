package uk.gov.moj.sdt.cmc.consumers.client.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.request.claim.ClaimRequest;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MockClaimRequestServiceTest {

    private MockClaimRequestService mockClaimRequestService;

    @BeforeEach
    void setUp() {
        mockClaimRequestService = new MockClaimRequestService();
    }

    @Test
    void shouldReturnMockClaimRequestResponse() {
        ClaimRequest claimRequest = new ClaimRequest();

        ClaimResponse claimResponse = mockClaimRequestService.claimRequest("testIdamId",
                                                                           "MCOL-20230713091446-000000001-0000001",
                                                                           claimRequest);

        assertNotNull(claimResponse, "Claim response should not be null");
    }
}
