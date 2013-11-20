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
 * following acknowledgement: "This product includes Money Claims OnLine."
 * - Products derived from this software may not be called "Money Claims OnLine" nor may
 * "Money Claims OnLine" appear in their names without prior written permission of the
 * Ministry of Justice.
 * - Redistributions of any form whatsoever must retain the following acknowledgement: "This
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
 * $Id: ClaimXsdTest.java 16414 2013-05-29 10:56:45Z agarwals $
 * $LastChangedRevision: 16414 $
 * $LastChangedDate: 2013-05-29 11:56:45 +0100 (Wed, 29 May 2013) $
 * $LastChangedBy: holmessm $ */
package uk.gov.moj.sdt.producers.comx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.test.util.DBUnitUtility;
import uk.gov.moj.sdt.utils.SpringApplicationContext;
import uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType;

/**
 * Test class for end to end web service tests..
 * 
 * @param <JaxbRequestType>
 *            the type of the JAXB top level object to create.
 * @param <EndpointPortType>
 *            the type of the endpoint to be called.
 * @author Robin Compston
 */
public abstract class AbstractWebServiceTest<JaxbRequestType, JaxbResponseType>
{
    /**
     * Static logging object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (AbstractWebServiceTest.class);

    /**
     * Setup the test.
     */
    @Before
    public void setUp ()
    {
        DBUnitUtility.loadDatabase (this.getClass (), true);
    }

    /**
     * Method to call remote endpoint to be tested.
     */
    protected void callWebService (Class<JaxbRequestType> requestClass)
    {
        // Get the argument to send to the web service.
        JaxbRequestType request = this.getJaxbFromXml (requestClass);

        // Call the remote service.
        JaxbResponseType response = this.callTestWebService (request);

        // Check the response returned by the web service.
        this.checkXmlFromJaxb (response);

        return;
    }

    /**
     * Turn the XML file for this test into a JAXB object tree. The expected XML
     * lives in the same package as the test with the name: <class name>.<method
     * name>.request.xml.
     * 
     * @return the JAXB object loaded with XML associated with this test class.
     */
    @SuppressWarnings ("unchecked")
    private JaxbRequestType getJaxbFromXml (Class<JaxbRequestType> requestClass)
    {
        JaxbRequestType request = null;

        try
        {
            // Find out method that called us.
            StackTraceElement[] stackTraceElements = Thread.currentThread ().getStackTrace ();

            // Assume that test method is three stacks deep.
            String methodName = stackTraceElements[3].getMethodName ();

            // Open a stream to the resource holding the XML for this class
            // method which is to be converted to JAXB.
            String resourceName = this.getClass ().getCanonicalName ();
            // Adjust format for a resource name.
            resourceName = resourceName.replace ('.', '/');
            // Add the method name and suffix.
            resourceName = resourceName + "." + methodName + ".request.xml";
            InputStream inputStream = this.getClass ().getClassLoader ().getResourceAsStream (resourceName);

            // Create JAXB object of required type from the XML input stream.
            JAXBContext jaxbContext = JAXBContext.newInstance (requestClass);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller ();
            @SuppressWarnings ("rawtypes") JAXBElement jaxbElement =
                    jaxbUnmarshaller.unmarshal (new StreamSource (inputStream), requestClass);
            request = (JaxbRequestType) jaxbElement.getValue ();
        }
        catch (Exception e)
        {
            e.printStackTrace ();
            Assert.fail ("Failure to unmarshall request from web service [" + requestClass.toString () + "]");
        }

        return request;
    }

    /**
     * Turn the JAXB object tree into an XML string and check that it is what we
     * expected for this test. The expected XML lives in the same package as the
     * test with the name: <class name>.<method name>.response.xml.
     * 
     * @param response
     *            the JAXB object returned by the web service.
     * @return the XML corresponding to the given JAXB object tree.
     */
    private void checkXmlFromJaxb (JaxbResponseType response)
    {
        try
        {
            // Create JAXB object of required type from the XML input stream.
            JAXBContext jaxbContext = JAXBContext.newInstance (response.getClass ());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller ();

            // Output pretty printed.
            jaxbMarshaller.setProperty (Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Output stream to write result to.
            OutputStream out = new OutputStream ()
            {
                private StringBuilder string = new StringBuilder ();

                @Override
                public void write (int b) throws IOException
                {
                    this.string.append ((char) b);
                }

                @Override
                public String toString ()
                {
                    return this.string.toString ();
                }
            };

            JAXBElement<JaxbResponseType> jaxbResponse = this.wrapJaxbObject (response);

            // Convert the JAXB object tree into XML.
            jaxbMarshaller.marshal (jaxbResponse, out);

            // Get the XML out of the output stream.
            String actualXml = out.toString ();

            // Find out method that called us.
            StackTraceElement[] stackTraceElements = Thread.currentThread ().getStackTrace ();

            // Assume that test method is three stacks deep.
            String methodName = stackTraceElements[3].getMethodName ();

            // Open a stream to the resource holding the XML for this class
            // method which is to be converted to JAXB.
            String resourceName = this.getClass ().getCanonicalName ();
            // Adjust format for a resource name.
            resourceName = resourceName.replace ('.', '/');
            // Add the method name and suffix.
            resourceName = resourceName + "." + methodName + ".response.xml";
            InputStream inputStream = this.getClass ().getClassLoader ().getResourceAsStream (resourceName);
            String expectedXml = IOUtils.toString (inputStream, "UTF-8");

            // Blank out the sdt bulk reference and submitted date since these
            // will not match otherwise.
            expectedXml = removeVariantText (expectedXml, "sdtBulkReference");
            expectedXml = removeVariantText (expectedXml, "submittedDate");
            expectedXml = removeLineFeeds (expectedXml);
            actualXml = removeVariantText (actualXml, "sdtBulkReference");
            actualXml = removeVariantText (actualXml, "submittedDate");
            actualXml = removeLineFeeds (actualXml);

            // Check xml.
            Assert.assertEquals ("Expected XML [" + resourceName + "] does not match, ", expectedXml, actualXml);
        }
        catch (Exception e)
        {
            e.printStackTrace ();
            Assert.fail ("Failure to marshall response from web service [" + response.toString () + "]");
        }

        return;
    }

    /**
     * Utility to remove carriage return (\r), linefeeds (\n) and tabs (\t) otherwise the
     * test for equality does not work.
     * 
     * @param xml
     *            the XML to remove text from.
     * @return the modified XML.
     */
    private String removeLineFeeds (final String xml)
    {
        // Get characters from String.
        char[] inChars = xml.toCharArray ();

        // Make array big enough for all given String.
        char[] outChars = new char[inChars.length];

        int i1 = 0;
        int i2 = 0;
        boolean skipSpace = false;

        // Exclude line feeds and carriage returns.
        // Allow no consecutive spaces (ie only one space).
        while (i1 < inChars.length)
        {
            if (i1 != 0 && inChars[i1 - 1] == ' ' && inChars[i1] == ' ')
            {
                skipSpace = true;
            }
            if (inChars[i1] != '\n' && inChars[i1] != '\r' && inChars[i1] != '\t' && !skipSpace)
            {
                outChars[i2++] = inChars[i1];
            }
            if (skipSpace)
                skipSpace = false;
            i1++;
        }

        // Convert back to String.
        return new String (outChars, 0, i2);
    }

    /**
     * Utility to remove the variant text to allow non variant text to be
     * matched.
     * 
     * @param xml
     *            the XML to remove text from.
     * @param tag
     *            the tag whose content is to be removed.
     * @return the modified XML.
     */
    private String removeVariantText (final String xml, final String tag)
    {
        final Pattern pattern = Pattern.compile ("(<[\\w]+:" + tag + ">).*(</[\\w]+:" + tag + ">)");

        // Match it against the result of all previous match replacements.
        final Matcher matcher = pattern.matcher (xml);

        String newXml = xml;

        if (matcher.find ())
        {
            // Inject the system specific response into the current envelope
            newXml = matcher.replaceFirst (matcher.group (1) + matcher.group (2));
        }

        return newXml;
    }

    /**
     * Call the required web service for this test.
     * 
     * @param request
     *            A request JAXB object tree.
     * @return a response JAXB object.
     */
    protected abstract JaxbResponseType callTestWebService (final JaxbRequestType request);

    /**
     * Wrap the JAXB object in a JAXB context object so that when it is marshalled it has an @XmlRootElement and does
     * not throw an exception.
     * 
     * @param response
     *            A request response JAXB object tree.
     * @return a wrapped response JAXB object.
     */
    protected abstract JAXBElement<JaxbResponseType> wrapJaxbObject (final JaxbResponseType response);

    /**
     * Return a client to call SDT's external endpoint. The client is customised with endpoint url and timeout values.
     * 
     * @return client for SDT's external endpoint.
     */
    protected ISdtEndpointPortType getSdtEndpointClient ()
    {

        // Get the SOAP proxy client.
        ISdtEndpointPortType client =
                (ISdtEndpointPortType) SpringApplicationContext
                        .getBean ("uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType");

        Client clientProxy = ClientProxy.getClient (client);

        // Set endpoint address
        // BindingProvider provider = (BindingProvider) client;
        // provider.getRequestContext ().put (BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
        // "http://localhost.:8888/producers-comx/service/sdtapi");

        HTTPConduit httpConduit = (HTTPConduit) clientProxy.getConduit ();
        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy ();
        // Specifies the amount of time, in milliseconds, that the client will attempt to establish a connection before
        // it times out
        httpClientPolicy.setConnectionTimeout (10000);
        // Specifies the amount of time, in milliseconds, that the client will wait for a response before it times out.
        httpClientPolicy.setReceiveTimeout (10000);
        httpConduit.setClient (httpClientPolicy);

        return client;

    }

}
