package uk.gov.moj.sdt.domain;

public enum RequestType {

    BREATHINGSPACE("mcolBreathingSpace"),
    CLAIM("mcolClaim"),
    CLAIM_STATUS_UPDATE("mcolClaimStatusUpdate"),
    DEFENCECRITERIA("mcolDefenceCriteria"),
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
