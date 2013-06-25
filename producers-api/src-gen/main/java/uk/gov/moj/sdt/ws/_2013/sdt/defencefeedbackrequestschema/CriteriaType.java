
package uk.gov.moj.sdt.ws._2013.sdt.defencefeedbackrequestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for criteriaType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="criteriaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mcolDefence" type="{http://ws.sdt.moj.gov.uk/2013/sdt/DefenceFeedbackRequestSchema}mcolDefenceType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "criteriaType", propOrder = {
    "mcolDefence"
})
public class CriteriaType {

    @XmlElement(required = true)
    protected McolDefenceType mcolDefence;

    /**
     * Gets the value of the mcolDefence property.
     * 
     * @return
     *     possible object is
     *     {@link McolDefenceType }
     *     
     */
    public McolDefenceType getMcolDefence() {
        return mcolDefence;
    }

    /**
     * Sets the value of the mcolDefence property.
     * 
     * @param value
     *     allowed object is
     *     {@link McolDefenceType }
     *     
     */
    public void setMcolDefence(McolDefenceType value) {
        this.mcolDefence = value;
    }

}
