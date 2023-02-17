package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.model.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.api.CmcApi;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimStatusUpdate;

@Service("ClaimStatusUpdate")
public class ClaimStatusUpdateService implements IClaimStatusUpdate {

    private CmcApi cmcApi;

    private ClaimStatusUpdateRequest claimStatusUpdateRequestObj;

    @Autowired
    public ClaimStatusUpdateService(CmcApi cmcApi) {
        this.cmcApi = cmcApi;
    }
    @Override
    public Object claimStatusUpdate(ClaimStatusUpdateRequest claimStatusUpdateRequest, String idAmId, String sdtRequestId) {

        //Do stuff with the ClaimStatusUpdateObj checking etc
        this.claimStatusUpdateRequestObj = claimStatusUpdateRequest;

         return cmcApi.claimStatusUpdate("", "", idAmId, sdtRequestId, claimStatusUpdateRequestObj);

    }
}
