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
package uk.gov.moj.sdt.validation;

import uk.gov.moj.sdt.utils.SdtXmlTestBase;

/**
 * Tests for individual submission requests for claim, judgment etc.
 * 
 * @author Saurabh Agarwal
 * 
 */
public class IndividualSubmitRequestXsdTest extends SdtXmlTestBase
{

    /**
     * The name of the xsd.
     */
    private static final String XSD_NAME = "IndividualSubmitRequest";

    /**
     * The name of the folder where XSD is stored.
     */
    private static final String XSD_DIR = "IndvRequestResponse/";

    /**
     * The name of the folder where all valid/invalid XML is stored.
     */
    private static final String XML_DIR = "IndvRequestResponse/";

    /**
     * The path of the xsd file.
     */
    private static final String XSD_PATH = XSD_DIR + XSD_NAME + ".xsd";

    /**
     * Constructs a new {@link IndividualSubmitRequestXsdTest}.
     * 
     * @param testName Name of this test.
     */
    public IndividualSubmitRequestXsdTest (final String testName)
    {
        super (testName);
    }

    /**
     * Tests XML file is valid.
     */
    public void testValidForClaimXml ()
    {
        final String condition = "ForClaimValid";
        final String xmlPath = XML_DIR + XSD_NAME + condition + SdtXmlTestBase.XML_FILE_SUFFIX;
        this.validateXsd (xmlPath, XSD_PATH, null);
    }

    /**
     * Tests that expected errors are reported for missing mandatory fields.
     */
    public void testInvalidXmlForClaimMandatoryMissing ()
    {
        final String condition = "ForClaimMandatoryMissing";
        final String xmlPath = XML_DIR + XSD_NAME + condition + SdtXmlTestBase.XML_FILE_SUFFIX;
        final String errorFilePathname = XML_DIR + XSD_NAME + condition + SdtXmlTestBase.ERROR_FILE_SUFFIX;

        this.validateXsd (xmlPath, XSD_PATH, errorFilePathname);
    }

    /**
     * Tests XML file is valid.
     */
    public void testValidForJudgmentXml ()
    {
        final String condition = "ForJudgmentValid";
        final String xmlPath = XML_DIR + XSD_NAME + condition + SdtXmlTestBase.XML_FILE_SUFFIX;
        this.validateXsd (xmlPath, XSD_PATH, null);
    }

    /**
     * Tests that expected errors are reported for missing mandatory fields.
     */
    public void testInvalidXmlForJudgmentMandatoryMissing ()
    {
        final String condition = "ForJudgmentMandatoryMissing";
        final String xmlPath = XML_DIR + XSD_NAME + condition + SdtXmlTestBase.XML_FILE_SUFFIX;
        final String errorFilePathname = XML_DIR + XSD_NAME + condition + SdtXmlTestBase.ERROR_FILE_SUFFIX;

        this.validateXsd (xmlPath, XSD_PATH, errorFilePathname);
    }

    /**
     * Tests XML file is valid.
     */
    public void testValidForJudgmentForthwithXml ()
    {
        final String condition = "ForJudgmentForthwithValid";
        final String xmlPath = XML_DIR + XSD_NAME + condition + SdtXmlTestBase.XML_FILE_SUFFIX;
        this.validateXsd (xmlPath, XSD_PATH, null);
    }

    /**
     * Tests that expected errors are reported for missing mandatory fields.
     */
    public void testInvalidXmlForJudgmentForthwithMandatoryMissing ()
    {
        final String condition = "ForJudgmentForthwithMandatoryMissing";
        final String xmlPath = XML_DIR + XSD_NAME + condition + SdtXmlTestBase.XML_FILE_SUFFIX;
        final String errorFilePathname = XML_DIR + XSD_NAME + condition + SdtXmlTestBase.ERROR_FILE_SUFFIX;

        this.validateXsd (xmlPath, XSD_PATH, errorFilePathname);
    }

    /**
     * Tests XML file is valid.
     */
    public void testValidForWarrantXml ()
    {
        final String condition = "ForWarrantValid";
        final String xmlPath = XML_DIR + XSD_NAME + condition + SdtXmlTestBase.XML_FILE_SUFFIX;
        this.validateXsd (xmlPath, XSD_PATH, null);
    }

    /**
     * Tests that expected errors are reported for missing mandatory fields.
     */
    public void testInvalidXmlForWarrantMandatoryMissing ()
    {
        final String condition = "ForWarrantMandatoryMissing";
        final String xmlPath = XML_DIR + XSD_NAME + condition + SdtXmlTestBase.XML_FILE_SUFFIX;
        final String errorFilePathname = XML_DIR + XSD_NAME + condition + SdtXmlTestBase.ERROR_FILE_SUFFIX;

        this.validateXsd (xmlPath, XSD_PATH, errorFilePathname);
    }

    /**
     * Tests XML file is valid.
     */
    public void testValidForClaimUpdateXml ()
    {
        final String condition = "ForClaimUpdateValid";
        final String xmlPath = XML_DIR + XSD_NAME + condition + SdtXmlTestBase.XML_FILE_SUFFIX;
        this.validateXsd (xmlPath, XSD_PATH, null);
    }

    /**
     * Tests that expected errors are reported for missing mandatory fields.
     */
    public void testInvalidXmlForClaimUpdateMandatoryMissing ()
    {
        final String condition = "ForClaimUpdateMandatoryMissing";
        final String xmlPath = XML_DIR + XSD_NAME + condition + SdtXmlTestBase.XML_FILE_SUFFIX;
        final String errorFilePathname = XML_DIR + XSD_NAME + condition + SdtXmlTestBase.ERROR_FILE_SUFFIX;

        this.validateXsd (xmlPath, XSD_PATH, errorFilePathname);
    }

    /**
     * Tests that expected errors are reported for incorrect format of fields.
     */
    public void testInvalidXmlIncorrectFormat ()
    {
        final String condition = "IncorrectFormat";
        final String xmlPath = XML_DIR + XSD_NAME + condition + SdtXmlTestBase.XML_FILE_SUFFIX;
        final String errorFilePathname = XML_DIR + XSD_NAME + condition + SdtXmlTestBase.ERROR_FILE_SUFFIX;

        this.validateXsd (xmlPath, XSD_PATH, errorFilePathname);
    }

}
