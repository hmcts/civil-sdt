
package uk.gov.moj.sdt.ws._2013.mcol.requestschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.gov.moj.sdt.ws._2013.mcol.requestschema package. 
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

    private final static QName _UpdateClaimRequest_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/RequestSchema", "updateClaimRequest");
    private final static QName _CreateClaimRequest_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/RequestSchema", "createClaimRequest");
    private final static QName _IndividualRequest_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/RequestSchema", "individualRequest");
    private final static QName _CreateWarrantRequest_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/RequestSchema", "createWarrantRequest");
    private final static QName _CreateJudgmentRequest_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/RequestSchema", "createJudgmentRequest");
    private final static QName _CreateJudgmentWarrantRequest_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/RequestSchema", "createJudgmentWarrantRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.mcol.requestschema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IndividualRequestType }
     * 
     */
    public IndividualRequestType createIndividualRequestType() {
        return new IndividualRequestType();
    }

    /**
     * Create an instance of {@link CreateWarrantRequestType }
     * 
     */
    public CreateWarrantRequestType createCreateWarrantRequestType() {
        return new CreateWarrantRequestType();
    }

    /**
     * Create an instance of {@link CreateJudgmentRequestType }
     * 
     */
    public CreateJudgmentRequestType createCreateJudgmentRequestType() {
        return new CreateJudgmentRequestType();
    }

    /**
     * Create an instance of {@link CreateJudgmentWarrantRequestType }
     * 
     */
    public CreateJudgmentWarrantRequestType createCreateJudgmentWarrantRequestType() {
        return new CreateJudgmentWarrantRequestType();
    }

    /**
     * Create an instance of {@link UpdateClaimRequestType }
     * 
     */
    public UpdateClaimRequestType createUpdateClaimRequestType() {
        return new UpdateClaimRequestType();
    }

    /**
     * Create an instance of {@link CreateClaimRequestType }
     * 
     */
    public CreateClaimRequestType createCreateClaimRequestType() {
        return new CreateClaimRequestType();
    }

    /**
     * Create an instance of {@link HeaderType }
     * 
     */
    public HeaderType createHeaderType() {
        return new HeaderType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateClaimRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/RequestSchema", name = "updateClaimRequest")
    public JAXBElement<UpdateClaimRequestType> createUpdateClaimRequest(UpdateClaimRequestType value) {
        return new JAXBElement<UpdateClaimRequestType>(_UpdateClaimRequest_QNAME, UpdateClaimRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateClaimRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/RequestSchema", name = "createClaimRequest")
    public JAXBElement<CreateClaimRequestType> createCreateClaimRequest(CreateClaimRequestType value) {
        return new JAXBElement<CreateClaimRequestType>(_CreateClaimRequest_QNAME, CreateClaimRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IndividualRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/RequestSchema", name = "individualRequest")
    public JAXBElement<IndividualRequestType> createIndividualRequest(IndividualRequestType value) {
        return new JAXBElement<IndividualRequestType>(_IndividualRequest_QNAME, IndividualRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateWarrantRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/RequestSchema", name = "createWarrantRequest")
    public JAXBElement<CreateWarrantRequestType> createCreateWarrantRequest(CreateWarrantRequestType value) {
        return new JAXBElement<CreateWarrantRequestType>(_CreateWarrantRequest_QNAME, CreateWarrantRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateJudgmentRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/RequestSchema", name = "createJudgmentRequest")
    public JAXBElement<CreateJudgmentRequestType> createCreateJudgmentRequest(CreateJudgmentRequestType value) {
        return new JAXBElement<CreateJudgmentRequestType>(_CreateJudgmentRequest_QNAME, CreateJudgmentRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateJudgmentWarrantRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/RequestSchema", name = "createJudgmentWarrantRequest")
    public JAXBElement<CreateJudgmentWarrantRequestType> createCreateJudgmentWarrantRequest(CreateJudgmentWarrantRequestType value) {
        return new JAXBElement<CreateJudgmentWarrantRequestType>(_CreateJudgmentWarrantRequest_QNAME, CreateJudgmentWarrantRequestType.class, null, value);
    }

}
