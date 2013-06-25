
package uk.gov.moj.sdt.ws._2013.mcol.responseschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.gov.moj.sdt.ws._2013.mcol.responseschema package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CreateResponse_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/ResponseSchema", "createResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.mcol.responseschema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CreateResponseType }
     * 
     */
    public CreateResponseType createCreateResponseType() {
        return new CreateResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/ResponseSchema", name = "createResponse")
    public JAXBElement<CreateResponseType> createCreateResponse(CreateResponseType value) {
        return new JAXBElement<CreateResponseType>(_CreateResponse_QNAME, CreateResponseType.class, null, value);
    }

}
