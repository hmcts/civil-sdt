package uk.gov.moj.sdt.cmc.consumers.request.claim;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "dailyAmount",
    "owedDate",
    "claimDate",
    "claimAmountInterestBase"
})
public class Interest {

    @JsonProperty(value = "interestDailyAmount")
    @JsonAlias(value = "dailyAmount")
    private Double dailyAmount;

    @JsonProperty(value = "interestOwedDate")
    @JsonAlias(value = "owedDate")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Europe/London")
    private Date owedDate;

    @JsonProperty(value = "interestClaimDate")
    @JsonAlias(value = "claimDate")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Europe/London")
    private Date claimDate;

    private Double claimAmountInterestBase;
}
