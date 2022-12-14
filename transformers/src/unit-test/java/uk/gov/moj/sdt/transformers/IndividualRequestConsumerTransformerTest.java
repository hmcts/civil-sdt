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
package uk.gov.moj.sdt.transformers;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkCustomerApplication;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkCustomerApplication;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.IIndividualRequest.IndividualRequestStatus;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.CreateStatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.CreateStatusType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.ErrorType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvrequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvrequestschema.IndividualRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvresponseschema.IndividualResponseType;

/**
 * Unit tests for IndividualRequestConsumerTransformer.
 *
 * @author d164190
 */
public class IndividualRequestConsumerTransformerTest extends AbstractSdtUnitTestBase {
    /**
     * Individual Request Consumer Transformer.
     */
    private IndividualRequestConsumerTransformer transformer;

    /**
     * Set up variables for the test.
     */
    public void setUpLocalTests() {
        Constructor<IndividualRequestConsumerTransformer> c;
        try {
            // Make the constructor visible so we can get a new instance of it.
            c = IndividualRequestConsumerTransformer.class.getDeclaredConstructor();
            c.setAccessible(true);
            transformer = c.newInstance();
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Test the transformation from jaxb (IndividualResponseType) to domain (IIndividualRequest) object with status
     * ACCEPTED.
     */
    @Test
    public void testTransformJaxbToDomainAccepted() {
        // Set up the jaxb object to transform
        final IndividualResponseType jaxb = new IndividualResponseType();
        final CreateStatusType statusType = new CreateStatusType();
        statusType.setCode(CreateStatusCodeType.ACCEPTED);
        jaxb.setStatus(statusType);

        final IIndividualRequest domain = new IndividualRequest();
        transformer.transformJaxbToDomain(jaxb, domain);

        // Test the jaxb object has been transformed to a domain object
        Assert.assertEquals("Request Status is incorrect", IndividualRequestStatus.ACCEPTED.getStatus(),
                domain.getRequestStatus());
        Assert.assertNotNull("Request updated data should be populated", domain.getUpdatedDate());
        Assert.assertNotNull("Request completed data should be populated", domain.getCompletedDate());
    }

    /**
     * Test the transformation from jaxb (IndividualResponseType) to domain (IIndividualRequest) object with status
     * INITIALLY ACCEPTED.
     */
    @Test
    public void testTransformJaxbToDomainInitiallyAccepted() {
        // Set up the jaxb object to transform
        final IndividualResponseType jaxb = new IndividualResponseType();
        final CreateStatusType statusType = new CreateStatusType();
        statusType.setCode(CreateStatusCodeType.INITIALLY_ACCEPTED);
        jaxb.setStatus(statusType);

        final IIndividualRequest domain = new IndividualRequest();
        transformer.transformJaxbToDomain(jaxb, domain);

        // Test the jaxb object has been transformed to a domain object
        Assert.assertEquals("Request Status is incorrect", IndividualRequestStatus.INITIALLY_ACCEPTED.getStatus(),
                domain.getRequestStatus());
        Assert.assertNotNull("Request updated data should be populated", domain.getUpdatedDate());
    }

    /**
     * Test the transformation from jaxb (IndividualResponseType) to domain (IIndividualRequest) object with status
     * AWAITING DATA.
     */
    @Test
    public void testTransformJaxbToDomainAwaitingData() {
        // Set up the jaxb object to transform
        final IndividualResponseType jaxb = new IndividualResponseType();
        final CreateStatusType statusType = new CreateStatusType();
        statusType.setCode(CreateStatusCodeType.AWAITING_DATA);
        jaxb.setStatus(statusType);

        final IIndividualRequest domain = new IndividualRequest();
        transformer.transformJaxbToDomain(jaxb, domain);

        // Test the jaxb object has been transformed to a domain object
        Assert.assertEquals("Request Status is incorrect", IndividualRequestStatus.AWAITING_DATA.getStatus(),
                domain.getRequestStatus());
        Assert.assertNotNull("Request updated data should be populated", domain.getUpdatedDate());
    }

    /**
     * Test the transformation from jaxb (IndividualResponseType) to domain (IIndividualRequest) object with status
     * REJECTED.
     */
    @Test
    public void testTransformJaxbToDomainRejected() {
        // Set up the jaxb object to transform
        final IndividualResponseType jaxb = new IndividualResponseType();
        final CreateStatusType statusType = new CreateStatusType();
        final ErrorType errorType = new ErrorType();
        final IIndividualRequest domain = new IndividualRequest();

        statusType.setCode(CreateStatusCodeType.REJECTED);
        errorType.setCode("FAILURE");
        errorType.setDescription("MCOL has Failed to process the request");
        statusType.setError(errorType);
        jaxb.setStatus(statusType);

        transformer.transformJaxbToDomain(jaxb, domain);

        final IErrorLog errorLog = domain.getErrorLog();

        // Test the jaxb object has been transformed to a domain object
        Assert.assertEquals("Request Status is incorrect", IndividualRequestStatus.REJECTED.getStatus(), domain
                .getRequestStatus().toString());
        Assert.assertNotNull("Request updated data should be populated", domain.getUpdatedDate());
        Assert.assertNotNull("Request completed data should be populated", domain.getCompletedDate());
        Assert.assertEquals("Error code is incorrect", "FAILURE", errorLog.getErrorCode());
        Assert.assertEquals("Error description is incorrect", "MCOL has Failed to process the request",
                errorLog.getErrorText());
    }

    /**
     * Test the transformation from jaxb (IndividualResponseType) to domain (IIndividualRequest) object with status
     * ERROR.
     */
    @Test
    public void testTransformJaxbToDomainError() {
        // Set up the jaxb object to transform
        final IndividualResponseType jaxb = new IndividualResponseType();
        final CreateStatusType statusType = new CreateStatusType();
        final ErrorType errorType = new ErrorType();
        final IIndividualRequest domain = new IndividualRequest();

        statusType.setCode(CreateStatusCodeType.ERROR);
        errorType.setCode("ERROR");
        errorType.setDescription("MCOL has found an error in processing the request");
        statusType.setError(errorType);
        jaxb.setStatus(statusType);

        transformer.transformJaxbToDomain(jaxb, domain);

        final IErrorLog errorLog = domain.getErrorLog();

        // Test the jaxb object has been transformed to a domain object
        Assert.assertEquals("Request Status is incorrect", IndividualRequestStatus.REJECTED.getStatus(), domain
                .getRequestStatus().toString());
        Assert.assertNotNull("Request updated data should be populated", domain.getUpdatedDate());
        Assert.assertNotNull("Request completed data should be populated", domain.getCompletedDate());
        Assert.assertEquals("Error code is incorrect", "ERROR", errorLog.getErrorCode());
        Assert.assertEquals("Error description is incorrect", "MCOL has found an error in processing the request",
                errorLog.getErrorText());
    }

    /**
     * Test the transformation from domain (IIndividualRequest) to jaxb (IndividualRequestType) object.
     */
    @Test
    public void testTransformDomainToJaxb() {
        final IIndividualRequest domain = new IndividualRequest();
        final IBulkSubmission bulkSubmission = new BulkSubmission();
        final ITargetApplication targetApplication = new TargetApplication();
        final IBulkCustomer bulkCustomer = new BulkCustomer();

        domain.setRequestType("MCOL_Request_type");
        domain.setSdtRequestReference("SDT0001234");

        targetApplication.setTargetApplicationCode("MCOL");
        bulkSubmission.setTargetApplication(targetApplication);

        bulkCustomer.setSdtCustomerId(12345L);
        bulkCustomer.setBulkCustomerApplications(createBulkCustomerApplications("MCOL"));

        bulkSubmission.setBulkCustomer(bulkCustomer);
        domain.setBulkSubmission(bulkSubmission);

        final IndividualRequestType jaxb = transformer.transformDomainToJaxb(domain);
        final HeaderType header = jaxb.getHeader();

        // Test the domain object has been transformed to a jaxb object
        Assert.assertEquals("", "MCOL_Request_type", header.getRequestType());
        Assert.assertEquals("", "SDT0001234", header.getSdtRequestId());
        Assert.assertEquals("", "appId", header.getTargetAppCustomerId());

    }

    /**
     * the list of applications for a customer.
     *
     * @param applicationName the application name
     * @return the set of bulk customer applications for this customer
     */
    private Set<IBulkCustomerApplication> createBulkCustomerApplications(final String applicationName) {
        final Set<IBulkCustomerApplication> bulkCustomerApplications = new HashSet<IBulkCustomerApplication>();

        final IBulkCustomerApplication bulkCustomerApp = new BulkCustomerApplication();
        bulkCustomerApp.setCustomerApplicationId("appId");
        final ITargetApplication application = new TargetApplication();
        application.setTargetApplicationCode(applicationName);
        bulkCustomerApp.setTargetApplication(application);
        bulkCustomerApplications.add(bulkCustomerApp);
        return bulkCustomerApplications;
    }
}
