package uk.gov.moj.sdt.cmc.consumers.request.claim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "claimantReference",
    "claimant",
    "defendant1",
    "defendant2",
    "correspondenceDetailType",
    "sendParticularsSeparately",
    "reserveRightToClaimInterest",
    "interest",
    "claimAmount",
    "solicitorCost",
    "particulars",
    "sotSignature"
})
public class ClaimRequest {

    private String claimantReference;

    private Claimant claimant;

    private Defendant defendant1;

    private Defendant defendant2;

    private CorrespondenceDetailType correspondenceDetailType;

    private Boolean sendParticularsSeparately;

    private Boolean reserveRightToClaimInterest;

    private Interest interest;

    private Double claimAmount;

    private Double solicitorCost;

    private String particulars;

    private SotSignature sotSignature;
}
