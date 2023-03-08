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

    private IClaimStatusUpdateService claimStatusUpdate;

    public CMCApiFallback(@Qualifier("MockBreathingSpaceService") IBreathingSpace breathingSpace,
                          @Qualifier("MockClaimStatusUpdateService") IClaimStatusUpdateService claimStatusUpdate) {
        this.breathingSpace = breathingSpace;
        this.claimStatusUpdate = claimStatusUpdate;
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
}
