
package uk.gov.moj.sdt.ws._2013.mcol.judgmentschema;

import java.util.Calendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter2;
import org.w3._2001.xmlschema.Adapter3;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.AddressType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.SotSignatureType;


/**
 * <p>Java class for mcolJudgmentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mcolJudgmentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="claimNumber" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}claimNumberType"/>
 *         &lt;element name="jointJudgment" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="judgmentType" type="{http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema}judgmentTypeType"/>
 *         &lt;element name="sentParticularsSeparately" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="defendantId" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}defendantIdType" minOccurs="0"/>
 *         &lt;element name="defendant1Address" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}addressType" minOccurs="0"/>
 *         &lt;element name="defendant1DateOfBirth" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="defendant2Address" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}addressType" minOccurs="0"/>
 *         &lt;element name="defendant2DateOfBirth" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="paymentSchedule" type="{http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema}paymentScheduleType"/>
 *         &lt;element name="interest" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
 *         &lt;element name="solicitorCost" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
 *         &lt;element name="deductedAmount" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
 *         &lt;element name="judgmentAmount" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
 *         &lt;element name="payee" type="{http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema}payeeType" minOccurs="0"/>
 *         &lt;element name="sotSignature" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}sotSignatureType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mcolJudgmentType", propOrder = {
    "claimNumber",
    "jointJudgment",
    "judgmentType",
    "sentParticularsSeparately",
    "defendantId",
    "defendant1Address",
    "defendant1DateOfBirth",
    "defendant2Address",
    "defendant2DateOfBirth",
    "paymentSchedule",
    "interest",
    "solicitorCost",
    "deductedAmount",
    "judgmentAmount",
    "payee",
    "sotSignature"
})
public class McolJudgmentType {

    @XmlElement(required = true)
    protected String claimNumber;
    protected boolean jointJudgment;
    @XmlElement(required = true)
    protected JudgmentTypeType judgmentType;
    protected Boolean sentParticularsSeparately;
    protected String defendantId;
    protected AddressType defendant1Address;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar defendant1DateOfBirth;
    protected AddressType defendant2Address;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar defendant2DateOfBirth;
    @XmlElement(required = true)
    protected PaymentScheduleType paymentSchedule;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter3 .class)
    @XmlSchemaType(name = "unsignedLong")
    protected Long interest;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter3 .class)
    @XmlSchemaType(name = "unsignedLong")
    protected Long solicitorCost;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter3 .class)
    @XmlSchemaType(name = "unsignedLong")
    protected Long deductedAmount;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter3 .class)
    @XmlSchemaType(name = "unsignedLong")
    protected Long judgmentAmount;
    protected PayeeType payee;
    @XmlElement(required = true)
    protected SotSignatureType sotSignature;

    /**
     * Gets the value of the claimNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaimNumber() {
        return claimNumber;
    }

    /**
     * Sets the value of the claimNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaimNumber(String value) {
        this.claimNumber = value;
    }

    /**
     * Gets the value of the jointJudgment property.
     * 
     */
    public boolean isJointJudgment() {
        return jointJudgment;
    }

    /**
     * Sets the value of the jointJudgment property.
     * 
     */
    public void setJointJudgment(boolean value) {
        this.jointJudgment = value;
    }

    /**
     * Gets the value of the judgmentType property.
     * 
     * @return
     *     possible object is
     *     {@link JudgmentTypeType }
     *     
     */
    public JudgmentTypeType getJudgmentType() {
        return judgmentType;
    }

    /**
     * Sets the value of the judgmentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JudgmentTypeType }
     *     
     */
    public void setJudgmentType(JudgmentTypeType value) {
        this.judgmentType = value;
    }

    /**
     * Gets the value of the sentParticularsSeparately property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSentParticularsSeparately() {
        return sentParticularsSeparately;
    }

    /**
     * Sets the value of the sentParticularsSeparately property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSentParticularsSeparately(Boolean value) {
        this.sentParticularsSeparately = value;
    }

    /**
     * Gets the value of the defendantId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefendantId() {
        return defendantId;
    }

    /**
     * Sets the value of the defendantId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefendantId(String value) {
        this.defendantId = value;
    }

    /**
     * Gets the value of the defendant1Address property.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getDefendant1Address() {
        return defendant1Address;
    }

    /**
     * Sets the value of the defendant1Address property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setDefendant1Address(AddressType value) {
        this.defendant1Address = value;
    }

    /**
     * Gets the value of the defendant1DateOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getDefendant1DateOfBirth() {
        return defendant1DateOfBirth;
    }

    /**
     * Sets the value of the defendant1DateOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefendant1DateOfBirth(Calendar value) {
        this.defendant1DateOfBirth = value;
    }

    /**
     * Gets the value of the defendant2Address property.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getDefendant2Address() {
        return defendant2Address;
    }

    /**
     * Sets the value of the defendant2Address property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setDefendant2Address(AddressType value) {
        this.defendant2Address = value;
    }

    /**
     * Gets the value of the defendant2DateOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getDefendant2DateOfBirth() {
        return defendant2DateOfBirth;
    }

    /**
     * Sets the value of the defendant2DateOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefendant2DateOfBirth(Calendar value) {
        this.defendant2DateOfBirth = value;
    }

    /**
     * Gets the value of the paymentSchedule property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentScheduleType }
     *     
     */
    public PaymentScheduleType getPaymentSchedule() {
        return paymentSchedule;
    }

    /**
     * Sets the value of the paymentSchedule property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentScheduleType }
     *     
     */
    public void setPaymentSchedule(PaymentScheduleType value) {
        this.paymentSchedule = value;
    }

    /**
     * Gets the value of the interest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Long getInterest() {
        return interest;
    }

    /**
     * Sets the value of the interest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterest(Long value) {
        this.interest = value;
    }

    /**
     * Gets the value of the solicitorCost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Long getSolicitorCost() {
        return solicitorCost;
    }

    /**
     * Sets the value of the solicitorCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSolicitorCost(Long value) {
        this.solicitorCost = value;
    }

    /**
     * Gets the value of the deductedAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Long getDeductedAmount() {
        return deductedAmount;
    }

    /**
     * Sets the value of the deductedAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeductedAmount(Long value) {
        this.deductedAmount = value;
    }

    /**
     * Gets the value of the judgmentAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Long getJudgmentAmount() {
        return judgmentAmount;
    }

    /**
     * Sets the value of the judgmentAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJudgmentAmount(Long value) {
        this.judgmentAmount = value;
    }

    /**
     * Gets the value of the payee property.
     * 
     * @return
     *     possible object is
     *     {@link PayeeType }
     *     
     */
    public PayeeType getPayee() {
        return payee;
    }

    /**
     * Sets the value of the payee property.
     * 
     * @param value
     *     allowed object is
     *     {@link PayeeType }
     *     
     */
    public void setPayee(PayeeType value) {
        this.payee = value;
    }

    /**
     * Gets the value of the sotSignature property.
     * 
     * @return
     *     possible object is
     *     {@link SotSignatureType }
     *     
     */
    public SotSignatureType getSotSignature() {
        return sotSignature;
    }

    /**
     * Sets the value of the sotSignature property.
     * 
     * @param value
     *     allowed object is
     *     {@link SotSignatureType }
     *     
     */
    public void setSotSignature(SotSignatureType value) {
        this.sotSignature = value;
    }

}
