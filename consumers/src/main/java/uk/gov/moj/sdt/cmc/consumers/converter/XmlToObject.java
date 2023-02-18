package uk.gov.moj.sdt.cmc.consumers.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;

public final class XmlToObject {

    private XmlToObject() {
    }

    public static <T> T  convertXmlToObject(String xml,
                                     Class<T> targetClass) throws IOException {
        String json = convertXmlToJson(xml);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, targetClass);
    }

    public static String convertXmlToJson(String xml) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        ObjectMapper jsonMapper = new ObjectMapper();
        return jsonMapper.writeValueAsString(xmlMapper.readTree(xml.getBytes()));
    }
}
