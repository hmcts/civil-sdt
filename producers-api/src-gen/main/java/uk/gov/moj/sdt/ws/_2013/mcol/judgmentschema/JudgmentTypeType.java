
package uk.gov.moj.sdt.ws._2013.mcol.judgmentschema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for judgmentTypeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="judgmentTypeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="A"/>
 *     &lt;enumeration value="D"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "judgmentTypeType")
@XmlEnum
public enum JudgmentTypeType {

    A,
    D;

    public String value() {
        return name();
    }

    public static JudgmentTypeType fromValue(String v) {
        return valueOf(v);
    }

}
