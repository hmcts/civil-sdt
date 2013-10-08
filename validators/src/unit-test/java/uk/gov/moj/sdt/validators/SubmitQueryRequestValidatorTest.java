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

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.SubmitQueryRequest;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.utils.SdtUnitTestBase;
import uk.gov.moj.sdt.validators.exception.CustomerNotSetupException;

/**
 * Tests for {@link SubmitQueryRequestValidatorTest}.
 * 
 * 
 * 
 * @author d120520
 * 
 */

public class SubmitQueryRequestValidatorTest extends SdtUnitTestBase
{

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog (SubmitQueryRequestValidatorTest.class);

    /**
     * SubmitQueryRequestValidator.
     */
    private SubmitQueryRequestValidator validator;

    /**
     * SubmitQueryRequest.
     */
    private SubmitQueryRequest submitQueryRequest;

    /**
     * IBulkCustomer.
     */
    private IBulkCustomer bulkCustomer;

    /**
     * ITargetApplication.
     */
    private ITargetApplication targetApplication;

    /**
     * IBulkCustomerDao.
     */
    private IBulkCustomerDao mockIBulkCustomerDao;

    /**
     * Constructor for test.
     * 
     * @param testName name of this test class.
     */
    public SubmitQueryRequestValidatorTest (final String testName)
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
        validator = new SubmitQueryRequestValidator ();

        // domain objects
        bulkCustomer = createCustomer (addAppToCustomerApplications ("PCOL CODE", "PCOL"));

        submitQueryRequest = new SubmitQueryRequest ();
        submitQueryRequest.setBulkCustomer (bulkCustomer);

        // mock BulkCustomer object
        mockIBulkCustomerDao = EasyMock.createMock (IBulkCustomerDao.class);
    }

    /**
     * create a bulk customer.
     * 
     * @param applicationSet the set of ITargetApplication objects
     * @return a bulk customer
     */
    private IBulkCustomer createCustomer (final Set<ITargetApplication> applicationSet)
    {
        final IBulkCustomer bulkCustomer = new BulkCustomer ();
        bulkCustomer.setSdtCustomerId (12345L);
        bulkCustomer.setTargetApplications (applicationSet);

        return bulkCustomer;
    }

    /**
     * The purpose of this test is to test an invalid request and test the exception.
     */

    /**
     * the list of applications for a customer.
     * 
     * @param applicationCode the code for the application
     * @param applicationName the application name
     * @return the set of target applications for this customer
     */
    private Set<ITargetApplication> addAppToCustomerApplications (final String applicationCode,
                                                                  final String applicationName)
    {
        final Set<ITargetApplication> targetApplications = new HashSet<ITargetApplication> ();
        targetApplications.add (createTargetApp (applicationCode, applicationName));
        return targetApplications;
    }

    /**
     * create an application with a given name.
     * 
     * @param targetApplicationCode the code for the application, MCOL etc
     * @param applicationName the application name
     * @return ITargetApplication
     */
    private ITargetApplication createTargetApp (final String targetApplicationCode, final String applicationName)
    {
        final ITargetApplication application = new TargetApplication ();
        application.setTargetApplicationCode (targetApplicationCode);
        application.setTargetApplicationName (applicationName);
        return application;
    }

    /**
     * test the scenario where the customer does not have access to the target application.
     */
    @Test
    public void testCustomerDoesNotHaveAccess ()
    {
        try
        {

            submitQueryRequest.setTargetApplication (createTargetApp ("MCOL CODE", "MCOL"));
            // set up the mock objects
            expect (mockIBulkCustomerDao.getBulkCustomerBySdtId (12345L)).andReturn (bulkCustomer);
            replay (mockIBulkCustomerDao);

            // inject the bulk customer into the validator
            validator.setBulkCustomerDao (mockIBulkCustomerDao);

            submitQueryRequest.accept (validator, null);
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
     * test the scenario where the customer has access to the target application.
     */
    @Test
    public void testCustomerHasAccess ()
    {

        // set up QueryRequest to use PCOL as the application, to match the customer application list.
        submitQueryRequest.setTargetApplication (createTargetApp ("PCOL CODE", "PCOL"));
        // set up the mock objects
        expect (mockIBulkCustomerDao.getBulkCustomerBySdtId (12345L)).andReturn (bulkCustomer);
        replay (mockIBulkCustomerDao);

        // inject the bulk customer into the validator
        validator.setBulkCustomerDao (mockIBulkCustomerDao);

        submitQueryRequest.accept (validator, null);

    }

}
