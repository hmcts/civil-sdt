package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.CMCApi;
import uk.gov.moj.sdt.cmc.consumers.model.breathingspace.BreathingSpace;
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
        BreathingSpace breathingSpaceRequest = mock(BreathingSpace.class);
        breathingSpaceService.breathingSpace(breathingSpaceRequest);
        verify(cmcApi).breathingSpace(anyString(), anyString(), any(BreathingSpace.class));
    }

}
