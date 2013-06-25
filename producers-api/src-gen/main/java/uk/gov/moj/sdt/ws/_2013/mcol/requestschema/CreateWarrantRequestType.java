
package uk.gov.moj.sdt.ws._2013.mcol.requestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.mcol.warrantschema.WarrantType;


/**
 * <p>Java class for createWarrantRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createWarrantRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://ws.sdt.moj.gov.uk/2013/mcol/RequestSchema}headerType"/>
 *         &lt;element name="mcolWarrant" type="{http://ws.sdt.moj.gov.uk/2013/mcol/WarrantSchema}warrantType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createWarrantRequestType", propOrder = {
    "header",
    "mcolWarrant"
})
public class CreateWarrantRequestType {

    @XmlElement(required = true)
    protected HeaderType header;
    @XmlElement(required = true)
    protected WarrantType mcolWarrant;

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
     * Gets the value of the mcolWarrant property.
     * 
     * @return
     *     possible object is
     *     {@link WarrantType }
     *     
     */
    public WarrantType getMcolWarrant() {
        return mcolWarrant;
    }

    /**
     * Sets the value of the mcolWarrant property.
     * 
     * @param value
     *     allowed object is
     *     {@link WarrantType }
     *     
     */
    public void setMcolWarrant(WarrantType value) {
        this.mcolWarrant = value;
    }

}
