
package uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvresponseschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvresponseschema package.
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

    private static final QName _IndividualResponse_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/IndvResponseSchema", "individualResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvresponseschema
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IndividualResponseType }
     */
    public IndividualResponseType createIndividualResponseType() {
        return new IndividualResponseType();
    }

    /**
     * Create an instance of {@link HeaderType }
     */
    public HeaderType createHeaderType() {
        return new HeaderType();
    }

    /**
     * Create an instance of {@link IndividualResponseType.TargetAppDetail }
     */
    public IndividualResponseType.TargetAppDetail createIndividualResponseTypeTargetAppDetail() {
        return new IndividualResponseType.TargetAppDetail();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IndividualResponseType }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/IndvResponseSchema", name = "individualResponse")
    public JAXBElement<IndividualResponseType> createIndividualResponse(IndividualResponseType value) {
        return new JAXBElement<IndividualResponseType>(_IndividualResponse_QNAME, IndividualResponseType.class, null, value);
    }

}
