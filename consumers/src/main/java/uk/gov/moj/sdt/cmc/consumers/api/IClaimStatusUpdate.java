package uk.gov.moj.sdt.cmc.consumers.api;

import uk.gov.moj.sdt.cmc.consumers.model.ClaimStatusUpdateRequest;

public interface IClaimStatusUpdate {

    Object claimStatusUpdate(ClaimStatusUpdateRequest claimStatusUpdateRequestObj, String idAmId, String sdtRequestId);
}
