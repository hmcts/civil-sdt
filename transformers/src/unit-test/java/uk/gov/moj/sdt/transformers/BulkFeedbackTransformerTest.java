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

import junit.framework.Assert;
import junit.framework.TestCase;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IBulkFeedbackRequest;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.ErrorType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.IndividualStatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.IndividualStatusType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackrequestschema.BulkFeedbackRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackrequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema.BulkFeedbackResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema.ResponseType;

/**
 * Unit tests for BulkRequestTransformer.
 * 
 * @author d130680
 * 
 */
public class BulkFeedbackTransformerTest extends TestCase
{

    /**
     * Bulk feedback transformer.
     */
    private BulkFeedbackTransformer transformer;

    /**
     * Set up variables for the test.
     */
    @Before
    public void setUp ()
    {
        Constructor<BulkFeedbackTransformer> c;
        try
        {
            // Make the constructor visible so we can get a new instance of it.
            c = BulkFeedbackTransformer.class.getDeclaredConstructor ();
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
        final BulkFeedbackRequestType jaxb = new BulkFeedbackRequestType ();
        final long sdtCustomerId = 123;
        final String sdtBulkReference = "A123456";

        // Set up the header
        final HeaderType header = new HeaderType ();
        header.setSdtBulkReference (sdtBulkReference);
        header.setSdtCustomerId (sdtCustomerId);
        jaxb.setHeader (header);

        // Do the transformation
        final IBulkFeedbackRequest domain = transformer.transformJaxbToDomain (jaxb);
        Assert.assertEquals ("SDT Customer ID does not match", sdtCustomerId, domain.getBulkCustomer ()
                .getSdtCustomerId ());
        Assert.assertEquals ("SDT Bulk Reference does not match", sdtBulkReference, domain.getSdtBulkReference ());

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

        final BulkFeedbackResponseType jaxb = transformer.transformDomainToJaxb (domain);

        // Check the domain object has been transformed
        Assert.assertEquals ("The number of request does not match", numberOfRequest, domain.getNumberOfRequest ());
        Assert.assertEquals ("The SDT Bulk Reference does not match", sdtBulkReference, domain.getSdtBulkReference ());
        Assert.assertEquals ("The Customer Reference does not match", customerRef, domain.getCustomerReference ());
        Assert.assertEquals ("The created date does not match", createdDate, domain.getCreatedDate ());
        Assert.assertEquals ("The submission status does not match", submissionStatus, domain.getSubmissionStatus ());

        final List<ResponseType> responseTypes = jaxb.getResponses ().getResponse ();
        // Individual request 1
        ResponseType responseType = responseTypes.get (0);
        Assert.assertEquals ("Request ID for individual request 1 does not match", customerRequestReference1,
                responseType.getRequestId ());
        Assert.assertEquals ("Status for individual request 1 does not match",
                IndividualStatusCodeType.ACCEPTED.value (), responseType.getStatus ().getCode ().value ());

        // Individual request 2
        responseType = responseTypes.get (1);
        Assert.assertEquals ("Request ID for individual request 2 does not match", customerRequestReference2,
                responseType.getRequestId ());
        Assert.assertEquals ("Status for individual request 2 does not match",
                IndividualStatusCodeType.RECEIVED.value (), responseType.getStatus ().getCode ().value ());

        // Individual request 3
        responseType = responseTypes.get (2);
        Assert.assertEquals ("Request ID for individual request 3 does not match", customerRequestReference3,
                responseType.getRequestId ());
        Assert.assertEquals ("Status for individual request 3 does not match",
                IndividualStatusCodeType.REJECTED.value (), responseType.getStatus ().getCode ().value ());

        // Check for the errors
        final IndividualStatusType individualStatusType = responseType.getStatus ();
        Assert.assertEquals ("Error status for individual request 3 does not match",
                IndividualStatusCodeType.REJECTED.value (), individualStatusType.getCode ().value ());
        final ErrorType errorType = individualStatusType.getError ();
        Assert.assertEquals ("Error code for individual request 3 does not match", errorCode, errorType.getCode ());
        Assert.assertEquals ("Error text for individual request 3 does not match", errorText,
                errorType.getDescription ());
    }
}
