
package uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema;

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
 *         &lt;element name="targetApplicationId" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}targetApplicationIdType"/>
 *         &lt;element name="queryReference" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "headerType", propOrder = {
        "sdtCustomerId",
        "targetApplicationId",
        "queryReference"
})
public class HeaderType {

    protected long sdtCustomerId;
    @XmlElement(required = true)
    protected String targetApplicationId;
    protected String queryReference;

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
     * Gets the value of the targetApplicationId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTargetApplicationId() {
        return targetApplicationId;
    }

    /**
     * Sets the value of the targetApplicationId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTargetApplicationId(String value) {
        this.targetApplicationId = value;
    }

    /**
     * Gets the value of the queryReference property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getQueryReference() {
        return queryReference;
    }

    /**
     * Sets the value of the queryReference property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setQueryReference(String value) {
        this.queryReference = value;
    }

}
