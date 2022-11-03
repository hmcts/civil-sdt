
package uk.gov.moj.sdt.ws._2013.mcol.breathingspaceschema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for breathingSpaceNotificationType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="breathingSpaceNotificationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="BS"/>
 *     &lt;enumeration value="BC"/>
 *     &lt;enumeration value="MH"/>
 *     &lt;enumeration value="MC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "breathingSpaceNotificationType")
@XmlEnum
public enum BreathingSpaceNotificationType {

    BS,
    BC,
    MH,
    MC;

    public String value() {
        return name();
    }

    public static BreathingSpaceNotificationType fromValue(String v) {
        return valueOf(v);
    }

}
