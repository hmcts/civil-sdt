package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimStatusUpdateResponse;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;

@Component
public class CMCApiFallback implements CMCApi {

    private IBreathingSpace breathingSpace;

    private IClaimStatusUpdate claimStatusUpdate;

    public CMCApiFallback(@Qualifier("MockBreathingSpaceService") IBreathingSpace breathingSpace,
                          @Qualifier("MockClaimStatusUpdateService") IClaimStatusUpdate claimStatusUpdate) {
        this.breathingSpace = breathingSpace;
        this.claimStatusUpdate = claimStatusUpdate;
    }

    @Override
    public BreathingSpaceResponse breathingSpace(BreathingSpaceRequest breathingSpaceRequest) {
        return this.breathingSpace.breathingSpace(breathingSpaceRequest);
    }

    @Override
    public ClaimStatusUpdateResponse claimStatusUpdate(ClaimStatusUpdateRequest claimStatusUpdateRequest) {
        return this.claimStatusUpdate.claimStatusUpdate(claimStatusUpdateRequest);
    }
}
