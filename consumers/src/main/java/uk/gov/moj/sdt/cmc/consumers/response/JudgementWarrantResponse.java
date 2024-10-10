package uk.gov.moj.sdt.cmc.consumers.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JudgementWarrantResponse {

    private ResponseStatus responseStatus;

    @JacksonXmlProperty(localName = "mcolResponseDetail")
    private JudgementWarrantResponseDetail judgementWarrantResponseDetail;
}
