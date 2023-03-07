package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgementRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponse;

@Component
public class CMCApiFallback implements CMCApi {

    private IBreathingSpaceService breathingSpace;

    private IJudgementService judgementService;

    public CMCApiFallback(@Qualifier("MockBreathingSpaceService") IBreathingSpaceService breathingSpace,
                          @Qualifier("MockJudgementRequestService") IJudgementService judgementService) {
        this.breathingSpace = breathingSpace;
        this.judgementService = judgementService;
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
}
