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

package uk.gov.moj.sdt.services.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import uk.gov.moj.sdt.services.utils.api.ISdtBulkReferenceGenerator;

/**
 * Test class for SdtBulkReferenceGenerator.
 * 
 * @author Manoj Kulkarni
 * 
 */
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (locations = {"classpath:/uk/gov/moj/sdt/services/spring.context.xml",
        "classpath:/uk/gov/moj/sdt/services/cache/spring.context.xml",
        "classpath:/uk/gov/moj/sdt/services/utils/spring.context.xml",
        "classpath:/uk/gov/moj/sdt/services/mbeans/spring.context.xml",
        "classpath:/uk/gov/moj/sdt/services/messaging/spring.hibernate.test.xml",
        "classpath:/uk/gov/moj/sdt/services/messaging/spring.context.test.xml",
        "classpath*:/uk/gov/moj/sdt/dao/**/spring*.xml",
        "classpath:/uk/gov/moj/sdt/consumers/spring.context.integ.test.xml",
        "classpath*:/uk/gov/moj/sdt/transformers/**/spring*.xml",
        "classpath*:/uk/gov/moj/sdt/interceptors/**/spring*.xml", "classpath*:/uk/gov/moj/sdt/utils/**/spring*.xml"})
@Transactional
public class SdtBulkReferenceGeneratorIntTest extends AbstractTransactionalJUnit4SpringContextTests
{
    /**
     * Logger object.
     */
    private static final Logger LOG = LoggerFactory.getLogger (SdtBulkReferenceGeneratorIntTest.class);

    /**
     * Test method for the SDT bulk reference generation.
     */
    @Test
    @Transactional
    public void testGetSdtBulkReference ()
    {
        final ISdtBulkReferenceGenerator referenceGenerator =
                (ISdtBulkReferenceGenerator) this.applicationContext
                        .getBean ("uk.gov.moj.sdt.services.utils.api.ISdtBulkReferenceGenerator");

        // Negative Test 1 - Supply blank application name
        try
        {
            referenceGenerator.getSdtBulkReference (null);
        }
        catch (final IllegalArgumentException e)
        {
            LOG.debug (e.getMessage ());
            assertTrue (true);
        }

        // Negative Test 2 - Supply application name less than expected value of 4
        try
        {
            referenceGenerator.getSdtBulkReference ("NCO");
        }
        catch (final IllegalArgumentException e)
        {
            LOG.debug (e.getMessage ());
            assertTrue (true);
        }

        // Negative Test 3 - Supply application name more than expected value of 4
        try
        {
            referenceGenerator.getSdtBulkReference ("MCOLS");
        }
        catch (final IllegalArgumentException e)
        {
            LOG.debug (e.getMessage ());
            assertTrue (true);
        }

        final String bulkReferenceNumber = referenceGenerator.getSdtBulkReference ("MCOL");
        LOG.debug ("Generated reference number is " + bulkReferenceNumber);
        assertNotNull (bulkReferenceNumber);
        assertEquals (29, bulkReferenceNumber.length ());
    }

}