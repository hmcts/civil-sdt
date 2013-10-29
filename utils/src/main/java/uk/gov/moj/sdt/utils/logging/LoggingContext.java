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

import uk.gov.moj.sdt.utils.logging.api.ILoggingContext;

/**
 * Object stored in thread local holding performance logging information.
 * 
 * @author Robin Compston
 */
public class LoggingContext implements ILoggingContext
{
    /**
     * {@link com.logica.ibra.ext.model.api.IIbraontext#SERIAL_VERSION_UID}.
     */
    private static final long serialVersionUID = ILoggingContext.SERIAL_VERSION_UID;

    /**
     * Global logging id used by all threads.
     */
    private static long nextLoggingId;

    /**
     * The name of the property <code>loggingFlags</code>.
     */
    // TODO turn off.
    private long loggingFlags = Long.MAX_VALUE;

    /**
     * The name of the property <code>loggingId</code>.
     */
    private long loggingId;

    /**
     * Constructs a new {@link LoggingContext} instance.
     */
    public LoggingContext ()
    {
    }

    @Override
    public long getLoggingFlags ()
    {
        return this.loggingFlags;
    }

    @Override
    public void setLoggingFlags (final long loggingFlags)
    {
        this.loggingFlags = loggingFlags;
    }

    @Override
    public long getLoggingId ()
    {
        return this.loggingId;
    }

    @Override
    public void setLoggingId (final long loggingId)
    {
        this.loggingId = loggingId;
    }

    /**
     * Get the next logging id to be assigned to some new thread.
     * 
     * @return the next logging id.
     */
    public static synchronized long getNextLoggingId ()
    {
        return ++LoggingContext.nextLoggingId;
    }
}
