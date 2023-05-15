package uk.gov.moj.sdt.producers;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.SpringApplicationContext;
import uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test class for end to end web service tests..
 *
 * @param <JaxbRequestType>  the type of the JAXB top level object to create.
 * @param <JaxbResponseType> the type of the JAXB response.
 * @author Robin Compston
 */
public abstract class AbstractWebServiceTest<JaxbRequestType, JaxbResponseType> extends AbstractSdtUnitTestBase {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWebServiceTest.class);

    /**
     * Setup the test.
     */
    @Override
    @BeforeEach
    public void setUp() {}

    /**
     * Method to call remote endpoint to be tested.
     */
    protected void callWebService(Class<JaxbRequestType> requestClass) {
        // Get the argument to send to the web service.
        JaxbRequestType request = this.getJaxbFromXml(requestClass);

        // Call the remote service.
        JaxbResponseType response;
        try {
            response = this.callTestWebService(request);
        } catch (RuntimeException e) {
            throw new SdtRunTimeException("RUNTIME_ERROR", e.getMessage());
        }

        // Check the response returned by the web service.
        this.checkXmlFromJaxb(response);
    }

    /**
     * Turn the XML file for this test into a JAXB object tree. The expected XML
     * lives in the same package as the test with the name: <class name>.<method
     * name>.request.xml.
     *
     * @return the JAXB object loaded with XML associated with this test class.
     */
    @SuppressWarnings("unchecked")
    protected JaxbRequestType getJaxbFromXml(Class<JaxbRequestType> requestClass) {
        JaxbRequestType request = null;

        try {
            // Find out method that called us.
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

            // Assume that test method is three stacks deep.
            String methodName = stackTraceElements[3].getMethodName();

            // Open a stream to the resource holding the XML for this class
            // method which is to be converted to JAXB.
            String resourceName = this.getClass().getCanonicalName();
            // Adjust format for a resource name.
            resourceName = resourceName.replace('.', '/');
            // Add the method name and suffix.
            resourceName = resourceName + "." + methodName + ".request.xml";
            LOGGER.debug("XML resourceName = {}", resourceName);
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourceName);

            // Create JAXB object of required type from the XML input stream.
            JAXBContext jaxbContext = JAXBContext.newInstance(requestClass);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            @SuppressWarnings("rawtypes") JAXBElement jaxbElement =
                    jaxbUnmarshaller.unmarshal(new StreamSource(inputStream), requestClass);
            request = (JaxbRequestType) jaxbElement.getValue();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failure to unmarshall request from web service [" + requestClass.toString() + "]");
        }

        return request;
    }

    /**
     * Turn the JAXB object tree into an XML string and check that it is what we
     * expected for this test. The expected XML lives in the same package as the
     * test with the name: <class name>.<method name>.response.xml.
     *
     * @param response the JAXB object returned by the web service.
     */
    protected void checkXmlFromJaxb(JaxbResponseType response) {
        try {
            // Create JAXB object of required type from the XML input stream.
            JAXBContext jaxbContext = JAXBContext.newInstance(response.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // Output pretty printed.
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Output stream to write result to.
            String actualXml;
            try (OutputStream out = new OutputStream() {
                private StringBuilder string = new StringBuilder();

                @Override
                public void write(int b) throws IOException {
                    this.string.append((char) b);
                }

                @Override
                public String toString() {
                    return this.string.toString();
                }
            }) {

                JAXBElement<JaxbResponseType> jaxbResponse = this.wrapJaxbObject(response);

                // Convert the JAXB object tree into XML.
                jaxbMarshaller.marshal(jaxbResponse, out);

                // Get the XML out of the output stream.
                actualXml = out.toString();
            }

            // Find out method that called us.
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

            // Assume that test method is three stacks deep.
            String methodName = stackTraceElements[3].getMethodName();

            // Open a stream to the resource holding the XML for this class
            // method which is to be converted to JAXB.
            String resourceName = this.getClass().getCanonicalName();
            // Adjust format for a resource name.
            resourceName = resourceName.replace('.', '/');
            // Add the method name and suffix.
            resourceName = resourceName + "." + methodName + ".response.xml";
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourceName);
            String expectedXml = "";
            if (null != inputStream) {
                expectedXml = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            }

            // Blank out the sdt bulk reference and submitted date since these
            // will not match otherwise.
            expectedXml = removeVariantText(expectedXml, "sdtBulkReference");
            expectedXml = removeVariantText(expectedXml, "submittedDate");
            // Following only needed for concurrent duplicate test since customer reference keeps changing.
            expectedXml = removeVariantText(expectedXml, "customerReference");
            expectedXml = removeLineFeeds(expectedXml);
            expectedXml = removeBetweenNodeSpaces(expectedXml);
            actualXml = removeVariantText(actualXml, "sdtBulkReference");
            actualXml = removeVariantText(actualXml, "submittedDate");
            actualXml = removeVariantText(actualXml, "customerReference");
            actualXml = removeLineFeeds(actualXml);
            actualXml = removeBetweenNodeSpaces(actualXml);

            checkXmlChunks(expectedXml, actualXml);

            if (!actualXml.equals(expectedXml)) {
                LOGGER.error("expected [{}], actual [{}]", expectedXml, actualXml);
            }

            // Check xml.
            assertEquals(expectedXml, actualXml, "Expected XML [" + resourceName + "] does not match, ");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failure to marshall response from web service [" + response.toString() + "]");
        }
    }

    /**
     * Utility to show xml chunk differences. Helps with debugging large xml strings
     *
     * @param expectedXml the expected XML.
     * @param actualXml the actual XML.
     */    private void checkXmlChunks(String expectedXml, String actualXml) {
        if (actualXml.length() == 0 || expectedXml.length() == 0) {
            return;
        }

        String actualXmlStrip;
        String expectedXmlStrip;
        final int incrementX = 200;
        int startX = 0;
        int endX = 0;
        while (startX < actualXml.length()) {
            endX = startX + incrementX;
            if (endX >= actualXml.length()) {
                endX = actualXml.length();
            }

            actualXmlStrip =  getXmlStrip(actualXml, startX, endX);
            expectedXmlStrip =  getXmlStrip(expectedXml, startX, endX);
            checkXmlStrip(actualXmlStrip, expectedXmlStrip, startX, endX);

            // increment starting position
            startX +=incrementX;
        }

    }

    private String getXmlStrip(String xml, int startX, int endX) {
        if (startX < xml.length()-1) {
            if (endX <= xml.length()) {
                return xml.substring(startX, endX);
            } else {
                return xml.substring(startX, xml.length());
            }
        }
        return "";
    }

    private void checkXmlStrip(String actualXmlStrip, String expectedXmlStrip, int startX, int endX) {

        if (!actualXmlStrip.equals(expectedXmlStrip)) {
            LOGGER.info("startX:{}; endX:{}", startX, endX);
            LOGGER.info(" actualXmlStrip  : {}",  actualXmlStrip);
            LOGGER.info(" expectedXmlStrip: {}",  expectedXmlStrip);
            LOGGER.error("expected [{}], actual [{}]", expectedXmlStrip, actualXmlStrip);
        }
        assertEquals(expectedXmlStrip, actualXmlStrip);
    }

    /**
     * Utility to remove spacing between nodes.
     *
     * @param xml the XML to remove text from.
     * @return the modified XML.
     */
    private String removeBetweenNodeSpaces(final String xml) {
        return xml.replace("> <", "><");

    }


    /**
     * Utility to remove carriage return (\r), linefeeds (\n) and tabs (\t) otherwise the
     * test for equality does not work.
     *
     * @param xml the XML to remove text from.
     * @return the modified XML.
     */
    private String removeLineFeeds(final String xml) {
        // Get characters from String.
        char[] inChars = xml.toCharArray();

        // Make array big enough for all given String.
        char[] outChars = new char[inChars.length];

        int i1 = 0;
        int i2 = 0;
        boolean skipSpace = false;

        // Exclude line feeds and carriage returns.
        // Allow no consecutive spaces (ie only one space).
        while (i1 < inChars.length) {
            if (i1 != 0 && inChars[i1 - 1] == ' ' && inChars[i1] == ' ') {
                skipSpace = true;
            }
            if (inChars[i1] != '\n' && inChars[i1] != '\r' && inChars[i1] != '\t' && !skipSpace) {
                outChars[i2++] = inChars[i1];
            }
            if (skipSpace)
                skipSpace = false;
            i1++;
        }

        // Convert back to String.
        return new String(outChars, 0, i2);
    }

    /**
     * Utility to remove the variant text to allow non variant text to be
     * matched.
     *
     * @param xml the XML to remove text from.
     * @param tag the tag whose content is to be removed.
     * @return the modified XML.
     */
    private String removeVariantText(final String xml, final String tag) {
        // Remove tag and tag contents with optional namespace prefix.
        final Pattern pattern = Pattern.compile("(<[\\S:&&[^!>/]]*?" + tag + ">).*(</[\\S:]*?" + tag + ">)");

        // Match it against the result of all previous match replacements.
        final Matcher matcher = pattern.matcher(xml);

        String newXml = xml;

        if (matcher.find()) {
            // Inject the system specific response into the current envelope
            newXml = matcher.replaceFirst(matcher.group(1) + matcher.group(2));
        }

        return newXml;
    }

    /**
     * Call the required web service for this test.
     *
     * @param request A request JAXB object tree.
     * @return a response JAXB object.
     */
    protected abstract JaxbResponseType callTestWebService(final JaxbRequestType request);

    /**
     * Wrap the JAXB object in a JAXB context object so that when it is marshalled it has an @XmlRootElement and does
     * not throw an exception.
     *
     * @param response A request response JAXB object tree.
     * @return a wrapped response JAXB object.
     */
    protected abstract JAXBElement<JaxbResponseType> wrapJaxbObject(final JaxbResponseType response);

    /**
     * Return a client to call SDT's external endpoint. The client is customised with timeout values.
     *
     * @return client for SDT's external endpoint.
     */
    protected ISdtEndpointPortType getSdtEndpointClient() {
        return getSdtEndpointClient(5000, 100000);
    }

    /**
     * Return a client to call SDT's external endpoint. The client is customised with timeout values.
     *
     * @param connTimeout     connection timeout.
     * @param responseTimeout response timeout.
     * @return client for SDT's external endpoint.
     */
    protected ISdtEndpointPortType getSdtEndpointClient(final long connTimeout, final long responseTimeout) {

        // Get the SOAP proxy client.
        ISdtEndpointPortType client = (ISdtEndpointPortType) SpringApplicationContext.getBean("ISdtEndpointPortType");

        Client clientProxy = ClientProxy.getClient(client);

        HTTPConduit httpConduit = (HTTPConduit) clientProxy.getConduit();

        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        // Specifies the amount of time, in milliseconds, that the client will attempt to establish a connection before
        // it times out
        httpClientPolicy.setConnectionTimeout(connTimeout);
        // Specifies the amount of time, in milliseconds, that the client will wait for a response before it times out.
        httpClientPolicy.setReceiveTimeout(responseTimeout);
        httpConduit.setClient(httpClientPolicy);

        return client;
    }
}
