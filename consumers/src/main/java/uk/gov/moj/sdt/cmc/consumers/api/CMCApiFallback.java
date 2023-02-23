package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;

@Component
public class CMCApiFallback implements CMCApi {

    private IBreathingSpace breathingSpace;

    public CMCApiFallback(@Qualifier("MockBreathingSpaceService") IBreathingSpace breathingSpace) {
        this.breathingSpace = breathingSpace;
    }

    @Override
    public BreathingSpaceResponse breathingSpace(BreathingSpaceRequest breathingSpaceRequest) {
        return this.breathingSpace.breathingSpace(breathingSpaceRequest);
    }
}
