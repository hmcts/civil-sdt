package uk.gov.moj.sdt.cmc.consumers.model.claimStatusUpdate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.moj.sdt.cmc.consumers.model.CMCRequest;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimStatusUpdateRequest extends CMCRequest {

    private String requestType;

    private String requestId;

    @JsonProperty(value = "mcolClaimStatusUpdate", access = JsonProperty.Access.WRITE_ONLY)
    private ClaimStatusUpdate claimStatusUpdate;

    public String getCaseManRef () {
        return claimStatusUpdate.getClaimNumber();
    }

    public String getRespondentId () {
        return claimStatusUpdate.getDefendantId();
    }

    public String getUpdateType () {
        return claimStatusUpdate.getNotificationType();
    }

    public String getPaidInFullDate () {
        return claimStatusUpdate.getPaidInFullDate();
    }

    public Boolean getSection38Compliancy () {
        return claimStatusUpdate.getSection38Compliancy();
    }
}
