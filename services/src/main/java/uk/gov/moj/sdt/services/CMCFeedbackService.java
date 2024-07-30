package uk.gov.moj.sdt.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.dao.api.IBulkSubmissionDao;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.services.api.ICMCFeedbackService;

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
            existingIndividualRequest.setTargetApplicationResponse(individualRequest.getTargetApplicationResponse());
            individualRequestDao.persist(existingIndividualRequest);
        } else {
            log.warn("Individual Request with Sdt Request Reference [{}] not found", sdtRequestId);
        }
    }
}
