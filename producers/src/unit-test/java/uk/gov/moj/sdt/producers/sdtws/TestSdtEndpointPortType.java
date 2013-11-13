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
 * $Id$
 * $LastChangedRevision$
 * $LastChangedDate$
 * $LastChangedBy$ */
package uk.gov.moj.sdt.producers.sdtws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import uk.gov.moj.sdt.handlers.api.IWsCreateBulkRequestHandler;
import uk.gov.moj.sdt.handlers.api.IWsReadBulkRequestHandler;
import uk.gov.moj.sdt.handlers.api.IWsReadSubmitQueryHandler;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.BulkStatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.BulkStatusType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackrequestschema.BulkFeedbackRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema.BulkFeedbackResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema.BulkRequestStatusType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.BulkRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkresponseschema.BulkResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema.SubmitQueryRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.submitqueryresponseschema.SubmitQueryResponseType;

/**
 * Unit test for {@link SdtEndpointPortType}.
 * 
 * @author d276205
 * 
 */
public class TestSdtEndpointPortType
{

    /**
     * Test subject.
     */
    private SdtEndpointPortType portType;

    /**
     * Mocked IWsCreateBulkRequestHandler instance.
     */
    private IWsCreateBulkRequestHandler mockCreateBulkRequestHandler;

    /**
     * Mocked IWsReadBulkRequestHandler instance.
     */
    private IWsReadBulkRequestHandler mockBulkRequestHandler;

    /**
     * Mocked IWsReadSubmitQueryHandler instance.
     */
    private IWsReadSubmitQueryHandler mockSubmitQueryHandler;

    /**
     * Set up common for all tests.
     */
    @Before
    public void setUp ()
    {
        portType = new SdtEndpointPortType ();

        mockCreateBulkRequestHandler = EasyMock.createMock (IWsCreateBulkRequestHandler.class);
        portType.setWsCreateBulkRequestHandler (mockCreateBulkRequestHandler);

        mockBulkRequestHandler = EasyMock.createMock (IWsReadBulkRequestHandler.class);
        portType.setWsReadBulkRequestHandler (mockBulkRequestHandler);

        mockSubmitQueryHandler = EasyMock.createMock (IWsReadSubmitQueryHandler.class);
        portType.setWsReadSubmitQueryHandler (mockSubmitQueryHandler);
    }

    /**
     * Test submit bulk method completes successfully.
     */
    @Test
    public void testSubmitBulkSuccess ()
    {
        final BulkResponseType dummyResponse = createBulkResponse ();
        final BulkRequestType dummyRequest = createBulkRequest ();

        EasyMock.expect (mockCreateBulkRequestHandler.submitBulk (dummyRequest)).andReturn (dummyResponse);
        EasyMock.replay (mockCreateBulkRequestHandler);

        final BulkResponseType response = portType.submitBulk (dummyRequest);

        EasyMock.verify (mockCreateBulkRequestHandler);
        assertNotNull ("Response expected", response);
    }

    /**
     * Test submit bulk method handles exceptions successfully.
     */
    @Test
    public void testSubmitBulkException ()
    {
        EasyMock.expect (mockCreateBulkRequestHandler.submitBulk (EasyMock.anyObject (BulkRequestType.class)))
                .andThrow (new RuntimeException ("test"));
        EasyMock.replay (mockCreateBulkRequestHandler);

        try
        {
            portType.submitBulk (createBulkRequest ());
            fail ("Runtime exception should have been thrown");
        }
        catch (final RuntimeException re)
        {
            assertEquals ("",
                    "A SDT system component error has occurred. Please contact the SDT support team for assistance",
                    re.getMessage ());
        }

        EasyMock.verify (mockCreateBulkRequestHandler);
    }

    /**
     * Test bulk feedback method completes successfully.
     */
    @Test
    public void testBulkFeedbackSuccess ()
    {
        EasyMock.expect (mockBulkRequestHandler.getBulkFeedback (EasyMock.anyObject (BulkFeedbackRequestType.class)))
                .andReturn (createBulkFeedbackResponse ());
        EasyMock.replay (mockBulkRequestHandler);

        final BulkFeedbackResponseType response = portType.getBulkFeedback (createBulkFeedbackRequestType ());

        assertNotNull ("Response expected", response);
        EasyMock.verify (mockBulkRequestHandler);
    }

    /**
     * Test bulk feedback method handles exceptions successfully.
     */
    @Test
    public void testBulkFeedbackException ()
    {
        EasyMock.expect (mockBulkRequestHandler.getBulkFeedback (EasyMock.anyObject (BulkFeedbackRequestType.class)))
                .andThrow (new RuntimeException ("test"));
        EasyMock.replay (mockBulkRequestHandler);

        try
        {
            portType.getBulkFeedback (createBulkFeedbackRequestType ());
            fail ("Runtime exception should have been thrown");
        }
        catch (final RuntimeException re)
        {
            assertEquals ("",
                    "A SDT system component error has occurred. Please contact the SDT support team for assistance",
                    re.getMessage ());
        }

        EasyMock.verify (mockBulkRequestHandler);
    }

    /**
     * Test submit query method completes successfully.
     */
    @Test
    public void testSubmitQuerySuccess ()
    {
        EasyMock.expect (mockSubmitQueryHandler.submitQuery (EasyMock.anyObject (SubmitQueryRequestType.class)))
                .andReturn (createSubmitQueryResponse ());
        EasyMock.replay (mockSubmitQueryHandler);

        final SubmitQueryResponseType response = portType.submitQuery (createsubmitQueryRequestType ());

        assertNotNull ("Response expected", response);
        EasyMock.verify (mockSubmitQueryHandler);
    }

    /**
     * Test submit query method handles exceptions successfully.
     */
    @Test
    public void testSubmitQueryException ()
    {
        EasyMock.expect (mockSubmitQueryHandler.submitQuery (EasyMock.anyObject (SubmitQueryRequestType.class)))
                .andThrow (new RuntimeException ("test"));
        EasyMock.replay (mockSubmitQueryHandler);

        try
        {
            portType.submitQuery (createsubmitQueryRequestType ());
            fail ("Runtime exception should have been thrown");
        }
        catch (final RuntimeException re)
        {
            assertEquals ("",
                    "A SDT system component error has occurred. Please contact the SDT support team for assistance",
                    re.getMessage ());
        }

        EasyMock.verify (mockSubmitQueryHandler);
    }

    /**
     * Creates dummy bulk submission request.
     * 
     * @return dummy request.
     */
    private BulkRequestType createBulkRequest ()
    {
        final BulkRequestType request = new BulkRequestType ();

        final HeaderType header = new HeaderType ();
        header.setCustomerReference ("1");
        header.setRequestCount (1);
        header.setSdtCustomerId (12345678);
        header.setTargetApplicationId ("mcol");

        request.setHeader (header);
        return request;
    }

    /**
     * Creates dummy bulk submission response.
     * 
     * @return dummy response.
     */
    private BulkResponseType createBulkResponse ()
    {
        final BulkResponseType response = new BulkResponseType ();

        final StatusType statusType = new StatusType ();
        statusType.setCode (StatusCodeType.OK);
        response.setStatus (statusType);
        return response;
    }

    /**
     * Creates dummy request.
     * 
     * @return dummy request.
     */
    private BulkFeedbackRequestType createBulkFeedbackRequestType ()
    {
        final BulkFeedbackRequestType request = new BulkFeedbackRequestType ();

        final uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackrequestschema.HeaderType header =
                new uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackrequestschema.HeaderType ();
        header.setSdtBulkReference ("123");
        header.setSdtCustomerId (12345678);

        request.setHeader (header);
        return request;
    }

    /**
     * Creates dummy bulk feedback response.
     * 
     * @return dummy response.
     */
    private BulkFeedbackResponseType createBulkFeedbackResponse ()
    {
        final BulkFeedbackResponseType response = new BulkFeedbackResponseType ();

        final StatusType statusType = new StatusType ();
        statusType.setCode (StatusCodeType.OK);

        final BulkRequestStatusType bulkRequestStatus = new BulkRequestStatusType ();
        bulkRequestStatus.setCustomerReference ("123");
        bulkRequestStatus.setRequestCount (0L);
        bulkRequestStatus.setSdtBulkReference ("123");
        bulkRequestStatus.setSdtService ("sdt");
        bulkRequestStatus.setStatus (statusType);

        final BulkStatusType bulkStatusType = new BulkStatusType ();
        bulkStatusType.setCode (BulkStatusCodeType.COMPLETED);
        bulkRequestStatus.setBulkStatus (bulkStatusType);

        response.setBulkRequestStatus (bulkRequestStatus);
        return response;
    }

    /**
     * Creates dummy request.
     * 
     * @return dummy request.
     */
    private SubmitQueryRequestType createsubmitQueryRequestType ()
    {
        final SubmitQueryRequestType request = new SubmitQueryRequestType ();

        final uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema.HeaderType header =
                new uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema.HeaderType ();
        header.setTargetApplicationId ("mcol");
        header.setSdtCustomerId (12345678);

        request.setHeader (header);
        return request;
    }

    /**
     * Creates dummy submit query response.
     * 
     * @return dummy response.
     */
    private SubmitQueryResponseType createSubmitQueryResponse ()
    {
        final SubmitQueryResponseType response = new SubmitQueryResponseType ();

        final StatusType statusType = new StatusType ();
        statusType.setCode (StatusCodeType.OK);

        response.setSdtCustomerId (123);
        response.setStatus (statusType);
        response.setSdtService ("sdt");
        return response;
    }

}
