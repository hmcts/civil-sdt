package uk.gov.moj.sdt.cmc.consumers.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarrantResponse {

    private String warrantNumber;

    private String enforcingCourtCode;

    private String enforcingCourtName;

    private Long fee;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date issueDate;

    private JudgmentWarrantStatus judgmentWarrantStatus;
}
