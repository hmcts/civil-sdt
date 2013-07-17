
package uk.gov.moj.sdt.ws._2013.mcol.submitqueryresponseschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.gov.moj.sdt.ws._2013.mcol.submitqueryresponseschema package. 
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

    private final static QName _SubmitQueryResponse_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/SubmitQueryResponseSchema", "submitQueryResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.mcol.submitqueryresponseschema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link McolDefenceDetailType }
     * 
     */
    public McolDefenceDetailType createMcolDefenceDetailType() {
        return new McolDefenceDetailType();
    }

    /**
     * Create an instance of {@link SubmitQueryResponseType }
     * 
     */
    public SubmitQueryResponseType createSubmitQueryResponseType() {
        return new SubmitQueryResponseType();
    }

    /**
     * Create an instance of {@link DefendantType }
     * 
     */
    public DefendantType createDefendantType() {
        return new DefendantType();
    }

    /**
     * Create an instance of {@link McolResultsType }
     * 
     */
    public McolResultsType createMcolResultsType() {
        return new McolResultsType();
    }

    /**
     * Create an instance of {@link McolDefenceDetailType.Defendants }
     * 
     */
    public McolDefenceDetailType.Defendants createMcolDefenceDetailTypeDefendants() {
        return new McolDefenceDetailType.Defendants();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubmitQueryResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/SubmitQueryResponseSchema", name = "submitQueryResponse")
    public JAXBElement<SubmitQueryResponseType> createSubmitQueryResponse(SubmitQueryResponseType value) {
        return new JAXBElement<SubmitQueryResponseType>(_SubmitQueryResponse_QNAME, SubmitQueryResponseType.class, null, value);
    }

}
