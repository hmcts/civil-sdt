
package uk.gov.moj.sdt.ws._2013.mcol.queryschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.gov.moj.sdt.ws._2013.mcol.queryschema package. 
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

    private final static QName _McolDefenceCriteria_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema", "mcolDefenceCriteria");
    private final static QName _McolDefenceDetail_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema", "mcolDefenceDetail");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.mcol.queryschema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link McolDefenceCriteriaType }
     * 
     */
    public McolDefenceCriteriaType createMcolDefenceCriteriaType() {
        return new McolDefenceCriteriaType();
    }

    /**
     * Create an instance of {@link McolDefenceDetailType }
     * 
     */
    public McolDefenceDetailType createMcolDefenceDetailType() {
        return new McolDefenceDetailType();
    }

    /**
     * Create an instance of {@link DefendantType }
     * 
     */
    public DefendantType createDefendantType() {
        return new DefendantType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link McolDefenceCriteriaType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema", name = "mcolDefenceCriteria")
    public JAXBElement<McolDefenceCriteriaType> createMcolDefenceCriteria(McolDefenceCriteriaType value) {
        return new JAXBElement<McolDefenceCriteriaType>(_McolDefenceCriteria_QNAME, McolDefenceCriteriaType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link McolDefenceDetailType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema", name = "mcolDefenceDetail")
    public JAXBElement<McolDefenceDetailType> createMcolDefenceDetail(McolDefenceDetailType value) {
        return new JAXBElement<McolDefenceDetailType>(_McolDefenceDetail_QNAME, McolDefenceDetailType.class, null, value);
    }

}
