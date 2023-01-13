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
package uk.gov.moj.sdt.validators;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.dao.api.IBulkSubmissionDao;
import uk.gov.moj.sdt.domain.ErrorMessage;
import uk.gov.moj.sdt.domain.GlobalParameter;
import uk.gov.moj.sdt.domain.api.IErrorMessage;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.validators.exception.CustomerNotFoundException;
import uk.gov.moj.sdt.validators.exception.CustomerNotSetupException;
import uk.gov.moj.sdt.validators.exception.CustomerReferenceNotUniqueException;
import uk.gov.moj.sdt.validators.exception.DuplicateUserRequestIdentifierException;
import uk.gov.moj.sdt.validators.exception.InvalidBulkReferenceException;
import uk.gov.moj.sdt.validators.exception.RequestCountMismatchException;

import java.util.ArrayList;
import java.util.List;

/**
 * Test Class for SDT Validator.
 *
 * @author d164190
 */
public class SdtValidatorTest extends AbstractSdtUnitTestBase {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SdtValidatorTest.class);

    /**
     * Mock Error messages cache.
     */
    private ICacheable mockErrorMessagesCache;

    /**
     * Mock global parameters cache.
     */
    private ICacheable mockGlobalParameterCache;

    /**
     * Validator to run the test through.
     */
    private BulkSubmissionValidator validator;

    /**
     * Array containing the error messages.
     */
    private IErrorMessage[] result;

    /**
     * Global parameter.
     */
    private IGlobalParameter globalParam;

    /**
     * Set up test artefact.
     */
    public void setUpLocalTests() {
        mockErrorMessagesCache = EasyMock.createMock(ICacheable.class);
        mockGlobalParameterCache = EasyMock.createMock(ICacheable.class);

        IBulkCustomerDao mockIBulkCustomerDao = EasyMock.createMock(IBulkCustomerDao.class);
        IBulkSubmissionDao bulkSubmissionDao = EasyMock.createMock(IBulkSubmissionDao.class);
        validator = new BulkSubmissionValidator(mockIBulkCustomerDao, mockGlobalParameterCache, mockErrorMessagesCache, bulkSubmissionDao);

        result = new ErrorMessage[7];
        result[0] = new ErrorMessage();
        result[0].setErrorCode("DUP_CUST_FILEID");
        result[0].setErrorText("Duplicate User File Reference {0} supplied. This was previously used to submit a "
                + "Bulk Request on {1} and the SDT Bulk Reference {2} was allocated.");

        result[1] = new ErrorMessage();
        result[1].setErrorCode("REQ_COUNT_MISMATCH");
        result[1].setErrorText("Unexpected Total Number of Requests identified. {0} requested identified,"
                + " {1} requests expected in Bulk Request {2}.");

        result[2] = new ErrorMessage();
        result[2].setErrorCode("SDT_INT_ERR");
        result[2].setErrorText("A system error has occurred. Please contact {0} for assistance.");

        result[3] = new ErrorMessage();
        result[3].setErrorCode("CUST_NOT_SETUP");
        result[3].setErrorText("The Bulk Customer organisation is not setup to send Service Request messages to "
                + "the {0}. Please contact {1} for assistance.");

        result[4] = new ErrorMessage();
        result[4].setErrorCode("CUST_ID_INVALID");
        result[4].setErrorText("The Bulk Customer organisation does not have an SDT Customer ID set up."
                + " Please contact {0} for assistance.");

        result[5] = new ErrorMessage();
        result[5].setErrorCode("BULK_REF_INVALID");
        result[5].setErrorText("There is no Bulk Request submission associated with your account for the supplied"
                + " SDT Bulk Reference {0}.");

        result[6] = new ErrorMessage();
        result[6].setErrorCode("DUP_CUST_REQID");
        result[6].setErrorText("Duplicate Unique Request Identifier submitted {0}.");

        globalParam = new GlobalParameter();
        globalParam.setName("CONTACT_DETAILS");
        globalParam.setValue("TBD");
    }

    /**
     * Test that the correct message is retrieved.
     */
    public void testGetErrorMessage() {
        EasyMock.expect(mockErrorMessagesCache.getValue(IErrorMessage.class, "DUP_CUST_REQID")).andReturn(result[6]);
        EasyMock.replay(mockErrorMessagesCache);

        List<String> replacements = null;
        replacements = new ArrayList<String>();
        replacements.add("MCOL0000012345");

        final String description = validator.getErrorMessage(replacements, IErrorMessage.ErrorCode.DUP_CUST_REQID);
        LOGGER.debug("testGetErrorMessage [" + description + "]");
        EasyMock.verify(mockErrorMessagesCache);
        Assert.assertTrue("Incorrect Message and/or substitution",
                description.contains("Duplicate Unique Request Identifier submitted MCOL0000012345."));
    }

    /**
     *
     */
    public void testGetContactDetails() {
        EasyMock.expect(mockGlobalParameterCache.getValue(IGlobalParameter.class, "CONTACT_DETAILS")).andReturn(
                globalParam);
        EasyMock.replay(mockGlobalParameterCache);

        final String contactDetails = validator.getContactDetails();
        LOGGER.debug("testGetContactDetails [" + contactDetails + "]");
        EasyMock.verify(mockGlobalParameterCache);
        Assert.assertTrue("Incorrect contact details", contactDetails.contains("TBD"));
    }

    /**
     * Test that the correct exception is created.
     */
    @Test
    public void testCreateValidationExceptionDupCustFileid() {
        EasyMock.expect(mockErrorMessagesCache.getValue(IErrorMessage.class, "DUP_CUST_FILEID"))
                .andReturn(result[0]);
        EasyMock.replay(mockErrorMessagesCache);

        List<String> replacements = null;
        replacements = new ArrayList<String>();
        replacements.add("MCOL00001234");
        replacements.add("09/10/2013");
        replacements.add("SDT00001234");

        try {
            validator.createValidationException(replacements, IErrorMessage.ErrorCode.DUP_CUST_FILEID);
            Assert.fail("Failed to throw expected CustomerReferenceNotUniqueException for error code DUP_CUST_FILEID");
        } catch (final CustomerReferenceNotUniqueException e) {
            LOGGER.debug(e.getMessage());
            EasyMock.verify(mockErrorMessagesCache);
            Assert.assertTrue("Failed with code", e.getMessage().contains("DUP_CUST_FILEID"));
            Assert.assertTrue("Substitution value incorrect", e.getMessage().contains("MCOL00001234"));
            Assert.assertTrue("Substitution value incorrect", e.getMessage().contains("09/10/2013"));
            Assert.assertTrue("Substitution value incorrect", e.getMessage().contains("SDT00001234"));
        }
    }

    /**
     * Test that the correct exception is created.
     */
    @Test
    public void testCreateValidationExceptionReqCountMismatch() {
        EasyMock.expect(mockErrorMessagesCache.getValue(IErrorMessage.class, "REQ_COUNT_MISMATCH")).andReturn(
                result[1]);
        EasyMock.replay(mockErrorMessagesCache);

        List<String> replacements = null;
        replacements = new ArrayList<String>();
        replacements.add("9");
        replacements.add("10");
        replacements.add("MCOL0000123");
        try {
            validator.createValidationException(replacements, IErrorMessage.ErrorCode.REQ_COUNT_MISMATCH);
            Assert.fail("Failed to throw expected RequestCountMismatchException for error code REQ_COUNT_MISMATCH");
        } catch (final RequestCountMismatchException e) {
            LOGGER.debug(e.getMessage());
            EasyMock.verify(mockErrorMessagesCache);
            Assert.assertTrue("Failed with code", e.getMessage().contains("REQ_COUNT_MISMATCH"));
            Assert.assertTrue("Substitution value incorrect", e.getMessage().contains("MCOL0000123"));
        }
    }

    /**
     * Test that the correct exception is created.
     */
    @Test
    public void testCreateValidationExceptionCustNotSetup() {
        EasyMock.expect(mockErrorMessagesCache.getValue(IErrorMessage.class, "CUST_NOT_SETUP")).andReturn(result[3]);
        EasyMock.replay(mockErrorMessagesCache);

        List<String> replacements = null;
        replacements = new ArrayList<String>();
        replacements.add("MCOL");
        replacements.add("TBD");

        try {
            validator.createValidationException(replacements, IErrorMessage.ErrorCode.CUST_NOT_SETUP);
            Assert.fail("Failed to throw expected CustomerNotSetupException for error code CUST_NOT_SETUP");
        } catch (final CustomerNotSetupException e) {
            LOGGER.debug(e.getMessage());
            EasyMock.verify(mockErrorMessagesCache);
            Assert.assertTrue("Failed with code", e.getMessage().contains("CUST_NOT_SETUP"));
            Assert.assertTrue("Substitution value incorrect", e.getMessage().contains("MCOL"));
            Assert.assertTrue("Substitution value incorrect", e.getMessage().contains("TBD"));
        }
    }

    /**
     * Test that the correct exception is created.
     */
    @Test
    public void testCreateValidationExceptionCustRefMissing() {
        EasyMock.expect(mockErrorMessagesCache.getValue(IErrorMessage.class, "CUST_ID_INVALID"))
                .andReturn(result[4]);
        EasyMock.replay(mockErrorMessagesCache);

        List<String> replacements = null;
        replacements = new ArrayList<String>();
        replacements.add("TBD");

        try {
            validator.createValidationException(replacements, IErrorMessage.ErrorCode.CUST_ID_INVALID);
            Assert.fail("Failed to throw expected CustomerNotFoundException for error code CUST_ID_INVALID");
        } catch (final CustomerNotFoundException e) {
            LOGGER.debug(e.getMessage());
            EasyMock.verify(mockErrorMessagesCache);
            Assert.assertTrue("Failed with code", e.getMessage().contains("CUST_ID_INVALID"));
            Assert.assertTrue("Substitution value incorrect", e.getMessage().contains("TBD"));
        }
    }

    /**
     * Test that the correct exception is created.
     */
    @Test
    public void testCreateValidationExceptionBulkRefInvalid() {
        EasyMock.expect(mockErrorMessagesCache.getValue(IErrorMessage.class, "BULK_REF_INVALID")).andReturn(
                result[5]);
        EasyMock.replay(mockErrorMessagesCache);

        List<String> replacements = null;
        replacements = new ArrayList<String>();
        replacements.add("MCOL000001234");

        try {
            validator.createValidationException(replacements, IErrorMessage.ErrorCode.BULK_REF_INVALID);
            Assert.fail("Failed to throw expected InvalidBulkReferenceException for error code BULK_REF_INVALID");
        } catch (final InvalidBulkReferenceException e) {
            LOGGER.debug(e.getMessage());
            EasyMock.verify(mockErrorMessagesCache);
            Assert.assertTrue("Failed with code", e.getMessage().contains("BULK_REF_INVALID"));
            Assert.assertTrue("Substitution value incorrect", e.getMessage().contains("MCOL000001234"));
        }
    }

    /**
     * Test that the correct exception is created.
     */
    @Test
    public void testCreateValidationExceptionDupCustReqid() {
        EasyMock.expect(mockErrorMessagesCache.getValue(IErrorMessage.class, "DUP_CUST_REQID")).andReturn(result[6]);
        EasyMock.replay(mockErrorMessagesCache);

        List<String> replacements = null;
        replacements = new ArrayList<String>();
        replacements.add("MCOL000009876");

        try {
            validator.createValidationException(replacements, IErrorMessage.ErrorCode.DUP_CUST_REQID);
            Assert.fail("Failed to throw expected DuplicateUserRequestIdentifierException for error code"
                    + " DUP_CUST_REQID");
        } catch (final DuplicateUserRequestIdentifierException e) {
            LOGGER.debug(e.getMessage());
            EasyMock.verify(mockErrorMessagesCache);
            Assert.assertTrue("Failed with code", e.getMessage().contains("DUP_CUST_REQID"));
            Assert.assertTrue("Substitution value incorrect", e.getMessage().contains("MCOL000009876"));
        }
    }
}
