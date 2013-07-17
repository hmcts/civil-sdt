
package uk.gov.moj.sdt.ws._2013.mcol.submitqueryresponseschema;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusType;


/**
 * <p>Java class for submitQueryResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="submitQueryResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mcolCustomerId" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}mcolCustomerIdType"/>
 *         &lt;element name="resultCount" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;element name="mcolResults" type="{http://ws.sdt.moj.gov.uk/2013/mcol/SubmitQueryResponseSchema}mcolResultsType"/>
 *         &lt;element name="status" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}statusType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "submitQueryResponseType", propOrder = {
    "mcolCustomerId",
    "resultCount",
    "mcolResults",
    "status"
})
public class SubmitQueryResponseType {

    @XmlElement(required = true)
    protected String mcolCustomerId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger resultCount;
    @XmlElement(required = true)
    protected McolResultsType mcolResults;
    @XmlElement(required = true)
    protected StatusType status;

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
     * Gets the value of the resultCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getResultCount() {
        return resultCount;
    }

    /**
     * Sets the value of the resultCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setResultCount(BigInteger value) {
        this.resultCount = value;
    }

    /**
     * Gets the value of the mcolResults property.
     * 
     * @return
     *     possible object is
     *     {@link McolResultsType }
     *     
     */
    public McolResultsType getMcolResults() {
        return mcolResults;
    }

    /**
     * Sets the value of the mcolResults property.
     * 
     * @param value
     *     allowed object is
     *     {@link McolResultsType }
     *     
     */
    public void setMcolResults(McolResultsType value) {
        this.mcolResults = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link StatusType }
     *     
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusType }
     *     
     */
    public void setStatus(StatusType value) {
        this.status = value;
    }

}
