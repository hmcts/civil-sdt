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
package uk.gov.moj.sdt.services.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.services.messaging.SdtMessage;
import uk.gov.moj.sdt.services.messaging.api.IMessageWriter;
import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;
import uk.gov.moj.sdt.services.utils.api.IMessagingUtility;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of the IMessagingUtility interface providing methods
 * to do the tasks related to message queueing.
 *
 * @author Manoj Kulkarni
 */
@Component("MessagingUtility")
public class MessagingUtility implements IMessagingUtility {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingUtility.class);

    /**
     * Message writer for queueing messages to the messaging server.
     */
    private IMessageWriter messageWriter;

    @Value("${sdt.service.config.queue_delay:500}")
    private int queueDelay;

    @Value("${enable-single-executor:false}")
    private boolean enableSingleExecutor;

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    public MessagingUtility(@Qualifier("MessageWriter")
                                IMessageWriter messageWriter) {
        this.messageWriter = messageWriter;
    }

    @Override
    public void enqueueRequest(final IIndividualRequest individualRequest) {
        LOGGER.debug("enqueueRequest: [{}]", individualRequest.getSdtRequestReference());
        if (enableSingleExecutor) {
            executorService.schedule(() -> {
                LOGGER.debug("enqueueRequests: executorService (single)");
                queueRequest(individualRequest);
                }
                , queueDelay, TimeUnit.MILLISECONDS
            );
        } else {
            queueRequest(individualRequest);
        }
    }

    @Override
    public void enqueueRequests(List<IIndividualRequest> individualRequests) {
        LOGGER.debug("enqueueRequests: Number of requests [{}]", individualRequests.size());
        executorService.schedule(() -> {
            LOGGER.debug("enqueueRequests: executorService (multiple)");
            queueRequest(individualRequests);
            }
            , queueDelay, TimeUnit.MILLISECONDS
        );
    }

    private void queueRequest(List<IIndividualRequest> individualRequests) {
        LOGGER.debug("queueRequest: Number of requests [{}]", individualRequests.size());
        for (IIndividualRequest iRequest : individualRequests) {
            boolean enqueueable = iRequest.isEnqueueable();
            LOGGER.debug("Queue IndividualRequestReference {} with BulkReference {} Enqueueable {} ",
                         iRequest.getSdtRequestReference(), iRequest.getSdtBulkReference(), enqueueable);
            if (enqueueable) {
                LOGGER.debug("queueRequest: Before queueRequest");
                queueRequest(iRequest);
            }
        }
    }

    private void queueRequest(IIndividualRequest individualRequest) {
        LOGGER.debug("queueRequest: Individual request [{}]", individualRequest.getSdtRequestReference());
        final String targetAppCode =
            individualRequest.getBulkSubmission().getTargetApplication().getTargetApplicationCode();

        final ISdtMessage messageObj = new SdtMessage();

        messageObj.setSdtRequestReference(individualRequest.getSdtRequestReference());
        LOGGER.debug("Queuing Request {} to target app code {}",  messageObj, targetAppCode);
        getMessageWriter().queueMessage(messageObj, targetAppCode);
        LOGGER.debug("Queue Request Successful {} to target app code {}",  messageObj, targetAppCode);
    }

    /**
     * @return the Message Writer
     */
    public IMessageWriter getMessageWriter() {
        return messageWriter;
    }

    /**
     * @param messageWriter the Message writer implementation.
     */
    public void setMessageWriter(final IMessageWriter messageWriter) {
        this.messageWriter = messageWriter;
    }

}
