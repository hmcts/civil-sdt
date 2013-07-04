
package uk.gov.moj.sdt.ws._2013.mcol.judgmentschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.SotSignatureType;


/**
 * <p>Java class for mcolJudgmentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mcolJudgmentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="judgmentDetail" type="{http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema}judgmentDetailType"/>
 *         &lt;element name="sotSignature" type="{http://ws.sdt.moj.gov.uk/2013/sdt/BaseSchema}sotSignatureType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mcolJudgmentType", propOrder = {
    "judgmentDetail",
    "sotSignature"
})
public class McolJudgmentType {

    @XmlElement(required = true)
    protected JudgmentDetailType judgmentDetail;
    @XmlElement(required = true)
    protected SotSignatureType sotSignature;

    /**
     * Gets the value of the judgmentDetail property.
     * 
     * @return
     *     possible object is
     *     {@link JudgmentDetailType }
     *     
     */
    public JudgmentDetailType getJudgmentDetail() {
        return judgmentDetail;
    }

    /**
     * Sets the value of the judgmentDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link JudgmentDetailType }
     *     
     */
    public void setJudgmentDetail(JudgmentDetailType value) {
        this.judgmentDetail = value;
    }

    /**
     * Gets the value of the sotSignature property.
     * 
     * @return
     *     possible object is
     *     {@link SotSignatureType }
     *     
     */
    public SotSignatureType getSotSignature() {
        return sotSignature;
    }

    /**
     * Sets the value of the sotSignature property.
     * 
     * @param value
     *     allowed object is
     *     {@link SotSignatureType }
     *     
     */
    public void setSotSignature(SotSignatureType value) {
        this.sotSignature = value;
    }

}
