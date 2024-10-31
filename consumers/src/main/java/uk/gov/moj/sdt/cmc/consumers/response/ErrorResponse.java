package uk.gov.moj.sdt.cmc.consumers.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

    private Integer errorCode;

    private String errorText;
}
