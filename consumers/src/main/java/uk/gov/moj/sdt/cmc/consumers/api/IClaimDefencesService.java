package uk.gov.moj.sdt.cmc.consumers.api;

import uk.gov.moj.sdt.cmc.consumers.model.SubmitQueryResponse;

public interface IClaimDefencesService {

    SubmitQueryResponse claimDefences(String fromDate, String toDate);
}
