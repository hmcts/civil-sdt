
package uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema;

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
 *         &lt;element name="criterion" type="{http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryRequestSchema}criterionType"/>
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
    "criterion"
})
public class CriteriaType {

    @XmlElement(required = true)
    protected CriterionType criterion;

    /**
     * Gets the value of the criterion property.
     * 
     * @return
     *     possible object is
     *     {@link CriterionType }
     *     
     */
    public CriterionType getCriterion() {
        return criterion;
    }

    /**
     * Sets the value of the criterion property.
     * 
     * @param value
     *     allowed object is
     *     {@link CriterionType }
     *     
     */
    public void setCriterion(CriterionType value) {
        this.criterion = value;
    }

}
