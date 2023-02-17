package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.model.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.model.ClaimStatusUpdateRequest;

@Component
public class CmcApiFallback implements CmcApi {

    private IBreathingSpace breathingSpace;

    private IClaimDefences claimDefences;

    private IClaimStatusUpdate claimStatusUpdateService;

    public CmcApiFallback(@Qualifier("MockBreathingSpaceService") IBreathingSpace breathingSpace,
                          @Qualifier("MockClaimDefencesService") IClaimDefences claimDefences,
                          @Qualifier("MockClaimStatusUpdateService") IClaimStatusUpdate claimStatusUpdateService) {
        this.breathingSpace = breathingSpace;
        this.claimDefences = claimDefences;
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
                                    String idAmId, String sdtRequestId,
                                    ClaimStatusUpdateRequest claimStatusUpdateRequestObj) {
        return claimStatusUpdateService.claimStatusUpdate(claimStatusUpdateRequestObj, idAmId, sdtRequestId);
    }

    @Override
    public Object claimDefences(String authorisation,
                                String serviceAuthorization,
                                String idamId,
                                String fromDate,
                                String toDate) {
        return claimDefences.claimDefences(fromDate, toDate);
    }
}
