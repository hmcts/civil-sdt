package uk.gov.moj.sdt.cmc.consumers.request.judgement;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.gov.moj.sdt.cmc.consumers.request.common.Address;
import uk.gov.moj.sdt.cmc.consumers.request.common.SotSignature;

import java.util.Date;

@ToString
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "caseManRef",
    "jointJudgment",
    "judgmentType",
    "sentParticularsSeparately",
    "respondentId",
    "respondent1Address",
    "respondent1DOB",
    "respondent2Address",
    "respondent2DOB",
    "paymentSchedule",
    "interest",
    "solicitorCost",
    "deductedAmount",
    "claimAmountAdmitted",
    "courtFee",
    "legalCosts",
    "payee",
    "sotName"
})
public class JudgementRequest {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIMEZONE_EUROPE_LONDON = "Europe/London";

    private String claimNumber;

    private Boolean jointJudgment;

    private JudgmentType judgmentType;

    private Boolean sentParticularsSeparately;

    private String defendantId;

    private Address defendant1Address;

    @JsonProperty(value = "respondent1DOB")
    @JsonAlias(value = "defendant1DateOfBirth")
    @JsonFormat(pattern = DATE_FORMAT, timezone = TIMEZONE_EUROPE_LONDON)
    private Date defendant1DateOfBirth;

    private Address defendant2Address;

    @JsonProperty(value = "respondent2DOB")
    @JsonAlias(value = "defendant2DateOfBirth")
    @JsonFormat(pattern = DATE_FORMAT, timezone = TIMEZONE_EUROPE_LONDON)
    private Date defendant2DateOfBirth;

    private PaymentSchedule paymentSchedule;

    private Long interest;

    private Long solicitorCost;

    private Long deductedAmount;

    private Long claimAmountAdmitted;

    private Long courtFee;

    private Long legalCosts;

    private Payee payee;

    private SotSignature sotSignature;

    public String getCaseManRef() {
        return claimNumber;
    }

    public Boolean isJointJudgment() {
        return jointJudgment;
    }

    public JudgmentType getJudgmentType() {
        return judgmentType;
    }

    public Boolean isSentParticularsSeparately() {
        return sentParticularsSeparately;
    }

    public String getRespondentId() {
        return defendantId;
    }

    public Address getRespondent1Address() {
        return defendant1Address;
    }

    public Date getRespondent1DOB() {
        return defendant1DateOfBirth;
    }

    public Address getRespondent2Address() {
        return defendant2Address;
    }

    public Date getRespondent2DOB() {
        return defendant2DateOfBirth;
    }

    public PaymentSchedule getPaymentSchedule() {
        return paymentSchedule;
    }

    public Long getInterest() {
        return interest;
    }

    public Long getSolicitorCost() {
        return solicitorCost;
    }

    public Long getDeductedAmount() {
        return deductedAmount;
    }

    public Long getClaimAmountAdmitted() {
        return claimAmountAdmitted;
    }

    public Long getCourtFee() {
        return courtFee;
    }

    public Long getLegalCosts() {
        return legalCosts;
    }

    public Payee getPayee() {
        return payee;
    }

    public String getSotName() {
        return sotSignature != null ? sotSignature.getName() : "";
    }
}
