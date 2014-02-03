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

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.services.TargetApplicationSubmissionService;
import uk.gov.moj.sdt.services.messaging.api.ISdtMessage;

/**
 * Test class for Message reader implementation.
 * 
 * @author Manoj Kulkarni
 * 
 */
public class IndividualRequestMdbTest
{
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (IndividualRequestMdbTest.class);

    /**
     * Mock service for the message processing.
     */
    private TargetApplicationSubmissionService mockTargetSubmissionService;

    /**
     * Individual Request Mdb Test instance.
     */
    private IndividualRequestMdb individualRequestMdb;

    /**
     * Pre-testing initialization.
     */
    @Before
    public void setUp ()
    {
        mockTargetSubmissionService = EasyMock.createMock (TargetApplicationSubmissionService.class);
    }

    /**
     * Method to test successful scenario for readMessage.
     * 
     * @throws JMSException during the test setup
     */
    @Test
    public void readMessageSuccess () throws JMSException
    {
        individualRequestMdb = new IndividualRequestMdb ();
        individualRequestMdb.setTargetAppSubmissionService (mockTargetSubmissionService);

        // Create the actual message to send.
        final ISdtMessage sdtMessage = new SdtMessage ();
        sdtMessage.setSdtRequestReference ("ReadTest1");

        // Wrap the message in the ObjectMessage
        final ObjectMessage objMessage = EasyMock.createMock (ObjectMessage.class);
        objMessage.setObject ((Serializable) sdtMessage);
        EasyMock.expectLastCall ().anyTimes ();

        EasyMock.expect (objMessage.getObject ()).andReturn ((Serializable) sdtMessage);
        EasyMock.replay (objMessage);

        mockTargetSubmissionService.processRequestToSubmit (sdtMessage.getSdtRequestReference ());

        EasyMock.expectLastCall ();

        EasyMock.replay (mockTargetSubmissionService);

        individualRequestMdb.readMessage (objMessage);

        EasyMock.verify (mockTargetSubmissionService);
        EasyMock.verify (objMessage);

        Assert.assertTrue ("Test successfully completed", true);

    }

    /**
     * Test method to test the scenario where the service throws a Data Access Exception.
     * 
     * @throws JMSException if any during the test setup
     */
    @Test
    public void readMessageDataAccessException () throws JMSException
    {
        individualRequestMdb = new IndividualRequestMdb ();
        individualRequestMdb.setTargetAppSubmissionService (mockTargetSubmissionService);

        // Create the actual message to send.
        final ISdtMessage sdtMessage = new SdtMessage ();
        sdtMessage.setSdtRequestReference ("ReadTest1");

        // Wrap the message in the ObjectMessage
        final ObjectMessage objMessage = EasyMock.createMock (ObjectMessage.class);
        objMessage.setObject ((Serializable) sdtMessage);
        EasyMock.expectLastCall ().anyTimes ();

        EasyMock.expect (objMessage.getObject ()).andReturn ((Serializable) sdtMessage);
        EasyMock.replay (objMessage);

        mockTargetSubmissionService.processRequestToSubmit (sdtMessage.getSdtRequestReference ());

        EasyMock.expectLastCall ().andThrow (new RuntimeException ("Data error occurred"));

        EasyMock.replay (mockTargetSubmissionService);

        try
        {
            individualRequestMdb.readMessage (objMessage);
            Assert.fail ("RuntimeException was expected to be thrown");
        }
        catch (final RuntimeException re)
        {
            Assert.assertTrue ("Expected to throw the exception", true);
        }

        EasyMock.verify (mockTargetSubmissionService);
        EasyMock.verify (objMessage);

        Assert.assertTrue ("Test successfully completed", true);
    }

    /**
     * Test method to test the scenario where the message is some other object other than
     * the ObjectMessage.
     * 
     * @throws JMSException if any during the test setup
     */
    @Test
    public void readMessageInvalidObject () throws JMSException
    {
        individualRequestMdb = new IndividualRequestMdb ();
        individualRequestMdb.setTargetAppSubmissionService (mockTargetSubmissionService);

        final Message message = EasyMock.createMock (Message.class);

        individualRequestMdb.readMessage (message);

        Assert.assertTrue ("Test successfully completed", true);
    }

}
