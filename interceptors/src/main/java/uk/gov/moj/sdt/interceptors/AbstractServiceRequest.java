/* Copyrights and Licenses
 *
 * Copyright (c) 2012-2014 by the Ministry of Justice. All rights reserved.
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
package uk.gov.moj.sdt.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.domain.ServiceRequest;
import uk.gov.moj.sdt.domain.api.IServiceRequest;
import uk.gov.moj.sdt.utils.SdtContext;

import java.time.LocalDateTime;

/**
 * Shared abstract class for audit logging.
 *
 * @author d195274
 */
public abstract class AbstractServiceRequest extends AbstractSdtInterceptor {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServiceRequest.class);

    /**
     * The persistence class for this interceptor.
     */
    private IGenericDao serviceRequestDao;

    /**
     * Default constructor.
     *
     * @param phase the CXF phase to apply this to.
     */
    protected AbstractServiceRequest(final String phase) {
        super(phase);
    }

    /**
     * The ServiceRequestDAO.
     *
     * @return a concrete instance of the dao.
     */
    public IGenericDao getServiceRequestDao() {
        return serviceRequestDao;
    }

    /**
     * Set the serviceRequestDAO.
     *
     * @param serviceRequestDao the dao
     */
    public void setServiceRequestDao(final IGenericDao serviceRequestDao) {
        this.serviceRequestDao = serviceRequestDao;
    }

    /**
     * When the application closes the message output stream the service request
     * must also be persisted. Since Hibernate wraps everything up into
     * transactional units trying to write the output stream value anywhere else
     * in the stack seems to miss the Hibernate persist ending up with an empty
     * response payload being persisted.
     *
     * @param envelope the String envelope value of the output soap message.
     */
    protected void persistEnvelope(final String envelope) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("AbstractServiceRequest creating outbound payload database log for ServiceRequest: {}",
                    SdtContext.getContext().getServiceRequestId());
        }

        final Long serviceRequestId = SdtContext.getContext().getServiceRequestId();

        // If there is no service request id then the ServiceRequestInboundInterceptor has not been called and there
        // will be no service request row in the database.
        if (serviceRequestId != null) {
            // Get the log message for the inbound request so we can add the outbound response to it.
            final IServiceRequest serviceRequest =
                    this.getServiceRequestDao().fetch(ServiceRequest.class, serviceRequestId);

            // Add the response and timestamp to the service request record.
            serviceRequest.setResponsePayload(envelope.getBytes());
            serviceRequest.setResponseDateTime(LocalDateTime.now());

            // Note that bulk reference will be null if this is not a bulk submission.
            final String bulkReference = SdtContext.getContext().getSubmitBulkReference();
            if (bulkReference != null && bulkReference.length() > 0) {
                serviceRequest.setBulkReference(bulkReference);
            }

            this.getServiceRequestDao().persist(serviceRequest);
        }
    }
}
