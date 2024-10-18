package uk.gov.moj.sdt.services.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.services.utils.api.IMessagingUtility;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RetryCaseLockedUtilityTest extends AbstractSdtUnitTestBase {

    @Mock
    private IIndividualRequestDao mockIndividualRequestDao;

    @Mock
    private IMessagingUtility mockMessagingUtility;

    private RetryCaseLockedUtility retryCaseLockedUtility;

    @Override
    protected void setUpLocalTests() {
        retryCaseLockedUtility = new RetryCaseLockedUtility(mockIndividualRequestDao, mockMessagingUtility);
    }

    @Test
    void testRetryCaseLockedIndividualRequest() {
        IIndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setRequestStatus(IIndividualRequest.IndividualRequestStatus.CASE_LOCKED.getStatus());
        individualRequest.setForwardingAttempts(1);

        retryCaseLockedUtility.retryCaseLockedIndividualRequest(individualRequest);

        assertEquals(IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus(),
                     individualRequest.getRequestStatus(),
                     "Individual request has unexpected request status");
        assertEquals(0,
                     individualRequest.getForwardingAttempts(),
                     "Individual request has unexpected forwarding attempts value");
        assertNotNull(individualRequest.getUpdatedDate(), "Individual request updated date should not be null");

        verify(mockIndividualRequestDao).persist(individualRequest);
        verify(mockMessagingUtility).enqueueRequest(individualRequest);

    }
}
