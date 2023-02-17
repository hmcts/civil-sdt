package uk.gov.moj.sdt.cmc.consumers.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uk.gov.moj.sdt.domain.RequestType;

@Getter
@ToString
public class BreathingSpaceRequest extends CMCRequest {

    private String caseManRef;

    private String respondentId;

    private String bsType;

    public BreathingSpaceRequest(String caseManRef,
                          String respondentId,
                          String bsType) {
        this.caseManRef = caseManRef;
        this.respondentId = respondentId;
        this.bsType = bsType;
        setRequestType(RequestType.BREATHING_SPACE);
    }
}
