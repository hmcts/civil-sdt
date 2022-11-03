
package uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackrequestschema;

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
 *         &lt;element name="sdtCustomerId" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}sdtCustomerIdType"/>
 *         &lt;element name="sdtBulkReference" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}sdtBulkReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "headerType", propOrder = {
        "sdtCustomerId",
        "sdtBulkReference"
})
public class HeaderType {

    protected long sdtCustomerId;
    @XmlElement(required = true)
    protected String sdtBulkReference;

    /**
     * Gets the value of the sdtCustomerId property.
     */
    public long getSdtCustomerId() {
        return sdtCustomerId;
    }

    /**
     * Sets the value of the sdtCustomerId property.
     */
    public void setSdtCustomerId(long value) {
        this.sdtCustomerId = value;
    }

    /**
     * Gets the value of the sdtBulkReference property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSdtBulkReference() {
        return sdtBulkReference;
    }

    /**
     * Sets the value of the sdtBulkReference property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSdtBulkReference(String value) {
        this.sdtBulkReference = value;
    }

}
