package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BreathingSpaceServiceTest extends CMCConsumersClientTestBase {

    private BreathingSpaceService breathingSpaceService;

    @Mock
    private BreathingSpaceRequest mockBreathingSpaceRequest;

    @Mock
    private BreathingSpaceResponse mockBreathingSpaceResponse;

    @Override
    protected void setUpLocalTests() {
        breathingSpaceService = new BreathingSpaceService(mockCmcApi);
    }

    @Test
    void getClient() {
        when(mockCmcApi.breathingSpace(IDAM_ID_HEADER,
                                       SDT_REQUEST_ID,
                                       mockBreathingSpaceRequest))
            .thenReturn(mockBreathingSpaceResponse);

        BreathingSpaceResponse breathingSpaceResponse =
            breathingSpaceService.breathingSpace(IDAM_ID_HEADER, SDT_REQUEST_ID, mockBreathingSpaceRequest);

        assertNotNull(breathingSpaceResponse, "BreathingSpaceResponse should not be null");
        assertSame(mockBreathingSpaceResponse, breathingSpaceResponse, "Unexpected BreathingSpaceResponse returned");

        verify(mockCmcApi).breathingSpace(IDAM_ID_HEADER, SDT_REQUEST_ID, mockBreathingSpaceRequest);
    }

}
