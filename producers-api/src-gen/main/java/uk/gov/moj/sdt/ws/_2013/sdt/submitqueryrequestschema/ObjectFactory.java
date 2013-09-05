
package uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema package. 
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

    private final static QName _SubmitQueryRequest_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryRequestSchema", "submitQueryRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SubmitQueryRequestType }
     * 
     */
    public SubmitQueryRequestType createSubmitQueryRequestType() {
        return new SubmitQueryRequestType();
    }

    /**
     * Create an instance of {@link HeaderType }
     * 
     */
    public HeaderType createHeaderType() {
        return new HeaderType();
    }

    /**
     * Create an instance of {@link CriteriaType }
     * 
     */
    public CriteriaType createCriteriaType() {
        return new CriteriaType();
    }

    /**
     * Create an instance of {@link CriterionType }
     * 
     */
    public CriterionType createCriterionType() {
        return new CriterionType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubmitQueryRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryRequestSchema", name = "submitQueryRequest")
    public JAXBElement<SubmitQueryRequestType> createSubmitQueryRequest(SubmitQueryRequestType value) {
        return new JAXBElement<SubmitQueryRequestType>(_SubmitQueryRequest_QNAME, SubmitQueryRequestType.class, null, value);
    }

}
