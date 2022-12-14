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

package uk.gov.moj.sdt.producers;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType;
import uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema.SubmitQueryRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.submitqueryresponseschema.ObjectFactory;
import uk.gov.moj.sdt.ws._2013.sdt.submitqueryresponseschema.SubmitQueryResponseType;

/**
 * Test class for end to end web service tests..
 *
 * @author Robin Compston
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/uk/gov/moj/sdt/producers/spring*e2e.test.xml",
        "classpath*:/uk/gov/moj/sdt/utils/**/spring*.xml", "classpath*:/uk/gov/moj/sdt/transformers/**/spring*.xml"})
public class SubmitQueryTest extends AbstractWebServiceTest<SubmitQueryRequestType, SubmitQueryResponseType> {

    /**
     * Method to call remote submit query endpoint to be tested.
     */
    @Test
    @Ignore
    public void testValid() {
        this.callWebService(SubmitQueryRequestType.class);
    }

    /**
     * Method to call remote submit query endpoint to be tested.
     */
    @Test
    public void testErrorResult() {
        try {
            this.callWebService(SubmitQueryRequestType.class);
        } catch (SOAPFaultException e) {
            Assert.assertTrue("Unexpected exception message in SOAPFaultException [" + e.getMessage() + "]", e
                    .getMessage().contains("The content of element 'criterion' is not complete"));
        }
    }

    /**
     * Scenario - Invalid SDT Customer details.
     */
    @Test
    public void testInvalidCustomer() {
        this.callWebService(SubmitQueryRequestType.class);
    }

    /**
     * Scenario - Customer does not have access to target application.
     */
    @Test
    public void testInvalidTargetApp() {
        this.callWebService(SubmitQueryRequestType.class);
    }

    @Override
    protected SubmitQueryResponseType callTestWebService(final SubmitQueryRequestType request) {
        // Get the SOAP proxy client.
        ISdtEndpointPortType client = getSdtEndpointClient();

        // Call the specific business method for this text - note that a single test can only use one web service
        // business method.
        return client.submitQuery(request);
    }

    @Override
    protected JAXBElement<SubmitQueryResponseType> wrapJaxbObject(final SubmitQueryResponseType response) {
        // Use the provided factor to create a wrapped instance of the response.
        ObjectFactory objectFactory = new ObjectFactory();
        return objectFactory.createSubmitQueryResponse(response);
    }
}
