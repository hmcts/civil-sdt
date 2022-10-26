
package uk.gov.moj.sdt.ws._2013.sdt.bulkresponseschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the uk.gov.moj.sdt.ws._2013.sdt.bulkresponseschema package.
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

    private final static QName _BulkResponse_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/sdt/BulkResponseSchema", "bulkResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.sdt.bulkresponseschema
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BulkResponseType }
     */
    public BulkResponseType createBulkResponseType() {
        return new BulkResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BulkResponseType }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/sdt/BulkResponseSchema", name = "bulkResponse")
    public JAXBElement<BulkResponseType> createBulkResponse(BulkResponseType value) {
        return new JAXBElement<BulkResponseType>(_BulkResponse_QNAME, BulkResponseType.class, null, value);
    }

}
