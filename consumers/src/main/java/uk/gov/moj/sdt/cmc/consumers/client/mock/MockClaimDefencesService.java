package uk.gov.moj.sdt.cmc.consumers.client.mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimDefencesService;
import uk.gov.moj.sdt.cmc.consumers.model.ResponseType;
import uk.gov.moj.sdt.cmc.consumers.model.SubmitQueryResponse;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResult;

@Service("MockClaimDefencesService")
public class MockClaimDefencesService implements IClaimDefencesService {

    @Override
    public SubmitQueryResponse claimDefences(String authorization, String serviceAuthorization, String idamId,
                                             String fromDate, String toDate) {
        return createClaimDefencesResponse();
    }

    private SubmitQueryResponse createClaimDefencesResponse() {
        List<ClaimDefencesResult> results = new ArrayList<>();
        results.add(new ClaimDefencesResult("case1", "resp1",
                LocalDate.of(2020,10,20), LocalDateTime.of(2020,10,21,10,20,30), ResponseType.AS.value(),
                "defence1"));
        results.add(new ClaimDefencesResult("case2", "resp2",
                LocalDate.of(2020,10,27), LocalDateTime.of(2020,10,28,11,21,31), ResponseType.PA.value(),
                "defence2"));
        results.add(new ClaimDefencesResult("case3", "resp3",
                LocalDate.of(2020,10,29), LocalDateTime.of(2020,10,30,12,13,14), ResponseType.DE.value(),
                "defence2"));
        results.add(new ClaimDefencesResult("case4", "resp4",
                LocalDate.of(2020,11,2), LocalDateTime.of(2020,11,02,10,15,20), ResponseType.AS.value(),
                "defence4"));
        results.add(new ClaimDefencesResult("case5", "resp5",
                LocalDate.of(2020,11,9), LocalDateTime.of(2020,11,10,11,20,11), ResponseType.DE.value(),
                "defence5"));
        results.add(new ClaimDefencesResult("case6", "resp6",
                LocalDate.of(2020,11,9), LocalDateTime.of(2020,11,10,11,20,11), ResponseType.DC.value(),
                "I am innocent"));
        results.add(new ClaimDefencesResult("case6", "resp7",
                LocalDate.of(2020,11,9), LocalDateTime.of(2020,11,10,11,50,11), ResponseType.AS.value(),
                "Not guilty"));
        results.add(new ClaimDefencesResult("case6", "resp8",
                LocalDate.of(2020,11,9), LocalDateTime.of(2020,11,10,11,59,11), ResponseType.PA.value(),
                "Taking the 5th"));

        SubmitQueryResponse submitQueryResponse = new SubmitQueryResponse();
        submitQueryResponse.setClaimDefencesResults(results);
        submitQueryResponse.setClaimDefencesResultsCount(results.size());

        return submitQueryResponse;
    }

}
