package uk.gov.moj.sdt.cmc.consumers.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class XmlToObject {

    public <T> T  convertXmlToObject(String xml,
                                     Class<T> targetClass) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(xml, targetClass);
    }

    public String convertXmlToJson(String xml) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        ObjectMapper jsonMapper = new ObjectMapper();
        return jsonMapper.writeValueAsString(xmlMapper.readTree(xml.getBytes()));
    }
}
