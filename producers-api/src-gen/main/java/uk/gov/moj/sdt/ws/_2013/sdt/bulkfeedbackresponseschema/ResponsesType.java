
package uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for responsesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="responsesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="mcolResponses" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema}mcolResponsesType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responsesType", propOrder = {
    "mcolResponses"
})
public class ResponsesType {

    protected McolResponsesType mcolResponses;

    /**
     * Gets the value of the mcolResponses property.
     * 
     * @return
     *     possible object is
     *     {@link McolResponsesType }
     *     
     */
    public McolResponsesType getMcolResponses() {
        return mcolResponses;
    }

    /**
     * Sets the value of the mcolResponses property.
     * 
     * @param value
     *     allowed object is
     *     {@link McolResponsesType }
     *     
     */
    public void setMcolResponses(McolResponsesType value) {
        this.mcolResponses = value;
    }

}
