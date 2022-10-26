
package uk.gov.moj.sdt.ws._2013.sdt.targetapp.submitqueryresponseschema;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import org.w3c.dom.Element;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusType;


/**
 * <p>Java class for submitQueryResponseType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="submitQueryResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="targetAppCustomerId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="resultCount" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;element name="targetAppDetail">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;any processContents='lax' maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="status" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}statusType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "submitQueryResponseType", propOrder = {
        "targetAppCustomerId",
        "resultCount",
        "targetAppDetail",
        "status"
})
public class SubmitQueryResponseType {

    @XmlElement(required = true)
    protected String targetAppCustomerId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger resultCount;
    @XmlElement(required = true)
    protected SubmitQueryResponseType.TargetAppDetail targetAppDetail;
    @XmlElement(required = true)
    protected StatusType status;

    /**
     * Gets the value of the targetAppCustomerId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTargetAppCustomerId() {
        return targetAppCustomerId;
    }

    /**
     * Sets the value of the targetAppCustomerId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTargetAppCustomerId(String value) {
        this.targetAppCustomerId = value;
    }

    /**
     * Gets the value of the resultCount property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getResultCount() {
        return resultCount;
    }

    /**
     * Sets the value of the resultCount property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setResultCount(BigInteger value) {
        this.resultCount = value;
    }

    /**
     * Gets the value of the targetAppDetail property.
     *
     * @return possible object is
     * {@link SubmitQueryResponseType.TargetAppDetail }
     */
    public SubmitQueryResponseType.TargetAppDetail getTargetAppDetail() {
        return targetAppDetail;
    }

    /**
     * Sets the value of the targetAppDetail property.
     *
     * @param value allowed object is
     *              {@link SubmitQueryResponseType.TargetAppDetail }
     */
    public void setTargetAppDetail(SubmitQueryResponseType.TargetAppDetail value) {
        this.targetAppDetail = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return possible object is
     * {@link StatusType }
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value allowed object is
     *              {@link StatusType }
     */
    public void setStatus(StatusType value) {
        this.status = value;
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
     *       &lt;choice>
     *         &lt;any processContents='lax' maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "any"
    })
    public static class TargetAppDetail {

        @XmlAnyElement(lax = true)
        protected List<Object> any;

        /**
         * Gets the value of the any property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the any property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAny().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Object }
         * {@link Element }
         */
        public List<Object> getAny() {
            if (any == null) {
                any = new ArrayList<Object>();
            }
            return this.any;
        }

    }

}
