package uk.gov.moj.sdt.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CMCFeedback {

    private int errorCode;
    private String errorText;
    @JsonFormat(pattern="yyyy-MM-dd")
    protected Date issueDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    protected Date serviceDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    protected Date judgmentEnteredDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    protected Date firstPaymentDate;
    private String warrantNumber;
    private String enforcingCourtCode;
    private String enforcingCourtName;
    private double fee;
    private String judgmentWarrantStatus;
    private String processingStatus;
}
