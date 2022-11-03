
package uk.gov.moj.sdt.ws._2013.mcol.responsedetailschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the uk.gov.moj.sdt.ws._2013.mcol.responsedetailschema package.
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

    private static final QName _McolResponseDetail_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/ResponseDetailSchema", "mcolResponseDetail");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.mcol.responsedetailschema
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResponseDetailType }
     */
    public ResponseDetailType createResponseDetailType() {
        return new ResponseDetailType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResponseDetailType }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/ResponseDetailSchema", name = "mcolResponseDetail")
    public JAXBElement<ResponseDetailType> createMcolResponseDetail(ResponseDetailType value) {
        return new JAXBElement<ResponseDetailType>(_McolResponseDetail_QNAME, ResponseDetailType.class, null, value);
    }

}
