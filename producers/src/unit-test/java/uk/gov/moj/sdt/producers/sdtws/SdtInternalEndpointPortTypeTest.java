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
 * $Id$
 * $LastChangedRevision$
 * $LastChangedDate$
 * $LastChangedBy$ */
package uk.gov.moj.sdt.producers.sdtws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import uk.gov.moj.sdt.handlers.api.IWsUpdateItemHandler;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusCodeType;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusType;
import uk.gov.moj.sdt.ws._2013.sdt.individualupdaterequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.individualupdaterequestschema.UpdateRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.individualupdateresponseschema.UpdateResponseType;

/**
 * Unit test for {@link SdtInternalEndpointPortType}.
 *
 * @author d276205
 */
public class SdtInternalEndpointPortTypeTest extends AbstractSdtUnitTestBase {

    /**
     * Test subject.
     */
    private SdtInternalEndpointPortType portType;

    /**
     * Mocked IWsUpdateItemHandler instance.
     */
    private IWsUpdateItemHandler mockUpdateItemHandler;

    /**
     * Set up common for all tests.
     */
    @Before
    public void setUp() {
        portType = new SdtInternalEndpointPortType();

        mockUpdateItemHandler = EasyMock.createMock(IWsUpdateItemHandler.class);
        portType.setUpdateItemHandler(mockUpdateItemHandler);

    }

    /**
     * Test update item method completes successfully.
     */
    @Test
    public void testUpdateItemSuccess() {
        EasyMock.expect(mockUpdateItemHandler.updateItem(EasyMock.anyObject(UpdateRequestType.class))).andReturn(
                createUpdateResponse());
        EasyMock.replay(mockUpdateItemHandler);

        final UpdateResponseType response = portType.updateItem(createUpdateRequest());
        EasyMock.verify(mockUpdateItemHandler);
        assertNotNull("Response expected", response);
    }

    /**
     * Test update item method handles exceptions successfully.
     */
    @Test
    public void testUpdateItemException() {
        EasyMock.expect(mockUpdateItemHandler.updateItem(EasyMock.anyObject(UpdateRequestType.class))).andThrow(
                new RuntimeException("test"));
        EasyMock.replay(mockUpdateItemHandler);

        try {
            portType.updateItem(createUpdateRequest());
            fail("Runtime exception should have been thrown");
        } catch (final RuntimeException re) {
            assertEquals("",
                    "A SDT system component error has occurred. Please contact the SDT support team for assistance",
                    re.getMessage());
        }

        EasyMock.verify(mockUpdateItemHandler);
    }

    /**
     * Creates dummy request.
     *
     * @return dummy request.
     */
    private UpdateRequestType createUpdateRequest() {
        final UpdateRequestType request = new UpdateRequestType();

        final HeaderType header = new HeaderType();
        header.setSdtRequestId("12345678");

        request.setHeader(header);
        return request;
    }

    /**
     * Creates dummy response.
     *
     * @return dummy response.
     */
    private UpdateResponseType createUpdateResponse() {
        final UpdateResponseType response = new UpdateResponseType();

        final StatusType statusType = new StatusType();
        statusType.setCode(StatusCodeType.OK);
        response.setStatus(statusType);
        return response;
    }

}
