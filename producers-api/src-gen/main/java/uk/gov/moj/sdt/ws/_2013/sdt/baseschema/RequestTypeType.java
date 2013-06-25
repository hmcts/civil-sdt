
package uk.gov.moj.sdt.ws._2013.sdt.baseschema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for requestTypeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="requestTypeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="mcolClaim"/>
 *     &lt;enumeration value="mcolJudgment"/>
 *     &lt;enumeration value="mcolJudgmentWarrant"/>
 *     &lt;enumeration value="mcolWarrant"/>
 *     &lt;enumeration value="mcolClaimStatusUpdate"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "requestTypeType")
@XmlEnum
public enum RequestTypeType {

    @XmlEnumValue("mcolClaim")
    MCOL_CLAIM("mcolClaim"),
    @XmlEnumValue("mcolJudgment")
    MCOL_JUDGMENT("mcolJudgment"),
    @XmlEnumValue("mcolJudgmentWarrant")
    MCOL_JUDGMENT_WARRANT("mcolJudgmentWarrant"),
    @XmlEnumValue("mcolWarrant")
    MCOL_WARRANT("mcolWarrant"),
    @XmlEnumValue("mcolClaimStatusUpdate")
    MCOL_CLAIM_STATUS_UPDATE("mcolClaimStatusUpdate");
    private final String value;

    RequestTypeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RequestTypeType fromValue(String v) {
        for (RequestTypeType c: RequestTypeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
