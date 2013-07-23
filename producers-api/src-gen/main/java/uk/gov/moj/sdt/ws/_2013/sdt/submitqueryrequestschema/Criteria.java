
package uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.CriteriaTypeType;


/**
 * <p>Java class for criteria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="criteria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="criteriaType" use="required" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}criteriaTypeType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "criteria")
@XmlSeeAlso({
    McolCriteriaType.class
})
public abstract class Criteria {

    @XmlAttribute(name = "criteriaType", required = true)
    protected CriteriaTypeType criteriaType;

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
