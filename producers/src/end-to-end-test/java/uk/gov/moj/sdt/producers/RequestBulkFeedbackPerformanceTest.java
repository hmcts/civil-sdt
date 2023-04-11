package uk.gov.moj.sdt.producers;

import javax.xml.bind.JAXBElement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.moj.sdt.test.utils.DBUnitUtilityBean;
import uk.gov.moj.sdt.test.utils.TestConfig;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackrequestschema.BulkFeedbackRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema.BulkFeedbackResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.bulkfeedbackresponseschema.ObjectFactory;
import uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.ISdtEndpointPortType;
import uk.gov.moj.sdt.ws._2013.sdt.sdtendpoint.SdtEndpoint;
import uk.gov.moj.sdt.ws._2013.sdt.sdtinternalendpoint.SdtInternalEndpoint;

/**
 * End to end performance test for bulk feedback web service.
 *
 * @author Saurabh Agarwal
 */

@ActiveProfiles("integ")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { EndToEndTestConfig.class, DaoEndToEndTestConfig.class})
//@ContextConfiguration(locations = {"classpath*:/uk/gov/moj/sdt/producers/spring*e2e.test.xml",
//        "classpath*:/uk/gov/moj/sdt/utils/**/spring*.xml", "classpath*:/uk/gov/moj/sdt/transformers/**/spring*.xml"})
public class RequestBulkFeedbackPerformanceTest extends
        AbstractWebServiceTest<BulkFeedbackRequestType, BulkFeedbackResponseType> {
    /**
     * Method to call remote request bulk feedback endpoint to be tested.
     */
    @Test
    public void testValidRequestBulkFeedback() {
        this.callWebService(BulkFeedbackRequestType.class);
    }

    @Override
    protected BulkFeedbackResponseType callTestWebService(final BulkFeedbackRequestType request) {
        // Get the SOAP proxy client.
        ISdtEndpointPortType client = getSdtEndpointClient();

        // Call the specific business method for this text - note that a single test can only use one web service
        // business method.
        return client.getBulkFeedback(request);
    }

    @Override
    protected JAXBElement<BulkFeedbackResponseType> wrapJaxbObject(final BulkFeedbackResponseType response) {
        // Use the provided factor to create a wrapped instance of the response.
        ObjectFactory objectFactory = new ObjectFactory();
        return objectFactory.createBulkFeedbackResponse(response);
    }

}
