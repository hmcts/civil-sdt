package uk.gov.moj.sdt.utils.cmc;

public enum RequestType {

    BREATHING_SPACE("mcolBreathingSpace"),
    CLAIM("mcolClaim"),
    CLAIM_STATUS_UPDATE("mcolClaimStatusUpdate"),
    DEFENCE_CRITERIA("mcolDefenceCriteria"),
    JUDGMENT("mcolJudgment"),
    JUDGMENT_WARRANT("mcolJudgmentWarrant"),
    WARRANT("mcolWarrant");

    private String requestType;

    RequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestType() {
        return requestType;
    }

}
