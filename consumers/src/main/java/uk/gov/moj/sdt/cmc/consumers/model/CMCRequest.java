package uk.gov.moj.sdt.cmc.consumers.model;

import uk.gov.moj.sdt.domain.RequestType;

public abstract class CMCRequest {
    private RequestType requestType;

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }
}
