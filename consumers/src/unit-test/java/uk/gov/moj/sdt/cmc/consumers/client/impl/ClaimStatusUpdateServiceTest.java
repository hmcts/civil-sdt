package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimStatusUpdateResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClaimStatusUpdateServiceTest extends CMCConsumersClientTestBase {

    private ClaimStatusUpdateService claimStatusUpdateService;

    @Mock
    ClaimStatusUpdateRequest mockClaimStatusUpdateRequest;

    @Mock
    ClaimStatusUpdateResponse mockClaimStatusUpdateResponse;

    @Override
    protected void setUpLocalTests() {
        claimStatusUpdateService = new ClaimStatusUpdateService(mockCmcApi);
    }

    @Test
    void getClient() {
        when(mockCmcApi.claimStatusUpdate(IDAM_ID_HEADER, SDT_REQUEST_ID, mockClaimStatusUpdateRequest))
            .thenReturn(mockClaimStatusUpdateResponse);

        ClaimStatusUpdateResponse claimStatusUpdateResponse =
            claimStatusUpdateService.claimStatusUpdate(IDAM_ID_HEADER, SDT_REQUEST_ID, mockClaimStatusUpdateRequest);

        assertNotNull(claimStatusUpdateResponse, "ClaimStatusUpdateResponse should not be null");
        assertEquals(mockClaimStatusUpdateResponse,
                     claimStatusUpdateResponse,
                     "Unexpected ClaimStatusUpdateResponse returned");

        verify(mockCmcApi).claimStatusUpdate(IDAM_ID_HEADER, SDT_REQUEST_ID, mockClaimStatusUpdateRequest);
    }

}
