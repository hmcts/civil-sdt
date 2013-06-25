
package uk.gov.moj.sdt.ws._2013.mcol.requestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.mcol.judgmentschema.McolJudgmentType;


/**
 * <p>Java class for createJudgmentRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createJudgmentRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://ws.sdt.moj.gov.uk/2013/mcol/RequestSchema}headerType"/>
 *         &lt;element name="mcolJudgment" type="{http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema}mcolJudgmentType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createJudgmentRequestType", propOrder = {
    "header",
    "mcolJudgment"
})
public class CreateJudgmentRequestType {

    @XmlElement(required = true)
    protected HeaderType header;
    @XmlElement(required = true)
    protected McolJudgmentType mcolJudgment;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link HeaderType }
     *     
     */
    public HeaderType getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderType }
     *     
     */
    public void setHeader(HeaderType value) {
        this.header = value;
    }

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

}
