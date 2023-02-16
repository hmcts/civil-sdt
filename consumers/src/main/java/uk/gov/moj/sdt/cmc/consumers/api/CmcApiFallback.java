package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.model.ICmcRequest;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;

@Component
public class CmcApiFallback implements CmcApi {

    private IBreathingSpace breathingSpace;

    private IClaimDefences claimDefences;

    public CmcApiFallback(@Qualifier("MockBreathingSpaceService") IBreathingSpace breathingSpace,
                          @Qualifier("MockClaimDefencesService") IClaimDefences claimDefences) {
        this.breathingSpace = breathingSpace;
        this.claimDefences = claimDefences;
    }

    @Override
    public Object breathingSpace(String authorisation,
                                 String serviceAuthorization,
                                 IIndividualRequest individualRequest) {
        return breathingSpace.breathingSpace(individualRequest);
    }

    @Override
    public Object claimDefences(String authorisation,
                                String serviceAuthorization,
                                ICmcRequest cmcRequest) {
        return claimDefences.claimDefences(cmcRequest);
    }
}
