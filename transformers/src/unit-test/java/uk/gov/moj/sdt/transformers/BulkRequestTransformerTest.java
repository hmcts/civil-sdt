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
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.IndividualStatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.BulkRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.RequestItemType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.RequestsType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkresponseschema.BulkResponseType;

/**
 * Unit tests for BulkRequestTransformer.
 * 
 * @author d130680
 * 
 */
public class BulkRequestTransformerTest extends AbstractSdtUnitTestBase
{
    /**
     * Bulk request transformer.
     */
    private BulkRequestTransformer transformer;

    /**
     * Set up variables for the test.
     */
    public void setUpLocalTests ()
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
        final String targetApplicationId = "MCOL";
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
        requestsType.getRequest ().add (createRequestItem ("request 1", "mcolClaimStatusUpdate"));

        // Individual request 2
        requestsType.getRequest ().add (createRequestItem ("request 2", "mcolClaim"));

        // Individual request 3
        requestsType.getRequest ().add (createRequestItem ("request 3", "mcolWarrant"));

        jaxb.setRequests (requestsType);

        final IBulkSubmission domain = transformer.transformJaxbToDomain (jaxb);

        // Test the jaxb object has been transformed to a domain object
        Assert.assertEquals ("SDT Customer ID does not match", sdtCustomerId, domain.getBulkCustomer ()
                .getSdtCustomerId ());
        Assert.assertEquals ("Target Application ID does not match", targetApplicationId, domain
                .getTargetApplication ().getTargetApplicationCode ());
        Assert.assertEquals ("Individual request list size does not match", requestsType.getRequest ().size (), domain
                .getIndividualRequests ().size ());

        int index = 0;
        for (RequestItemType item : requestsType.getRequest ())
        {
            verify (item, domain.getIndividualRequests ().get (index), ++index);
        }

    }

    /**
     * Creates an instance of RequestItemType with given values.
     * 
     * @param id request id
     * @param type request type
     * @return RequestItemType
     */
    private RequestItemType createRequestItem (final String id, final String type)
    {
        final RequestItemType requestItem = new RequestItemType ();
        requestItem.setRequestId (id);
        requestItem.setRequestType (type);
        return requestItem;
    }

    /**
     * Verifies that individual request contains expected values.
     * 
     * @param expected request item type
     * @param actual individual request
     * @param row record number
     */
    private void verify (final RequestItemType expected, final IIndividualRequest actual, final int row)
    {
        Assert.assertNotNull (actual.getBulkSubmission ());
        Assert.assertEquals ("Customer reference does not match",
                IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus (), actual.getRequestStatus ());
        Assert.assertEquals ("Request id for individual request " + row + " does not match", expected.getRequestId (),
                actual.getCustomerRequestReference ());
        Assert.assertEquals ("Line number for individual request " + row + " does not match", row,
                actual.getLineNumber ());
        Assert.assertEquals ("Request type mismatch", expected.getRequestType (), actual.getRequestType ());
    }

    /**
     * Test the from transformation domain to jaxb object.
     */
    @Test
    public void testTransformDomainToJaxb ()
    {
        // Set up the domain object to transform
        final IBulkSubmission domain = new BulkSubmission ();
        final long numberOfRequest = 8;
        final String sdtBulkReference = "A123456789";
        final String customerRef = "C10000123";
        final LocalDateTime createdDate = new LocalDateTime ();
        final String submissionStatus = "Uploaded";

        domain.setNumberOfRequest (numberOfRequest);
        domain.setSdtBulkReference (sdtBulkReference);
        domain.setCustomerReference (customerRef);
        domain.setCreatedDate (createdDate);
        domain.setSubmissionStatus (submissionStatus);

        // Setup some individual requests
        final List<IIndividualRequest> individualRequests = new ArrayList<IIndividualRequest> ();
        final String customerRequestReference1 = "request 1";
        IndividualRequest ir = new IndividualRequest ();
        ir.setCustomerRequestReference (customerRequestReference1);
        ir.setRequestStatus (IndividualStatusCodeType.ACCEPTED.value ());
        individualRequests.add (ir);

        ir = new IndividualRequest ();
        final String customerRequestReference2 = "request 2";
        ir.setCustomerRequestReference (customerRequestReference2);
        ir.setRequestStatus (IndividualStatusCodeType.RECEIVED.value ());
        individualRequests.add (ir);

        // Set one up as rejected so an error log can be created
        ir = new IndividualRequest ();
        final String customerRequestReference3 = "request 3";
        ir.setCustomerRequestReference (customerRequestReference3);
        ir.setRequestStatus (IndividualStatusCodeType.REJECTED.value ());

        // Set the error log and message
        final String errorCode = "87";
        final String errorText = "Specified claim does not belong to the requesting customer.";
        final ErrorLog errorLog = new ErrorLog (errorCode, errorText);
        ir.setErrorLog (errorLog);
        individualRequests.add (ir);

        domain.setIndividualRequests (individualRequests);

        final BulkResponseType jaxb = transformer.transformDomainToJaxb (domain);

        Assert.assertEquals ("The Sdt Bulk Reference is as expected", domain.getSdtBulkReference (),
                jaxb.getSdtBulkReference ());
        Assert.assertEquals ("The customer reference is as expected", domain.getCustomerReference (),
                jaxb.getCustomerReference ());
        Assert.assertEquals ("The Sdt Service is as expected", AbstractTransformer.SDT_SERVICE, jaxb.getSdtService ());
        Assert.assertEquals ("The number of requests are as expected", jaxb.getRequestCount (),
                domain.getNumberOfRequest ());
        Assert.assertNotNull ("The submitted date is found", jaxb.getSubmittedDate ());

        Assert.assertEquals ("The status code is as expected", jaxb.getStatus ().getCode ().value (), "Ok");
    }
}
