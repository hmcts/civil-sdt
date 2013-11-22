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
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.gov.moj.sdt.interceptors.AbstractSdtInterceptor;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.logging.LoggingContext;
import uk.gov.moj.sdt.utils.logging.PerformanceLogger;
import uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean;

/**
 * Class to intercept incoming messages and do performance logging. Should run before any changes are made to the
 * incoming message, i.e. first.
 * 
 * @author Robin Compston
 * 
 */
public class PerformanceLoggerInboundInterceptor extends AbstractSdtInterceptor
{

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (PerformanceLoggerInboundInterceptor.class);

    /**
     * Create instance of {@link PerformanceLoggerInboundInterceptor}.
     */
    public PerformanceLoggerInboundInterceptor ()
    {
        super (Phase.RECEIVE);
        addBefore (XmlInboundInterceptor.class.getName ());
    }

    /**
     * Create instance of {@link PerformanceLoggerInboundInterceptor}.
     * 
     * @param phase phase of the CXF interceptor chain in which this interceptor should run.
     */
    public PerformanceLoggerInboundInterceptor (final String phase)
    {
        super (phase);
    }

    @Override
    @Transactional (propagation = Propagation.REQUIRES_NEW)
    public void handleMessage (final SoapMessage message) throws Fault
    {
        // Setup logging flags from current value in SdtMetricsMBean for this thread - used by all subsequent processing
        // of this request.
        SdtContext.getContext ().getLoggingContext ()
                .setLoggingFlags (SdtMetricsMBean.getSdtMetrics ().getPerformanceLoggingFlags ());

        // Increment thread local logging id for this invocation, but only if we are at the start of a new thread of
        // work.
        if (SdtContext.getContext ().getLoggingContext ().getMinorLoggingId () == 0)
        {
            SdtContext.getContext ().getLoggingContext ().setMajorLoggingId (LoggingContext.getNextLoggingId ());
        }

        // Write message to 'performance.log' for this logging point.
        if (PerformanceLogger.isPerformanceEnabled (PerformanceLogger.LOGGING_POINT_1))
        {
            PerformanceLogger.log (this.getClass (), PerformanceLogger.LOGGING_POINT_1,
                    "PerformanceLoggerInboundInterceptor handling message",
                    "\n\n\t" + PerformanceLogger.format (this.readInputMessage (message)) + "\n");
        }
    }
}