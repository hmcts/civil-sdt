package uk.gov.moj.sdt.cmc.consumers.model;

import uk.gov.moj.sdt.domain.RequestType;

public abstract class CMCRequest {
    private RequestType requestTypeName;

    public RequestType getRequestTypeName() {
        return requestTypeName;
    }

    public void setRequestTypeName(RequestType requestTypeName) {
        this.requestTypeName = requestTypeName;
    }
}
