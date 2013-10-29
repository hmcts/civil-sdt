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
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.EasyMock;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.dao.api.IBulkSubmissionDao;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.ErrorMessage;
import uk.gov.moj.sdt.domain.GlobalParameter;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IErrorMessage;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.utils.Utilities;
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
public class BulkSubmissionValidatorTest extends AbstractValidatorUnitTest
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
     * bulk submission.
     */
    private BulkSubmission bulkSubmission;

    /**
     * The bulk customer.
     */
    private IBulkCustomer bulkCustomer;

    /**
     * Parameter cache.
     */
    private ICacheable globalParameterCache;

    /**
     * Parameter cache.
     */
    private ICacheable errorMessagesCache;

    /**
     * Error message.
     */
    private IErrorMessage errorMessage;

    /**
     * Contact details for assistance.
     */
    private String contact = "THE MOJ";

    /**
     * List of individual requests.
     */
    private List<IIndividualRequest> individualRequests;

    /**
     * The number of requests.
     */
    private final long numberOfRequests = 1L;

    /**
     * Request id.
     */

    private final long requestId = 20L;

    /**
     * Data retention period.
     * 
     */
    private final int dataRetentionPeriod = 90;

    /**
     * SDT Bulk reference.
     */
    private String sdtBulkReference = "sdtBulkReference";

    /**
     * Current date time.
     */
    private LocalDateTime now = new LocalDateTime ();

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

        globalParameterCache = EasyMock.createMock (ICacheable.class);

    }

    /**
     * This method sets up a bulk submission and puts a domain object in there (bulk customer).
     * 
     * @param numberOfRequests the number of requests in this submission
     * @param bulkCustomer the bulk customer
     * @param individualRequests the list of individual requests
     * @param application the application name for this bulk submission (MCOL, PCOL etc)
     */

    /**
     * The purpose of this test is to have a clean run through these three conditions.
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
        bulkCustomer = createCustomer (createBulkCustomerApplications ("MCOL"));

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

        expect (mockIBulkSubmissionDao.getBulkSubmission (bulkCustomer, bulkSubmission.getCustomerReference (), 90))
                .andReturn (null);
        replay (mockIBulkSubmissionDao);

        validator.setBulkSubmissionDao (mockIBulkSubmissionDao);

        setupDataRetentionCache ();

        bulkSubmission.accept (validator, null);

        EasyMock.verify (mockIBulkCustomerDao);
        EasyMock.verify (mockIBulkSubmissionDao);
        EasyMock.verify (globalParameterCache);

    }

    /**
     * The purpose of this test is to have a clean run through these three conditions.
     * 1) The customer has access to the target application
     * 2) No invalid bulk submission
     * 3) The second individual request has a duplicate request id
     * 
     */
    @Test
    public void testDuplicateIndividualRequest ()
    {

        // set up a bulk customer to use the MCOL application
        bulkCustomer = createCustomer (createBulkCustomerApplications ("MCOL"));

        // create an individual request
        individualRequests = new ArrayList<IIndividualRequest> ();
        IIndividualRequest individualRequest = new IndividualRequest ();
        individualRequest.setId (1L);
        individualRequest.setCustomerRequestReference ("Duplicate");
        individualRequests.add (individualRequest);
        // Set the duplicate request
        individualRequest = new IndividualRequest ();
        individualRequest.setId (2L);
        individualRequest.setCustomerRequestReference ("Duplicate");
        individualRequests.add (individualRequest);

        createBulkSubmission (individualRequests.size (), bulkCustomer, individualRequests, "MCOL");

        // set up the mock objects
        expect (mockIBulkCustomerDao.getBulkCustomerBySdtId (12345L)).andReturn (bulkCustomer);
        replay (mockIBulkCustomerDao);

        // inject the bulk customer into the validator
        validator.setBulkCustomerDao (mockIBulkCustomerDao);

        // create a mock Bulk Submission DAO and return true
        mockIBulkSubmissionDao = EasyMock.createMock (IBulkSubmissionDao.class);

        expect (
                mockIBulkSubmissionDao.getBulkSubmission (bulkCustomer, bulkSubmission.getCustomerReference (),
                        dataRetentionPeriod)).andReturn (null);
        replay (mockIBulkSubmissionDao);

        validator.setBulkSubmissionDao (mockIBulkSubmissionDao);

        setupDataRetentionCache ();

        // Set up Error messages cache
        errorMessage = new ErrorMessage ();
        errorMessage.setErrorCode (IErrorMessage.ErrorCode.DUPLD_CUST_REQID.name ());
        errorMessage.setErrorText ("Unique Request Identifier has been specified more "
                + "than once within the originating Bulk Request.");
        errorMessagesCache = EasyMock.createMock (ICacheable.class);
        expect (errorMessagesCache.getValue (IErrorMessage.class, IErrorMessage.ErrorCode.DUPLD_CUST_REQID.name ()))
                .andReturn (errorMessage);
        replay (errorMessagesCache);
        validator.setErrorMessagesCache (errorMessagesCache);

        bulkSubmission.accept (validator, null);

        // Check the duplicate individual request has been rejected
        Assert.assertEquals (IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus (), bulkSubmission
                .getIndividualRequests ().get (1).getRequestStatus ());

    }

    /**
     * 
     * This test will fail on the check customer has access to MCOL application and catch the exception.
     */
    @Test
    public void testCustomerDoesNotHaveAccess ()
    {
        try
        {

            // set up the data we are going to use for this customer
            // set up bulk customer with the application it can use

            // set up a bulk customer to use the PCOL application to make it error as the bulk submission sets MCOL.
            bulkCustomer = createCustomer (createBulkCustomerApplications ("PCOL"));

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

            setupContactDetailsCache ();

            // Set up Error messages cache
            errorMessage = new ErrorMessage ();
            errorMessage.setErrorCode (IErrorMessage.ErrorCode.CUST_NOT_SETUP.name ());
            errorMessage.setErrorText ("The Bulk Customer organisation is not setup to send Service "
                    + "Request messages to the {0}. Please contact {1} for assistance.");
            errorMessagesCache = EasyMock.createMock (ICacheable.class);
            expect (errorMessagesCache.getValue (IErrorMessage.class, IErrorMessage.ErrorCode.CUST_NOT_SETUP.name ()))
                    .andReturn (errorMessage);
            replay (errorMessagesCache);
            validator.setErrorMessagesCache (errorMessagesCache);

            bulkSubmission.accept (validator, null);
            Assert.fail ("Test failed to throw CustomerNotSetupException ");

        }
        catch (final CustomerNotSetupException e)
        {
            LOGGER.debug (e.getMessage ().toString ());
            EasyMock.verify (mockIBulkCustomerDao);

            Assert.assertTrue ("Error code incorrect",
                    e.getErrorCode ().equals (IErrorMessage.ErrorCode.CUST_NOT_SETUP.name ()));
            // CHECKSTYLE:OFF
            Assert.assertTrue (
                    "Substitution value incorrect",
                    e.getErrorDescription ()
                            .equals (
                                    "The Bulk Customer organisation is not setup to send Service Request messages to the MCOL. " +
                                            "Please contact " + contact + " for assistance."));
            // CHECKSTYLE:OFF
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
            // set up a bulk customer to use the MCOL application to make it error as the bulk submission sets MCOL.
            bulkCustomer = createCustomer (createBulkCustomerApplications ("MCOL"));

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

            expect (
                    mockIBulkSubmissionDao.getBulkSubmission (bulkCustomer, bulkSubmission.getCustomerReference (),
                            dataRetentionPeriod)).andReturn (bulkSubmission);
            replay (mockIBulkSubmissionDao);

            validator.setBulkSubmissionDao (mockIBulkSubmissionDao);

            setupDataRetentionCache ();

            // Set up Error messages cache
            errorMessage = new ErrorMessage ();
            errorMessage.setErrorCode (IErrorMessage.ErrorCode.DUP_CUST_FILEID.name ());
            errorMessage.setErrorText ("Duplicate User File Reference {0} supplied. "
                    + "This was previously used to submit a Bulk Request on {1} "
                    + "and the SDT Bulk Reference {2} was allocated.");
            errorMessagesCache = EasyMock.createMock (ICacheable.class);
            expect (errorMessagesCache.getValue (IErrorMessage.class, IErrorMessage.ErrorCode.DUP_CUST_FILEID.name ()))
                    .andReturn (errorMessage);
            replay (errorMessagesCache);
            validator.setErrorMessagesCache (errorMessagesCache);

            bulkSubmission.accept (validator, null);
            Assert.fail ("Test failed to throw CustomerReferenceNotUniqueException ");

        }
        catch (final CustomerReferenceNotUniqueException e)
        {
            EasyMock.verify (mockIBulkCustomerDao);
            EasyMock.verify (mockIBulkSubmissionDao);
            Assert.assertTrue ("Error code incorrect",
                    e.getErrorCode ().equals (IErrorMessage.ErrorCode.DUP_CUST_FILEID.name ()));
            Assert.assertTrue (
                    "Substitution value incorrect",
                    e.getErrorDescription ().equals (
                            "Duplicate User File Reference " + bulkSubmission.getCustomerReference () + " supplied. " +
                                    "This was previously used to submit a Bulk Request on " +
                                    Utilities.formatDateTimeForMessage (now) + " and the SDT Bulk Reference " +
                                    sdtBulkReference + " was allocated."));
        }

    }

    /**
     * 
     * This test will have an incorrect number of requests and catch the exception.
     */
    @Test
    public void testRequestCountdoesNotMatch ()
    {

        final long mismatchTotal = 15;
        try
        {

            // set up a bulk customer to use the MCOL application
            bulkCustomer = createCustomer (createBulkCustomerApplications ("MCOL"));

            // create an individual request
            final IIndividualRequest individualRequest = new IndividualRequest ();
            individualRequest.setId (requestId);
            individualRequests = new ArrayList<IIndividualRequest> ();
            individualRequests.add (individualRequest);

            createBulkSubmission (mismatchTotal, bulkCustomer, individualRequests, "MCOL");

            // set up the mock objects
            expect (mockIBulkCustomerDao.getBulkCustomerBySdtId (12345L)).andReturn (bulkCustomer);
            replay (mockIBulkCustomerDao);

            // inject the bulk customer into the validator
            validator.setBulkCustomerDao (mockIBulkCustomerDao);

            // create a mock Bulk Submission DAO and return true
            mockIBulkSubmissionDao = EasyMock.createMock (IBulkSubmissionDao.class);

            expect (
                    mockIBulkSubmissionDao.getBulkSubmission (bulkCustomer, bulkSubmission.getCustomerReference (),
                            dataRetentionPeriod)).andReturn (null);
            replay (mockIBulkSubmissionDao);

            validator.setBulkSubmissionDao (mockIBulkSubmissionDao);

            setupDataRetentionCache ();

            // Set up Error messages cache
            errorMessage = new ErrorMessage ();
            errorMessage.setErrorCode (IErrorMessage.ErrorCode.REQ_COUNT_MISMATCH.name ());
            errorMessage
                    .setErrorText ("Unexpected Total Number of Requests identified. {0} requested identified, {1} requests expected in Bulk Request {2}.");
            errorMessagesCache = EasyMock.createMock (ICacheable.class);
            expect (
                    errorMessagesCache.getValue (IErrorMessage.class,
                            IErrorMessage.ErrorCode.REQ_COUNT_MISMATCH.name ())).andReturn (errorMessage);
            replay (errorMessagesCache);
            validator.setErrorMessagesCache (errorMessagesCache);

            bulkSubmission.accept (validator, null);

            Assert.fail ("Test failed to throw RequestCountMismatchException ");
        }
        catch (final RequestCountMismatchException e)
        {
            LOGGER.debug (e.getMessage ().toString ());

            EasyMock.verify (mockIBulkCustomerDao);
            EasyMock.verify (mockIBulkSubmissionDao);
            Assert.assertTrue ("Error code incorrect",
                    e.getErrorCode ().equals (IErrorMessage.ErrorCode.REQ_COUNT_MISMATCH.name ()));
            Assert.assertTrue (
                    "Substitution value incorrect",
                    e.getErrorDescription ().equals (
                            "Unexpected Total Number of Requests identified. 1 requested identified, " + mismatchTotal +
                                    " requests expected in Bulk Request " + bulkSubmission.getCustomerReference () +
                                    "."));
        }

    }

    /**
     * 
     * This test the scenario where all individual requests of a bulk submission are rejected.
     */
    @Test
    public void testIndividualRequestAllRejected ()
    {
        // set up the data we are going to use for this customer
        // set up bulk customer with the application it can use
        bulkCustomer = createCustomer (createBulkCustomerApplications ("MCOL"));

        // set up the mock objects
        expect (mockIBulkCustomerDao.getBulkCustomerBySdtId (12345L)).andReturn (bulkCustomer);
        replay (mockIBulkCustomerDao);

        // inject the bulk customer into the validator
        validator.setBulkCustomerDao (mockIBulkCustomerDao);

        // reset the list of individual request
        individualRequests = new ArrayList<IIndividualRequest> ();

        // Rejected error
        IErrorLog errorLog =
                new ErrorLog (IErrorMessage.ErrorCode.DUP_CUST_REQID.name (),
                        "Duplicate Unique Request Identifier submitted {0}");

        // create an individual request 1
        IIndividualRequest individualRequest = new IndividualRequest ();
        individualRequest.setId (1L);
        individualRequest.markRequestAsRejected (errorLog);
        individualRequests.add (individualRequest);

        // create an individual request 2
        individualRequest = new IndividualRequest ();
        individualRequest.setId (2L);
        individualRequest.markRequestAsRejected (errorLog);
        individualRequests.add (individualRequest);

        // create an individual request 3
        individualRequest = new IndividualRequest ();
        individualRequest.setId (3L);
        individualRequest.markRequestAsRejected (errorLog);
        individualRequests.add (individualRequest);

        createBulkSubmission (3, bulkCustomer, individualRequests, "MCOL");

        // Set up Error messages cache
        final String errorText = "The submitted Bulk Request does not contain valid individual Requests.";

        errorMessage = new ErrorMessage ();
        errorMessage.setErrorCode (IErrorMessage.ErrorCode.NO_VALID_REQS.name ());
        errorMessage.setErrorText (errorText);
        errorMessagesCache = EasyMock.createMock (ICacheable.class);
        expect (errorMessagesCache.getValue (IErrorMessage.class, IErrorMessage.ErrorCode.NO_VALID_REQS.name ()))
                .andReturn (errorMessage);
        replay (errorMessagesCache);
        validator.setErrorMessagesCache (errorMessagesCache);

        // Test the method
        validator.checkIndividualRequests (bulkSubmission);

        Assert.assertEquals (IBulkSubmission.BulkRequestStatus.COMPLETED.name (), bulkSubmission.getSubmissionStatus ());
        Assert.assertEquals (IErrorMessage.ErrorCode.NO_VALID_REQS.name (), bulkSubmission.getErrorCode ());
    }

    private void createBulkSubmission (final long numberOfRequests, final IBulkCustomer bulkCustomer,
                                       final List<IIndividualRequest> individualRequests, final String application)
    {

        bulkSubmission = new BulkSubmission ();
        bulkSubmission.setBulkCustomer (bulkCustomer);
        bulkSubmission.setCustomerReference ("Customer Reference");
        bulkSubmission.setTargetApplication (createTargetApp (application));
        bulkSubmission.setNumberOfRequest (numberOfRequests);
        bulkSubmission.setIndividualRequests (individualRequests);
        bulkSubmission.setCreatedDate (now);
        bulkSubmission.setSdtBulkReference (sdtBulkReference);
    }

    /**
     * Method to mock and test GlobalParameterCache.
     */
    private void setupDataRetentionCache ()
    {
        // Setup data retention period
        final IGlobalParameter globalParameterData = new GlobalParameter ();
        globalParameterData.setName (IGlobalParameter.ParameterKey.DATA_RETENTION_PERIOD.name ());
        globalParameterData.setValue ("90");

        expect (
                globalParameterCache.getValue (IGlobalParameter.class,
                        IGlobalParameter.ParameterKey.DATA_RETENTION_PERIOD.name ())).andReturn (globalParameterData);
        replay (globalParameterCache);

        validator.setGlobalParameterCache (globalParameterCache);

    }

    /**
     * Method to mock and test GlobalParameterCache.
     */
    private void setupContactDetailsCache ()
    {

        // Set up contact details
        final IGlobalParameter globalParameterContact = new GlobalParameter ();
        globalParameterContact.setName (IGlobalParameter.ParameterKey.CONTACT_DETAILS.name ());
        globalParameterContact.setValue (contact);

        expect (
                globalParameterCache.getValue (IGlobalParameter.class,
                        IGlobalParameter.ParameterKey.CONTACT_DETAILS.name ())).andReturn (globalParameterContact);
        replay (globalParameterCache);
        validator.setGlobalParameterCache (globalParameterCache);
    }

}
