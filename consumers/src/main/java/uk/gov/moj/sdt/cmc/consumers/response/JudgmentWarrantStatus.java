package uk.gov.moj.sdt.cmc.consumers.response;

import com.fasterxml.jackson.annotation.JsonValue;

public enum JudgmentWarrantStatus {

    JUDGMENT_REQUEST_ERROR("Judgment Request error."),
    WARRANT_REQUEST_ERROR("Warrant Request error."),
    JUDGMENT_ACCEPTED_BY_CCBC("Judgment accepted by CCBC."),
    JUDGMENT_ACCEPTED_WARRANT_ACCEPTED_BY_CCBC("Judgment accepted by CCBC.  Warrant accepted by CCBC."),
    JUDGMENT_REJECTED_WARRANT_ACCEPTED_BY_CCBC("Judgment rejected by CCBC.  Warrant accepted by CCBC."),
    JUDGMENT_REJECTED_BY_CCBC("Judgment rejected by CCBC."),
    JUDGMENT_ACCEPTED_WARRANT_REJECTED_BY_CCBC("Judgment accepted by CCBC. Warrant rejected by CCBC."),
    JUDGMENT_REJECTED_WARRANT_REJECTED_BY_CCBC("Judgment rejected by CCBC.  Warrant rejected by CCBC."),
    WARRANT_ACCEPTED_BY_CCBC("Warrant accepted by CCBC."),
    WARRANT_REJECTED_BY_CCBC("Warrant rejected by CCBC.");

    private final String message;

    JudgmentWarrantStatus(String message) {
        this.message = message;
    }

    @JsonValue
    public String getMessage() {
        return message;
    }
}
