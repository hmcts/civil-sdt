
package uk.gov.moj.sdt.ws._2013.mcol.warrantschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter3;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.AddressType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.SotSignatureType;


/**
 * <p>Java class for warrantType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="warrantType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="claimNumber" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}claimNumberType"/>
 *         &lt;element name="defendantId" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}defendantIdType"/>
 *         &lt;element name="defendantAddress" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}addressType"/>
 *         &lt;element name="balanceOfDebt" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
 *         &lt;element name="warrantAmount" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
 *         &lt;element name="solicitorCost" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
 *         &lt;element name="additionalNotes" type="{http://ws.sdt.moj.gov.uk/2013/mcol/WarrantSchema}additionalNotesType" minOccurs="0"/>
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
@XmlType(name = "warrantType", propOrder = {
    "claimNumber",
    "defendantId",
    "defendantAddress",
    "balanceOfDebt",
    "warrantAmount",
    "solicitorCost",
    "additionalNotes",
    "sotSignature"
})
public class WarrantType {

    @XmlElement(required = true)
    protected String claimNumber;
    @XmlElement(required = true)
    protected String defendantId;
    @XmlElement(required = true)
    protected AddressType defendantAddress;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter3 .class)
    @XmlSchemaType(name = "unsignedLong")
    protected Long balanceOfDebt;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter3 .class)
    @XmlSchemaType(name = "unsignedLong")
    protected Long warrantAmount;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter3 .class)
    @XmlSchemaType(name = "unsignedLong")
    protected Long solicitorCost;
    protected String additionalNotes;
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
     * Gets the value of the defendantAddress property.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getDefendantAddress() {
        return defendantAddress;
    }

    /**
     * Sets the value of the defendantAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setDefendantAddress(AddressType value) {
        this.defendantAddress = value;
    }

    /**
     * Gets the value of the balanceOfDebt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Long getBalanceOfDebt() {
        return balanceOfDebt;
    }

    /**
     * Sets the value of the balanceOfDebt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBalanceOfDebt(Long value) {
        this.balanceOfDebt = value;
    }

    /**
     * Gets the value of the warrantAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Long getWarrantAmount() {
        return warrantAmount;
    }

    /**
     * Sets the value of the warrantAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarrantAmount(Long value) {
        this.warrantAmount = value;
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
     * Gets the value of the additionalNotes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalNotes() {
        return additionalNotes;
    }

    /**
     * Sets the value of the additionalNotes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalNotes(String value) {
        this.additionalNotes = value;
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
