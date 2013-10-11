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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.gov.moj.sdt.dao.api.IBulkSubmissionDao;
import uk.gov.moj.sdt.domain.BulkFeedbackRequest;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.ErrorMessage;
import uk.gov.moj.sdt.domain.api.IBulkFeedbackRequest;
import uk.gov.moj.sdt.domain.api.IErrorMessage;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.utils.SdtUnitTestBase;
import uk.gov.moj.sdt.validators.exception.InvalidBulkReferenceException;

/**
 * Tests for {@link BulkCustomerValidatorTest}.
 * 
 * @author d120520
 * 
 */

public class BulkFeedbackRequestValidatorTest extends SdtUnitTestBase
{
    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog (BulkFeedbackRequestValidatorTest.class);

    /**
     * Subject for test.
     */
    private BulkFeedbackRequestValidator validator;

    /**
     * The IBulkFeedbackRequest.
     * 
     */

    private IBulkFeedbackRequest bulkFeedbackRequest;

    /**
     * The Dao.
     * 
     */

    private BulkSubmission bulkSubmission;

    /**
     * The Dao.
     * 
     */
    private IBulkSubmissionDao mockIBulkSubmissionDao;

    /**
     * The string reference for our bulk request.
     */
    private String reference = " Bulk reference in request ";

    /**
     * Error messages cache.
     */
    private ICacheable errorMessagesCache;

    /**
     * Error message.
     */
    private IErrorMessage errorMessage;

    /**
     * Constructor for test.
     * 
     * @param testName name of this test class.
     */
    public BulkFeedbackRequestValidatorTest (final String testName)
    {
        super (testName);
    }

    /**
     * Setup of the Validator and Domain class instance.
     */
    @Before
    public void setUp ()
    {

        validator = new BulkFeedbackRequestValidator ();

        // Create mocks needed for this test.
        mockIBulkSubmissionDao = EasyMock.createMock (IBulkSubmissionDao.class);
        bulkFeedbackRequest = new BulkFeedbackRequest ();
        bulkSubmission = new BulkSubmission ();
        bulkSubmission.setSdtBulkReference (reference);
        // Setup bulk request for test.
        bulkFeedbackRequest.setSdtBulkReference (reference);

        // Set up Error messages cache
        errorMessage = new ErrorMessage ();
        errorMessage.setErrorCode (IErrorMessage.ErrorCode.BULK_REF_INVALID.name ());
        errorMessage.setErrorText ("There is no Bulk Request submission associated with your account for "
                + "the supplied SDT Bulk Reference {0}.");
        errorMessagesCache = EasyMock.createMock (ICacheable.class);
        expect (errorMessagesCache.getValue (IErrorMessage.class, IErrorMessage.ErrorCode.BULK_REF_INVALID.name ()))
                .andReturn (errorMessage);
        replay (errorMessagesCache);
        validator.setErrorMessagesCache (errorMessagesCache);

    }

    /**
     * Test success flow.
     */
    @Test
    public void testBulkReferenceFound ()
    {

        // Tell the mock dao to return this request
        // expect (mockIBulkSubmissionDao.isBulkReferenceValid (reference)).andReturn (true);
        expect (mockIBulkSubmissionDao.getBulkSubmission (reference)).andReturn (bulkSubmission);
        replay (mockIBulkSubmissionDao);

        // Inject the mock dao into the validator.
        validator.setBulkSubmissionDao (mockIBulkSubmissionDao);

        // Validate the bulk customer.
        bulkFeedbackRequest.accept (validator, null);
    }

    /**
     * Test success flow.
     */
    @Test
    public void testBulkReferenceNotFound ()
    {

        // Tell the mock dao to return this request
        // expect (mockIBulkSubmissionDao.isBulkReferenceValid (reference)).andReturn (false);
        expect (mockIBulkSubmissionDao.getBulkSubmission (reference)).andReturn (null);
        replay (mockIBulkSubmissionDao);

        // Inject the mock dao into the validator.
        validator.setBulkSubmissionDao (mockIBulkSubmissionDao);

        try
        {
            // Validate the request
            bulkFeedbackRequest.accept (validator, null);

            Assert.fail ("Test failed to throw InvalidBulkReferenceException.");
        }
        catch (final InvalidBulkReferenceException e)
        {

            Assert.assertEquals (IErrorMessage.ErrorCode.BULK_REF_INVALID.name (), e.getErrorCode ());
            Assert.assertEquals ("There is no Bulk Request submission associated with your account for the " +
                    "supplied SDT Bulk Reference " + reference + ".", e.getErrorDescription ());
        }
    }
}
