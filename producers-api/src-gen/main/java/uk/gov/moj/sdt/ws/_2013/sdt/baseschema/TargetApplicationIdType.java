
package uk.gov.moj.sdt.ws._2013.sdt.baseschema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for targetApplicationIdType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="targetApplicationIdType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="mcol"/>
 *     &lt;enumeration value="mcol_com"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "targetApplicationIdType")
@XmlEnum
public enum TargetApplicationIdType {

    @XmlEnumValue("mcol")
    MCOL("mcol"),
    @XmlEnumValue("mcol_com")
    MCOL_COM("mcol_com");
    private final String value;

    TargetApplicationIdType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TargetApplicationIdType fromValue(String v) {
        for (TargetApplicationIdType c: TargetApplicationIdType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
