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

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.ErrorMessage;
import uk.gov.moj.sdt.domain.GlobalParameter;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IErrorMessage;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.validators.exception.CustomerNotFoundException;

/**
 * Tests for {@link BulkCustomerValidatorTest}.
 *
 * @author d120520
 */
public class BulkCustomerValidatorTest extends AbstractValidatorUnitTest {
    /**
     * Subject for test.
     */
    private BulkCustomerValidator validator;

    /**
     * IBulkCustomer dao.
     */
    private IBulkCustomerDao mockIBulkCustomerDao;

    /**
     * The bulk customer.
     */
    private IBulkCustomer bulkCustomer;

    /**
     * Parameter cache.
     */
    private ICacheable globalParameterCache;

    /**
     * Global parameter.
     */
    private IGlobalParameter globalParameter;

    /**
     * Error messages cache.
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
     * SDT Customer Id used for testing.
     */
    private long sdtCustomerId = 12345;

    /**
     * Setup of the Validator and Domain class instance.
     */
    public void setUpLocalTests() {

        validator = new BulkCustomerValidator();

        // Create mocks needed for this test.
        mockIBulkCustomerDao = EasyMock.createMock(IBulkCustomerDao.class);
        bulkCustomer = new BulkCustomer();

        // Set up Global parameters cache
        globalParameter = new GlobalParameter();
        globalParameter.setName(IGlobalParameter.ParameterKey.CONTACT_DETAILS.name());
        globalParameter.setValue(contact);
        globalParameterCache = EasyMock.createMock(ICacheable.class);
        expect(
                globalParameterCache.getValue(IGlobalParameter.class,
                        IGlobalParameter.ParameterKey.CONTACT_DETAILS.name())).andReturn(globalParameter);
        replay(globalParameterCache);
        validator.setGlobalParameterCache(globalParameterCache);

        // Set up Error messages cache
        errorMessage = new ErrorMessage();
        errorMessage.setErrorCode(IErrorMessage.ErrorCode.CUST_ID_INVALID.name());
        errorMessage.setErrorText("The Bulk Customer organisation does not have an SDT Customer ID set up. "
                + "Please contact {1} for assistance.");
        errorMessagesCache = EasyMock.createMock(ICacheable.class);
        expect(errorMessagesCache.getValue(IErrorMessage.class, IErrorMessage.ErrorCode.CUST_ID_INVALID.name()))
                .andReturn(errorMessage);
        replay(errorMessagesCache);
        validator.setErrorMessagesCache(errorMessagesCache);

    }

    /**
     * Test success flow.
     */
    @Test
    public void testCustomerFound() {
        // Setup bulk customer for test.
        bulkCustomer.setSdtCustomerId(12345L);

        // Tell the mock dao to return the same bulk customer.
        expect(mockIBulkCustomerDao.getBulkCustomerBySdtId(sdtCustomerId)).andReturn(bulkCustomer);
        replay(mockIBulkCustomerDao);

        // Inject the mock dao into the validator.
        validator.setBulkCustomerDao(mockIBulkCustomerDao);

        // Validate the bulk customer.
        bulkCustomer.accept(validator, null);
        EasyMock.verify(mockIBulkCustomerDao);

        Assert.assertEquals(bulkCustomer.getSdtCustomerId(), sdtCustomerId);
        Assert.assertTrue(true);
    }

    /**
     * Test success flow.
     */
    @Test
    public void testCustomerNotFound() {
        bulkCustomer.setSdtCustomerId(12345L);

        // Tell the mock dao to return the same bulk customer.
        expect(mockIBulkCustomerDao.getBulkCustomerBySdtId(sdtCustomerId)).andReturn(null);
        replay(mockIBulkCustomerDao);

        // Inject the mock dao into the validator.
        validator.setBulkCustomerDao(mockIBulkCustomerDao);

        try {
            // Validate the bulk customer.
            bulkCustomer.accept(validator, null);

            Assert.fail("Test failed to throw CustomerNotFoundException.");
        } catch (final CustomerNotFoundException e) {
            EasyMock.verify(mockIBulkCustomerDao);

            // [^\[]*\[CUST_NOT_SETUP\][^\[]*\[12345\].*
            Assert.assertTrue("Error code incorrect",
                    e.getErrorCode().equals(IErrorMessage.ErrorCode.CUST_ID_INVALID.name()));
            // CHECKSTYLE:OFF
            Assert.assertTrue(
                    "Substitution value incorrect",
                    e.getErrorDescription().equals(
                            "The Bulk Customer organisation does not have an SDT Customer ID set up. Please contact " +
                                    contact + " for assistance."));
            // CHECKSTYLE:ON
        }
    }
}
