
package uk.gov.moj.sdt.producers.prototype.common.disc_types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.gov.moj.sdt.producers.prototype.common.disc_types package. 
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

    private final static QName _GetGreetingResponseElement_QNAME = new QName("http://common.prototype.producers.sdt.moj.gov.uk/disc_types.xml", "getGreetingResponseElement");
    private final static QName _GetGreetingRequestElement_QNAME = new QName("http://common.prototype.producers.sdt.moj.gov.uk/disc_types.xml", "getGreetingRequestElement");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.producers.prototype.common.disc_types
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetGreetingRequestType }
     * 
     */
    public GetGreetingRequestType createGetGreetingRequestType() {
        return new GetGreetingRequestType();
    }

    /**
     * Create an instance of {@link GetGreetingResponseType }
     * 
     */
    public GetGreetingResponseType createGetGreetingResponseType() {
        return new GetGreetingResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetGreetingResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://common.prototype.producers.sdt.moj.gov.uk/disc_types.xml", name = "getGreetingResponseElement")
    public JAXBElement<GetGreetingResponseType> createGetGreetingResponseElement(GetGreetingResponseType value) {
        return new JAXBElement<GetGreetingResponseType>(_GetGreetingResponseElement_QNAME, GetGreetingResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetGreetingRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://common.prototype.producers.sdt.moj.gov.uk/disc_types.xml", name = "getGreetingRequestElement")
    public JAXBElement<GetGreetingRequestType> createGetGreetingRequestElement(GetGreetingRequestType value) {
        return new JAXBElement<GetGreetingRequestType>(_GetGreetingRequestElement_QNAME, GetGreetingRequestType.class, null, value);
    }

}
