package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.request.WarrantRequest;
import uk.gov.moj.sdt.cmc.consumers.response.WarrantResponse;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarrantServiceTest extends CMCConsumersClientTestBase {

    private WarrantService warrantService;

    @Mock
    private WarrantRequest mockWarrantRequest;

    @Mock
    private WarrantResponse mockWarrantResponse;

    @Override
    protected void setUpLocalTests() {
        warrantService = new WarrantService(mockCmcApi);
    }

    @Test
    void warrantRequest() {
        when(mockCmcApi.warrantRequest(AUTHORIZATION,
                                       SERVICE_AUTHORISATION,
                                       IDAM_ID_HEADER,
                                       SDT_REQUEST_ID,
                                       mockWarrantRequest))
            .thenReturn(mockWarrantResponse);

        WarrantResponse warrantResponse = warrantService.warrantRequest(AUTHORIZATION,
                                                                        SERVICE_AUTHORISATION,
                                                                        IDAM_ID_HEADER,
                                                                        SDT_REQUEST_ID,
                                                                        mockWarrantRequest);

        assertNotNull(warrantResponse, "WarrantResponse should not be null");
        assertSame(mockWarrantResponse, warrantResponse, "Unexpected WarrantResponse returned");

        verify(mockCmcApi).warrantRequest(AUTHORIZATION,
                                          SERVICE_AUTHORISATION,
                                          IDAM_ID_HEADER,
                                          SDT_REQUEST_ID,
                                          mockWarrantRequest);
    }

}
