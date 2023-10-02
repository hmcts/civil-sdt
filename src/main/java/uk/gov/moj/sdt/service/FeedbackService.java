package uk.gov.moj.sdt.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.response.ProcessingStatus;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.request.CMCFeedback;
import uk.gov.moj.sdt.utils.cmc.RequestType;

@Service
public class FeedbackService {

    private IIndividualRequestDao individualRequestDao;

    public FeedbackService(@Qualifier("IndividualRequestDao")
                               IIndividualRequestDao individualRequestDao) {
        this.individualRequestDao = individualRequestDao;
    }

    public void feedback(String sdtRequestId, CMCFeedback cmcFeedback) {
        IIndividualRequest individualRequest = individualRequestDao.getRequestBySdtReference(sdtRequestId);
        String requestType = individualRequest.getRequestType();
        ProcessingStatus processingStatus = ProcessingStatus.valueOf(cmcFeedback.getProcessingStatus());
        if (RequestType.JUDGMENT.getType().equals(requestType)) {
        } else if (RequestType.BREATHING_SPACE.getType().equals(requestType)) {
            individualRequest.setRequestStatus(cmcFeedback.getProcessingStatus());
        } else if (RequestType.CLAIM_STATUS_UPDATE.getType().equals(requestType)) {
            individualRequest.setRequestStatus(processingStatus.name());
        } else if (RequestType.CLAIM.getType().equals(requestType)) {
            individualRequest.setTargetApplicationResponse(null);
        } else if (RequestType.WARRANT.getType().equals(requestType)) {
            individualRequest.setTargetApplicationResponse(null);
        } else if (RequestType.JUDGMENT_WARRANT.getType().equals(requestType)) {
            individualRequest.setTargetApplicationResponse(null);
        }
        individualRequestDao.persist(individualRequest);
    }
}
