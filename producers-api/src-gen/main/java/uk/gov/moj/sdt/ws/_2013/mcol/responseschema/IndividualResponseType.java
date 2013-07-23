
package uk.gov.moj.sdt.ws._2013.mcol.responseschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.mcol.responsedetailschema.ResponseDetailType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.CreateStatusType;


/**
 * <p>Java class for individualResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="individualResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://ws.sdt.moj.gov.uk/2013/mcol/ResponseSchema}headerType"/>
 *         &lt;element name="mcolResponseDetail" type="{http://ws.sdt.moj.gov.uk/2013/mcol/ResponseDetailSchema}responseDetailType"/>
 *         &lt;element name="status" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}createStatusType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "individualResponseType", propOrder = {
    "header",
    "mcolResponseDetail",
    "status"
})
public class IndividualResponseType {

    @XmlElement(required = true)
    protected HeaderType header;
    @XmlElement(required = true)
    protected ResponseDetailType mcolResponseDetail;
    @XmlElement(required = true)
    protected CreateStatusType status;

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
     * Gets the value of the mcolResponseDetail property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseDetailType }
     *     
     */
    public ResponseDetailType getMcolResponseDetail() {
        return mcolResponseDetail;
    }

    /**
     * Sets the value of the mcolResponseDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseDetailType }
     *     
     */
    public void setMcolResponseDetail(ResponseDetailType value) {
        this.mcolResponseDetail = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link CreateStatusType }
     *     
     */
    public CreateStatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreateStatusType }
     *     
     */
    public void setStatus(CreateStatusType value) {
        this.status = value;
    }

}
