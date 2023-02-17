package uk.gov.moj.sdt.consumers.exception;

public class InvalidRequestTypeException extends RuntimeException {

    public InvalidRequestTypeException(String requestType) {
        super(String.format("Request Type: {} not supported", requestType));
    }
}
