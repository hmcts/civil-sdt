package uk.gov.moj.sdt.cmc.consumers.response.judgement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.moj.sdt.cmc.consumers.response.JudgmentWarrantStatus;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonPropertyOrder({
    "judgmentEnteredDate",
    "firstPaymentDate",
    "judgmentWarrantStatus"
})
@JacksonXmlRootElement(localName = "mcolResponseDetail")
public class JudgementResponseDetail {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIMEZONE_EUROPE_LONDON = "Europe/London";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = DATE_FORMAT, timezone = TIMEZONE_EUROPE_LONDON)
    private Date firstPaymentDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = DATE_FORMAT, timezone = TIMEZONE_EUROPE_LONDON)
    private Date judgmentEnteredDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JudgmentWarrantStatus judgmentWarrantStatus;

    public String getJudgmentWarrantStatus() {
        return judgmentWarrantStatus == null ? null : judgmentWarrantStatus.getMessage();
    }
}
