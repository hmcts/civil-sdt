package uk.gov.moj.sdt.cmc.consumers.client.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.model.CmcRequest;
import uk.gov.moj.sdt.cmc.consumers.model.ICmcRequest;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class MockClaimDefencesServiceTest extends AbstractSdtUnitTestBase {

    private MockClaimDefencesService mockClaimDefencesService;

    @BeforeEach
    @Override
    public void setUp() {
        mockClaimDefencesService = new MockClaimDefencesService();
    }

    @Test
    void getClient() {
        final String idAmId = "";
        final String fromDateTime = "";
        final String toDateTime = "";
        ICmcRequest cmcRequest = new CmcRequest(idAmId, fromDateTime, toDateTime);
        Object returnValue = mockClaimDefencesService.claimDefences(cmcRequest);
        assertNull(returnValue);
    }

}
