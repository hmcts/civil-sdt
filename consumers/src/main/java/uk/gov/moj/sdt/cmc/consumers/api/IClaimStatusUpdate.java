package uk.gov.moj.sdt.cmc.consumers.api;

public interface IClaimStatusUpdate {

    Object claimStatusUpdate(ClaimStatusUpdate claimStatusUpdateObj,String idAmId, String sdtRequestId);
}
