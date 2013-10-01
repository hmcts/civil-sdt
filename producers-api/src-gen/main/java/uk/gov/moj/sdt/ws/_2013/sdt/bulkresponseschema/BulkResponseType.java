package uk.gov.moj.sdt.ws._2013.sdt.bulkresponseschema;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.w3._2001.xmlschema.Adapter1;

import uk.gov.moj.sdt.ws._2013.sdt.baseschema.AbstractResponseType;

/**
 * <p>
 * Java class for bulkResponseType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bulkResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}abstractResponseType">
 *       &lt;sequence>
 *         &lt;element name="customerReference" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}customerReferenceType"/>
 *         &lt;element name="sdtBulkReference" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}sdtBulkReferenceType" minOccurs="0"/>
 *         &lt;element name="submittedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="sdtService" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="requestCount" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType (XmlAccessType.FIELD)
@XmlRootElement
@XmlType (name = "bulkResponseType", propOrder = {"customerReference", "sdtBulkReference", "submittedDate",
        "sdtService", "requestCount"})
public class BulkResponseType extends AbstractResponseType
{

    @XmlElement (required = true)
    protected String customerReference;
    protected String sdtBulkReference;
    @XmlElement (required = true, type = String.class)
    @XmlJavaTypeAdapter (Adapter1.class)
    @XmlSchemaType (name = "dateTime")
    protected Calendar submittedDate;
    @XmlElement (required = true)
    protected String sdtService;
    protected long requestCount;

    /**
     * Gets the value of the customerReference property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public String getCustomerReference ()
    {
        return customerReference;
    }

    /**
     * Sets the value of the customerReference property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCustomerReference (String value)
    {
        this.customerReference = value;
    }

    /**
     * Gets the value of the sdtBulkReference property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public String getSdtBulkReference ()
    {
        return sdtBulkReference;
    }

    /**
     * Sets the value of the sdtBulkReference property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSdtBulkReference (String value)
    {
        this.sdtBulkReference = value;
    }

    /**
     * Gets the value of the submittedDate property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public Calendar getSubmittedDate ()
    {
        return submittedDate;
    }

    /**
     * Sets the value of the submittedDate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSubmittedDate (Calendar value)
    {
        this.submittedDate = value;
    }

    /**
     * Gets the value of the sdtService property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public String getSdtService ()
    {
        return sdtService;
    }

    /**
     * Sets the value of the sdtService property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSdtService (String value)
    {
        this.sdtService = value;
    }

    /**
     * Gets the value of the requestCount property.
     * 
     */
    public long getRequestCount ()
    {
        return requestCount;
    }

    /**
     * Sets the value of the requestCount property.
     * 
     */
    public void setRequestCount (long value)
    {
        this.requestCount = value;
    }

}
