
package uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.TargetApplicationIdType;


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
 *         &lt;element name="requestCount" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}requestCountType"/>
 *         &lt;element name="customerReference" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}customerReferenceType"/>
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
    "sdtCustomerId",
    "targetApplicationId",
    "requestCount",
    "customerReference"
})
public class HeaderType {

    @XmlElement(required = true)
    protected BigInteger sdtCustomerId;
    @XmlElement(required = true)
    protected TargetApplicationIdType targetApplicationId;
    @XmlElement(required = true)
    protected BigInteger requestCount;
    @XmlElement(required = true)
    protected String customerReference;

    /**
     * Gets the value of the sdtCustomerId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSdtCustomerId() {
        return sdtCustomerId;
    }

    /**
     * Sets the value of the sdtCustomerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSdtCustomerId(BigInteger value) {
        this.sdtCustomerId = value;
    }

    /**
     * Gets the value of the targetApplicationId property.
     * 
     * @return
     *     possible object is
     *     {@link TargetApplicationIdType }
     *     
     */
    public TargetApplicationIdType getTargetApplicationId() {
        return targetApplicationId;
    }

    /**
     * Sets the value of the targetApplicationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetApplicationIdType }
     *     
     */
    public void setTargetApplicationId(TargetApplicationIdType value) {
        this.targetApplicationId = value;
    }

    /**
     * Gets the value of the requestCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRequestCount() {
        return requestCount;
    }

    /**
     * Sets the value of the requestCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRequestCount(BigInteger value) {
        this.requestCount = value;
    }

    /**
     * Gets the value of the customerReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerReference() {
        return customerReference;
    }

    /**
     * Sets the value of the customerReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerReference(String value) {
        this.customerReference = value;
    }

}
