package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.CmcApi;
import uk.gov.moj.sdt.cmc.consumers.model.CmcRequest;
import uk.gov.moj.sdt.cmc.consumers.model.ICmcRequest;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
        when(cmcApi.claimDefences(anyString(), anyString(), any()))
                .thenReturn(OBJECT);
    }

    @Test
    void getClient() {
        String idAmId = "";
        String fromDateTime = "";
        String toDateTime = "";
        ICmcRequest cmcRequest = new CmcRequest(idAmId, fromDateTime, toDateTime);

        Object returnValue = claimDefencesService.claimDefences(cmcRequest);
        assertNotNull(returnValue);
        assertEquals(OBJECT, returnValue);
    }

}
