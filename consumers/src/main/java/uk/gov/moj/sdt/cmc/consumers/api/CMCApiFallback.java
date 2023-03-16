package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.request.claim.ClaimRequest;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgementRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimStatusUpdateResponse;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponse;

@Component
public class CMCApiFallback implements CMCApi {

    private IBreathingSpaceService breathingSpace;

    private IJudgementService judgementService;

    private IClaimStatusUpdateService claimStatusUpdate;

    private IClaimRequestService claimRequestService;

    public CMCApiFallback(@Qualifier("MockBreathingSpaceService") IBreathingSpaceService breathingSpace,
                          @Qualifier("MockJudgementRequestService") IJudgementService judgementService,
                          @Qualifier("MockClaimStatusUpdateService") IClaimStatusUpdateService claimStatusUpdate,
                          @Qualifier("MockClaimRequestService") IClaimRequestService claimRequestService) {
        this.breathingSpace = breathingSpace;
        this.judgementService = judgementService;
        this.claimStatusUpdate = claimStatusUpdate;
        this.claimRequestService = claimRequestService;
    }

    @Override
    public BreathingSpaceResponse breathingSpace(String idamId, String sdtRequestId, BreathingSpaceRequest breathingSpaceRequest) {
        return breathingSpace.breathingSpace(idamId, sdtRequestId, breathingSpaceRequest);
    }

    @Override
    public JudgementResponse requestJudgment(String idamId,
                                             String sdtRequestId,
                                             JudgementRequest judgementRequest) {
        return judgementService.requestJudgment(idamId, sdtRequestId, judgementRequest);
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
