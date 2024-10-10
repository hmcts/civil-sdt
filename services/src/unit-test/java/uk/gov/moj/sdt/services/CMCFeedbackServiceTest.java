package uk.gov.moj.sdt.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.dao.api.IBulkSubmissionDao;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.moj.sdt.domain.api.IBulkSubmission.BulkRequestStatus.COMPLETED;
import static uk.gov.moj.sdt.domain.api.IBulkSubmission.BulkRequestStatus.VALIDATED;
import static uk.gov.moj.sdt.domain.api.IIndividualRequest.IndividualRequestStatus.ACCEPTED;
import static uk.gov.moj.sdt.domain.api.IIndividualRequest.IndividualRequestStatus.FORWARDED;
import static uk.gov.moj.sdt.domain.api.IIndividualRequest.IndividualRequestStatus.REJECTED;

@ExtendWith(MockitoExtension.class)
class CMCFeedbackServiceTest extends AbstractSdtUnitTestBase {

    private static final String REQUEST_SDT_REF = "MCOL-20241010120000-000000001-0000001";

    private static final String REQUEST_TARGET_APP_RESPONSE = "Test target app response";

    @Mock
    private IIndividualRequestDao mockIndividualRequestDao;

    @Mock
    private IBulkSubmissionDao mockBulkSubmissionDao;

    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private CriteriaBuilder mockCriteriaBuilder;

    @Mock
    private Root<IndividualRequest> mockRoot;

    @Mock
    private Path<Expression<String>> mockExpressionBulkRef;

    @Mock
    private Path<Expression<String>> mockExpressionReqStatus;

    @Mock
    private Predicate mockPredicate;

    @Mock
    private CriteriaQuery<IndividualRequest> mockCriteriaQuery;

    @Mock
    private CriteriaQuery<IndividualRequest> mockCriteriaQuerySelect;

    private CMCFeedbackService cmcFeedbackService;

    @Override
    protected void setUpLocalTests() {
        cmcFeedbackService = new CMCFeedbackService(mockIndividualRequestDao, mockBulkSubmissionDao);
    }

    @Test
    void testCmcFeedbackBulkSubmissionComplete() {
        IIndividualRequest cmcIndividualRequest = createCmcIndividualRequest();
        IIndividualRequest existingIndividualRequest = createExistingIndividualRequest();

        when(mockIndividualRequestDao.getRequestBySdtReference(REQUEST_SDT_REF)).thenReturn(existingIndividualRequest);
        setQueryMockBehaviour(0L);

        cmcFeedbackService.cmcFeedback(cmcIndividualRequest);

        assertEquals(ACCEPTED.getStatus(),
                     existingIndividualRequest.getRequestStatus(),
                     "Individual request has unexpected request status");
        String actualTargetAppResponse = existingIndividualRequest.getTargetApplicationResponse() == null ? "" :
            new String(existingIndividualRequest.getTargetApplicationResponse(), StandardCharsets.UTF_8);
        assertEquals(REQUEST_TARGET_APP_RESPONSE,
                     actualTargetAppResponse,
                     "Individual request has unexpected target app response");

        IBulkSubmission bulkSubmission = existingIndividualRequest.getBulkSubmission();
        assertEquals(COMPLETED.getStatus(),
                     bulkSubmission.getSubmissionStatus(),
                     "Bulk submission has unexpected submission status");
        assertNotNull(bulkSubmission.getUpdatedDate(), "Bulk submission updated date should not be null");
        assertNotNull(bulkSubmission.getCompletedDate(), "Bulk submission completed date should not be null");

        verify(mockIndividualRequestDao).getRequestBySdtReference(REQUEST_SDT_REF);
        verify(mockIndividualRequestDao).persist(existingIndividualRequest);
        verify(mockIndividualRequestDao).queryAsCount(any(), any());
        verify(mockBulkSubmissionDao).persist(bulkSubmission);
    }

    @Test
    void testCmcFeedbackBulkSubmissionNotComplete() {
        IIndividualRequest cmcIndividualRequest = createCmcIndividualRequest();
        IIndividualRequest existingIndividualRequest = createExistingIndividualRequest();

        when(mockIndividualRequestDao.getRequestBySdtReference(REQUEST_SDT_REF)).thenReturn(existingIndividualRequest);
        setQueryMockBehaviour(1L);

        cmcFeedbackService.cmcFeedback(cmcIndividualRequest);

        assertEquals(ACCEPTED.getStatus(),
                     existingIndividualRequest.getRequestStatus(),
                     "Individual request has unexpected request status");
        String actualTargetAppResponse = existingIndividualRequest.getTargetApplicationResponse() == null ? "" :
            new String(existingIndividualRequest.getTargetApplicationResponse(), StandardCharsets.UTF_8);
        assertEquals(REQUEST_TARGET_APP_RESPONSE,
                     actualTargetAppResponse,
                     "Individual request has unexpected target app response");

        IBulkSubmission bulkSubmission = existingIndividualRequest.getBulkSubmission();
        assertEquals(VALIDATED.getStatus(),
                     bulkSubmission.getSubmissionStatus(),
                     "Bulk submission has unexpected submission status");
        assertNull(bulkSubmission.getUpdatedDate(), "Bulk submission updated date should be null");
        assertNull(bulkSubmission.getCompletedDate(), "Bulk submission completed date should be null");

        verify(mockIndividualRequestDao).getRequestBySdtReference(REQUEST_SDT_REF);
        verify(mockIndividualRequestDao).persist(existingIndividualRequest);
        verify(mockIndividualRequestDao).queryAsCount(any(), any());
        verify(mockBulkSubmissionDao, never()).persist(bulkSubmission);
    }

    @Test
    void testCmcFeedbackExistingIndividualRequestNotFound() {
        IIndividualRequest cmcIndividualRequest = createCmcIndividualRequest();

        when(mockIndividualRequestDao.getRequestBySdtReference(REQUEST_SDT_REF)).thenReturn(null);

        cmcFeedbackService.cmcFeedback(cmcIndividualRequest);

        verify(mockIndividualRequestDao).getRequestBySdtReference(REQUEST_SDT_REF);
        verify(mockIndividualRequestDao, never()).persist(any(IIndividualRequest.class));
        verify(mockBulkSubmissionDao, never()).persist(any(IBulkSubmission.class));
    }

    private IIndividualRequest createCmcIndividualRequest() {
        IIndividualRequest individualRequest = new IndividualRequest();

        individualRequest.setSdtRequestReference(REQUEST_SDT_REF);
        individualRequest.setRequestStatus(ACCEPTED.getStatus());
        individualRequest.setTargetApplicationResponse(REQUEST_TARGET_APP_RESPONSE.getBytes(StandardCharsets.UTF_8));

        return individualRequest;
    }

    private IIndividualRequest createExistingIndividualRequest() {
        IBulkSubmission bulkSubmission = new BulkSubmission();
        bulkSubmission.setSubmissionStatus(VALIDATED.getStatus());

        IIndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setSdtRequestReference(REQUEST_SDT_REF);
        individualRequest.setRequestStatus(FORWARDED.getStatus());
        individualRequest.setBulkSubmission(bulkSubmission);

        return individualRequest;
    }

    private void setQueryMockBehaviour(long countValue) {
        List<String> statusList = Arrays.asList(ACCEPTED.getStatus(), REJECTED.getStatus());

        when(mockIndividualRequestDao.getEntityManager()).thenReturn(mockEntityManager);
        when(mockEntityManager.getCriteriaBuilder()).thenReturn(mockCriteriaBuilder);
        when(mockCriteriaBuilder.createQuery(IndividualRequest.class)).thenReturn(mockCriteriaQuery);
        when(mockCriteriaQuery.from(IndividualRequest.class)).thenReturn(mockRoot);
        when(mockRoot.<Expression<String>>get("sdtBulkReference")).thenReturn(mockExpressionBulkRef);
        when(mockRoot.<Expression<String>>get("requestStatus")).thenReturn(mockExpressionReqStatus);
        when(mockExpressionReqStatus.in(statusList)).thenReturn(mockPredicate);
        when(mockCriteriaQuery.select(mockRoot)).thenReturn(mockCriteriaQuerySelect);
        when(mockIndividualRequestDao.queryAsCount(any(), any())).thenReturn(countValue);
    }
}
