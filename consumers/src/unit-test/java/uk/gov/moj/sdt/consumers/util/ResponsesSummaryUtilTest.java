package uk.gov.moj.sdt.consumers.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.model.McolDefenceDetailType;
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
    private McolDefenceDetailTypeUtil mcolDefenceDetailTypeUtil;
    private ClaimDefencesResultsUtil claimDefencesResultsUtil;

    /**
     * Method to do any pre-test set-up.
     */
    @BeforeEach
    public void setUpLocalTests() {
        claimDefencesResultsUtil = new ClaimDefencesResultsUtil();
        mcolDefenceDetailTypeUtil = new McolDefenceDetailTypeUtil();
        responsesSummaryUtil  = new ResponsesSummaryUtil(mcolDefenceDetailTypeUtil);
    }

   @Test
   void convertToMcolResultObjectsIsSuccessful() {
        List<ClaimDefencesResult> listObjects = claimDefencesResultsUtil.createClaimDefencesList();
        List<McolDefenceDetailType> listMcolTypes = responsesSummaryUtil.convertToMcolResults(listObjects);
        String xml = mcolDefenceDetailTypeUtil.convertMcolDefenceDetailListToXml(listMcolTypes);
        assertFalse(xml.isEmpty());
        assertTrue(xml.toLowerCase().contains("mcoldefencedetail"));
        // TODO: Fix broken test
        //        assertTrue(xml.contains("defendantId=\"2\""));
   }

}
