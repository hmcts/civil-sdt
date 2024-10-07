package uk.gov.moj.sdt.services;

import org.junit.jupiter.api.Test;
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
import uk.gov.moj.sdt.dao.api.IIndividualRequestDao;
import uk.gov.moj.sdt.dao.config.DaoTestConfig;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.IndividualRequest;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.services.config.ConnectionFactoryTestConfig;
import uk.gov.moj.sdt.services.config.ServicesTestConfig;
import uk.gov.moj.sdt.test.utils.AbstractIntegrationTest;
import uk.gov.moj.sdt.test.utils.TestConfig;
import uk.gov.moj.sdt.utils.SdtContext;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ActiveProfiles("integ")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestConfig.class, ServicesTestConfig.class, DaoTestConfig.class, ConnectionFactoryTestConfig.class})
@Sql(scripts = {"classpath:uk/gov/moj/sdt/services/sql/RefData.sql",
    "classpath:uk/gov/moj/sdt/services/sql/UpdateRequestServiceIntTest.sql"})
@Transactional
class UpdateRequestServiceIntTest extends AbstractIntegrationTest {

    private static final String ERR_MSG_IND_REQ_STATUS =
        "Individual Request %s has unexpected status";
    private static final String ERR_MSG_IND_REQ_FORWARDING =
        "Individual Request %s has unexpected number of forwarding attempts";
    private static final String ERR_MSG_IND_REQ_SHOULD_HAVE_UPDATED_DATE =
        "Individual Request %s should have an updated date";
    private static final String ERR_MSG_IND_REQ_SHOULD_HAVE_COMPLETED_DATE =
        "Individual Request %s should have a completed date";
    private static final String ERR_MSG_IND_REQ_SHOULD_NOT_HAVE_COMPLETED_DATE =
        "Individual Request %s should not have a completed date";

    private static final String ERR_MSG_IND_REQ_SHOULD_HAVE_ERR_LOG =
        "Individual Request %s should have an Error Log";
    private static final String ERR_MSG_IND_REQ_SHOULD_NOT_HAVE_ERR_LOG =
        "Individual Request %s should not have an Error Log";
    private static final String ERR_MSG_IND_REQ_ERR_LOG_CODE =
        "Individual Request %s Error Log has unexpected code";
    private static final String ERR_MSG_IND_REQ_ERR_LOG_TEXT =
        "Individual Request %s Error Log has unexpected text";

    private static final String ERR_MSG_BULK_SUB_STATUS =
        "Bulk Submission %s has unexpected status";
    private static final String ERR_MSG_BULK_SUB_SHOULD_HAVE_UPDATED_DATE =
        "Bulk Submission %s should have an updated date";
    private static final String ERR_MSG_BULK_SUB_SHOULD_NOT_HAVE_UPDATED_DATE =
        "Bulk Submission %s should not have an updated date";
    private static final String ERR_MSG_BULK_SUB_SHOULD_HAVE_COMPLETED_DATE =
        "Bulk Submission %s should have a completed date";
    private static final String ERR_MSG_BULK_SUB_SHOULD_NOT_HAVE_COMPLETED_DATE =
        "Bulk Submission %s should not have a completed date";

    private static final String ERR_CODE_MAX_LINES_EXCEEDED = "17";
    private static final String ERR_TEXT_MAX_LINES_EXCEEDED =
        "Maximum number of lines for the claim particulars exceeded.";
    private static final String ERR_CODE_COSTS_TOO_HIGH = "21";
    private static final String ERR_TEXT_COSTS_TOO_HIGH = "Costs too high for amount claimed.";

    private static final String TARGET_APP_RESPONSE = "response";

    private final IIndividualRequestDao individualRequestDao;

    private final UpdateRequestService updateRequestService;

    @Autowired
    public UpdateRequestServiceIntTest(IIndividualRequestDao individualRequestDao,
                                       UpdateRequestService updateRequestService) {
        this.individualRequestDao = individualRequestDao;
        this.updateRequestService = updateRequestService;
    }

    /**
     * Test update of individual requests with accepted or rejected status.
     * Associated bulk submission should be set as completed.
     * @param request The individual request to be updated
     */
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("individualRequestsForCompleted")
    void updateIndividualRequestBulkSubmissionCompleted(IIndividualRequest request) {
        SdtContext.getContext().setRawInXml(TARGET_APP_RESPONSE);

        updateRequestService.updateIndividualRequest(request);

        String requestRef = request.getSdtRequestReference();
        IIndividualRequest updatedRequest = individualRequestDao.getRequestBySdtReference(requestRef);
        assertIndividualRequest(requestRef, request, updatedRequest);

        IBulkSubmission bulkSubmission = updatedRequest.getBulkSubmission();
        String bulkRef = bulkSubmission.getSdtBulkReference();

        assertNotNull(bulkSubmission, "Could not find Bulk Submission for Individual Request " + requestRef);
        assertEquals(IBulkSubmission.BulkRequestStatus.COMPLETED.getStatus(),
                     bulkSubmission.getSubmissionStatus(),
                     String.format(ERR_MSG_BULK_SUB_STATUS, bulkRef));
        assertNotNull(bulkSubmission.getUpdatedDate(),
                      String.format(ERR_MSG_BULK_SUB_SHOULD_HAVE_UPDATED_DATE, bulkRef));
        assertNotNull(bulkSubmission.getCompletedDate(),
                      String.format(ERR_MSG_BULK_SUB_SHOULD_HAVE_COMPLETED_DATE, bulkRef));
    }

    /**
     * Test update of individual requests with accepted or rejected status.
     * Associated bulk submission should not be set as completed.
     * @param request The individual request to be updated
     */
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("individualRequestsForNotCompleted")
    void updateIndividualRequestBulkSubmissionNotCompleted(IIndividualRequest request) {
        SdtContext.getContext().setRawInXml(TARGET_APP_RESPONSE);

        updateRequestService.updateIndividualRequest(request);

        String requestRef = request.getSdtRequestReference();
        IIndividualRequest updatedRequest = individualRequestDao.getRequestBySdtReference(requestRef);
        assertIndividualRequest(requestRef, request, updatedRequest);

        IBulkSubmission bulkSubmission = updatedRequest.getBulkSubmission();
        assertBulkSubmissionNotCompleted(requestRef, bulkSubmission);
    }

    /**
     * Test update of individual request with resubmit status.
     * Associated bulk submission should not be set as completed.
     */
    @Test
    void updateIndividualRequestResubmit() {
        IIndividualRequest request =
            createIndividualRequest("MCOL-20240528120000-000000007-0000001",
                                    IIndividualRequest.IndividualRequestStatus.RESUBMIT_MESSAGE.getStatus());

        SdtContext.getContext().setRawInXml(TARGET_APP_RESPONSE);

        updateRequestService.updateIndividualRequest(request);

        String requestRef = request.getSdtRequestReference();
        IIndividualRequest updatedRequest = individualRequestDao.getRequestBySdtReference(requestRef);

        assertNotNull(updatedRequest, "Could not find Individual Request " + requestRef);
        assertEquals(IIndividualRequest.IndividualRequestStatus.RECEIVED.getStatus(),
                     updatedRequest.getRequestStatus(),
                     String.format(ERR_MSG_IND_REQ_STATUS, requestRef));
        assertEquals(0,
                     updatedRequest.getForwardingAttempts(),
                     String.format(ERR_MSG_IND_REQ_FORWARDING, requestRef));
        assertNotNull(updatedRequest.getUpdatedDate(),
                      String.format(ERR_MSG_IND_REQ_SHOULD_HAVE_UPDATED_DATE, requestRef));
        assertNull(updatedRequest.getCompletedDate(),
                   String.format(ERR_MSG_IND_REQ_SHOULD_NOT_HAVE_COMPLETED_DATE, requestRef));

        IBulkSubmission bulkSubmission = updatedRequest.getBulkSubmission();
        assertBulkSubmissionNotCompleted(requestRef, bulkSubmission);
    }

    private void assertIndividualRequest(String requestRef,
                                         IIndividualRequest request,
                                         IIndividualRequest updatedRequest) {
        assertNotNull(updatedRequest, "Could not find Individual Request " + requestRef);
        assertEquals(request.getRequestStatus(),
                     updatedRequest.getRequestStatus(),
                     String.format(ERR_MSG_IND_REQ_STATUS, requestRef));

        assertEquals(0,
                     updatedRequest.getForwardingAttempts(),
                     String.format(ERR_MSG_IND_REQ_FORWARDING, requestRef));
        assertNotNull(updatedRequest.getUpdatedDate(),
                      String.format(ERR_MSG_IND_REQ_SHOULD_HAVE_UPDATED_DATE, requestRef));
        assertNotNull(updatedRequest.getCompletedDate(),
                      String.format(ERR_MSG_IND_REQ_SHOULD_HAVE_COMPLETED_DATE, requestRef));

        IErrorLog requestErrorLog = updatedRequest.getErrorLog();
        IErrorLog updatedRequestErrorLog = updatedRequest.getErrorLog();

        if (requestErrorLog == null) {
            assertNull(updatedRequestErrorLog,
                       String.format(ERR_MSG_IND_REQ_SHOULD_NOT_HAVE_ERR_LOG, requestRef));
        } else {
            assertNotNull(updatedRequestErrorLog,
                          String.format(ERR_MSG_IND_REQ_SHOULD_HAVE_ERR_LOG, requestRef));
            assertEquals(requestErrorLog.getErrorCode(),
                         updatedRequestErrorLog.getErrorCode(),
                         String.format(ERR_MSG_IND_REQ_ERR_LOG_CODE, requestRef));
            assertEquals(requestErrorLog.getErrorText(),
                         updatedRequestErrorLog.getErrorText(),
                         String.format(ERR_MSG_IND_REQ_ERR_LOG_TEXT, requestRef));
        }
    }

    private void assertBulkSubmissionNotCompleted(String requestRef, IBulkSubmission bulkSubmission) {
        String bulkRef = bulkSubmission.getSdtBulkReference();

        assertNotNull(bulkSubmission, "Could not find Bulk Submission for Individual Request " + requestRef);
        assertNotEquals(IBulkSubmission.BulkRequestStatus.COMPLETED.getStatus(),
                        bulkSubmission.getSubmissionStatus(),
                        String.format(ERR_MSG_BULK_SUB_STATUS, bulkRef));
        assertNull(bulkSubmission.getUpdatedDate(),
                   String.format(ERR_MSG_BULK_SUB_SHOULD_NOT_HAVE_UPDATED_DATE, bulkRef));
        assertNull(bulkSubmission.getCompletedDate(),
                   String.format(ERR_MSG_BULK_SUB_SHOULD_NOT_HAVE_COMPLETED_DATE, bulkRef));
    }

    private static Stream<Arguments> individualRequestsForCompleted() {
        return Stream.of(
            arguments(named(
                "Requests all accepted",
                createIndividualRequest(
                    "MCOL-20240528120000-000000001-0000001",
                    IIndividualRequest.IndividualRequestStatus.ACCEPTED.getStatus()
                )
            )),
            arguments(named(
                "Requests all rejected",
                createIndividualRequest(
                    "MCOL-20240528120000-000000002-0000001",
                    IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus(),
                    ERR_CODE_MAX_LINES_EXCEEDED,
                    ERR_TEXT_MAX_LINES_EXCEEDED
                )
            )),
            arguments(named(
                "Requests all accepted or rejected",
                createIndividualRequest(
                    "MCOL-20240528120000-000000003-0000001",
                    IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus(),
                    ERR_CODE_COSTS_TOO_HIGH,
                    ERR_TEXT_COSTS_TOO_HIGH
                )
            ))
        );
    }

    private static Stream<Arguments> individualRequestsForNotCompleted() {
        return Stream.of(
            arguments(named(
                "Requests accepted and forwarded",
                createIndividualRequest(
                    "MCOL-20240528120000-000000004-0000001",
                    IIndividualRequest.IndividualRequestStatus.ACCEPTED.getStatus()
                )
            )),
            arguments(named(
                "Requests rejected and forwarded",
                createIndividualRequest(
                    "MCOL-20240528120000-000000005-0000001",
                    IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus(),
                    ERR_CODE_MAX_LINES_EXCEEDED,
                    ERR_TEXT_MAX_LINES_EXCEEDED
                )
            )),
            arguments(named(
                "Requests accepted, rejected and forwarded",
                createIndividualRequest(
                    "MCOL-20240528120000-000000006-0000001",
                    IIndividualRequest.IndividualRequestStatus.REJECTED.getStatus(),
                    ERR_CODE_COSTS_TOO_HIGH,
                    ERR_TEXT_COSTS_TOO_HIGH
                )
            ))
        );
    }

    private static IIndividualRequest createIndividualRequest(String requestRef, String status) {
        IndividualRequest request = new IndividualRequest();
        request.setSdtRequestReference(requestRef);
        request.setRequestStatus(status);

        return request;
    }

    private static IIndividualRequest createIndividualRequest(String requestRef,
                                                              String status,
                                                              String errorCode,
                                                              String errorText) {
        ErrorLog errorLog = new ErrorLog();
        errorLog.setErrorCode(errorCode);
        errorLog.setErrorText(errorText);

        IIndividualRequest request = createIndividualRequest(requestRef, status);
        request.setErrorLog(errorLog);

        return request;
    }
}
