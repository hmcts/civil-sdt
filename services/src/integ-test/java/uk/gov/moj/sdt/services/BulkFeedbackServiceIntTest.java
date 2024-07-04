package uk.gov.moj.sdt.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.BulkFeedbackRequest;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.IBulkFeedbackRequest;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.services.api.IBulkFeedbackService;
import uk.gov.moj.sdt.services.config.ServicesTestConfig;
import uk.gov.moj.sdt.test.utils.AbstractIntegrationTest;
import uk.gov.moj.sdt.test.utils.TestConfig;
import uk.gov.moj.sdt.utils.SdtContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ActiveProfiles("integ")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestConfig.class, ServicesTestConfig.class})
@Sql(scripts = {"classpath:uk/gov/moj/sdt/services/sql/RefData.sql",
    "classpath:uk/gov/moj/sdt/services/sql/BulkFeedbackServiceIntTest.sql"})
@Transactional
class BulkFeedbackServiceIntTest extends AbstractIntegrationTest {

    private final IBulkFeedbackService bulkFeedbackService;

    @Autowired
    public BulkFeedbackServiceIntTest(IBulkFeedbackService bulkFeedbackService) {
        this.bulkFeedbackService = bulkFeedbackService;
    }

    @ParameterizedTest
    @MethodSource("dataForGetBulkFeedback")
    void testGetBulkFeedbackTargetAppResponses(String sdtBulkReference,
                                               int numIndividualRequests,
                                               int numTargetAppResponses) {
        IBulkFeedbackRequest bulkFeedbackRequest = createBulkFeedbackRequest(sdtBulkReference);

        IBulkSubmission bulkSubmission = bulkFeedbackService.getBulkFeedback(bulkFeedbackRequest);

        assertNotNull(bulkSubmission, "Bulk submission should not be null");
        assertEquals(sdtBulkReference, bulkSubmission.getSdtBulkReference(), "Unexpected bulk submission returned");

        List<IIndividualRequest> individualRequests = bulkSubmission.getIndividualRequests();
        assertNotNull(individualRequests, "Bulk request should have individual requests");
        assertEquals(numIndividualRequests, individualRequests.size(), "Unexpected number of individual requests");

        Map<String, String> targetAppRespMap = SdtContext.getContext().getTargetApplicationRespMap();
        assertNotNull(targetAppRespMap, "Target app response map should not be null");
        assertEquals(numTargetAppResponses,
                     targetAppRespMap.size(),
                     "Unexpected number of entries in target app response map");

        int expectedLineNumber = 1;
        String expectedTargetAppResponse;
        String targetAppResponse;
        int foundTargetAppResponses = 0;

        for (IIndividualRequest individualRequest : individualRequests) {
            assertEquals(expectedLineNumber,
                         individualRequest.getLineNumber(),
                         "Individual requests in unexpected order.  Expected line number " + expectedLineNumber);

            expectedTargetAppResponse =
                null == individualRequest.getTargetApplicationResponse() ? "" :
                    new String(individualRequest.getTargetApplicationResponse(), StandardCharsets.UTF_8);

            targetAppResponse = targetAppRespMap.get(individualRequest.getCustomerRequestReference());

            if (expectedTargetAppResponse.isEmpty()) {
                // Individual request doesn't have a target app response, so confirm that there was no
                // corresponding entry for it in the target app response map
                assertNull(targetAppResponse,
                           "No target app response expected for customer ref " +
                               individualRequest.getCustomerRequestReference());
            } else {
                // Individual request has a target app response, so confirm that a corresponding entry for
                // it exists in the target app response map
                assertEquals(expectedTargetAppResponse,
                             targetAppResponse,
                             "Unexpected target app response found for customer ref " +
                                 individualRequest.getCustomerRequestReference());
                foundTargetAppResponses++;
            }

            expectedLineNumber++;
        }

        assertEquals(numTargetAppResponses,
                     foundTargetAppResponses,
                     "Number of expected target app responses did not match those found in target app response map");
    }

    private static Stream<Arguments> dataForGetBulkFeedback() {
        // SDT bulk reference, expected number of individual requests, expected number of target app responses
        return Stream.of(
            Arguments.of("MCOL-20240627140000-000000001", 6, 0),
            Arguments.of("MCOL-20240627140000-000000002", 3, 2)
        );
    }

    private IBulkFeedbackRequest createBulkFeedbackRequest(String sdtBulkReference) {
        IBulkCustomer bulkCustomer = new BulkCustomer();
        bulkCustomer.setSdtCustomerId(10000001L);

        IBulkFeedbackRequest bulkFeedbackRequest = new BulkFeedbackRequest();
        bulkFeedbackRequest.setBulkCustomer(bulkCustomer);
        bulkFeedbackRequest.setSdtBulkReference(sdtBulkReference);

        return bulkFeedbackRequest;
    }
}
