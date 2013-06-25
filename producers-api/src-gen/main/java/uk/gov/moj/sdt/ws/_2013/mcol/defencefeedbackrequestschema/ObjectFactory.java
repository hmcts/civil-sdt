
package uk.gov.moj.sdt.ws._2013.mcol.defencefeedbackrequestschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.gov.moj.sdt.ws._2013.mcol.defencefeedbackrequestschema package. 
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

    private final static QName _DefenceRequest_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/DefenceFeedbackRequestSchema", "defenceRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.mcol.defencefeedbackrequestschema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DefenceRequestType }
     * 
     */
    public DefenceRequestType createDefenceRequestType() {
        return new DefenceRequestType();
    }

    /**
     * Create an instance of {@link HeaderType }
     * 
     */
    public HeaderType createHeaderType() {
        return new HeaderType();
    }

    /**
     * Create an instance of {@link McolDefenceType }
     * 
     */
    public McolDefenceType createMcolDefenceType() {
        return new McolDefenceType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefenceRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/DefenceFeedbackRequestSchema", name = "defenceRequest")
    public JAXBElement<DefenceRequestType> createDefenceRequest(DefenceRequestType value) {
        return new JAXBElement<DefenceRequestType>(_DefenceRequest_QNAME, DefenceRequestType.class, null, value);
    }

}
