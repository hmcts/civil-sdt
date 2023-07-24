package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.request.claim.ClaimRequest;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClaimRequestServiceTest extends CMCConsumersClientTestBase {

    private ClaimRequestService claimRequestService;

    @Mock
    private ClaimRequest mockClaimRequest;

    @Mock
    private ClaimResponse mockClaimResponse;

    @Override
    protected void setUpLocalTests() {
        claimRequestService = new ClaimRequestService(mockCmcApi);
    }

    @Test
    void testClaimRequest() {
        when(mockCmcApi.createSDTClaim(IDAM_ID_HEADER, SDT_REQUEST_ID, mockClaimRequest)).thenReturn(mockClaimResponse);

        ClaimResponse claimResponse = claimRequestService.claimRequest(IDAM_ID_HEADER, SDT_REQUEST_ID, mockClaimRequest);

        assertNotNull(claimResponse, "ClaimResponse should not be null");
        assertSame(mockClaimResponse, claimResponse, "Unexpected ClaimResponse object returned");

        verify(mockCmcApi).createSDTClaim(IDAM_ID_HEADER, SDT_REQUEST_ID, mockClaimRequest);
    }

}
