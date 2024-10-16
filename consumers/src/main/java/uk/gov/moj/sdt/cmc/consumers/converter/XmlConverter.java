package uk.gov.moj.sdt.cmc.consumers.converter;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class XmlConverter {

    private XmlMapper xmlMapper = new XmlMapper();

    public <T> T convertXmlToObject(String xml, Class<T> targetClass) throws IOException {
        return xmlMapper.readValue(xml, targetClass);
    }

    public <T> String convertObjectToXml(T object) throws IOException {
        return xmlMapper.writeValueAsString(object);
    }
}
