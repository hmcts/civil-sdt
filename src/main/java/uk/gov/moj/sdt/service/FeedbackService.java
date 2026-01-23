package uk.gov.moj.sdt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlConverter;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimResponse;
import uk.gov.moj.sdt.cmc.consumers.response.JudgementWarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.JudgmentWarrantStatus;
import uk.gov.moj.sdt.cmc.consumers.response.ProcessingStatus;
import uk.gov.moj.sdt.cmc.consumers.response.WarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponse;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.request.CMCFeedback;
import uk.gov.moj.sdt.utils.cmc.RequestType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class FeedbackService {

    private IIndividualRequestDao individualRequestDao;

    private XmlConverter xmlToObject;

    public FeedbackService(@Qualifier("IndividualRequestDao")
                               IIndividualRequestDao individualRequestDao,
                           XmlConverter xmlToObject) {
        this.individualRequestDao = individualRequestDao;
        this.xmlToObject = xmlToObject;
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
        ClaimResponse response = new ClaimResponse();
        response.setClaimNumber(cmcFeedback.getClaimNumber());
        response.setIssueDate(cmcFeedback.getIssueDate());
        response.setServiceDate(cmcFeedback.getServiceDate());
        response.setFee(cmcFeedback.getFee());
        individualRequest.setTargetApplicationResponse(convertObjectToXml(response));
    }

    private void updateJudgementRequest(CMCFeedback cmcFeedback,
                                          IIndividualRequest individualRequest) {
        addStatusAndErrorLog(cmcFeedback, individualRequest);
        JudgementResponse response = new JudgementResponse();
        response.setJudgmentWarrantStatus(JudgmentWarrantStatus
                                              .getJudgmentWarrantStatus(cmcFeedback.getJudgmentWarrantStatus()));
        response.setJudgmentEnteredDate(cmcFeedback.getJudgmentEnteredDate());
        response.setFirstPaymentDate(cmcFeedback.getFirstPaymentDate());
        individualRequest.setTargetApplicationResponse(convertObjectToXml(response));
    }

    private void updateWarrantRequest(CMCFeedback cmcFeedback,
                                        IIndividualRequest individualRequest) {
        addStatusAndErrorLog(cmcFeedback, individualRequest);
        WarrantResponse response = new WarrantResponse();
        response.setIssueDate(cmcFeedback.getIssueDate());
        response.setJudgmentWarrantStatus(JudgmentWarrantStatus
                                              .getJudgmentWarrantStatus(cmcFeedback.getJudgmentWarrantStatus()));
        response.setFee(cmcFeedback.getFee());
        response.setWarrantNumber(cmcFeedback.getWarrantNumber());
        response.setEnforcingCourtCode(cmcFeedback.getEnforcingCourtCode());
        response.setEnforcingCourtName(cmcFeedback.getEnforcingCourtName());
        individualRequest.setTargetApplicationResponse(convertObjectToXml(response));
    }

    private void updateCombinedJudgementWarrantRequest(CMCFeedback cmcFeedback,
                                      IIndividualRequest individualRequest) {
        addStatusAndErrorLog(cmcFeedback, individualRequest);
        JudgementWarrantResponse response = new JudgementWarrantResponse();
        response.setJudgmentWarrantStatus(JudgmentWarrantStatus
                                              .getJudgmentWarrantStatus(cmcFeedback.getJudgmentWarrantStatus()));
        response.setFee(cmcFeedback.getFee());
        response.setWarrantNumber(cmcFeedback.getWarrantNumber());
        response.setEnforcingCourtName(cmcFeedback.getEnforcingCourtName());
        response.setEnforcingCourtCode(cmcFeedback.getEnforcingCourtCode());
        response.setJudgmentEnteredDate(cmcFeedback.getJudgmentEnteredDate());
        response.setFirstPaymentDate(cmcFeedback.getFirstPaymentDate());
        response.setIssueDate(cmcFeedback.getIssueDate());
        individualRequest.setTargetApplicationResponse(convertObjectToXml(response));
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

    private byte[] convertObjectToXml(Object response) {
        try {
            return xmlToObject.convertObjectToXml(response).getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "".getBytes();
        }
    }
}
