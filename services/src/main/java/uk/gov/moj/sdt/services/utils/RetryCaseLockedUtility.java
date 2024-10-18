package uk.gov.moj.sdt.services.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.services.utils.api.IMessagingUtility;

@Component("retryCaseLockedUtility")
@Slf4j
public class RetryCaseLockedUtility {

    private IIndividualRequestDao individualRequestDao;

    private IMessagingUtility messagingUtility;

    @Autowired
    public RetryCaseLockedUtility(@Qualifier("IndividualRequestDao") IIndividualRequestDao individualRequestDao,
                                  @Qualifier("MessagingUtility") IMessagingUtility messagingUtility) {
        this.individualRequestDao = individualRequestDao;
        this.messagingUtility = messagingUtility;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void retryCaseLockedIndividualRequest(IIndividualRequest individualRequest) {
        log.debug("Retrying case locked individual request [{}]", individualRequest.getSdtRequestReference());

        individualRequest.resetForwardingAttempts();
        individualRequestDao.persist(individualRequest);

        messagingUtility.enqueueRequest(individualRequest);
    }
}
