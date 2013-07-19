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

package uk.gov.moj.sdt.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test class for SdtBulkReferenceGenerator.
 * 
 * @author Manoj Kulkarni
 * 
 */
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (locations = {"classpath*:**/applicationContext.xml", "/uk/gov/moj/sdt/dao/spring.context.xml",
        "classpath*:/**/spring*.xml", "/uk/gov/moj/sdt/dao/spring*.xml"})
public class SdtBulkReferenceGeneratorIntTest extends AbstractJUnit4SpringContextTests
{
    /**
     * Logger object.
     */
    private static final Logger LOG = LoggerFactory.getLogger (SdtBulkReferenceGeneratorIntTest.class);

    /**
     * Test method for the SDT bulk reference generation.
     */
    @Test
    public void testGetSDTBulkReference ()
    {
        final SdtBulkReferenceGenerator referenceGenerator =
                (SdtBulkReferenceGenerator) this.applicationContext
                        .getBean ("uk.gov.moj.sdt.utils.SdtBulkReferenceGenerator");

        // Negative Test 1 - Supply blank application name
        try
        {
            referenceGenerator.getSDTBulkReference (null);
        }
        catch (final IllegalArgumentException e)
        {
            LOG.debug (e.getMessage ());
            assertTrue (true);
        }

        // Negative Test 2 - Supply application name less than expected value of 4
        try
        {
            referenceGenerator.getSDTBulkReference ("NCO");
        }
        catch (final IllegalArgumentException e)
        {
            LOG.debug (e.getMessage ());
            assertTrue (true);
        }

        // Negative Test 3 - Supply application name more than expected value of 4
        try
        {
            referenceGenerator.getSDTBulkReference ("MCOLS");
        }
        catch (final IllegalArgumentException e)
        {
            LOG.debug (e.getMessage ());
            assertTrue (true);
        }

        final String bulkReferenceNumber = referenceGenerator.getSDTBulkReference ("MCOL");
        LOG.debug ("Generated reference number is " + bulkReferenceNumber);
        assertNotNull (bulkReferenceNumber);
        assertEquals (29, bulkReferenceNumber.length ());
    }

}