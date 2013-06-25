
package uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for bulkFeedbackResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bulkFeedbackResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bulkRequestStatus" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema}bulkRequestStatusType"/>
 *         &lt;element name="responses" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema}responsesType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bulkFeedbackResponseType", propOrder = {
    "bulkRequestStatus",
    "responses"
})
public class BulkFeedbackResponseType {

    @XmlElement(required = true)
    protected BulkRequestStatusType bulkRequestStatus;
    @XmlElement(required = true)
    protected ResponsesType responses;

    /**
     * Gets the value of the bulkRequestStatus property.
     * 
     * @return
     *     possible object is
     *     {@link BulkRequestStatusType }
     *     
     */
    public BulkRequestStatusType getBulkRequestStatus() {
        return bulkRequestStatus;
    }

    /**
     * Sets the value of the bulkRequestStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link BulkRequestStatusType }
     *     
     */
    public void setBulkRequestStatus(BulkRequestStatusType value) {
        this.bulkRequestStatus = value;
    }

    /**
     * Gets the value of the responses property.
     * 
     * @return
     *     possible object is
     *     {@link ResponsesType }
     *     
     */
    public ResponsesType getResponses() {
        return responses;
    }

    /**
     * Sets the value of the responses property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponsesType }
     *     
     */
    public void setResponses(ResponsesType value) {
        this.responses = value;
    }

}
