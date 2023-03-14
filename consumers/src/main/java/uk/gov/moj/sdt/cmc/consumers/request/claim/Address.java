package uk.gov.moj.sdt.cmc.consumers.request.claim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "line1",
    "line2",
    "line3",
    "line4",
    "postcode",
    "posttown"
})
public class Address {

    private String line1;

    private String line2;

    private String line3;

    private String line4;

    private String postcode;

    private String posttown;
}
