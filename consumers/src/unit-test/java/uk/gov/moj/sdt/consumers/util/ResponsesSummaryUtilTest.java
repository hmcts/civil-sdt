package uk.gov.moj.sdt.consumers.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.model.McolDefenceDetailType;
import uk.gov.moj.sdt.cmc.consumers.model.ResponseType;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResult;
import uk.gov.moj.sdt.cmc.consumers.util.ResponsesSummaryUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the consumer gateway.
 *
 * @author Mark Dathorne
 */
@ExtendWith(MockitoExtension.class)
class ResponseSummaryUtilTest {

    private ResponsesSummaryUtil responsesSummaryUtil;

    /**
     * Method to do any pre-test set-up.
     */
    @BeforeEach
    public void setUpLocalTests() {
        responsesSummaryUtil  = new ResponsesSummaryUtil();
    }

   @Test
   void convertToMcolResultObjectsIsSuccessful() {
        List<Object> listObjects = createClaimDefencesList();
        List<Object> listConvertedObjects = responsesSummaryUtil.convertToMcolResultObjects(listObjects);
        assertFalse(listConvertedObjects.isEmpty());
        assertEquals(listConvertedObjects.size(), listObjects.size());
        McolDefenceDetailType detailType = (McolDefenceDetailType) listConvertedObjects.get(0);
        assertNotNull(detailType.getDefendantResponse());
   }

    private List<Object> createClaimDefencesList() {
        ClaimDefencesResult result1 = createClaimDefencesResult("case11", "resp11",
                "2021-10-20", "2021-10-21T11:23:34", ResponseType.DE.name(),
                "defence11");
        ClaimDefencesResult result2 = createClaimDefencesResult("case12", "resp12",
                "2021-10-27", "2021-10-28T06:08:10", ResponseType.AS.name(),
                "defence12");
        ClaimDefencesResult result3 = createClaimDefencesResult("case13", "resp13",
                "2021-10-29", "2021-10-30T03:10:23", ResponseType.PA.name(),
                "defence12");
        ClaimDefencesResult result4 = createClaimDefencesResult("case14", "resp14",
                "2021-11-02", "2021-11-02T07:15:49", ResponseType.DC.name(),
                "defence14");
        ClaimDefencesResult result5 = createClaimDefencesResult("case15", "resp15",
                "2021-11-09", "2021-11-10T15:19:24", ResponseType.DE.name(),
                "defence15");

        return List.of(result1, result2, result3, result4, result5);
    }

    private ClaimDefencesResult createClaimDefencesResult(String caseManRef, String respondentId,
                                                          String defendantResponseFiledDate,
                                                          String defendantResponseCreatedDate, String responseType,
                                                          String defence) {
        return new ClaimDefencesResult(caseManRef, respondentId, defendantResponseFiledDate,
                defendantResponseCreatedDate, responseType ,defence);
    }

}
