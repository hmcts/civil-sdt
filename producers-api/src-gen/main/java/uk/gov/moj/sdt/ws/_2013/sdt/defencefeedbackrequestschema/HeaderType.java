
package uk.gov.moj.sdt.ws._2013.sdt.defencefeedbackrequestschema;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.TargetApplicationIdType;


/**
 * <p>Java class for headerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="headerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sdtCustomerId" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}sdtCustomerIdType"/>
 *         &lt;element name="targetApplicationId" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}targetApplicationIdType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "headerType", propOrder = {
    "sdtCustomerId",
    "targetApplicationId"
})
public class HeaderType {

    @XmlElement(required = true)
    protected BigInteger sdtCustomerId;
    @XmlElement(required = true)
    protected TargetApplicationIdType targetApplicationId;

    /**
     * Gets the value of the sdtCustomerId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSdtCustomerId() {
        return sdtCustomerId;
    }

    /**
     * Sets the value of the sdtCustomerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSdtCustomerId(BigInteger value) {
        this.sdtCustomerId = value;
    }

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

}
