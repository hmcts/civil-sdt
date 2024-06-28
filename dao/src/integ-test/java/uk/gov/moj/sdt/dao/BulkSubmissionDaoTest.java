/* Copyrights and Licenses
 *
 * Copyright (c) 2013 by the Ministry of Justice. All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 * - All advertising materials mentioning features or use of this software must display the
 * following acknowledgment: "This product includes Money Claims OnLine."
 * - Products derived from this software may not be called "Money Claims OnLine" nor may
 * "Money Claims OnLine" appear in their names without prior written permission of the
 * Ministry of Justice.
 * - Redistributions of any form whatsoever must retain the following acknowledgment: "This
 * product includes Money Claims OnLine."
 * This software is provided "as is" and any expressed or implied warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the Ministry of Justice or its contributors be liable for any
 * direct, indirect, incidental, special, exemplary, or consequential damages (including, but
 * not limited to, procurement of substitute goods or services; loss of use, data, or profits;
 * or business interruption). However caused any on any theory of liability, whether in contract,
 * strict liability, or tort (including negligence or otherwise) arising in any way out of the use of this
 * software, even if advised of the possibility of such damage.
 *
 * $Id: $
 * $LastChangedRevision: $
 * $LastChangedDate: $
 * $LastChangedBy: $ */
package uk.gov.moj.sdt.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.dao.api.IBulkSubmissionDao;
import uk.gov.moj.sdt.dao.api.ITargetApplicationDao;
import uk.gov.moj.sdt.dao.config.DaoTestConfig;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.test.utils.AbstractIntegrationTest;
import uk.gov.moj.sdt.test.utils.TestConfig;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test class for the Bulk Submissions Dao.
 *
 * @author Manoj Kulkarni
 */
@ActiveProfiles("integ")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestConfig.class, DaoTestConfig.class})
@Sql(scripts = {"classpath:uk/gov/moj/sdt/dao/sql/BulkSubmissionDaoTest.sql"})
@Transactional
class BulkSubmissionDaoTest extends AbstractIntegrationTest {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BulkSubmissionDaoTest.class);

    private static final long BULK_CUSTOMER_ID = 10711L;
    private static final long TARGET_APPLICATION_ID = 10713L;
    private static final int DATA_RETENTION_PERIOD = 90;

    private static final String BULK_SUBMISSION_NULL = "Bulk submission should not be null";
    private static final String UNEXPECTED_CUSTOMER_REF = "Bulk submission has unexpected customer ref";

    /**
     * Bulk Submission DAO.
     */
    private final IBulkSubmissionDao bulkSubmissionDao;

    private final IBulkCustomerDao bulkCustomerDao;

    private final ITargetApplicationDao targetApplicationDao;

    /**
     * Bulk Customer to use for the test.
     */
    private BulkCustomer bulkCustomer;

    /**
     * Target Application to use.
     */
    private TargetApplication targetApplication;

    @Autowired
    public BulkSubmissionDaoTest(IBulkSubmissionDao bulkSubmissionDao,
                                 IBulkCustomerDao bulkCustomerDao,
                                 ITargetApplicationDao targetApplicationDao) {
        this.bulkSubmissionDao = bulkSubmissionDao;
        this.bulkCustomerDao = bulkCustomerDao;
        this.targetApplicationDao = targetApplicationDao;
    }

    /**
     * Setup the test.
     */
    @BeforeEach
    public void setUp() {
        bulkCustomer = bulkCustomerDao.fetch(BulkCustomer.class, BULK_CUSTOMER_ID);
        targetApplication = targetApplicationDao.fetch(TargetApplication.class, TARGET_APPLICATION_ID);
    }

    /**
     * This method tests that record can be successfully inserted into the Bulk Submission.
     */
    @Test
    void testInsert() {
        String sdtBulkRef = "MCOL-20240627120000-000000001";
        String customerRef = "REF1";

        createBulkSubmission(customerRef, LocalDateTime.now(), sdtBulkRef);

        final IBulkSubmission bulkSubmission =
            bulkSubmissionDao.getBulkSubmissionBySdtRef(bulkCustomer, sdtBulkRef, DATA_RETENTION_PERIOD);

        assertNotNull(bulkSubmission, BULK_SUBMISSION_NULL);

        assertEquals(sdtBulkRef,
                     bulkSubmission.getSdtBulkReference(),
                     "Bulk submission has unexpected SDT bulk reference");

        String response = new String(bulkSubmission.getPayload(), StandardCharsets.UTF_8);
        LOGGER.debug("payload for bulk submission is {}", response);
        assertEquals(response, "<Payload>2</Payload>", "Bulk submission has unexpected payload");

        assertEquals(customerRef, bulkSubmission.getCustomerReference(), UNEXPECTED_CUSTOMER_REF);
    }

    /**
     * Test get bulk submission by SDT bulk reference.
     */
    @Test
    void testGetBulkSubmissionBySdtBulkRef() {
        final String sdtBulkRef = "MCOL-10012013010101-100099999";

        final IBulkSubmission bulkSubmission =
            bulkSubmissionDao.getBulkSubmissionBySdtRef(bulkCustomer, sdtBulkRef, DATA_RETENTION_PERIOD);

        assertNotNull(bulkSubmission, BULK_SUBMISSION_NULL);
        assertEquals(bulkSubmission.getSdtBulkReference(),
                     sdtBulkRef,
                     "Bulk submission has unexpected SDT bulk reference");

        List<IIndividualRequest> individualRequests = bulkSubmission.getIndividualRequests();

        assertNotNull(individualRequests, "Individual requests should not be null");
        assertEquals(4, individualRequests.size(), "Unexpected number of individual requests");

        int expectedLineNumber = 1;
        for (IIndividualRequest individualRequest : individualRequests) {
            assertEquals(expectedLineNumber,
                         individualRequest.getLineNumber().intValue(),
                         "Individual requests in unexpected order.  Expected line number " + expectedLineNumber);
            assertEquals(sdtBulkRef + String.format("-%07d", expectedLineNumber),
                         individualRequest.getSdtRequestReference(),
                         "Individual request has unexpected SDT request reference");
            expectedLineNumber++;
        }
    }

    /**
     * Test get bulk submission by SDT bulk reference not founds.
     */
    @Test
    void testGetBulkSubmissionBySdtBulkRefNotFound() {
        final String sbr = "NO_SUCH_ID";

        final IBulkSubmission bulkSubmission =
            bulkSubmissionDao.getBulkSubmissionBySdtRef(bulkCustomer, sbr, DATA_RETENTION_PERIOD);

        assertNull(bulkSubmission, "Bulk submission should be null");
    }

    /**
     * Test the upper limit of the data retention period, i.e. today
     */
    @Test
    void testGetBulkSubmissionUpper() {
        final String customerReference = "customer reference 1";
        createBulkSubmission(customerReference, LocalDateTime.now(), "MCOL-20240627120000-000000002");

        final IBulkSubmission bulkSubmission =
            bulkSubmissionDao.getBulkSubmission(bulkCustomer, customerReference, DATA_RETENTION_PERIOD);

        assertNotNull(bulkSubmission, BULK_SUBMISSION_NULL);
        assertEquals(bulkSubmission.getCustomerReference(), customerReference, UNEXPECTED_CUSTOMER_REF);
    }

    /**
     * Test the upper limit of the data retention period, i.e. 90 days ago.
     */
    @Test
    void testGetBulkSubmissionLower() {
        final String customerReference = "customer reference 2";

        // Set the created date to be 90 days ago
        createBulkSubmission(customerReference,
                             LocalDateTime.now().minusDays(DATA_RETENTION_PERIOD),
                             "MCOL-20240627120000-000000003");

        final IBulkSubmission bulkSubmission =
            bulkSubmissionDao.getBulkSubmission(bulkCustomer, customerReference, DATA_RETENTION_PERIOD);

        assertNotNull(bulkSubmission, BULK_SUBMISSION_NULL);
        assertEquals(bulkSubmission.getCustomerReference(), customerReference, UNEXPECTED_CUSTOMER_REF);
    }

    /**
     * Test with a bulk submission past 90 days ago and use an old customer reference.
     */
    @Test
    void testGetBulkSubmissionPastRetention() {
        final String customerReference = "customer reference 3";

        // Set the created date to be 91 days ago
        createBulkSubmission(customerReference,
                             LocalDateTime.now().minusDays(DATA_RETENTION_PERIOD + 1L),
                             "MCOL-20240627120000-000000004");

        final IBulkSubmission bulkSubmission =
            bulkSubmissionDao.getBulkSubmission(bulkCustomer, customerReference, DATA_RETENTION_PERIOD);

        assertNull(bulkSubmission, "Bulk submission should be null");
    }

    /**
     * Create a bulk submission.
     *
     * @param customerReference customer reference
     * @param date              created date
     * @param sbr               sdt bulk reference
     */
    private void createBulkSubmission(final String customerReference, final LocalDateTime date, final String sbr) {
        final BulkSubmission bulkSubmission = new BulkSubmission();

        bulkSubmission.setBulkCustomer(bulkCustomer);
        bulkSubmission.setTargetApplication(targetApplication);

        bulkSubmission.setCreatedDate(date);
        bulkSubmission.setCustomerReference(customerReference);
        bulkSubmission.setNumberOfRequest(1);
        final String xmlToLoad = "<Payload>2</Payload>";
        bulkSubmission.setPayload(xmlToLoad.getBytes());
        bulkSubmission.setSdtBulkReference(sbr);
        bulkSubmission.setSubmissionStatus(IBulkSubmission.BulkRequestStatus.UPLOADED.getStatus());

        bulkSubmissionDao.persist(bulkSubmission);
    }
}
