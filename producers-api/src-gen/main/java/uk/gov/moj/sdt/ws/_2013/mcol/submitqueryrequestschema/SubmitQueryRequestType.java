
package uk.gov.moj.sdt.ws._2013.mcol.submitqueryrequestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for submitQueryRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="submitQueryRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://ws.sdt.moj.gov.uk/2013/mcol/SubmitQueryRequestSchema}headerType"/>
 *         &lt;element name="mcolCriteria" type="{http://ws.sdt.moj.gov.uk/2013/mcol/SubmitQueryRequestSchema}mcolCriteriaType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "submitQueryRequestType", propOrder = {
    "header",
    "mcolCriteria"
})
public class SubmitQueryRequestType {

    @XmlElement(required = true)
    protected HeaderType header;
    @XmlElement(required = true)
    protected McolCriteriaType mcolCriteria;

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
     * Gets the value of the mcolCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link McolCriteriaType }
     *     
     */
    public McolCriteriaType getMcolCriteria() {
        return mcolCriteria;
    }

    /**
     * Sets the value of the mcolCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link McolCriteriaType }
     *     
     */
    public void setMcolCriteria(McolCriteriaType value) {
        this.mcolCriteria = value;
    }

}
