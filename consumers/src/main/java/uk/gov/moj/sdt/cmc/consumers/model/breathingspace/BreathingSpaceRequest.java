package uk.gov.moj.sdt.cmc.consumers.model.breathingspace;


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
public class BreathingSpaceRequest implements CMCRequest {

    private String requestType;

    private String requestId;

    @JsonProperty(value = "mcolBreathingSpace", access = JsonProperty.Access.WRITE_ONLY)
    private BreathingSpace breathingSpace;


    public String getCaseManRef() {
        return breathingSpace.getClaimNumber();
    }

    public String getRespondentId() {
        return breathingSpace.getDefendantId();
    }

    public String getBsType() {
        return breathingSpace.getBreathingSpaceNotificationType();
    }
}


