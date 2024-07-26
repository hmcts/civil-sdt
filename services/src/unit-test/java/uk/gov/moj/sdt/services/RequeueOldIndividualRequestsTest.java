package uk.gov.moj.sdt.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.mbeans.api.ISdtManagementMBean;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequeueOldIndividualRequestsTest extends AbstractSdtUnitTestBase {

    private static final int MINIMUM_AGE = 15;

    private RequeueOldIndividualRequests requeueOldIndividualRequests;

    @Mock
    private ISdtManagementMBean sdtManagementMBean;

    @Mock
    private IIndividualRequestDao individualRequestDao;

    @Override
    protected void setUpLocalTests() {
        requeueOldIndividualRequests = new RequeueOldIndividualRequests(sdtManagementMBean, individualRequestDao);
        requeueOldIndividualRequests.setMinimumAge(MINIMUM_AGE);
    }

    @Test
    void shouldInvokeRequeueOldIndividualRequests() {
        when(individualRequestDao.countStaleIndividualRequests(MINIMUM_AGE)).thenReturn(1L);
        requeueOldIndividualRequests.requeueOldIndividualRequests();
        verify(individualRequestDao).countStaleIndividualRequests(MINIMUM_AGE);
        verify(sdtManagementMBean).requeueOldIndividualRequests(MINIMUM_AGE);
    }

    @Test
    void shouldNotInvokeRequeueOldIndividualRequests() {
        when(individualRequestDao.countStaleIndividualRequests(MINIMUM_AGE)).thenReturn(0L);
        requeueOldIndividualRequests.requeueOldIndividualRequests();
        verify(individualRequestDao).countStaleIndividualRequests(MINIMUM_AGE);
        verify(sdtManagementMBean, never()).requeueOldIndividualRequests(MINIMUM_AGE);
    }

}
