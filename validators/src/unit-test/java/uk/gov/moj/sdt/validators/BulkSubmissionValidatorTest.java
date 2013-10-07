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
 * $Id: SubmitQueryEnricherTest.java 17032 2013-09-12 15:25:50Z agarwals $
 * $LastChangedRevision: 17032 $
 * $LastChangedDate: 2013-09-12 16:25:50 +0100 (Thu, 12 Sep 2013) $
 * $LastChangedBy: agarwals $ */

package uk.gov.moj.sdt.validators;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.dao.api.IBulkSubmissionDao;
import uk.gov.moj.sdt.dao.api.ITargetApplicationDao;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.GlobalParameter;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.utils.SdtUnitTestBase;
import uk.gov.moj.sdt.validators.exception.CustomerNotSetupException;
import uk.gov.moj.sdt.validators.exception.CustomerReferenceNotUniqueException;
import uk.gov.moj.sdt.validators.exception.RequestCountMismatchException;

/**
 * Tests for {@link BulkSubmissionValidatorTest}.
 * 
 * 
 * 
 * @author d120520
 * 
 */
public class BulkSubmissionValidatorTest extends SdtUnitTestBase
{
    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog (BulkSubmissionValidatorTest.class);

    /**
     * Subject for test.
     */
    private BulkSubmissionValidator validator;

    /**
     * IBulkSubmission dao.
     */
    private IBulkSubmissionDao mockIBulkSubmissionDao;

    /**
     * The bulk customer dao.
     */

    private IBulkCustomerDao mockIBulkCustomerDao;

    /**
     * The DAO.
     */

    private ITargetApplicationDao mockITargetApplicationDao;

    /**
     * bulk submission.
     */

    private BulkSubmission bulkSubmission;

    /**
     * The bulk customer.
     */
    private IBulkCustomer bulkCustomer;

    /**
     * The customer reference number.
     */
    private String customerReferenceId = " customerReference ";

    /**
     * Customer reference.
     */

    private String customerReference;

    /**
     * The bulk customer id number.
     */
    private Long sdtCustomerId;

    /**
     * IBulkSubmission dao.
     */

    private Set<IBulkCustomer> bulkCustomers;

    private Long dataRetention = 12345L;

    private ICacheable globalParameterCache;

    private IGlobalParameter globalParameter;

    private List<IIndividualRequest> individualRequests;

    final long numberOfRequests = 1L;

    final long requestId = 20L;

    /**
     * Constructor for test.
     * 
     * @param testName name of this test class.
     */
    public BulkSubmissionValidatorTest (final String testName)
    {
        super (testName);
    }

    /**
     * Setup of the Validator and Domain class instance.
     */
    @Before
    public void setUp ()
    {

        // subject of test
        validator = new BulkSubmissionValidator ();

        // mock BulkCustomer object
        mockIBulkCustomerDao = EasyMock.createMock (IBulkCustomerDao.class);

    }

    private void createBulkSubmission (final long numberOfRequests, final IBulkCustomer bulkCustomer,
                                       final List individualRequests, String application)
    {

        // setup a bulk submission and put a domain object in there (bulk customer)
        bulkSubmission = new BulkSubmission ();
        bulkSubmission.setBulkCustomer (bulkCustomer);
        bulkSubmission.setCustomerReference ("Customer Reference");
        bulkSubmission.setTargetApplication (createTargetApp (application));
        bulkSubmission.setNumberOfRequest (numberOfRequests);
        bulkSubmission.setIndividualRequests (individualRequests);
    }

    private IBulkCustomer createCustomer (final Set<ITargetApplication> applicationSet)
    {
        final IBulkCustomer bulkCustomer = new BulkCustomer ();
        bulkCustomer.setSdtCustomerId (12345L);
        bulkCustomer.setTargetApplications (applicationSet);

        return bulkCustomer;
    }

    // create me an application with a given name
    private ITargetApplication createTargetApp (String targetApplicationCode)
    {
        ITargetApplication application = new TargetApplication ();
        application.setTargetApplicationCode (targetApplicationCode);
        return application;
    }

    // the list of applications for a customer
    private Set<ITargetApplication> addAppToCustomerApplications (String applicationName)
    {
        Set<ITargetApplication> targetApplications = new HashSet<ITargetApplication> ();
        targetApplications.add (createTargetApp (applicationName));
        return targetApplications;
    }

    /**
     * The purpose of this test is to have a clean run through these three conditions
     * 1) The customer has access to the target application
     * 2) No invalid bulk submission
     * 3) The individual requests matches the bulk submission requests
     * 
     * 
     * Test conditions used that will pass this test are:
     * create a bulk submission with one request, one customer, one individual requests and using the MCOL app
     */
    @Test
    public void testAllSuccess ()
    {

        // set up a bulk customer to use the MCOL application
        bulkCustomer = createCustomer (addAppToCustomerApplications ("MCOL"));

        // create an individual request
        final IIndividualRequest individualRequest = new IndividualRequest ();
        individualRequest.setId (requestId);
        individualRequests = new ArrayList<IIndividualRequest> ();
        individualRequests.add (individualRequest);

        createBulkSubmission (numberOfRequests, bulkCustomer, individualRequests, "MCOL");

        // set up the mock objects
        expect (mockIBulkCustomerDao.getBulkCustomerBySdtId (12345L)).andReturn (bulkCustomer);
        replay (mockIBulkCustomerDao);

        // inject the bulk customer into the validator
        validator.setBulkCustomerDao (mockIBulkCustomerDao);

        // create a mock Bulk Submission DAO and return true
        mockIBulkSubmissionDao = EasyMock.createMock (IBulkSubmissionDao.class);

        expect (mockIBulkSubmissionDao.getBulkSubmission (bulkCustomer, bulkSubmission.getCustomerReference (), 30))
                .andReturn (null);
        replay (mockIBulkSubmissionDao);

        validator.setBulkSubmissionDao (mockIBulkSubmissionDao);

        globalParameter = new GlobalParameter ();
        globalParameter.setName ("DATA_RETENTION_PERIOD");
        globalParameter.setValue ("30");
        globalParameterCache = EasyMock.createMock (ICacheable.class);
        expect (globalParameterCache.getValue (IGlobalParameter.class, "DATA_RETENTION_PERIOD")).andReturn (
                globalParameter);
        replay (globalParameterCache);

        validator.setGlobalParameterCache (globalParameterCache);
        bulkSubmission.accept (validator, null);
    }

    /**
     * 
     * This test will fail on the check customer has access to MCOL application and catch the exception
     */

    @Test
    public void testCustomerDoesNotHaveAccess ()
    {
        try
        {

            // set up the data we are going to use for this customer
            // set up bulk customer with the application it can use

            // set up a bulk customer to use the PCOL application to make it error as the bulk submission sets MCOL.
            bulkCustomer = createCustomer (addAppToCustomerApplications ("PCOL"));

            // set up the mock objects
            expect (mockIBulkCustomerDao.getBulkCustomerBySdtId (12345L)).andReturn (bulkCustomer);
            replay (mockIBulkCustomerDao);

            // inject the bulk customer into the validator
            validator.setBulkCustomerDao (mockIBulkCustomerDao);

            // create an individual request
            final IIndividualRequest individualRequest = new IndividualRequest ();
            individualRequest.setId (requestId);
            individualRequests = new ArrayList<IIndividualRequest> ();
            individualRequests.add (individualRequest);

            createBulkSubmission (numberOfRequests, bulkCustomer, individualRequests, "MCOL");
            bulkSubmission.accept (validator, null);
            Assert.fail ("Test failed to throw CustomerNotSetupException ");

        }
        catch (final CustomerNotSetupException e)
        {
            LOGGER.debug (e.getMessage ().toString ());
            EasyMock.verify (mockIBulkCustomerDao);

            Assert.assertTrue ("Error code incorrect", e.getMessage ().contains ("CUST_NOT_SETUP"));
            Assert.assertTrue ("Substitution value incorrect", e.getMessage ().contains (
                    "Bulk Customer organisation is not " + "set up to send Service Request messages to the MCOL"));
        }

    }

    /**
     * 
     * This test will contain an invalid bulk submission and catch the exception.
     */

    @Test
    public void testInvalidBulkSubmission ()
    {

        try
        {

            // set up the data we are going to use for this customer
            // set up bulk customer with the application it can use

            // set up a bulk customer to use the PCOL application to make it error as the bulk submission sets MCOL.
            bulkCustomer = createCustomer (addAppToCustomerApplications ("MCOL"));

            // set up the mock objects
            expect (mockIBulkCustomerDao.getBulkCustomerBySdtId (12345L)).andReturn (bulkCustomer);
            replay (mockIBulkCustomerDao);

            // inject the bulk customer into the validator
            validator.setBulkCustomerDao (mockIBulkCustomerDao);

            // create an individual request
            final IIndividualRequest individualRequest = new IndividualRequest ();
            individualRequest.setId (requestId);
            individualRequests = new ArrayList<IIndividualRequest> ();
            individualRequests.add (individualRequest);

            createBulkSubmission (numberOfRequests, bulkCustomer, individualRequests, "MCOL");

            // create a mock Bulk Submission DAO and return true
            mockIBulkSubmissionDao = EasyMock.createMock (IBulkSubmissionDao.class);

            expect (mockIBulkSubmissionDao.getBulkSubmission (bulkCustomer, bulkSubmission.getCustomerReference (), 30))
                    .andReturn (bulkSubmission);
            replay (mockIBulkSubmissionDao);

            validator.setBulkSubmissionDao (mockIBulkSubmissionDao);

            globalParameter = new GlobalParameter ();
            globalParameter.setName ("DATA_RETENTION_PERIOD");
            globalParameter.setValue ("30");
            globalParameterCache = EasyMock.createMock (ICacheable.class);
            expect (globalParameterCache.getValue (IGlobalParameter.class, "DATA_RETENTION_PERIOD")).andReturn (
                    globalParameter);
            replay (globalParameterCache);

            validator.setGlobalParameterCache (globalParameterCache);

            bulkSubmission.accept (validator, null);
            Assert.fail ("Test failed to throw CustomerReferenceNotUniqueException ");

        }
        catch (final CustomerReferenceNotUniqueException e)
        {
            LOGGER.debug (e.getMessage ().toString ());

            Assert.assertTrue ("Error code incorrect", e.getMessage ().contains ("DUP_CUST_FILEID"));
            Assert.assertTrue ("Substitution value incorrect",
                    e.getMessage ().contains ("Duplicate User File Reference Customer Reference supplied"));
        }

    }

    /**
     * 
     * This test will have an incorrect number of requests and catch the exception.
     */

    @Test
    public void testRequestCountdoesNotMatch ()
    {

        try
        {

            // set up a bulk customer to use the MCOL application
            bulkCustomer = createCustomer (addAppToCustomerApplications ("MCOL"));

            // create an individual request
            final IIndividualRequest individualRequest = new IndividualRequest ();
            individualRequest.setId (requestId);
            individualRequests = new ArrayList<IIndividualRequest> ();
            individualRequests.add (individualRequest);

            createBulkSubmission (15L, bulkCustomer, individualRequests, "MCOL");

            // set up the mock objects
            expect (mockIBulkCustomerDao.getBulkCustomerBySdtId (12345L)).andReturn (bulkCustomer);
            replay (mockIBulkCustomerDao);

            // inject the bulk customer into the validator
            validator.setBulkCustomerDao (mockIBulkCustomerDao);

            // create a mock Bulk Submission DAO and return true
            mockIBulkSubmissionDao = EasyMock.createMock (IBulkSubmissionDao.class);

            expect (mockIBulkSubmissionDao.getBulkSubmission (bulkCustomer, bulkSubmission.getCustomerReference (), 30))
                    .andReturn (null);
            replay (mockIBulkSubmissionDao);

            validator.setBulkSubmissionDao (mockIBulkSubmissionDao);

            globalParameter = new GlobalParameter ();
            globalParameter.setName ("DATA_RETENTION_PERIOD");
            globalParameter.setValue ("30");
            globalParameterCache = EasyMock.createMock (ICacheable.class);
            expect (globalParameterCache.getValue (IGlobalParameter.class, "DATA_RETENTION_PERIOD")).andReturn (
                    globalParameter);
            replay (globalParameterCache);

            validator.setGlobalParameterCache (globalParameterCache);
            bulkSubmission.accept (validator, null);

            Assert.fail ("Test failed to throw RequestCountMismatchException ");
        }
        catch (final RequestCountMismatchException e)
        {
            LOGGER.debug (e.getMessage ().toString ());

            Assert.assertTrue ("Error code incorrect", e.getMessage ().contains ("REQ_COUNT_MISMATCH"));
            Assert.assertTrue ("Substitution value incorrect",
                    e.getMessage ().contains ("Unexpected Total Number of Requests identified"));
        }

    }

}
