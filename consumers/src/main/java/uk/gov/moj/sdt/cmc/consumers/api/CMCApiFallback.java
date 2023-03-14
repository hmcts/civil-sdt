package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimRequest;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimStatusUpdateResponse;

@Component
public class CMCApiFallback implements CMCApi {

    private IBreathingSpace breathingSpace;

    private IClaimStatusUpdateService claimStatusUpdate;

    private IClaimRequestService claimRequestService;

    public CMCApiFallback(@Qualifier("MockBreathingSpaceService") IBreathingSpace breathingSpace,
                          @Qualifier("MockClaimStatusUpdateService") IClaimStatusUpdateService claimStatusUpdate,
                          @Qualifier("MockClaimRequestService") IClaimRequestService claimRequestService) {
        this.breathingSpace = breathingSpace;
        this.claimStatusUpdate = claimStatusUpdate;
        this.claimRequestService = claimRequestService;
    }

    @Override
    public BreathingSpaceResponse breathingSpace(BreathingSpaceRequest breathingSpaceRequest) {
        return this.breathingSpace.breathingSpace(breathingSpaceRequest);
    }

    @Override
    public ClaimStatusUpdateResponse claimStatusUpdate(String idamId,
                                                       String sdtRequestId,
                                                       ClaimStatusUpdateRequest claimStatusUpdateRequest) {
        return this.claimStatusUpdate.claimStatusUpdate(idamId, sdtRequestId, claimStatusUpdateRequest);
    }

    @Override
    public ClaimResponse createSDTClaim(String idamId,
                                        String sdtRequestId,
                                        ClaimRequest claimRequest) {
        return this.claimRequestService.claimRequest(idamId, sdtRequestId, claimRequest);
    }
}
