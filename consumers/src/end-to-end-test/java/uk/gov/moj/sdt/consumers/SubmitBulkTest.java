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
 * $Id: ClaimXsdTest.java 16414 2013-05-29 10:56:45Z agarwals $
 * $LastChangedRevision: 16414 $
 * $LastChangedDate: 2013-05-29 11:56:45 +0100 (Wed, 29 May 2013) $
 * $LastChangedBy: holmessm $ */

package uk.gov.moj.sdt.consumers;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.BindingProvider;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.moj.sdt.utils.SpringApplicationContext;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.BulkRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkresponseschema.BulkResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkresponseschema.ObjectFactory;
import uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType;

/**
 * Test class for end to end web service tests..
 * 
 * @author Robin Compston
 * 
 */
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (locations = {"classpath*:uk/gov/moj/sdt/consumers/spring*e2e.test.xml",
        "classpath*:uk/gov/moj/sdt/utils/spring*.xml", "classpath*:uk/gov/moj/sdt/transformers/spring*.xml"})
public class SubmitBulkTest extends AbstractWebServiceTest<BulkRequestType, BulkResponseType>
{

    /**
     * Method to call remote submit bulk endpoint to be tested.
     */
    @Test
    public void testValid ()
    {
        this.callWebService (BulkRequestType.class);
    }

    /**
     * Method to call remote submit bulk endpoint to be tested.
     */
    @Test
    public void testCountMismatch ()
    {
        this.callWebService (BulkRequestType.class);
    }

    @Override
    protected BulkResponseType callTestWebService (final BulkRequestType request)
    {
        // Get the SOAP proxy client.
        ISdtEndpointPortType client =
                (ISdtEndpointPortType) SpringApplicationContext
                        .getBean ("uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType");

        Client clientProxy = ClientProxy.getClient (client);

        // Set endpoint address
        BindingProvider provider = (BindingProvider) client;
        provider.getRequestContext ().put (BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                "http://localhost:8888/producers-comx/service/sdtapi");

        HTTPConduit httpConduit = (HTTPConduit) clientProxy.getConduit ();
        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy ();
        // Specifies the amount of time, in milliseconds, that the client will attempt to establish a connection before
        // it times out
        httpClientPolicy.setConnectionTimeout (10000);
        // Specifies the amount of time, in milliseconds, that the client will wait for a response before it times out.
        httpClientPolicy.setReceiveTimeout (10000);
        httpConduit.setClient (httpClientPolicy);

        // Call the specific business method for this text - note that a single test can only use one web service
        // business method.
        return client.submitBulk (request);
    }

    @Override
    protected JAXBElement<BulkResponseType> wrapJaxbObject (final BulkResponseType response)
    {
        // Use the provided factor to create a wrapped instance of the response.
        ObjectFactory objectFactory = new ObjectFactory ();
        return objectFactory.createBulkResponse (response);
    }
}
