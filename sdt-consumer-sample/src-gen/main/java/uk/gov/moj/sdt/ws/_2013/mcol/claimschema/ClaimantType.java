
package uk.gov.moj.sdt.ws._2013.mcol.claimschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 Describes a claimant
 *             
 * 
 * <p>Java class for claimantType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="claimantType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="60"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="address">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="line1" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}lineType"/>
 *                   &lt;element name="line2" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}lineType"/>
 *                   &lt;element name="line3" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}lineType" minOccurs="0"/>
 *                   &lt;element name="line4" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}lineType" minOccurs="0"/>
 *                   &lt;element name="postcode" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}postcodeType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
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
@XmlType(name = "claimantType", propOrder = {
    "name",
    "address"
})
public class ClaimantType {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected ClaimantType.Address address;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link ClaimantType.Address }
     *     
     */
    public ClaimantType.Address getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClaimantType.Address }
     *     
     */
    public void setAddress(ClaimantType.Address value) {
        this.address = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="line1" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}lineType"/>
     *         &lt;element name="line2" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}lineType"/>
     *         &lt;element name="line3" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}lineType" minOccurs="0"/>
     *         &lt;element name="line4" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}lineType" minOccurs="0"/>
     *         &lt;element name="postcode" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}postcodeType" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "line1",
        "line2",
        "line3",
        "line4",
        "postcode"
    })
    public static class Address {

        @XmlElement(required = true)
        protected String line1;
        @XmlElement(required = true)
        protected String line2;
        protected String line3;
        protected String line4;
        protected String postcode;

        /**
         * Gets the value of the line1 property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLine1() {
            return line1;
        }

        /**
         * Sets the value of the line1 property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLine1(String value) {
            this.line1 = value;
        }

        /**
         * Gets the value of the line2 property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLine2() {
            return line2;
        }

        /**
         * Sets the value of the line2 property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLine2(String value) {
            this.line2 = value;
        }

        /**
         * Gets the value of the line3 property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLine3() {
            return line3;
        }

        /**
         * Sets the value of the line3 property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLine3(String value) {
            this.line3 = value;
        }

        /**
         * Gets the value of the line4 property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLine4() {
            return line4;
        }

        /**
         * Sets the value of the line4 property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLine4(String value) {
            this.line4 = value;
        }

        /**
         * Gets the value of the postcode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPostcode() {
            return postcode;
        }

        /**
         * Sets the value of the postcode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPostcode(String value) {
            this.postcode = value;
        }

    }

}
