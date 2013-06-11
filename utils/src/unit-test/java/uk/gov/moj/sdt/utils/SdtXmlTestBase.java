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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import uk.gov.moj.sdt.utilities.Utilities;

/**
 * A base test class that compares XML file output.
 * 
 * @author Robin Compston
 */
public class SdtXmlTestBase extends AbstractSdtGoodFileTestBase
{
    /**
     * Directory containing 'good' files against which new XML output is checked
     * when running XSLT transforms.
     */
    public static final String XML_GOOD_DIR = "./test/xml/good";

    /**
     * Directory to which XML is written by tests when running XSLT transforms.
     */
    public static final String XML_OUTPUT_DIR = "./test/xml/output";

    /**
     * Directory containing XML files used to validate XSD files.
     */
    public static final String XML_VALIDATION_DIR = "src/unit-test/resources/xml/validation";

    /**
     * Directory containing XML files used to validate XSD files.
     */
    public static final String XSD_DIR = "src/main/resources/xsd";

    /**
     * Suffix for XML files.
     */
    public static final String XML_FILE_SUFFIX = ".xml";

    /**
     * Suffix for file containing error messages.
     */
    public static final String ERROR_FILE_SUFFIX = "ErrorMessages.txt";

    /**
     * Logging object.
     */
    private static final Logger LOG = LoggerFactory.getLogger (SdtXmlTestBase.class);

    /**
     * Flag to detect whether parsing error was encountered.
     */
    private boolean errorEncountered;

    /**
     * Constructs a new {@link SdtXmlTestBase}.
     * 
     * @param testName
     *            Name of this test.
     */
    public SdtXmlTestBase (final String testName)
    {
        super (testName);
    }

    /**
     * For now we just need to return true, tests inheriting this will need to
     * change it.
     * 
     * @param line
     *            the line to be checked for inclusion.
     * @return true - include line, false - do not include line.
     */
    protected boolean includeLine (final String line)
    {
        return true;
    }

    /**
     * Get the start message from the output file.
     * 
     * @return Return the start string.
     */
    public String getTestStartString ()
    {
        return null;
    }

    /**
     * Get the completed message from the output file.
     * 
     * @return Return the completed string.
     */
    public String getTestCompletedString ()
    {
        return null;
    }

    /**
     * Compares the XML output files from a test with a previously stored XML
     * from a successful run of the test and returns the result.
     * 
     * @param xmlFileName
     *            Name of output file.
     * @param xmlDocument
     *            Document object that represents the output file.
     * @param useDelimiters
     *            Whether to only do comparison within scope of start and
     *            complete markers.
     * @return true - files match, false - files do not match.
     */
    public boolean compareTestOutputFiles (final String xmlFileName, final Document xmlDocument,
                                           final boolean useDelimiters)
    {
        try
        {
            // Make sure the expected XML output file exists.
            final String xmlOutPath =
                    Utilities.checkFileExists (SdtXmlTestBase.XML_OUTPUT_DIR, xmlFileName + ".out", true);

            // Create Writer for the formatter to use.
            SdtXmlTestBase.LOG.debug ("File to write is: " + xmlOutPath);
            final BufferedWriter bw = new BufferedWriter (new FileWriter (xmlOutPath));

            // Format XML output as pretty.
            final XMLOutputter xmlOut = new XMLOutputter (Format.getPrettyFormat ());
            xmlOut.output (xmlDocument, bw);
            bw.close ();

            // Check good file also exists.
            SdtXmlTestBase.LOG.debug ("Written file: " + xmlOutPath);
            final String xmlGoodPath =
                    Utilities.checkFileExists (SdtXmlTestBase.XML_GOOD_DIR, xmlFileName + ".good", false);

            // compare the output and the good file.
            SdtXmlTestBase.LOG.info ("Comparing XML file " + xmlOutPath + " against good file " + xmlGoodPath);
            return this.compareTestOutputFiles (xmlOutPath, xmlGoodPath, useDelimiters);
        }
        catch (final IOException e)
        {
            SdtXmlTestBase.LOG.error ("Exception: ", e);
            return false;
        }
    }

    /**
     * Finds xml and xsd and creates the parser to validate xml.
     * 
     * @param xmlPathname the path of the xml.
     * @param xsdPathname the path of the xsd.
     * @param expectedMessages the expected error message(s).
     * @throws IOException IOException.
     * @throws SAXException SAXException.
     * @throws ParserConfigurationException ParserConfigurationException.
     */
    private void evaluateXsd (final String xmlPathname, final String xsdPathname, final List<String> expectedMessages)
        throws IOException, SAXException, ParserConfigurationException
    {
        errorEncountered = false;

        // Make sure the XML file to be validated exists.
        final String xmlPath = Utilities.checkFileExists (SdtXmlTestBase.XML_VALIDATION_DIR, xmlPathname, false);

        // Make sure the XSD file that is to be checked exists.
        final String xsdPath = Utilities.checkFileExists (SdtXmlTestBase.XSD_DIR, xsdPathname, false);

        // Create schema factory amd set XSD file as the schema for factory to
        // create.
        final SchemaFactory schemaFactory = SchemaFactory.newInstance (XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final Schema schema = schemaFactory.newSchema (new File (xsdPath));

        // Create SAX parser factory; turn on validation and check all name
        // spaces; use given schema for validation.
        final SAXParserFactory saxFactory = SAXParserFactory.newInstance ();
        // saxFactory.setValidating (true);
        saxFactory.setNamespaceAware (true);
        saxFactory.setSchema (schema);

        // Create SAX parser to do validation (not DOM as we do not need to
        // create a document).
        final SAXParser parser = saxFactory.newSAXParser ();
        parseXml (parser, xmlPath, expectedMessages);
    }

    /**
     * Finds xml and xsd and creates the parser to validate xml.
     * 
     * @param xmlPathname the path of the xml.
     * @param xsdPathname the path of the xsd.
     * @param errorFilePathname the path of the file containing expected error message(s).
     * @throws IOException IOException.
     * @throws SAXException SAXException.
     * @throws ParserConfigurationException ParserConfigurationException.
     */
    private void evaluateXsd (final String xmlPathname, final String xsdPathname, final String errorFilePathname)
        throws IOException, SAXException, ParserConfigurationException
    {
        errorEncountered = false;

        // Make sure the XML file to be validated exists.
        final String xmlPath = Utilities.checkFileExists (SdtXmlTestBase.XML_VALIDATION_DIR, xmlPathname, false);

        // Make sure the XSD file that is to be checked exists.
        final String xsdPath = Utilities.checkFileExists (SdtXmlTestBase.XSD_DIR, xsdPathname, false);

        final String expectedErrorMessages = getExpectedErrorMessages (errorFilePathname);

        // Create schema factory amd set XSD file as the schema for factory to
        // create.
        final SchemaFactory schemaFactory = SchemaFactory.newInstance (XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final Schema schema = schemaFactory.newSchema (new File (xsdPath));

        // Create SAX parser factory; turn on validation and check all name
        // spaces; use given schema for validation.
        final SAXParserFactory saxFactory = SAXParserFactory.newInstance ();
        // saxFactory.setValidating (true);
        saxFactory.setNamespaceAware (true);
        saxFactory.setSchema (schema);

        // Create SAX parser to do validation (not DOM as we do not need to
        // create a document).
        final SAXParser parser = saxFactory.newSAXParser ();
        parseXml (parser, xmlPath, expectedErrorMessages);
    }

    /**
     * The actually reads the xml against the xsd.
     * 
     * @param parser the parser.
     * @param xmlPath The location of the xml.
     * @param expectedMessages the expected error message(s).
     * @throws IOException IOException.
     * @throws SAXException SAXException.
     */
    private void parseXml (final SAXParser parser, final String xmlPath, final List<String> expectedMessages)
        throws IOException, SAXException
    {
        // Use the XSD file to validate the proof XML.
        parser.parse (xmlPath, new DefaultHandler ()
        {
            // Anonymous Default Handler to implement required error logic.
            public void error (final SAXParseException e) throws SAXException
            {
                // Record that we hit an error during parsing.
                SdtXmlTestBase.this.errorEncountered = true;

                // Was it the wrong error?
                if (expectedMessages != null && expectedMessages.contains (e.getMessage ()))
                {
                    expectedMessages.remove (e.getMessage ());
                }
                else
                {
                    LOG.error ("Parser encountered unexpected error - expected message: [" + e.getMessage () +
                            "], actual message: [" + e.getMessage () + "]");
                    SdtXmlTestBase.fail ("Parser encountered unexpected error: expected [" + e.getMessage () +
                            "], actual [" + e.getMessage () + "]");
                }
                return;
            }
        });

    }

    /**
     * The actually reads the xml against the xsd.
     * 
     * @param parser the parser.
     * @param xmlPath The location of the xml.
     * @param expectedMessages the expected error message(s).
     * @throws IOException IOException.
     * @throws SAXException SAXException.
     */
    private void parseXml (final SAXParser parser, final String xmlPath, final String expectedMessages)
        throws IOException, SAXException
    {
        // Use the XSD file to validate the proof XML.
        parser.parse (xmlPath, new DefaultHandler ()
        {
            // Anonymous Default Handler to implement required error logic.
            public void error (final SAXParseException e) throws SAXException
            {
                // Record that we hit an error during parsing.
                SdtXmlTestBase.this.errorEncountered = true;

                if (expectedMessages != null && expectedMessages.contains (e.getMessage ()))
                {
                    LOG.debug ("Found expected error - " + e.getMessage ());
                }
                else
                {
                    LOG.error ("Parser encountered unexpected error - " + e.getMessage ());
                    SdtXmlTestBase.fail ("Parser encountered unexpected error: expected [" + e.getMessage () +
                            "], actual [" + e.getMessage () + "]");
                }
                return;
            }
        });

    }

    /**
     * Reads messages from given error file path and returns as a String.
     * 
     * @param errorFilePathname file containing error messages.
     * @return String representing error messages
     * @throws IOException in case of any errors related with error file.
     */
    private String getExpectedErrorMessages (final String errorFilePathname) throws IOException
    {
        final StringBuilder sb = new StringBuilder ();
        String errorFilePath = null;

        if (errorFilePathname != null)
        {
            errorFilePath = Utilities.checkFileExists (SdtXmlTestBase.XML_VALIDATION_DIR, errorFilePathname, false);
            LOG.debug ("Error message file - " + errorFilePath);
        }
        else
        {
            return null;
        }

        String sCurrentLine;

        final BufferedReader br = new BufferedReader (new FileReader (errorFilePath));

        while ((sCurrentLine = br.readLine ()) != null)
        {
            sb.append (sCurrentLine);
        }
        return sb.toString ();

    }

    /**
     * Use known XML files (with and without faults) to prove that XSD file
     * implements the correct validation.
     * 
     * @param xmlPathname
     *            path name of XML file to be used to check veracity of XSD.
     * @param xsdPathname
     *            path name of XSD file to be checked.
     * @param errorFilePathname
     *            path name of error message file (optional).
     */
    protected void validateXsd (final String xmlPathname, final String xsdPathname, final String errorFilePathname)
    {
        try
        {
            evaluateXsd (xmlPathname, xsdPathname, errorFilePathname);
        }
        catch (final IOException e)
        {
            LOG.error ("Exception while validating XML [" + xmlPathname + "] with XSD [" + xsdPathname + "]", e);
            SdtXmlTestBase.fail ("Exception while validating XML [" + xmlPathname + "] with XSD [" + xsdPathname + "]");
        }
        catch (final SAXException e)
        {
            LOG.error ("Exception while validating XML [" + xmlPathname + "] with XSD [" + xsdPathname + "]", e);
            SdtXmlTestBase.fail ("Exception while validating XML [" + xmlPathname + "] with XSD [" + xsdPathname + "]");
        }
        catch (final ParserConfigurationException e)
        {
            LOG.error ("Exception while validating XML [" + xmlPathname + "] with XSD [" + xsdPathname + "]", e);
            SdtXmlTestBase.fail ("Exception while validating XML [" + xmlPathname + "] with XSD [" + xsdPathname + "]");
        }

        // Has no error been reported and yet we expected one?
        if ( !this.errorEncountered && errorFilePathname != null)
        {
            LOG.error ("Parser failed to encountered expected error(s)");
            SdtXmlTestBase.fail ("Parser failed to encountered expected error(s)");
        }

    }
}
