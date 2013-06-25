
package uk.gov.moj.sdt.ws._2013.mcol.claimschema;

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
 * <p>Java class for interestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="interestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="reserveRightToClaimInterest" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="dailyAmount" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
 *         &lt;element name="owedDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="claimDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="claimAmountInterestBase" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "interestType", propOrder = {
    "reserveRightToClaimInterest",
    "dailyAmount",
    "owedDate",
    "claimDate",
    "claimAmountInterestBase"
})
public class InterestType {

    protected boolean reserveRightToClaimInterest;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter3 .class)
    @XmlSchemaType(name = "unsignedLong")
    protected Long dailyAmount;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar owedDate;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar claimDate;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter3 .class)
    @XmlSchemaType(name = "unsignedLong")
    protected Long claimAmountInterestBase;

    /**
     * Gets the value of the reserveRightToClaimInterest property.
     * 
     */
    public boolean isReserveRightToClaimInterest() {
        return reserveRightToClaimInterest;
    }

    /**
     * Sets the value of the reserveRightToClaimInterest property.
     * 
     */
    public void setReserveRightToClaimInterest(boolean value) {
        this.reserveRightToClaimInterest = value;
    }

    /**
     * Gets the value of the dailyAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Long getDailyAmount() {
        return dailyAmount;
    }

    /**
     * Sets the value of the dailyAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDailyAmount(Long value) {
        this.dailyAmount = value;
    }

    /**
     * Gets the value of the owedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getOwedDate() {
        return owedDate;
    }

    /**
     * Sets the value of the owedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwedDate(Calendar value) {
        this.owedDate = value;
    }

    /**
     * Gets the value of the claimDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getClaimDate() {
        return claimDate;
    }

    /**
     * Sets the value of the claimDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaimDate(Calendar value) {
        this.claimDate = value;
    }

    /**
     * Gets the value of the claimAmountInterestBase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Long getClaimAmountInterestBase() {
        return claimAmountInterestBase;
    }

    /**
     * Sets the value of the claimAmountInterestBase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaimAmountInterestBase(Long value) {
        this.claimAmountInterestBase = value;
    }

}
