package uk.gov.moj.sdt.cmc.consumers.request.judgement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {

    private String line1;

    private String line2;

    private String line3;

    private String line4;

    private String posttown;

    private String postcode;

    public String getAddressLine1() {
        return line1;
    }

    public String getAddressLine2() {
        return line2;
    }

    public String getAddressLine3() {
        return line3;
    }

    public String getAddressLine4() {
        return line4;
    }

    public String getPostCode() {
        return postcode;
    }

    public String getPostTown() {
        return posttown;
    }
}
