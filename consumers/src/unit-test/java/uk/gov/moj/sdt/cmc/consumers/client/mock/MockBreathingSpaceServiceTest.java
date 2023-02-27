package uk.gov.moj.sdt.cmc.consumers.client.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ProcessingStatus;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class MockBreathingSpaceServiceTest extends AbstractSdtUnitTestBase {

    private MockBreathingSpaceService mockBreathingSpaceService;

    @BeforeEach
    @Override
    public void setUp() {
        mockBreathingSpaceService = new MockBreathingSpaceService();
    }

    @Test
    void shouldReturnBreathingSpaceResponse() {
        BreathingSpaceRequest breathingSpaceRequest = mock(BreathingSpaceRequest.class);
        BreathingSpaceResponse breathingSpaceResponse = mockBreathingSpaceService.breathingSpace(breathingSpaceRequest);
        assertNotNull(breathingSpaceResponse);
        assertEquals(ProcessingStatus.QUEUED, breathingSpaceResponse.getProcessingStatus());
    }
}
