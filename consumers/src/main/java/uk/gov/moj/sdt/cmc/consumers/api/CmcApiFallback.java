package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.model.BreathingSpaceRequest;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;

@Component
public class CmcApiFallback implements CmcApi {

    private IBreathingSpace breathingSpace;

    public CmcApiFallback(@Qualifier("MockBreathingSpaceService") IBreathingSpace breathingSpace) {
        this.breathingSpace = breathingSpace;
    }

    @Override
    public void breathingSpace(String authorisation,
                                 String serviceAuthorization,
                                 BreathingSpaceRequest breathingSpaceRequest) {
        breathingSpace.breathingSpace(breathingSpaceRequest);
    }
}
