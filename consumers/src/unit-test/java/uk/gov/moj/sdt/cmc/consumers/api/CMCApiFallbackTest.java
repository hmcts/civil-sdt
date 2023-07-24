package uk.gov.moj.sdt.cmc.consumers.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.request.JudgementWarrantRequest;
import uk.gov.moj.sdt.cmc.consumers.request.WarrantRequest;
import uk.gov.moj.sdt.cmc.consumers.request.claim.ClaimRequest;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgementRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimStatusUpdateResponse;
import uk.gov.moj.sdt.cmc.consumers.response.JudgementWarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.WarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponse;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CMCApiFallbackTest {

    private static final String IDAM_ID_HEADER = "Test IDAM id";
    private static final String SDT_REQUEST_ID = "Test SDT Request id";
    private static final String SERVICE_AUTHORISATION = "Test service authorisation";

    private CMCApiFallback cmcApiFallback;

    @Mock
    private IBreathingSpaceService mockBreathingSpaceService;
    @Mock
    private BreathingSpaceRequest mockBreathingSpaceRequest;
    @Mock
    private BreathingSpaceResponse mockBreathingSpaceResponse;

    @Mock
    private IJudgementService mockJudgementService;
    @Mock
    private JudgementRequest mockJudgementRequest;
    @Mock
    private JudgementResponse mockJudgementResponse;

    @Mock
    private IClaimDefencesService mockClaimDefencesService;
    @Mock
    private ClaimDefencesResponse mockClaimDefencesResponse;

    @Mock
    private IClaimStatusUpdateService mockClaimStatusUpdateService;
    @Mock
    private ClaimStatusUpdateRequest mockClaimStatusUpdateRequest;
    @Mock
    private ClaimStatusUpdateResponse mockClaimStatusUpdateResponse;

    @Mock
    private IClaimRequestService mockClaimRequestService;
    @Mock
    private ClaimRequest mockClaimRequest;
    @Mock
    private ClaimResponse mockClaimResponse;

    @Mock
    private IWarrantService mockWarrantService;
    @Mock
    private WarrantRequest mockWarrantRequest;
    @Mock
    private WarrantResponse mockWarrantResponse;

    @Mock
    private IJudgementWarrantService mockJudgementWarrantService;
    @Mock
    private JudgementWarrantRequest mockJudgementWarrantRequest;
    @Mock
    private JudgementWarrantResponse mockJudgementWarrantResponse;

    @BeforeEach
    void setUp() {
        cmcApiFallback = new CMCApiFallback(mockBreathingSpaceService,
                                            mockClaimDefencesService,
                                            mockJudgementService,
                                            mockClaimStatusUpdateService,
                                            mockClaimRequestService,
                                            mockWarrantService,
                                            mockJudgementWarrantService);
    }

    @Test
    void testBreathingSpace() {
        when(mockBreathingSpaceService.breathingSpace(IDAM_ID_HEADER, SDT_REQUEST_ID, mockBreathingSpaceRequest))
            .thenReturn(mockBreathingSpaceResponse);

        BreathingSpaceResponse breathingSpaceResponse =
            cmcApiFallback.breathingSpace(IDAM_ID_HEADER, SDT_REQUEST_ID, mockBreathingSpaceRequest);

        assertNotNull(breathingSpaceResponse, "BreathingSpaceResponse should not be null");
        assertSame(mockBreathingSpaceResponse, breathingSpaceResponse, "Unexpected BreathingSpaceResponse returned");

        verify(mockBreathingSpaceService).breathingSpace(IDAM_ID_HEADER, SDT_REQUEST_ID, mockBreathingSpaceRequest);
    }

    @Test
    void testJudgementResponse() {
        when(mockJudgementService.requestJudgment(IDAM_ID_HEADER, SDT_REQUEST_ID, mockJudgementRequest))
            .thenReturn(mockJudgementResponse);

        JudgementResponse judgementResponse =
            cmcApiFallback.requestJudgment(IDAM_ID_HEADER, SDT_REQUEST_ID, mockJudgementRequest);

        assertNotNull(judgementResponse, "JudgementResponse should not be null");
        assertSame(mockJudgementResponse, judgementResponse, "Unexpected JudgementResponse returned");

        verify(mockJudgementService).requestJudgment(IDAM_ID_HEADER, SDT_REQUEST_ID, mockJudgementRequest);
    }

    @Test
    void testClaimStatusUpdate() {
        when(mockClaimStatusUpdateService.claimStatusUpdate(IDAM_ID_HEADER,
                                                            SDT_REQUEST_ID,
                                                            mockClaimStatusUpdateRequest))
            .thenReturn(mockClaimStatusUpdateResponse);

        ClaimStatusUpdateResponse claimStatusUpdateResponse =
            cmcApiFallback.claimStatusUpdate(IDAM_ID_HEADER, SDT_REQUEST_ID, mockClaimStatusUpdateRequest);

        assertNotNull(claimStatusUpdateResponse, "ClaimStatusUpdateResponse should not be null");
        assertSame(mockClaimStatusUpdateResponse,
                   claimStatusUpdateResponse,
                   "Unexpected ClaimStatusUpdateResponse returned");

        verify(mockClaimStatusUpdateService).claimStatusUpdate(IDAM_ID_HEADER,
                                                               SDT_REQUEST_ID,
                                                               mockClaimStatusUpdateRequest);
    }

    @Test
    void testClaimDefencesResponse() {
        String fromDate = "2023-01-01T00:00:00";
        String toDate = "2023-01-10T23:59:59";
        when(mockClaimDefencesService.claimDefences(AUTHORIZATION,
                                                    SERVICE_AUTHORISATION,
                                                    IDAM_ID_HEADER,
                                                    fromDate,
                                                    toDate))
            .thenReturn(mockClaimDefencesResponse);

        ClaimDefencesResponse claimDefencesResponse =
            cmcApiFallback.claimDefences(AUTHORIZATION, SERVICE_AUTHORISATION, IDAM_ID_HEADER, fromDate, toDate);

        assertNotNull(claimDefencesResponse, "ClaimDefencesResponse should not be null");
        assertSame(mockClaimDefencesResponse, claimDefencesResponse, "Unexpected ClaimDefencesResponse returned");

        verify(mockClaimDefencesService).claimDefences(AUTHORIZATION,
                                                       SERVICE_AUTHORISATION,
                                                       IDAM_ID_HEADER,
                                                       fromDate,
                                                       toDate);
    }

    @Test
    void testWarrantResponse() {
        when(mockWarrantService.warrantRequest(AUTHORIZATION,
                                               SERVICE_AUTHORISATION,
                                               IDAM_ID_HEADER,
                                               SDT_REQUEST_ID,
                                               mockWarrantRequest))
            .thenReturn(mockWarrantResponse);

        WarrantResponse warrantResponse =
            cmcApiFallback.warrantRequest(AUTHORIZATION,
                                          SERVICE_AUTHORISATION,
                                          IDAM_ID_HEADER,
                                          SDT_REQUEST_ID,
                                          mockWarrantRequest);

        assertNotNull(warrantResponse, "WarrantResponse should not be nuill");
        assertSame(mockWarrantResponse, warrantResponse, "Unexpected WarrantResponse returned");

        verify(mockWarrantService).warrantRequest(AUTHORIZATION,
                                                  SERVICE_AUTHORISATION,
                                                  IDAM_ID_HEADER,
                                                  SDT_REQUEST_ID,
                                                  mockWarrantRequest);
    }

    @Test
    void testJudgementWarrantResponse() {
        when(mockJudgementWarrantService.judgementWarrantRequest(AUTHORIZATION,
                                                                 SERVICE_AUTHORISATION,
                                                                 IDAM_ID_HEADER,
                                                                 SDT_REQUEST_ID,
                                                                 mockJudgementWarrantRequest))
            .thenReturn(mockJudgementWarrantResponse);

        JudgementWarrantResponse judgementWarrantResponse =
            cmcApiFallback.judgementWarrantRequest(AUTHORIZATION,
                                                   SERVICE_AUTHORISATION,
                                                   IDAM_ID_HEADER,
                                                   SDT_REQUEST_ID,
                                                   mockJudgementWarrantRequest);

        assertNotNull(judgementWarrantResponse, "JudgementWarrantResponse should not be null");
        assertSame(mockJudgementWarrantResponse,
                   judgementWarrantResponse,
                   "Unexpected JudgementWarrantResponse returned");

        verify(mockJudgementWarrantService).judgementWarrantRequest(AUTHORIZATION,
                                                                    SERVICE_AUTHORISATION,
                                                                    IDAM_ID_HEADER,
                                                                    SDT_REQUEST_ID,
                                                                    mockJudgementWarrantRequest);
    }

    @Test
    void testCreateSdtClaim() {
        when(mockClaimRequestService.claimRequest(IDAM_ID_HEADER, SDT_REQUEST_ID, mockClaimRequest))
            .thenReturn(mockClaimResponse);

        ClaimResponse claimResponse = cmcApiFallback.createSDTClaim(IDAM_ID_HEADER, SDT_REQUEST_ID, mockClaimRequest);

        assertNotNull(claimResponse, "ClaimResponse should not be null");
        assertSame(mockClaimResponse, claimResponse, "Unexpected ClaimResponse returned");

        verify(mockClaimRequestService).claimRequest(IDAM_ID_HEADER, SDT_REQUEST_ID, mockClaimRequest);
    }
}
