package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.CMCApi;
import uk.gov.moj.sdt.cmc.consumers.model.claimStatusUpdate.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClaimStatusUpdateServiceTest extends AbstractSdtUnitTestBase {

    @Mock
    private CMCApi cmcApi;

    private ClaimStatusUpdateService claimStatusUpdateService;

    @BeforeEach
    @Override
    public void setUp() {
        claimStatusUpdateService = new ClaimStatusUpdateService(cmcApi);
    }

    @Test
    void getClient() {
        ClaimStatusUpdateRequest claimStatusUpdateRequest = mock(ClaimStatusUpdateRequest.class);
        claimStatusUpdateService.claimStatusUpdate(claimStatusUpdateRequest);
        verify(cmcApi).claimStatusUpdate(anyString(), anyString(), any(ClaimStatusUpdateRequest.class));
    }

}
