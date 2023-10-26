package uk.gov.moj.sdt.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.services.mbeans.SdtManagementMBean;

@Service
public class RequeueOldIndividualRequests {

    private static Logger LOGGER = LoggerFactory.getLogger(RequeueOldIndividualRequests.class);

    private SdtManagementMBean sdtManagementMBean;

    public RequeueOldIndividualRequests(SdtManagementMBean sdtManagementMBean) {
        this.sdtManagementMBean = sdtManagementMBean;
    }

    @Scheduled(cron = "${sdt.requeue.cron}")
    public void requeueOldIndividualRequests() {
        LOGGER.debug("EXECUTING: the Scheduled Job to requeue old individual requests");
        final int minimumAgeInMinutes = 15;
        sdtManagementMBean.requeueOldIndividualRequests(minimumAgeInMinutes);

        LOGGER.debug("COMPLETED: the Scheduled Job to requeue old individual requests");
    }

}
