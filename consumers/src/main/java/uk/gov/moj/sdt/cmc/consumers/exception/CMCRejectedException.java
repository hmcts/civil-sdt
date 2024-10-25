package uk.gov.moj.sdt.cmc.consumers.exception;

import lombok.Getter;

import java.io.Serial;

public class CMCRejectedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6125751945448516793L;

    private final int errorCode;

    @Getter
    private final String errorText;

    public CMCRejectedException(int errorCode, String errorText) {
        super();

        this.errorCode = errorCode;
        this.errorText = errorText;
    }

    public String getErrorCode() {
        return String.valueOf(errorCode);
    }
}
