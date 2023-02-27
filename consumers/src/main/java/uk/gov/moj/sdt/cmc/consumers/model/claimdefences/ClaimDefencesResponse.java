package uk.gov.moj.sdt.cmc.consumers.model.claimdefences;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimDefencesResponse {
    private Integer resultCount;
    private ClaimDefencesResult[] results;
}
