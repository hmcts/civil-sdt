package uk.gov.moj.sdt.cmc.consumers.request.claim;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.gov.moj.sdt.cmc.consumers.request.common.SotSignature;

import java.util.Date;
import java.util.List;

@ToString
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "bulkCustomerId",
    "claimantReference",
    "claimant",
    "defendant1",
    "defendant2",
    "sendParticularsSeparately",
    "particulars",
    "reserveRightToClaimInterest",
    "interestDailyAmount",
    "interestOwedDate",
    "interestClaimDate",
    "claimAmountInterestBase",
    "claimAmount",
    "solicitorCost",
    "statementOfTruthName"
})
public class ClaimRequest {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIMEZONE_EUROPE_LONDON = "Europe/London";

    @Getter
    private String bulkCustomerId;

    @Getter
    private String claimantReference;

    @JsonProperty(value = "applicant")
    @JsonAlias(value = "claimant")
    @Getter
    private Claimant claimant;

    @JsonProperty(value = "respondent1")
    @JsonAlias(value = "defendant1")
    @Getter
    private Defendant defendant1;

    @JsonProperty(value = "respondent2")
    @JsonAlias(value = "defendant2")
    @Getter
    private Defendant defendant2;

    @Getter
    private Boolean sendParticularsSeparately;

    @JsonProperty(value = "claimInterest")
    @JsonAlias(value = "reserveRightToClaimInterest")
    @Getter
    private Boolean reserveRightToClaimInterest;

    private Interest interest;

    @Getter
    private Long claimAmount;

    @Getter
    private Long solicitorCost;

    @JsonProperty(value = "particularsLines")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "particulars")
    @Getter
    private List<String> particulars;

    private SotSignature sotSignature;

    public Long getInterestDailyAmount() {
        return interest == null ? null : interest.getDailyAmount();
    }

    @JsonFormat(pattern = DATE_FORMAT, timezone = TIMEZONE_EUROPE_LONDON)
    public Date getInterestOwedDate() {
        return interest == null ? null : interest.getOwedDate();
    }

    @JsonFormat(pattern = DATE_FORMAT, timezone = TIMEZONE_EUROPE_LONDON)
    public Date getInterestClaimDate() {
        return interest == null ? null : interest.getClaimDate();
    }

    public Long getClaimAmountInterestBase() {
        return interest == null ? null : interest.getClaimAmountInterestBase();
    }

    public String getStatementOfTruthName() {
        return sotSignature == null ? "" : sotSignature.getName();
    }
}
