
package uk.gov.moj.sdt.ws._2013.sdt.baseschema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateStatusCodeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="updateStatusCodeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ACCEPTED"/>
 *     &lt;enumeration value="REJECTED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "updateStatusCodeType")
@XmlEnum
public enum UpdateStatusCodeType {

    ACCEPTED,
    REJECTED;

    public String value() {
        return name();
    }

    public static UpdateStatusCodeType fromValue(String v) {
        return valueOf(v);
    }

}
