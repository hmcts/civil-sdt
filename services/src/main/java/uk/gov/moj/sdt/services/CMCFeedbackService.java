package uk.gov.moj.sdt.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.dao.api.IBulkSubmissionDao;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.services.api.ICMCFeedbackService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;

import static uk.gov.moj.sdt.domain.api.IIndividualRequest.IndividualRequestStatus.ACCEPTED;
import static uk.gov.moj.sdt.domain.api.IIndividualRequest.IndividualRequestStatus.REJECTED;

@Component("cmcFeedbackService")
@Slf4j
public class CMCFeedbackService implements ICMCFeedbackService {

    private IIndividualRequestDao individualRequestDao;

    private IBulkSubmissionDao bulkSubmissionDao;

    @Autowired
    public CMCFeedbackService(@Qualifier("IndividualRequestDao") IIndividualRequestDao individualRequestDao,
                              @Qualifier("BulkSubmissionDao") IBulkSubmissionDao bulkSubmissionDao) {
        this.individualRequestDao = individualRequestDao;
        this.bulkSubmissionDao = bulkSubmissionDao;
    }

    @Override
    public void cmcFeedback(IIndividualRequest individualRequest) {
        String sdtRequestId = individualRequest.getSdtRequestReference();
        IIndividualRequest existingIndividualRequest = individualRequestDao.getRequestBySdtReference(sdtRequestId);

        if (existingIndividualRequest != null) {
            existingIndividualRequest.setRequestStatus(individualRequest.getRequestStatus());
            existingIndividualRequest.setTargetApplicationResponse(individualRequest.getTargetApplicationResponse());
            individualRequestDao.persist(existingIndividualRequest);

            IBulkSubmission bulkSubmission = existingIndividualRequest.getBulkSubmission();
            String sdtBulkReference = bulkSubmission.getSdtBulkReference();
            if (allIndividualRequestsCompleted(sdtBulkReference)) {
                log.debug("All individual requests for bulk submission [{}] have been processed now. " +
                              "Marking the bulk submission as Completed", sdtBulkReference);

                bulkSubmission.markAsCompleted();
                bulkSubmissionDao.persist(bulkSubmission);
            }
        } else {
            log.warn("Individual Request with Sdt Request Reference [{}] not found", sdtRequestId);
        }
    }

    private boolean allIndividualRequestsCompleted(String sdtBulkReference) {
        List<String> completeRequestStatusList = Arrays.asList(ACCEPTED.getStatus(), REJECTED.getStatus());

        CriteriaBuilder criteriaBuilder = individualRequestDao.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<IndividualRequest> criteriaQuery = criteriaBuilder.createQuery(IndividualRequest.class);
        Root<IndividualRequest> root = criteriaQuery.from(IndividualRequest.class);

        Predicate[] predicates = new Predicate[2];
        predicates[0] = criteriaBuilder.equal(root.get("sdtBulkReference"), sdtBulkReference);
        Expression<String> requestStatusExpression = root.get("requestStatus");
        predicates[1] = requestStatusExpression.in(completeRequestStatusList).not();

        CriteriaQuery<IndividualRequest> countQuery = criteriaQuery.select(root).where(predicates);

        long requestsCount = individualRequestDao.queryAsCount(IndividualRequest.class, () -> countQuery);

        return requestsCount == 0;
    }
}
