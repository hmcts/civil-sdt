
package uk.gov.moj.sdt.ws._2013.mcol.judgmentschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the uk.gov.moj.sdt.ws._2013.mcol.judgmentschema package.
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

    private static final QName _McolJudgmentWarrant_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema", "mcolJudgmentWarrant");
    private static final QName _McolJudgment_QNAME = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema", "mcolJudgment");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.mcol.judgmentschema
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JudgmentWarrantType }
     */
    public JudgmentWarrantType createJudgmentWarrantType() {
        return new JudgmentWarrantType();
    }

    /**
     * Create an instance of {@link McolJudgmentType }
     */
    public McolJudgmentType createMcolJudgmentType() {
        return new McolJudgmentType();
    }

    /**
     * Create an instance of {@link FullPaymentType }
     */
    public FullPaymentType createFullPaymentType() {
        return new FullPaymentType();
    }

    /**
     * Create an instance of {@link ImmediatePaymentType }
     */
    public ImmediatePaymentType createImmediatePaymentType() {
        return new ImmediatePaymentType();
    }

    /**
     * Create an instance of {@link PaymentScheduleType }
     */
    public PaymentScheduleType createPaymentScheduleType() {
        return new PaymentScheduleType();
    }

    /**
     * Create an instance of {@link PayeeType }
     */
    public PayeeType createPayeeType() {
        return new PayeeType();
    }

    /**
     * Create an instance of {@link InstalmentPaymentType }
     */
    public InstalmentPaymentType createInstalmentPaymentType() {
        return new InstalmentPaymentType();
    }

    /**
     * Create an instance of {@link PayeeAddressType }
     */
    public PayeeAddressType createPayeeAddressType() {
        return new PayeeAddressType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link JudgmentWarrantType }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema", name = "mcolJudgmentWarrant")
    public JAXBElement<JudgmentWarrantType> createMcolJudgmentWarrant(JudgmentWarrantType value) {
        return new JAXBElement<JudgmentWarrantType>(_McolJudgmentWarrant_QNAME, JudgmentWarrantType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link McolJudgmentType }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema", name = "mcolJudgment")
    public JAXBElement<McolJudgmentType> createMcolJudgment(McolJudgmentType value) {
        return new JAXBElement<McolJudgmentType>(_McolJudgment_QNAME, McolJudgmentType.class, null, value);
    }

}
