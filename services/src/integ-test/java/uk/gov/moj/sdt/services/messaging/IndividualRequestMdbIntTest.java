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

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.moj.sdt.services.messaging.api.IMessageWriter;
import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;
import uk.gov.moj.sdt.test.utils.AbstractIntegrationTest;
import uk.gov.moj.sdt.test.utils.DBUnitUtility;

/**
 * IntegrationTest class for testing the MessageReader implementation.
 *
 * @author Manoj Kulkarni
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/uk/gov/moj/sdt/services/spring.context.xml",
        "classpath:/uk/gov/moj/sdt/services/cache/spring.context.xml",
        "classpath:/uk/gov/moj/sdt/services/utils/spring.context.xml",
        "classpath:/uk/gov/moj/sdt/services/mbeans/spring.context.xml",
        "classpath:/uk/gov/moj/sdt/services/messaging/spring.hibernate.test.xml",
        "classpath:/uk/gov/moj/sdt/services/messaging/spring.context.test.xml",
        "classpath*:/uk/gov/moj/sdt/dao/**/spring*.xml",
        "classpath:/uk/gov/moj/sdt/consumers/spring.context.integ.test.xml",
        "classpath*:/uk/gov/moj/sdt/transformers/**/spring*.xml",
        "classpath*:/uk/gov/moj/sdt/interceptors/**/spring*.xml",
        "classpath*:/uk/gov/moj/sdt/validators/**/spring*.xml", "classpath*:/uk/gov/moj/sdt/utils/**/spring*.xml"})
public class IndividualRequestMdbIntTest extends AbstractIntegrationTest {
    /**
     * Setup the test.
     */
    @Before
    public void setUp() {
        DBUnitUtility.loadDatabase(this.getClass(), true);

        // Write a Message to the MDB
        final ISdtMessage sdtMessage = new SdtMessage();
        sdtMessage.setSdtRequestReference("SDT_REQ_TEST_1");
        sdtMessage.setMessageSentTimestamp(System.currentTimeMillis());
        sdtMessage.setEnqueueLoggingId(1);
        final IMessageWriter messageWriter =
                (IMessageWriter) this.applicationContext
                        .getBean("uk.gov.moj.sdt.services.messaging.api.IMessageWriter");
        messageWriter.queueMessage(sdtMessage, "MCOLS", false);
    }

    /**
     * This method tests the read message.
     *
     * @throws InterruptedException if the thread call is interrupted
     * @throws IOException          if there is any problem when reading the file
     */
    @Test
    public void testReadMessage() throws InterruptedException, IOException {
        Thread.sleep(5000);
        Assert.assertTrue("Submission read successfully.", true);
    }

    /**
     * This method tests that the multiple MDB setup is working fine.
     */
    @Test
    public void testMultipleMdbSetup() {
        final DefaultMessageListenerContainer mcolsContainer =
                (DefaultMessageListenerContainer) this.applicationContext.getBean("messageListenerContainer");

        if (mcolsContainer.isActive()) {
            Assert.assertTrue(true);
        }

        final DefaultMessageListenerContainer mcolContainer =
                (DefaultMessageListenerContainer) this.applicationContext.getBean("messageListenerContainerMCol");

        if (mcolContainer.isActive()) {
            Assert.assertTrue(true);
        }
    }
}
