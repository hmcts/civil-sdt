package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResult;
import uk.gov.moj.sdt.consumers.util.ClaimDefencesResultsUtil;

import java.util.List;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClaimDefenceServiceTest extends CMCConsumersClientTestBase {

    private ClaimDefencesService claimDefencesService;

    @Override
    protected void setUpLocalTests() {
        claimDefencesService = new ClaimDefencesService(mockCmcApi);
    }

    @Test
    void shouldReturnClaimDefencesResponse() {
        String fromDateTime = "2020-01-22T11:12:13";
        String toDateTime = "2020-12-01T13:14:15";
        final ClaimDefencesResponse claimDefencesResponse = createClaimDefencesResponse();
        when(mockCmcApi.claimDefences(AUTHORIZATION, SERVICE_AUTHORISATION, IDAM_ID_HEADER, fromDateTime, toDateTime))
            .thenReturn(claimDefencesResponse);

        ClaimDefencesResponse returnValue = claimDefencesService.claimDefences(AUTHORIZATION,
                                                                               SERVICE_AUTHORISATION,
                                                                               IDAM_ID_HEADER,
                                                                               fromDateTime,
                                                                               toDateTime);

        assertNotNull(returnValue, "ClaimDefencesResponse should not be null");
        assertEquals(claimDefencesResponse.getResults(),
                     returnValue.getResults(),
                     "Unexpected ClaimDefencesResponse returned");
        assertEquals(claimDefencesResponse.getResultCount(),
                     returnValue.getResultCount(),
                     "Unexpected number of results in ClaimDefencesResponse");

        verify(mockCmcApi).claimDefences(AUTHORIZATION,
                                         SERVICE_AUTHORISATION,
                                         IDAM_ID_HEADER,
                                         fromDateTime,
                                         toDateTime);
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
