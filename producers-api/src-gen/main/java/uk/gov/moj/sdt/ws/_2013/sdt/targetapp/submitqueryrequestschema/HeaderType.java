
package uk.gov.moj.sdt.ws._2013.sdt.targetapp.submitqueryrequestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.CriteriaTypeType;


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
 *         &lt;element name="targetAppCustomerId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="criteriaType" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}criteriaTypeType"/>
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
    "targetAppCustomerId",
    "criteriaType"
})
public class HeaderType {

    @XmlElement(required = true)
    protected String targetAppCustomerId;
    @XmlElement(required = true)
    protected CriteriaTypeType criteriaType;

    /**
     * Gets the value of the targetAppCustomerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetAppCustomerId() {
        return targetAppCustomerId;
    }

    /**
     * Sets the value of the targetAppCustomerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetAppCustomerId(String value) {
        this.targetAppCustomerId = value;
    }

    /**
     * Gets the value of the criteriaType property.
     * 
     * @return
     *     possible object is
     *     {@link CriteriaTypeType }
     *     
     */
    public CriteriaTypeType getCriteriaType() {
        return criteriaType;
    }

    /**
     * Sets the value of the criteriaType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CriteriaTypeType }
     *     
     */
    public void setCriteriaType(CriteriaTypeType value) {
        this.criteriaType = value;
    }

}
