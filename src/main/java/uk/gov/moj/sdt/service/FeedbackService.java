package uk.gov.moj.sdt.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.gov.moj.sdt.cmc.consumers.response.ProcessingStatus;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.api.IErrorLog;
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
        if (RequestType.JUDGMENT.getType().equals(requestType)) {
            updateJudgementRequest(cmcFeedback, individualRequest);
        } else if (RequestType.BREATHING_SPACE.getType().equals(requestType)) {
            updateBreathingSpaceRequest(cmcFeedback, individualRequest);
        } else if (RequestType.CLAIM_STATUS_UPDATE.getType().equals(requestType)) {
            updateClaimStatusUpdateRequest(cmcFeedback, individualRequest);
        } else if (RequestType.CLAIM.getType().equals(requestType)) {
            updateCreateClaimRequest(cmcFeedback, individualRequest);
        } else if (RequestType.WARRANT.getType().equals(requestType)) {
            updateWarrantRequest(cmcFeedback, individualRequest);
        } else if (RequestType.JUDGMENT_WARRANT.getType().equals(requestType)) {
            updateCombinedJudgementWarrantRequest(cmcFeedback, individualRequest);
        }
        individualRequestDao.persist(individualRequest);
    }

    private void updateCreateClaimRequest(CMCFeedback cmcFeedback,
                                          IIndividualRequest individualRequest) {
        addStatusAndErrorLog(cmcFeedback, individualRequest);

    }

    private void updateJudgementRequest(CMCFeedback cmcFeedback,
                                          IIndividualRequest individualRequest) {
        addStatusAndErrorLog(cmcFeedback, individualRequest);
    }

    private void updateWarrantRequest(CMCFeedback cmcFeedback,
                                        IIndividualRequest individualRequest) {
        addStatusAndErrorLog(cmcFeedback, individualRequest);
    }

    private void updateCombinedJudgementWarrantRequest(CMCFeedback cmcFeedback,
                                      IIndividualRequest individualRequest) {
        addStatusAndErrorLog(cmcFeedback, individualRequest);
    }

    private void updateClaimStatusUpdateRequest(CMCFeedback cmcFeedback,
                                      IIndividualRequest individualRequest) {
        addStatusAndErrorLog(cmcFeedback, individualRequest);
    }

    private void updateBreathingSpaceRequest(CMCFeedback cmcFeedback,
                                                IIndividualRequest individualRequest) {
        addStatusAndErrorLog(cmcFeedback, individualRequest);
    }

    private void addStatusAndErrorLog(CMCFeedback cmcFeedback, IIndividualRequest individualRequest) {
        if (StringUtils.hasText(cmcFeedback.getErrorText())) {
            final IErrorLog errorLog = new ErrorLog(String.valueOf(cmcFeedback.getErrorCode()), cmcFeedback.getErrorText());
            individualRequest.setErrorLog(errorLog);
        }
        if (StringUtils.hasText(cmcFeedback.getProcessingStatus())) {
            ProcessingStatus processingStatus = ProcessingStatus.valueOf(cmcFeedback.getProcessingStatus());
            if (processingStatus == ProcessingStatus.PROCESSED) {
                individualRequest.setRequestStatus(processingStatus.name());
            }
        }
    }
}
