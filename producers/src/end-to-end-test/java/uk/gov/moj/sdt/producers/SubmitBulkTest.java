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

package uk.gov.moj.sdt.producers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.junit.Assert;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.moj.sdt.test.utils.DBUnitUtility;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.BulkRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkresponseschema.BulkResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkresponseschema.ObjectFactory;
import uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType;

/**
 * Test class for end to end web service tests..
 *
 * @author Robin Compston
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/uk/gov/moj/sdt/producers/spring*e2e.test.xml",
        "classpath*:/uk/gov/moj/sdt/utils/**/spring*.xml", "classpath*:/uk/gov/moj/sdt/transformers/**/spring*.xml"})
public class SubmitBulkTest extends AbstractWebServiceTest<BulkRequestType, BulkResponseType> {
    /**
     * Thirty seconds in milliseconds.
     */
    static final int THIRTY_SECONDS = 30000;

    /**
     * Method to call remote submit bulk endpoint to be tested.
     */
    @Test
    public void testValid() {
        this.callWebService(BulkRequestType.class);
    }

    /**
     * Method to call remote submit bulk endpoint to be tested.
     */
    @Test
    public void testCountMismatch() {
        this.callWebService(BulkRequestType.class);
    }

    /**
     * Method to test ability of SDT to handle two concurrent in-memory duplicates (same customer and customer
     * reference), and to allow the first and return a duplicate error for the second.
     */
    @Test
    public void testTwoConcurrentDuplicates() {
        boolean duplicateDetected = false;

        // Used to limit number of attempts.
        int attempts = 0;

        // Keep trying to send pairs of messages until they are both in memory at the same time and detected as
        // concurrent.
        while (!duplicateDetected && attempts < 10) {
            duplicateDetected = this.sendConcurrentDuplicates(attempts, 2);
            attempts++;

            // Must clear out previous failed attempt at concurrent duplicate from database.
            if (!duplicateDetected) {
                DBUnitUtility.loadDatabase(this.getClass(), true);
            }
        }
    }

    /**
     * Method to test ability of SDT to handle two concurrent in-memory duplicates (same customer and customer
     * reference), and to allow the first and return a duplicate error for the second.
     */
    @Test
    public void testTenConcurrentDuplicates() {
        boolean duplicateDetected = false;

        // Used to limit number of attempts.
        int attempts = 0;

        // Keep trying to send pairs of messages until they are both in memory at the same time and detected as
        // concurrent.
        while (!duplicateDetected && attempts < 10) {
            duplicateDetected = this.sendConcurrentDuplicates(attempts, 10);
            attempts++;

            // Must clear out previous failed attempt at concurrent duplicate from database.
            if (!duplicateDetected) {
                DBUnitUtility.loadDatabase(this.getClass(), true);
            }
        }
    }

    @Override
    protected BulkResponseType callTestWebService(final BulkRequestType request) {
        // Get the SOAP proxy client.
        ISdtEndpointPortType client = getSdtEndpointClient();

        // Call the specific business method for this text - note that a single test can only use one web service
        // business method.
        return client.submitBulk(request);
    }

    @Override
    protected JAXBElement<BulkResponseType> wrapJaxbObject(final BulkResponseType response) {
        // Use the provided factor to create a wrapped instance of the response.
        ObjectFactory objectFactory = new ObjectFactory();
        return objectFactory.createBulkResponse(response);
    }

    /**
     * Method to send two duplicate messages to the application server in close succession in the hope that we will get
     * an in memory concurrent duplicate detected.
     *
     * @param attempts         the number of times we have tried to send a pair of messages.
     * @param concurrencyLevel the number of concurrent threads sending messages.
     * @return true - duplicate detected, false - duplicate not detected.
     */
    protected boolean sendConcurrentDuplicates(final int attempts, final int concurrencyLevel) {
        // Define outside loops.
        ConcurrentMessage concurrentMessage;

        // Create all necessary threads and store in list.
        List<ConcurrentMessage> concurrentMessages = new ArrayList<ConcurrentMessage>(concurrencyLevel);
        for (int i = 0; i < concurrencyLevel; i++) {
            concurrentMessage = new ConcurrentMessage(attempts);
            concurrentMessages.add(concurrentMessage);

            // Start the thread.
            concurrentMessage.start();
        }

        // Wait for all threads to complete.
        for (int i = 0; i < concurrencyLevel; i++) {
            try {
                concurrentMessages.get(i).join(THIRTY_SECONDS);
            } catch (final InterruptedException e) {
                Assert.fail("Unexpected interruption of concurrent message thread.");
            }
        }

        // Count all duplicates.
        int duplicateCount = 0;
        for (int i = 0; i < concurrencyLevel; i++) {
            if (concurrentMessages.get(i).isDuplicate()) {
                duplicateCount++;
            }
        }

        // All should have been duplicates except for one which worked.
        if (duplicateCount == concurrencyLevel - 1) {
            return true;
        }

        // Test did not get expected result - try again - it may be a timing issue.
        return false;
    }

    /**
     * Inner class to send a single SOAP message in a separate thread
     *
     * @author Robin Compston.
     */
    private class ConcurrentMessage extends Thread {
        /**
         * Flag to indicate server saw this message as a duplicate.
         */
        private boolean duplicate = false;

        /**
         * Suffix to customer reference and request id to make each pair of message unique.
         */
        private int attemptOffset;

        /**
         * Constructor for ConcurrentMessage class.
         *
         * @param attemptOffset suffix to be added to customer reference and request id to make each pair of messages
         *                      unique.
         */
        public ConcurrentMessage(final int attemptOffset) {
            this.attemptOffset = attemptOffset;
        }

        @Override
        public void run() {
            this.testConcurrentDuplicate();
        }

        /**
         * Method name used to fool infrastructure into thinking this is the outer class method name allowing normal
         * method of identifying associated XML files for test.
         *
         * @return true - duplicate message detected by server, false - no duplicate detected.
         */
        public void testConcurrentDuplicate() {
            try {
                this.callWebService();
            } catch (final ComparisonFailure e) {
                if (e.getActual().matches(
                        ".*?Duplicate User File Reference .*? supplied. This was previously used to submit "
                                + "a Bulk Request on .*? and the SDT Bulk Reference " + "MCOL-.*? was allocated..*?")) {
                    setDuplicate(true);
                }
            }
        }

        /**
         * Method is needed to add one extra stack frame to allow the Abstract class to correctly find the method name
         * of the test.
         */
        private void callWebService() {
            // Get the argument to send to the web service.
            BulkRequestType request = getJaxbFromXml(BulkRequestType.class);

            // Adjust the requests to allow pairs of potential duplicates to be distinguished from each other.
            request.getHeader().setCustomerReference(
                    request.getHeader().getCustomerReference() + this.attemptOffset);
            request.getRequests().getRequest().get(0)
                    .setRequestId(request.getRequests().getRequest().get(0).getRequestId() + this.attemptOffset);

            // Call the remote service.
            BulkResponseType response;
            try {
                response = callTestWebService(request);
            } catch (RuntimeException e) {
                throw e;
            }

            // Check the response returned by the web service.
            checkXmlFromJaxb(response);

            return;
        }

        /**
         * Return duplicate flag value.
         *
         * @return true - duplicate message detected by server, false - no duplicate detected.
         */
        public boolean isDuplicate() {
            return duplicate;
        }

        /**
         * Set the value of the duplicate flag.
         *
         * @param duplicate value of the duplicate flag.
         */
        private void setDuplicate(final boolean duplicate) {
            this.duplicate = duplicate;
        }
    }
}
