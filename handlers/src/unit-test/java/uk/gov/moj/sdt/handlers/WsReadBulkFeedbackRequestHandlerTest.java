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
package uk.gov.moj.sdt.handlers;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkCustomerApplication;
import uk.gov.moj.sdt.domain.BulkFeedbackRequest;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomerApplication;
import uk.gov.moj.sdt.domain.api.IBulkFeedbackRequest;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.services.api.IBulkFeedbackService;
import uk.gov.moj.sdt.transformers.BulkFeedbackTransformer;
import uk.gov.moj.sdt.transformers.api.ITransformer;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackrequestschema.BulkFeedbackRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackrequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema.BulkFeedbackResponseType;

/**
 * Test class for BulkSubmissionService.
 * 
 * @author Sally Vonka
 * 
 */
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (locations = {"classpath*:/uk/gov/moj/sdt/handlers/**/spring*.xml",
        "classpath*:/uk/gov/moj/sdt/domain/**/spring*.xml", "classpath*:/uk/gov/moj/sdt/transformers/**/spring*.xml",
        "classpath*:/uk/gov/moj/sdt/utils/**/spring*.xml", "classpath*:/uk/gov/moj/sdt/validators/**/spring*.xml"})
public class WsReadBulkFeedbackRequestHandlerTest
{
    /**
     * Logger for debugging.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (WsReadBulkFeedbackRequestHandlerTest.class);

    /**
     * Bulk Feedback Service for testing.
     */
    private WsReadBulkFeedbackRequestHandler wsReadBulkFeedbackReqHandler;

    /**
     * Bulk Submission DAO property for looking up the bulk submission object.
     */
    private IBulkFeedbackService mockBulkFeedbackService;

    /**
     * The transformer associated with this handler.
     */
    // CHECKSTYLE:OFF
    private ITransformer<BulkFeedbackRequestType, BulkFeedbackResponseType, IBulkFeedbackRequest, IBulkSubmission> transformer;
    // CHECKSTYLE:ON
    /**
     * The BulkFeedbackRequestType.
     * 
     */
    private BulkFeedbackRequestType bulkFeedbackRequestType;

    /**
     * Bulk Customer to use for the test.
     * 
     * 
     * /**
     * Setup of the mock dao and injection of other objects.
     */
    @SuppressWarnings ("unchecked")
    @Before
    public void setUp ()
    {
        wsReadBulkFeedbackReqHandler = new WsReadBulkFeedbackRequestHandler ();

        mockBulkFeedbackService = EasyMock.createMock (IBulkFeedbackService.class);
        // ITransformer transformer = new BulkFeedbackTransformer();
        Constructor<BulkFeedbackTransformer> c;
        try
        {
            // Make the constructor visible so we can get a new instance of it.
            c = BulkFeedbackTransformer.class.getDeclaredConstructor ();
            c.setAccessible (true);
            transformer = c.newInstance ();
        }
        catch (final InstantiationException e)
        {
            e.printStackTrace ();
        }
        catch (final IllegalAccessException e)
        {
            e.printStackTrace ();
        }
        catch (final InvocationTargetException e)
        {
            e.printStackTrace ();
        }
        catch (final NoSuchMethodException e)
        {
            e.printStackTrace ();
        }

        wsReadBulkFeedbackReqHandler.setBulkFeedbackService (mockBulkFeedbackService);
        wsReadBulkFeedbackReqHandler.setTransformer (transformer);

        bulkFeedbackRequestType = new BulkFeedbackRequestType ();
    }

    /**
     * @throws IOException if there is any issue
     */
    @Test
    @Ignore
    public void testGetBulkFeedback () throws IOException
    {
        final long customerId = 12345678;
        final IBulkFeedbackRequest bulkFeedbackRequestDomain = new BulkFeedbackRequest ();
        final BulkCustomer bulkCustomer = new BulkCustomer ();
        final IBulkSubmission bulkSubmission = new BulkSubmission ();
        // final BulkFeedbackRequestType bulkFeedbackRequest = new BulkFeedbackRequestType();

        bulkCustomer.setSdtCustomerId (customerId);
        bulkCustomer.setBulkCustomerApplications (createBulkCustomerApplications ("MCOL"));
        bulkFeedbackRequestDomain.setBulkCustomer (bulkCustomer);
        bulkFeedbackRequestDomain.setSdtBulkReference ("MCOL-10012013010101-100099999");

        bulkSubmission.setSdtBulkReference ("MCOL-10012013010101-100099999");

        final HeaderType headerType = new HeaderType ();
        headerType.setSdtBulkReference ("MCOL-10012013010101-100099999");
        headerType.setSdtCustomerId ((long) customerId);

        bulkFeedbackRequestType.setHeader (headerType);
        final BulkFeedbackResponseType bulkFeedbackResponseType;

        EasyMock.expect (mockBulkFeedbackService.getBulkFeedback (bulkFeedbackRequestDomain))
                .andReturn (bulkSubmission);
        EasyMock.replay (mockBulkFeedbackService);

        bulkFeedbackResponseType = wsReadBulkFeedbackReqHandler.getBulkFeedback (bulkFeedbackRequestType);

        EasyMock.verify (mockBulkFeedbackService);

    }

    /**
     * the list of applications for a customer.
     * 
     * @param applicationName the application name
     * @return the set of bulk customer applications for this customer
     */
    private Set<IBulkCustomerApplication> createBulkCustomerApplications (final String applicationName)
    {
        final Set<IBulkCustomerApplication> bulkCustomerApplications = new HashSet<IBulkCustomerApplication> ();

        final IBulkCustomerApplication bulkCustomerApp = new BulkCustomerApplication ();
        bulkCustomerApp.setCustomerApplicationId ("appId");
        final ITargetApplication application = new TargetApplication ();
        application.setTargetApplicationCode (applicationName);
        bulkCustomerApp.setTargetApplication (application);
        bulkCustomerApplications.add (bulkCustomerApp);
        return bulkCustomerApplications;
    }

}
