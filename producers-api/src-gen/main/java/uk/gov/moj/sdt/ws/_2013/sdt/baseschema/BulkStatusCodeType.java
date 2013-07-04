
package uk.gov.moj.sdt.ws._2013.sdt.baseschema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for bulkStatusCodeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="bulkStatusCodeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="UPLOADED"/>
 *     &lt;enumeration value="FAILED"/>
 *     &lt;enumeration value="VALIDATED"/>
 *     &lt;enumeration value="COMPLETED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "bulkStatusCodeType")
@XmlEnum
public enum BulkStatusCodeType {

    UPLOADED,
    FAILED,
    VALIDATED,
    COMPLETED;

    public String value() {
        return name();
    }

    public static BulkStatusCodeType fromValue(String v) {
        return valueOf(v);
    }

}
