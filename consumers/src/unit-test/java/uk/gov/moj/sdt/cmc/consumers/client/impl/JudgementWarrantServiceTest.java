package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.request.JudgementWarrantRequest;
import uk.gov.moj.sdt.cmc.consumers.response.JudgementWarrantResponse;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JudgementWarrantServiceTest extends CMCConsumersClientTestBase {

    private JudgementWarrantService judgementWarrantService;

    @Mock
    private JudgementWarrantRequest mockJudgementWarrantRequest;

    @Mock
    private JudgementWarrantResponse mockJudgementWarrantResponse;

    @Override
    protected void setUpLocalTests() {
        judgementWarrantService = new JudgementWarrantService(mockCmcApi);
    }

    @Test
    void testJudgementWarrantRequest() {
        when(mockCmcApi.judgementWarrantRequest(AUTHORIZATION,
                                                SERVICE_AUTHORISATION,
                                                IDAM_ID_HEADER,
                                                SDT_REQUEST_ID,
                                                mockJudgementWarrantRequest))
            .thenReturn(mockJudgementWarrantResponse);

        JudgementWarrantResponse judgementWarrantResponse =
            judgementWarrantService.judgementWarrantRequest(AUTHORIZATION,
                                                            SERVICE_AUTHORISATION,
                                                            IDAM_ID_HEADER,
                                                            SDT_REQUEST_ID,
                                                            mockJudgementWarrantRequest);

        assertNotNull(judgementWarrantResponse, "JudgementWarrantResponse should not be null");
        assertSame(mockJudgementWarrantResponse,
                   judgementWarrantResponse,
                   "Unexpected JudgementWarrantResponse returned");

        verify(mockCmcApi).judgementWarrantRequest(AUTHORIZATION,
                                                   SERVICE_AUTHORISATION,
                                                   IDAM_ID_HEADER,
                                                   SDT_REQUEST_ID,
                                                   mockJudgementWarrantRequest);
    }
}
