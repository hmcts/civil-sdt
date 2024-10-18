package uk.gov.moj.sdt.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.services.utils.RetryCaseLockedUtility;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetryCaseLockedServiceTest extends AbstractSdtUnitTestBase {

    private static final int MINIMUM_AGE_IN_MINUTES = 60;

    @Mock
    private IIndividualRequestDao mockIndividualRequestDao;

    @Mock
    private RetryCaseLockedUtility mockRetryCaseLockedUtility;

    private RetryCaseLockedService retryCaseLockedService;

    @Override
    protected void setUpLocalTests() {
        retryCaseLockedService = new RetryCaseLockedService(mockIndividualRequestDao, mockRetryCaseLockedUtility);
        retryCaseLockedService.setMinimumAge(MINIMUM_AGE_IN_MINUTES);
    }

    @Test
    void testCaseLockedRequests() {
        List<IIndividualRequest> individualRequests = new ArrayList<>();
        individualRequests.add(createIndividualRequest("MCOL-20241016000000-000000001-0000001"));
        individualRequests.add(createIndividualRequest("MCOL-20241016000000-000000001-0000002"));

        when(mockIndividualRequestDao.getCaseLockedIndividualRequests(MINIMUM_AGE_IN_MINUTES))
            .thenReturn(individualRequests);

        retryCaseLockedService.retryCaseLockedIndividualRequests();

        verify(mockIndividualRequestDao).getCaseLockedIndividualRequests(MINIMUM_AGE_IN_MINUTES);
        verify(mockRetryCaseLockedUtility, times(2)).retryCaseLockedIndividualRequest(any(IIndividualRequest.class));
    }

    @Test
    void testNoCaseLockedRequests() {
        List<IIndividualRequest> individualRequests = new ArrayList<>();
        when(mockIndividualRequestDao.getCaseLockedIndividualRequests(MINIMUM_AGE_IN_MINUTES))
            .thenReturn(individualRequests);

        retryCaseLockedService.retryCaseLockedIndividualRequests();

        verify(mockIndividualRequestDao).getCaseLockedIndividualRequests(MINIMUM_AGE_IN_MINUTES);
        verify(mockRetryCaseLockedUtility, never()).retryCaseLockedIndividualRequest(any(IIndividualRequest.class));
    }

    private IIndividualRequest createIndividualRequest(String requestRef) {
        IIndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setSdtRequestReference(requestRef);
        individualRequest.markRequestAsCaseLocked();

        return individualRequest;
    }
}
