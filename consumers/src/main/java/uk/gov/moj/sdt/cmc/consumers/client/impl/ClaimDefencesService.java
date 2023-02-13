package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.CmcApi;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimDefences;


@Service("ClaimDefencesService")
public class ClaimDefencesService implements IClaimDefences {

    private CmcApi cmcApi;

    @Override
    public Object claimDefences(String idAmId, String fromDateTime, String toDateTime) {
        return cmcApi.claimDefences("", "", idAmId, fromDateTime, toDateTime);
    }
}
