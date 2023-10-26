package uk.gov.moj.sdt.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.services.mbeans.SdtManagementMBean;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RequeueOldIndividualRequestsTest extends AbstractSdtUnitTestBase {

    private RequeueOldIndividualRequests requeueOldIndividualRequests;

    @Mock
    private SdtManagementMBean sdtManagementMBean;

    @BeforeEach
    @Override
    public void setUp() {
        requeueOldIndividualRequests = new RequeueOldIndividualRequests(sdtManagementMBean);
    }

    @Test
    void shouldInvokeRequeueOldIndividualRequests() {
        requeueOldIndividualRequests.requeueOldIndividualRequests();
        verify(sdtManagementMBean).requeueOldIndividualRequests(15);
    }

}
