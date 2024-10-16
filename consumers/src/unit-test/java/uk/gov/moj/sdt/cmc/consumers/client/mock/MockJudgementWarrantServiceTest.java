package uk.gov.moj.sdt.cmc.consumers.client.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.request.JudgementWarrantRequest;
import uk.gov.moj.sdt.cmc.consumers.response.JudgementWarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.JudgementWarrantResponseDetail;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static uk.gov.moj.sdt.cmc.consumers.response.JudgmentWarrantStatus.JUDGMENT_ACCEPTED_WARRANT_ACCEPTED_BY_CCBC;

@ExtendWith(MockitoExtension.class)
class MockJudgementWarrantServiceTest extends AbstractSdtUnitTestBase {

    private static final String AUTHORISATION = "";

    private static final String SERVICE_AUTHORISATION = "";

    private static final String IDAM_ID_HEADER = "IDAMID";

    private static final String SDT_REQUEST_ID = "SDTREQUESTID";

    private MockJudgementWarrantService mockJudgementWarrantService;

    @BeforeEach
    @Override
    public void setUp() {
        mockJudgementWarrantService = new MockJudgementWarrantService();
    }

    @Test
    void getClient() {
        JudgementWarrantRequest request = mock(JudgementWarrantRequest.class);
        JudgementWarrantResponse response = mockJudgementWarrantService.judgementWarrantRequest(AUTHORISATION,
                                                                                                SERVICE_AUTHORISATION,
                                                                                                IDAM_ID_HEADER,
                                                                                                SDT_REQUEST_ID,
                                                                                                request);
        assertNotNull(response);

        JudgementWarrantResponseDetail judgementWarrantResponseDetail = response.getJudgementWarrantResponseDetail();
        assertNotNull(judgementWarrantResponseDetail);
        assertEquals(45678L, judgementWarrantResponseDetail.getFee());
        assertEquals("123456", judgementWarrantResponseDetail.getWarrantNumber());
        assertNotNull(judgementWarrantResponseDetail.getFirstPaymentDate());
        assertNotNull(judgementWarrantResponseDetail.getJudgmentEnteredDate());
        assertEquals(JUDGMENT_ACCEPTED_WARRANT_ACCEPTED_BY_CCBC.getMessage(),
                     judgementWarrantResponseDetail.getJudgmentWarrantStatus());
        assertEquals("123", judgementWarrantResponseDetail.getEnforcingCourtCode());
        assertEquals("Court Code", judgementWarrantResponseDetail.getEnforcingCourtName());
    }
}
