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

import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.utils.SdtUnitTestBase;
import uk.gov.moj.sdt.validators.exception.CustomerNotSetupException;

/**
 * Tests for {@link BulkCustomerValidatorTest}.
 * 
 * @author d120520
 * 
 */
public class BulkCustomerValidatorTest extends SdtUnitTestBase
{
    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog (BulkCustomerValidatorTest.class);

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
     * Constructor for test.
     * 
     * @param testName name of this test class.
     */
    public BulkCustomerValidatorTest (final String testName)
    {
        super (testName);
    }

    /**
     * Setup of the Validator and Domain class instance.
     */
    @Before
    public void setUp ()
    {

        validator = new BulkCustomerValidator ();

        // Create mocks needed for this test.
        mockIBulkCustomerDao = EasyMock.createMock (IBulkCustomerDao.class);
        bulkCustomer = new BulkCustomer ();
    }

    /**
     * Test success flow.
     */
    @Test
    public void testCustomerFound ()
    {
        // Setup bulk customer for test.
        bulkCustomer.setSdtCustomerId (12345L);

        // Tell the mock dao to return the same bulk customer.
        expect (mockIBulkCustomerDao.getBulkCustomerBySdtId (12345L)).andReturn (bulkCustomer);
        replay (mockIBulkCustomerDao);

        // Inject the mock dao into the validator.
        validator.setBulkCustomerDao (mockIBulkCustomerDao);

        // Validate the bulk customer.
        bulkCustomer.accept (validator, null);
        EasyMock.verify (mockIBulkCustomerDao);
    }

    /**
     * Test success flow.
     */
    @Test
    public void testCustomerNotFound ()
    {
        bulkCustomer.setSdtCustomerId (12345L);

        // Tell the mock dao to return the same bulk customer.
        expect (mockIBulkCustomerDao.getBulkCustomerBySdtId (12345L)).andReturn (null);
        replay (mockIBulkCustomerDao);

        // Inject the mock dao into the validator.
        validator.setBulkCustomerDao (mockIBulkCustomerDao);

        try
        {
            // Validate the bulk customer.
            bulkCustomer.accept (validator, null);

            Assert.fail ("Test failed to throw CustomerNotSetupException.");
        }
        catch (final CustomerNotSetupException e)
        {
            EasyMock.verify (mockIBulkCustomerDao);

            // [^\[]*\[CUST_NOT_SETUP\][^\[]*\[12345\].*
            Assert.assertTrue ("Error code incorrect", e.getMessage ().contains ("CUST_NOT_SETUP"));
            Assert.assertTrue ("Substitution value incorrect", e.getMessage ().contains ("12345"));
        }
    }
}
