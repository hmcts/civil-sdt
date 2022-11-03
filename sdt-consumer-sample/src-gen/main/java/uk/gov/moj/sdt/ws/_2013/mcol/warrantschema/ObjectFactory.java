
package uk.gov.moj.sdt.ws._2013.mcol.warrantschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the uk.gov.moj.sdt.ws._2013.mcol.warrantschema package.
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

    private static final QName _McolWarrant_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/WarrantSchema", "mcolWarrant");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.mcol.warrantschema
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link WarrantType }
     */
    public WarrantType createWarrantType() {
        return new WarrantType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WarrantType }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/WarrantSchema", name = "mcolWarrant")
    public JAXBElement<WarrantType> createMcolWarrant(WarrantType value) {
        return new JAXBElement<WarrantType>(_McolWarrant_QNAME, WarrantType.class, null, value);
    }

}
