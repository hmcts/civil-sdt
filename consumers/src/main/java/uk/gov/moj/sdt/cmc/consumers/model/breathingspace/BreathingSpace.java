package uk.gov.moj.sdt.cmc.consumers.model.breathingspace;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class BreathingSpace {

    private String claimNumber;

    private String defendantId;

    private String breathingSpaceNotificationType;
}
