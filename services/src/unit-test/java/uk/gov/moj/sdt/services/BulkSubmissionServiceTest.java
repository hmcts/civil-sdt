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
 * $Id: $
 * $LastChangedRevision: $
 * $LastChangedDate: $
 * $LastChangedBy: $ */
package uk.gov.moj.sdt.services;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.moj.sdt.dao.api.IBulkCustomerDao;
import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.dao.api.ITargetApplicationDao;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.ErrorMessage;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.ServiceRequest;
import uk.gov.moj.sdt.domain.ServiceRouting;
import uk.gov.moj.sdt.domain.ServiceType;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IErrorMessage;
import uk.gov.moj.sdt.domain.api.IIndividualRequest.IndividualRequestStatus;
import uk.gov.moj.sdt.domain.api.IServiceRequest;
import uk.gov.moj.sdt.domain.api.IServiceRouting;
import uk.gov.moj.sdt.domain.api.IServiceType;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.services.utils.IndividualRequestsXmlParser;
import uk.gov.moj.sdt.services.utils.api.IMessagingUtility;
import uk.gov.moj.sdt.services.utils.api.ISdtBulkReferenceGenerator;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.Utilities;
import uk.gov.moj.sdt.utils.concurrent.InFlightMessage;
import uk.gov.moj.sdt.utils.concurrent.api.IInFlightMessage;
import uk.gov.moj.sdt.validators.exception.CustomerReferenceNotUniqueException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Test class for BulkSubmissionService.
 *
 * @author Manoj Kulkarni
 */
public class BulkSubmissionServiceTest extends AbstractSdtUnitTestBase {
    /**
     * Logger for debugging.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BulkSubmissionServiceTest.class);

    /**
     * Bulk Submission Service for testing.
     */
    private BulkSubmissionService bulkSubmissionService;

    /**
     * Generic dao.
     */
    private IGenericDao mockGenericDao;

    /**
     * Message writer for queueing messages to the messaging server.
     */
    // private IMessageWriter mockMessageWriter;

    /**
     * Individual requests xml parser for parsing the xml requests.
     */
    private IndividualRequestsXmlParser individualRequestsXmlParser;

    /**
     * Bulk Customer dao.
     */
    private IBulkCustomerDao mockBulkCustomerDao;

    /**
     * Target Application dao.
     */
    private ITargetApplicationDao mockTargetApplicationDao;

    /**
     * SDT Bulk Reference Generator.
     */
    private ISdtBulkReferenceGenerator mockSdtBulkReferenceGenerator;

    /**
     * Messaging Utility reference.
     */
    private IMessagingUtility mockMessagingUtility;

    /**
     *
     */
    private Map<String, IInFlightMessage> mockConcurrencyMap;

    /**
     *
     */
    private ICacheable mockErrorMessagesCache;

    /**
     * Setup of the mock dao and injection of other objects.
     */
    @Before
    public void setUp() {

        mockGenericDao = EasyMock.createMock(IGenericDao.class);
        // This class cannot be easily mocked since it's within a Runnable block so it's been removed for clarity
        // mockMessageWriter = EasyMock.createMock (IMessageWriter.class);
        // bulkSubmissionService.setMessageWriter (mockMessageWriter);

        mockBulkCustomerDao = EasyMock.createMock(IBulkCustomerDao.class);
        mockTargetApplicationDao = EasyMock.createMock(ITargetApplicationDao.class);
        individualRequestsXmlParser = new IndividualRequestsXmlParser();
        mockSdtBulkReferenceGenerator = EasyMock.createMock(ISdtBulkReferenceGenerator.class);
        mockMessagingUtility = EasyMock.createMock(IMessagingUtility.class);
        mockErrorMessagesCache = EasyMock.createMock(ICacheable.class);

        bulkSubmissionService = new BulkSubmissionService(mockGenericDao,
                                                          mockBulkCustomerDao,
                                                          mockTargetApplicationDao,
                                                          individualRequestsXmlParser,
                                                          mockMessagingUtility,
                                                          mockSdtBulkReferenceGenerator,
                                                          mockErrorMessagesCache);
        mockConcurrencyMap = EasyMock.createMock(HashMap.class);
        bulkSubmissionService.setConcurrencyMap(mockConcurrencyMap);
    }

    /**
     * Test method for the saving of bulk submission.
     *
     * @throws IOException if there is any error in reading the file.
     */
    @Test
    public void testSaveBulkSubmission() throws IOException {
        SdtContext.getContext().setRawInXml(Utilities.getRawXml("src/unit-test/resources/", "testXMLValid2.xml"));

        // Activate Mock Generic Dao
        final IBulkSubmission bulkSubmission = this.createBulkSubmission();
        mockGenericDao.persist(bulkSubmission);
        EasyMock.expectLastCall();

        // Mock the serviceRequest fetch
        final IServiceRequest serviceRequest = new ServiceRequest();
        EasyMock.expect(mockGenericDao.fetch(IServiceRequest.class, 1)).andReturn(serviceRequest);

        // Replay the EasyMock
        EasyMock.replay(mockGenericDao);

        EasyMock.expect(mockBulkCustomerDao.getBulkCustomerBySdtId(10)).andReturn(bulkSubmission.getBulkCustomer());

        EasyMock.replay(mockBulkCustomerDao);

        final String key =
                bulkSubmission.getBulkCustomer().getSdtCustomerId() + bulkSubmission.getCustomerReference();

        // Setup concurrency map as if validator had done it.
        IInFlightMessage inFlightMessage = new InFlightMessage();
        inFlightMessage.setCompetingThreads(new HashMap<Thread, Thread>());
        inFlightMessage.getCompetingThreads().put(Thread.currentThread(), Thread.currentThread());

        EasyMock.expect(mockConcurrencyMap.get(key)).andReturn(inFlightMessage);
        EasyMock.expect(mockConcurrencyMap.put(key, inFlightMessage)).andReturn(inFlightMessage);
        EasyMock.replay(mockConcurrencyMap);

        // Put a dummy value into the SdtContext
        SdtContext.getContext().setServiceRequestId(1L);

        // Call the bulk submission service
        bulkSubmissionService.saveBulkSubmission(bulkSubmission);

        LOGGER.debug("bulkSubmission[" + bulkSubmission + "]");

        // Verify the Mock
        EasyMock.verify(mockGenericDao);

        Assert.assertTrue("Expected to pass", true);

    }

    /**
     * This method tests bulk submission with multiple individual request containing
     * 2 valid and 1 invalid request.
     *
     * @throws IOException if there is any issue
     */
    @Test
    public void testSubmissionWithMultipleRequests() throws IOException {
        final String rawXml = Utilities.getRawXml("src/unit-test/resources/", "testXMLValid3.xml");
        SdtContext.getContext().setRawInXml(rawXml);

        // Activate Mock Generic Dao
        final IBulkSubmission bulkSubmission = this.createBulkSubmission();
        addValidIndividualRequest(bulkSubmission, "ICustReq124");
        addValidIndividualRequest(bulkSubmission, "ICustReq125");

        LOGGER.debug("Size of Individual Requests in Bulk Submission is " +
                bulkSubmission.getIndividualRequests().size());

        mockGenericDao.persist(bulkSubmission);
        EasyMock.expectLastCall();

        // Mock the serviceRequest fetch
        final IServiceRequest serviceRequest = new ServiceRequest();
        EasyMock.expect(mockGenericDao.fetch(IServiceRequest.class, 1)).andReturn(serviceRequest);

        // Replay the EasyMock
        EasyMock.replay(mockGenericDao);

        EasyMock.expect(mockBulkCustomerDao.getBulkCustomerBySdtId(10)).andReturn(bulkSubmission.getBulkCustomer());

        EasyMock.replay(mockBulkCustomerDao);

        final String key =
                bulkSubmission.getBulkCustomer().getSdtCustomerId() + bulkSubmission.getCustomerReference();

        // Setup concurrency map as if validator had done it.
        IInFlightMessage inFlightMessage = new InFlightMessage();
        inFlightMessage.setCompetingThreads(new HashMap<Thread, Thread>());
        inFlightMessage.getCompetingThreads().put(Thread.currentThread(), Thread.currentThread());

        EasyMock.expect(mockConcurrencyMap.get(key)).andReturn(inFlightMessage);
        EasyMock.expect(mockConcurrencyMap.put(key, inFlightMessage)).andReturn(inFlightMessage);
        EasyMock.replay(mockConcurrencyMap);

        // Put a dummy value into the SdtContext
        SdtContext.getContext().setServiceRequestId(1L);

        // Call the bulk submission service
        bulkSubmissionService.saveBulkSubmission(bulkSubmission);

        // Verify the Mock
        EasyMock.verify(mockGenericDao);

        Assert.assertTrue("Expected to pass", true);
    }

    /**
     * This method tests bulk submission concurrency issue
     * 2 valid and 1 invalid request.
     *
     * @throws IOException if there is any issue
     */
    @Test
    public void testSubmissionWithConcurrenyIssue() throws IOException {
        final String rawXml = Utilities.getRawXml("src/unit-test/resources/", "testXMLValid3.xml");
        SdtContext.getContext().setRawInXml(rawXml);

        // Activate Mock Generic Dao
        final IBulkSubmission bulkSubmission = this.createBulkSubmission();
        addValidIndividualRequest(bulkSubmission, "ICustReq124");
        addValidIndividualRequest(bulkSubmission, "ICustReq125");

        LOGGER.debug("Size of Individual Requests in Bulk Submission is " +
                bulkSubmission.getIndividualRequests().size());

        mockGenericDao.persist(bulkSubmission);
        EasyMock.expectLastCall();

        // Mock the serviceRequest fetch
        final IServiceRequest serviceRequest = new ServiceRequest();
        EasyMock.expect(mockGenericDao.fetch(IServiceRequest.class, 1)).andReturn(serviceRequest);

        // Replay the EasyMock
        EasyMock.replay(mockGenericDao);

        EasyMock.expect(mockBulkCustomerDao.getBulkCustomerBySdtId(10)).andReturn(bulkSubmission.getBulkCustomer());

        EasyMock.replay(mockBulkCustomerDao);

        final String key =
                bulkSubmission.getBulkCustomer().getSdtCustomerId() + bulkSubmission.getCustomerReference();

        // Setup concurrency map as if validator had done it.
        IInFlightMessage inFlightMessage = new InFlightMessage();
        inFlightMessage.setSdtBulkReference("SDTBULKREFERENCE");
        inFlightMessage.setCompetingThreads(new HashMap<Thread, Thread>());
        inFlightMessage.getCompetingThreads().put(Thread.currentThread(), Thread.currentThread());

        EasyMock.expect(mockConcurrencyMap.get(key)).andReturn(inFlightMessage);
        EasyMock.expect(mockConcurrencyMap.put(key, inFlightMessage)).andReturn(inFlightMessage);
        EasyMock.replay(mockConcurrencyMap);

        final String errorCodeStr = IErrorMessage.ErrorCode.DUP_CUST_FILEID.toString();
        final IErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorCode(errorCodeStr);
        errorMessage.setErrorDescription("Some Discription");
        errorMessage.setErrorText("Some Text");

        EasyMock.expect(mockErrorMessagesCache.getValue(IErrorMessage.class, errorCodeStr)).andReturn(errorMessage);

        EasyMock.replay(mockErrorMessagesCache);
        // Put a dummy value into the SdtContext
        SdtContext.getContext().setServiceRequestId(1L);

        try {
            // Call the bulk submission service
            bulkSubmissionService.saveBulkSubmission(bulkSubmission);

            Assert.fail("Should have thrown exception");
        } catch (final Throwable e) {
            if (!(e instanceof CustomerReferenceNotUniqueException) ||
                    !e.getMessage().equals("Failed with code [DUP_CUST_FILEID]; message[Some Text]")) {
                Assert.fail("Unexpected exception returned" + e.getStackTrace());
            }
        }
    }

    /**
     * @return Bulk Submission object for the testing.
     */
    private IBulkSubmission createBulkSubmission() {
        final IBulkSubmission bulkSubmission = new BulkSubmission();
        final IBulkCustomer bulkCustomer = new BulkCustomer();
        final ITargetApplication targetApp = new TargetApplication();

        targetApp.setId(1L);
        targetApp.setTargetApplicationCode("MCOL");
        targetApp.setTargetApplicationName("TEST_TargetApp");
        final Set<IServiceRouting> serviceRoutings = new HashSet<>();

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

        bulkSubmission.setTargetApplication(targetApp);

        bulkCustomer.setId(1L);
        bulkCustomer.setSdtCustomerId(10L);

        bulkSubmission.setBulkCustomer(bulkCustomer);

        bulkSubmission.setCreatedDate(LocalDateTime.now());
        bulkSubmission.setCustomerReference("TEST_CUST_REF");
        bulkSubmission.setId(1L);
        bulkSubmission.setNumberOfRequest(2);
        bulkSubmission.setPayload("TEST_XML");
        bulkSubmission.setSubmissionStatus("SUBMITTED");

        final IndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setCompletedDate(LocalDateTime.now());
        individualRequest.setCreatedDate(LocalDateTime.now());
        individualRequest.setCustomerRequestReference("ICustReq123");
        individualRequest.setId(1L);
        individualRequest.setRequestStatus(IndividualRequestStatus.RECEIVED.getStatus());

        bulkSubmission.addIndividualRequest(individualRequest);

        return bulkSubmission;
    }

    /**
     * @param customerReference the customer reference number
     * @param bulkSubmission    bulk submission
     */
    private void addValidIndividualRequest(final IBulkSubmission bulkSubmission, final String customerReference) {
        final IndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setCompletedDate(LocalDateTime.now());
        individualRequest.setCreatedDate(LocalDateTime.now());
        individualRequest.setCustomerRequestReference(customerReference);
        individualRequest.setId(1L);
        individualRequest.setRequestStatus(IndividualRequestStatus.RECEIVED.getStatus());

        bulkSubmission.addIndividualRequest(individualRequest);
    }

}
