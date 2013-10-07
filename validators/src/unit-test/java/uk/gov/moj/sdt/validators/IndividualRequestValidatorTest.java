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
import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.GlobalParameter;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.utils.SdtUnitTestBase;

/**
 * Tests for {@link IndividualRequestValidatorTest}.
 * 
 * 
 * 
 * @author d120520
 * 
 */

public class IndividualRequestValidatorTest extends SdtUnitTestBase
{

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog (BulkSubmissionValidatorTest.class);

    /**
     * IndividualRequestValidator.
     */
    private IndividualRequestValidator validator;

    /**
     * IIndividualRequestDao.
     */

    private IIndividualRequestDao mockIndividualRequestDao;

    /**
     * BulkSubmission.
     */

    private BulkSubmission bulkSubmission;

    /**
     * IBulkCustomer.
     */
    private IBulkCustomer bulkCustomer;

    /**
     * requestId.
     */
    private long requestId;

    /**
     * IIndividualRequest.
     */
    private IIndividualRequest individualRequest;

    /**
     * Parameter cache.
     */
    private ICacheable globalParameterCache;

    /**
     * Global parameter.
     */
    private IGlobalParameter globalParameter;

    /**
     * Constructor for test.
     * 
     * @param testName name of this test class.
     */
    public IndividualRequestValidatorTest (final String testName)
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
        validator = new IndividualRequestValidator ();

        // mock BulkCustomer object
        mockIndividualRequestDao = EasyMock.createMock (IIndividualRequestDao.class);

        // create a bulk customer
        bulkCustomer = new BulkCustomer ();
        bulkCustomer.setSdtCustomerId (12345L);

        bulkSubmission = new BulkSubmission ();
        bulkSubmission.setBulkCustomer (bulkCustomer);

        // create an individual request
        individualRequest = new IndividualRequest ();
        individualRequest.setId (requestId);
        individualRequest.setBulkSubmission (bulkSubmission);
        individualRequest.setCustomerRequestReference ("customerRequestReference");

        globalParameter = new GlobalParameter ();
        globalParameter.setName ("DATA_RETENTION_PERIOD");
        globalParameter.setValue ("30");
        globalParameterCache = EasyMock.createMock (ICacheable.class);
        expect (globalParameterCache.getValue (IGlobalParameter.class, "DATA_RETENTION_PERIOD")).andReturn (
                globalParameter);
        replay (globalParameterCache);

        validator.setGlobalParameterCache (globalParameterCache);

    }

    /**
     * The purpose of this test is to test an invalid request and test the exception.
     */
    @Test
    public void testInvalidRequest ()
    {

        expect (
                mockIndividualRequestDao.getIndividualRequest (bulkCustomer,
                        individualRequest.getCustomerRequestReference (), 30)).andReturn (individualRequest);
        replay (mockIndividualRequestDao);

        // inject the bulk customer into the validator
        validator.setIndividualRequestDao (mockIndividualRequestDao);
        individualRequest.accept (validator, null);
        Assert.assertTrue (
                "Error code",
                individualRequest.getErrorLog ().getErrorText ()
                        .contains ("Duplicate Unique Request Identifier submitted"));

    }

    /**
     * The purpose of this test is to pass a valid request.
     */
    @Test
    public void testValidRequest ()
    {

        expect (
                mockIndividualRequestDao.getIndividualRequest (bulkCustomer,
                        individualRequest.getCustomerRequestReference (), 30)).andReturn (null);
        replay (mockIndividualRequestDao);

        // inject the bulk customer into the validator
        validator.setIndividualRequestDao (mockIndividualRequestDao);
        individualRequest.accept (validator, null);

    }
}