package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.model.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;

@Component
public class CMCApiFallback implements CMCApi {

    private IBreathingSpace breathingSpace;
    private IClaimDefences claimDefences;
    private IClaimStatusUpdate claimStatusUpdate;

    public CMCApiFallback(@Qualifier("MockBreathingSpaceService") IBreathingSpace breathingSpace,
                          @Qualifier("MockClaimDefencesService") IClaimDefences claimDefences,
                          @Qualifier("MockClaimStatusUpdateService") IClaimStatusUpdate claimStatusUpdate) {
        this.breathingSpace = breathingSpace;
        this.claimDefences = claimDefences;
        this.claimStatusUpdate = claimStatusUpdate;
    }

    @Override
    public BreathingSpaceResponse breathingSpace(BreathingSpaceRequest breathingSpaceRequest) {
        return this.breathingSpace.breathingSpace(breathingSpaceRequest);
    }

    @Override
    public Object claimStatusUpdate(String authorisation,
                                    String serviceAuthorization,
                                    String idAmId, String sdtRequestId,
                                    ClaimStatusUpdateRequest claimStatusUpdateRequestObj) {
        return claimStatusUpdate.claimStatusUpdate(claimStatusUpdateRequestObj, idAmId, sdtRequestId);
    }

    @Override
    public ClaimDefencesResponse claimDefences(String authorisation,
                                               String serviceAuthorization,
                                               String idamId,
                                               String fromDate,
                                               String toDate) {
        return claimDefences.claimDefences(fromDate, toDate);
    }
}
