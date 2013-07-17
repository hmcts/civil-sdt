
package uk.gov.moj.sdt.ws._2013.mcol.submitqueryresponseschema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for responseType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="responseType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DE"/>
 *     &lt;enumeration value="DC"/>
 *     &lt;enumeration value="PA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "responseType")
@XmlEnum
public enum ResponseType {

    DE,
    DC,
    PA;

    public String value() {
        return name();
    }

    public static ResponseType fromValue(String v) {
        return valueOf(v);
    }

}
