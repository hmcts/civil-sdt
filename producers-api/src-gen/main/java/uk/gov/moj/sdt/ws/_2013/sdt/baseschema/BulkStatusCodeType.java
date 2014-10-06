
package uk.gov.moj.sdt.ws._2013.sdt.baseschema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for bulkStatusCodeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="bulkStatusCodeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Validated"/>
 *     &lt;enumeration value="Completed"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "bulkStatusCodeType")
@XmlEnum
public enum BulkStatusCodeType {

    @XmlEnumValue("Validated")
    VALIDATED("Validated"),
    @XmlEnumValue("Completed")
    COMPLETED("Completed");
    private final String value;

    BulkStatusCodeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BulkStatusCodeType fromValue(String v) {
        for (BulkStatusCodeType c: BulkStatusCodeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
