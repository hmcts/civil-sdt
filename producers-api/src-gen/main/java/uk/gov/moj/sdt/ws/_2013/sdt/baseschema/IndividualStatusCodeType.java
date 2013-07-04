
package uk.gov.moj.sdt.ws._2013.sdt.baseschema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for individualStatusCodeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="individualStatusCodeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="RECEIVED"/>
 *     &lt;enumeration value="FORWARDED"/>
 *     &lt;enumeration value="INITIALLY_ACCEPTED"/>
 *     &lt;enumeration value="ACCEPTED"/>
 *     &lt;enumeration value="REJECTED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "individualStatusCodeType")
@XmlEnum
public enum IndividualStatusCodeType {

    RECEIVED,
    FORWARDED,
    INITIALLY_ACCEPTED,
    ACCEPTED,
    REJECTED;

    public String value() {
        return name();
    }

    public static IndividualStatusCodeType fromValue(String v) {
        return valueOf(v);
    }

}
