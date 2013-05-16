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
package uk.gov.moj.sdt.validation;

import java.util.ArrayList;
import java.util.List;

import uk.gov.moj.sdt.utils.SdtXmlConstants;
import uk.gov.moj.sdt.utils.SdtXmlTestBase;

/**
 * Test case for IndividualUpdateRequestXsdTest.
 * 
 * @author Simon Holmes
 * 
 */
public class DefenceFeedbackResponseMcolXsdTest extends SdtXmlTestBase
{

    /**
     * The name of the service.
     */
    private String xsdName = "DefenceFeedbackResponseMCOL";

    /**
     * The name of the folder where all valid/invalid XML is stored.
     */
    private String xmlFolderName = "DefenceRequest&Response" + "/";

    /**
     * The path of the xsd file.
     */
    private String xsdPath = xmlFolderName + xsdName + ".xsd";

    /**
     * Constructs a new {@link DefenceFeedbackResponseMcolXsdTest}.
     * 
     * @param testName Name of this test.
     */
    public DefenceFeedbackResponseMcolXsdTest (final String testName)
    {
        super (testName);
    }

    /**
     * {@inheritDoc}
     */
    public void testValidXml ()
    {
        final String xmlPath = xmlFolderName + xsdName + "Valid.xml";
        this.proveXsd (xmlPath, xsdPath, null);
    }

    /**
     * {@inheritDoc}
     */
    public void testInvalidXmlMandatory ()
    {

    }

    /**
     * {@inheritDoc}
     */
    public void testInvalidRange ()
    {
        final String errorDetails = "Range";
        final String xmlPath = xmlFolderName + xsdName + errorDetails + "Invalid.xml";

        final List<String> expectedMessages = new ArrayList<String> ();
        expectedMessages.add (SdtXmlConstants.CLAIM_NUMBER_PATTERN_INVALID);
        expectedMessages.add (SdtXmlConstants.DEFENDANT_ID_VALUE_INVALID);
        expectedMessages.add (SdtXmlConstants.DATE_TIME_INVALID_FORMAT);
        expectedMessages.add (SdtXmlConstants.RESPONSE_TYPE_NOT_VALID);
        expectedMessages.add (SdtXmlConstants.STATUS_CODE_INVALID);
        expectedMessages.add (SdtXmlConstants.ERROR_CODE_INVALID);
        expectedMessages.add (SdtXmlConstants.ERROR_DESCRIPTION_INVALID);

        this.proveXsd (xmlPath, xsdPath, expectedMessages);
    }
}
