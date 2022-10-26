
package uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvrequestschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvrequestschema package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _IndividualRequest_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/IndvRequestSchema", "individualRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvrequestschema
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IndividualRequestType }
     */
    public IndividualRequestType createIndividualRequestType() {
        return new IndividualRequestType();
    }

    /**
     * Create an instance of {@link HeaderType }
     */
    public HeaderType createHeaderType() {
        return new HeaderType();
    }

    /**
     * Create an instance of {@link IndividualRequestType.TargetAppDetail }
     */
    public IndividualRequestType.TargetAppDetail createIndividualRequestTypeTargetAppDetail() {
        return new IndividualRequestType.TargetAppDetail();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IndividualRequestType }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/IndvRequestSchema", name = "individualRequest")
    public JAXBElement<IndividualRequestType> createIndividualRequest(IndividualRequestType value) {
        return new JAXBElement<IndividualRequestType>(_IndividualRequest_QNAME, IndividualRequestType.class, null, value);
    }

}
