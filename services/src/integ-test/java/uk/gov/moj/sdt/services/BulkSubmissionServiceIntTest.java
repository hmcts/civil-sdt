/* Copyrights and Licenses
 * 
 * Copyright (c) 2012-2013 by the Ministry of Justice. All rights reserved.
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
package uk.gov.moj.sdt.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.ServiceRouting;
import uk.gov.moj.sdt.domain.ServiceType;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.IServiceRouting;
import uk.gov.moj.sdt.domain.api.IServiceType;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.services.api.IBulkSubmissionService;
import uk.gov.moj.sdt.test.util.DBUnitUtility;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.Utilities;

/**
 * Implementation of the integration test for BulkSubmissionService.
 * 
 * @author Manoj kulkarni
 * 
 */
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (locations = {"classpath*:**/applicationContext.xml", "/uk/gov/moj/sdt/dao/spring.context.xml",
        "classpath*:/**/spring*.xml", "/uk/gov/moj/sdt/dao/spring*.xml"})
@TransactionConfiguration (defaultRollback = true)
@Transactional
public class BulkSubmissionServiceIntTest extends AbstractJUnit4SpringContextTests
{
    /**
     * Logger object.
     */
    private static final Logger LOG = LoggerFactory.getLogger (BulkSubmissionServiceIntTest.class);

    /**
     * Setup the test.
     */
    @Before
    public void setUp ()
    {
        LOG.debug ("Before SetUp");
        DBUnitUtility.loadDatabase (this.getClass (), true);
        LOG.debug ("After SetUp");
    }

    /**
     * This method tests for persistence of a single submission.
     * 
     * @throws IOException if there is any error reading from the test file.
     */
    @Test
    @Transactional
    @Rollback (true)
    public void saveSingleSubmission () throws IOException
    {
        final String rawXml = this.getRawXml ("testXMLValid2.xml");
        SdtContext.getContext ().setRawInXml (rawXml);

        final IBulkSubmissionService bulkSubmissionService =
                (IBulkSubmissionService) this.applicationContext
                        .getBean ("uk.gov.moj.sdt.services.api.IBulkSubmission");

        final IBulkSubmission bulkSubmission = this.createBulkSubmission ();

        // Call the bulk submission service
        bulkSubmissionService.saveBulkSubmission (bulkSubmission);

        Assert.assertTrue ("Submission saved successfully.", true);

        Assert.assertNotNull (bulkSubmission.getPayload ());

        Assert.assertEquals (bulkSubmission.getNumberOfRequest (), 1L);

        final List<IIndividualRequest> individualRequests = bulkSubmission.getIndividualRequests ();
        Assert.assertNotNull (individualRequests);
        Assert.assertEquals (individualRequests.size (), 1);
        for (IIndividualRequest request : individualRequests)
        {
            Assert.assertNotNull (request.getRequestPayload ());
            LOG.debug ("Payload for request " + request.getId () + "is " + request.getRequestPayload ());
        }

    }

    /**
     * This method tests for persistence of a submission containing multiple individual requests.
     * 
     * @throws IOException if there is any error reading from the test file.
     */
    @Test
    public void saveMultipleSubmissions () throws IOException
    {
        final String rawXml = this.getRawXml ("testXMLValid3.xml");
        SdtContext.getContext ().setRawInXml (rawXml);

        final IBulkSubmissionService bulkSubmissionService =
                (IBulkSubmissionService) this.applicationContext
                        .getBean ("uk.gov.moj.sdt.services.api.IBulkSubmission");
    }

    /**
     * 
     * @return Bulk Submission object for the testing.
     */
    private IBulkSubmission createBulkSubmission ()
    {
        final IBulkSubmission bulkSubmission = new BulkSubmission ();
        final IBulkCustomer bulkCustomer = new BulkCustomer ();
        final ITargetApplication targetApp = new TargetApplication ();

        targetApp.setId (10713L);
        targetApp.setTargetApplicationCode ("MCOL");
        targetApp.setTargetApplicationName ("MCOL");
        final Set<IServiceRouting> serviceRoutings = new HashSet<IServiceRouting> ();

        final IServiceRouting serviceRouting = new ServiceRouting ();
        serviceRouting.setId (1L);
        serviceRouting.setWebServiceEndpoint ("MCOL_END_POINT");

        final IServiceType serviceType = new ServiceType ();
        serviceType.setId (1L);
        serviceType.setName ("RequestTest1");
        serviceType.setDescription ("RequestTestDesc1");
        serviceType.setStatus ("RequestTestStatus");

        serviceRouting.setServiceType (serviceType);

        serviceRoutings.add (serviceRouting);

        targetApp.setServiceRoutings (serviceRoutings);

        bulkSubmission.setTargetApplication (targetApp);

        bulkCustomer.setSdtCustomerId (2L);

        bulkSubmission.setBulkCustomer (bulkCustomer);

        bulkSubmission
                .setCompletedDate (LocalDateTime.fromDateFields (new java.util.Date (System.currentTimeMillis ())));
        bulkSubmission.setCreatedDate (LocalDateTime.fromDateFields (new java.util.Date (System.currentTimeMillis ())));
        bulkSubmission.setCustomerReference ("10711");
        bulkSubmission.setNumberOfRequest (1);
        bulkSubmission.setSdtBulkReference ("SDT_BULKREF_0001");
        bulkSubmission.setSubmissionStatus ("SUBMITTED");
        bulkSubmission.setUpdatedDate (LocalDateTime.fromDateFields (new java.util.Date (System.currentTimeMillis ())));

        final List<IIndividualRequest> individualRequests = new ArrayList<IIndividualRequest> ();
        final IndividualRequest individualRequest = new IndividualRequest ();
        individualRequest.setCompletedDate (LocalDateTime.fromDateFields (new java.util.Date (System
                .currentTimeMillis ())));
        individualRequest
                .setCreatedDate (LocalDateTime.fromDateFields (new java.util.Date (System.currentTimeMillis ())));
        individualRequest.setCustomerRequestReference ("ICustReq123");
        individualRequest.setSdtRequestReference ("SDT_test_1");
        individualRequest.setSdtBulkReference ("SDT_BULKREF_0001");
        // individualRequest.setPayload ("IXML1");
        individualRequest.setRequestStatus ("Received");
        individualRequest.setBulkSubmission (bulkSubmission);
        individualRequests.add (individualRequest);

        bulkSubmission.setIndividualRequests (individualRequests);

        return bulkSubmission;
    }

    /**
     * 
     * @param sdtReference the unique sdt reference number
     * @param customerReference the customer reference number
     * @return a valid individual request object
     */
    private IndividualRequest getValidIndividualRequest (final String sdtReference, final String customerReference)
    {
        final IndividualRequest individualRequest = new IndividualRequest ();
        individualRequest.setCompletedDate (LocalDateTime.fromDateFields (new java.util.Date (System
                .currentTimeMillis ())));
        individualRequest
                .setCreatedDate (LocalDateTime.fromDateFields (new java.util.Date (System.currentTimeMillis ())));
        individualRequest.setCustomerRequestReference (customerReference);
        individualRequest.setSdtRequestReference (sdtReference);
        individualRequest.setRequestStatus ("Received");

        return individualRequest;
    }

    /**
     * 
     * @return rax xml from a test file
     * @param fileName the name of the file to load
     * @throws IOException during the read operations
     */
    private String getRawXml (final String fileName) throws IOException
    {
        // Read the test xml file.
        File myFile;
        String message = "";

        // XPathHandler xmlHandler = new XPathHandler ();

        myFile = new File (Utilities.checkFileExists ("src/integ-test/resources/", fileName, false));

        message = FileUtils.readFileToString (myFile);

        return message;

    }

}
