package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.CmcApi;
import uk.gov.moj.sdt.cmc.consumers.api.IBreathingSpace;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;


@Service("BreathingSpaceService")
public class BreathingSpaceService implements IBreathingSpace {

    private CmcApi cmcApi;

    @Override
    public Object breathingSpace(IIndividualRequest individualRequest) {
        return cmcApi.breathingSpace("", "", individualRequest);
    }
}