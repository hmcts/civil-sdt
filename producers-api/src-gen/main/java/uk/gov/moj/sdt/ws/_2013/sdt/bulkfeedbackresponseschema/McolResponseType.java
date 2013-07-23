
package uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.IndividualStatusType;


/**
 * <p>Java class for mcolResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mcolResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema}responseType">
 *       &lt;sequence>
 *         &lt;any/>
 *         &lt;element name="status" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}individualStatusType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mcolResponseType", propOrder = {
    "any",
    "status"
})
public class McolResponseType
    extends ResponseType
{

    @XmlAnyElement(lax = true)
    protected Object any;
    @XmlElement(required = true)
    protected IndividualStatusType status;

    /**
     * Gets the value of the any property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getAny() {
        return any;
    }

    /**
     * Sets the value of the any property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setAny(Object value) {
        this.any = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link IndividualStatusType }
     *     
     */
    public IndividualStatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link IndividualStatusType }
     *     
     */
    public void setStatus(IndividualStatusType value) {
        this.status = value;
    }

}
