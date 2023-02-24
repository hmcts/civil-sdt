package uk.gov.moj.sdt.cmc.consumers.client.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

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
    void getClient() {
        BreathingSpaceRequest breathingSpaceRequest = mock(BreathingSpaceRequest.class);
        mockBreathingSpaceService.breathingSpace(breathingSpaceRequest);
    }
}
