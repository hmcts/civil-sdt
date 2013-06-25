
package uk.gov.moj.sdt.ws._2013.mcol.claimschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.gov.moj.sdt.ws._2013.mcol.claimschema package. 
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

    private final static QName _McolClaim_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/ClaimSchema", "mcolClaim");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.mcol.claimschema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CorrespondenceDetailType }
     * 
     */
    public CorrespondenceDetailType createCorrespondenceDetailType() {
        return new CorrespondenceDetailType();
    }

    /**
     * Create an instance of {@link ClaimType }
     * 
     */
    public ClaimType createClaimType() {
        return new ClaimType();
    }

    /**
     * Create an instance of {@link InterestType }
     * 
     */
    public InterestType createInterestType() {
        return new InterestType();
    }

    /**
     * Create an instance of {@link CorrespondenceDetailType.Address }
     * 
     */
    public CorrespondenceDetailType.Address createCorrespondenceDetailTypeAddress() {
        return new CorrespondenceDetailType.Address();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClaimType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/ClaimSchema", name = "mcolClaim")
    public JAXBElement<ClaimType> createMcolClaim(ClaimType value) {
        return new JAXBElement<ClaimType>(_McolClaim_QNAME, ClaimType.class, null, value);
    }

}
