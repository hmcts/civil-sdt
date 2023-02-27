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
        readXmlFileAndValidateData(BREATHING_SPACE, "claimNumber", "H0PR0001");
    }

    @Test
    void findFromDateFromClaimDefencesXmlString() throws IOException {
        readXmlFileAndValidateData(CLAIM_DEFENCES, "fromDate", "2009-12-01");
    }

    private void readXmlFileAndValidateData(String fileName, String xmlNodeName, String expectedValue) {
        String xmlContent = readXmlAsString(fileName);
        XmlElementValueReader xmlReader = new XmlElementValueReader();
        String returnValue = xmlReader.getElementValue(xmlContent, xmlNodeName);
        assertNotNull(returnValue);
        assertEquals(expectedValue, returnValue);
    }

}
