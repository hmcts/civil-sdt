package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgementRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponse;

@Component
public class CMCApiFallback implements CMCApi {

    private IBreathingSpace breathingSpace;

    private IJudgement judgementService;

    public CMCApiFallback(@Qualifier("MockBreathingSpaceService") IBreathingSpace breathingSpace,
                          @Qualifier("MockJudgementRequestService") IJudgement judgementService) {
        this.breathingSpace = breathingSpace;
        this.judgementService = judgementService;
    }

    @Override
    public BreathingSpaceResponse breathingSpace(BreathingSpaceRequest breathingSpaceRequest) {
        return breathingSpace.breathingSpace(breathingSpaceRequest);
    }

    @Override
    public JudgementResponse requestJudgment(String idamId,
                                             String sdtRequestId,
                                             JudgementRequest judgementRequest) {
        return judgementService.requestJudgment(idamId, sdtRequestId, judgementRequest);
    }
}
