
package uk.gov.moj.sdt.ws._2013.sdt.baseschema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for criteriaTypeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="criteriaTypeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="mcolDefenceCriteria"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "criteriaTypeType")
@XmlEnum
public enum CriteriaTypeType {

    @XmlEnumValue("mcolDefenceCriteria")
    MCOL_DEFENCE_CRITERIA("mcolDefenceCriteria");
    private final String value;

    CriteriaTypeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CriteriaTypeType fromValue(String v) {
        for (CriteriaTypeType c: CriteriaTypeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
