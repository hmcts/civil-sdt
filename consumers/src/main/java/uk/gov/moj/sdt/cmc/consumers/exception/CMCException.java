package uk.gov.moj.sdt.cmc.consumers.exception;

public class CMCException extends RuntimeException {

    public CMCException(String message, Throwable cause) {
        super(message, cause);
    }
}
