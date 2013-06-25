
package uk.gov.moj.sdt.ws._2013.mcol.requestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.mcol.claimstatusupdateschema.ClaimStatusUpdateType;


/**
 * <p>Java class for updateClaimRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateClaimRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://ws.sdt.moj.gov.uk/2013/mcol/RequestSchema}headerType"/>
 *         &lt;element name="mcolClaimStatusUpdate" type="{http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema}claimStatusUpdateType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateClaimRequestType", propOrder = {
    "header",
    "mcolClaimStatusUpdate"
})
public class UpdateClaimRequestType {

    @XmlElement(required = true)
    protected HeaderType header;
    @XmlElement(required = true)
    protected ClaimStatusUpdateType mcolClaimStatusUpdate;

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
     * Gets the value of the mcolClaimStatusUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link ClaimStatusUpdateType }
     *     
     */
    public ClaimStatusUpdateType getMcolClaimStatusUpdate() {
        return mcolClaimStatusUpdate;
    }

    /**
     * Sets the value of the mcolClaimStatusUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClaimStatusUpdateType }
     *     
     */
    public void setMcolClaimStatusUpdate(ClaimStatusUpdateType value) {
        this.mcolClaimStatusUpdate = value;
    }

}
