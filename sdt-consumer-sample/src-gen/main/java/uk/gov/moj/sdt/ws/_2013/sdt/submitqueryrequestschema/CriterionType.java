
package uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import uk.gov.moj.sdt.ws._2013.mcol.queryschema.McolDefenceCriteriaType;


/**
 * <p>Java class for criterionType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="criterionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="mcolDefenceCriteria" type="{http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema}mcolDefenceCriteriaType"/>
 *       &lt;/choice>
 *       &lt;attribute name="criteriaType" use="required" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}criteriaTypeType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "criterionType", propOrder = {
        "mcolDefenceCriteria"
})
public class CriterionType {

    protected McolDefenceCriteriaType mcolDefenceCriteria;
    @XmlAttribute(name = "criteriaType", required = true)
    protected String criteriaType;

    /**
     * Gets the value of the mcolDefenceCriteria property.
     *
     * @return possible object is
     * {@link McolDefenceCriteriaType }
     */
    public McolDefenceCriteriaType getMcolDefenceCriteria() {
        return mcolDefenceCriteria;
    }

    /**
     * Sets the value of the mcolDefenceCriteria property.
     *
     * @param value allowed object is
     *              {@link McolDefenceCriteriaType }
     */
    public void setMcolDefenceCriteria(McolDefenceCriteriaType value) {
        this.mcolDefenceCriteria = value;
    }

    /**
     * Gets the value of the criteriaType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCriteriaType() {
        return criteriaType;
    }

    /**
     * Sets the value of the criteriaType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCriteriaType(String value) {
        this.criteriaType = value;
    }

}
