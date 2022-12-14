
package uk.gov.moj.sdt.ws._2013.sdt.baseschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for individualStatusType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="individualStatusType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="error" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}errorType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="code" use="required" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}individualStatusCodeType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "individualStatusType", propOrder = {
        "error"
})
public class IndividualStatusType {

    protected ErrorType error;
    @XmlAttribute(name = "code", required = true)
    protected IndividualStatusCodeType code;

    /**
     * Gets the value of the error property.
     *
     * @return possible object is
     * {@link ErrorType }
     */
    public ErrorType getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     *
     * @param value allowed object is
     *              {@link ErrorType }
     */
    public void setError(ErrorType value) {
        this.error = value;
    }

    /**
     * Gets the value of the code property.
     *
     * @return possible object is
     * {@link IndividualStatusCodeType }
     */
    public IndividualStatusCodeType getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     *
     * @param value allowed object is
     *              {@link IndividualStatusCodeType }
     */
    public void setCode(IndividualStatusCodeType value) {
        this.code = value;
    }

}
