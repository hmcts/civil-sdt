package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.model.claimStatusUpdate.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.model.breathingspace.BreathingSpaceRequest;

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
    public void breathingSpace(String authorisation,
                                 String serviceAuthorization,
                                 BreathingSpaceRequest breathingSpaceRequest) {
        breathingSpace.breathingSpace(breathingSpaceRequest);
    }

    @Override
    public void claimStatusUpdate(String authorisation,
                                  String serviceAuthorization,
                                   ClaimStatusUpdateRequest claimStatusUpdateRequestObj) {
        claimStatusUpdate.claimStatusUpdate(claimStatusUpdateRequestObj);
    }
}
