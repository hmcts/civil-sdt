package uk.gov.moj.sdt.cmc.consumers.xml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class XmlElementValueReader {

    public String getElementValue(String xmlContent, String xmlNodeName) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            ObjectNode dataInstance = xmlMapper.readValue(xmlContent.getBytes(), ObjectNode.class);
            return getValuesInObject(dataInstance, xmlNodeName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    private String getValuesInObject(ObjectNode jsonObject, String key) {
        Iterator<Map.Entry<String, JsonNode>> iterator = jsonObject.fields();
        while(iterator.hasNext()) {
            Map.Entry<String, JsonNode> entrySet = iterator.next();
            JsonNode valueNode = entrySet.getValue();
            String currentKey = entrySet.getKey();
            if (currentKey.equals(key)) {
                return valueNode.textValue();
            }

            if (valueNode instanceof ObjectNode) {
                return getValuesInObject((ObjectNode) valueNode, key);
            }
        }
        return "";
    }
}
