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

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIMEZONE_EUROPE_LONDON = "Europe/London";

    @JsonProperty(value = "interestDailyAmount")
    @JsonAlias(value = "dailyAmount")
    private Long dailyAmount;

    @JsonProperty(value = "interestOwedDate")
    @JsonAlias(value = "owedDate")
    @JsonFormat(pattern = DATE_FORMAT, timezone = TIMEZONE_EUROPE_LONDON)
    private Date owedDate;

    @JsonProperty(value = "interestClaimDate")
    @JsonAlias(value = "claimDate")
    @JsonFormat(pattern = DATE_FORMAT, timezone = TIMEZONE_EUROPE_LONDON)
    private Date claimDate;

    private Long claimAmountInterestBase;
}
