
package uk.gov.moj.sdt.ws._2013.mcol.judgmentschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.mcol.warrantschema.WarrantType;


/**
 * <p>Java class for judgmentWarrantType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="judgmentWarrantType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mcolJudgment" type="{http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema}mcolJudgmentType"/>
 *         &lt;element name="mcolWarrant" type="{http://ws.sdt.moj.gov.uk/2013/mcol/WarrantSchema}warrantType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "judgmentWarrantType", propOrder = {
    "mcolJudgment",
    "mcolWarrant"
})
public class JudgmentWarrantType {

    @XmlElement(required = true)
    protected McolJudgmentType mcolJudgment;
    @XmlElement(required = true)
    protected WarrantType mcolWarrant;

    /**
     * Gets the value of the mcolJudgment property.
     * 
     * @return
     *     possible object is
     *     {@link McolJudgmentType }
     *     
     */
    public McolJudgmentType getMcolJudgment() {
        return mcolJudgment;
    }

    /**
     * Sets the value of the mcolJudgment property.
     * 
     * @param value
     *     allowed object is
     *     {@link McolJudgmentType }
     *     
     */
    public void setMcolJudgment(McolJudgmentType value) {
        this.mcolJudgment = value;
    }

    /**
     * Gets the value of the mcolWarrant property.
     * 
     * @return
     *     possible object is
     *     {@link WarrantType }
     *     
     */
    public WarrantType getMcolWarrant() {
        return mcolWarrant;
    }

    /**
     * Sets the value of the mcolWarrant property.
     * 
     * @param value
     *     allowed object is
     *     {@link WarrantType }
     *     
     */
    public void setMcolWarrant(WarrantType value) {
        this.mcolWarrant = value;
    }

}
