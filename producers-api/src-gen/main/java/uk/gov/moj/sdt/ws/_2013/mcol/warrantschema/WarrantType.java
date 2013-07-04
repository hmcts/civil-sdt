
package uk.gov.moj.sdt.ws._2013.mcol.warrantschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.SotSignatureType;


/**
 * <p>Java class for warrantType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="warrantType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="warrantDetail" type="{http://ws.sdt.moj.gov.uk/2013/mcol/WarrantSchema}warrantDetailType"/>
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
@XmlType(name = "warrantType", propOrder = {
    "warrantDetail",
    "sotSignature"
})
public class WarrantType {

    @XmlElement(required = true)
    protected WarrantDetailType warrantDetail;
    @XmlElement(required = true)
    protected SotSignatureType sotSignature;

    /**
     * Gets the value of the warrantDetail property.
     * 
     * @return
     *     possible object is
     *     {@link WarrantDetailType }
     *     
     */
    public WarrantDetailType getWarrantDetail() {
        return warrantDetail;
    }

    /**
     * Sets the value of the warrantDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link WarrantDetailType }
     *     
     */
    public void setWarrantDetail(WarrantDetailType value) {
        this.warrantDetail = value;
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
