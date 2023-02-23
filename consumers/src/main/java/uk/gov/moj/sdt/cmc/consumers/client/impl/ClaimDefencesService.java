package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.CMCApi;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimDefences;


@Service("ClaimDefencesService")
public class ClaimDefencesService implements IClaimDefences {

    private final CMCApi cmcApi;

    @Autowired
    public ClaimDefencesService(CMCApi cmcApi) {
        this.cmcApi = cmcApi;
    }

    @Override
    public Object claimDefences(String fromDate,
                                String toDate) {
        return cmcApi.claimDefences("", "",
                                    "",
                                    fromDate,
                                    toDate);
    }
}
