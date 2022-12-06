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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.dao.api.IBulkSubmissionDao;
import uk.gov.moj.sdt.dao.api.ITargetApplicationDao;
import uk.gov.moj.sdt.dao.config.DaoTestConfig;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.test.utils.AbstractIntegrationTest;
import uk.gov.moj.sdt.test.utils.TestConfig;

import java.time.LocalDateTime;

/**
 * Test class for the Bulk Submissions Dao.
 *
 * @author Manoj Kulkarni
 */
@ActiveProfiles("integ")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TestConfig.class, DaoTestConfig.class})
@Sql(scripts = {"classpath:uk/gov/moj/sdt/dao/sql/BulkSubmissionDaoTest.sql"})
public class BulkSubmissionDaoTest extends AbstractIntegrationTest {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BulkSubmissionDaoTest.class);

    /**
     * Bulk Submission DAO.
     */
    private IBulkSubmissionDao bulkSubmissionDao;

    private IBulkCustomerDao bulkCustomerDao;
    private ITargetApplicationDao targetApplicationDao;

    /**
     * Bulk Customer to use for the test.
     */
    private BulkCustomer bulkCustomer;

    /**
     * Target Application to use.
     */
    private TargetApplication targetApplication;

    /**
     * Data retention period.
     */
    private int dataRetentionPeriod;

    /**
     * SDT Bulk Reference.
     */
    private String sdtBulkReference;
        /**
         * Setup the test.
         */
    @Before
    public void setUp() {
        bulkSubmissionDao = this.applicationContext.getBean(IBulkSubmissionDao.class);
        bulkCustomerDao = this.applicationContext.getBean(IBulkCustomerDao.class);
        targetApplicationDao = this.applicationContext.getBean(ITargetApplicationDao.class);
        bulkCustomer = bulkCustomerDao.fetch(BulkCustomer.class, 10711);
        targetApplication = targetApplicationDao.fetch(TargetApplication.class, 10713L);
        dataRetentionPeriod = 90;
        sdtBulkReference = "MCOL-10012013010101-100000009";
    }

    /**
     * This method tests that record can be successfully inserted into the Bulk Submission.
     */
    @Test
    public void testInsert() {
        final IBulkSubmission bulkSubmission = new BulkSubmission();

        bulkSubmission.setBulkCustomer(bulkCustomer);

        bulkSubmission.setTargetApplication(targetApplication);

        bulkSubmission.setCreatedDate(LocalDateTime.now());
        bulkSubmission.setCustomerReference("REF1");
        bulkSubmission.setNumberOfRequest(1);
        final String xmlToLoad = "<Payload>2</Payload>";
        bulkSubmission.setPayload(xmlToLoad);
        bulkSubmission.setSdtBulkReference(sdtBulkReference);
        bulkSubmission.setSubmissionStatus(IBulkSubmission.BulkRequestStatus.UPLOADED.getStatus());

        bulkSubmissionDao.persist(bulkSubmission);

        final IBulkSubmission submission =
                bulkSubmissionDao.getBulkSubmissionBySdtRef(bulkCustomer, sdtBulkReference, dataRetentionPeriod);

        Assert.assertNotNull(submission);

        LOGGER.debug("payload for bulk submission is " + submission.getPayload());

        Assert.assertEquals(submission.getPayload(), xmlToLoad);
        Assert.assertEquals(submission.getCustomerReference(), "REF1");
    }

    /**
     * Test get bulk submission by SDT bulk reference.
     */
    @Test
    public void testGetBulkSubmissionBySdtBulkRef() {
        final String sbr = "MCOL-10012013010101-100099999";

        final IBulkSubmission submission =
                bulkSubmissionDao.getBulkSubmissionBySdtRef(bulkCustomer, sbr, dataRetentionPeriod);

        Assert.assertNotNull(submission);
        Assert.assertEquals(submission.getSdtBulkReference(), sbr);

        Assert.assertEquals("Check individual request count", 1, submission.getIndividualRequests().size());

        for (IIndividualRequest req : submission.getIndividualRequests()) {
            Assert.assertEquals(5, req.getId().longValue());
        }

    }

    /**
     * Test get bulk submission by SDT bulk reference not founds.
     */
    @Test
    public void testGetBulkSubmissionBySdtBulkRefNotFound() {
        final String sbr = "NO_SUCH_ID";
        IBulkSubmission submission = null;
        try {
            submission = bulkSubmissionDao.getBulkSubmissionBySdtRef(bulkCustomer, sbr, dataRetentionPeriod);
        } catch (Exception e) {
        }
        Assert.assertNull(submission);

    }

    /**
     * Test the upper limit of the data retention period, i.e. today
     */
    @Test
    public void testGetBulkSubmissionUpper() {
        final String customerReference = "customer reference 1";
        createBulkSubmission(customerReference, LocalDateTime.now(), sdtBulkReference);
        final IBulkSubmission bulkSubmission =
                bulkSubmissionDao.getBulkSubmission(bulkCustomer, customerReference, dataRetentionPeriod);
        Assert.assertNotNull(bulkSubmission);
        Assert.assertEquals(bulkSubmission.getCustomerReference(), customerReference);

    }

    /**
     * Test the upper limit of the data retention period, i.e. 90 days ago.
     */
    @Test
    public void testGetBulkSubmissionLower() {
        final String customerReference = "customer reference 2";
        // Set the created date to be 90 days ago

        createBulkSubmission(customerReference, LocalDateTime.now().minusDays(dataRetentionPeriod), sdtBulkReference);
        final IBulkSubmission bulkSubmission =
                bulkSubmissionDao.getBulkSubmission(bulkCustomer, customerReference, dataRetentionPeriod + 1);
        Assert.assertNotNull(bulkSubmission);
        Assert.assertEquals(bulkSubmission.getCustomerReference(), customerReference);

    }

    /**
     * Test with a bulk submission past 90 days ago and use an old customer reference.
     */
    @Test
    public void testGetBulkSubmissionPastRetention() {
        final String customerReference = "customer reference 1";
        // Set the created date to be 91 days ago

        createBulkSubmission(customerReference,
                             LocalDateTime.now().minusDays(dataRetentionPeriod + 1),
                             sdtBulkReference);
        IBulkSubmission bulkSubmission = null;
        try {
            bulkSubmission =
                bulkSubmissionDao.getBulkSubmission(bulkCustomer, customerReference, dataRetentionPeriod);
        } catch (Exception e) {
        }
        Assert.assertNull(bulkSubmission);
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
        bulkSubmission.setPayload(xmlToLoad);
        bulkSubmission.setSdtBulkReference(sbr);
        bulkSubmission.setSubmissionStatus(IBulkSubmission.BulkRequestStatus.UPLOADED.getStatus());

        bulkSubmissionDao.persist(bulkSubmission);
    }
}
