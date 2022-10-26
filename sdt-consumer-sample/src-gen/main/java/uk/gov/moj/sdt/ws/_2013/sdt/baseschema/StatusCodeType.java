
package uk.gov.moj.sdt.ws._2013.sdt.baseschema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for statusCodeType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="statusCodeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Ok"/>
 *     &lt;enumeration value="Error"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "statusCodeType")
@XmlEnum
public enum StatusCodeType {

    @XmlEnumValue("Ok")
    OK("Ok"),
    @XmlEnumValue("Error")
    ERROR("Error");
    private final String value;

    StatusCodeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static StatusCodeType fromValue(String v) {
        for (StatusCodeType c : StatusCodeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
