package uk.gov.moj.sdt.cmc.consumers.converter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;

@Component
public class XmlToObjectConverter {

    private XmlMapper xmlMapper = new XmlMapper();

    public <T> T convertXmlToObject(String xml,
                                     Class<T> targetClass) throws IOException {
        return xmlMapper.readValue(xml, targetClass);
    }

    public String convertXmlToJson(String xml) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        return jsonMapper.writeValueAsString(xmlMapper.readTree(xml.getBytes()));
    }

    public <T> String convertObjectToXml(T object) throws JsonProcessingException {
        return xmlMapper.writeValueAsString(object);
    }
}
