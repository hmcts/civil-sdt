package uk.gov.moj.sdt.cmc.consumers.client.mock;

import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimDefences;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResult;

@Service("MockClaimDefencesService")
public class MockClaimDefencesService implements IClaimDefences {

    @Override
    public ClaimDefencesResponse claimDefences(String fromDate,
                                               String toDate) {
        return createClaimDefencesResponse();
    }

    private ClaimDefencesResponse createClaimDefencesResponse() {
        ClaimDefencesResponse response = new ClaimDefencesResponse();
        ClaimDefencesResult result1 = createClaimDefencesResult("case1", "resp1",
                "2020-10-20", "2020-10-21T10:20:30", "type1",
                "defence1");
        ClaimDefencesResult result2 = createClaimDefencesResult("case2", "resp2",
                "2020-10-27", "2020-10-28T11:21:31", "type2",
                "defence2");
        ClaimDefencesResult result3 = createClaimDefencesResult("case3", "resp3",
                "2020-10-29", "2020-10-30T12:13:14", "type3",
                "defence2");
        ClaimDefencesResult result4 = createClaimDefencesResult("case4", "resp4",
                "2020-11-02", "2020-11-02T10:15:20", "type4",
                "defence4");
        ClaimDefencesResult result5 = createClaimDefencesResult("case5", "resp5",
                "2020-11-09", "2020-11-10T11:20:11", "type5",
                "defence5");
        ClaimDefencesResult result6 = createClaimDefencesResult("case6", "resp6",
                "2020-11-22", "2020-11-23T11:34:45", "type6",
                "defence6");
        ClaimDefencesResult[] results = new ClaimDefencesResult[6];
        results[0] = result1;
        results[1] = result2;
        results[2] = result3;
        results[3] = result4;
        results[4] = result5;
        results[5] = result6;

        response.setResults(results);
        response.setResultCount(results.length);
        return response;
    }
    private ClaimDefencesResult createClaimDefencesResult(String caseManRef, String respondentId,
                                                          String defendantResponseFiledDate,
                                                          String defendantResponseCreatedDate, String responseType,
                                                          String defence) {
        return new ClaimDefencesResult(caseManRef, respondentId, defendantResponseFiledDate,
                defendantResponseCreatedDate, responseType ,defence);
    }

}
