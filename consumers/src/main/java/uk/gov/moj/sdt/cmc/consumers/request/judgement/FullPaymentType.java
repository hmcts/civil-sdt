package uk.gov.moj.sdt.cmc.consumers.request.judgement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FullPaymentType {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIMEZONE_EUROPE_LONDON = "Europe/London";

    @JsonFormat(pattern = DATE_FORMAT, timezone = TIMEZONE_EUROPE_LONDON)
    protected Date fullByDate;
}
