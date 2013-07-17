
package uk.gov.moj.sdt.ws._2013.mcol.submitqueryrequestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mcolCriteriaType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mcolCriteriaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="mcolDefenceCriteria" type="{http://ws.sdt.moj.gov.uk/2013/mcol/SubmitQueryRequestSchema}mcolDefenceCriteriaType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mcolCriteriaType", propOrder = {
    "mcolDefenceCriteria"
})
public class McolCriteriaType {

    protected McolDefenceCriteriaType mcolDefenceCriteria;

    /**
     * Gets the value of the mcolDefenceCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link McolDefenceCriteriaType }
     *     
     */
    public McolDefenceCriteriaType getMcolDefenceCriteria() {
        return mcolDefenceCriteria;
    }

    /**
     * Sets the value of the mcolDefenceCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link McolDefenceCriteriaType }
     *     
     */
    public void setMcolDefenceCriteria(McolDefenceCriteriaType value) {
        this.mcolDefenceCriteria = value;
    }

}
