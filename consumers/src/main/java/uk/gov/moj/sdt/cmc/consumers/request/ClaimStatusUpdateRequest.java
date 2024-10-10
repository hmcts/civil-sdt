package uk.gov.moj.sdt.cmc.consumers.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
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

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIMEZONE_EUROPE_LONDON = "Europe/London";

    private String claimNumber;

    private String defendantId;

    private UpdateType notificationType;

    @JsonFormat(pattern = DATE_FORMAT, timezone = TIMEZONE_EUROPE_LONDON)
    private Date paidInFullDate;

    private Boolean section38Compliancy;

    public String getCaseManRef() {
        return claimNumber;
    }

    public String getRespondentId() {
        return defendantId;
    }

    public UpdateType getUpdateType() {
        return notificationType;
    }

    public Date getPaidInFullDate() { return paidInFullDate; }

    public Boolean getSection38Compliancy() { return section38Compliancy; }
}
