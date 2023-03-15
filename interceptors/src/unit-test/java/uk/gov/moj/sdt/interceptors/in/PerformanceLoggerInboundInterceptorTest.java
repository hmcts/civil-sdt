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
package uk.gov.moj.sdt.interceptors.in;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.MessageImpl;
import org.apache.cxf.phase.Phase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.logging.PerformanceLogger;

import java.io.OutputStream;

import static org.mockito.ArgumentMatchers.*;

/**
 * Test class.
 *
 * @author d195274
 */
@ExtendWith(MockitoExtension.class)
class PerformanceLoggerInboundInterceptorTest extends AbstractSdtUnitTestBase {

    /**
     * Test that the process correctly works via mocked out extensions.
     */
    @Test
    void testHandleMessage() {
        final SoapMessage soapMessage = getDummySoapMessageWithCachedOutputStream();
        final PerformanceLoggerInboundInterceptor performanceLoggerInboundInterceptor =
            new PerformanceLoggerInboundInterceptor();

        try (MockedStatic<PerformanceLogger> mockedStaticPerformanceLogger =
                 Mockito.mockStatic(PerformanceLogger.class)) {

            mockedStaticPerformanceLogger.when(() -> PerformanceLogger
                .isPerformanceEnabled(PerformanceLogger.LOGGING_POINT_1))
                .thenReturn(true);

            performanceLoggerInboundInterceptor.handleMessage(soapMessage);

            mockedStaticPerformanceLogger.verify(() -> PerformanceLogger
                .isPerformanceEnabled(PerformanceLogger.LOGGING_POINT_1));
            mockedStaticPerformanceLogger.verify(() -> PerformanceLogger
                .log(any(), anyLong(), anyString(), anyString() ));
        }
    }

    @Test
    void testHandleMessagePhaseConstructor() {
        final PerformanceLoggerInboundInterceptor performanceLoggerInboundInterceptor =
            new PerformanceLoggerInboundInterceptor(Phase.INVOKE);

        Assertions.assertNotNull(performanceLoggerInboundInterceptor);
    }

    /**
     * Builds a dummy soap message
     *
     * * @return a soap messaeg
     */
    private SoapMessage getDummySoapMessageWithCachedOutputStream() {
        SoapMessage soapMessage = new SoapMessage(new MessageImpl());
        CachedOutputStream cachedOutputStream = new CachedOutputStream();
        soapMessage.setContent(OutputStream.class, cachedOutputStream);
        return soapMessage;
    }
}
