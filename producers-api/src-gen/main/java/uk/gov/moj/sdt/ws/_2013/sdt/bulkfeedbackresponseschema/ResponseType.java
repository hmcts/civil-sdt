
package uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.RequestTypeType;


/**
 * <p>Java class for responseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="responseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="requestType" use="required" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}requestTypeType" />
 *       &lt;attribute name="requestId" use="required" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}requestIdType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseType")
@XmlSeeAlso({
    McolResponseType.class
})
public abstract class ResponseType {

    @XmlAttribute(name = "requestType", required = true)
    protected RequestTypeType requestType;
    @XmlAttribute(name = "requestId", required = true)
    protected String requestId;

    /**
     * Gets the value of the requestType property.
     * 
     * @return
     *     possible object is
     *     {@link RequestTypeType }
     *     
     */
    public RequestTypeType getRequestType() {
        return requestType;
    }

    /**
     * Sets the value of the requestType property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestTypeType }
     *     
     */
    public void setRequestType(RequestTypeType value) {
        this.requestType = value;
    }

    /**
     * Gets the value of the requestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the value of the requestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestId(String value) {
        this.requestId = value;
    }

}
