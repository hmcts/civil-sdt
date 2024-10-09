package uk.gov.moj.sdt.services.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.services.utils.api.IMessagingUtility;

@Component("requeueIndividualRequestUtility")
@Slf4j
public class RequeueIndividualRequestUtility {

    private final IIndividualRequestDao individualRequestDao;

    private final IMessagingUtility messagingUtility;

    @Autowired
    public RequeueIndividualRequestUtility(IIndividualRequestDao individualRequestDao,
                                           IMessagingUtility messagingUtility) {
        this.individualRequestDao = individualRequestDao;
        this.messagingUtility = messagingUtility;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requeueIndividualRequest(IIndividualRequest individualRequest) {
        log.debug("Re-queue pending individual request [{}]", individualRequest.getSdtRequestReference());
        individualRequest.setDeadLetter(false);

        messagingUtility.enqueueRequest(individualRequest);

        // Re-set the forwarding attempts on the individual request.
        individualRequest.resetForwardingAttempts();

        // Persist the individual request.
        individualRequestDao.persist(individualRequest);
    }
}
