package uk.gov.moj.sdt.cmc.consumers.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "caseManRef",
    "respondentId",
    "updateType",
    "paidInFullDate",
    "section38Compliancy"

})
public class ClaimStatusUpdateRequest {

    private String claimNumber;

    private String defendantId;

    private String notificationType;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date paidInFullDate;

    private boolean section38Compliancy;

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public void setDefendantId(String defendantId) {
        this.defendantId = defendantId;
    }

    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }

    public void setPaidInFullDate(Date paidInFullDate) { this.paidInFullDate = paidInFullDate; }

    public void setSection38Compliancy(boolean section38Compliancy) { this.section38Compliancy = section38Compliancy; }

    public String getCaseManRef() {
        return claimNumber;
    }

    public String getRespondentId() {
        return defendantId;
    }

    public String getUpdateType() {
        return notificationType;
    }

    public Date getPaidInFullDate() { return paidInFullDate; }

    public boolean getSection38Compliancy() { return section38Compliancy; }
}
