package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.CMCApi;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BreathingSpaceServiceTest extends AbstractSdtUnitTestBase {

    @Mock
    private CMCApi cmcApi;

    private BreathingSpaceService breathingSpaceService;

    @BeforeEach
    @Override
    public void setUp() {
        breathingSpaceService = new BreathingSpaceService(cmcApi);
    }

    @Test
    void getClient() {
        BreathingSpaceRequest breathingSpaceRequest = mock(BreathingSpaceRequest.class);
        breathingSpaceService.breathingSpace(breathingSpaceRequest);
        verify(cmcApi).breathingSpace(breathingSpaceRequest);
    }

}
