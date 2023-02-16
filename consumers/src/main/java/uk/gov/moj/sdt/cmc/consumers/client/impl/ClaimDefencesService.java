package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.CmcApi;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimDefences;
import uk.gov.moj.sdt.cmc.consumers.model.ICmcRequest;


@Service("ClaimDefencesService")
public class ClaimDefencesService implements IClaimDefences {

    private CmcApi cmcApi;

    @Autowired
    public ClaimDefencesService(CmcApi cmcApi) {
        this.cmcApi = cmcApi;
    }

    @Override
    public Object claimDefences(ICmcRequest cmcRequest) {
        return cmcApi.claimDefences("", "", cmcRequest);
    }
}