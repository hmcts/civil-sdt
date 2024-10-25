package uk.gov.moj.sdt.cmc.consumers.decoder;

import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.moj.sdt.cmc.consumers.exception.CMCCaseLockedException;
import uk.gov.moj.sdt.cmc.consumers.exception.CMCRejectedException;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.cmc.exception.CMCException;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class CMCErrorDecoderTest extends AbstractSdtUnitTestBase {

    private static final String METHOD_KEY = "testMethodKey";
    private static final String REQUEST_URL = "testUrl";

    private static final int STATUS_BAD_REQUEST = 400;
    private static final int STATUS_NOT_FOUND = 404;
    private static final int STATUS_CASE_LOCKED = 423;
    private static final int STATUS_INTERNAL_SERVER_ERROR = 500;

    private static final String ERROR_UNEXPECTED_MESSAGE = "Exception has unexpected message";
    private static final String ERROR_UNEXPECTED_EXCEPTION = "Error decoder returned an unexpected exception";

    private CMCErrorDecoder cmcErrorDecoder;

    @Override
    protected void setUpLocalTests() {
        cmcErrorDecoder = new CMCErrorDecoder();
    }

    @Test
    void testDecodeRejectedException() {
        String responseBody = "{\"errorCode\":72,\"errorText\":\"Paid in full date missing\"}";
        Response response = createResponseWithBody(STATUS_BAD_REQUEST, responseBody);

        Exception exception = cmcErrorDecoder.decode(METHOD_KEY, response);

        assertInstanceOf(CMCRejectedException.class, exception, ERROR_UNEXPECTED_EXCEPTION);

        CMCRejectedException cmcRejectedException = (CMCRejectedException) exception;
        assertEquals("72", cmcRejectedException.getErrorCode(), "Exception has unexpected error code");
        assertEquals("Paid in full date missing",
                     cmcRejectedException.getErrorText(),
                     "Exception has unexpected error text");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Bad Request", "{\"error\":\"Bad Request\"}"})
    void testDecodeRejectedExceptionNonErrorResponseBody(String responseBody) {
        Response response = createResponseWithBody(STATUS_BAD_REQUEST, responseBody);

        Exception exception = cmcErrorDecoder.decode(METHOD_KEY, response);

        assertInstanceOf(CMCException.class, exception, ERROR_UNEXPECTED_EXCEPTION);

        String expectedMessage = "status: 400, body: " + responseBody;
        assertEquals(expectedMessage, exception.getMessage(), ERROR_UNEXPECTED_MESSAGE);
    }

    @Test
    void testDecodeCaseLockedException() {
        Response response = createResponse(STATUS_CASE_LOCKED);

        Exception exception = cmcErrorDecoder.decode(METHOD_KEY, response);

        assertInstanceOf(CMCCaseLockedException.class, exception, ERROR_UNEXPECTED_EXCEPTION);
    }

    @Test
    void testDecodeUnexpectedException() {
        String responseBody = "{\"status\" : 404, \"error\" : \"Not Found\"}";
        Response response = createResponseWithBody(STATUS_NOT_FOUND, responseBody);

        Exception exception = cmcErrorDecoder.decode(METHOD_KEY, response);

        assertInstanceOf(CMCException.class, exception, ERROR_UNEXPECTED_EXCEPTION);

        String expectedMessage = "status: 404, body: " + responseBody;
        assertEquals(expectedMessage, exception.getMessage(), ERROR_UNEXPECTED_MESSAGE);
    }

    @Test
    void testDecodeUnexpectedExceptionNoBody() {
        Response response = createResponse(STATUS_INTERNAL_SERVER_ERROR);

        Exception exception = cmcErrorDecoder.decode(METHOD_KEY, response);

        assertInstanceOf(CMCException.class, exception, ERROR_UNEXPECTED_EXCEPTION);
        assertEquals("status: 500", exception.getMessage(), ERROR_UNEXPECTED_MESSAGE);
    }

    private Response createResponse(int status) {
        return createResponseBuilder(status).build();
    }

    private Response createResponseWithBody(int status, String responseBody) {
        return createResponseBuilder(status).body(responseBody.getBytes(StandardCharsets.UTF_8)).build();
    }

    private Response.Builder createResponseBuilder(int status) {
        Response.Builder builder = Response.builder();
        builder.status(status).request(createRequest());

        return builder;
    }

    /**
     * Create a dummy feign Request.  This is needed to be able to create a feign Response.
     * @return A dummy feign Request
     */
    private Request createRequest() {
        Map<String, Collection<String>> headers = new HashMap<>();
        return Request.create(Request.HttpMethod.POST, REQUEST_URL, headers, null, null, null);
    }
}
