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
import uk.gov.moj.sdt.consumers.util.ClaimDefencesResultsUtil;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

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
    void shouldReturnClaimDefencesResponse() {
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

        ClaimDefencesResultsUtil resultsUtil = new ClaimDefencesResultsUtil();
        List<ClaimDefencesResult> results = resultsUtil.createClaimDefencesList();

        claimDefencesResponse.setResults(results);

        claimDefencesResponse.setResultCount(results.size());
        return claimDefencesResponse;
    }

}
