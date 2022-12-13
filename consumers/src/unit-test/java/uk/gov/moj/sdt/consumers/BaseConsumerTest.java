package uk.gov.moj.sdt.consumers;

import javax.xml.soap.SOAPFault;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkCustomerApplication;
import uk.gov.moj.sdt.domain.ServiceRouting;
import uk.gov.moj.sdt.domain.ServiceType;
import uk.gov.moj.sdt.domain.SubmitQueryRequest;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkCustomerApplication;
import uk.gov.moj.sdt.domain.api.IServiceRouting;
import uk.gov.moj.sdt.domain.api.IServiceType;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.transformers.api.IConsumerTransformer;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.submitqueryrequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.submitqueryrequestschema.SubmitQueryRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.submitqueryresponseschema.SubmitQueryResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.targetappinternalendpoint.ITargetAppInternalEndpointPortType;

import java.util.HashSet;
import java.util.Set;

/**
 * Base Consumer Test class for the submit query consumer and consumer gateway .
 *
 * @author Mark Dathorne
 */
class BaseConsumerTest {

    /**
     * Consumer transformer for submit query.
     */
    @Mock
    protected IConsumerTransformer<SubmitQueryResponseType, SubmitQueryRequestType, ISubmitQueryRequest,
            ISubmitQueryRequest> mockTransformer;

    /**
     * Mock Client instance.
     */
    @Mock
    protected ITargetAppInternalEndpointPortType mockClient;

    @Mock
    protected SOAPFault soapFault;

    /**
     * Submit Query Consumer instance of the inner class under test.
     */
    protected SubQueryConsumer submitQueryConsumer;

    /**
     * Submit query request instance for testing in the methods.
     */
    protected ISubmitQueryRequest submitQueryRequest;

    /**
     * Submit query Request type for the query request.
     */
    protected SubmitQueryRequestType submitQueryRequestType;

    /**
     * Connection time out constant.
     */
    protected static final long CONNECTION_TIME_OUT = 30000;

    /**
     * Received time out constant.
     */
    protected static final long RECEIVE_TIME_OUT = 60000;

    /**
     * Method to do any pre-test set-up.
     */
    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        submitQueryConsumer = new SubQueryConsumer();
        submitQueryConsumer.setTransformer(mockTransformer);
        // submitQueryConsumer.setRethrowOnFailureToConnect (true);

        submitQueryRequest = this.createSubmitQueryRequest();
        submitQueryRequestType = this.createRequestType(submitQueryRequest);
    }


    /**
     * Need to extend the consumer class under test for overriding base class methods
     * of the getClient as it is abstract method.
     */
    protected class SubQueryConsumer extends SubmitQueryConsumer {
        /**
         * Get the client for the specified target application. If the client is not cached already, a new client
         * connection is created otherwise the already cached client is returned.
         *
         * @param targetApplicationCode the target application code
         * @param serviceType           the service type associated with the target application code
         * @param webServiceEndPoint    the Web Service End Point URL
         * @param connectionTimeOut     the connection time out value
         * @param receiveTimeOut        the acknowledgement time out value
         * @return the target application end point port bean i.e. the client interface.
         */
        @Override
        public ITargetAppInternalEndpointPortType getClient(final String targetApplicationCode,
                                                            final String serviceType, final String webServiceEndPoint,
                                                            final long connectionTimeOut, final long receiveTimeOut) {
            return mockClient;
        }
    }

    /**
     * @param domainObject the submit query domain object.
     * @return the Jaxb submit query request type.
     */
    protected SubmitQueryRequestType createRequestType(final ISubmitQueryRequest domainObject) {
        final SubmitQueryRequestType requestType = new SubmitQueryRequestType();
        final HeaderType headerType = new HeaderType();
        headerType.setTargetAppCustomerId("TestCust");
        headerType.setCriteriaType("TEST_CRITERIA");
        requestType.setHeader(headerType);

        return requestType;
    }

    /**
     * @return submit query request domain object
     */
    protected ISubmitQueryRequest createSubmitQueryRequest() {
        // final IBulkSubmission bulkSubmission = new BulkSubmission ();
        final IBulkCustomer bulkCustomer = new BulkCustomer();
        bulkCustomer.setId(1L);
        bulkCustomer.setSdtCustomerId(10L);

        final ITargetApplication targetApp = new TargetApplication();

        targetApp.setId(1L);
        targetApp.setTargetApplicationCode("MCOL");
        targetApp.setTargetApplicationName("TEST_TargetApp");
        final Set<IServiceRouting> serviceRoutings = new HashSet<>();

        final ServiceRouting serviceRouting = new ServiceRouting();
        serviceRouting.setId(1L);
        serviceRouting.setWebServiceEndpoint("MCOL_END_POINT");

        final IServiceType serviceType = new ServiceType();
        serviceType.setId(1L);
        serviceType.setName(IServiceType.ServiceTypeName.SUBMIT_QUERY.name());
        serviceType.setDescription("RequestTestDesc1");
        serviceType.setStatus("RequestTestStatus");

        serviceRouting.setServiceType(serviceType);
        serviceRoutings.add(serviceRouting);
        targetApp.setServiceRoutings(serviceRoutings);

        final Set<IBulkCustomerApplication> bulkCustomerApplications = new HashSet<>();
        final BulkCustomerApplication bulkCustomerApplication = new BulkCustomerApplication();
        bulkCustomerApplication.setBulkCustomer(bulkCustomer);
        bulkCustomerApplication.setTargetApplication(targetApp);
        bulkCustomerApplications.add(bulkCustomerApplication);
        bulkCustomer.setBulkCustomerApplications(bulkCustomerApplications);

        final ISubmitQueryRequest ssubmitQueryRequest = new SubmitQueryRequest();
        ssubmitQueryRequest.setBulkCustomer(bulkCustomer);
        ssubmitQueryRequest.setTargetApplication(targetApp);
        ssubmitQueryRequest.setResultCount(1);
        return ssubmitQueryRequest;

    }

}
