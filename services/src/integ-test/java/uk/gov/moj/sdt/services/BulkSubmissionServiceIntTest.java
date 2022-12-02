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
package uk.gov.moj.sdt.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.context.junit4.SpringRunner;
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
import uk.gov.moj.sdt.services.api.IBulkSubmissionService;
import uk.gov.moj.sdt.services.config.ServicesTestConfig;
import uk.gov.moj.sdt.test.utils.AbstractIntegrationTest;
import uk.gov.moj.sdt.test.utils.DBUnitUtility;
import uk.gov.moj.sdt.test.utils.TestConfig;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.Utilities;
import uk.gov.moj.sdt.utils.concurrent.InFlightMessage;
import uk.gov.moj.sdt.utils.concurrent.api.IInFlightMessage;
import uk.gov.moj.sdt.validators.exception.CustomerReferenceNotUniqueException;

/**
 * Implementation of the integration test for BulkSubmissionService.
 *
 * @author Manoj kulkarni
 */
@ActiveProfiles("integ")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class, ServicesTestConfig.class })
public class BulkSubmissionServiceIntTest extends AbstractIntegrationTest {

    /**
     * Test subject.
     */
    private IBulkSubmissionService bulkSubmissionService;

    /**
     * Map holding the concurrency information for each thread. This needs to be cleared in the test as the test calls
     * the service only and it is the handler above the service in the real application which is responsible for
     * clearing the map. If it is not cleared, all tests but the first will fail because the map will still have the
     * previous test information in it.
     */
    protected Map<String, IInFlightMessage> concurrencyMap;

    /**
     * Setup the test.
     */
    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        DBUnitUtility.loadDatabase(this.getClass(), true);

        bulkSubmissionService =
                (IBulkSubmissionService) this.applicationContext
                        .getBean("BulkSubmissionService");

        // Get the concurrency map so we can clear it after test.
        concurrencyMap = new HashMap<>();

    }

    /**
     * This method tests for persistence of a single submission.
     *
     * @throws IOException if there is any error reading from the test file.
     */
    @Test
    @Rollback(false)
    public void testSaveSingleSubmission() throws IOException {
        final String rawXml = Utilities.getRawXml("src/integ-test/resources/", "testSampleRequest.xml");
        SdtContext.getContext().setRawInXml(rawXml);

        final IBulkSubmission bulkSubmission = this.createBulkSubmission(1);

        // Set the service request id so it can be retrieved in the saveBulkSubmission code
        SdtContext.getContext().setServiceRequestId(10800L);

        // Setup concurrency map as if validator had done it.
        IInFlightMessage inFlightMessage = new InFlightMessage();
        inFlightMessage.setCompetingThreads(new HashMap<Thread, Thread>());
        String key = bulkSubmission.getBulkCustomer().getSdtCustomerId() + bulkSubmission.getCustomerReference();
        concurrencyMap.put(key, inFlightMessage);

        // Call the bulk submission service
        bulkSubmissionService.saveBulkSubmission(bulkSubmission);

        clearConcurrencyMap(bulkSubmission);

        Assert.assertNotNull(bulkSubmission.getPayload());

        Assert.assertEquals(1L, bulkSubmission.getNumberOfRequest());

        Assert.assertNotNull(bulkSubmission.getSdtBulkReference());

        Assert.assertNotNull(bulkSubmission.getServiceRequest());

        final List<IIndividualRequest> individualRequests = bulkSubmission.getIndividualRequests();
        Assert.assertNotNull(individualRequests);
        Assert.assertEquals(1, individualRequests.size());
        for (IIndividualRequest request : individualRequests) {

            Assert.assertNotNull(request.getSdtBulkReference());
            Assert.assertNotNull(request.getSdtRequestReference());
            Assert.assertNotNull(request.getRequestPayload());
        }
    }

    /**
     * This method tests for persistence of a submission containing multiple individual requests.
     *
     * @throws IOException if there is any error reading from the test file.
     */
    @Test
    @Rollback(false)
    public void testSaveMultipleSubmissions() throws IOException {
        final String rawXml = Utilities.getRawXml("src/integ-test/resources/", "testLargeSampleRequest.xml");
        SdtContext.getContext().setRawInXml(rawXml);

        final IBulkSubmission bulkSubmission = this.createBulkSubmission(62);

        bulkSubmission.setNumberOfRequest(62L);

        // Set the service request id so it can be retrieved in the saveBulkSubmission code
        SdtContext.getContext().setServiceRequestId(10800L);

        // Setup concurrency map as if validator had done it.
        IInFlightMessage inFlightMessage = new InFlightMessage();
        inFlightMessage.setCompetingThreads(new HashMap<Thread, Thread>());
        String key = bulkSubmission.getBulkCustomer().getSdtCustomerId() + bulkSubmission.getCustomerReference();
        concurrencyMap.put(key, inFlightMessage);

        // Call the bulk submission service
        bulkSubmissionService.saveBulkSubmission(bulkSubmission);

        clearConcurrencyMap(bulkSubmission);

        Assert.assertNotNull(bulkSubmission.getPayload());

        Assert.assertEquals(62L, bulkSubmission.getNumberOfRequest());

        Assert.assertNotNull(bulkSubmission.getSdtBulkReference());

        final List<IIndividualRequest> individualRequests = bulkSubmission.getIndividualRequests();
        Assert.assertNotNull(individualRequests);
        Assert.assertEquals(62, individualRequests.size());

        for (IIndividualRequest request : individualRequests) {

            Assert.assertNotNull(request.getSdtBulkReference());
            Assert.assertNotNull(request.getSdtRequestReference());
            Assert.assertNotNull(request.getRequestPayload());
        }
    }

    /**
     * This method tests for an exception raised by the second of two duplicate submissions. The first of these should
     * work and the second fail. The submissions are identified as duplicates because they have the same customer and
     * customer reference. This error is simulated in the integration test by not clearing the concurrency map after the
     * first submission. This would normally be done by the handler. By leaving the map populated with the reference
     * from the first submission the service layer acts as if there are two duplicate submission in memory concurrently.
     *
     * @throws IOException if there is any error reading from the test file.
     */
    @Test
    @Rollback(false)
    public void testConcurrentDuplicateSubmissions() throws IOException {
        final String rawXml = Utilities.getRawXml("src/integ-test/resources/", "testLargeSampleRequest.xml");
        SdtContext.getContext().setRawInXml(rawXml);

        final IBulkSubmission bulkSubmission = this.createBulkSubmission(62);

        bulkSubmission.setNumberOfRequest(62L);

        // Set the service request id so it can be retrieved in the saveBulkSubmission code
        SdtContext.getContext().setServiceRequestId(10800L);

        // Setup concurrency map as if validator had done it.
        IInFlightMessage inFlightMessage = new InFlightMessage();
        inFlightMessage.setCompetingThreads(new HashMap<Thread, Thread>());
        String key = bulkSubmission.getBulkCustomer().getSdtCustomerId() + bulkSubmission.getCustomerReference();
        concurrencyMap.put(key, inFlightMessage);

        // Call the bulk submission service
        bulkSubmissionService.saveBulkSubmission(bulkSubmission);

        // DO NOT CLEAR concurrencyMap.

        Assert.assertNotNull(bulkSubmission.getPayload());

        Assert.assertEquals(62L, bulkSubmission.getNumberOfRequest());

        Assert.assertNotNull(bulkSubmission.getSdtBulkReference());

        final List<IIndividualRequest> individualRequests = bulkSubmission.getIndividualRequests();
        Assert.assertNotNull(individualRequests);
        Assert.assertEquals(62, individualRequests.size());

        for (IIndividualRequest request : individualRequests) {

            Assert.assertNotNull(request.getSdtBulkReference());
            Assert.assertNotNull(request.getSdtRequestReference());
            Assert.assertNotNull(request.getRequestPayload());
        }

        try {
            // Call the bulk submission service for second time without clearing the concurrencyMap from the first call.
            bulkSubmissionService.saveBulkSubmission(bulkSubmission);

            Assert.fail("Should have thrown exception");
        } catch (final Throwable e) {
            if (!(e instanceof CustomerReferenceNotUniqueException) ||
                    !e.getMessage().matches(
                            "Failed with code \\[DUP_CUST_FILEID\\]; "
                                    + "message\\[Duplicate User File Reference 10711 supplied. This was "
                                    + "previously used to submit a Bulk Request on .*? and "
                                    + "the SDT Bulk Reference \\S*? was allocated.\\]")) {
                Assert.fail("Unexpected exception returned\n" + e.getStackTrace());
            }
        }

        clearConcurrencyMap(bulkSubmission);
    }

    /**
     * @param numberOfRequests -the number of individual requests.
     * @return Bulk Submission object for the testing.
     */
    private IBulkSubmission createBulkSubmission(final int numberOfRequests) {
        final IBulkSubmission bulkSubmission = new BulkSubmission();
        final IBulkCustomer bulkCustomer = new BulkCustomer();
        final ITargetApplication targetApp = new TargetApplication();

        targetApp.setId(1L);
        targetApp.setTargetApplicationCode("MCOL");
        targetApp.setTargetApplicationName("MCOL");
        final Set<IServiceRouting> serviceRoutings = new HashSet<>();

        final IServiceRouting serviceRouting = new ServiceRouting();
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

        bulkCustomer.setSdtCustomerId(2L);

        bulkSubmission.setBulkCustomer(bulkCustomer);

        bulkSubmission.setCreatedDate(LocalDateTime.now());
        bulkSubmission.setCustomerReference("10711");
        bulkSubmission.setNumberOfRequest(1);

        bulkSubmission.setSubmissionStatus("SUBMITTED");

        if (numberOfRequests == 1) {
            createIndividualRequest(bulkSubmission, "ICustReq123", "Received", 1);
        } else {
            int k = 123;
            for (int i = 0; i < 62; i++) {
                createIndividualRequest(bulkSubmission, "ICustReq" + k, "Received", i + 1);
                k++;
            }
        }

        return bulkSubmission;
    }

    /**
     * Helper method to setup a single request.
     *
     * @param bulkSubmission    bulk submission record.
     * @param customerReference customer reference.
     * @param status            status.
     * @param lineNumber        record counter.
     * @return IndividualRequest
     */
    private IndividualRequest createIndividualRequest(final IBulkSubmission bulkSubmission,
                                                      final String customerReference, final String status,
                                                      final int lineNumber) {
        final IndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setCreatedDate(LocalDateTime.now());
        individualRequest.setCustomerRequestReference(customerReference);
        individualRequest.setRequestStatus(status);
        individualRequest.setLineNumber(lineNumber);
        bulkSubmission.addIndividualRequest(individualRequest);

        return individualRequest;
    }

    /**
     * Clear out the concurrency map after the last test, else subsequent tests will fail.
     *
     * @param bulkSubmission bulk submission record.
     */
    private void clearConcurrencyMap(final IBulkSubmission bulkSubmission) {
        final String key =
                bulkSubmission.getBulkCustomer().getSdtCustomerId() + bulkSubmission.getCustomerReference();

        // Set concurrency map for this user and customer reference.
        concurrencyMap.remove(key);
    }
}
