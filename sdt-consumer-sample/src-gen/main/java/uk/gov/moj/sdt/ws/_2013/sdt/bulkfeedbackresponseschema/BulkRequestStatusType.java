
package uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema;

import java.util.Calendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.w3._2001.xmlschema.Adapter1;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.AbstractResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.BulkStatusType;


/**
 * <p>Java class for bulkRequestStatusType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="bulkRequestStatusType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}abstractResponseType">
 *       &lt;sequence>
 *         &lt;element name="customerReference" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}customerReferenceType" minOccurs="0"/>
 *         &lt;element name="sdtBulkReference" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}sdtBulkReferenceType"/>
 *         &lt;element name="submittedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="sdtService" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="requestCount" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="bulkStatus" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}bulkStatusType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bulkRequestStatusType", propOrder = {
        "customerReference",
        "sdtBulkReference",
        "submittedDate",
        "sdtService",
        "requestCount",
        "bulkStatus"
})
public class BulkRequestStatusType
        extends AbstractResponseType {

    protected String customerReference;
    @XmlElement(required = true)
    protected String sdtBulkReference;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar submittedDate;
    @XmlElement(required = true)
    protected String sdtService;
    protected Long requestCount;
    protected BulkStatusType bulkStatus;

    /**
     * Gets the value of the customerReference property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCustomerReference() {
        return customerReference;
    }

    /**
     * Sets the value of the customerReference property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCustomerReference(String value) {
        this.customerReference = value;
    }

    /**
     * Gets the value of the sdtBulkReference property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSdtBulkReference() {
        return sdtBulkReference;
    }

    /**
     * Sets the value of the sdtBulkReference property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSdtBulkReference(String value) {
        this.sdtBulkReference = value;
    }

    /**
     * Gets the value of the submittedDate property.
     *
     * @return possible object is
     * {@link String }
     */
    public Calendar getSubmittedDate() {
        return submittedDate;
    }

    /**
     * Sets the value of the submittedDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSubmittedDate(Calendar value) {
        this.submittedDate = value;
    }

    /**
     * Gets the value of the sdtService property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSdtService() {
        return sdtService;
    }

    /**
     * Sets the value of the sdtService property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSdtService(String value) {
        this.sdtService = value;
    }

    /**
     * Gets the value of the requestCount property.
     *
     * @return possible object is
     * {@link Long }
     */
    public Long getRequestCount() {
        return requestCount;
    }

    /**
     * Sets the value of the requestCount property.
     *
     * @param value allowed object is
     *              {@link Long }
     */
    public void setRequestCount(Long value) {
        this.requestCount = value;
    }

    /**
     * Gets the value of the bulkStatus property.
     *
     * @return possible object is
     * {@link BulkStatusType }
     */
    public BulkStatusType getBulkStatus() {
        return bulkStatus;
    }

    /**
     * Sets the value of the bulkStatus property.
     *
     * @param value allowed object is
     *              {@link BulkStatusType }
     */
    public void setBulkStatus(BulkStatusType value) {
        this.bulkStatus = value;
    }

}
