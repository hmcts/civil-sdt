package uk.gov.moj.sdt.cmc.consumers.client.xml;

import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.client.BaseXmlTest;
import uk.gov.moj.sdt.cmc.consumers.xml.XmlElementValueReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlReaderTest extends BaseXmlTest {

    private static final String BREATHING_SPACE = "BreathingSpace.xml";

    private static final String CLAIM_DEFENCES = "ClaimDefences.xml";

    @Test
    void shouldConvertBreathingSpaceRequestToString() {
        String xmlContent = readXmlAsString(BREATHING_SPACE);
        XmlElementValueReader xmlReader = new XmlElementValueReader();
        String claimNumberValue = xmlReader.getElementValue(xmlContent, "claimNumber");
        assertNotNull(claimNumberValue);
        assertEquals("H0PR0001", claimNumberValue);
    }

// TODO: fix broken test
//    @Test
//    void findValuesFromClaimDefencesXmlString() {
//        String xmlContent = readXmlAsString(CLAIM_DEFENCES);
//        XmlElementValueReader xmlReader = new XmlElementValueReader();
//        String sdtCustomerId = xmlReader.getElementValue(xmlContent, "sdtCustomerId");
//        String targetApplicationId = xmlReader.getElementValue(xmlContent, "targetApplicationId");
//        String fromDate = xmlReader.getElementValue(xmlContent, "fromDate");
//        String toDate = xmlReader.getElementValue(xmlContent, "toDate");
//        assertNotNull(sdtCustomerId);
//        assertEquals("12345678", sdtCustomerId);
//        assertNotNull(targetApplicationId);
//        assertEquals("mcol", targetApplicationId);
//        assertNotNull(fromDate);
//        assertEquals("2009-12-01", fromDate);
//        assertNotNull(toDate);
//        assertEquals("2009-12-02", toDate);
//    }

}
