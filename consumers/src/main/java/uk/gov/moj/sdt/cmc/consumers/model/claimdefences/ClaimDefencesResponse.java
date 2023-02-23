package uk.gov.moj.sdt.cmc.consumers.model.claimdefences;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClaimDefencesResponse {
    private Integer resultCount;
    private ClaimDefencesResult[] results;
}
