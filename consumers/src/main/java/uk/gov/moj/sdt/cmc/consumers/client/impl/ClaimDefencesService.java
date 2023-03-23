package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.CMCApi;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimDefencesService;
import uk.gov.moj.sdt.response.SubmitQueryResponse;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;

@Service("ClaimDefencesService")
public class ClaimDefencesService implements IClaimDefencesService {

    private final CMCApi cmcApi;

    @Autowired
    public ClaimDefencesService(CMCApi cmcApi) {
        this.cmcApi = cmcApi;
    }

    @Override
    public SubmitQueryResponse claimDefences(String authorization, String serviceAuthorization, String idamId, String fromDate,
                                               String toDate) {

        SubmitQueryResponse submitQueryResponse = new SubmitQueryResponse();
        ClaimDefencesResponse claimDefencesResponse =  cmcApi.claimDefences(authorization, serviceAuthorization, idamId,
                                    fromDate, toDate);
        submitQueryResponse.setClaimDefencesResultsCount(claimDefencesResponse.getResultCount());
        submitQueryResponse.setClaimDefencesResults(claimDefencesResponse.getResults());
        return submitQueryResponse;
    }
}
