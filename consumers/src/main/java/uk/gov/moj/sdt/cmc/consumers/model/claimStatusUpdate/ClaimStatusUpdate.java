package uk.gov.moj.sdt.cmc.consumers.model.claimStatusUpdate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ClaimStatusUpdate {

    private String claimNumber;

    private String defendantId;

    private String notificationType;

    private String paidInFullDate;

    private Boolean section38Compliancy;
}
