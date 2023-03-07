package uk.gov.moj.sdt.consumers.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.model.ResponseType;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResult;
import uk.gov.moj.sdt.cmc.consumers.util.ResponsesSummaryUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
                LocalDate.of(2021,10,20),
                LocalDateTime.of(2021,10,21,11,20,11),
                ResponseType.DE.name(),
                "defence11");
        ClaimDefencesResult result2 = createClaimDefencesResult("case12", "resp12",
                LocalDate.of(2021,10,27),
                LocalDateTime.of(2021,10,28,6,8,10),
                ResponseType.AS.name(),
                "defence12");
        ClaimDefencesResult result3 = createClaimDefencesResult("case13", "resp13",
                LocalDate.of(2021,10,29),
                LocalDateTime.of(2021,10,30,3,10,23),
                ResponseType.PA.name(),
                "defence12");
        ClaimDefencesResult result4 = createClaimDefencesResult("case14", "resp14",
                LocalDate.of(2021,11,2),
                LocalDateTime.of(2021,11,2,7,15,49),
                ResponseType.DC.name(),
                "defence14");
        ClaimDefencesResult result5 = createClaimDefencesResult("case15", "resp15",
                LocalDate.of(2021,11,9),
                LocalDateTime.of(2021,11,10,13,19,24),
                ResponseType.DE.name(),
                "It wasn't me!");
        ClaimDefencesResult result6 = createClaimDefencesResult("case15", "resp15",
                LocalDate.of(2021,11,9),
                LocalDateTime.of(2021,11, 10,14,19,24),
                ResponseType.DE.name(),
                "My name is Shaggy");
        ClaimDefencesResult result7 = createClaimDefencesResult("case15", "resp15",
                LocalDate.of(2021,11,9),
                LocalDateTime.of(2021,11,10,15,19,24),
                ResponseType.DE.name(),
                "And I am Chaka Demus - innocent");

        return List.of(result1, result2, result3, result4, result5, result6, result7);
    }

    private ClaimDefencesResult createClaimDefencesResult(String caseManRef, String respondentId,
                                                          LocalDate defendantResponseFiledDate,
                                                          LocalDateTime defendantResponseCreatedDate, String responseType,
                                                          String defence) {
        return new ClaimDefencesResult(caseManRef, respondentId, defendantResponseFiledDate,
                defendantResponseCreatedDate, responseType ,defence);
    }

}
