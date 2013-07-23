
package uk.gov.moj.sdt.ws._2013.sdt.individualupdaterequestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.UpdateStatusType;


/**
 * <p>Java class for updateRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://ws.sdt.moj.gov.uk/2013/sdt/IndividualUpdateRequestSchema}headerType"/>
 *         &lt;element name="updateDetail" type="{http://ws.sdt.moj.gov.uk/2013/sdt/IndividualUpdateRequestSchema}updateDetailType"/>
 *         &lt;element name="status" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}updateStatusType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateRequestType", propOrder = {
    "header",
    "updateDetail",
    "status"
})
public class UpdateRequestType {

    @XmlElement(required = true)
    protected HeaderType header;
    @XmlElement(required = true)
    protected UpdateDetailType updateDetail;
    @XmlElement(required = true)
    protected UpdateStatusType status;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link HeaderType }
     *     
     */
    public HeaderType getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderType }
     *     
     */
    public void setHeader(HeaderType value) {
        this.header = value;
    }

    /**
     * Gets the value of the updateDetail property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateDetailType }
     *     
     */
    public UpdateDetailType getUpdateDetail() {
        return updateDetail;
    }

    /**
     * Sets the value of the updateDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateDetailType }
     *     
     */
    public void setUpdateDetail(UpdateDetailType value) {
        this.updateDetail = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateStatusType }
     *     
     */
    public UpdateStatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateStatusType }
     *     
     */
    public void setStatus(UpdateStatusType value) {
        this.status = value;
    }

}
