package uk.gov.moj.sdt.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.utils.mbeans.api.ISdtManagementMBean;

@Service
public class RequeueOldIndividualRequests {

    private static Logger LOGGER = LoggerFactory.getLogger(RequeueOldIndividualRequests.class);

    private ISdtManagementMBean sdtManagementMBean;

    private IIndividualRequestDao individualRequestDao;

    @Value("${sdt.requeue.minimumAge:15}")
    private int minimumAge;

    @Autowired
    public RequeueOldIndividualRequests(ISdtManagementMBean sdtManagementMBean,
                                        @Qualifier("IndividualRequestDao") IIndividualRequestDao individualRequestDao) {
        this.sdtManagementMBean = sdtManagementMBean;
        this.individualRequestDao = individualRequestDao;
    }
    @Scheduled(cron = "${sdt.requeue.cron}")
    public void requeueOldIndividualRequests() {
        LOGGER.debug("EXECUTING: the Scheduled Job to requeue old individual requests");

        if (individualRequestDao.countStaleIndividualRequests(minimumAge) > 0) {
            sdtManagementMBean.requeueOldIndividualRequests(minimumAge);
        } else {
            LOGGER.info("No rejected messages to requeue");
        }

        LOGGER.debug("COMPLETED: the Scheduled Job to requeue old individual requests");
    }

    public void setMinimumAge(int minimumAge) {
        this.minimumAge = minimumAge;
    }

}
