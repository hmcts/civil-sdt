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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.SdtContext;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

/**
 * Test class for the ContextCleanupFilter.
 *
 * @author Manoj Kulkarni
 */
@ExtendWith(MockitoExtension.class)
class ContextCleanupFilterTest extends AbstractSdtUnitTestBase {

    /**
     * Mocked servlet request for testing.
     */
    @Mock
    private ServletRequest mockServletRequest;

    /**
     * Mocked servlet response for testing.
     */
    @Mock
    private ServletResponse mockServletResponse;

    /**
     * Mocked filter chain for testing.
     */
    @Mock
    private FilterChain mockFilterChain;

    /**
     * ContextCleanupFilter instance for testing.
     */
    private ContextCleanupFilter contextCleanupFilter;

    /**
     * Method called before the test methods.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test method for the doFilter method of the filter.
     *
     * @throws ServletException the exception thrown by the doFilter
     * @throws IOException      the exception thrown on IO problems
     */
    @Test
    @Disabled("temporarily")
    void doFilterTest() throws IOException, ServletException {
        // Add code to set something in SdtContext.
        SdtContext.getContext().setRawInXml("TestXml");

        assertNotNull(SdtContext.getContext().getRawInXml());

        contextCleanupFilter = new ContextCleanupFilter();

        mockFilterChain.doFilter(mockServletRequest, mockServletResponse);

        //EasyMock.expectLastCall();

        contextCleanupFilter.doFilter(mockServletRequest, mockServletResponse, mockFilterChain);

        verify(mockServletRequest);
        verify(mockServletResponse);
        verify(mockFilterChain);

        // Add code to verify that the entities in SdtContext are removed.
        assertNull(SdtContext.getContext().getRawInXml(), "Sdt Context not cleaned up");

        assertTrue(true, "Test completed successfully");
    }

    /**
     * Test method for the doFilter method of the filter when the doFilter throws
     * an exception but still clears sdt context.
     *
     * @throws ServletException the exception thrown by the doFilter
     * @throws IOException      the exception thrown on IO problems
     */
    @Test
    @Disabled("temporarily")
    void doFilterTestForError() throws IOException, ServletException {
        // Add code to set something in SdtContext.
        SdtContext.getContext().setRawInXml("TestXml");

        assertNotNull(SdtContext.getContext().getRawInXml());

        contextCleanupFilter = new ContextCleanupFilter();

        mockFilterChain.doFilter(mockServletRequest, mockServletResponse);

       // EasyMock.expectLastCall().andThrow(new ServletException("Error"));
//        verify().

        try {
            contextCleanupFilter.doFilter(mockServletRequest, mockServletResponse, mockFilterChain);
        } catch (final ServletException e) {
            assertTrue(true);
        }

        verify(mockServletRequest);
        verify(mockServletResponse);
        verify(mockFilterChain);

        // Add code to verify that the entities in SdtContext are removed.
        assertNull(SdtContext.getContext().getRawInXml(), "Sdt Context expected to be cleaned up");
    }

}
