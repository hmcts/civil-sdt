package uk.gov.moj.sdt.cmc.consumers.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CmcRequest implements  ICmcRequest {
    private String idAmId;
    private String fromDateTime;
    private String toDateTime;

    @Override
    public String getIdAmId() {
        return this.idAmId;
    }

    @Override
    public String getFromDateTime() {
        return this.fromDateTime;
    }

    @Override
    public String getToDateTime() {
        return this.toDateTime;
    }
}
