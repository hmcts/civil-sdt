package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.CMCApi;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResult;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

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
        final ClaimDefencesResponse RESPONSE = createClaimDefencesResponse();
        when(cmcApi.claimDefences(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(RESPONSE);
        String fromDateTime = "2020-01-22T11:12:13";
        String toDateTime = "2020-12-01T13:14:15";
        ClaimDefencesResponse returnValue = claimDefencesService.claimDefences(fromDateTime, toDateTime);
        assertNotNull(returnValue);
        assertEquals(RESPONSE, returnValue);
    }


    private ClaimDefencesResponse createClaimDefencesResponse() {
        ClaimDefencesResponse claimDefencesResponse = new ClaimDefencesResponse();

        ClaimDefencesResult result1 = createClaimDefencesResult("case1", "resp1", "2020-11-12",
                "2020-11-13", "type1", "defence1");
        ClaimDefencesResult result2 = createClaimDefencesResult("case1", "resp2", "2020-11-13",
                "2020-11-14", "type2", "defence2");
        ClaimDefencesResult result3 = createClaimDefencesResult("case1", "resp3", "2020-11-14",
                "2020-11-15", "type3", "defence3");
        ClaimDefencesResult result4 = createClaimDefencesResult("case1", "resp4", "2020-11-15",
                "2020-11-16", "type", "defence4");

        ClaimDefencesResult[] results = new ClaimDefencesResult[4];
        results[0] = result1;
        results[1] = result2;
        results[2] = result3;
        results[3] = result4;

        claimDefencesResponse.setResults(results);

        claimDefencesResponse.setResultCount(results.length);
        return claimDefencesResponse;
    }

    private ClaimDefencesResult createClaimDefencesResult(String caseManRef, String respondentId,
                                                          String defendantResponseFiledDate, String defendantResponseCreatedDate,
                                                          String responseType, String defence) {
        ClaimDefencesResult claimDefencesResult = new ClaimDefencesResult(caseManRef, respondentId,
                defendantResponseFiledDate, defendantResponseCreatedDate, responseType, defence);
        return claimDefencesResult;
    }


}
