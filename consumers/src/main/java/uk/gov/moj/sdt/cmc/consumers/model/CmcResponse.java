package uk.gov.moj.sdt.cmc.consumers.model;

public class CmcResponse implements ICmcResponse {
    private String status;

    @Override
    public String getStatus() {
        return this.status;
    }
}
