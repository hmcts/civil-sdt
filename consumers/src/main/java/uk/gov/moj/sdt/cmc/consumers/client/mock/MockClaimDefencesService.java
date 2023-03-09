package uk.gov.moj.sdt.cmc.consumers.client.mock;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimDefencesService;
import uk.gov.moj.sdt.cmc.consumers.model.ResponseType;
import uk.gov.moj.sdt.cmc.consumers.model.SubmitQueryResponse;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResult;

@Service("MockClaimDefencesService")
public class MockClaimDefencesService implements IClaimDefencesService {

    @Override
    public SubmitQueryResponse claimDefences(String fromDate,
                                             String toDate) {
        return createClaimDefencesResponse();
    }

    private SubmitQueryResponse createClaimDefencesResponse() {
        ClaimDefencesResponse response = new ClaimDefencesResponse();
        ClaimDefencesResult result1 = createClaimDefencesResult("case1", "resp1",
                LocalDate.of(2020,10,20), LocalDateTime.of(2020,10,21,10,20,30), ResponseType.AS.value(),
                "defence1");
        ClaimDefencesResult result2 = createClaimDefencesResult("case2", "resp2",
                LocalDate.of(2020,10,27), LocalDateTime.of(2020,10,28,11,21,31), ResponseType.PA.value(),
                "defence2");
        ClaimDefencesResult result3 = createClaimDefencesResult("case3", "resp3",
                LocalDate.of(2020,10,29), LocalDateTime.of(2020,10,30,12,13,14), ResponseType.DE.value(),
                "defence2");
        ClaimDefencesResult result4 = createClaimDefencesResult("case4", "resp4",
                LocalDate.of(2020,11,2), LocalDateTime.of(2020,11,02,10,15,20), ResponseType.AS.value(),
                "defence4");
        ClaimDefencesResult result5 = createClaimDefencesResult("case5", "resp5",
                LocalDate.of(2020,11,9), LocalDateTime.of(2020,11,10,11,20,11), ResponseType.DE.value(),
                "defence5");
        ClaimDefencesResult result6 = createClaimDefencesResult("case6", "resp6",
                LocalDate.of(2020,11,9), LocalDateTime.of(2020,11,10,11,20,11), ResponseType.DC.value(),
                "I am innocent");
        ClaimDefencesResult result7 = createClaimDefencesResult("case6", "resp7",
                LocalDate.of(2020,11,9), LocalDateTime.of(2020,11,10,11,50,11), ResponseType.AS.value(),
                "Not guilty");
        ClaimDefencesResult result8 = createClaimDefencesResult("case6", "resp8",
                LocalDate.of(2020,11,9), LocalDateTime.of(2020,11,10,11,59,11), ResponseType.PA.value(),
                "Taking the 5th");
        ClaimDefencesResult[] results = new ClaimDefencesResult[8];
        results[0] = result1;
        results[1] = result2;
        results[2] = result3;
        results[3] = result4;
        results[4] = result5;
        results[5] = result6;
        results[6] = result7;
        results[7] = result8;

        response.setResults(results);
        response.setResultCount(results.length);

        SubmitQueryResponse submitQueryResponse = new SubmitQueryResponse();
        submitQueryResponse.setClaimDefencesResults(results);
        submitQueryResponse.setClaimDefencesResultsCount(results.length);

        return submitQueryResponse;
    }
    private ClaimDefencesResult createClaimDefencesResult(String caseManRef, String respondentId,
                                                          LocalDate defendantResponseFiledDate,
                                                          LocalDateTime defendantResponseCreatedDate, String responseType,
                                                          String defence) {
        return new ClaimDefencesResult(caseManRef, respondentId, defendantResponseFiledDate,
                defendantResponseCreatedDate, responseType ,defence);
    }

}
