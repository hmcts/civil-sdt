package uk.gov.moj.sdt.services.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.services.utils.api.IMessagingUtility;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static uk.gov.moj.sdt.domain.api.IIndividualRequest.IndividualRequestStatus.FORWARDED;
import static uk.gov.moj.sdt.domain.api.IIndividualRequest.IndividualRequestStatus.RECEIVED;

@ExtendWith(MockitoExtension.class)
class RequeueIndividualRequestUtilityTest extends AbstractSdtUnitTestBase {

    @Mock
    IIndividualRequestDao mockIndividualRequestDao;

    @Mock
    private IMessagingUtility mockMessagingUtility;

    private RequeueIndividualRequestUtility requeueIndividualRequestUtility;

    @Override
    protected void setUpLocalTests() {
        requeueIndividualRequestUtility =
            new RequeueIndividualRequestUtility(mockIndividualRequestDao, mockMessagingUtility);
    }

    @Test
    void testRequeueIndividualRequest() {
        IndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setRequestStatus(FORWARDED.getStatus());
        individualRequest.setForwardingAttempts(2);
        individualRequest.setDeadLetter(true);

        requeueIndividualRequestUtility.requeueIndividualRequest(individualRequest);

        assertEquals(RECEIVED.getStatus(),
                     individualRequest.getRequestStatus(),
                     "Individual request has unexpected request status");
        assertEquals(0,
                     individualRequest.getForwardingAttempts(),
                     "Individual request has unexpected number of forwarding attempts");
        assertFalse(individualRequest.isDeadLetter(), "Individual request dead letter flag should not be true");

        verify(mockMessagingUtility).enqueueRequest(individualRequest);
        verify(mockIndividualRequestDao).persist(individualRequest);
    }
}
