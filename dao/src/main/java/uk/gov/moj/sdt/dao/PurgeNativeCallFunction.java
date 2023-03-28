package uk.gov.moj.sdt.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@Transactional
public class PurgeNativeCallFunction {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PurgeNativeCallFunction.class);

    public PurgeNativeCallFunction() {}

    @Transactional
    public void executePurgeStoredProc(Integer commitInterval) {

        LOGGER.debug("STARTING: the Purge stored procedure with commit interval [{}]", commitInterval);

        /* Test Stored Procedure */
        LOGGER.debug("Creating Store Procedure...");
        entityManager.joinTransaction();
        entityManager.createNativeQuery("call sdt_owner.purge(?1)")
                .setParameter(1, commitInterval)
                .executeUpdate();

        LOGGER.debug("COMPLETING: the Purge stored procedure.");
    }

}
