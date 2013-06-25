
package uk.gov.moj.sdt.ws._2013.sdt.baseschema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for createStatusCodeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="createStatusCodeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ACCEPTED"/>
 *     &lt;enumeration value="INITIALLY_ACCEPTED"/>
 *     &lt;enumeration value="REJECTED"/>
 *     &lt;enumeration value="ERROR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "createStatusCodeType")
@XmlEnum
public enum CreateStatusCodeType {

    ACCEPTED,
    INITIALLY_ACCEPTED,
    REJECTED,
    ERROR;

    public String value() {
        return name();
    }

    public static CreateStatusCodeType fromValue(String v) {
        return valueOf(v);
    }

}
