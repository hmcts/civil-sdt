
package uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import uk.gov.moj.sdt.ws._2013.mcol.claimschema.ClaimType;
import uk.gov.moj.sdt.ws._2013.mcol.claimstatusupdateschema.ClaimStatusUpdateType;
import uk.gov.moj.sdt.ws._2013.mcol.judgmentschema.JudgmentWarrantType;
import uk.gov.moj.sdt.ws._2013.mcol.judgmentschema.McolJudgmentType;
import uk.gov.moj.sdt.ws._2013.mcol.warrantschema.WarrantType;


/**
 * <p>Java class for mcolRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mcolRequestType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.sdt.moj.gov.uk/2013/sdt/BulkRequestSchema}request">
 *       &lt;choice>
 *         &lt;element name="mcolClaim" type="{http://ws.sdt.moj.gov.uk/2013/mcol/ClaimSchema}claimType"/>
 *         &lt;element name="mcolJudgment" type="{http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema}mcolJudgmentType"/>
 *         &lt;element name="mcolClaimStatusUpdate" type="{http://ws.sdt.moj.gov.uk/2013/mcol/ClaimStatusUpdateSchema}claimStatusUpdateType"/>
 *         &lt;element name="mcolWarrant" type="{http://ws.sdt.moj.gov.uk/2013/mcol/WarrantSchema}warrantType"/>
 *         &lt;element name="mcolJudgmentWarrant" type="{http://ws.sdt.moj.gov.uk/2013/mcol/JudgmentSchema}judgmentWarrantType"/>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mcolRequestType", propOrder = {
    "mcolClaim",
    "mcolJudgment",
    "mcolClaimStatusUpdate",
    "mcolWarrant",
    "mcolJudgmentWarrant"
})
public class McolRequestType
    extends Request
{

    protected ClaimType mcolClaim;
    protected McolJudgmentType mcolJudgment;
    protected ClaimStatusUpdateType mcolClaimStatusUpdate;
    protected WarrantType mcolWarrant;
    protected JudgmentWarrantType mcolJudgmentWarrant;

    /**
     * Gets the value of the mcolClaim property.
     * 
     * @return
     *     possible object is
     *     {@link ClaimType }
     *     
     */
    public ClaimType getMcolClaim() {
        return mcolClaim;
    }

    /**
     * Sets the value of the mcolClaim property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClaimType }
     *     
     */
    public void setMcolClaim(ClaimType value) {
        this.mcolClaim = value;
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

    /**
     * Gets the value of the mcolClaimStatusUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link ClaimStatusUpdateType }
     *     
     */
    public ClaimStatusUpdateType getMcolClaimStatusUpdate() {
        return mcolClaimStatusUpdate;
    }

    /**
     * Sets the value of the mcolClaimStatusUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClaimStatusUpdateType }
     *     
     */
    public void setMcolClaimStatusUpdate(ClaimStatusUpdateType value) {
        this.mcolClaimStatusUpdate = value;
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

    /**
     * Gets the value of the mcolJudgmentWarrant property.
     * 
     * @return
     *     possible object is
     *     {@link JudgmentWarrantType }
     *     
     */
    public JudgmentWarrantType getMcolJudgmentWarrant() {
        return mcolJudgmentWarrant;
    }

    /**
     * Sets the value of the mcolJudgmentWarrant property.
     * 
     * @param value
     *     allowed object is
     *     {@link JudgmentWarrantType }
     *     
     */
    public void setMcolJudgmentWarrant(JudgmentWarrantType value) {
        this.mcolJudgmentWarrant = value;
    }

}
