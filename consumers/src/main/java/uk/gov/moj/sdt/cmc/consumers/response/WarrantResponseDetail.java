package uk.gov.moj.sdt.cmc.consumers.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "issueDate",
    "warrantNumber",
    "enforcingCourtCode",
    "enforcingCourtName",
    "fee",
    "judgmentWarrantStatus"
})
@JacksonXmlRootElement(localName = "mcolResponseDetail")
public class WarrantResponseDetail {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIMEZONE_EUROPE_LONDON = "Europe/London";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = DATE_FORMAT, timezone = TIMEZONE_EUROPE_LONDON)
    private Date issueDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String warrantNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String enforcingCourtCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String enforcingCourtName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long fee;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JudgmentWarrantStatus judgmentWarrantStatus;

    public String getJudgmentWarrantStatus() {
        return judgmentWarrantStatus == null ? null : judgmentWarrantStatus.getMessage();
    }
}
