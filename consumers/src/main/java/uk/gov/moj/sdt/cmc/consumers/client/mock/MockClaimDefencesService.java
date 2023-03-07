package uk.gov.moj.sdt.cmc.consumers.client.mock;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
                LocalDate.of(2020,10,20), LocalDateTime.of(2020,10,21,10,20,30), "type1",
                "defence1");
        ClaimDefencesResult result2 = createClaimDefencesResult("case2", "resp2",
                LocalDate.of(2020,10,27), LocalDateTime.of(2020,10,28,11,21,31), "type2",
                "defence2");
        ClaimDefencesResult result3 = createClaimDefencesResult("case3", "resp3",
                LocalDate.of(2020,10,29), LocalDateTime.of(2020,10,30,12,13,14), "type3",
                "defence2");
        ClaimDefencesResult result4 = createClaimDefencesResult("case4", "resp4",
                LocalDate.of(2020,11,2), LocalDateTime.of(2020,11,02,10,15,20), "type4",
                "defence4");
        ClaimDefencesResult result5 = createClaimDefencesResult("case5", "resp5",
                LocalDate.of(2020,11,9), LocalDateTime.of(2020,11,10,11,20,11), "type5",
                "defence5");
        ClaimDefencesResult result6 = createClaimDefencesResult("case6", "resp6",
                LocalDate.of(2020,11,9), LocalDateTime.of(2020,11,10,11,20,11), "type5",
                "I am innocent");
        ClaimDefencesResult result7 = createClaimDefencesResult("case6", "resp7",
                LocalDate.of(2020,11,9), LocalDateTime.of(2020,11,10,11,50,11), "type5",
                "Not guilty");
        ClaimDefencesResult result8 = createClaimDefencesResult("case6", "resp8",
                LocalDate.of(2020,11,9), LocalDateTime.of(2020,11,10,11,59,11), "type5",
                "Taking the 5th");
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
                                                          LocalDate defendantResponseFiledDate,
                                                          LocalDateTime defendantResponseCreatedDate, String responseType,
                                                          String defence) {
        return new ClaimDefencesResult(caseManRef, respondentId, defendantResponseFiledDate,
                defendantResponseCreatedDate, responseType ,defence);
    }

}
