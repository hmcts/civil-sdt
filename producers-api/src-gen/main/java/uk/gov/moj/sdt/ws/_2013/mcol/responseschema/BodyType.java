
package uk.gov.moj.sdt.ws._2013.mcol.responseschema;

import java.util.Calendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter2;
import org.w3._2001.xmlschema.Adapter3;


/**
 * <p>Java class for bodyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bodyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="claimNumber" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}claimNumberType" minOccurs="0"/>
 *         &lt;element name="issueDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="serviceDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="warrantNumber" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}warrantNumberType" minOccurs="0"/>
 *         &lt;element name="enforcingCourtCode" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}courtCodeType" minOccurs="0"/>
 *         &lt;element name="enforcingCourtName" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}courtNameType" minOccurs="0"/>
 *         &lt;element name="fee" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
 *         &lt;element name="additionalStatus" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bodyType", propOrder = {
    "claimNumber",
    "issueDate",
    "serviceDate",
    "warrantNumber",
    "enforcingCourtCode",
    "enforcingCourtName",
    "fee",
    "additionalStatus"
})
public class BodyType {

    protected String claimNumber;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar issueDate;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar serviceDate;
    protected String warrantNumber;
    protected String enforcingCourtCode;
    protected String enforcingCourtName;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter3 .class)
    @XmlSchemaType(name = "unsignedLong")
    protected Long fee;
    protected String additionalStatus;

    /**
     * Gets the value of the claimNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaimNumber() {
        return claimNumber;
    }

    /**
     * Sets the value of the claimNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaimNumber(String value) {
        this.claimNumber = value;
    }

    /**
     * Gets the value of the issueDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getIssueDate() {
        return issueDate;
    }

    /**
     * Sets the value of the issueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueDate(Calendar value) {
        this.issueDate = value;
    }

    /**
     * Gets the value of the serviceDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getServiceDate() {
        return serviceDate;
    }

    /**
     * Sets the value of the serviceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceDate(Calendar value) {
        this.serviceDate = value;
    }

    /**
     * Gets the value of the warrantNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarrantNumber() {
        return warrantNumber;
    }

    /**
     * Sets the value of the warrantNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarrantNumber(String value) {
        this.warrantNumber = value;
    }

    /**
     * Gets the value of the enforcingCourtCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnforcingCourtCode() {
        return enforcingCourtCode;
    }

    /**
     * Sets the value of the enforcingCourtCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnforcingCourtCode(String value) {
        this.enforcingCourtCode = value;
    }

    /**
     * Gets the value of the enforcingCourtName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnforcingCourtName() {
        return enforcingCourtName;
    }

    /**
     * Sets the value of the enforcingCourtName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnforcingCourtName(String value) {
        this.enforcingCourtName = value;
    }

    /**
     * Gets the value of the fee property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Long getFee() {
        return fee;
    }

    /**
     * Sets the value of the fee property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFee(Long value) {
        this.fee = value;
    }

    /**
     * Gets the value of the additionalStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalStatus() {
        return additionalStatus;
    }

    /**
     * Sets the value of the additionalStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalStatus(String value) {
        this.additionalStatus = value;
    }

}
