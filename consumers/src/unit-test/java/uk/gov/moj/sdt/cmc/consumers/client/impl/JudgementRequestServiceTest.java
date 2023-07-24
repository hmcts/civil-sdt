package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgementRequest;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JudgementRequestServiceTest extends CMCConsumersClientTestBase {

    private JudgementRequestService judgementRequestService;

    @Mock
    private JudgementRequest mockJudgementRequest;

    @Mock
    private JudgementResponse mockJudgementResponse;

    @Override
    protected void setUpLocalTests() {
        judgementRequestService = new JudgementRequestService(mockCmcApi);
    }

    @Test
    void testRequestJudgement() {
        when(mockCmcApi.requestJudgment(IDAM_ID_HEADER, SDT_REQUEST_ID, mockJudgementRequest))
            .thenReturn(mockJudgementResponse);

        JudgementResponse judgementResponse =
            judgementRequestService.requestJudgment(IDAM_ID_HEADER, SDT_REQUEST_ID, mockJudgementRequest);

        assertNotNull(judgementResponse, "JudgementResponse should not be null");
        assertSame(mockJudgementResponse, judgementResponse, "Unexpected JudgementResponse returned");

        verify(mockCmcApi).requestJudgment(IDAM_ID_HEADER, SDT_REQUEST_ID, mockJudgementRequest);
    }

}
