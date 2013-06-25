
package uk.gov.moj.sdt.ws._2013.mcol.claimschema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter3;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.PersonType;


/**
 * Represents Claim document.
 * 
 * <p>Java class for claimType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="claimType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="claimantReference" type="{http://ws.sdt.moj.gov.uk/2013/mcol/ClaimSchema}claimantReferenceType" minOccurs="0"/>
 *         &lt;element name="claimant" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}personType" minOccurs="0"/>
 *         &lt;element name="claimantCorrespondence" type="{http://ws.sdt.moj.gov.uk/2013/mcol/ClaimSchema}correspondenceDetailType" minOccurs="0"/>
 *         &lt;element name="defendant1" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}personType"/>
 *         &lt;element name="defendant2" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}personType" minOccurs="0"/>
 *         &lt;element name="sendParticularsSeparately" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="interest" type="{http://ws.sdt.moj.gov.uk/2013/mcol/ClaimSchema}interestType" minOccurs="0"/>
 *         &lt;element name="claimAmount" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
 *         &lt;element name="solicitorCost" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
 *         &lt;element name="particulars" type="{http://ws.sdt.moj.gov.uk/2013/mcol/ClaimSchema}particularsType" maxOccurs="24"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "claimType", propOrder = {
    "claimantReference",
    "claimant",
    "claimantCorrespondence",
    "defendant1",
    "defendant2",
    "sendParticularsSeparately",
    "interest",
    "claimAmount",
    "solicitorCost",
    "particulars"
})
public class ClaimType {

    protected String claimantReference;
    protected PersonType claimant;
    protected CorrespondenceDetailType claimantCorrespondence;
    @XmlElement(required = true)
    protected PersonType defendant1;
    protected PersonType defendant2;
    protected boolean sendParticularsSeparately;
    protected InterestType interest;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter3 .class)
    @XmlSchemaType(name = "unsignedLong")
    protected Long claimAmount;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter3 .class)
    @XmlSchemaType(name = "unsignedLong")
    protected Long solicitorCost;
    @XmlElement(required = true)
    protected List<String> particulars;

    /**
     * Gets the value of the claimantReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaimantReference() {
        return claimantReference;
    }

    /**
     * Sets the value of the claimantReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaimantReference(String value) {
        this.claimantReference = value;
    }

    /**
     * Gets the value of the claimant property.
     * 
     * @return
     *     possible object is
     *     {@link PersonType }
     *     
     */
    public PersonType getClaimant() {
        return claimant;
    }

    /**
     * Sets the value of the claimant property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonType }
     *     
     */
    public void setClaimant(PersonType value) {
        this.claimant = value;
    }

    /**
     * Gets the value of the claimantCorrespondence property.
     * 
     * @return
     *     possible object is
     *     {@link CorrespondenceDetailType }
     *     
     */
    public CorrespondenceDetailType getClaimantCorrespondence() {
        return claimantCorrespondence;
    }

    /**
     * Sets the value of the claimantCorrespondence property.
     * 
     * @param value
     *     allowed object is
     *     {@link CorrespondenceDetailType }
     *     
     */
    public void setClaimantCorrespondence(CorrespondenceDetailType value) {
        this.claimantCorrespondence = value;
    }

    /**
     * Gets the value of the defendant1 property.
     * 
     * @return
     *     possible object is
     *     {@link PersonType }
     *     
     */
    public PersonType getDefendant1() {
        return defendant1;
    }

    /**
     * Sets the value of the defendant1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonType }
     *     
     */
    public void setDefendant1(PersonType value) {
        this.defendant1 = value;
    }

    /**
     * Gets the value of the defendant2 property.
     * 
     * @return
     *     possible object is
     *     {@link PersonType }
     *     
     */
    public PersonType getDefendant2() {
        return defendant2;
    }

    /**
     * Sets the value of the defendant2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonType }
     *     
     */
    public void setDefendant2(PersonType value) {
        this.defendant2 = value;
    }

    /**
     * Gets the value of the sendParticularsSeparately property.
     * 
     */
    public boolean isSendParticularsSeparately() {
        return sendParticularsSeparately;
    }

    /**
     * Sets the value of the sendParticularsSeparately property.
     * 
     */
    public void setSendParticularsSeparately(boolean value) {
        this.sendParticularsSeparately = value;
    }

    /**
     * Gets the value of the interest property.
     * 
     * @return
     *     possible object is
     *     {@link InterestType }
     *     
     */
    public InterestType getInterest() {
        return interest;
    }

    /**
     * Sets the value of the interest property.
     * 
     * @param value
     *     allowed object is
     *     {@link InterestType }
     *     
     */
    public void setInterest(InterestType value) {
        this.interest = value;
    }

    /**
     * Gets the value of the claimAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Long getClaimAmount() {
        return claimAmount;
    }

    /**
     * Sets the value of the claimAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaimAmount(Long value) {
        this.claimAmount = value;
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
     * Gets the value of the particulars property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the particulars property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParticulars().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getParticulars() {
        if (particulars == null) {
            particulars = new ArrayList<String>();
        }
        return this.particulars;
    }

}
