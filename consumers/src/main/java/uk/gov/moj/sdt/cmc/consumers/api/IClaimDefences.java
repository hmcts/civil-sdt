package uk.gov.moj.sdt.cmc.consumers.api;

import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;

public interface IClaimDefences {

    ClaimDefencesResponse claimDefences(String fromDate, String toDate);
}
