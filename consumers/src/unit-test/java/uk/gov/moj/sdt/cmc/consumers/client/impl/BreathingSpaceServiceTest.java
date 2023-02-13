package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.CmcApi;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BreathingSpaceServiceTest extends AbstractSdtUnitTestBase {

    @Mock
    private CmcApi cmcApi;

    private BreathingSpaceService breathingSpaceService;

    private Object OBJECT = new Object();

    @BeforeEach
    @Override
    public void setUp() {
        breathingSpaceService = new BreathingSpaceService(cmcApi);
        when(cmcApi.breathingSpace(anyString(), anyString(), any(IIndividualRequest.class)))
            .thenReturn(OBJECT);
    }

    @Test
    void getClient() {
        IIndividualRequest individualRequest = mock(IIndividualRequest.class);
        Object returnValue = breathingSpaceService.breathingSpace(individualRequest);
        assertNotNull(returnValue);
        assertEquals(OBJECT, returnValue);
        verify(cmcApi.breathingSpace(anyString(), anyString(), any(IIndividualRequest.class)));
    }

}
