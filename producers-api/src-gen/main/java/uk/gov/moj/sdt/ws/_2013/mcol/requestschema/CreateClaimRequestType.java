
package uk.gov.moj.sdt.ws._2013.mcol.requestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.mcol.claimschema.ClaimType;


/**
 * <p>Java class for createClaimRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createClaimRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://ws.sdt.moj.gov.uk/2013/mcol/RequestSchema}headerType"/>
 *         &lt;element name="mcolClaim" type="{http://ws.sdt.moj.gov.uk/2013/mcol/ClaimSchema}claimType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createClaimRequestType", propOrder = {
    "header",
    "mcolClaim"
})
public class CreateClaimRequestType {

    @XmlElement(required = true)
    protected HeaderType header;
    @XmlElement(required = true)
    protected ClaimType mcolClaim;

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
     * Gets the value of the mcolClaim property.
     * 
     * @return
     *     possible object is
     *     {@link ClaimType }
     *     
     */
    public ClaimType getMcolClaim() {
        return mcolClaim;
    }

    /**
     * Sets the value of the mcolClaim property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClaimType }
     *     
     */
    public void setMcolClaim(ClaimType value) {
        this.mcolClaim = value;
    }

}
