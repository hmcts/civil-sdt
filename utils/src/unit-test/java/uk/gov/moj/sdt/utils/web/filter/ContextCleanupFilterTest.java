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
package uk.gov.moj.sdt.utils.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.utils.SdtContext;

/**
 * Test class for the ContextCleanupFilter.
 * 
 * @author Manoj Kulkarni
 * 
 */
public class ContextCleanupFilterTest
{
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (ContextCleanupFilterTest.class);

    /**
     * ContextCleanupFilter instance for testing.
     */
    private ContextCleanupFilter contextCleanupFilter;

    /**
     * Mocked servlet request for testing.
     */
    private ServletRequest mockServletRequest;

    /**
     * Mocked servlet response for testing.
     */
    private ServletResponse mockServletResponse;

    /**
     * Mocked filter chain for testing.
     */
    private FilterChain mockFilterChain;

    /**
     * Method called before the test methods.
     */
    @Before
    public void setUp ()
    {
        mockServletRequest = EasyMock.createMock (ServletRequest.class);
        mockServletResponse = EasyMock.createMock (ServletResponse.class);
        mockFilterChain = EasyMock.createMock (FilterChain.class);
    }

    /**
     * Test method for the doFilter method of the filter.
     * 
     * @throws ServletException the exception thrown by the doFilter
     * @throws IOException the exception thrown on IO problems
     */
    @Test
    public void doFilterTest () throws IOException, ServletException
    {
        // Add code to set something in SdtContext.
        SdtContext.getContext ().setRawInXml ("TestXml");

        Assert.assertNotNull (SdtContext.getContext ().getRawInXml ());

        contextCleanupFilter = new ContextCleanupFilter ();

        mockFilterChain.doFilter (mockServletRequest, mockServletResponse);

        EasyMock.expectLastCall ();

        EasyMock.replay (mockServletRequest);
        EasyMock.replay (mockServletResponse);
        EasyMock.replay (mockFilterChain);

        contextCleanupFilter.doFilter (mockServletRequest, mockServletResponse, mockFilterChain);

        EasyMock.verify (mockServletRequest);
        EasyMock.verify (mockServletResponse);
        EasyMock.verify (mockFilterChain);

        // Add code to verify that the entities in SdtContext are removed.
        Assert.assertNull ("Sdt Context not cleaned up", SdtContext.getContext ().getRawInXml ());

        Assert.assertTrue ("Test completed successfully", true);

    }

    /**
     * Test method for the doFilter method of the filter when the doFilter throws
     * an exception.
     * 
     * @throws ServletException the exception thrown by the doFilter
     * @throws IOException the exception thrown on IO problems
     */
    @Test
    public void doFilterTestForError () throws IOException, ServletException
    {
        // Add code to set something in SdtContext.
        SdtContext.getContext ().setRawInXml ("TestXml");

        Assert.assertNotNull (SdtContext.getContext ().getRawInXml ());

        contextCleanupFilter = new ContextCleanupFilter ();

        mockFilterChain.doFilter (mockServletRequest, mockServletResponse);

        EasyMock.expectLastCall ().andThrow (new ServletException ("Error"));

        EasyMock.replay (mockServletRequest);
        EasyMock.replay (mockServletResponse);
        EasyMock.replay (mockFilterChain);

        try
        {
            contextCleanupFilter.doFilter (mockServletRequest, mockServletResponse, mockFilterChain);
        }
        catch (final ServletException e)
        {
            Assert.assertTrue (true);
        }

        EasyMock.verify (mockServletRequest);
        EasyMock.verify (mockServletResponse);
        EasyMock.verify (mockFilterChain);

        // Add code to verify that the entities in SdtContext are not removed.
        Assert.assertNotNull ("Sdt Context not expected to be cleaned up", SdtContext.getContext ().getRawInXml ());

        Assert.assertTrue ("Test completed successfully", true);

    }

}
