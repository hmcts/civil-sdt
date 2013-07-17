
package uk.gov.moj.sdt.ws._2013.mcol.submitqueryresponseschema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mcolDefenceDetailType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mcolDefenceDetailType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="claimNumber" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}claimNumberType"/>
 *         &lt;element name="defendants">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="defendant" type="{http://ws.sdt.moj.gov.uk/2013/mcol/SubmitQueryResponseSchema}defendantType" maxOccurs="2"/>
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
@XmlType(name = "mcolDefenceDetailType", propOrder = {
    "claimNumber",
    "defendants"
})
public class McolDefenceDetailType {

    @XmlElement(required = true)
    protected String claimNumber;
    @XmlElement(required = true)
    protected McolDefenceDetailType.Defendants defendants;

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
     * Gets the value of the defendants property.
     * 
     * @return
     *     possible object is
     *     {@link McolDefenceDetailType.Defendants }
     *     
     */
    public McolDefenceDetailType.Defendants getDefendants() {
        return defendants;
    }

    /**
     * Sets the value of the defendants property.
     * 
     * @param value
     *     allowed object is
     *     {@link McolDefenceDetailType.Defendants }
     *     
     */
    public void setDefendants(McolDefenceDetailType.Defendants value) {
        this.defendants = value;
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
     *         &lt;element name="defendant" type="{http://ws.sdt.moj.gov.uk/2013/mcol/SubmitQueryResponseSchema}defendantType" maxOccurs="2"/>
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
        "defendant"
    })
    public static class Defendants {

        @XmlElement(required = true)
        protected List<DefendantType> defendant;

        /**
         * Gets the value of the defendant property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the defendant property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDefendant().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DefendantType }
         * 
         * 
         */
        public List<DefendantType> getDefendant() {
            if (defendant == null) {
                defendant = new ArrayList<DefendantType>();
            }
            return this.defendant;
        }

    }

}
