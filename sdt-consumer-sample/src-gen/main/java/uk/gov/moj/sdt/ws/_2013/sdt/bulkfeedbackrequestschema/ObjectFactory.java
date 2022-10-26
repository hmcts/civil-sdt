
package uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackrequestschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackrequestschema package.
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

    private final static QName _BulkFeedbackRequest_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackRequestSchema", "bulkFeedbackRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackrequestschema
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BulkFeedbackRequestType }
     */
    public BulkFeedbackRequestType createBulkFeedbackRequestType() {
        return new BulkFeedbackRequestType();
    }

    /**
     * Create an instance of {@link HeaderType }
     */
    public HeaderType createHeaderType() {
        return new HeaderType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BulkFeedbackRequestType }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackRequestSchema", name = "bulkFeedbackRequest")
    public JAXBElement<BulkFeedbackRequestType> createBulkFeedbackRequest(BulkFeedbackRequestType value) {
        return new JAXBElement<BulkFeedbackRequestType>(_BulkFeedbackRequest_QNAME, BulkFeedbackRequestType.class, null, value);
    }

}
