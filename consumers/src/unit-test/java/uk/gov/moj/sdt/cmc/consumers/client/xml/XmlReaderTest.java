package uk.gov.moj.sdt.cmc.consumers.client.xml;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.client.BaseXmlTest;
import uk.gov.moj.sdt.cmc.consumers.xml.XmlElementValueReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlReaderTest extends BaseXmlTest {

    private static final String BREATHING_SPACE = "BreathingSpace.xml";

    private static final String CLAIM_DEFENCES = "ClaimDefences.xml";

    @BeforeEach
    public void setup() {
    }

    @Test
    void shouldConvertBreathingSpaceRequestToString() throws IOException {
        String xmlContent = readXmlAsString(BREATHING_SPACE);
        XmlElementValueReader xmlReader = new XmlElementValueReader();
        String claimNumberValue = xmlReader.getElementValue(xmlContent, "claimNumber");
        assertNotNull(claimNumberValue);
        assertEquals("H0PR0001", claimNumberValue);
    }

    @Test
    void findFromDateFromClaimDefencesXmlString() throws IOException {
        String xmlContent = readXmlAsString(CLAIM_DEFENCES);
        XmlElementValueReader xmlReader = new XmlElementValueReader();
        String fromDate = xmlReader.getElementValue(xmlContent, "fromDate");
        assertNotNull(fromDate);
        assertEquals("2009-12-01", fromDate);
    }

}
