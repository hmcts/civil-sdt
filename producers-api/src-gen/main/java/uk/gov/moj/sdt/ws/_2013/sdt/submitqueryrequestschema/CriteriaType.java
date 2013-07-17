
package uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *       &lt;choice>
 *         &lt;element name="mcolCriteria" type="{http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryRequestSchema}mcolCriteriaType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "criteriaType", propOrder = {
    "mcolCriteria"
})
public class CriteriaType {

    protected McolCriteriaType mcolCriteria;

    /**
     * Gets the value of the mcolCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link McolCriteriaType }
     *     
     */
    public McolCriteriaType getMcolCriteria() {
        return mcolCriteria;
    }

    /**
     * Sets the value of the mcolCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link McolCriteriaType }
     *     
     */
    public void setMcolCriteria(McolCriteriaType value) {
        this.mcolCriteria = value;
    }

}
