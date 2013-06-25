
package uk.gov.moj.sdt.ws._2013.mcol.defencefeedbackrequestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for defenceRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="defenceRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://ws.sdt.moj.gov.uk/2013/mcol/DefenceFeedbackRequestSchema}headerType"/>
 *         &lt;element name="mcolDefence" type="{http://ws.sdt.moj.gov.uk/2013/mcol/DefenceFeedbackRequestSchema}mcolDefenceType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "defenceRequestType", propOrder = {
    "header",
    "mcolDefence"
})
public class DefenceRequestType {

    @XmlElement(required = true)
    protected HeaderType header;
    @XmlElement(required = true)
    protected McolDefenceType mcolDefence;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link HeaderType }
     *     
     */
    public HeaderType getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderType }
     *     
     */
    public void setHeader(HeaderType value) {
        this.header = value;
    }

    /**
     * Gets the value of the mcolDefence property.
     * 
     * @return
     *     possible object is
     *     {@link McolDefenceType }
     *     
     */
    public McolDefenceType getMcolDefence() {
        return mcolDefence;
    }

    /**
     * Sets the value of the mcolDefence property.
     * 
     * @param value
     *     allowed object is
     *     {@link McolDefenceType }
     *     
     */
    public void setMcolDefence(McolDefenceType value) {
        this.mcolDefence = value;
    }

}
