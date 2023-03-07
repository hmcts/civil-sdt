package uk.gov.moj.sdt.consumers.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.model.ResponseType;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResult;
import uk.gov.moj.sdt.cmc.consumers.util.ResponsesSummaryUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the consumer gateway.
 *
 * @author Mark Dathorne
 */
@ExtendWith(MockitoExtension.class)
class ResponseSummaryUtilTest {

    private ResponsesSummaryUtil responsesSummaryUtil;
    private XmlToObjectConverter xmlToObjectConverter;

    /**
     * Method to do any pre-test set-up.
     */
    @BeforeEach
    public void setUpLocalTests() {
        responsesSummaryUtil  = new ResponsesSummaryUtil(xmlToObjectConverter);
    }

   @Test
   void convertToMcolResultObjectsIsSuccessful() {
        List<ClaimDefencesResult> listObjects = createClaimDefencesList();
        String xml = responsesSummaryUtil.convertToMcolResultsXml(listObjects);
        assertFalse(xml.isEmpty());
        assertTrue(xml.contains("mcolDefenceDetail"));
        assertTrue(xml.contains("defendantId=\"2\""));
   }

    private List<ClaimDefencesResult> createClaimDefencesList() {
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
                "2021-11-09", "2021-11-10T13:19:24", ResponseType.DE.name(),
                "It wasn't me!");
        ClaimDefencesResult result6 = createClaimDefencesResult("case15", "resp15",
                "2021-11-09", "2021-11-10T14:19:24", ResponseType.DE.name(),
                "My name is Shaggy");
        ClaimDefencesResult result7 = createClaimDefencesResult("case15", "resp15",
                "2021-11-09", "2021-11-10T15:19:24", ResponseType.DE.name(),
                "And I am Chaka Demus - innocent");

        return List.of(result1, result2, result3, result4, result5, result6, result7);
    }

    private ClaimDefencesResult createClaimDefencesResult(String caseManRef, String respondentId,
                                                          String defendantResponseFiledDate,
                                                          String defendantResponseCreatedDate, String responseType,
                                                          String defence) {
        return new ClaimDefencesResult(caseManRef, respondentId, defendantResponseFiledDate,
                defendantResponseCreatedDate, responseType ,defence);
    }

}
