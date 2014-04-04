
package uk.gov.moj.sdt.ws._2013.mcol.judgmentschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for paymentScheduleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="paymentScheduleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="inFullByPayment" type="{http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema}fullPaymentType"/>
 *         &lt;element name="instalment" type="{http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema}instalmentPaymentType"/>
 *         &lt;element name="immediatePayment" type="{http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema}immediatePaymentType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paymentScheduleType", propOrder = {
    "inFullByPayment",
    "instalment",
    "immediatePayment"
})
public class PaymentScheduleType {

    protected FullPaymentType inFullByPayment;
    protected InstalmentPaymentType instalment;
    protected ImmediatePaymentType immediatePayment;

    /**
     * Gets the value of the inFullByPayment property.
     * 
     * @return
     *     possible object is
     *     {@link FullPaymentType }
     *     
     */
    public FullPaymentType getInFullByPayment() {
        return inFullByPayment;
    }

    /**
     * Sets the value of the inFullByPayment property.
     * 
     * @param value
     *     allowed object is
     *     {@link FullPaymentType }
     *     
     */
    public void setInFullByPayment(FullPaymentType value) {
        this.inFullByPayment = value;
    }

    /**
     * Gets the value of the instalment property.
     * 
     * @return
     *     possible object is
     *     {@link InstalmentPaymentType }
     *     
     */
    public InstalmentPaymentType getInstalment() {
        return instalment;
    }

    /**
     * Sets the value of the instalment property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstalmentPaymentType }
     *     
     */
    public void setInstalment(InstalmentPaymentType value) {
        this.instalment = value;
    }

    /**
     * Gets the value of the immediatePayment property.
     * 
     * @return
     *     possible object is
     *     {@link ImmediatePaymentType }
     *     
     */
    public ImmediatePaymentType getImmediatePayment() {
        return immediatePayment;
    }

    /**
     * Sets the value of the immediatePayment property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImmediatePaymentType }
     *     
     */
    public void setImmediatePayment(ImmediatePaymentType value) {
        this.immediatePayment = value;
    }

}
