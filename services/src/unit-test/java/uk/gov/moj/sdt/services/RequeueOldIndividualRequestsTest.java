package uk.gov.moj.sdt.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.mbeans.api.ISdtManagementMBean;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RequeueOldIndividualRequestsTest extends AbstractSdtUnitTestBase {

    private static final int MINIMUM_AGE = 15;

    private RequeueOldIndividualRequests requeueOldIndividualRequests;

    @Mock
    private ISdtManagementMBean sdtManagementMBean;

    @BeforeEach
    @Override
    public void setUp() {
        requeueOldIndividualRequests = new RequeueOldIndividualRequests(sdtManagementMBean);
        requeueOldIndividualRequests.setMinimumAge(MINIMUM_AGE);
    }

    @Test
    void shouldInvokeRequeueOldIndividualRequests() {
        requeueOldIndividualRequests.requeueOldIndividualRequests();
        verify(sdtManagementMBean).requeueOldIndividualRequests(MINIMUM_AGE);
    }

}
