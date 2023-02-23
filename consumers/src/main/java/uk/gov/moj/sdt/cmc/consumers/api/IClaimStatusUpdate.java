package uk.gov.moj.sdt.cmc.consumers.api;

import uk.gov.moj.sdt.cmc.consumers.model.claimStatusUpdate.ClaimStatusUpdateRequest;

public interface IClaimStatusUpdate {

    void claimStatusUpdate(ClaimStatusUpdateRequest claimStatusUpdateRequestObj);
}
