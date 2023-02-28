package uk.gov.moj.sdt.cmc.consumers.exception;

public class CMCException extends RuntimeException {

    @java.io.Serial
    private static final long serialVersionUID = 5575187133464364204L;

    public CMCException(String message, Throwable cause) {
        super(message, cause);
    }
}
