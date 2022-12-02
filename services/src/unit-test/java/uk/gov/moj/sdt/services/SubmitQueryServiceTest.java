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

import javax.xml.ws.WebServiceException;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Qualifier;
import uk.gov.moj.sdt.consumers.api.IConsumerGateway;
import uk.gov.moj.sdt.consumers.exception.OutageException;
import uk.gov.moj.sdt.consumers.exception.SoapFaultException;
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
import uk.gov.moj.sdt.services.utils.GenericXmlParser;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.SdtContext;

/**
 * Test class for SubmitQueryService.
 *
 * @author 274994
 */
public class SubmitQueryServiceTest extends AbstractSdtUnitTestBase {
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
    public void setUp() {

        // Instantiate all the mocked objects and set them in the target

        mockConsumerGateway = EasyMock.createMock(IConsumerGateway.class);
        mockGlobalParamCache = EasyMock.createMock(ICacheable.class);
        mockErrorMsgCacheable = EasyMock.createMock(ICacheable.class);
        mockBulkCustomerDao = EasyMock.createMock(IBulkCustomerDao.class);

        final GenericXmlParser genericParser = new GenericXmlParser();
        genericParser.setEnclosingTag("targetAppDetail");

        submitQueryService = new SubmitQueryService(mockConsumerGateway,
                                                    mockGlobalParamCache,
                                                    mockErrorMsgCacheable,
                                                    genericParser,
                                                    genericParser,
                                                    mockBulkCustomerDao);
        submitQueryService.setBulkCustomerDao(mockBulkCustomerDao);

    }

    /**
     * Unit test method to test for request timed out.
     */
    @Test
    public void testSubmitQueryRequestTimeout() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        setUpSubmitQueryRequest(submitQueryRequest);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName("TARGET_APP_TIMEOUT");
        connectionTimeOutParam.setValue("1000");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "TARGET_APP_TIMEOUT")).andReturn(
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName("TARGET_APP_RESP_TIMEOUT");
        receiveTimeOutParam.setValue("12000");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "TARGET_APP_RESP_TIMEOUT"))
                .andReturn(receiveTimeOutParam);

        final IGlobalParameter maxQueryReq = new GlobalParameter();
        maxQueryReq.setName("MCOL_MAX_CONCURRENT_QUERY_REQ");
        maxQueryReq.setValue("5");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "MCOL_MAX_CONCURRENT_QUERY_REQ"))
                .andReturn(maxQueryReq);

        final IGlobalParameter contactDetails = new GlobalParameter();
        contactDetails.setName("CONTACT_DETAILS");
        contactDetails.setValue("SDT Team");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "CONTACT_DETAILS")).andReturn(
                contactDetails);

        final TimeoutException timeoutEx = new TimeoutException("TIMEOUT_ERROR", "Timeout occurred");
        this.mockConsumerGateway.submitQuery(submitQueryRequest, 1000, 12000);
        EasyMock.expectLastCall().andThrow(timeoutEx);

        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode("TAR_APP_ERROR");
        errorMsg.setErrorDescription("Request timed out");
        errorMsg.setErrorText("The system encountered a problem.");

        EasyMock.expect(this.mockErrorMsgCacheable.getValue(IErrorMessage.class, "TAR_APP_ERROR")).andReturn(
                errorMsg);

        EasyMock.replay(mockConsumerGateway);
        EasyMock.replay(mockGlobalParamCache);
        EasyMock.replay(mockErrorMsgCacheable);
        EasyMock.replay(mockBulkCustomerDao);

        SdtContext.getContext().setRawInXml("criteria");
        this.submitQueryService.submitQuery(submitQueryRequest);

        EasyMock.verify(mockConsumerGateway);
        EasyMock.verify(mockGlobalParamCache);
        EasyMock.verify(mockErrorMsgCacheable);
        EasyMock.verify(mockBulkCustomerDao);

        Assert.assertEquals("Raw output xml should be null", null, SdtContext.getContext().getRawOutXml());

        Assert.assertTrue("Expected to pass", true);
    }

    /**
     * Unit test method to test server outage exception.
     */
    @Test
    public void testSubmitQueryRequestOutage() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        setUpSubmitQueryRequest(submitQueryRequest);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName("TARGET_APP_TIMEOUT");
        connectionTimeOutParam.setValue("1000");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "TARGET_APP_TIMEOUT")).andReturn(
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName("TARGET_APP_RESP_TIMEOUT");
        receiveTimeOutParam.setValue("12000");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "TARGET_APP_RESP_TIMEOUT"))
                .andReturn(receiveTimeOutParam);

        final IGlobalParameter maxQueryReq = new GlobalParameter();
        maxQueryReq.setName("MCOL_MAX_CONCURRENT_QUERY_REQ");
        maxQueryReq.setValue("5");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "MCOL_MAX_CONCURRENT_QUERY_REQ"))
                .andReturn(maxQueryReq);

        final IGlobalParameter contactDetails = new GlobalParameter();
        contactDetails.setName("CONTACT_DETAILS");
        contactDetails.setValue("SDT Team");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "CONTACT_DETAILS")).andReturn(
                contactDetails);

        final OutageException outageEx = new OutageException("OUTAGE_ERROR", "Server unavailable.");
        this.mockConsumerGateway.submitQuery(submitQueryRequest, 1000, 12000);
        EasyMock.expectLastCall().andThrow(outageEx);

        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode("TAR_APP_ERROR");
        errorMsg.setErrorDescription("Server unavailable.");
        errorMsg.setErrorText("The system encountered a problem.");

        EasyMock.expect(this.mockErrorMsgCacheable.getValue(IErrorMessage.class, "TAR_APP_ERROR")).andReturn(
                errorMsg);

        EasyMock.replay(mockConsumerGateway);
        EasyMock.replay(mockGlobalParamCache);
        EasyMock.replay(mockErrorMsgCacheable);
        EasyMock.replay(mockBulkCustomerDao);

        SdtContext.getContext().setRawInXml("criteria");
        this.submitQueryService.submitQuery(submitQueryRequest);

        EasyMock.verify(mockConsumerGateway);
        EasyMock.verify(mockGlobalParamCache);
        EasyMock.verify(mockErrorMsgCacheable);
        EasyMock.verify(mockBulkCustomerDao);

        Assert.assertEquals("Raw output xml should be null", null, SdtContext.getContext().getRawOutXml());

        Assert.assertTrue("Expected to pass", true);
    }

    /**
     * Unit test method to test server soap fault exception.
     */
    @Test
    public void testSubmitQueryRequestSoapFault() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        setUpSubmitQueryRequest(submitQueryRequest);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName("TARGET_APP_TIMEOUT");
        connectionTimeOutParam.setValue("1000");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "TARGET_APP_TIMEOUT")).andReturn(
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName("TARGET_APP_RESP_TIMEOUT");
        receiveTimeOutParam.setValue("12000");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "TARGET_APP_RESP_TIMEOUT"))
                .andReturn(receiveTimeOutParam);

        final IGlobalParameter maxQueryReq = new GlobalParameter();
        maxQueryReq.setName("MCOL_MAX_CONCURRENT_QUERY_REQ");
        maxQueryReq.setValue("5");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "MCOL_MAX_CONCURRENT_QUERY_REQ"))
                .andReturn(maxQueryReq);

        final SoapFaultException soapFaultEx = new SoapFaultException("SOAPFAULT_ERROR", "Soap fault occurred");
        this.mockConsumerGateway.submitQuery(submitQueryRequest, 1000, 12000);
        EasyMock.expectLastCall().andThrow(soapFaultEx);

        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode("SDT_INT_ERR");
        errorMsg.setErrorDescription("SDT Internal Error.");
        errorMsg.setErrorText("SDT Internal Error, please report to {0}");
        EasyMock.expect(this.mockErrorMsgCacheable.getValue(IErrorMessage.class, "SDT_INT_ERR")).andReturn(errorMsg);

        final IGlobalParameter contactNameParameter = new GlobalParameter();
        contactNameParameter.setValue("Tester");
        contactNameParameter.setName("CONTACT_DETAILS");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "CONTACT_DETAILS")).andReturn(
                contactNameParameter);

        EasyMock.replay(mockConsumerGateway);
        EasyMock.replay(mockGlobalParamCache);
        EasyMock.replay(mockErrorMsgCacheable);
        EasyMock.replay(mockBulkCustomerDao);

        SdtContext.getContext().setRawInXml("criteria");
        this.submitQueryService.submitQuery(submitQueryRequest);

        EasyMock.verify(mockConsumerGateway);
        EasyMock.verify(mockGlobalParamCache);
        EasyMock.verify(mockErrorMsgCacheable);
        EasyMock.verify(mockBulkCustomerDao);

        Assert.assertEquals("Raw output xml should be null", null, SdtContext.getContext().getRawOutXml());

        Assert.assertTrue("Expected to pass", true);

        Assert.assertEquals("SDT Internal Error, please report to Tester", submitQueryRequest.getErrorLog()
                .getErrorText());
    }

    /**
     * Unit test method to test web service exception.
     */
    @Test
    public void testSubmitQueryRequestForWebServiceException() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        setUpSubmitQueryRequest(submitQueryRequest);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName("TARGET_APP_TIMEOUT");
        connectionTimeOutParam.setValue("1000");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "TARGET_APP_TIMEOUT")).andReturn(
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName("TARGET_APP_RESP_TIMEOUT");
        receiveTimeOutParam.setValue("12000");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "TARGET_APP_RESP_TIMEOUT"))
                .andReturn(receiveTimeOutParam);

        final IGlobalParameter maxQueryReq = new GlobalParameter();
        maxQueryReq.setName("MCOL_MAX_CONCURRENT_QUERY_REQ");
        maxQueryReq.setValue("5");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "MCOL_MAX_CONCURRENT_QUERY_REQ"))
                .andReturn(maxQueryReq);

        final WebServiceException wsException = new WebServiceException("WS_ERROR");
        this.mockConsumerGateway.submitQuery(submitQueryRequest, 1000, 12000);
        EasyMock.expectLastCall().andThrow(wsException);

        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode("SDT_INT_ERR");
        errorMsg.setErrorDescription("SDT Internal Error.");
        errorMsg.setErrorText("SDT Internal Error, please report to {0}");
        EasyMock.expect(this.mockErrorMsgCacheable.getValue(IErrorMessage.class, "SDT_INT_ERR")).andReturn(errorMsg);

        final IGlobalParameter contactNameParameter = new GlobalParameter();
        contactNameParameter.setValue("Tester");
        contactNameParameter.setName("CONTACT_DETAILS");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "CONTACT_DETAILS")).andReturn(
                contactNameParameter);

        EasyMock.replay(mockConsumerGateway);
        EasyMock.replay(mockGlobalParamCache);
        EasyMock.replay(mockErrorMsgCacheable);
        EasyMock.replay(mockBulkCustomerDao);

        SdtContext.getContext().setRawInXml("criteria");
        this.submitQueryService.submitQuery(submitQueryRequest);

        EasyMock.verify(mockConsumerGateway);
        EasyMock.verify(mockGlobalParamCache);
        EasyMock.verify(mockErrorMsgCacheable);
        EasyMock.verify(mockBulkCustomerDao);

        Assert.assertEquals("Raw output xml should be null", null, SdtContext.getContext().getRawOutXml());

        Assert.assertTrue("Expected to pass", true);

        Assert.assertEquals("SDT Internal Error, please report to Tester", submitQueryRequest.getErrorLog()
                .getErrorText());
    }

    /**
     * Unit test method to test server soap fault exception.
     */
    @Test
    public void testSubmitQueryRequestThrottling() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        setUpSubmitQueryRequest(submitQueryRequest);

        final IGlobalParameter maxQueryReq = new GlobalParameter();
        maxQueryReq.setName("MCOL_MAX_CONCURRENT_QUERY_REQ");
        maxQueryReq.setValue("0");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "MCOL_MAX_CONCURRENT_QUERY_REQ"))
                .andReturn(maxQueryReq);

        final IErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setErrorCode("TAR_APP_BUSY");
        errorMsg.setErrorDescription("Target Application Busy.");
        errorMsg.setErrorText("Target Application Busy.");
        EasyMock.expect(this.mockErrorMsgCacheable.getValue(IErrorMessage.class, "TAR_APP_BUSY"))
                .andReturn(errorMsg);

        EasyMock.replay(mockConsumerGateway);
        EasyMock.replay(mockGlobalParamCache);
        EasyMock.replay(mockErrorMsgCacheable);
        EasyMock.replay(mockBulkCustomerDao);

        SdtContext.getContext().setRawInXml("criteria");
        this.submitQueryService.submitQuery(submitQueryRequest);

        EasyMock.verify(mockConsumerGateway);
        EasyMock.verify(mockGlobalParamCache);
        EasyMock.verify(mockErrorMsgCacheable);
        EasyMock.verify(mockBulkCustomerDao);

        Assert.assertEquals("Raw output xml should be null", null, SdtContext.getContext().getRawOutXml());

        Assert.assertTrue("Expected to pass", true);
    }

    /**
     * Unit test method to test submit query request success.
     */
    @Test
    public void testSubmitQueryServiceSuccess() {
        final ISubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();

        setUpSubmitQueryRequest(submitQueryRequest);

        final IGlobalParameter connectionTimeOutParam = new GlobalParameter();
        connectionTimeOutParam.setName("TARGET_APP_TIMEOUT");
        connectionTimeOutParam.setValue("1000");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "TARGET_APP_TIMEOUT")).andReturn(
                connectionTimeOutParam);

        final IGlobalParameter receiveTimeOutParam = new GlobalParameter();
        receiveTimeOutParam.setName("TARGET_APP_RESP_TIMEOUT");
        receiveTimeOutParam.setValue("12000");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "TARGET_APP_RESP_TIMEOUT"))
                .andReturn(receiveTimeOutParam);

        final IGlobalParameter maxQueryReq = new GlobalParameter();
        maxQueryReq.setName("MCOL_MAX_CONCURRENT_QUERY_REQ");
        maxQueryReq.setValue("5");
        EasyMock.expect(this.mockGlobalParamCache.getValue(IGlobalParameter.class, "MCOL_MAX_CONCURRENT_QUERY_REQ"))
                .andReturn(maxQueryReq);

        this.mockConsumerGateway.submitQuery(submitQueryRequest, 1000, 12000);
        EasyMock.expectLastCall().andAnswer(new IAnswer<Object>() {
            @Override
            public Object answer() throws Throwable {
                ((SubmitQueryRequest) EasyMock.getCurrentArguments()[0]).setStatus(ISubmitQueryRequest.Status.OK
                        .getStatus());
                // required to be null for a void method
                return null;
            }
        });

        EasyMock.replay(mockConsumerGateway);
        EasyMock.replay(mockGlobalParamCache);
        EasyMock.replay(mockErrorMsgCacheable);
        EasyMock.replay(mockBulkCustomerDao);

        SdtContext.getContext().setRawInXml("criteria");
        this.submitQueryService.submitQuery(submitQueryRequest);

        EasyMock.verify(mockConsumerGateway);
        EasyMock.verify(mockGlobalParamCache);
        EasyMock.verify(mockErrorMsgCacheable);
        EasyMock.verify(mockBulkCustomerDao);

        Assert.assertTrue("Expected to pass", true);
    }

    /**
     * Set up a valid submit query request object.
     *
     * @param submitQueryRequest test object for submit query.
     */
    private void setUpSubmitQueryRequest(final ISubmitQueryRequest submitQueryRequest) {
        final long sdtCustomerId = 10L;
        final IBulkCustomer bulkCustomer = new BulkCustomer();
        bulkCustomer.setId(1L);
        bulkCustomer.setSdtCustomerId(10L);

        final ITargetApplication targetApp = new TargetApplication();

        targetApp.setId(1L);
        targetApp.setTargetApplicationCode("MCOL");
        targetApp.setTargetApplicationName("TEST_TargetApp");
        final Set<IServiceRouting> serviceRoutings = new HashSet<IServiceRouting>();

        final ServiceRouting serviceRouting = new ServiceRouting();
        serviceRouting.setId(1L);
        serviceRouting.setWebServiceEndpoint("MCOL_END_POINT");

        final IServiceType serviceType = new ServiceType();
        serviceType.setId(1L);
        serviceType.setName("RequestTest1");
        serviceType.setDescription("RequestTestDesc1");
        serviceType.setStatus("RequestTestStatus");

        serviceRouting.setServiceType(serviceType);
        serviceRoutings.add(serviceRouting);
        targetApp.setServiceRoutings(serviceRoutings);

        final Set<IBulkCustomerApplication> bulkCustomerApplications = new HashSet<IBulkCustomerApplication>();
        final BulkCustomerApplication bulkCustomerApplication = new BulkCustomerApplication();
        bulkCustomerApplication.setBulkCustomer(bulkCustomer);
        bulkCustomerApplication.setTargetApplication(targetApp);
        bulkCustomerApplications.add(bulkCustomerApplication);

        bulkCustomer.setBulkCustomerApplications(bulkCustomerApplications);

        EasyMock.expect(this.mockBulkCustomerDao.getBulkCustomerBySdtId(sdtCustomerId)).andReturn(bulkCustomer);

        final IBulkCustomer inBulkCustomer = new BulkCustomer();
        inBulkCustomer.setSdtCustomerId(10L);

        final ITargetApplication inTargetApp = new TargetApplication();
        inTargetApp.setTargetApplicationCode("mcol");

        submitQueryRequest.setTargetApplication(inTargetApp);
        submitQueryRequest.setBulkCustomer(inBulkCustomer);

    }

}
