
package uk.gov.moj.sdt.ws._2013.sdt.baseschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateStatusType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="updateStatusType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="error" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}errorType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="code" use="required" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}updateStatusCodeType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateStatusType", propOrder = {
        "error"
})
public class UpdateStatusType {

    protected ErrorType error;
    @XmlAttribute(name = "code", required = true)
    protected UpdateStatusCodeType code;

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
     * {@link UpdateStatusCodeType }
     */
    public UpdateStatusCodeType getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     *
     * @param value allowed object is
     *              {@link UpdateStatusCodeType }
     */
    public void setCode(UpdateStatusCodeType value) {
        this.code = value;
    }

}
