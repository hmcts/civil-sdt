package uk.gov.moj.sdt.cmc.consumers.client.xml;

import java.io.IOException;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.client.BaseXmlTest;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObject;
import uk.gov.moj.sdt.cmc.consumers.xml.XmlReader;

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
        XmlReader xmlReader = new XmlReader();
        String claimNumberValue = xmlReader.getElementValue(xmlContent, "claimNumber");
        assertNotNull(claimNumberValue);
        assertEquals("H0PR0001", claimNumberValue);
    }

    @Test
    void shouldExtractValuesFromClaimDefencesRequestXml() throws IOException {
        String fromDate = null;
        String toDate = null;

        String xmlContent = readXmlAsString(CLAIM_DEFENCES);

        String jsonContent = XmlToObject.convertXmlToJson(xmlContent);
        JSONParser jsonParser = new JSONParser();
        try {
            Object obj = jsonParser.parse(jsonContent);
            JSONObject claimDefencesObject = (JSONObject) obj;
            JSONObject criteriaObject = (JSONObject) claimDefencesObject.get("criteria");
            JSONObject criterionObject = (JSONObject) criteriaObject.get("criterion");
            JSONObject mcolDefenceCriteriaObject = (JSONObject) criterionObject.get("mcolDefenceCriteria");
            fromDate = (String) mcolDefenceCriteriaObject.get("fromDate");
            toDate = (String) mcolDefenceCriteriaObject.get("toDate");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(fromDate);
        assertNotNull(toDate);
        assertEquals("2009-12-01", fromDate);
        assertEquals("2009-12-02", toDate);
    }

}
