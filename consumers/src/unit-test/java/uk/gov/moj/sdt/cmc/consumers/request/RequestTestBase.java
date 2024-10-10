package uk.gov.moj.sdt.cmc.consumers.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.moj.sdt.cmc.consumers.request.common.Address;
import uk.gov.moj.sdt.cmc.consumers.request.common.SotSignature;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class RequestTestBase extends AbstractSdtUnitTestBase {

    protected ObjectMapper objectMapper;

    @Override
    protected void setUpLocalTests() {
        objectMapper = new ObjectMapper();
    }

    public void assertExpectedRequestJson(String expectedRequestJson, String actualRequestJson) {
        assertNotNull(actualRequestJson, "Request JSON should not be null");
        assertEquals(expectedRequestJson, actualRequestJson, "Request JSON does not match expected value");
    }

    public Date createDate(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public Address createAddress(String prefix, String postcode) {
        String addressLinePrefix = prefix + " line ";

        Address address = new Address();
        address.setLine1(addressLinePrefix + "1");
        address.setLine2(addressLinePrefix + "2");
        address.setLine3(addressLinePrefix + "3");
        address.setLine4(addressLinePrefix + "4");
        address.setPosttown(prefix + " town");
        address.setPostcode(postcode);

        return address;
    }

    public SotSignature createSotSignature(String name) {
        SotSignature sotSignature = new SotSignature();

        sotSignature.setFlag(true);
        sotSignature.setName(name);

        return sotSignature;
    }
}
