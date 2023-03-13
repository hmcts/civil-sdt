package uk.gov.moj.sdt.cmc.consumers.client.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.model.SubmitQueryResponse;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        final String fromDateTime = "";
        final String toDateTime = "";
        SubmitQueryResponse returnValue = mockClaimDefencesService.claimDefences("", "", "", fromDateTime, toDateTime);
        assertNotNull(returnValue);
    }

}
