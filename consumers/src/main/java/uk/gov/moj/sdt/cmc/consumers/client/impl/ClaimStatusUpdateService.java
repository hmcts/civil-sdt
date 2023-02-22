package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.CMCApi;
import uk.gov.moj.sdt.cmc.consumers.model.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimStatusUpdate;

@Service("ClaimStatusUpdateService")
public class ClaimStatusUpdateService implements IClaimStatusUpdate {

    private CMCApi cmcApi;

    private ClaimStatusUpdateRequest claimStatusUpdateRequestObj;

    @Autowired
    public ClaimStatusUpdateService(CMCApi cmcApi) {
        this.cmcApi = cmcApi;
    }
    @Override
    public Object claimStatusUpdate(ClaimStatusUpdateRequest claimStatusUpdateRequest, String idAmId, String sdtRequestId) {

        //Do stuff with the ClaimStatusUpdateObj checking etc
        this.claimStatusUpdateRequestObj = claimStatusUpdateRequest;

         return cmcApi.claimStatusUpdate("", "", idAmId, sdtRequestId, claimStatusUpdateRequestObj);

    }
}
