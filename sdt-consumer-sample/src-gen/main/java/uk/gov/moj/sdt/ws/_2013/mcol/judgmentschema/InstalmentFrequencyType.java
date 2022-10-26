
package uk.gov.moj.sdt.ws._2013.mcol.judgmentschema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for instalmentFrequencyType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="instalmentFrequencyType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="W"/>
 *     &lt;enumeration value="F"/>
 *     &lt;enumeration value="M"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "instalmentFrequencyType")
@XmlEnum
public enum InstalmentFrequencyType {

    W,
    F,
    M;

    public String value() {
        return name();
    }

    public static InstalmentFrequencyType fromValue(String v) {
        return valueOf(v);
    }

}
