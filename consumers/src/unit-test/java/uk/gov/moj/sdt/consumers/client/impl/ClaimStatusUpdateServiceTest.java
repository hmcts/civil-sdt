package uk.gov.moj.sdt.consumers.client.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.ClaimStatusUpdate;
import uk.gov.moj.sdt.cmc.consumers.api.CmcApi;
import uk.gov.moj.sdt.cmc.consumers.client.impl.ClaimStatusUpdateService;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ClaimStatusUpdateServiceTest extends AbstractSdtUnitTestBase {
    @Mock
    private CmcApi cmcApi;

    private ClaimStatusUpdateService claimStatusUpdateService;

    private ClaimStatusUpdate claimStatusUpdateObj;

    private final Object OBJECT = new Object();
    @BeforeEach
    @Override
    public void setUp() {
        claimStatusUpdateService = new ClaimStatusUpdateService(cmcApi);
        claimStatusUpdateObj = new ClaimStatusUpdate();
        claimStatusUpdateObj.setUpdateType("WD");
        claimStatusUpdateObj.setCaseManRef("CaseManRef0101");
        claimStatusUpdateObj.setPaidInFullDate("01/10/2021");
        claimStatusUpdateObj.setRespondantId("1");
        claimStatusUpdateObj.setSection38Compliancy(true);

        when(cmcApi.claimStatusUpdate(anyString(), anyString(), anyString(), anyString(), any())).thenReturn(OBJECT);
        }

    @Test
    void getClaimsStatus() {
        String idAmId ="1L";
        String sdtRequestId ="";

        Object returnValue = claimStatusUpdateService.claimStatusUpdate(claimStatusUpdateObj, idAmId, sdtRequestId);

        assertNotNull(returnValue);
        assertEquals(OBJECT, returnValue);

    }



}
