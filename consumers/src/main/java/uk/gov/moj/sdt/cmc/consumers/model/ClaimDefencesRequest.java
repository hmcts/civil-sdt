package uk.gov.moj.sdt.cmc.consumers.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ClaimDefencesRequest implements ICmcRequest {

    String idAmId;
    String fromDateTime;
    String toDateTime;

}
