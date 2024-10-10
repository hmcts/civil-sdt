package uk.gov.moj.sdt.cmc.consumers.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgementRequest;

@ToString
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "judgmentRequest",
    "warrantRequest"
})
public class JudgementWarrantRequest {

    @JsonProperty(value = "judgmentRequest")
    @JsonAlias(value = "mcolJudgment")
    private JudgementRequest judgmentRequest;

    @JsonProperty(value = "warrantRequest")
    @JsonAlias(value = "mcolWarrant")
    private WarrantRequest warrantRequest;
}
