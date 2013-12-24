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
 * $Id: SubmitQueryTest.java 17596 2013-11-06 15:26:54Z agarwals $
 * $LastChangedRevision: 17596 $
 * $LastChangedDate: 2013-11-06 15:26:54 +0000 (Wed, 06 Nov 2013) $
 * $LastChangedBy: agarwals $ */

package uk.gov.moj.sdt.producers.comx;

import javax.xml.bind.JAXBElement;

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
 * 
 */
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (locations = {"classpath*:/uk/gov/moj/sdt/producers/comx/spring*e2e.test.xml",
        "classpath*:/uk/gov/moj/sdt/utils/**/spring*.xml", 
        "classpath*:/uk/gov/moj/sdt/transformers/**/spring*.xml"})
public class SubmitQueryTest extends AbstractWebServiceTest<SubmitQueryRequestType, SubmitQueryResponseType>
{

    /**
     * Method to call remote submit query endpoint to be tested.
     */
    @Test
    public void testValid ()
    {
        this.callWebService (SubmitQueryRequestType.class);
    }

    @Override
    protected SubmitQueryResponseType callTestWebService (final SubmitQueryRequestType request)
    {
        // Get the SOAP proxy client.
        ISdtEndpointPortType client = getSdtEndpointClient ();

        // Call the specific business method for this text - note that a single test can only use one web service
        // business method.
        return client.submitQuery (request);
    }

    @Override
    protected JAXBElement<SubmitQueryResponseType> wrapJaxbObject (final SubmitQueryResponseType response)
    {
        // Use the provided factor to create a wrapped instance of the response.
        ObjectFactory objectFactory = new ObjectFactory ();
        return objectFactory.createSubmitQueryResponse (response);
    }
}
