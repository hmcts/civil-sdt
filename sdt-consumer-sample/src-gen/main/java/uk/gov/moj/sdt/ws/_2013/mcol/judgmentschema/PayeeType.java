
package uk.gov.moj.sdt.ws._2013.mcol.judgmentschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for payeeType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="payeeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="70"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="address" type="{http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema}payeeAddressType"/>
 *         &lt;element name="telephoneNumber" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="[0-9][\d-]{0,13}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="dxNumber" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="35"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="faxNumber" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="24"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="email" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="254"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="pcm" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="reference" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="24"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bankAccountNumber" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bankAccountHolder" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="70"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bankSortCode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bankName" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bankInfo1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bankInfo2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="slipCodeline1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="58"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="slipCodeline2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="58"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="giroAccountNumber" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="giroTransCode1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="9"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="giroTransCode2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="9"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="apacsTransCode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "payeeType", propOrder = {
        "name",
        "address",
        "telephoneNumber",
        "dxNumber",
        "faxNumber",
        "email",
        "pcm",
        "reference",
        "bankAccountNumber",
        "bankAccountHolder",
        "bankSortCode",
        "bankName",
        "bankInfo1",
        "bankInfo2",
        "slipCodeline1",
        "slipCodeline2",
        "giroAccountNumber",
        "giroTransCode1",
        "giroTransCode2",
        "apacsTransCode"
})
public class PayeeType {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected PayeeAddressType address;
    protected String telephoneNumber;
    protected String dxNumber;
    protected String faxNumber;
    protected String email;
    protected String pcm;
    protected String reference;
    protected String bankAccountNumber;
    protected String bankAccountHolder;
    protected String bankSortCode;
    protected String bankName;
    protected String bankInfo1;
    protected String bankInfo2;
    protected String slipCodeline1;
    protected String slipCodeline2;
    protected String giroAccountNumber;
    protected String giroTransCode1;
    protected String giroTransCode2;
    protected String apacsTransCode;

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the address property.
     *
     * @return possible object is
     * {@link PayeeAddressType }
     */
    public PayeeAddressType getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     *
     * @param value allowed object is
     *              {@link PayeeAddressType }
     */
    public void setAddress(PayeeAddressType value) {
        this.address = value;
    }

    /**
     * Gets the value of the telephoneNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * Sets the value of the telephoneNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneNumber(String value) {
        this.telephoneNumber = value;
    }

    /**
     * Gets the value of the dxNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDxNumber() {
        return dxNumber;
    }

    /**
     * Sets the value of the dxNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDxNumber(String value) {
        this.dxNumber = value;
    }

    /**
     * Gets the value of the faxNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFaxNumber() {
        return faxNumber;
    }

    /**
     * Sets the value of the faxNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFaxNumber(String value) {
        this.faxNumber = value;
    }

    /**
     * Gets the value of the email property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the pcm property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPcm() {
        return pcm;
    }

    /**
     * Sets the value of the pcm property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPcm(String value) {
        this.pcm = value;
    }

    /**
     * Gets the value of the reference property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setReference(String value) {
        this.reference = value;
    }

    /**
     * Gets the value of the bankAccountNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    /**
     * Sets the value of the bankAccountNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBankAccountNumber(String value) {
        this.bankAccountNumber = value;
    }

    /**
     * Gets the value of the bankAccountHolder property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBankAccountHolder() {
        return bankAccountHolder;
    }

    /**
     * Sets the value of the bankAccountHolder property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBankAccountHolder(String value) {
        this.bankAccountHolder = value;
    }

    /**
     * Gets the value of the bankSortCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBankSortCode() {
        return bankSortCode;
    }

    /**
     * Sets the value of the bankSortCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBankSortCode(String value) {
        this.bankSortCode = value;
    }

    /**
     * Gets the value of the bankName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * Sets the value of the bankName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBankName(String value) {
        this.bankName = value;
    }

    /**
     * Gets the value of the bankInfo1 property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBankInfo1() {
        return bankInfo1;
    }

    /**
     * Sets the value of the bankInfo1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBankInfo1(String value) {
        this.bankInfo1 = value;
    }

    /**
     * Gets the value of the bankInfo2 property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBankInfo2() {
        return bankInfo2;
    }

    /**
     * Sets the value of the bankInfo2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBankInfo2(String value) {
        this.bankInfo2 = value;
    }

    /**
     * Gets the value of the slipCodeline1 property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSlipCodeline1() {
        return slipCodeline1;
    }

    /**
     * Sets the value of the slipCodeline1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSlipCodeline1(String value) {
        this.slipCodeline1 = value;
    }

    /**
     * Gets the value of the slipCodeline2 property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSlipCodeline2() {
        return slipCodeline2;
    }

    /**
     * Sets the value of the slipCodeline2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSlipCodeline2(String value) {
        this.slipCodeline2 = value;
    }

    /**
     * Gets the value of the giroAccountNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getGiroAccountNumber() {
        return giroAccountNumber;
    }

    /**
     * Sets the value of the giroAccountNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setGiroAccountNumber(String value) {
        this.giroAccountNumber = value;
    }

    /**
     * Gets the value of the giroTransCode1 property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getGiroTransCode1() {
        return giroTransCode1;
    }

    /**
     * Sets the value of the giroTransCode1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setGiroTransCode1(String value) {
        this.giroTransCode1 = value;
    }

    /**
     * Gets the value of the giroTransCode2 property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getGiroTransCode2() {
        return giroTransCode2;
    }

    /**
     * Sets the value of the giroTransCode2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setGiroTransCode2(String value) {
        this.giroTransCode2 = value;
    }

    /**
     * Gets the value of the apacsTransCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getApacsTransCode() {
        return apacsTransCode;
    }

    /**
     * Sets the value of the apacsTransCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setApacsTransCode(String value) {
        this.apacsTransCode = value;
    }

}
