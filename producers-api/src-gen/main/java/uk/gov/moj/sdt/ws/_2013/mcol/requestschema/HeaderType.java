
package uk.gov.moj.sdt.ws._2013.mcol.requestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.SotSignatureType;


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
 *         &lt;element name="mcolCustomerId" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}mcolCustomerIdType"/>
 *         &lt;element name="sotSignature" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}sotSignatureType"/>
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
    "mcolCustomerId",
    "sotSignature"
})
public class HeaderType {

    @XmlElement(required = true)
    protected String sdtRequestId;
    @XmlElement(required = true)
    protected String mcolCustomerId;
    @XmlElement(required = true)
    protected SotSignatureType sotSignature;

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
     * Gets the value of the mcolCustomerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMcolCustomerId() {
        return mcolCustomerId;
    }

    /**
     * Sets the value of the mcolCustomerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMcolCustomerId(String value) {
        this.mcolCustomerId = value;
    }

    /**
     * Gets the value of the sotSignature property.
     * 
     * @return
     *     possible object is
     *     {@link SotSignatureType }
     *     
     */
    public SotSignatureType getSotSignature() {
        return sotSignature;
    }

    /**
     * Sets the value of the sotSignature property.
     * 
     * @param value
     *     allowed object is
     *     {@link SotSignatureType }
     *     
     */
    public void setSotSignature(SotSignatureType value) {
        this.sotSignature = value;
    }

}
