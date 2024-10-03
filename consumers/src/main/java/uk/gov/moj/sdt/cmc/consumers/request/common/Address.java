package uk.gov.moj.sdt.cmc.consumers.request.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "addressLine1",
    "addressLine2",
    "addressLine3",
    "postTown",
    "postCode"
})
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
        return isFieldEmpty(line4) ? null : line3;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getPosttown() {
        return isFieldEmpty(line4) ? line3 : line4;
    }

    private boolean isFieldEmpty(String field) {
        return field == null || field.isEmpty();
    }
}
