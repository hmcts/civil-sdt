package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.CMCApi;
import uk.gov.moj.sdt.cmc.consumers.model.claimStatusUpdate.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimStatusUpdate;

@Service("ClaimStatusUpdateService")
public class ClaimStatusUpdateService implements IClaimStatusUpdate {

    private CMCApi cmcApi;

    @Autowired
    public ClaimStatusUpdateService(CMCApi cmcApi) {
        this.cmcApi = cmcApi;
    }
    @Override
    public void claimStatusUpdate(ClaimStatusUpdateRequest claimStatusUpdateRequest) {


         cmcApi.claimStatusUpdate("", "", claimStatusUpdateRequest);

    }
}
