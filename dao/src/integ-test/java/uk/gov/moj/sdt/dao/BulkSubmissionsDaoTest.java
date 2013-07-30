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

import static org.junit.Assert.assertEquals;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.test.util.DBUnitUtility;

/**
 * Test class for the Bulk Submissions Dao.
 * 
 * @author Manoj Kulkarni
 * 
 */
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (locations = {"classpath*:**/applicationContext.xml", "classpath*:**/spring*.xml"})
public class BulkSubmissionsDaoTest extends AbstractTransactionalJUnit4SpringContextTests
{
    /**
     * Logger object.
     */
    private static final Logger LOG = LoggerFactory.getLogger (BulkSubmissionsDaoTest.class);

    /**
     * Default constructor.
     */
    public BulkSubmissionsDaoTest ()
    {
        super ();
        DBUnitUtility.loadDatabase (this.getClass (), false);
    }

    /**
     * This method tests that record can be successfully inserted into the Bulk Submission.
     */
    @Test
    public void testInsert ()
    {
        final IGenericDao genericDao =
                (IGenericDao) this.applicationContext.getBean ("uk.gov.moj.sdt.dao.api.IGenericDao");

        final IBulkSubmission bulkSubmission = new BulkSubmission ();

        final IBulkCustomer bulkCustomer = genericDao.fetch (BulkCustomer.class, 10711);

        LOG.debug ("Bulk Customer is " + bulkCustomer);

        bulkSubmission.setBulkCustomer (bulkCustomer);

        LOG.debug ("Bulk Customer's target applications are " + bulkCustomer.getTargetApplications ());

        final ITargetApplication targetApp = genericDao.fetch (TargetApplication.class, 10713L);

        bulkSubmission.setTargetApplication (targetApp);

        bulkSubmission.setCreatedDate (LocalDateTime.now ());
        bulkSubmission.setCustomerReference ("REF1");
        bulkSubmission.setNumberOfRequest (1);
        final String xmlToLoad = "<Payload>2</Payload>";
        bulkSubmission.setPayload (xmlToLoad);
        bulkSubmission.setSdtBulkReference ("MCOL-10012013010101-100000009");
        bulkSubmission.setSubmissionStatus ("PENDING");

        genericDao.persist (bulkSubmission);

        LOG.debug ("Persisted successfully");

        final Criterion criterion = Restrictions.eq ("sdtBulkReference", "MCOL-10012013010101-100000009");
        final IBulkSubmission[] submissions = genericDao.query (BulkSubmission.class, criterion);

        LOG.debug ("submissions length is " + submissions.length);

        assertEquals (submissions.length, 1);

        LOG.debug ("payload for bulk submission is " + new String (submissions[0].getPayload ()));

        assertEquals (new String (submissions[0].getPayload ()), xmlToLoad);

        assertEquals (submissions[0].getCustomerReference (), "REF1");
    }
}
