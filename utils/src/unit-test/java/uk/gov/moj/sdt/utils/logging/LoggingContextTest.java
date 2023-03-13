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

import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.logging.api.ILoggingContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoggingContextTest extends AbstractSdtUnitTestBase {

    ILoggingContext loggingContext;

    @Override
    public void setUpLocalTests() {
        loggingContext = new LoggingContext();
    }

    @Test
    void setMinorLoggingIdTest() {

        //given

        //when
        loggingContext.setMinorLoggingId(1L);
        //then
        assertEquals(1L, loggingContext.getMinorLoggingId());
    }

    @Test
    void setMajorLoggingIdTest() {

        //given

        //when
        loggingContext.setMajorLoggingId(1L);
        //then
        assertEquals(1L, loggingContext.getMajorLoggingId());
    }

    @Test
    void getLoggingIdTest() {

        //given

        //when
        loggingContext.setMinorLoggingId(1L);
        loggingContext.setMajorLoggingId(2L);
        //then
        assertEquals("2.1", loggingContext.getLoggingId());
    }

    @Test
    void getLoggingMajorIdOnlyTest() {

        //given
        //when
        loggingContext.setMinorLoggingId(0L);
        loggingContext.setMajorLoggingId(2L);
        //then
        assertEquals("2", loggingContext.getLoggingId());
    }

    @Test
    void getNextLoggingIdTest() {

        // Threads created by mutation tests cause the static logging id to be incremented more than once.  To
        // prevent "Test will only pass if mutated" error during mutation test just check that logging id is
        // incremented by getNextLoggingId() rather than the value returned being equal to an expected value.
        long loggingId = LoggingContext.getNextLoggingId();
        assertTrue(LoggingContext.getNextLoggingId() > loggingId, "LoggingId should have been incremented");
    }
}
