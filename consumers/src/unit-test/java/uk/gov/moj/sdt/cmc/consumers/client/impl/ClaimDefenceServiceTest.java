package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.CMCApi;
import uk.gov.moj.sdt.cmc.consumers.model.SubmitQueryResponse;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResult;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClaimDefenceServiceTest extends AbstractSdtUnitTestBase {

    @Mock
    private CMCApi cmcApi;

    private ClaimDefencesService claimDefencesService;

    @BeforeEach
    @Override
    public void setUp() {
        claimDefencesService = new ClaimDefencesService(cmcApi);
    }

    @Test
    void getClient() {
        final ClaimDefencesResponse CLAIM_DEFENCES_RESPONSE = createClaimDefencesResponse();
        final SubmitQueryResponse SUBMIT_QUERY_RESPONSE = createSubmitQueryResponse(CLAIM_DEFENCES_RESPONSE);
        when(cmcApi.claimDefences(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(CLAIM_DEFENCES_RESPONSE);
        String fromDateTime = "2020-01-22T11:12:13";
        String toDateTime = "2020-12-01T13:14:15";
        SubmitQueryResponse returnValue = claimDefencesService.claimDefences("", "", "",
                fromDateTime, toDateTime);
        assertNotNull(returnValue);
        assertEquals(SUBMIT_QUERY_RESPONSE.getClaimDefencesResults(), returnValue.getClaimDefencesResults());
        assertEquals(SUBMIT_QUERY_RESPONSE.getClaimDefencesResultsCount(), returnValue.getClaimDefencesResultsCount());
    }

    private SubmitQueryResponse createSubmitQueryResponse(ClaimDefencesResponse claimDefencesResponse) {
        SubmitQueryResponse submitQueryResponse = new SubmitQueryResponse();
        submitQueryResponse.setClaimDefencesResultsCount(claimDefencesResponse.getResultCount());
        submitQueryResponse.setClaimDefencesResults(claimDefencesResponse.getResults());
        return submitQueryResponse;
    }

    private ClaimDefencesResponse createClaimDefencesResponse() {
        ClaimDefencesResponse claimDefencesResponse = new ClaimDefencesResponse();

        List<ClaimDefencesResult> results = new ArrayList<>();
        results.add(createClaimDefencesResult("case1", "resp1",
                LocalDate.of(2020,11,12),
                LocalDateTime.of(2020,11,13,11,20,11),
                "type1", "defence1"));
        results.add(createClaimDefencesResult("case2", "resp2",
                LocalDate.of(2020,11,13),
                LocalDateTime.of(2020,11,14,11,20,11),
                 "type2", "defence2"));
        results.add(createClaimDefencesResult("case3", "resp3",
                LocalDate.of(2020,11,14),
                LocalDateTime.of(2020,11,13,11,20,11),
                "type3", "defence3"));
        results.add(createClaimDefencesResult("case4", "resp4",
                LocalDate.of(2020,11,15),
                LocalDateTime.of(2020,11,16,11,20,11),
                "type", "defence4"));

        claimDefencesResponse.setResults(results);

        claimDefencesResponse.setResultCount(results.size());
        return claimDefencesResponse;
    }

    private ClaimDefencesResult createClaimDefencesResult(String caseManRef, String respondentId,
                                                          LocalDate defendantResponseFiledDate, LocalDateTime defendantResponseCreatedDate,
                                                          String responseType, String defence) {
        return new ClaimDefencesResult(caseManRef, respondentId,
                defendantResponseFiledDate, defendantResponseCreatedDate, responseType, defence);
    }

}
