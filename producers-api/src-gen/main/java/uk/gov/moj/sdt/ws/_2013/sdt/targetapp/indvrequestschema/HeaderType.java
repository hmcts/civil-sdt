
package uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvrequestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for headerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="headerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sdtRequestId" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}sdtRequestIdType"/>
 *         &lt;element name="targetAppCustomerId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="requestType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "headerType", propOrder = {
    "sdtRequestId",
    "targetAppCustomerId",
    "requestType"
})
public class HeaderType {

    @XmlElement(required = true)
    protected String sdtRequestId;
    @XmlElement(required = true)
    protected String targetAppCustomerId;
    @XmlElement(required = true)
    protected String requestType;

    /**
     * Gets the value of the sdtRequestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSdtRequestId() {
        return sdtRequestId;
    }

    /**
     * Sets the value of the sdtRequestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSdtRequestId(String value) {
        this.sdtRequestId = value;
    }

    /**
     * Gets the value of the targetAppCustomerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetAppCustomerId() {
        return targetAppCustomerId;
    }

    /**
     * Sets the value of the targetAppCustomerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetAppCustomerId(String value) {
        this.targetAppCustomerId = value;
    }

    /**
     * Gets the value of the requestType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Sets the value of the requestType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestType(String value) {
        this.requestType = value;
    }

}
