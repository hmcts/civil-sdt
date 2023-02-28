package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.CMCApi;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimStatusUpdate;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimStatusUpdateResponse;

@Service("ClaimStatusUpdateService")
public class ClaimStatusUpdateService implements IClaimStatusUpdate {

    private CMCApi cmcApi;

    @Autowired
    public ClaimStatusUpdateService(CMCApi cmcApi) {
        this.cmcApi = cmcApi;
    }
    @Override
    public ClaimStatusUpdateResponse claimStatusUpdate(ClaimStatusUpdateRequest claimStatusUpdateRequest) {
        return cmcApi.claimStatusUpdate(claimStatusUpdateRequest);
    }
}
