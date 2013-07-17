
package uk.gov.moj.sdt.ws._2013.sdt.submitqueryresponseschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="mcolResults" type="{http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryResponseSchema}mcolResultsType"/>
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
    "mcolResults"
})
public class ResultsType {

    protected McolResultsType mcolResults;

    /**
     * Gets the value of the mcolResults property.
     * 
     * @return
     *     possible object is
     *     {@link McolResultsType }
     *     
     */
    public McolResultsType getMcolResults() {
        return mcolResults;
    }

    /**
     * Sets the value of the mcolResults property.
     * 
     * @param value
     *     allowed object is
     *     {@link McolResultsType }
     *     
     */
    public void setMcolResults(McolResultsType value) {
        this.mcolResults = value;
    }

}
