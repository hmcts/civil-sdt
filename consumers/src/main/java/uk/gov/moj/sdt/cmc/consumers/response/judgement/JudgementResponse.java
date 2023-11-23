package uk.gov.moj.sdt.cmc.consumers.response.judgement;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uk.gov.moj.sdt.cmc.consumers.response.JudgmentWarrantStatus;

@Data
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JudgementResponse {

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date firstPaymentDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date judgmentEnteredDate;

    private JudgmentWarrantStatus judgmentWarrantStatus;

}
