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
package uk.gov.moj.sdt.transformers;

import java.lang.reflect.Constructor;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.misc.IndividualRequestStatus;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.BulkRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.RequestItemType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.RequestsType;

/**
 * Unit tests for BulkRequestTransformer.
 * 
 * @author d130680
 * 
 */
public class BulkRequestTransformerTest extends TestCase
{

    /**
     * Bulk request transformer.
     */
    private BulkRequestTransformer transformer;

    /**
     * Set up variables for the test.
     */
    @Before
    public void setUp ()
    {
        Constructor<BulkRequestTransformer> c;
        try
        {
            // Make the constructor visible so we can get a new instance of it.
            c = BulkRequestTransformer.class.getDeclaredConstructor ();
            c.setAccessible (true);
            transformer = c.newInstance ();
        }
        catch (final Exception e)
        {
            e.printStackTrace ();
        }

    }

    /**
     * Test the transformation from jaxb to domain object.
     */
    @Test
    public void testTransformJaxbToDomain ()
    {
        // Set up the jaxb object to transform
        final BulkRequestType jaxb = new BulkRequestType ();
        final long sdtCustomerId = 123;
        final String targetApplicationId = "mcol";
        final long resultCount = 20;
        final String customerReference = "A123546";

        // Set up the header
        final HeaderType header = new HeaderType ();
        header.setSdtCustomerId (sdtCustomerId);
        header.setTargetApplicationId (targetApplicationId);
        header.setRequestCount (resultCount);
        header.setCustomerReference (customerReference);
        jaxb.setHeader (header);

        // Set up the individual requests
        final RequestsType requestsType = new RequestsType ();

        // Individual request 1
        final RequestItemType item1 = new RequestItemType ();
        final String item1requestId = "request 1";
        final String item1requestType = "mcolClaimStatusUpdate";
        item1.setRequestId (item1requestId);
        item1.setRequestType (item1requestType);
        requestsType.getRequest ().add (item1);

        // Individual request 2
        final RequestItemType item2 = new RequestItemType ();
        final String item2requestId = "request 2";
        final String item2requestType = "mcolClaim";
        item2.setRequestId (item2requestId);
        item2.setRequestType (item2requestType);
        requestsType.getRequest ().add (item2);

        // Individual request 3
        final RequestItemType item3 = new RequestItemType ();
        final String item3requestId = "request 3";
        final String item3requestType = "mcolWarrant";
        item3.setRequestId (item3requestId);
        item3.setRequestType (item3requestType);
        requestsType.getRequest ().add (item3);

        jaxb.setRequests (requestsType);

        final IBulkSubmission domain = transformer.transformJaxbToDomain (jaxb);

        // Test the jaxb object has been transformed to a domain object
        Assert.assertEquals ("SDT Customer ID does not match", sdtCustomerId, domain.getBulkCustomer ()
                .getSdtCustomerId ());
        Assert.assertEquals ("Target Application ID does not match", targetApplicationId, domain
                .getTargetApplication ().getTargetApplicationCode ());
        Assert.assertEquals ("Individual request list size does not match", requestsType.getRequest ().size (), domain
                .getIndividualRequests ().size ());

        // Extract individual request 1 and assert
        final IIndividualRequest individualRequest1 = domain.getIndividualRequests ().get (0);
        // Test initial status is forwarded
        Assert.assertEquals ("Customer reference does not match", individualRequest1.getRequestStatus (),
                IndividualRequestStatus.FORWARDED.getStatus ());
        Assert.assertEquals ("Request id for individual request 1 does not match",
                individualRequest1.getCustomerRequestReference (), item1requestId);
        Assert.assertEquals ("Line number for individual request 1 does not match",
                individualRequest1.getLineNumber (), 1);

        // Extract individual request 2 and assert
        final IIndividualRequest individualRequest2 = domain.getIndividualRequests ().get (1);
        // Test initial status is forwarded
        Assert.assertEquals ("Customer reference does not match", individualRequest2.getRequestStatus (),
                IndividualRequestStatus.FORWARDED.getStatus ());
        Assert.assertEquals ("Request id for individual request 2 does not match",
                individualRequest2.getCustomerRequestReference (), item2requestId);
        Assert.assertEquals ("Line number for individual request 2 does not match",
                individualRequest2.getLineNumber (), 2);

        // Extract individual request 3 and assert
        final IIndividualRequest individualRequest3 = domain.getIndividualRequests ().get (2);
        // Test initial status is forwarded
        Assert.assertEquals ("Customer reference does not match", individualRequest3.getRequestStatus (),
                IndividualRequestStatus.FORWARDED.getStatus ());
        Assert.assertEquals ("Request id for individual request 3 does not match",
                individualRequest3.getCustomerRequestReference (), item3requestId);
        Assert.assertEquals ("Line number for individual request 3 does not match",
                individualRequest3.getLineNumber (), 3);

    }

    /**
     * Test the from transformation domain to jaxb object.
     */
    @Test
    public void testTransformDomainToJaxb ()
    {
        // TODO - method hasn't been implemented yet
    }
}
