package uk.gov.moj.sdt.cmc.consumers.model.claimdefences;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClaimDefencesResult {
    private String caseManRef;
    private String respondentId;
    private String defendantResponseFiledDate;
    private String defendantResponseCreatedDate;
    private String responseType;
    private String defence;
}
