package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.request.WarrantRequest;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgementRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimStatusUpdateResponse;
import uk.gov.moj.sdt.cmc.consumers.response.WarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponse;

@Component
public class CMCApiFallback implements CMCApi {

    private IBreathingSpaceService breathingSpace;

    private IJudgementService judgementService;

    private IClaimDefencesService claimDefences;

    private IClaimStatusUpdateService claimStatusUpdate;

    private IWarrantService warrantService;

    public CMCApiFallback(@Qualifier("MockBreathingSpaceService") IBreathingSpaceService breathingSpace,
                          @Qualifier("MockClaimDefencesService") IClaimDefencesService claimDefences,
                          @Qualifier("MockJudgementRequestService") IJudgementService judgementService,
                          @Qualifier("MockClaimStatusUpdateService") IClaimStatusUpdateService claimStatusUpdate) {
        this.breathingSpace = breathingSpace;
        this.claimDefences = claimDefences;
        this.judgementService = judgementService;
        this.claimStatusUpdate = claimStatusUpdate;
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
    public ClaimDefencesResponse claimDefences(String authorization, String serviceAuthorization,
                                             String idamId, String fromDate, String toDate) {
        return claimDefences.claimDefences(authorization, serviceAuthorization, idamId, fromDate, toDate);
    }

    @Override
    public WarrantResponse warrantRequest(String authorization,
                                          String serviceAuthorization,
                                          String idamId,
                                          String sdtRequestId,
                                          WarrantRequest warrantRequest) {
        return warrantService.warrantRequest(authorization, serviceAuthorization, idamId, sdtRequestId, warrantRequest);
    }

}
