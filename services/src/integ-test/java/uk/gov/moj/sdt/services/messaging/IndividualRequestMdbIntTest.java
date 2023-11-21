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
package uk.gov.moj.sdt.services.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.moj.sdt.services.api.ITargetApplicationSubmissionService;
import uk.gov.moj.sdt.services.config.ServicesTestConfig;
import uk.gov.moj.sdt.test.utils.AbstractIntegrationTest;
import uk.gov.moj.sdt.test.utils.TestConfig;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * IntegrationTest class for testing the MessageReader implementation.
 *
 * @author Manoj Kulkarni
 */
@ActiveProfiles("integ")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestConfig.class, ServicesTestConfig.class})
@Sql(scripts = {"classpath:uk/gov/moj/sdt/services/sql/RefData.sql", "classpath:uk/gov/moj/sdt/services/sql/IndividualRequestMdbIntTest.sql"})
@Transactional
public class IndividualRequestMdbIntTest extends AbstractIntegrationTest {

    @Autowired
    @Qualifier("messageListenerContainer")
    private DefaultMessageListenerContainer mcolsContainer;

    @Autowired
    @Qualifier("messageListenerContainerMCol")
    private DefaultMessageListenerContainer mcolContainer;

    @MockBean
    private ITargetApplicationSubmissionService targetApplicationSubmissionService;

    private IndividualRequestMdb individualRequestMdb;

    /**
     * Setup the test.
     */
    @BeforeEach
    public void setUp() {
        individualRequestMdb = new IndividualRequestMdb(targetApplicationSubmissionService);
    }

    /**
     * This method tests the read message.
     *
     * @throws InterruptedException if the thread call is interrupted
     * @throws JMSException         if there is any problem when reading object from ObjectMessage
     */
    @Test
    public void testReadMessage() throws InterruptedException, JMSException {
        // Write a Message to the MDB
        SdtMessage sdtMessage = createSdtMessage();
        ObjectMessage objectMessage = mock(ObjectMessage.class);
        when(objectMessage.getObject()).thenReturn(sdtMessage);
        individualRequestMdb.readMessage(objectMessage);
        verify(targetApplicationSubmissionService).processRequestToSubmit("SDT_REQ_TEST_1", null);
        assertTrue(true, "Submission read successfully.");
    }

    /**
     * This method tests that the multiple MDB setup is working fine.
     */
    @Test
    public void testMultipleMdbSetup() {
        assertTrue(mcolsContainer.isActive(), "mcolsContainer should be active");
        assertTrue(mcolContainer.isActive(), "mcolContainer should be active");
    }

    private SdtMessage createSdtMessage() {
        final SdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference("SDT_REQ_TEST_1");
        sdtMessage.setMessageSentTimestamp(System.currentTimeMillis());
        sdtMessage.setEnqueueLoggingId(1);
        return sdtMessage;
    }
}
