
package uk.gov.moj.sdt.ws._2013.mcol.breathingspaceschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.gov.moj.sdt.ws._2013.mcol.breathingspaceschema package. 
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

    private final static QName _McolBreathingSpace_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/BreathingSpaceSchema", "mcolBreathingSpace");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.mcol.breathingspaceschema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BreathingSpaceType }
     * 
     */
    public BreathingSpaceType createBreathingSpaceType() {
        return new BreathingSpaceType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BreathingSpaceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/BreathingSpaceSchema", name = "mcolBreathingSpace")
    public JAXBElement<BreathingSpaceType> createMcolBreathingSpace(BreathingSpaceType value) {
        return new JAXBElement<BreathingSpaceType>(_McolBreathingSpace_QNAME, BreathingSpaceType.class, null, value);
    }

}
