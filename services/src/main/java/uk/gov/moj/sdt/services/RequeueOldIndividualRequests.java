package uk.gov.moj.sdt.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.utils.mbeans.api.ISdtManagementMBean;

@Service
public class RequeueOldIndividualRequests {

    private static Logger LOGGER = LoggerFactory.getLogger(RequeueOldIndividualRequests.class);

    private ISdtManagementMBean sdtManagementMBean;

    @Value("${sdt.requeue.minimumAge:15}")
    private int minimumAge;

    @Autowired
    public RequeueOldIndividualRequests(ISdtManagementMBean sdtManagementMBean) {
        this.sdtManagementMBean = sdtManagementMBean;
    }

    @Scheduled(cron = "${sdt.requeue.cron}")
    public void requeueOldIndividualRequests() {
        LOGGER.debug("EXECUTING: the Scheduled Job to requeue old individual requests");
        sdtManagementMBean.requeueOldIndividualRequests(minimumAge);

        LOGGER.debug("COMPLETED: the Scheduled Job to requeue old individual requests");
    }

    public void setMinimumAge(int minimumAge) {
        this.minimumAge = minimumAge;
    }

}
