package uk.gov.moj.sdt.cmc.consumers.model;

import lombok.Getter;
import lombok.ToString;
import uk.gov.moj.sdt.domain.RequestType;

@ToString
@Getter
public class ClaimStatusUpdateRequest extends CMCRequest {

    private String caseManRef;

    private String respondantId;

    private String updateType;

    private String paidInFullDate;

    private boolean section38Compliancy;

    public ClaimStatusUpdateRequest(String caseManRef,
                                    String respondantId,
                                    String updateType,
                                    String paidInFullDate,
                                    boolean section38Compliancy) {
        this.caseManRef = caseManRef;
        this.respondantId = respondantId;
        this.updateType = updateType;
        this.paidInFullDate = paidInFullDate;
        this.section38Compliancy = section38Compliancy;
        setRequestTypeName(RequestType.CLAIM_STATUS_UPDATE);
    }
}
