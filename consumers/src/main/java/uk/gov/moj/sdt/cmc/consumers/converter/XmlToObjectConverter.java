package uk.gov.moj.sdt.cmc.consumers.converter;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.model.McolDefenceDetailType;
import uk.gov.moj.sdt.cmc.consumers.model.McolDefenceDetailTypes;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.namespace.QName;

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

    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema", name = "mcolDefenceDetail")
    public JAXBElement<McolDefenceDetailType> createMcolDefenceDetail(McolDefenceDetailType value) {
        final QName _McolDefenceDetail_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema",
                "mcolDefenceDetail");
        return new JAXBElement<>(_McolDefenceDetail_QNAME, McolDefenceDetailType.class, null, value);
    }

    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema", name = "results")
    public JAXBElement<McolDefenceDetailTypes> createMcolDefenceDetailList(McolDefenceDetailTypes value) {
        final QName _McolDefenceDetail_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema",
                "mcolDefenceDetail");
        return new JAXBElement<>(_McolDefenceDetail_QNAME, McolDefenceDetailTypes.class, null, value);
    }


}
