package uk.gov.moj.sdt.cmc.consumers.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BreathingSpaceRequest implements CMCRequest {

    private String caseManRef;

    private String respondentId;

    private String bsType;
}
