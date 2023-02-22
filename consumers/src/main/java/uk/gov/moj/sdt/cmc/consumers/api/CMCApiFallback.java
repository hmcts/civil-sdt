package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.model.breathingspace.BreathingSpace;

@Component
public class CMCApiFallback implements CMCApi {

    private IBreathingSpace breathingSpace;

    public CMCApiFallback(@Qualifier("MockBreathingSpaceService") IBreathingSpace breathingSpace) {
        this.breathingSpace = breathingSpace;
    }

    @Override
    public void breathingSpace(String authorisation,
                               String serviceAuthorization,
                               BreathingSpace breathingSpace) {
        this.breathingSpace.breathingSpace(breathingSpace);
    }
}
