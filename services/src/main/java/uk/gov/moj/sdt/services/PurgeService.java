package uk.gov.moj.sdt.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.moj.sdt.dao.PurgeNativeCallFunction;

@Transactional
@Service
public class PurgeService {

    private static Logger logger = LoggerFactory.getLogger(PurgeService.class);

    private PurgeNativeCallFunction purgeNativeCallFunction;

    public PurgeService(PurgeNativeCallFunction purgeNativeCallFunction) {
        this.purgeNativeCallFunction = purgeNativeCallFunction;
    }

    public void transactPurgeProc() {
        Integer commitInterval = 1000;
        logger.debug("STARTING: the Purge procedure ({}) under transaction", commitInterval);

        purgeNativeCallFunction.executePurgeStoredProc(commitInterval);

        logger.debug("COMPLETING: the Purge procedure under transaction");
    }

}
