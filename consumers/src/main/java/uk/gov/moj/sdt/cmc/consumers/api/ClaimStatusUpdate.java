package uk.gov.moj.sdt.cmc.consumers.api;

public class ClaimStatusUpdate {

    private String caseManRef;

    private String respondantId;

    private String updateType;

    private String paidInFullDate;

    private boolean section38Compliancy;


    public String getCaseManRef() {
        return caseManRef;
    }

    public void setCaseManRef(String caseManRef) {
        this.caseManRef = caseManRef;
    }

    public String getRespondantId() {
        return respondantId;
    }

    public void setRespondantId(String respondantId) {
        this.respondantId = respondantId;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getPaidInFullDate() {
        return paidInFullDate;
    }

    public void setPaidInFullDate(String paidInFullDate) {
        this.paidInFullDate = paidInFullDate;
    }

    public boolean isSection38Compliancy() {
        return section38Compliancy;
    }

    public void setSection38Compliancy(boolean section38Compliancy) {
        this.section38Compliancy = section38Compliancy;
    }

    @Override
    public String toString() {
        return "ClaimStatusUpdate{" +
            "caseManRef='" + caseManRef + '\'' +
            ", respondantId='" + respondantId + '\'' +
            ", updateType='" + updateType + '\'' +
            ", paidInFullDate='" + paidInFullDate + '\'' +
            ", section38Compliancy=" + section38Compliancy +
            '}';
    }
}
