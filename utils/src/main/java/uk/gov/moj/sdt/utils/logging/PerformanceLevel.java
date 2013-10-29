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

import org.apache.log4j.Level;

/**
 * Class to support log4j logging level associated with performance logging.
 * 
 * @author Robin Compston.
 */
public class PerformanceLevel extends Level
{
    /**
     * Value of performance level. This value is slightly higher than {@link org.apache.log4j.Priority#INFO_INT}.
     */
    public static final int PERFORMANCE_LEVEL_INT = Level.FATAL_INT + 1;

    /**
     * {@link Level} representing my log level.
     */
    public static final Level PERFORMANCE = new PerformanceLevel (PERFORMANCE_LEVEL_INT, "PERFORMANCE", 7);

    /**
     * Message to appear in log record associated with this level.
     */
    private static final String PERFORMANCE_MSG = "PERFORMANCE";

    /**
     * Serialisable uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     * 
     * @param arg0 level.
     * @param arg1 level string.
     * @param arg2 syslog equivalent.
     */
    protected PerformanceLevel (final int arg0, final String arg1, final int arg2)
    {
        super (arg0, arg1, arg2);

    }

    /**
     * Checks whether <code>sArg</code> is "PERFORMANCE" level. If yes then returns {@link PerformanceLevel#SECURITY},
     * else calls {@link PerformanceLevel#toLevel(String, Level)} passing it {@link Level#DEBUG} as the defaultLevel
     * 
     * @see Level#toLevel(java.lang.String)
     * @see Level#toLevel(java.lang.String, org.apache.log4j.Level)
     * 
     * @param sArg level string to check.
     * @return instance of {@link Level}.
     */
    public static Level toLevel (final String sArg)
    {
        if (sArg != null && sArg.toUpperCase ().equals (PERFORMANCE_MSG))
        {
            return PERFORMANCE;
        }
        return (Level) toLevel (sArg, Level.DEBUG);
    }

    /**
     * Checks whether <code>val</code> is {@link PerformanceLevel#PERFORMANCE_LEVEL_INT}. If yes then returns
     * {@link PerformanceLevel#PERFORMANCE},
     * else calls {@link PerformanceLevel#toLevel(int, Level)} passing it {@link Level#DEBUG} as the defaultLevel
     * 
     * @see Level#toLevel(int)
     * @see Level#toLevel(int, org.apache.log4j.Level)
     * 
     * @param val level value to check.
     * @return instance of {@link Level}.
     */
    public static Level toLevel (final int val)
    {
        if (val == PERFORMANCE_LEVEL_INT)
        {
            return PERFORMANCE;
        }
        return (Level) toLevel (val, Level.DEBUG);
    }

    /**
     * Checks whether <code>val</code> is {@link PerformanceLevel#PERFORMANCE_LEVEL_INT}. If yes then returns
     * {@link PerformanceLevel#PERFORMANCE},
     * else calls {@link Level#toLevel(int, org.apache.log4j.Level)}
     * 
     * @see Level#toLevel(int, org.apache.log4j.Level)
     * 
     * @param val level value to check.
     * @param defaultLevel {@link Level} to use.
     * @return instance of {@link Level}.
     */
    public static Level toLevel (final int val, final Level defaultLevel)
    {
        if (val == PERFORMANCE_LEVEL_INT)
        {
            return PERFORMANCE;
        }
        return Level.toLevel (val, defaultLevel);
    }

    /**
     * Checks whether <code>sArg</code> is "PERFORMANCE" level. If yes then returns {@link PerformanceLevel#SECURITY},
     * else calls {@link Level#toLevel(java.lang.String, org.apache.log4j.Level)}
     * 
     * @see Level#toLevel(java.lang.String, org.apache.log4j.Level)
     * 
     * @param sArg level string to check.
     * @param defaultLevel {@link Level} to use.
     * @return instance of {@link Level}.
     */
    public static Level toLevel (final String sArg, final Level defaultLevel)
    {
        if (sArg != null && sArg.toUpperCase ().equals (PERFORMANCE_MSG))
        {
            return PERFORMANCE;
        }
        return Level.toLevel (sArg, defaultLevel);
    }
}
