package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;

@Component
public class CmcApiFallback implements CmcApi {

    private IBreathingSpace breathingSpace;

    public CmcApiFallback(@Qualifier("MockBreathingSpaceService") IBreathingSpace breathingSpace) {
        this.breathingSpace = breathingSpace;
    }

    @Override
    public Object breathingSpace(String authorisation,
                                 String serviceAuthorization,
                                 IIndividualRequest individualRequest) {
        return breathingSpace.breathingSpace(individualRequest);
    }
}
