
package uk.gov.moj.sdt.ws._2013.sdt.submitqueryresponseschema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.mcol.queryschema.McolDefenceDetailType;


/**
 * <p>Java class for resultsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resultsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="mcolDefenceDetail" type="{http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema}mcolDefenceDetailType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultsType", propOrder = {
    "mcolDefenceDetail"
})
public class ResultsType {

    protected List<McolDefenceDetailType> mcolDefenceDetail;

    /**
     * Gets the value of the mcolDefenceDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mcolDefenceDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMcolDefenceDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link McolDefenceDetailType }
     * 
     * 
     */
    public List<McolDefenceDetailType> getMcolDefenceDetail() {
        if (mcolDefenceDetail == null) {
            mcolDefenceDetail = new ArrayList<McolDefenceDetailType>();
        }
        return this.mcolDefenceDetail;
    }

}
