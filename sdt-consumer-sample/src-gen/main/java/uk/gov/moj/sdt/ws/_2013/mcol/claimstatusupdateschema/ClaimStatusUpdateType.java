
package uk.gov.moj.sdt.ws._2013.mcol.claimstatusupdateschema;

import java.util.Calendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.w3._2001.xmlschema.Adapter2;


/**
 * <p>Java class for claimStatusUpdateType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="claimStatusUpdateType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="claimNumber" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}claimNumberType"/>
 *         &lt;element name="defendantId" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}defendantIdType" minOccurs="0"/>
 *         &lt;element name="notificationType" type="{http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema}notificationType"/>
 *         &lt;element name="paidInFullDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "claimStatusUpdateType", propOrder = {
        "claimNumber",
        "defendantId",
        "notificationType",
        "paidInFullDate"
})
public class ClaimStatusUpdateType {

    @XmlElement(required = true)
    protected String claimNumber;
    protected String defendantId;
    @XmlElement(required = true)
    protected NotificationType notificationType;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2.class)
    @XmlSchemaType(name = "date")
    protected Calendar paidInFullDate;

    /**
     * Gets the value of the claimNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getClaimNumber() {
        return claimNumber;
    }

    /**
     * Sets the value of the claimNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setClaimNumber(String value) {
        this.claimNumber = value;
    }

    /**
     * Gets the value of the defendantId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDefendantId() {
        return defendantId;
    }

    /**
     * Sets the value of the defendantId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDefendantId(String value) {
        this.defendantId = value;
    }

    /**
     * Gets the value of the notificationType property.
     *
     * @return possible object is
     * {@link NotificationType }
     */
    public NotificationType getNotificationType() {
        return notificationType;
    }

    /**
     * Sets the value of the notificationType property.
     *
     * @param value allowed object is
     *              {@link NotificationType }
     */
    public void setNotificationType(NotificationType value) {
        this.notificationType = value;
    }

    /**
     * Gets the value of the paidInFullDate property.
     *
     * @return possible object is
     * {@link String }
     */
    public Calendar getPaidInFullDate() {
        return paidInFullDate;
    }

    /**
     * Sets the value of the paidInFullDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPaidInFullDate(Calendar value) {
        this.paidInFullDate = value;
    }

}
