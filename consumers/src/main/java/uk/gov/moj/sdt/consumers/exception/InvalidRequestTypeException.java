package uk.gov.moj.sdt.consumers.exception;

public class InvalidRequestTypeException extends RuntimeException {

    @java.io.Serial
    private static final long serialVersionUID = -6226070744594893488L;

    public InvalidRequestTypeException(String requestType) {
        super(String.format("Request Type: {} not supported", requestType));
    }
}
