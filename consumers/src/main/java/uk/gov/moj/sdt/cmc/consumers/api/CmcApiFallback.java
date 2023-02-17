package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.model.BreathingSpaceRequest;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;

@Component
public class CmcApiFallback implements CmcApi {

    private IBreathingSpace breathingSpace;

    private IClaimStatusUpdate claimStatusUpdateService;

    public CmcApiFallback(@Qualifier("MockBreathingSpaceService") IBreathingSpace breathingSpace,
                          @Qualifier("MockClaimStatusUpdateService") IClaimStatusUpdate claimStatusUpdateService) {
        this.breathingSpace = breathingSpace;
        this.claimStatusUpdateService = claimStatusUpdateService;
    }

    @Override
    public void breathingSpace(String authorisation,
                                 String serviceAuthorization,
                                 BreathingSpaceRequest breathingSpaceRequest) {
        breathingSpace.breathingSpace(breathingSpaceRequest);
    }

    @Override
    public Object claimStatusUpdate(String authorisation,
                                  String serviceAuthorization,
                                  String idAmId, String sdtRequestId, ClaimStatusUpdate claimStatusUpdateObj) {
        return claimStatusUpdateService.claimStatusUpdate(claimStatusUpdateObj, idAmId, sdtRequestId);
    }
}
