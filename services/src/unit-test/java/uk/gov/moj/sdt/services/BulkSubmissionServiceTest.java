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

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for BulkSubmissionService.
 *
 * @author Manoj Kulkarni
 */
@ExtendWith(MockitoExtension.class)
class BulkSubmissionServiceTest extends AbstractSdtUnitTestBase {

    /**
     * Bulk Customer dao.
     */
    @Mock
    private IBulkCustomerDao mockBulkCustomerDao;

    /**
     * Target Application dao.
     */
    @Mock
    private ITargetApplicationDao mockTargetApplicationDao;

    /**
     * SDT Bulk Reference Generator.
     */
    @Mock
    private ISdtBulkReferenceGenerator mockSdtBulkReferenceGenerator;

    /**
     * Messaging Utility reference.
     */
    @Mock
    private IMessagingUtility mockMessagingUtility;

    /**
     *
     */
    @Mock
    private Map<String, IInFlightMessage> mockConcurrencyMap;

    /**
     *
     */
    @Mock
    private ICacheable mockErrorMessagesCache;

    /**
     * Generic dao.
     */
    @Mock
    private IGenericDao mockGenericDao;

    /**
     * Bulk Submission Service for testing.
     */
    private BulkSubmissionService bulkSubmissionService;

    private static final String PATH_TEST_RESOURCES = "src/unit-test/resources/";

    /**
     * Setup of the mock dao and injection of other objects.
     */
    @BeforeEach
    @Override
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        bulkSubmissionService = new BulkSubmissionService();
        bulkSubmissionService.setGenericDao(mockGenericDao);

        bulkSubmissionService.setBulkCustomerDao(mockBulkCustomerDao);
        bulkSubmissionService.setTargetApplicationDao(mockTargetApplicationDao);

        IndividualRequestsXmlParser individualRequestsXmlParser = new IndividualRequestsXmlParser();
        bulkSubmissionService.setIndividualRequestsXmlparser(individualRequestsXmlParser);

        bulkSubmissionService.setSdtBulkReferenceGenerator(mockSdtBulkReferenceGenerator);

        bulkSubmissionService.setMessagingUtility(mockMessagingUtility);

        bulkSubmissionService.setConcurrencyMap(mockConcurrencyMap);

        bulkSubmissionService.setErrorMessagesCache(mockErrorMessagesCache);
    }

    /**
     * Test method for the saving of bulk submission.
     *
     * @throws IOException if there is any error in reading the file.
     */
    @Test
    void testSaveBulkSubmission() throws IOException {
        SdtContext.getContext().setRawInXml(Utilities.getRawXml(PATH_TEST_RESOURCES, "testXMLValid2.xml"));

        // Activate Mock Generic Dao
        final IBulkSubmission bulkSubmission = this.createBulkSubmission();
        mockGenericDao.persist(bulkSubmission);

        // Mock the serviceRequest fetch
        final IServiceRequest serviceRequest = new ServiceRequest();
        when(mockGenericDao.fetch(IServiceRequest.class, 1)).thenReturn(serviceRequest);

        when(mockBulkCustomerDao.getBulkCustomerBySdtId(10)).thenReturn(bulkSubmission.getBulkCustomer());

        final String key =
                bulkSubmission.getBulkCustomer().getSdtCustomerId() + bulkSubmission.getCustomerReference();

        // Setup concurrency map as if validator had done it.
        IInFlightMessage inFlightMessage = new InFlightMessage();
        inFlightMessage.setCompetingThreads(new HashMap<>());
        inFlightMessage.getCompetingThreads().put(Thread.currentThread(), Thread.currentThread());

        when(mockConcurrencyMap.get(key)).thenReturn(inFlightMessage);

        // Put a dummy value into the SdtContext
        SdtContext.getContext().setServiceRequestId(1L);

        // Call the bulk submission service
        bulkSubmissionService.saveBulkSubmission(bulkSubmission);

        // Verify the Mock
        verify(mockGenericDao, times(2)).persist(any());

        assertTrue(true, "Expected to pass");
    }

    /**
     * This method tests bulk submission with multiple individual request containing
     * 2 valid and 1 invalid request.
     *
     * @throws IOException if there is any issue
     */
    @Test
    void testSubmissionWithMultipleRequests() throws IOException {
        final String rawXml = Utilities.getRawXml(PATH_TEST_RESOURCES, "testXMLValid3.xml");
        SdtContext.getContext().setRawInXml(rawXml);

        // Activate Mock Generic Dao
        final IBulkSubmission bulkSubmission = this.createBulkSubmission();
        addValidIndividualRequest(bulkSubmission, "ICustReq124");
        addValidIndividualRequest(bulkSubmission, "ICustReq125");

        mockGenericDao.persist(bulkSubmission);

        // Mock the serviceRequest fetch
        final IServiceRequest serviceRequest = new ServiceRequest();
        when(mockGenericDao.fetch(IServiceRequest.class, 1)).thenReturn(serviceRequest);

        when(mockBulkCustomerDao.getBulkCustomerBySdtId(10)).thenReturn(bulkSubmission.getBulkCustomer());

        final String key =
                bulkSubmission.getBulkCustomer().getSdtCustomerId() + bulkSubmission.getCustomerReference();

        // Setup concurrency map as if validator had done it.
        IInFlightMessage inFlightMessage = new InFlightMessage();
        inFlightMessage.setCompetingThreads(new HashMap<>());
        inFlightMessage.getCompetingThreads().put(Thread.currentThread(), Thread.currentThread());

        when(mockConcurrencyMap.get(key)).thenReturn(inFlightMessage);

        // Put a dummy value into the SdtContext
        SdtContext.getContext().setServiceRequestId(1L);

        // Call the bulk submission service
        bulkSubmissionService.saveBulkSubmission(bulkSubmission);

        // Verify the Mock
        verify(mockGenericDao, times(2)).persist(bulkSubmission);

        assertTrue(true, "Expected to pass");
    }

    /**
     * This method tests bulk submission concurrency issue
     * 2 valid and 1 invalid request.
     *
     * @throws IOException if there is any issue
     */
    @Test
    void testSubmissionWithConcurrenyIssue() throws IOException {
        final String rawXml = Utilities.getRawXml(PATH_TEST_RESOURCES, "testXMLValid3.xml");
        SdtContext.getContext().setRawInXml(rawXml);

        // Activate Mock Generic Dao
        final IBulkSubmission bulkSubmission = this.createBulkSubmission();
        addValidIndividualRequest(bulkSubmission, "ICustReq124");
        addValidIndividualRequest(bulkSubmission, "ICustReq125");

        mockGenericDao.persist(bulkSubmission);

        // Mock the serviceRequest fetch
        final IServiceRequest serviceRequest = new ServiceRequest();
        when(mockGenericDao.fetch(IServiceRequest.class, 1)).thenReturn(serviceRequest);

        when(mockBulkCustomerDao.getBulkCustomerBySdtId(10)).thenReturn(bulkSubmission.getBulkCustomer());

        final String key =
                bulkSubmission.getBulkCustomer().getSdtCustomerId() + bulkSubmission.getCustomerReference();

        // Setup concurrency map as if validator had done it.
        IInFlightMessage inFlightMessage = new InFlightMessage();
        inFlightMessage.setSdtBulkReference("SDTBULKREFERENCE");
        inFlightMessage.setCompetingThreads(new HashMap<>());
        inFlightMessage.getCompetingThreads().put(Thread.currentThread(), Thread.currentThread());

        when(mockConcurrencyMap.get(key)).thenReturn(inFlightMessage);

        final String errorCodeStr = IErrorMessage.ErrorCode.DUP_CUST_FILEID.toString();
        final IErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorCode(errorCodeStr);
        errorMessage.setErrorDescription("Some Description");
        errorMessage.setErrorText("Some Text");

        when(mockErrorMessagesCache.getValue(IErrorMessage.class, errorCodeStr)).thenReturn(errorMessage);

        // Put a dummy value into the SdtContext
        SdtContext.getContext().setServiceRequestId(1L);

        try {
            // Call the bulk submission service
            bulkSubmissionService.saveBulkSubmission(bulkSubmission);

            fail("Should have thrown exception");
        } catch (final Exception e) {
            if (!(e instanceof CustomerReferenceNotUniqueException) ||
                    !e.getMessage().equals("Failed with code [DUP_CUST_FILEID]; message[Some Text]")) {
                fail("Unexpected exception returned" + e.getStackTrace());
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
