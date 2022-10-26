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

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.moj.sdt.dao.api.IBulkSubmissionDao;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.test.utils.AbstractIntegrationTest;
import uk.gov.moj.sdt.test.utils.DBUnitUtility;

/**
 * Test class for the Bulk Submissions Dao.
 *
 * @author Manoj Kulkarni
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/uk/gov/moj/sdt/dao/**/spring*.xml",
        "classpath*:/uk/gov/moj/sdt/domain/**/spring*.xml", "classpath*:/uk/gov/moj/sdt/utils/**/spring*.xml"})
public class BulkSubmissionDaoTest extends AbstractIntegrationTest {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BulkSubmissionDaoTest.class);

    /**
     * Bulk Submission DAO.
     */
    private IBulkSubmissionDao bulkSubmissionDao;

    /**
     * Bulk Customer to use for the test.
     */
    private IBulkCustomer bulkCustomer;

    /**
     * Target Application to use.
     */
    private ITargetApplication targetApplication;

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
        DBUnitUtility.loadDatabase(this.getClass(), true);

        bulkSubmissionDao =
                (IBulkSubmissionDao) this.applicationContext.getBean("uk.gov.moj.sdt.dao.api.IBulkSubmissionDao");
        bulkCustomer = bulkSubmissionDao.fetch(IBulkCustomer.class, 10711);
        targetApplication = bulkSubmissionDao.fetch(ITargetApplication.class, 10713L);
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

        Assert.assertEquals(submission.getPayload()), xmlToLoad);
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
        final IBulkSubmission submission =
                bulkSubmissionDao.getBulkSubmissionBySdtRef(bulkCustomer, sbr, dataRetentionPeriod);

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
        Date d = new Date();
        d = DateUtils.addDays(d, dataRetentionPeriod * -1);

        createBulkSubmission(customerReference, LocalDateTime.fromDateFields(d), sdtBulkReference);
        final IBulkSubmission bulkSubmission =
                bulkSubmissionDao.getBulkSubmission(bulkCustomer, customerReference, dataRetentionPeriod);
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
        Date d = new Date();
        d = DateUtils.addDays(d, (dataRetentionPeriod + 1) * -1);

        createBulkSubmission(customerReference, LocalDateTime.fromDateFields(d), sdtBulkReference);
        final IBulkSubmission bulkSubmission =
                bulkSubmissionDao.getBulkSubmission(bulkCustomer, customerReference, dataRetentionPeriod);
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
        final IBulkSubmission bulkSubmission = new BulkSubmission();

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
