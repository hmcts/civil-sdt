
package uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.w3c.dom.Element;


/**
 * <p>Java class for requestItemType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="requestItemType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;any processContents='lax'/>
 *       &lt;/choice>
 *       &lt;attribute name="requestType" use="required" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}requestTypeType" />
 *       &lt;attribute name="requestId" use="required" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}requestIdType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "requestItemType", propOrder = {
        "any"
})
public class RequestItemType {

    @XmlAnyElement(lax = true)
    protected Object any;
    @XmlAttribute(name = "requestType", required = true)
    protected String requestType;
    @XmlAttribute(name = "requestId", required = true)
    protected String requestId;

    /**
     * Gets the value of the any property.
     *
     * @return possible object is
     * {@link Object }
     * {@link Element }
     */
    public Object getAny() {
        return any;
    }

    /**
     * Sets the value of the any property.
     *
     * @param value allowed object is
     *              {@link Object }
     *              {@link Element }
     */
    public void setAny(Object value) {
        this.any = value;
    }

    /**
     * Gets the value of the requestType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Sets the value of the requestType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRequestType(String value) {
        this.requestType = value;
    }

    /**
     * Gets the value of the requestId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the value of the requestId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRequestId(String value) {
        this.requestId = value;
    }

}
