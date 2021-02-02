
package uk.gov.moj.sdt.ws._2013.mcol.breathingspaceschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for breathingSpaceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="breathingSpaceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="claimNumber" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}claimNumberType"/>
 *         &lt;element name="defendantId" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}defendantIdType"/>
 *         &lt;element name="breathingSpaceNotificationType" type="{http://ws.sdt.moj.gov.uk/2013/mcol/BreathingSpaceSchema}breathingSpaceNotificationType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "breathingSpaceType", propOrder = {
    "claimNumber",
    "defendantId",
    "breathingSpaceNotificationType"
})
public class BreathingSpaceType {

    @XmlElement(required = true)
    protected String claimNumber;
    @XmlElement(required = true)
    protected String defendantId;
    @XmlElement(required = true)
    protected BreathingSpaceNotificationType breathingSpaceNotificationType;

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
     * Gets the value of the breathingSpaceNotificationType property.
     * 
     * @return
     *     possible object is
     *     {@link BreathingSpaceNotificationType }
     *     
     */
    public BreathingSpaceNotificationType getBreathingSpaceNotificationType() {
        return breathingSpaceNotificationType;
    }

    /**
     * Sets the value of the breathingSpaceNotificationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BreathingSpaceNotificationType }
     *     
     */
    public void setBreathingSpaceNotificationType(BreathingSpaceNotificationType value) {
        this.breathingSpaceNotificationType = value;
    }

}
