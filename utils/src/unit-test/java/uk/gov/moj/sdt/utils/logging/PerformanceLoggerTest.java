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
package uk.gov.moj.sdt.utils.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.SdtContext;

/**
 * Class to test performance logging.
 * 
 * @author Robin Compston.
 */
public class PerformanceLoggerTest extends AbstractSdtUnitTestBase
{
    /**
     * Pathname of performance log file.
     */
    private static final String PERFORMANCE_LOG_PATH = "../logs/sdt.performance.log";

    /**
     * File for performance log.
     */
    private File performanceLogFile;

    @Override
    protected void setUpLocalTests () throws Exception
    {
        // Clear performance log.
        performanceLogFile = new File (PERFORMANCE_LOG_PATH);
        if (performanceLogFile.exists ())
        {
            FileChannel outChan;
            try
            {
                outChan = new FileOutputStream (performanceLogFile, true).getChannel ();
                outChan.truncate (0);
                outChan.close ();
            }
            catch (final Exception e)
            {
                Assert.fail (e.getStackTrace ().toString ());
            }
        }

    }

    /**
     * Test writing of a performance message to performance log.
     */
    @Test
    public void testPerformanceMessage ()
    {
        // Setup SDT context for logging.
        SdtContext.getContext ().getLoggingContext ().setLoggingFlags (Integer.MAX_VALUE);
        SdtContext.getContext ().getLoggingContext ().setMajorLoggingId (987);

        // Write message to 'performance.log'.
        PerformanceLogger.log (this.getClass (), PerformanceLogger.LOGGING_POINT_1, "Hullo", " - Detail");

        // Validate the test.
        this.checkLogContents ("active flags=1111111111111111, logging point=0000000000000001, "
                + "reference=987: Hullo - Detail");
    }

    /**
     * Check that the log has the right contents.
     * 
     * @param contents contents to search for in the log file.
     */
    private void checkLogContents (final String contents)
    {
        // Check correct message is in log.
        try
        {
            final String text = new Scanner (new File (PERFORMANCE_LOG_PATH)).useDelimiter ("\\A").next ();

            // Build a search pattern with this request id. Allow for any order of requestId and requestType
            // attributes.
            final Pattern pattern = Pattern.compile (".*" + contents + ".*");

            // Match it against the result of all previous match replacements.
            final Matcher matcher = pattern.matcher (text);

            Assert.assertTrue ("Failed to find message [" + contents + "] in performance log", matcher.find ());
        }
        catch (final Exception e)
        {
            Assert.fail ("Failed to find contents [" + contents + "] in performance log.");
        }
    }
}
