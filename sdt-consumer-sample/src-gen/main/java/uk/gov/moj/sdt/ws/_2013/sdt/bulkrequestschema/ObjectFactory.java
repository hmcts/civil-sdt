
package uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema package. 
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

    private final static QName _BulkRequest_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/sdt/BulkRequestSchema", "bulkRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BulkRequestType }
     * 
     */
    public BulkRequestType createBulkRequestType() {
        return new BulkRequestType();
    }

    /**
     * Create an instance of {@link RequestsType }
     * 
     */
    public RequestsType createRequestsType() {
        return new RequestsType();
    }

    /**
     * Create an instance of {@link HeaderType }
     * 
     */
    public HeaderType createHeaderType() {
        return new HeaderType();
    }

    /**
     * Create an instance of {@link RequestItemType }
     * 
     */
    public RequestItemType createRequestItemType() {
        return new RequestItemType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BulkRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/sdt/BulkRequestSchema", name = "bulkRequest")
    public JAXBElement<BulkRequestType> createBulkRequest(BulkRequestType value) {
        return new JAXBElement<BulkRequestType>(_BulkRequest_QNAME, BulkRequestType.class, null, value);
    }

}
