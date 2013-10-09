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

import junit.framework.Assert;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.test.util.DBUnitUtility;

/**
 * Test class for the Individual Request Dao.
 * 
 * @author Son Loi
 * 
 */
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (locations = {"classpath*:**/applicationContext.xml", "classpath*:**/spring*.xml"})
public class IndividualRequestDaoTest extends AbstractTransactionalJUnit4SpringContextTests
{
    /**
     * Logger object.
     */
    private static final Logger LOG = LoggerFactory.getLogger (IndividualRequestDaoTest.class);

    /**
     * *Individual Request DAO.
     */
    private IIndividualRequestDao individualRequestDao;

    /**
     * Bulk Customer to use for the test.
     */
    private IBulkCustomer bulkCustomer;

    /**
     * Bulk Submission to use for the test.
     */
    private IBulkSubmission bulkSubmission;

    /**
     * Data retention period.
     */
    private int dataRetentionPeriod;

    /**
     * Setup the test.
     */
    @Before
    public void setUp ()
    {
        LOG.debug ("Before SetUp");
        DBUnitUtility.loadDatabase (this.getClass (), true);

        individualRequestDao =
                (IIndividualRequestDao) this.applicationContext
                        .getBean ("uk.gov.moj.sdt.dao.api.IIndividualRequestDao");
        bulkSubmission = individualRequestDao.fetch (IBulkSubmission.class, 10710);
        bulkCustomer = individualRequestDao.fetch (BulkCustomer.class, 10711);
        dataRetentionPeriod = 90;

        LOG.debug ("After SetUp");
    }

    /**
     * Test the retrieval of an Individual Request by SDT Request Reference.
     */
    @Test
    public void testGetRequestBySdtReference ()
    {

        final String sdtRequestReference = "SDT_REQ_TEST_1";
        // There should already be a record in the DB loaded as part of the test
        final IIndividualRequest individualRequest =
                individualRequestDao.getRequestBySdtReference (sdtRequestReference);

        Assert.assertNotNull (individualRequest);
        Assert.assertEquals (sdtRequestReference, individualRequest.getSdtRequestReference ());

    }

    /**
     * Test the upper limit of the data retention period, i.e. today
     */
    @Test
    public void testGetIndividualRequestUpper ()
    {
        final String customerRequestReference = "customer request reference 1";

        createIndividualRequest (customerRequestReference, LocalDateTime.now ());

        final IIndividualRequest individualRequest =
                individualRequestDao.getIndividualRequest (bulkCustomer, customerRequestReference, dataRetentionPeriod);
        Assert.assertNotNull (individualRequest);
        Assert.assertEquals (individualRequest.getCustomerRequestReference (), customerRequestReference);

    }

    /**
     * Test the upper limit of the data retention period, i.e. 90 days ago.
     */
    @Test
    public void testGetBulkSubmissionLower ()
    {
        final String customerRequestReference = "customer request reference 2";
        // Set the created date to be 90 days ago
        Date d = new Date ();
        d = DateUtils.addDays (d, dataRetentionPeriod * -1);

        createIndividualRequest (customerRequestReference, LocalDateTime.fromDateFields (d));
        final IIndividualRequest individualRequest =
                individualRequestDao.getIndividualRequest (bulkCustomer, customerRequestReference, dataRetentionPeriod);

        Assert.assertNotNull (individualRequest);
        Assert.assertEquals (individualRequest.getCustomerRequestReference (), customerRequestReference);

    }

    /**
     * Test with an individual request past 90 days ago and use an old customer reference.
     */
    @Test
    public void testGetIndividualRequestPastRetention ()
    {
        final String customerRequestReference = "customer request reference 1";
        // Set the created date to be 91 days ago
        Date d = new Date ();
        d = DateUtils.addDays (d, (dataRetentionPeriod + 1) * -1);

        createIndividualRequest (customerRequestReference, LocalDateTime.fromDateFields (d));

        final IIndividualRequest individualRequest =
                individualRequestDao.getIndividualRequest (bulkCustomer, customerRequestReference, dataRetentionPeriod);

        Assert.assertNull (individualRequest);

    }

    /**
     * Test get individual request not found.
     */
    @Test
    public void testGetBulkSubmissionNotFound ()
    {

        final IIndividualRequest individualRequest =
                individualRequestDao.getIndividualRequest (bulkCustomer, "NOT_FOUND", dataRetentionPeriod);

        Assert.assertNull (individualRequest);

    }

    /**
     * Create a individual request.
     * 
     * @param customerReference customer reference
     * @param date created date
     */
    private void createIndividualRequest (final String customerReference, final LocalDateTime date)
    {
        final IIndividualRequest individualRequest = new IndividualRequest ();
        individualRequest.setBulkSubmission (bulkSubmission);
        individualRequest.setCreatedDate (date);
        individualRequest.setCustomerRequestReference (customerReference);
        individualRequest.setLineNumber (1);
        individualRequest.setRequestStatus (IIndividualRequest.IndividualRequestStatus.ACCEPTED.getStatus ());
        individualRequest.setSdtBulkReference ("sdtBulkReference");
        individualRequest.setSdtRequestReference ("sdtRequestReference");
        individualRequest.setForwardingAttempts (1);
        individualRequest.setTargetApplicationResponse ("targetApplicationResponse");
        individualRequest.setRequestPayload ("requestPayload");
        individualRequest.setRequestType ("mcolClaim");

        individualRequestDao.persist (individualRequest);

        LOG.debug ("Individual Request persisted successfully");

    }

}
