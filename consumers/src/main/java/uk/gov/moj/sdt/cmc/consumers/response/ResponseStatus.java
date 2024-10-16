package uk.gov.moj.sdt.cmc.consumers.response;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResponseStatus {

    ACCEPTED("Accepted"),
    INITIALLY_ACCEPTED("Initially Accepted"),
    REJECTED("Rejected");

    private final String status;

    ResponseStatus(String status) {
        this.status = status;
    }

    @JsonValue
    String getStatus() {
        return status;
    }

}
