
package uk.gov.moj.sdt.ws._2013.mcol.defencefeedbackrequestschema;

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
 *         &lt;element name="mcolCustomerId" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}mcolCustomerIdType"/>
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
    "mcolCustomerId"
})
public class HeaderType {

    @XmlElement(required = true)
    protected String mcolCustomerId;

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

}
