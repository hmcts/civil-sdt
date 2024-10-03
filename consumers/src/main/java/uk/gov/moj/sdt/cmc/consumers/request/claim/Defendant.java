package uk.gov.moj.sdt.cmc.consumers.request.claim;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uk.gov.moj.sdt.cmc.consumers.request.common.Address;

@Data
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "name",
    "address"
})
public class Defendant {

    private String name;

    @JsonProperty(value = "primaryAddress")
    @JsonAlias(value = "address")
    private Address address;
}
