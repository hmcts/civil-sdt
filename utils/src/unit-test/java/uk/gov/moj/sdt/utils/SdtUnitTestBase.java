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

import junit.framework.TestCase;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class represents base class for all unit test cases.
 * 
 * @author Robin Compston
 */
public class SdtUnitTestBase extends TestCase
{
    /**
     * Static logging object.
     */
    private static final Logger LOG = LoggerFactory.getLogger (SdtUnitTestBase.class);

    /**
     * Name of log4j configuration file (in classpath).
     */
    private static final String LOG4J_XML_FILE = "../utils/src/main/java/log4j.xml";

    /**
     * Constructs a new {@link SdtUnitTestBase}.
     * 
     * @param testName name of test.
     */
    public SdtUnitTestBase (final String testName)
    {
        super (testName);

        this.init ();
    }

    /**
     * Do one time initialization for this test class.
     */
    private void init ()
    {
        // // Now load the properties for this test.
        // this.testProperties = new Properties ();

        // Initialize log4j for test purposes.
        DOMConfigurator.configure (LOG4J_XML_FILE);
    }

    /**
     * Standard JUnit setUp method. Gets run before each test method.
     * 
     * @throws Exception
     *             May throw runtime exception.
     */
    @Override
    protected void setUp () throws Exception
    {
        super.setUp ();

        SdtUnitTestBase.LOG.debug ("Start Test: " + this.getClass ().getCanonicalName () + "." + this.getName ());

        // Local set up if we are running outside of the appserver.
        this.setUpLocalTests ();
    }

    /**
     * Method to be overridden in derived class to do test specific setup.
     * 
     * @throws Exception
     *             general problem.
     */
    protected void setUpLocalTests () throws Exception
    {
    }

    /**
     * Convert windows String to UNIX by removing all Hex 0d.
     * 
     * @param in String from Windows platform.
     * @return String for UNIX platrofm.
     */
    protected String dos2Unix (final String in)
    {
        // Array to hold input.
        final char[] win = in.toCharArray ();
        final int len = in.length ();

        // Array to hold output.
        final char[] unix = new char[len];

        int pos2 = 0;
        for (int pos1 = 0; pos1 < len; pos1++)
        {
            // Copy all but CARRIAGE RETURN.
            if (win[pos1] != '\r')
            {
                unix[pos2++] = win[pos1];
            }
        }

        return new String (unix);
    }

//    /**
//     * Returns a required property.
//     * 
//     * @param key
//     *            The property key.
//     * @return The property value - not null or empty.
//     * @throws ConfigException
//     *             If there is an exception accessing data.
//     */
//    protected String getRequiredProperty (final String key)
//    {
//        if (key == null || "".equals (key))
//        {
//            throw new IllegalStateException ("Key argument cannot be empty or null");
//        }
//        if (this.testProperties == null)
//        {
//            throw new NullPointerException ("Reference to properties file was unexpectedly null");
//        }
//        final String val = this.testProperties.getProperty (key);
//        if (val == null || "".equals (val))
//        {
//            SdtTestBase.LOG.debug ("Test name = " + this.getName ());
//
//            throw new IllegalStateException ("Value with key '" + key + "' cannot be null or empty");
//        }
//        return val;
//    }
}
