package uk.gov.moj.sdt.services;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.services.utils.RetryCaseLockedUtility;
import uk.gov.moj.sdt.services.utils.api.IMessagingUtility;

import java.util.List;

@Slf4j
@Setter
@Service
public class RetryCaseLockedService {

    private IIndividualRequestDao individualRequestDao;

    private RetryCaseLockedUtility retryCaseLockedUtility;

    @Value("${sdt.case-locked.minimumAge:60}")
    @Getter
    private int minimumAge;

    @Autowired
    public RetryCaseLockedService(@Qualifier("IndividualRequestDao") IIndividualRequestDao individualRequestDao,
                                  @Qualifier("retryCaseLockedUtility") RetryCaseLockedUtility retryCaseLockedUtility) {
        this.individualRequestDao = individualRequestDao;
        this.retryCaseLockedUtility = retryCaseLockedUtility;
    }

    @Scheduled(cron = "${sdt.case-locked.cron}")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void retryCaseLockedIndividualRequests() {
        log.debug("Start scheduled job to retry case locked requests");

        final List<IIndividualRequest> requests = individualRequestDao.getCaseLockedIndividualRequests(minimumAge);

        if (requests.isEmpty()) {
            log.info("No case locked requests to retry");
        } else {
            log.info("Retrying {} case locked request(s) older than {} minutes", requests.size(), minimumAge);

            for (IIndividualRequest individualRequest : requests) {
                retryCaseLockedUtility.retryCaseLockedIndividualRequest(individualRequest);
            }
        }
        log.debug("End scheduled job to retry case locked requests");
    }
}
