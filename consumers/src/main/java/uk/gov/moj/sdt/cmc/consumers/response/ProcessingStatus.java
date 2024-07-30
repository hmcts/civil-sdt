package uk.gov.moj.sdt.cmc.consumers.response;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProcessingStatus {

    QUEUED("queued"),
    PROCESSED("processed");

    private final String status;

    ProcessingStatus(String status) {
        this.status = status;
    }

    @JsonValue
    String getStatus() {
        return status;
    }
}
