
package uk.gov.moj.sdt.ws._2013.sdt.defencefeedbackresponseschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.gov.moj.sdt.ws._2013.sdt.defencefeedbackresponseschema package. 
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

    private final static QName _DefenceResponse_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/sdt/DefenceFeedbackResponseSchema", "defenceResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.sdt.defencefeedbackresponseschema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DefenceDetailType }
     * 
     */
    public DefenceDetailType createDefenceDetailType() {
        return new DefenceDetailType();
    }

    /**
     * Create an instance of {@link DefenceResponseType }
     * 
     */
    public DefenceResponseType createDefenceResponseType() {
        return new DefenceResponseType();
    }

    /**
     * Create an instance of {@link DefendantType }
     * 
     */
    public DefendantType createDefendantType() {
        return new DefendantType();
    }

    /**
     * Create an instance of {@link DefenceDetailsType }
     * 
     */
    public DefenceDetailsType createDefenceDetailsType() {
        return new DefenceDetailsType();
    }

    /**
     * Create an instance of {@link DefenceDetailType.Defendants }
     * 
     */
    public DefenceDetailType.Defendants createDefenceDetailTypeDefendants() {
        return new DefenceDetailType.Defendants();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefenceResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/sdt/DefenceFeedbackResponseSchema", name = "defenceResponse")
    public JAXBElement<DefenceResponseType> createDefenceResponse(DefenceResponseType value) {
        return new JAXBElement<DefenceResponseType>(_DefenceResponse_QNAME, DefenceResponseType.class, null, value);
    }

}
