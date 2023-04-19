package uk.gov.moj.sdt.producers;

import java.io.InputStream;
import java.time.LocalDateTime;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StopWatch;

import uk.gov.moj.sdt.producers.config.EndToEndTestConfig;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.BulkRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.RequestItemType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkrequestschema.RequestsType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkresponseschema.BulkResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkresponseschema.ObjectFactory;
import uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Performance tests for bulk submission requests.
 *
 * @author Saurabh Agarwal
 */
@ActiveProfiles("end-to-end-test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { EndToEndTestConfig.class})
@Sql(scripts = {"classpath:database/baseline/V0001__init.sql",
        "classpath:database/baseline/create_purge_proc.sql",
        "classpath:database/baseline/create_finish_dbunit_load_proc.sql",
        "classpath:database/baseline/create_prepare_for_dbunit_load_proc.sql"})
public class SubmitBulkForSystemTest extends AbstractWebServiceTest<BulkRequestType, BulkResponseType> {

    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SubmitBulkForSystemTest.class);

    /**
     * Constant to define number of worker threads to process concurrent requests.
     */
    private static final int MAX_WORKER_THREADS = 1;

    /**
     * Constant to define number of individual requests per bulk.
     */
    private static final int IND_REQ_PER_BULK = 2000;

    /**
     * Maximum number of requests to execute on the threads.
     */
    private static final int MAX_REQUESTS = 1;

    /**
     * Current number of worker threads.
     */
    public static int threadCount = 0;

    /**
     * The number of requests that have completed i.e. response received
     * from the web service.
     */
    private static int REQUESTS_COMPLETED = 0;

    /**
     * Tests performance for concurrent requests.
     */
    @Test
    public void testConcurrentRequests() {
        LOGGER.info("Test performance for maximum " + MAX_WORKER_THREADS +
                " concurrent request(s). Each bulk request contains " + IND_REQ_PER_BULK +
                " individual request(s). Total requests = " + MAX_REQUESTS);

        BulkRequestType templateRequestType = this.getJaxbFromXml(BulkRequestType.class);

        // Measure total time using a stop watch.
        final StopWatch stopWatch = new StopWatch("BulkRequestClock");

        stopWatch.start();
        for (int i = 0; i < MAX_REQUESTS; i++) {
            // Following gives variation per request but for what th system test team, only one request is needed.
            BulkRequestType requestType = createBulkRequest(templateRequestType, "requestIdmax40PA" + (2403 + i));

            scheduleRequestToProcess(requestType);
        }

        // Wait for all the threads to finish.
        while (REQUESTS_COMPLETED < MAX_REQUESTS) {
            // Do Nothing.
        }
        stopWatch.stop();

        LOGGER.info("Total execution time(ms) - {}", stopWatch.getTotalTimeMillis());
    }

    /**
     * @param requestType the individual request type.
     */
    private void scheduleRequestToProcess(final BulkRequestType requestType) {
        Thread thread = new Thread(new BulkRequestProcessor(Thread.currentThread(), requestType));
        thread.start();

        if (SubmitBulkForSystemTest.threadCount >= MAX_WORKER_THREADS) {
            try {
                // Sleep indefinitely as thread should be interrupted to resume processing.
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                // Do nothing.
                LOGGER.debug("interrupted {}", e);
            }
        }
    }

    /**
     * Creates new bulk request using provided template request.
     *
     * @param templateBulkRequest template bulk request.
     * @param customerRef         customer reference to be populated in new bulk request.
     * @return BulkRequestType new instance.
     */
    private BulkRequestType createBulkRequest(final BulkRequestType templateBulkRequest, final String customerRef) {
        final BulkRequestType requestType = new BulkRequestType();

        HeaderType headerType = new HeaderType();
        headerType.setCustomerReference(customerRef);
        headerType.setRequestCount(IND_REQ_PER_BULK);
        headerType.setSdtCustomerId(templateBulkRequest.getHeader().getSdtCustomerId());
        headerType.setTargetApplicationId(templateBulkRequest.getHeader().getTargetApplicationId());
        requestType.setHeader(headerType);

        RequestsType requestsType = new RequestsType();
        requestType.setRequests(requestsType);

        RequestItemType template = templateBulkRequest.getRequests().getRequest().get(0);

        // Create new individual requests using template.
        for (int i = 1; i <= IND_REQ_PER_BULK; i++) {
            RequestItemType item = cloneRequestItem(template);
            item.setRequestId(customerRef + (7000 + i));
            requestsType.getRequest().add(item);
        }

        return requestType;
    }

    /**
     * Clones given request item.
     *
     * @param source request item to clone.
     * @return RequestItemType cloned instance.
     */
    private RequestItemType cloneRequestItem(final RequestItemType source) {
        RequestItemType dest = new RequestItemType();
        dest.setRequestId(source.getRequestId());
        dest.setRequestType(source.getRequestType());
        dest.setAny(source.getAny());
        return dest;
    }

    /**
     * Turn the XML file for this test into a JAXB object tree. The expected XML
     * lives in the same package as the test with the name: <class name>.<method
     * name>.request.xml.
     *
     * @return the JAXB object loaded with XML associated with this test class.
     */
    @Override
    protected BulkRequestType getJaxbFromXml(Class<BulkRequestType> requestClass) {
        BulkRequestType request = null;

        try {
            // Find out method that called us.
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

            // Test method is two stacks deep.
            String methodName = stackTraceElements[2].getMethodName();

            // Open a stream to the resource holding the XML for this class
            // method which is to be converted to JAXB.
            String resourceName = this.getClass().getCanonicalName();
            // Adjust format for a resource name.
            resourceName = resourceName.replace('.', '/');
            // Add the method name and suffix.
            resourceName = resourceName + "." + methodName + ".request.xml";
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourceName);

            // Create JAXB object of required type from the XML input stream.
            JAXBContext jaxbContext = JAXBContext.newInstance(requestClass);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            @SuppressWarnings("rawtypes") JAXBElement jaxbElement =
                    jaxbUnmarshaller.unmarshal(new StreamSource(inputStream), requestClass);
            request = (BulkRequestType) jaxbElement.getValue();
        } catch (Exception e) {
            LOGGER.error("Failure to unmarshall request from web service [" + requestClass.toString() + "]", e);
            fail("Failure to unmarshall request from web service [" + requestClass.toString() + "]");
        }

        return request;
    }

    @Override
    protected BulkResponseType callTestWebService(final BulkRequestType request) {
        // Get the SOAP proxy client.
        ISdtEndpointPortType client = getSdtEndpointClient(5000, 3000000);

        // Call the specific business method for this text - note that a single test can only use one web service
        // business method.
        return client.submitBulk(request);
    }

    @Override
    protected JAXBElement<BulkResponseType> wrapJaxbObject(final BulkResponseType response) {
        // Use the provided factor to create a wrapped instance of the response.
        ObjectFactory objectFactory = new ObjectFactory();
        return objectFactory.createBulkResponse(response);
    }

    /**
     * Increment the number of current worker threads.
     */
    public static synchronized void incrementThreadCount() {
        SubmitBulkForSystemTest.threadCount++;
    }

    /**
     * Decrement the number of current worker threads.
     */
    public static synchronized void decrementThreadCount() {
        SubmitBulkForSystemTest.threadCount--;
    }

    public static synchronized void incrementCompletedRequests() {
        SubmitBulkForSystemTest.REQUESTS_COMPLETED++;
    }

    class BulkRequestProcessor implements Runnable {

        /**
         * Request Type jaxb object.
         */
        private BulkRequestType requestType = null;

        /**
         * Thread which started this thread.
         */
        private Thread starter = null;

        /**
         * Constructor.`
         *
         * @param requestType
         */
        BulkRequestProcessor(final Thread starter, final BulkRequestType requestType) {
            this.starter = starter;
            this.requestType = requestType;
            SubmitBulkForSystemTest.incrementThreadCount();

        }

        @Override
        public void run() {
            try {
                LOGGER.info("Start test for thread [{}] for request [{}] at [{}]",
                        Thread.currentThread().getName(), requestType.getHeader().getCustomerReference(),
                        LocalDateTime.now());
                callTestWebService(requestType);
                LOGGER.info("End test for thread [{}] for request [{}] at [{}]",
                        Thread.currentThread().getName(), requestType.getHeader().getCustomerReference(),
                        LocalDateTime.now());
            } catch (Exception e) {
                LOGGER.error("Error executing request: " + requestType.getHeader().getCustomerReference(), e);
            } finally {
                SubmitBulkForSystemTest.decrementThreadCount();
                SubmitBulkForSystemTest.incrementCompletedRequests();
                starter.interrupt();
            }

        }

    }
}
