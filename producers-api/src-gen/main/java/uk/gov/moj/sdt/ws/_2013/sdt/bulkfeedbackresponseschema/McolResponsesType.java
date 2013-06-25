
package uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mcolResponsesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mcolResponsesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mcolResponse" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema}mcolResponseType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mcolResponsesType", propOrder = {
    "mcolResponse"
})
public class McolResponsesType {

    @XmlElement(required = true)
    protected List<McolResponseType> mcolResponse;

    /**
     * Gets the value of the mcolResponse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mcolResponse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMcolResponse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link McolResponseType }
     * 
     * 
     */
    public List<McolResponseType> getMcolResponse() {
        if (mcolResponse == null) {
            mcolResponse = new ArrayList<McolResponseType>();
        }
        return this.mcolResponse;
    }

}
