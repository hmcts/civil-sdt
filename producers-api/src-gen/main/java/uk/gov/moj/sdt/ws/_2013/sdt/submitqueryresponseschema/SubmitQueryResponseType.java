
package uk.gov.moj.sdt.ws._2013.sdt.submitqueryresponseschema;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.TargetApplicationIdType;


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
 *         &lt;element name="sdtCustomerId" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}sdtCustomerIdType"/>
 *         &lt;element name="targetApplicationId" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}targetApplicationIdType"/>
 *         &lt;element name="resultCount" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;element name="results" type="{http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryResponseSchema}resultsType"/>
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
    "sdtCustomerId",
    "targetApplicationId",
    "resultCount",
    "results",
    "status"
})
public class SubmitQueryResponseType {

    @XmlElement(required = true)
    protected BigInteger sdtCustomerId;
    @XmlElement(required = true)
    protected TargetApplicationIdType targetApplicationId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger resultCount;
    @XmlElement(required = true)
    protected ResultsType results;
    @XmlElement(required = true)
    protected StatusType status;

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
     * Gets the value of the results property.
     * 
     * @return
     *     possible object is
     *     {@link ResultsType }
     *     
     */
    public ResultsType getResults() {
        return results;
    }

    /**
     * Sets the value of the results property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultsType }
     *     
     */
    public void setResults(ResultsType value) {
        this.results = value;
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
