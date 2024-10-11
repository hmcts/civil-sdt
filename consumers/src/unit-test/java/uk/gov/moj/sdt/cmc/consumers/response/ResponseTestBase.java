package uk.gov.moj.sdt.cmc.consumers.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public abstract class ResponseTestBase extends AbstractSdtUnitTestBase {

    protected ObjectMapper objectMapper;

    @Override
    protected void setUpLocalTests() {
        objectMapper = new ObjectMapper();
    }

    public Date createDate(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return Date.from(date.atStartOfDay().atZone(ZoneId.of("Europe/London")).toInstant());
    }
}
