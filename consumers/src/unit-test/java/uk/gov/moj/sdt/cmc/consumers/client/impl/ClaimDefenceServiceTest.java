package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.CmcApi;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClaimDefenceServiceTest extends AbstractSdtUnitTestBase {

    @Mock
    private CmcApi cmcApi;

    private ClaimDefencesService claimDefencesService;

    private final Object OBJECT = new Object();

    @BeforeEach
    @Override
    public void setUp() {
        claimDefencesService = new ClaimDefencesService(cmcApi);
        when(cmcApi.claimDefences(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(OBJECT);
    }

    @Test
    void getClient() {
        String fromDateTime = "";
        String toDateTime = "";
        Object returnValue = claimDefencesService.claimDefences(fromDateTime, toDateTime);
        assertNotNull(returnValue);
        assertEquals(OBJECT, returnValue);
    }

}
