package uk.gov.moj.sdt.cmc.consumers.xml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class XmlElementValueReader {

    public String getElementValue(String xmlContent, String xmlNodeName) {
        JsonNode entityNode = null;
        try {
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode jsonNode = xmlMapper.readValue(xmlContent.getBytes(), JsonNode.class);
            entityNode = getValuesInObject(jsonNode, xmlNodeName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return entityNode != null ? entityNode.textValue() : "";
    }

    private JsonNode getValuesInObject(JsonNode node, String entityName) {
        if (node == null) {
            return null;
        }
        if (node.has(entityName)) {
            return node.get(entityName);
        }
        if (!node.isContainerNode()) {
            return null;
        }
        for (JsonNode child : node) {
            if (child.isContainerNode()) {
                JsonNode childResult = getValuesInObject(child, entityName);
                if (childResult != null && !childResult.isMissingNode()) {
                    return childResult;
                }
            }
        }
        return null;
    }
}
