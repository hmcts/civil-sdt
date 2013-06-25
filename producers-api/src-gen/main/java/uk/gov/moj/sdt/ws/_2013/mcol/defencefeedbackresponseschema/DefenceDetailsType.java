
package uk.gov.moj.sdt.ws._2013.mcol.defencefeedbackresponseschema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for defenceDetailsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="defenceDetailsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="defenceDetail" type="{http://ws.sdt.moj.gov.uk/2013/mcol/DefenceFeedbackResponseSchema}defenceDetailType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "defenceDetailsType", propOrder = {
    "defenceDetail"
})
public class DefenceDetailsType {

    @XmlElement(required = true)
    protected List<DefenceDetailType> defenceDetail;

    /**
     * Gets the value of the defenceDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the defenceDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDefenceDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DefenceDetailType }
     * 
     * 
     */
    public List<DefenceDetailType> getDefenceDetail() {
        if (defenceDetail == null) {
            defenceDetail = new ArrayList<DefenceDetailType>();
        }
        return this.defenceDetail;
    }

}
