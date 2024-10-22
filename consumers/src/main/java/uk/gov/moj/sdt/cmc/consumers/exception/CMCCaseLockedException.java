package uk.gov.moj.sdt.cmc.consumers.exception;

import java.io.Serial;

public class CMCCaseLockedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4125036333653935089L;

    public CMCCaseLockedException() {
        super();
    }
}
