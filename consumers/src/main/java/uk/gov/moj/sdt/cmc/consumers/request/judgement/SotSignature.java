package uk.gov.moj.sdt.cmc.consumers.request.judgement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SotSignature {

    private String name;

    private String flag;

    public String getName() {
        return name;
    }

    public String getFlag() {
        return flag;
    }
}
