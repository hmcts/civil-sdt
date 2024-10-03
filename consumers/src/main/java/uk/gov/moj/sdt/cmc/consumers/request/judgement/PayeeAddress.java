package uk.gov.moj.sdt.cmc.consumers.request.judgement;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
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
    "addressLine4",
    "postTown",
    "postcode"
})
public class PayeeAddress {

    @Getter
    @JsonAlias(value = "line1")
    private String addressLine1;

    @JsonAlias(value = "line2")
    private String addressLine2;

    @JsonAlias(value = "line3")
    private String addressLine3;

    @JsonAlias(value = "line4")
    private String addressLine4;

    @JsonAlias(value = "line5")
    private String addressLine5;

    @Getter
    private String postcode;

    public String getAddressLine2() {
        return isFieldEmpty(addressLine3) ? null : addressLine2;
    }

    public String getAddressLine3() {
        return isFieldEmpty(addressLine4) ? null : addressLine3;
    }

    public String getAddressLine4() {
        return isFieldEmpty(addressLine5) ? null : addressLine4;
    }

    public String getPostTown() {
        if (isFieldEmpty(addressLine5)) {
            if (isFieldEmpty(addressLine4)) {
                if (isFieldEmpty(addressLine3)) {
                    return addressLine2;
                } else {
                    return addressLine3;
                }
            } else {
                return addressLine4;
            }
        } else {
            return addressLine5;
        }
    }

    private boolean isFieldEmpty(String field) {
        return field == null || field.isEmpty();
    }
}
