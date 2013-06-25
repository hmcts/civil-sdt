
package uk.gov.moj.sdt.ws._2013.sdt.defencefeedbackresponseschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.TargetApplicationIdType;


/**
 * <p>Java class for defenceResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="defenceResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="targetApplicationId" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}targetApplicationIdType"/>
 *         &lt;element name="defenceDetails" type="{http://ws.sdt.moj.gov.uk/2013/sdt/DefenceFeedbackResponseSchema}defenceDetailsType"/>
 *         &lt;element name="status" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}statusType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "defenceResponseType", propOrder = {
    "targetApplicationId",
    "defenceDetails",
    "status"
})
public class DefenceResponseType {

    @XmlElement(required = true)
    protected TargetApplicationIdType targetApplicationId;
    @XmlElement(required = true)
    protected DefenceDetailsType defenceDetails;
    @XmlElement(required = true)
    protected StatusType status;

    /**
     * Gets the value of the targetApplicationId property.
     * 
     * @return
     *     possible object is
     *     {@link TargetApplicationIdType }
     *     
     */
    public TargetApplicationIdType getTargetApplicationId() {
        return targetApplicationId;
    }

    /**
     * Sets the value of the targetApplicationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetApplicationIdType }
     *     
     */
    public void setTargetApplicationId(TargetApplicationIdType value) {
        this.targetApplicationId = value;
    }

    /**
     * Gets the value of the defenceDetails property.
     * 
     * @return
     *     possible object is
     *     {@link DefenceDetailsType }
     *     
     */
    public DefenceDetailsType getDefenceDetails() {
        return defenceDetails;
    }

    /**
     * Sets the value of the defenceDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefenceDetailsType }
     *     
     */
    public void setDefenceDetails(DefenceDetailsType value) {
        this.defenceDetails = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link StatusType }
     *     
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusType }
     *     
     */
    public void setStatus(StatusType value) {
        this.status = value;
    }

}
