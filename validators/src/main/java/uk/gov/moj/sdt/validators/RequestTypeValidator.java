package uk.gov.moj.sdt.validators;

import uk.gov.moj.sdt.domain.api.IIndividualRequest;

import static uk.gov.moj.sdt.domain.RequestType.BREATHING_SPACE;
import static uk.gov.moj.sdt.domain.RequestType.CLAIM_STATUS_UPDATE;
import static uk.gov.moj.sdt.domain.RequestType.JUDGMENT;
import static uk.gov.moj.sdt.domain.RequestType.JUDGMENT_WARRANT;
import static uk.gov.moj.sdt.domain.RequestType.WARRANT;

public final class RequestTypeValidator {

    public static boolean isValidRequestType(String requestType) {
        return requestType != null && (JUDGMENT.getRequestType().equalsIgnoreCase(requestType)
            || WARRANT.getRequestType().equalsIgnoreCase(requestType)
            || CLAIM_STATUS_UPDATE.getRequestType().equalsIgnoreCase(requestType)
            || JUDGMENT_WARRANT.getRequestType().equalsIgnoreCase(requestType))
            || BREATHING_SPACE.getRequestType().equalsIgnoreCase(requestType);
    }
}
