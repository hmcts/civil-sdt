package uk.gov.moj.sdt.cmc.consumers.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;

public final class XmlToObject {

    private XmlToObject() {
    }

    public static <T> T  convertXmlToObject(String xml,
                                     Class<T> targetClass) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(xml, targetClass);
    }

    public static String convertXmlToJson(String xml) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        ObjectMapper jsonMapper = new ObjectMapper();
        return jsonMapper.writeValueAsString(xmlMapper.readTree(xml.getBytes()));
    }

    public static <T> String convertObjectToXml(T object) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.writeValueAsString(object);
    }
}
