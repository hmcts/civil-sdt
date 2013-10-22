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

package uk.gov.moj.sdt.consumers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.ServiceRouting;
import uk.gov.moj.sdt.domain.ServiceType;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.IServiceRouting;
import uk.gov.moj.sdt.domain.api.IServiceType;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.transformers.api.IConsumerTransformer;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.CreateStatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.CreateStatusType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvrequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvrequestschema.IndividualRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvresponseschema.IndividualResponseType;

/**
 * Test class for the individual request consumer.
 * 
 * @author Manoj Kulkarni
 * 
 */
public class IndividualRequestConsumerTest
{
    /**
     * Logger for debugging.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (IndividualRequestConsumerTest.class);

    /**
     * Consumer transformer for individual request.
     */
    // CHECKSTYLE:OFF
    private IConsumerTransformer<IndividualResponseType, IndividualRequestType, IIndividualRequest, IIndividualRequest> mockTransformer;
    // CHECKSTYLE:ON

    /**
     * Individual Request Consumer instance.
     */
    private IndividualRequestConsumer individualRequestConsumer;

    /**
     * Method to do any pre-test set-up.
     */
    @Before
    public void setUp ()
    {
        this.individualRequestConsumer = EasyMock.createMock (IndividualRequestConsumer.class);

    }

    /**
     * Test method for successful processing of individual request.
     */
    @Test
    public void processIndividualRequestSuccess ()
    {
        final IIndividualRequest individualRequest = this.createIndividualRequest ();
        final IndividualRequestType individualRequesType = this.createRequestType (individualRequest);

        final IndividualResponseType individualResponseType = generateResponse ();

        this.individualRequestConsumer.processIndividualRequest (EasyMock.isA (IIndividualRequest.class),
                EasyMock.anyLong (), EasyMock.anyLong ());

        EasyMock.expectLastCall ();

        EasyMock.replay (individualRequestConsumer);

        individualRequestConsumer.processIndividualRequest (individualRequest, 10000, 10000);

        EasyMock.verify (individualRequestConsumer);

        Assert.assertTrue ("Test finished successfully", true);

    }

    /**
     * 
     * @param domainObject the individual request domain object.
     * @return the Jaxb individual request type.
     */
    private IndividualRequestType createRequestType (final IIndividualRequest domainObject)
    {
        final IndividualRequestType requestType = new IndividualRequestType ();
        final HeaderType headerType = new HeaderType ();
        headerType.setRequestType ("testRequestType");
        headerType.setSdtRequestId (domainObject.getSdtRequestReference ());
        headerType.setTargetAppCustomerId ("TestCust");

        requestType.setHeader (headerType);

        return requestType;
    }

    /**
     * 
     * @return individual request domain object
     */
    private IIndividualRequest createIndividualRequest ()
    {
        final IBulkSubmission bulkSubmission = new BulkSubmission ();
        final IBulkCustomer bulkCustomer = new BulkCustomer ();
        final ITargetApplication targetApp = new TargetApplication ();

        targetApp.setId (1L);
        targetApp.setTargetApplicationCode ("mcol");
        targetApp.setTargetApplicationName ("TEST_TargetApp");
        final Set<IServiceRouting> serviceRoutings = new HashSet<IServiceRouting> ();

        final ServiceRouting serviceRouting = new ServiceRouting ();
        serviceRouting.setId (1L);
        serviceRouting.setWebServiceEndpoint ("MCOL_END_POINT");

        final IServiceType serviceType = new ServiceType ();
        serviceType.setId (1L);
        serviceType.setName (IServiceType.ServiceTypeName.SUBMIT_INDIVIDUAL.name ());
        serviceType.setDescription ("RequestTestDesc1");
        serviceType.setStatus ("RequestTestStatus");

        serviceRouting.setServiceType (serviceType);

        serviceRoutings.add (serviceRouting);

        targetApp.setServiceRoutings (serviceRoutings);

        bulkSubmission.setTargetApplication (targetApp);

        bulkCustomer.setId (1L);
        bulkCustomer.setSdtCustomerId (10L);

        bulkSubmission.setBulkCustomer (bulkCustomer);
        bulkSubmission.setCustomerReference ("TEST_CUST_REF");
        bulkSubmission.setId (1L);
        bulkSubmission.setNumberOfRequest (1);

        final IIndividualRequest individualRequest = new IndividualRequest ();
        individualRequest.setSdtRequestReference ("Test");

        final List<IIndividualRequest> requests = new ArrayList<IIndividualRequest> ();
        requests.add (individualRequest);

        bulkSubmission.setIndividualRequests (requests);

        individualRequest.setBulkSubmission (bulkSubmission);

        return individualRequest;

    }

    /**
     * 
     * @return the individual response type
     */
    private IndividualResponseType generateResponse ()
    {
        final IndividualResponseType responseType = new IndividualResponseType ();

        final uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvresponseschema.HeaderType headerType =
                new uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvresponseschema.HeaderType ();
        headerType.setSdtRequestId ("Test");

        responseType.setHeader (headerType);

        final CreateStatusType status = new CreateStatusType ();
        status.setCode (CreateStatusCodeType.ACCEPTED);

        responseType.setStatus (status);

        return responseType;

    }

}
