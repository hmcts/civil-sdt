
package uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for requestsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="requestsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="mcolRequests" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BulkRequestSchema}mcolRequestsType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "requestsType", propOrder = {
    "mcolRequests"
})
public class RequestsType {

    protected McolRequestsType mcolRequests;

    /**
     * Gets the value of the mcolRequests property.
     * 
     * @return
     *     possible object is
     *     {@link McolRequestsType }
     *     
     */
    public McolRequestsType getMcolRequests() {
        return mcolRequests;
    }

    /**
     * Sets the value of the mcolRequests property.
     * 
     * @param value
     *     allowed object is
     *     {@link McolRequestsType }
     *     
     */
    public void setMcolRequests(McolRequestsType value) {
        this.mcolRequests = value;
    }

}
