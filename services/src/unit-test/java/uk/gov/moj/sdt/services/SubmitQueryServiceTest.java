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

package uk.gov.moj.sdt.services;

import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.consumers.api.IConsumerGateway;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkCustomerApplication;
import uk.gov.moj.sdt.domain.ErrorMessage;
import uk.gov.moj.sdt.domain.GlobalParameter;
import uk.gov.moj.sdt.domain.ServiceRouting;
import uk.gov.moj.sdt.domain.ServiceType;
import uk.gov.moj.sdt.domain.SubmitQueryRequest;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkCustomerApplication;
import uk.gov.moj.sdt.domain.api.IErrorMessage;
import uk.gov.moj.sdt.domain.api.IGlobalParameter;
import uk.gov.moj.sdt.domain.api.IServiceRouting;
import uk.gov.moj.sdt.domain.api.IServiceType;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.utils.GenericXmlParser;
import uk.gov.moj.sdt.utils.SdtContext;

/**
 * Test class for SubmitQueryService.
 * 
 * @author 274994
 * 
 */
public class SubmitQueryServiceTest
{
    /**
     * Logger for debugging.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (SubmitQueryServiceTest.class);

    /**
     * Submit Query Service object.
     */
    private SubmitQueryService submitQueryService;

    /**
     * Mocked consumer gateway object.
     */
    private IConsumerGateway mockConsumerGateway;

    /**
     * The mocked ICacheable reference for global parameters cache.
     */
    private ICacheable mockGlobalParamCache;

    /**
     * The mocked ICacheable reference to the error message cache.
     */
    private ICacheable mockErrorMsgCacheable;

    /**
     * The mocked bulk customer dao reference.
     */
    private IBulkCustomerDao mockBulkCustomerDao;

    /**
     * Method to do any pre-test set-up.
     */
    @Before
    public void setUp ()
    {
        submitQueryService = new SubmitQueryService ();

        // Instantiate all the mocked objects and set them in the target

        mockConsumerGateway = EasyMock.createMock (IConsumerGateway.class);
        submitQueryService.setRequestConsumer (mockConsumerGateway);

        mockGlobalParamCache = EasyMock.createMock (ICacheable.class);
        submitQueryService.setGlobalParametersCache (mockGlobalParamCache);

        mockErrorMsgCacheable = EasyMock.createMock (ICacheable.class);
        submitQueryService.setErrorMessagesCache (mockErrorMsgCacheable);

        mockBulkCustomerDao = EasyMock.createMock (IBulkCustomerDao.class);
        submitQueryService.setBulkCustomerDao (mockBulkCustomerDao);

        final GenericXmlParser genericParser = new GenericXmlParser ();
        genericParser.setEnclosingTag ("targetAppDetail");

        submitQueryService.setQueryRequestXmlParser (genericParser);
    }

    /**
     * Test method to test for the time out.
     */
    @SuppressWarnings ("unchecked")
    @Test
    public void processRequestToSubmitTimeOut ()
    {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest ();

        setUpSubmitQueryRequest (submitQueryRequest);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter ();
        connectionTimeOutParam.setName ("TARGET_APP_TIMEOUT");
        connectionTimeOutParam.setValue ("1000");
        EasyMock.expect (this.mockGlobalParamCache.getValue (IGlobalParameter.class, "TARGET_APP_TIMEOUT")).andReturn (
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter ();
        receiveTimeOutParam.setName ("TARGET_APP_RESP_TIMEOUT");
        receiveTimeOutParam.setValue ("12000");
        EasyMock.expect (this.mockGlobalParamCache.getValue (IGlobalParameter.class, "TARGET_APP_RESP_TIMEOUT"))
                .andReturn (receiveTimeOutParam);

        final IGlobalParameter maxQueryReq = new GlobalParameter ();
        maxQueryReq.setName ("TEST_TARGETAPP_MAX_CONCURRENT_QUERY_REQ");
        maxQueryReq.setValue ("5");
        EasyMock.expect (
                this.mockGlobalParamCache.getValue (IGlobalParameter.class, "TEST_TARGETAPP_MAX_CONCURRENT_QUERY_REQ"))
                .andReturn (maxQueryReq);

        final TimeoutException timeoutEx = new TimeoutException ("TIMEOUT_ERROR", "Timeout occurred");
        this.mockConsumerGateway.submitQuery (submitQueryRequest, 1000, 12000);
        EasyMock.expectLastCall ().andThrow (timeoutEx);

        final IErrorMessage errorMsg = new ErrorMessage ();
        errorMsg.setErrorCode ("TAR_APP_ERROR");
        errorMsg.setErrorDescription ("Request timed out");
        errorMsg.setErrorText ("The system encountered a problem.");

        EasyMock.expect (this.mockErrorMsgCacheable.getValue (IErrorMessage.class, "TAR_APP_ERROR")).andReturn (
                errorMsg);

        EasyMock.replay (mockConsumerGateway);
        EasyMock.replay (mockGlobalParamCache);
        EasyMock.replay (mockErrorMsgCacheable);
        EasyMock.replay (mockBulkCustomerDao);

        SdtContext.getContext ().setRawInXml ("response");
        this.submitQueryService.submitQuery (submitQueryRequest);

        EasyMock.verify (mockConsumerGateway);
        EasyMock.verify (mockGlobalParamCache);
        EasyMock.verify (mockErrorMsgCacheable);
        EasyMock.verify (mockBulkCustomerDao);

        Assert.assertTrue ("Expected to pass", true);
    }

    /**
     * Set up a valid submit query request object.
     * 
     * @param submitQueryRequest test object for submit query.
     */
    private void setUpSubmitQueryRequest (final ISubmitQueryRequest submitQueryRequest)
    {
        final long sdtCustomerId = 10L;
        final IBulkCustomer bulkCustomer = new BulkCustomer ();
        bulkCustomer.setId (1L);
        bulkCustomer.setSdtCustomerId (10L);

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
        serviceType.setName ("RequestTest1");
        serviceType.setDescription ("RequestTestDesc1");
        serviceType.setStatus ("RequestTestStatus");

        serviceRouting.setServiceType (serviceType);
        serviceRoutings.add (serviceRouting);
        targetApp.setServiceRoutings (serviceRoutings);

        final Set<IBulkCustomerApplication> bulkCustomerApplications = new HashSet<IBulkCustomerApplication> ();
        final BulkCustomerApplication bulkCustomerApplication = new BulkCustomerApplication ();
        bulkCustomerApplication.setBulkCustomer (bulkCustomer);
        bulkCustomerApplication.setTargetApplication (targetApp);
        bulkCustomerApplications.add (bulkCustomerApplication);

        bulkCustomer.setBulkCustomerApplications (bulkCustomerApplications);

        EasyMock.expect (this.mockBulkCustomerDao.getBulkCustomerBySdtId (sdtCustomerId)).andReturn (bulkCustomer);

        final IBulkCustomer inBulkCustomer = new BulkCustomer ();
        inBulkCustomer.setSdtCustomerId (10L);

        final ITargetApplication inTargetApp = new TargetApplication ();
        inTargetApp.setTargetApplicationCode ("mcol");

        submitQueryRequest.setTargetApplication (inTargetApp);
        submitQueryRequest.setBulkCustomer (inBulkCustomer);

    }

}
