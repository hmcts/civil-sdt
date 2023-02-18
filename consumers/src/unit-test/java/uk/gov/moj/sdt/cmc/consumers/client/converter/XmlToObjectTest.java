package uk.gov.moj.sdt.cmc.consumers.client.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObject;
import uk.gov.moj.sdt.cmc.consumers.model.breathingspace.BreathingSpaceRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlToObjectTest {

    private static final String BREATHING_SPACE = "BreathingSpace.xml";

    @BeforeEach
    public void setup() {
    }

    @Test
    void shouldConvertBreathingSpaceRequestToString() throws IOException {
        String xmlContent = readXmlAsString(BREATHING_SPACE);
        String request = XmlToObject.convertXmlToJson(xmlContent);
        assertNotNull(request);
    }

    @Test
    void shouldConvertBreathingSpaceRequest() throws IOException {
        String xmlContent = readXmlAsString(BREATHING_SPACE);
        BreathingSpaceRequest request = XmlToObject.convertXmlToObject(xmlContent, BreathingSpaceRequest.class);
        assertNotNull(request);
    }

    @Test
    void shouldConvertBreathingSpaceRequestAndConvertToJson() throws IOException {
        String xmlContent = readXmlAsString(BREATHING_SPACE);
        BreathingSpaceRequest request = XmlToObject.convertXmlToObject(xmlContent, BreathingSpaceRequest.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(request);
        assertNotNull(jsonString);
    }

    private String readXmlAsString(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            URL breathingSpaceURL = getClass().getClassLoader().getResource(fileName);
            Reader fileReader = new FileReader(breathingSpaceURL.getPath());
            BufferedReader bufReader = new BufferedReader(fileReader);
            String line = bufReader.readLine();
            while (line != null) {
                sb.append(line).append("\n");
                line = bufReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
