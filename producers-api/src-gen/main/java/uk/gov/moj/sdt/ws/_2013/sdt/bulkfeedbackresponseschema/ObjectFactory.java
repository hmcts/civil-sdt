
package uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema package.
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

    private static final QName _BulkFeedbackResponse_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema", "bulkFeedbackResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResponseType }
     */
    public ResponseType createResponseType() {
        return new ResponseType();
    }

    /**
     * Create an instance of {@link BulkFeedbackResponseType }
     */
    public BulkFeedbackResponseType createBulkFeedbackResponseType() {
        return new BulkFeedbackResponseType();
    }

    /**
     * Create an instance of {@link ResponsesType }
     */
    public ResponsesType createResponsesType() {
        return new ResponsesType();
    }

    /**
     * Create an instance of {@link BulkRequestStatusType }
     */
    public BulkRequestStatusType createBulkRequestStatusType() {
        return new BulkRequestStatusType();
    }

    /**
     * Create an instance of {@link ResponseType.ResponseDetail }
     */
    public ResponseType.ResponseDetail createResponseTypeResponseDetail() {
        return new ResponseType.ResponseDetail();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BulkFeedbackResponseType }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema", name = "bulkFeedbackResponse")
    public JAXBElement<BulkFeedbackResponseType> createBulkFeedbackResponse(BulkFeedbackResponseType value) {
        return new JAXBElement<BulkFeedbackResponseType>(_BulkFeedbackResponse_QNAME, BulkFeedbackResponseType.class, null, value);
    }

}
