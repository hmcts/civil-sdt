package uk.gov.moj.sdt.cmc.consumers.response.judgement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.moj.sdt.cmc.consumers.response.ResponseStatus;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JudgementResponse {

    private ResponseStatus responseStatus;

    @JacksonXmlProperty(localName = "mcolResponseDetail")
    private JudgementResponseDetail judgementResponseDetail;
}
