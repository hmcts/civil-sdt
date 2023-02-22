package uk.gov.moj.sdt.consumers.client.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.CMCApi;
import uk.gov.moj.sdt.cmc.consumers.client.impl.ClaimStatusUpdateService;
import uk.gov.moj.sdt.cmc.consumers.model.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ClaimStatusUpdateRequestServiceTest extends AbstractSdtUnitTestBase {
    @Mock
    private CMCApi cmcApi;

    private ClaimStatusUpdateService claimStatusUpdateService;

    private ClaimStatusUpdateRequest claimStatusUpdateRequestObj;

    private final Object OBJECT = new Object();
    @BeforeEach
    @Override
    public void setUp() {
        claimStatusUpdateService = new ClaimStatusUpdateService(cmcApi);
        claimStatusUpdateRequestObj = new ClaimStatusUpdateRequest(
            "CaseManRef0101",
            "1",
            "WD",
            "01/10/2021",
            true
        );

        when(cmcApi.claimStatusUpdate(anyString(), anyString(), anyString(), anyString(), any())).thenReturn(OBJECT);
        }

    @Test
    void getClaimsStatus() {
        String idAmId ="1L";
        String sdtRequestId ="";

        Object returnValue = claimStatusUpdateService.claimStatusUpdate(claimStatusUpdateRequestObj, idAmId, sdtRequestId);

        assertNotNull(returnValue);
        assertEquals(OBJECT, returnValue);

    }



}
